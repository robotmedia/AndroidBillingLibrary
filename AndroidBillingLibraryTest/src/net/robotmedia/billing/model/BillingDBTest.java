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
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class BillingDBTest extends AndroidTestCase {

	public static void assertEqualsFromDb(Transaction a, Transaction b) {
		assertEquals(a.orderId, b.orderId);
		assertEquals(a.productId, b.productId);
		assertEquals(a.purchaseState, b.purchaseState);
		assertEquals(a.purchaseTime, b.purchaseTime);
		assertEquals(a.developerPayload, b.developerPayload);
	}
	
	public static final void deleteDB(BillingDB data) {
		data.mDb.delete(BillingDB.TABLE_TRANSACTIONS, null, null);
		data.close();
	}
	
	private BillingDB mData;
	
	private SQLiteDatabase createVersion1Database() {
		final SQLiteDatabase db = SQLiteDatabase.create(null);
		db.execSQL("CREATE TABLE " + BillingDB.TABLE_TRANSACTIONS + "(" +
				BillingDB.COLUMN__ID + " TEXT PRIMARY KEY, " +
				BillingDB.COLUMN_PRODUCT_ID + " INTEGER, " +
				BillingDB.COLUMN_STATE + " TEXT, " +
				BillingDB.COLUMN_PURCHASE_TIME + " TEXT, " +
				BillingDB.COLUMN_DEVELOPER_PAYLOAD + " INTEGER)");
		return db;
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mData = new BillingDB(getContext());
	}
		
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		deleteDB(mData);
	}

	private void testColumn(SQLiteDatabase db, String column, String expectedType, boolean result) {
		Cursor cursor = db.rawQuery("PRAGMA table_info(" +  BillingDB.TABLE_TRANSACTIONS + ")", null);
		boolean found = false;
		while (cursor.moveToNext()) {
			final String name = cursor.getString(1);
			if (name.equals(column)) {
				final String type = cursor.getString(2);
				assertEquals(type, expectedType);
				found = true;
			}
		}
		cursor.close();
		assertEquals(found, result);
	}
	
	@SmallTest
	public void testDatabaseHelperConstructor() throws Exception {
		BillingDB.DatabaseHelper helper = new BillingDB.DatabaseHelper(this.getContext());
		assertEquals(helper.getReadableDatabase().getVersion(), BillingDB.DATABASE_VERSION);
	}
	
	@SmallTest
	public void testDatabaseHelperOnCreate() throws Exception {
		BillingDB.DatabaseHelper helper = new BillingDB.DatabaseHelper(this.getContext());
		SQLiteDatabase db = SQLiteDatabase.create(null);
		helper.onCreate(db);
		testTable(db, BillingDB.TABLE_TRANSACTIONS);
		testColumn(db, BillingDB.COLUMN__ID, "TEXT", true);
		testColumn(db, BillingDB.COLUMN_PRODUCT_ID, "TEXT", true);
		testColumn(db, BillingDB.COLUMN_STATE, "TEXT", true);
		testColumn(db, BillingDB.COLUMN_PURCHASE_TIME, "TEXT", true);
		testColumn(db, BillingDB.COLUMN_DEVELOPER_PAYLOAD, "TEXT", true);
		testColumn(db, BillingDB.COLUMN_SIGNED_DATA, "TEXT", true);
		testColumn(db, BillingDB.COLUMN_SIGNATURE, "TEXT", true);
	}
	
	@SmallTest
	public void testDatabaseHelperOnUpgradeCurrentVersion() throws Exception {
		BillingDB.DatabaseHelper helper = new BillingDB.DatabaseHelper(this.getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		helper.onUpgrade(db, BillingDB.DATABASE_VERSION, BillingDB.DATABASE_VERSION);
	}
	
	@SmallTest
	public void testDatabaseHelperOnUpgradeVersion1To2() throws Exception {
		BillingDB.DatabaseHelper helper = new BillingDB.DatabaseHelper(this.getContext());
		SQLiteDatabase db = createVersion1Database();
		testColumn(db, BillingDB.COLUMN_SIGNED_DATA, "TEXT", false);
		testColumn(db, BillingDB.COLUMN_SIGNATURE, "TEXT", false);
		helper.onUpgrade(db, 1, 2);
		testColumn(db, BillingDB.COLUMN_SIGNED_DATA, "TEXT", true);
		testColumn(db, BillingDB.COLUMN_SIGNATURE, "TEXT", true);
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
	
	private void testTable(SQLiteDatabase db, String table) {
		Cursor cursor = db.query("sqlite_master", new String[] {"name"}, "type='table' AND name='" + table + "'", null, null, null, null);
		assertTrue(cursor.getCount() > 0);
		cursor.close();
	}
	
	@SmallTest
	public void testUnique() throws Exception {
		mData.insert(TransactionTest.TRANSACTION_1);
		mData.insert(TransactionTest.TRANSACTION_1);
		final Cursor cursor = mData.queryTransactions();
		assertEquals(cursor.getCount(), 1);
	}
}
