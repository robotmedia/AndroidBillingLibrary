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

    public enum PurchaseState {
        CANCELLED,   // User was charged for the order.
        // Responses to requestPurchase or restoreTransactions.
        PURCHASED,    // The charge failed on the server.
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
	static final String DEVELOPER_PAYLOAD = "developerPayload";
	static final String NOTIFICATION_ID = "notificationId";
	static final String ORDER_ID = "orderId";
	static final String PACKAGE_NAME = "packageName";
	static final String PRODUCT_ID = "productId";
	static final String PURCHASE_STATE = "purchaseState";

	static final String PURCHASE_TIME = "purchaseTime";
	
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

	public String developerPayload;
    public String notificationId;
    public String orderId;
    public String packageName;
    public String productId;
    public PurchaseState purchaseState;
    public long purchaseTime;
    
    public Purchase() {}
    
    public Purchase(String orderId, String productId, String packageName, PurchaseState purchaseState,
			String notificationId, long purchaseTime, String developerPayload) {
		this.orderId = orderId;
		this.productId = productId;
		this.packageName = packageName;
		this.purchaseState = purchaseState;
		this.notificationId = notificationId;
		this.purchaseTime = purchaseTime;
		this.developerPayload = developerPayload;
	}
    
}
