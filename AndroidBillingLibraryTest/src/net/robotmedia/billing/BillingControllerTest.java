package net.robotmedia.billing;

import java.util.List;

import net.robotmedia.billing.model.BillingDB;
import net.robotmedia.billing.model.BillingDBTest;
import net.robotmedia.billing.model.Transaction;
import net.robotmedia.billing.model.TransactionTest;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;

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
	
	@MediumTest
	public void testIsPurchased() throws Exception {
		assertFalse(BillingController.isPurchased(getContext(), TransactionTest.TRANSACTION_1.productId));
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1);
		assertTrue(BillingController.isPurchased(getContext(), TransactionTest.TRANSACTION_1.productId));
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1_REFUNDED);
		assertTrue(BillingController.isPurchased(getContext(), TransactionTest.TRANSACTION_1.productId));
	}
	
	@MediumTest
	public void testIsPurchasedNet() throws Exception {
		assertFalse(BillingController.isPurchasedNet(getContext(), TransactionTest.TRANSACTION_1.productId));
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1);
		assertTrue(BillingController.isPurchasedNet(getContext(), TransactionTest.TRANSACTION_1.productId));
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1_REFUNDED);
		assertFalse(BillingController.isPurchasedNet(getContext(), TransactionTest.TRANSACTION_1.productId));
	}
	
	@MediumTest
	public void testCountPurchases() throws Exception {
		assertEquals(BillingController.countPurchases(getContext(), TransactionTest.TRANSACTION_1.productId), 0);
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1);
		assertEquals(BillingController.countPurchases(getContext(), TransactionTest.TRANSACTION_1.productId), 1);
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1_REFUNDED);
		assertEquals(BillingController.countPurchases(getContext(), TransactionTest.TRANSACTION_1.productId), 1);
	}
	
	@MediumTest
	public void testCountPurchasesNet() throws Exception {
		assertEquals(BillingController.countPurchasesNet(getContext(), TransactionTest.TRANSACTION_1.productId), 0);
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1);
		assertEquals(BillingController.countPurchasesNet(getContext(), TransactionTest.TRANSACTION_1.productId), 1);
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1_REFUNDED);
		assertEquals(BillingController.countPurchasesNet(getContext(), TransactionTest.TRANSACTION_1.productId), 0);
	}
	
	@MediumTest
	public void testGetTransactions() throws Exception {
		final List<Transaction> transactions0 = BillingController.getTransactions(getContext());
		assertEquals(transactions0.size(), 0);
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_1);
		final List<Transaction> transactions1 = BillingController.getTransactions(getContext());
		assertEquals(transactions1.size(), 1);
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_2);
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
		BillingController.storeTransaction(getContext(), TransactionTest.TRANSACTION_2);
		final List<Transaction> transactions2 = BillingController.getTransactions(getContext(), TransactionTest.TRANSACTION_1.productId);
		assertEquals(transactions2.size(), 1);
	}
}
