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

import net.robotmedia.billing.BillingController.BillingStatus;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public abstract class AbstractBillingActivity extends Activity implements IBillingObserver, BillingController.IConfiguration {

	private static final String PREFERENCE_SUBSEQUENT_SESSION = "net.robotmedia.billing.AbstractBillingActivity.subsequentSession";;

	/**
	 * Returns the billing status. If it's currently unknown, requests to check
	 * if billing is supported and
	 * {@link AbstractBillingActivity#onBillingChecked(boolean)} should be
	 * called later with the result.
	 * 
	 * @return the current billing status (unknown, supported or unsupported).
	 * @see AbstractBillingActivity#onBillingChecked(boolean)
	 */
	public BillingStatus checkBillingSupported() {
		return BillingController.checkBillingSupported(this);
	}

	private boolean isSubsequentSession() {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		final boolean subsequentSession = preferences.getBoolean(PREFERENCE_SUBSEQUENT_SESSION, false);
		final Editor editor = preferences.edit();
		editor.putBoolean(PREFERENCE_SUBSEQUENT_SESSION, true);
		editor.commit();
		return subsequentSession;
	}

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BillingController.registerObserver(this); // This activity will handle
													// billing notifications
		BillingController.setConfiguration(this); // This activity will provide
													// the public key and salt
		if (!isSubsequentSession()) {
			BillingController.restoreTransactions(this);
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BillingController.unregisterObserver(this); // Avoid receiving
													// notifications after
													// destroy
		BillingController.setConfiguration(null);
	}

	/**
	 * Called after requesting the purchase of the specified item. The default
	 * implementation simply starts the pending intent.
	 * 
	 * @param itemId
	 *            id of the item whose purchase was requested.
	 * @param purchaseIntent
	 *            a purchase pending intent for the specified item.
	 */
	@Override
	public void onPurchaseIntent(String itemId, PendingIntent purchaseIntent) {
		BillingController.startPurchaseIntent(this, purchaseIntent, null);
	}

	/**
	 * Requests the purchase of the specified item. The transaction will not be
	 * confirmed automatically; such confirmation could be handled in
	 * {@link AbstractBillingActivity#onPurchaseExecuted(String)}. If automatic
	 * confirmation is preferred use
	 * {@link BillingController#requestPurchase(android.content.Context, String, boolean)}
	 * instead.
	 * 
	 * @param itemId
	 *            id of the item to be purchased.
	 */
	public void requestPurchase(String itemId) {
		BillingController.requestPurchase(this, itemId);
	}

	/**
	 * Requests to restore all transactions.
	 */
	public void restoreTransactions() {
		BillingController.restoreTransactions(this);
	}

}
