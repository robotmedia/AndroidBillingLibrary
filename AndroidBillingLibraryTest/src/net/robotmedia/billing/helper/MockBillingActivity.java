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

package net.robotmedia.billing.helper;

import net.robotmedia.billing.BillingRequest.ResponseCode;
import net.robotmedia.billing.model.Transaction.PurchaseState;

public class MockBillingActivity extends AbstractBillingActivity {

	@Override
	public void onBillingChecked(boolean supported) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onSubscriptionChecked(boolean supported) {
		// TODO Auto-generated method stub		
	}

	public byte[] getObfuscationSalt() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPublicKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onRequestPurchaseResponse(String itemId, ResponseCode response) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPurchaseStateChanged(String itemId, PurchaseState state, String orderId) {
		// TODO Auto-generated method stub
	}

}
