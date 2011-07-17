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
	
	public static final void deleteDB(BillingDB data) {
		data.mDb.delete(BillingDB.TABLE_TRANSACTIONS, null, null);
		data.close();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		deleteDB(mData);
	}
		
	@SmallTest
	public void testInsert() throws Exception {
		mData.insert(TransactionTest.TRANSACTION_1);
		final Cursor cursor = mData.queryTransactions();
		assertEquals(cursor.getCount(), 1);
		cursor.moveToNext();
		final Transaction stored = BillingDB.createTransaction(cursor);
		stored.packageName = TransactionTest.TRANSACTION_1.packageName; // Not stored in DB
		stored.notificationId = TransactionTest.TRANSACTION_1.notificationId; // Not stored in DB
		assertEqualsFromDb(TransactionTest.TRANSACTION_1, stored);
	}

	@SmallTest
	public void testUnique() throws Exception {
		mData.insert(TransactionTest.TRANSACTION_1);
		mData.insert(TransactionTest.TRANSACTION_1);
		final Cursor cursor = mData.queryTransactions();
		assertEquals(cursor.getCount(), 1);
	}
	
	@SmallTest
	public void testQueryTransactions() throws Exception {
		final Cursor cursor1 = mData.queryTransactions();
		assertEquals(cursor1.getCount(), 0);
		cursor1.close();

		mData.insert(TransactionTest.TRANSACTION_1);
		final Cursor cursor2 = mData.queryTransactions();
		assertEquals(cursor2.getCount(), 1);
		cursor2.moveToNext();
		final Transaction stored = BillingDB.createTransaction(cursor2);
		stored.packageName = TransactionTest.TRANSACTION_1.packageName; // Not stored in DB
		stored.notificationId = TransactionTest.TRANSACTION_1.notificationId; // Not stored in DB
		TransactionTest.assertEquals(TransactionTest.TRANSACTION_1, stored);
		cursor2.close();

		mData.insert(TransactionTest.TRANSACTION_2_REFUNDED);
		final Cursor cursor3 = mData.queryTransactions();
		assertEquals(cursor3.getCount(), 2);
		cursor3.close();
	}
	
	@SmallTest
	public void testQueryTransactionsString() throws Exception {
		final Cursor cursor1 = mData.queryTransactions(TransactionTest.TRANSACTION_1.productId);
		assertEquals(cursor1.getCount(), 0);
		cursor1.close();

		mData.insert(TransactionTest.TRANSACTION_1);
		final Cursor cursor2 = mData.queryTransactions(TransactionTest.TRANSACTION_1.productId);
		assertEquals(cursor2.getCount(), 1);
		cursor2.moveToNext();
		final Transaction stored = BillingDB.createTransaction(cursor2);
		stored.packageName = TransactionTest.TRANSACTION_1.packageName; // Not stored in DB
		stored.notificationId = TransactionTest.TRANSACTION_1.notificationId; // Not stored in DB
		TransactionTest.assertEquals(TransactionTest.TRANSACTION_1, stored);
		cursor2.close();

		mData.insert(TransactionTest.TRANSACTION_2_REFUNDED);
		final Cursor cursor3 = mData.queryTransactions(TransactionTest.TRANSACTION_1.productId);
		assertEquals(cursor3.getCount(), 1);
		cursor3.close();
	}
	
	@SmallTest
	public void testQueryTransactionsStringPurchaseState() throws Exception {
		final Cursor cursor1 = mData.queryTransactions(TransactionTest.TRANSACTION_1.productId, TransactionTest.TRANSACTION_1.purchaseState);
		assertEquals(cursor1.getCount(), 0);
		cursor1.close();

		mData.insert(TransactionTest.TRANSACTION_1);
		mData.insert(TransactionTest.TRANSACTION_2_REFUNDED);
		final Cursor cursor2 = mData.queryTransactions(TransactionTest.TRANSACTION_1.productId, TransactionTest.TRANSACTION_1.purchaseState);
		assertEquals(cursor2.getCount(), 1);
		cursor2.moveToNext();
		final Transaction stored = BillingDB.createTransaction(cursor2);
		stored.packageName = TransactionTest.TRANSACTION_1.packageName; // Not stored in DB
		stored.notificationId = TransactionTest.TRANSACTION_1.notificationId; // Not stored in DB
		TransactionTest.assertEquals(TransactionTest.TRANSACTION_1, stored);
		cursor2.close();
	}
	
}
