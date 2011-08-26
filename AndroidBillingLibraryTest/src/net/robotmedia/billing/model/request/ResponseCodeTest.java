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

package net.robotmedia.billing.model.request;

import net.robotmedia.billing.BillingRequest.ResponseCode;
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
