package net.robotmedia.billing;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

public class AbstractBillingActivityTest extends ActivityInstrumentationTestCase2<MockBillingActivity> {

	private static final String PURCHASE_ID = "android.example.purchased";

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

	// TODO: Find a way to test the following without hanging the test suite
	// @SmallTest
	// public void testOnPurchaseIntent() throws Exception {
	// final PendingIntent purchaseIntent = PendingIntent.getActivity(mActivity,
	// 0, new Intent(), 0);
	// mActivity.onPurchaseIntent(PURCHASE_ID, purchaseIntent);
	// }

	@MediumTest
	public void testRequestPurchase() throws Exception {
		mActivity.requestPurchase(PURCHASE_ID);
	}

	@MediumTest
	public void testRestoreTransactions() throws Exception {
		mActivity.restoreTransactions();
	}

}
