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

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

public class AbstractBillingActivityTest extends ActivityInstrumentationTestCase2<MockBillingActivity> {

//	private static final String PURCHASE_ID = "android.example.purchased";

	private MockBillingActivity mActivity;

	public AbstractBillingActivityTest() {
		super(MockBillingActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = this.getActivity();
	}

	@MediumTest
	public void testCheckBillingSupported() throws Exception {
		mActivity.checkBillingSupported();
	}
	
	@MediumTest
	public void testCheckSubscriptionSupported() throws Exception {
		mActivity.checkSubscriptionSupported();
	}

	// TODO: Find a way to test the following without hanging the test suite
	// @SmallTest
	// public void testOnPurchaseIntent() throws Exception {
	// final PendingIntent purchaseIntent = PendingIntent.getActivity(mActivity,
	// 0, new Intent(), 0);
	// mActivity.onPurchaseIntent(PURCHASE_ID, purchaseIntent);
	// }
//	@MediumTest
//	public void testRequestPurchase() throws Exception {
//		mActivity.requestPurchase(PURCHASE_ID);
//	}

	@MediumTest
	public void testRestoreTransactions() throws Exception {
		mActivity.restoreTransactions();
	}

}
