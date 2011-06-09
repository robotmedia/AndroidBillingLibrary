package net.robotmedia.billing;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class BillingServiceTest extends ServiceTestCase<BillingService> {

	private final static long NONCE = 147;
	private final static String[] NOTIFY_IDS = new String[] { "test" };
	private final static String ITEM_ID = "android.test.purchased";
	
	public BillingServiceTest() {
		super(BillingService.class);
	}
	
	@SmallTest
	public void testStart() throws Exception {
		final Intent intent = new Intent();
		intent.setClass(getContext(), BillingService.class);
		startService(intent);
	}
	
	@SmallTest
	public void testCheckBillingSupported() throws Exception {
		BillingService.checkBillingSupported(getContext());
	}

	@SmallTest
	public void testConfirmNotifications() throws Exception {
		BillingService.confirmNotifications(getContext(), NOTIFY_IDS);
	}

	@SmallTest
	public void testGetPurchaseInformation() throws Exception {
		BillingService.getPurchaseInformation(getContext(), NOTIFY_IDS, NONCE);
	}	

	@SmallTest
	public void testRequestPurchase() throws Exception {
		BillingService.requestPurchase(getContext(), ITEM_ID, null);
	}	

	@SmallTest
	public void testRestoreTransactions() throws Exception {
		BillingService.restoreTransations(getContext(), NONCE);
	}	

	@SmallTest
    public void testBind() {
        final Intent intent = new Intent();
        intent.setClass(getContext(), BillingService.class);
        final IBinder service = bindService(intent); 
        assertNull(service);
    }
}