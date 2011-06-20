package net.robotmedia.billing.model;

import java.util.Date;

import org.json.JSONObject;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

public class PurchaseTest extends TestCase {
	
	protected static final Purchase PURCHASE_1 = new Purchase("order1", "android.test.purchased", "com.example", Purchase.PurchaseState.PURCHASED, "notificationId", new Date().getTime(), "developerPayload");
	protected static final Purchase PURCHASE_2 = new Purchase("order2", "android.test.refunded", "com.example", Purchase.PurchaseState.REFUNDED, "notificationId", new Date().getTime(), "developerPayload");

	public static void assertEquals(Purchase a, Purchase b) {
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
		json.put(Purchase.ORDER_ID, PURCHASE_1.orderId);
		json.put(Purchase.PRODUCT_ID, PURCHASE_1.productId);
		json.put(Purchase.PACKAGE_NAME, PURCHASE_1.packageName);
		json.put(Purchase.PURCHASE_STATE, PURCHASE_1.purchaseState.ordinal());
		json.put(Purchase.NOTIFICATION_ID, PURCHASE_1.notificationId);
		json.put(Purchase.PURCHASE_TIME, PURCHASE_1.purchaseTime);
		json.put(Purchase.DEVELOPER_PAYLOAD, PURCHASE_1.developerPayload);
		final Purchase parsed = Purchase.parse(json);
		assertEquals(PURCHASE_1, parsed);
	}
	
	@SmallTest
	public void testParseOnlyMandatoryFields() throws Exception {
		JSONObject json = new JSONObject();
		json.put(Purchase.PRODUCT_ID, PURCHASE_1.productId);
		json.put(Purchase.PACKAGE_NAME, PURCHASE_1.packageName);
		json.put(Purchase.PURCHASE_STATE, PURCHASE_1.purchaseState.ordinal());
		json.put(Purchase.PURCHASE_TIME, PURCHASE_1.purchaseTime);
		final Purchase parsed = Purchase.parse(json);
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
		assertEquals(Purchase.PurchaseState.PURCHASED.ordinal(), 0);
		assertEquals(Purchase.PurchaseState.CANCELLED.ordinal(), 1);
		assertEquals(Purchase.PurchaseState.REFUNDED.ordinal(), 2);
	}
}
