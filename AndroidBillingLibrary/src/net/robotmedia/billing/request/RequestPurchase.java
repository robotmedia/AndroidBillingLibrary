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
	
	@Override
	public void onResponseCode(ResponseCode response) {
		super.onResponseCode(response);
		BillingController.onRequestPurchaseResponse(itemId, response);
	}

	
}
