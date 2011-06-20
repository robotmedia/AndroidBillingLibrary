package net.robotmedia.billing.model.request;

import net.robotmedia.billing.request.ResponseCode;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class ResponseCodeTest extends AndroidTestCase {

	@SmallTest
	public void testOrdinal() throws Exception {
		assertEquals(ResponseCode.RESULT_OK.ordinal(), 0);
		assertEquals(ResponseCode.RESULT_USER_CANCELED.ordinal(), 1);
		assertEquals(ResponseCode.RESULT_SERVICE_UNAVAILABLE.ordinal(), 2);
		assertEquals(ResponseCode.RESULT_BILLING_UNAVAILABLE.ordinal(), 3);
		assertEquals(ResponseCode.RESULT_ITEM_UNAVAILABLE.ordinal(), 4);
		assertEquals(ResponseCode.RESULT_DEVELOPER_ERROR.ordinal(), 5);
		assertEquals(ResponseCode.RESULT_ERROR.ordinal(), 6);
	}
	
	@SmallTest
	public void testValueOf() throws Exception {
		assertEquals(ResponseCode.RESULT_OK, ResponseCode.valueOf(0));
		assertEquals(ResponseCode.RESULT_USER_CANCELED, ResponseCode.valueOf(1));
		assertEquals(ResponseCode.RESULT_SERVICE_UNAVAILABLE, ResponseCode.valueOf(2));
		assertEquals(ResponseCode.RESULT_BILLING_UNAVAILABLE, ResponseCode.valueOf(3));
		assertEquals(ResponseCode.RESULT_ITEM_UNAVAILABLE, ResponseCode.valueOf(4));
		assertEquals(ResponseCode.RESULT_DEVELOPER_ERROR, ResponseCode.valueOf(5));
		assertEquals(ResponseCode.RESULT_ERROR, ResponseCode.valueOf(6));
	}
	
	@SmallTest
	public void testIsResponseOk() throws Exception {
		assertTrue(ResponseCode.isResponseOk(0));
		assertFalse(ResponseCode.isResponseOk(6));
	}
	
}
