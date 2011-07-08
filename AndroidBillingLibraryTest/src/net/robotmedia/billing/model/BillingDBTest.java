package net.robotmedia.billing.model;

import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class BillingDBTest extends AndroidTestCase {

	private BillingDB mData;
	
	public static void assertEqualsFromDb(Transaction a, Transaction b) {
		assertEquals(a.orderId, b.orderId);
		assertEquals(a.productId, b.productId);
		assertEquals(a.purchaseState, b.purchaseState);
		assertEquals(a.purchaseTime, b.purchaseTime);
		assertEquals(a.developerPayload, b.developerPayload);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mData = new BillingDB(getContext());
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mData.mDb.delete(BillingDB.TABLE_TRANSACTIONS, null, null);
		mData.close();
	}
		
	@SmallTest
	public void testInsert() throws Exception {
		mData.insert(TransactionTest.PURCHASE_1);
		final Cursor cursor = mData.queryTransactions();
		assertEquals(cursor.getCount(), 1);
		cursor.moveToNext();
		final Transaction stored = BillingDB.createTransaction(cursor);
		stored.packageName = TransactionTest.PURCHASE_1.packageName; // Not stored in DB
		stored.notificationId = TransactionTest.PURCHASE_1.notificationId; // Not stored in DB
		assertEqualsFromDb(TransactionTest.PURCHASE_1, stored);
	}

	@SmallTest
	public void testUnique() throws Exception {
		mData.insert(TransactionTest.PURCHASE_1);
		mData.insert(TransactionTest.PURCHASE_1);
		final Cursor cursor = mData.queryTransactions();
		assertEquals(cursor.getCount(), 1);
	}
	
	@SmallTest
	public void testQueryPurchases() throws Exception {
		mData.insert(TransactionTest.PURCHASE_1);
		mData.insert(TransactionTest.PURCHASE_2);
		final Cursor cursor = mData.queryTransactions(TransactionTest.PURCHASE_1.productId, TransactionTest.PURCHASE_1.purchaseState);
		assertEquals(cursor.getCount(), 1);
		cursor.moveToNext();
		final Transaction stored = BillingDB.createTransaction(cursor);
		stored.packageName = TransactionTest.PURCHASE_1.packageName; // Not stored in DB
		stored.notificationId = TransactionTest.PURCHASE_1.notificationId; // Not stored in DB
		TransactionTest.assertEquals(TransactionTest.PURCHASE_1, stored);		
	}
	
}
