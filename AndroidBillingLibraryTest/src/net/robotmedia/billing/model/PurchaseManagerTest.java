package net.robotmedia.billing.model;

import java.util.List;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class PurchaseManagerTest extends AndroidTestCase {
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		BillingDB mData = new BillingDB(getContext());
		mData.mDb.delete(BillingDB.TABLE_PURCHASES, null, null);
		mData.close();
	}
	
	@MediumTest
	public void testAddPurchase() throws Exception {
		PurchaseManager.addPurchase(getContext(), PurchaseTest.PURCHASE_1);
		final List<Purchase> purchases = PurchaseManager.getPurchases(getContext());
		assertEquals(purchases.size(), 1);
		final Purchase stored = purchases.get(0);
		BillingDBTest.assertEqualsFromDb(PurchaseTest.PURCHASE_1, stored);
	}
	
	public static Purchase clone(Purchase p) {
		return new Purchase(p.orderId, p.productId, p.packageName, p.purchaseState, p.notificationId, p.purchaseTime, p.developerPayload);
	}
	
	@MediumTest
	public void testCountPurchases() throws Exception {
		assertEquals(PurchaseManager.countPurchases(getContext(), PurchaseTest.PURCHASE_1.productId), 0);
		PurchaseManager.addPurchase(getContext(), PurchaseTest.PURCHASE_1);
		assertEquals(PurchaseManager.countPurchases(getContext(), PurchaseTest.PURCHASE_1.productId), 1);
		final Purchase newOrder = clone(PurchaseTest.PURCHASE_1);
		newOrder.orderId = "newOrder";
		PurchaseManager.addPurchase(getContext(), newOrder);
		assertEquals(PurchaseManager.countPurchases(getContext(), PurchaseTest.PURCHASE_1.productId), 2);
	}
	
	@MediumTest
	public void testIsPurchased() throws Exception {
		assertFalse(PurchaseManager.isPurchased(getContext(), PurchaseTest.PURCHASE_1.productId));
		PurchaseManager.addPurchase(getContext(), PurchaseTest.PURCHASE_1);
		assertTrue(PurchaseManager.isPurchased(getContext(), PurchaseTest.PURCHASE_1.productId));
	}
}
