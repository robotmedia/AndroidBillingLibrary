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

package net.robotmedia.billing.request;

import net.robotmedia.billing.BillingController;
import android.app.PendingIntent;
import android.os.Bundle;

public class RequestPurchase extends BillingRequest {

	private String itemId;
	private String developerPayload;
	
	private static final String KEY_ITEM_ID = "ITEM_ID";
	private static final String KEY_DEVELOPER_PAYLOAD = "DEVELOPER_PAYLOAD";
	private static final String KEY_PURCHASE_INTENT = "PURCHASE_INTENT";
	
	public RequestPurchase(String packageName, String itemId, String developerPayload) {
		super(packageName);
		this.itemId = itemId;
		this.developerPayload = developerPayload;
	}

	@Override
	public String getRequestType() {
		return "REQUEST_PURCHASE";
	}

	@Override
	protected void addParams(Bundle request) {
		request.putString(KEY_ITEM_ID, itemId);
		if (developerPayload != null) {
			request.putString(KEY_DEVELOPER_PAYLOAD, developerPayload);
		}
	}
	
	@Override
	protected void processOkResponse(Bundle response) {
		final PendingIntent purchaseIntent = response.getParcelable(KEY_PURCHASE_INTENT);
		BillingController.onPurchaseIntent(itemId, purchaseIntent);
	}
	
}
