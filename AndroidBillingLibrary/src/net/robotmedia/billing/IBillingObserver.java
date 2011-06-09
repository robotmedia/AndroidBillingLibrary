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

import android.app.PendingIntent;

public interface IBillingObserver {

	/**
	 * Called after checking if in-app billing is supported or not.
	 * @param supported if true, in-app billing is supported. Otherwise, it isn't.
	 * @see BillingController#checkBillingSupported(android.content.Context)
	 */
	public void onBillingChecked(boolean supported);
	
	/**
	 * Called after requesting the purchase of the specified item.
	 * @param itemId id of the item whose purchase was requested.
	 * @param purchaseIntent a purchase pending intent for the specified item.
	 * @see BillingController#requestPurchase(android.content.Context, String, boolean)
	 */
	public void onPurchaseIntent(String itemId, PendingIntent purchaseIntent);
	
	/**
	 * Called after the purchase of the specified item was cancelled.
	 * @param itemId id of the item whose purchase was cancelled.
	 * @see BillingController#startPurchaseIntent(android.app.Activity, PendingIntent, android.content.Intent)
	 */
	public void onPurchaseCancelled(String itemId);

	/**
	 * Called after the purchase of the specified item was executed.
	 * @param itemId id of the item whose purchase was executed.
	 * @see BillingController#startPurchaseIntent(android.app.Activity, PendingIntent, android.content.Intent)
	 */
	public void onPurchaseExecuted(String itemId);

	/**
	 * Called after the purchase of the specified item was refunded.
	 * @param itemId id of the item whose purchase was refunded.
	 * @see BillingController#startPurchaseIntent(android.app.Activity, PendingIntent, android.content.Intent)
	 */
	public void onPurchaseRefunded(String itemId);

}
