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

import net.robotmedia.billing.request.ResponseCode;
import android.app.PendingIntent;

public interface IBillingObserver {

	/**
	 * Called after checking if in-app billing is supported or not.
	 * 
	 * @param supported
	 *            if true, in-app billing is supported. Otherwise, it isn't.
	 * @see BillingController#checkBillingSupported(android.content.Context)
	 */
	public void onBillingChecked(boolean supported);

	/**
	 * Called after the purchase of the specified item was cancelled.
	 * 
	 * @param itemId
	 *            id of the item whose purchase was cancelled.
	 * @see BillingController#startPurchaseIntent(android.app.Activity,
	 *      PendingIntent, android.content.Intent)
	 */
	public void onPurchaseCancelled(String itemId);

	/**
	 * Called after the purchase of the specified item was executed.
	 * 
	 * @param itemId
	 *            id of the item whose purchase was executed.
	 * @see BillingController#startPurchaseIntent(android.app.Activity,
	 *      PendingIntent, android.content.Intent)
	 */
	public void onPurchaseExecuted(String itemId);

	/**
	 * Called after requesting the purchase of the specified item.
	 * 
	 * @param itemId
	 *            id of the item whose purchase was requested.
	 * @param purchaseIntent
	 *            a purchase pending intent for the specified item.
	 * @see BillingController#requestPurchase(android.content.Context, String,
	 *      boolean)
	 */
	public void onPurchaseIntent(String itemId, PendingIntent purchaseIntent);

	/**
	 * Called after the purchase of the specified item was refunded.
	 * 
	 * @param itemId
	 *            id of the item whose purchase was refunded.
	 * @see BillingController#startPurchaseIntent(android.app.Activity,
	 *      PendingIntent, android.content.Intent)
	 */
	public void onPurchaseRefunded(String itemId);

	/**
	 * Called with the response for the purchase request of the specified item.
	 * This is used for reporting various errors, or if the user backed out and
	 * didn't purchase the item.
	 * 
	 * @param itemId
	 *            id of the item whose purchase was requested
	 * @param response
	 *            response of the purchase request
	 */
	public void onRequestPurchaseResponse(String itemId, ResponseCode response);

	/**
	 * Called after a restore transactions request has been successfully
	 * received by the server.
	 */
	public void onTransactionsRestored();

}
