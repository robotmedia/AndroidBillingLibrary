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

import java.util.List;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class TransactionManagerTest extends AndroidTestCase {
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		BillingDB mData = new BillingDB(getContext());
		mData.mDb.delete(BillingDB.TABLE_TRANSACTIONS, null, null);
		mData.close();
	}
	
	@MediumTest
	public void testAddPurchase() throws Exception {
		TransactionManager.addTransaction(getContext(), TransactionTest.TRANSACTION_1);
		final List<Transaction> purchases = TransactionManager.getTransactions(getContext());
		assertEquals(purchases.size(), 1);
		final Transaction stored = purchases.get(0);
		BillingDBTest.assertEqualsFromDb(TransactionTest.TRANSACTION_1, stored);
	}
	
	@MediumTest
	public void testCountPurchases() throws Exception {
		assertEquals(TransactionManager.countPurchases(getContext(), TransactionTest.TRANSACTION_1.productId), 0);
		TransactionManager.addTransaction(getContext(), TransactionTest.TRANSACTION_1);
		assertEquals(TransactionManager.countPurchases(getContext(), TransactionTest.TRANSACTION_1.productId), 1);
		final Transaction newOrder = TransactionTest.TRANSACTION_1.clone();
		newOrder.orderId = "newOrder";
		TransactionManager.addTransaction(getContext(), newOrder);
		assertEquals(TransactionManager.countPurchases(getContext(), TransactionTest.TRANSACTION_1.productId), 2);
	}
	
	@MediumTest
	public void testIsPurchased() throws Exception {
		assertFalse(TransactionManager.isPurchased(getContext(), TransactionTest.TRANSACTION_1.productId));
		TransactionManager.addTransaction(getContext(), TransactionTest.TRANSACTION_1);
		assertTrue(TransactionManager.isPurchased(getContext(), TransactionTest.TRANSACTION_1.productId));
	}
	
	@MediumTest
	public void testGetTransactions() throws Exception {
		final List<Transaction> transactions1 = TransactionManager.getTransactions(getContext());
		assertEquals(transactions1.size(), 0);
		TransactionManager.addTransaction(getContext(), TransactionTest.TRANSACTION_1);
		final List<Transaction> transactions2 = TransactionManager.getTransactions(getContext());
		assertEquals(transactions2.size(), 1);
		TransactionManager.addTransaction(getContext(), TransactionTest.TRANSACTION_2_REFUNDED);
		final List<Transaction> transactions3 = TransactionManager.getTransactions(getContext());
		assertEquals(transactions3.size(), 2);
	}
	
	@MediumTest
	public void testGetTransactionsString() throws Exception {
		final List<Transaction> transactions1 = TransactionManager.getTransactions(getContext(), TransactionTest.TRANSACTION_1.productId);
		assertEquals(transactions1.size(), 0);
		TransactionManager.addTransaction(getContext(), TransactionTest.TRANSACTION_1);
		final List<Transaction> transactions2 = TransactionManager.getTransactions(getContext(), TransactionTest.TRANSACTION_1.productId);
		assertEquals(transactions2.size(), 1);
		TransactionManager.addTransaction(getContext(), TransactionTest.TRANSACTION_2_REFUNDED);
		final List<Transaction> transactions3 = TransactionManager.getTransactions(getContext(), TransactionTest.TRANSACTION_1.productId);
		assertEquals(transactions3.size(), 1);
	}
	
}
