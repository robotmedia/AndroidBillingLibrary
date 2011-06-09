package net.robotmedia.billing.model;

import java.util.Date;

import org.json.JSONObject;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

public class PurchaseTest extends TestCase {
	
	protected static final Purchase PURCHASE = new Purchase("orderId", "android.test.purchased", "com.example", Purchase.PurchaseState.PURCHASED, "notificationId", new Date().getTime(), "developerPayload");

	@SmallTest
	public void testParseAllFields() throws Exception {
		JSONObject json = new JSONObject();
		json.put(Purchase.ORDER_ID, PURCHASE.orderId);
		json.put(Purchase.PRODUCT_ID, PURCHASE.productId);
		json.put(Purchase.PACKAGE_NAME, PURCHASE.packageName);
		json.put(Purchase.PURCHASE_STATE, PURCHASE.purchaseState.ordinal());
		json.put(Purchase.NOTIFICATION_ID, PURCHASE.notificationId);
		json.put(Purchase.PURCHASE_TIME, PURCHASE.purchaseTime);
		json.put(Purchase.DEVELOPER_PAYLOAD, PURCHASE.developerPayload);
		final Purchase parsed = Purchase.parse(json);
		assertEquals(PURCHASE.orderId, parsed.orderId);
		assertEquals(PURCHASE.productId, parsed.productId);
		assertEquals(PURCHASE.packageName, parsed.packageName);
		assertEquals(PURCHASE.purchaseState, parsed.purchaseState);
		assertEquals(PURCHASE.purchaseTime, parsed.purchaseTime);
		assertEquals(PURCHASE.developerPayload, parsed.developerPayload);
	}
	
	@SmallTest
	public void testParseOnlyMandatoryFields() throws Exception {
		JSONObject json = new JSONObject();
		json.put(Purchase.PRODUCT_ID, PURCHASE.productId);
		json.put(Purchase.PACKAGE_NAME, PURCHASE.packageName);
		json.put(Purchase.PURCHASE_STATE, PURCHASE.purchaseState.ordinal());
		json.put(Purchase.PURCHASE_TIME, PURCHASE.purchaseTime);
		final Purchase parsed = Purchase.parse(json);
		assertEquals(PURCHASE.productId, parsed.productId);
		assertEquals(PURCHASE.packageName, parsed.packageName);
		assertEquals(PURCHASE.purchaseState, parsed.purchaseState);
		assertEquals(PURCHASE.purchaseTime, parsed.purchaseTime);
	}
}
