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

package net.robotmedia.billing.model;

import java.util.Date;

import org.json.JSONObject;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

public class TransactionTest extends TestCase {
	
	public static final Transaction TRANSACTION_1 = new Transaction("order1", "android.test.purchased", "com.example", Transaction.PurchaseState.PURCHASED, "notificationId", new Date().getTime(), "developerPayload");
	public static final Transaction TRANSACTION_2 = new Transaction("order2", "product_2", "com.example", Transaction.PurchaseState.PURCHASED, "notificationId", new Date().getTime(), "developerPayload");
	public static final Transaction TRANSACTION_2_REFUNDED = new Transaction("order4", "product_2", "com.example", Transaction.PurchaseState.REFUNDED, "notificationId", new Date().getTime(), "developerPayload");
	public static final Transaction TRANSACTION_1_REFUNDED = new Transaction("order3", "android.test.purchased", "com.example", Transaction.PurchaseState.REFUNDED, "notificationId", new Date().getTime(), "developerPayload");
	public static void assertEquals(Transaction a, Transaction b) {
		assertTrue(a.equals(b));
	}
	
	@SmallTest
	public void testParseAllFields() throws Exception {
		JSONObject json = new JSONObject();
		json.put(Transaction.ORDER_ID, TRANSACTION_1.orderId);
		json.put(Transaction.PRODUCT_ID, TRANSACTION_1.productId);
		json.put(Transaction.PACKAGE_NAME, TRANSACTION_1.packageName);
		json.put(Transaction.PURCHASE_STATE, TRANSACTION_1.purchaseState.ordinal());
		json.put(Transaction.NOTIFICATION_ID, TRANSACTION_1.notificationId);
		json.put(Transaction.PURCHASE_TIME, TRANSACTION_1.purchaseTime);
		json.put(Transaction.DEVELOPER_PAYLOAD, TRANSACTION_1.developerPayload);
		final Transaction parsed = Transaction.parse(json);
		assertEquals(TRANSACTION_1, parsed);
	}
	
	@SmallTest
	public void testParseOnlyMandatoryFields() throws Exception {
		JSONObject json = new JSONObject();
		json.put(Transaction.PRODUCT_ID, TRANSACTION_1.productId);
		json.put(Transaction.PACKAGE_NAME, TRANSACTION_1.packageName);
		json.put(Transaction.PURCHASE_STATE, TRANSACTION_1.purchaseState.ordinal());
		json.put(Transaction.PURCHASE_TIME, TRANSACTION_1.purchaseTime);
		final Transaction parsed = Transaction.parse(json);
		assertNull(parsed.orderId);
		assertEquals(TRANSACTION_1.productId, parsed.productId);
		assertEquals(TRANSACTION_1.packageName, parsed.packageName);
		assertEquals(TRANSACTION_1.purchaseState, parsed.purchaseState);
		assertNull(parsed.notificationId);
		assertEquals(TRANSACTION_1.purchaseTime, parsed.purchaseTime);
		assertNull(parsed.developerPayload);
	}
	
	@SmallTest
	public void testPurchaseStateOrdinal() throws Exception {
		assertEquals(Transaction.PurchaseState.PURCHASED.ordinal(), 0);
		assertEquals(Transaction.PurchaseState.CANCELLED.ordinal(), 1);
		assertEquals(Transaction.PurchaseState.REFUNDED.ordinal(), 2);
		assertEquals(Transaction.PurchaseState.EXPIRED.ordinal(), 3);
	}
	
	@SmallTest
	public void testEquals() throws Exception {
		assertTrue(TRANSACTION_1.equals(TRANSACTION_1));
		assertTrue(TRANSACTION_1.equals(TRANSACTION_1.clone()));
		assertFalse(TRANSACTION_1.equals(TRANSACTION_2_REFUNDED));
	}
	
	@SmallTest
	public void testClone() throws Exception {
		assertEquals(TRANSACTION_1, TRANSACTION_1.clone());
	}
}
