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

package net.robotmedia.billing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.robotmedia.billing.BillingController.BillingStatus;
import net.robotmedia.billing.BillingRequest.ResponseCode;
import net.robotmedia.billing.helper.MockBillingObserver;
import net.robotmedia.billing.model.BillingDB;
import net.robotmedia.billing.model.BillingDBTest;
import net.robotmedia.billing.model.Transaction;
import net.robotmedia.billing.model.TransactionTest;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

public class BillingControllerTest extends AndroidTestCase {

	private BillingDB mData;
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		BillingDBTest.deleteDB(mData);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mData = new BillingDB(getContext());
	}
	
	@SmallTest
	public void testCheckBillingSupported() throws Exception {
		BillingController.checkBillingSupported(getContext());
	}
	
	@SmallTest
	public void testCheckSubscriptionSupported() throws Exception {
		BillingController.checkSubscriptionSupported(getContext());
	}
	
	@MediumTest
	public void testIsPurchased() throws Exception {
		assertFalse(BillingController.isPurchased(getContext(), TransactionTest.TRANSACTION_1.productId));
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1);
		assertTrue(BillingController.isPurchased(getContext(), TransactionTest.TRANSACTION_1.productId));
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1_REFUNDED);
		assertTrue(BillingController.isPurchased(getContext(), TransactionTest.TRANSACTION_1.productId));
	}
	
	@MediumTest
	public void testCountPurchases() throws Exception {
		assertEquals(BillingController.countPurchases(getContext(), TransactionTest.TRANSACTION_1.productId), 0);
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1);
		assertEquals(BillingController.countPurchases(getContext(), TransactionTest.TRANSACTION_1.productId), 1);
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1_REFUNDED);
		assertEquals(BillingController.countPurchases(getContext(), TransactionTest.TRANSACTION_1.productId), 1);
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_2);
		assertEquals(BillingController.countPurchases(getContext(), TransactionTest.TRANSACTION_1.productId), 1);
	}
	
	@MediumTest
	public void testGetTransactions() throws Exception {
		final List<Transaction> transactions0 = BillingController.getTransactions(getContext());
		assertEquals(transactions0.size(), 0);
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1);
		final List<Transaction> transactions1 = BillingController.getTransactions(getContext());
		assertEquals(transactions1.size(), 1);
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_2_REFUNDED);
		final List<Transaction> transactions2 = BillingController.getTransactions(getContext());
		assertEquals(transactions2.size(), 2);
	}

	@MediumTest
	public void testGetTransactionsString() throws Exception {
		final List<Transaction> transactions0 = BillingController.getTransactions(getContext());
		assertEquals(transactions0.size(), 0);
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1);
		final List<Transaction> transactions1 = BillingController.getTransactions(getContext(), TransactionTest.TRANSACTION_1.productId);
		assertEquals(transactions1.size(), 1);
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_2_REFUNDED);
		final List<Transaction> transactions2 = BillingController.getTransactions(getContext(), TransactionTest.TRANSACTION_1.productId);
		assertEquals(transactions2.size(), 1);
	}
	
	@SmallTest
	public void testOnTransactionRestored() throws Exception {
		final Set<Boolean> flags = new HashSet<Boolean>();
		final IBillingObserver observer = new MockBillingObserver() {
			@Override
			public void onTransactionsRestored() {
				flags.add(true);
			}
		};
		BillingController.registerObserver(observer);
		BillingController.onTransactionsRestored();
		assertEquals(flags.size(), 1);
		BillingController.unregisterObserver(observer);
	}
	
	@SmallTest
	public void testOnRequestPurchaseResponse() throws Exception {
		final String testItemId = TransactionTest.TRANSACTION_1.productId;
		final ResponseCode testResponse = ResponseCode.RESULT_OK;
		final Set<Boolean> flags = new HashSet<Boolean>();
		final IBillingObserver observer = new MockBillingObserver() {
			@Override
			public void onRequestPurchaseResponse(String itemId, ResponseCode response) { 
				flags.add(true);
				assertEquals(testItemId, itemId);
				assertEquals(testResponse, response);
			}
		};
		BillingController.registerObserver(observer);
		BillingController.onRequestPurchaseResponse(testItemId, testResponse);
		assertEquals(flags.size(), 1);
		BillingController.unregisterObserver(observer);
	}
	
	public void testOnBillingCheckedSupportedTrue() throws Exception {
		final Set<Boolean> flags = new HashSet<Boolean>();
		final IBillingObserver observer = new MockBillingObserver() {
			@Override
			public void onBillingChecked(boolean supported) {
				flags.add(true);
				assertTrue(supported);
			}
		};
		BillingController.registerObserver(observer);
		BillingController.onBillingChecked(true);
		assertEquals(flags.size(), 1);
		assertEquals(BillingController.checkBillingSupported(getContext()), BillingStatus.SUPPORTED);
		BillingController.unregisterObserver(observer);
	}
	
	public void testOnBillingCheckedSupportedFalse() throws Exception {
		final Set<Boolean> flags = new HashSet<Boolean>();
		final IBillingObserver observer = new MockBillingObserver() {
			@Override
			public void onBillingChecked(boolean supported) {
				flags.add(true);
				assertFalse(supported);
			}
		};
		BillingController.registerObserver(observer);
		BillingController.onBillingChecked(false);
		assertEquals(flags.size(), 1);
		assertEquals(BillingController.checkBillingSupported(getContext()), BillingStatus.UNSUPPORTED);
		assertEquals(BillingController.checkSubscriptionSupported(getContext()), BillingStatus.UNSUPPORTED);
		BillingController.unregisterObserver(observer);
	}
	
	public void testOnSubscriptionCheckedSupportedTrue() throws Exception {
		final Set<Boolean> flags = new HashSet<Boolean>();
		final IBillingObserver observer = new MockBillingObserver() {
			@Override
			public void onSubscriptionChecked(boolean supported) {
				flags.add(true);
				assertTrue(supported);
			}
		};
		BillingController.registerObserver(observer);
		BillingController.onSubscriptionChecked(true);
		assertEquals(flags.size(), 1);
		assertEquals(BillingController.checkBillingSupported(getContext()), BillingStatus.SUPPORTED);
		assertEquals(BillingController.checkSubscriptionSupported(getContext()), BillingStatus.SUPPORTED);
		BillingController.unregisterObserver(observer);
	}
	
	public void testOnSubscriptionCheckedSupportedFalse() throws Exception {
		final Set<Boolean> flags = new HashSet<Boolean>();
		final IBillingObserver observer = new MockBillingObserver() {
			@Override
			public void onSubscriptionChecked(boolean supported) {
				flags.add(true);
				assertFalse(supported);
			}
		};
		BillingController.registerObserver(observer);
		BillingController.onSubscriptionChecked(false);
		assertEquals(flags.size(), 1);
		assertEquals(BillingController.checkSubscriptionSupported(getContext()), BillingStatus.UNSUPPORTED);
		BillingController.unregisterObserver(observer);
	}
}
