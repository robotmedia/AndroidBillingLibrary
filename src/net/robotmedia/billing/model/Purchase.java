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

package net.robotmedia.billing.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Purchase {

    private static final String DEVELOPER_PAYLOAD = "developerPayload";
	private static final String NOTIFICATION_ID = "notificationId";
	private static final String ORDER_ID = "orderId";
	private static final String PURCHASE_TIME = "purchaseTime";
	private static final String PACKAGE_NAME = "packageName";
	private static final String PRODUCT_ID = "productId";
	private static final String PURCHASE_STATE = "purchaseState";

	public enum PurchaseState {
        // Responses to requestPurchase or restoreTransactions.
        PURCHASED,   // User was charged for the order.
        CANCELLED,    // The charge failed on the server.
        REFUNDED;    // User received a refund for the order.

        // Converts from an ordinal value to the PurchaseState
        public static PurchaseState valueOf(int index) {
            PurchaseState[] values = PurchaseState.values();
            if (index < 0 || index >= values.length) {
                return CANCELLED;
            }
            return values[index];
        }
    }
	
    public PurchaseState purchaseState;
    public String notificationId;
    public String productId;
    public String packageName;
    public String orderId;
    public long purchaseTime;
    public String developerPayload;
    
    public static Purchase parse(JSONObject json) throws JSONException {
    	final Purchase purchase = new Purchase();
        final int response = json.getInt(PURCHASE_STATE);
        purchase.purchaseState = PurchaseState.valueOf(response);
        purchase.productId = json.getString(PRODUCT_ID);
        purchase.packageName = json.getString(PACKAGE_NAME);
        purchase.purchaseTime = json.getLong(PURCHASE_TIME);
        purchase.orderId = json.optString(ORDER_ID, "");
        purchase.notificationId = json.optString(NOTIFICATION_ID, null);
        purchase.developerPayload = json.optString(DEVELOPER_PAYLOAD, null);
        return purchase;
    }
    
}
