/*   Copyright 2011 Robot Media SL (http://www.robotmedia.net)
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

package net.robotmedia.billing;

import net.robotmedia.billing.BillingController.BillingStatus;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public abstract class AbstractBillingActivity extends Activity implements IBillingObserver, BillingController.IConfiguration {

	private static final String KEY_TRANSACTIONS_RESTORED = "net.robotmedia.billing.AbstractBillingActivity.transactionsRestored";

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

	private boolean isTransactionsRestored() {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		return preferences.getBoolean(KEY_TRANSACTIONS_RESTORED, false);
	}
	
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BillingController.registerObserver(this); // This activity will handle
													// billing notifications
		BillingController.setConfiguration(this); // This activity will provide
													// the public key and salt
		this.checkBillingSupported();
		if (!isTransactionsRestored()) {
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

	@Override
	public void onTransactionsRestored() {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		final Editor editor = preferences.edit();
		editor.putBoolean(KEY_TRANSACTIONS_RESTORED, true);
		editor.commit();
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
