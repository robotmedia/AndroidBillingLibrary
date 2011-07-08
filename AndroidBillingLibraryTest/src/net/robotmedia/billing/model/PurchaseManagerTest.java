package net.robotmedia.billing.model;

import java.util.List;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class PurchaseManagerTest extends AndroidTestCase {
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		BillingDB mData = new BillingDB(getContext());
		mData.mDb.delete(BillingDB.TABLE_TRANSACTIONS, null, null);
		mData.close();
	}
	
	@MediumTest
	public void testAddPurchase() throws Exception {
		TransactionManager.addTransaction(getContext(), TransactionTest.PURCHASE_1);
		final List<Transaction> purchases = TransactionManager.getTransactions(getContext());
		assertEquals(purchases.size(), 1);
		final Transaction stored = purchases.get(0);
		BillingDBTest.assertEqualsFromDb(TransactionTest.PURCHASE_1, stored);
	}
	
	public static Transaction clone(Transaction p) {
		return new Transaction(p.orderId, p.productId, p.packageName, p.purchaseState, p.notificationId, p.purchaseTime, p.developerPayload);
	}
	
	@MediumTest
	public void testCountPurchases() throws Exception {
		assertEquals(TransactionManager.countPurchases(getContext(), TransactionTest.PURCHASE_1.productId), 0);
		TransactionManager.addTransaction(getContext(), TransactionTest.PURCHASE_1);
		assertEquals(TransactionManager.countPurchases(getContext(), TransactionTest.PURCHASE_1.productId), 1);
		final Transaction newOrder = clone(TransactionTest.PURCHASE_1);
		newOrder.orderId = "newOrder";
		TransactionManager.addTransaction(getContext(), newOrder);
		assertEquals(TransactionManager.countPurchases(getContext(), TransactionTest.PURCHASE_1.productId), 2);
	}
	
	@MediumTest
	public void testIsPurchased() throws Exception {
		assertFalse(TransactionManager.isPurchased(getContext(), TransactionTest.PURCHASE_1.productId));
		TransactionManager.addTransaction(getContext(), TransactionTest.PURCHASE_1);
		assertTrue(TransactionManager.isPurchased(getContext(), TransactionTest.PURCHASE_1.productId));
	}
}
