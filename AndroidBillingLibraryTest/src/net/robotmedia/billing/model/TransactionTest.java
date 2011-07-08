package net.robotmedia.billing.model;

import java.util.Date;

import org.json.JSONObject;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

public class TransactionTest extends TestCase {
	
	protected static final Transaction PURCHASE_1 = new Transaction("order1", "android.test.purchased", "com.example", Transaction.PurchaseState.PURCHASED, "notificationId", new Date().getTime(), "developerPayload");
	protected static final Transaction PURCHASE_2 = new Transaction("order2", "android.test.refunded", "com.example", Transaction.PurchaseState.REFUNDED, "notificationId", new Date().getTime(), "developerPayload");

	public static void assertEquals(Transaction a, Transaction b) {
		assertEquals(a.orderId, b.orderId);
		assertEquals(a.productId, b.productId);
		assertEquals(a.packageName, b.packageName);
		assertEquals(a.purchaseState, b.purchaseState);
		assertEquals(a.notificationId, b.notificationId);
		assertEquals(a.purchaseTime, b.purchaseTime);
		assertEquals(a.developerPayload, b.developerPayload);
	}
	
	@SmallTest
	public void testParseAllFields() throws Exception {
		JSONObject json = new JSONObject();
		json.put(Transaction.ORDER_ID, PURCHASE_1.orderId);
		json.put(Transaction.PRODUCT_ID, PURCHASE_1.productId);
		json.put(Transaction.PACKAGE_NAME, PURCHASE_1.packageName);
		json.put(Transaction.PURCHASE_STATE, PURCHASE_1.purchaseState.ordinal());
		json.put(Transaction.NOTIFICATION_ID, PURCHASE_1.notificationId);
		json.put(Transaction.PURCHASE_TIME, PURCHASE_1.purchaseTime);
		json.put(Transaction.DEVELOPER_PAYLOAD, PURCHASE_1.developerPayload);
		final Transaction parsed = Transaction.parse(json);
		assertEquals(PURCHASE_1, parsed);
	}
	
	@SmallTest
	public void testParseOnlyMandatoryFields() throws Exception {
		JSONObject json = new JSONObject();
		json.put(Transaction.PRODUCT_ID, PURCHASE_1.productId);
		json.put(Transaction.PACKAGE_NAME, PURCHASE_1.packageName);
		json.put(Transaction.PURCHASE_STATE, PURCHASE_1.purchaseState.ordinal());
		json.put(Transaction.PURCHASE_TIME, PURCHASE_1.purchaseTime);
		final Transaction parsed = Transaction.parse(json);
		assertNull(parsed.orderId);
		assertEquals(PURCHASE_1.productId, parsed.productId);
		assertEquals(PURCHASE_1.packageName, parsed.packageName);
		assertEquals(PURCHASE_1.purchaseState, parsed.purchaseState);
		assertNull(parsed.notificationId);
		assertEquals(PURCHASE_1.purchaseTime, parsed.purchaseTime);
		assertNull(parsed.developerPayload);
	}
	
	@SmallTest
	public void testPurchaseStateOrdinal() throws Exception {
		assertEquals(Transaction.PurchaseState.PURCHASED.ordinal(), 0);
		assertEquals(Transaction.PurchaseState.CANCELLED.ordinal(), 1);
		assertEquals(Transaction.PurchaseState.REFUNDED.ordinal(), 2);
	}
}
