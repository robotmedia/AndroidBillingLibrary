/*	Copyright 2011 Robot Media SL <http://www.robotmedia.net>. All rights reserved.
 *
 *	This file is part of Android Billing Library.
 *
 *	Android Billing Library is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	Android Billing Library is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser Public License for more details.
 *
 *	You should have received a copy of the GNU Lesser Public License
 *	along with Android Billing Library.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.robotmedia.billing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.robotmedia.billing.model.Purchase;
import net.robotmedia.billing.model.PurchaseManager;
import net.robotmedia.billing.request.BillingRequest;
import net.robotmedia.billing.request.ResponseCode;
import net.robotmedia.billing.utils.Compatibility;
import net.robotmedia.billing.utils.Security;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class BillingController {

	/**
	 * Used to provide on-demand values to the billing controller.
	 */
	public interface IConfiguration {

		/**
		 * Returns a salt for the obfuscation of purchases in local memory.
		 * @return array of 20 random bytes.
		 */
		public byte[] getObfuscationSalt();

		/**
		 * Returns the public key used to verify the signature of responses of the Market Billing service.
		 * @return Base64 encoded public key.
		 */
		public String getPublicKey();
	}

	private static Set<String> automaticConfirmations = new HashSet<String>();
	private static IConfiguration configuration = null;
	private static boolean debug = false;

	private static final String JSON_NONCE = "nonce";
	private static final String JSON_ORDERS = "orders";
	private static HashMap<String, Set<String>> manualConfirmations = new HashMap<String, Set<String>>();

	private static Set<IBillingObserver> observers = new HashSet<IBillingObserver>();

	private static final String TAG = BillingController.class.getSimpleName();

	/**
	 * Adds the specified notification to the set of manual confirmations of the
	 * specified item.
	 * 
	 * @param itemId
	 *            id of the item.
	 * @param notificationId
	 *            id of the notification.
	 */
	private static final void addManualConfirmation(String itemId, String notificationId) {
		Set<String> notifications = manualConfirmations.get(itemId);
		if (notifications == null) {
			notifications = new HashSet<String>();
			manualConfirmations.put(itemId, notifications);
		}
		notifications.add(notificationId);
	}

	/**
	 * Requests to check if billing is supported. Observers should receive a
	 * {@link IBillingObserver#onBillingChecked(boolean)} notification.
	 * 
	 * @param context
	 * @see IBillingObserver#onBillingChecked(boolean)
	 */
	public static void checkBillingSupported(Context context) {
		BillingService.checkBillingSupported(context);
	}

	/**
	 * Requests to confirm all pending notifications for the specified item.
	 * 
	 * @param context
	 * @param itemId
	 *            id of the item whose purchase must be confirmed.
	 * @return true if pending notifications for this item were found, false
	 *         otherwise.
	 */
	public static boolean confirmNotifications(Context context, String itemId) {
		final Set<String> notifications = manualConfirmations.get(itemId);
		if (notifications != null) {
			confirmNotifications(context, notifications.toArray(new String[] {}));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Requests to confirm all specified notifications.
	 * 
	 * @param context
	 * @param notifyIds
	 *            array with the ids of all the notifications to confirm.
	 */
	private static void confirmNotifications(Context context, String[] notifyIds) {
		BillingService.confirmNotifications(context, notifyIds);
	}

	/**
	 * Returns the number of purchases for the specified item. Refunded
	 * purchases are not subtracted.
	 * 
	 * @param context
	 * @param itemId
	 *            id of the item whose purchases will be counted.
	 * @return number of purchases for the specified item.
	 */
	public static int countPurchases(Context context, String itemId) {
		final byte[] salt = getSalt();
		itemId = salt != null ? Security.obfuscate(context, salt, itemId) : itemId;
		return PurchaseManager.countPurchases(context, itemId);
	}

	/**
	 * Requests purchase information for the specified notification. Immediately
	 * followed by a call to
	 * {@link #onPurchaseInformationResponse(long, boolean)} and later to
	 * {@link #onPurchaseStateChanged(Context, String, String)}, if the request
	 * is successful.
	 * 
	 * @param context
	 * @param notifyId
	 *            id of the notification whose purchase information is
	 *            requested.
	 */
	private static void getPurchaseInformation(Context context, String notifyId) {
		final long nonce = Security.generateNonce();
		BillingService.getPurchaseInformation(context, new String[] { notifyId }, nonce);
	}

	/**
	 * Lists all purchases stored locally, including cancellations and refunds.
	 * 
	 * @param context
	 * @return list of purchases.
	 */
	public static List<Purchase> getPurchases(Context context) {
		List<Purchase> purchases = PurchaseManager.getPurchases(context);
		final byte[] salt = getSalt();
		if (salt != null) {
			for (Purchase p : purchases) {
				unobfuscate(context, p);
			}
		}
		return purchases;
	}

	/**
	 * Gets the salt from the configuration and logs a warning if it's null.
	 * 
	 * @return salt.
	 */
	private static byte[] getSalt() {
		byte[] salt = null;
		if (configuration == null || ((salt = configuration.getObfuscationSalt()) == null)) {
			Log.w(TAG, "Can't (un)obfuscate purchases without salt");
		}
		return salt;
	}

	/**
	 * Returns true if the specified item has been registered as purchased in
	 * local memory. Note that the item might have been purchased in another
	 * installation, but not yet registered in this one.
	 * 
	 * @param context
	 * @param itemId
	 *            item id.
	 * @return true if the specified item is purchased, false otherwise.
	 */
	public static boolean isPurchased(Context context, String itemId) {
		final byte[] salt = getSalt();
		itemId = salt != null ? Security.obfuscate(context, salt, itemId) : itemId;
		return PurchaseManager.isPurchased(context, itemId);
	}

	/**
	 * Notifies observers of the purchase state change of the specified item.
	 * 
	 * @param itemId
	 *            id of the item whose purchase state has changed.
	 * @param state
	 *            new purchase state of the item.
	 */
	private static void notifyPurchaseStateChange(String itemId, Purchase.PurchaseState state) {
		for (IBillingObserver o : observers) {
			switch (state) {
			case CANCELLED:
				o.onPurchaseCancelled(itemId);
				break;
			case PURCHASED:
				o.onPurchaseExecuted(itemId);
				break;
			case REFUNDED:
				o.onPurchaseRefunded(itemId);
				break;
			}
		}
	}

	/**
	 * Obfuscates the specified purchase. Only the order id, product id and
	 * developer payload are obfuscated.
	 * 
	 * @param context
	 * @param purchase
	 *            purchase to be obfuscated.
	 * @see #unobfuscate(Context, Purchase)
	 */
	private static void obfuscate(Context context, Purchase purchase) {
		final byte[] salt = getSalt();
		if (salt == null) {
			return;
		}
		purchase.orderId = Security.obfuscate(context, salt, purchase.orderId);
		purchase.productId = Security.obfuscate(context, salt, purchase.productId);
		purchase.developerPayload = Security.obfuscate(context, salt, purchase.developerPayload);
	}

	/**
	 * Called after the response to a
	 * {@link net.robotmedia.billing.request.CheckBillingSupported} request is
	 * received.
	 * 
	 * @param supported
	 */
	public static void onBillingChecked(boolean supported) {
		for (IBillingObserver o : observers) {
			o.onBillingChecked(supported);
		}
	}

	/**
	 * Called when an IN_APP_NOTIFY message is received.
	 * 
	 * @param context
	 * @param notifyId
	 *            notification id.
	 */
	protected static void onNotify(Context context, String notifyId) {
		getPurchaseInformation(context, notifyId);
	}

	/**
	 * Called after the response to a
	 * {@link net.robotmedia.billing.request.RequestPurchase} request is
	 * received.
	 * 
	 * @param itemId
	 *            id of the item whose purchase was requested.
	 * @param purchaseIntent
	 *            intent to purchase the item.
	 */
	public static void onPurchaseIntent(String itemId, PendingIntent purchaseIntent) {
		for (IBillingObserver o : observers) {
			o.onPurchaseIntent(itemId, purchaseIntent);
		}
	}

	/**
	 * Called after the response to a
	 * {@link net.robotmedia.billing.request.GetPurchaseInformation} request is
	 * received. Registers all transactions in local memory and confirms those
	 * who can be confirmed automatically.
	 * 
	 * @param context
	 * @param signedData
	 *            signed JSON data received from the Market Billing service.
	 * @param signature
	 *            data signature.
	 */
	protected static void onPurchaseStateChanged(Context context, String signedData, String signature) {
		if (TextUtils.isEmpty(signedData)) {
			Log.w(TAG, "Signed data is empty");
			return;
		}

		if (!debug) {
			if (TextUtils.isEmpty(signature)) {
				Log.w(TAG, "Empty signature requires debug mode");
				return;
			}
			final String publicKey;
			if (configuration == null || TextUtils.isEmpty(publicKey = configuration.getPublicKey())) {
				Log.w(TAG, "Please set the public key or turn on debug mode");
				return;
			}
			if (!Security.verify(signedData, signature, publicKey)) {
				Log.w(TAG, "Signature does not match data.");
				return;
			}
		}

		List<Purchase> purchases;
		try {
			JSONObject jObject = new JSONObject(signedData);
			if (!verifyNonce(jObject)) {
				Log.w(BillingController.class.getSimpleName(), "Invalid nonce");
				return;
			}
			purchases = parsePurchases(jObject);
		} catch (JSONException e) {
			Log.e(BillingController.class.getSimpleName(), "JSON exception: ", e);
			return;
		}

		ArrayList<String> confirmations = new ArrayList<String>();
		for (Purchase p : purchases) {
			if (p.notificationId != null && automaticConfirmations.contains(p.productId)) {
				confirmations.add(p.notificationId);
			} else {
				// TODO: Discriminate between purchases, cancellations and
				// refunds.
				addManualConfirmation(p.productId, p.notificationId);
			}

			// Save itemId and purchaseState before obfuscation for later use
			final String itemId = p.productId;
			final Purchase.PurchaseState purchaseState = p.purchaseState;
			obfuscate(context, p);
			PurchaseManager.addPurchase(context, p);

			notifyPurchaseStateChange(itemId, purchaseState);
		}
		if (!confirmations.isEmpty()) {
			final String[] notifyIds = confirmations.toArray(new String[confirmations.size()]);
			confirmNotifications(context, notifyIds);
		}
	}

	/**
	 * Called after a {@link net.robotmedia.billing.request.BillingRequest} is
	 * sent.
	 * 
	 * @param requestId
	 *            the id the request.
	 * @param request
	 *            the billing request.
	 */
	protected static void onRequestSent(long requestId, BillingRequest request) {
		if (debug) {
			Log.d(BillingController.class.getSimpleName(), "Request " + requestId + " of type " + request.getRequestType() + " sent");
		}
		if (!request.isSuccess() && request.hasNonce()) {
			Security.removeNonce(request.getNonce());
		}
	}

	/**
	 * Called after a {@link net.robotmedia.billing.request.BillingRequest} is
	 * sent. Mostly used for debugging purposes.
	 * 
	 * @param context
	 * @param requestId
	 *            the id of the request.
	 * @param responseCode
	 *            the response code.
	 * @see net.robotmedia.billing.request.ResponseCode
	 */
	protected static void onResponseCode(Context context, long requestId, int responseCode) {
		if (debug) {
			Log.d(BillingController.class.getSimpleName(), "Request " + requestId + " received response " + ResponseCode.valueOf(responseCode));
		}
	}

	/**
	 * Parse all purchases from the JSON data received from the Market Billing
	 * service.
	 * 
	 * @param data
	 *            JSON data received from the Market Billing service.
	 * @return list of purchases.
	 * @throws JSONException
	 *             if the data couldn't be properly parsed.
	 */
	private static List<Purchase> parsePurchases(JSONObject data) throws JSONException {
		ArrayList<Purchase> purchases = new ArrayList<Purchase>();
		JSONArray orders = data.optJSONArray(JSON_ORDERS);
		int numTransactions = 0;
		if (orders != null) {
			numTransactions = orders.length();
		}
		for (int i = 0; i < numTransactions; i++) {
			JSONObject jElement = orders.getJSONObject(i);
			Purchase p = Purchase.parse(jElement);
			purchases.add(p);
		}
		return purchases;
	}

	/**
	 * Registers the specified billing observer.
	 * 
	 * @param observer
	 *            the billing observer to add.
	 * @return true if the observer wasn't previously registered, false
	 *         otherwise.
	 * @see #unregisterObserver(IBillingObserver)
	 */
	public static boolean registerObserver(IBillingObserver observer) {
		return observers.add(observer);
	}

	/**
	 * Requests the purchase of the specified item. The transaction will not be
	 * confirmed automatically.
	 * 
	 * @param context
	 * @param itemId
	 *            id of the item to be purchased.
	 * @see #requestPurchase(Context, String, boolean)
	 */
	public static void requestPurchase(Context context, String itemId) {
		requestPurchase(context, itemId, false);
	}

	/**
	 * Requests the purchase of the specified item with optional automatic
	 * confirmation.
	 * 
	 * @param context
	 * @param itemId
	 *            id of the item to be purchased.
	 * @param confirm
	 *            if true, the transaction will be confirmed automatically. If
	 *            false, the transaction will have to be confirmed with a call
	 *            to {@link #confirmNotifications(Context, String)}.
	 * @see IBillingObserver#onPurchaseIntent(String, PendingIntent)
	 */
	public static void requestPurchase(Context context, String itemId, boolean confirm) {
		if (confirm) {
			automaticConfirmations.add(itemId);
		}
		BillingService.requestPurchase(context, itemId, null);
	}

	/**
	 * Requests to restore all transactions.
	 * 
	 * @param context
	 */
	public static void restoreTransactions(Context context) {
		final long nonce = Security.generateNonce();
		BillingService.restoreTransations(context, nonce);
	}

	/**
	 * Sets the configuration instance of the controller.
	 * 
	 * @param config
	 *            configuration instance.
	 */
	public static void setConfiguration(IConfiguration config) {
		configuration = config;
	}

	/**
	 * Sets debug mode.
	 * 
	 * @param value
	 */
	public static final void setDebug(boolean value) {
		debug = value;
	}

	/**
	 * Starts the specified purchase intent with the specified activity.
	 * 
	 * @param activity
	 * @param purchaseIntent
	 *            purchase intent.
	 * @param intent
	 */
	public static void startPurchaseIntent(Activity activity, PendingIntent purchaseIntent, Intent intent) {
		if (Compatibility.isStartIntentSenderSupported()) {
			// This is on Android 2.0 and beyond. The in-app buy page activity
			// must be on the activity stack of the application.
			Compatibility.startIntentSender(activity, purchaseIntent.getIntentSender(), intent);
		} else {
			// This is on Android version 1.6. The in-app buy page activity must
			// be on its own separate activity stack instead of on the activity
			// stack of the application.
			try {
				purchaseIntent.send(activity, 0 /* code */, intent);
			} catch (CanceledException e) {
				Log.e(TAG, "Error starting purchase intent", e);
			}
		}
	}

	/**
	 * Unobfuscated the specified purchase.
	 * 
	 * @param context
	 * @param purchase
	 *            purchase to unobfuscate.
	 * @see #obfuscate(Context, Purchase)
	 */
	private static void unobfuscate(Context context, Purchase purchase) {
		final byte[] salt = getSalt();
		if (salt == null) {
			return;
		}
		purchase.orderId = Security.unobfuscate(context, salt, purchase.orderId);
		purchase.productId = Security.unobfuscate(context, salt, purchase.productId);
		purchase.developerPayload = Security.unobfuscate(context, salt, purchase.developerPayload);
	}

	/**
	 * Unregisters the specified billing observer.
	 * 
	 * @param observer
	 *            the billing observer to unregister.
	 * @return true if the billing observer was unregistered, false otherwise.
	 * @see #registerObserver(IBillingObserver)
	 */
	public static boolean unregisterObserver(IBillingObserver observer) {
		return observers.remove(observer);
	}

	private static boolean verifyNonce(JSONObject data) {
		long nonce = data.optLong(JSON_NONCE);
		if (Security.isNonceKnown(nonce)) {
			Security.removeNonce(nonce);
			return true;
		} else {
			return false;
		}
	}

}
