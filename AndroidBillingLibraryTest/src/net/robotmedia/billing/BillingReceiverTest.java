package net.robotmedia.billing;

import net.robotmedia.billing.request.ResponseCode;
import android.content.Intent;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class BillingReceiverTest extends AndroidTestCase {

	private BillingReceiver mReceiver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mReceiver = new BillingReceiver();
	}

	@SmallTest
	public void testNotify() throws Exception {
		final Intent intent = new Intent(BillingReceiver.ACTION_NOTIFY);
		intent.putExtra(BillingReceiver.EXTRA_NOTIFICATION_ID, "notificationId");
		mReceiver.onReceive(getContext(), intent);
	}
	
	@SmallTest
	public void testResponseCode() throws Exception {
		final Intent intent = new Intent(BillingReceiver.ACTION_RESPONSE_CODE);
		intent.putExtra(BillingReceiver.EXTRA_REQUEST_ID, "requestId");
		intent.putExtra(BillingReceiver.EXTRA_RESPONSE_CODE, ResponseCode.RESULT_OK.ordinal());
		mReceiver.onReceive(getContext(), intent);
	}
	
	@SmallTest
	public void testPurchaseStateChanged() throws Exception {
		final Intent intent = new Intent(BillingReceiver.ACTION_PURCHASE_STATE_CHANGED);
		intent.putExtra(BillingReceiver.EXTRA_INAPP_SIGNED_DATA, "");
		intent.putExtra(BillingReceiver.EXTRA_INAPP_SIGNATURE, "");
		mReceiver.onReceive(getContext(), intent);
	}

}
