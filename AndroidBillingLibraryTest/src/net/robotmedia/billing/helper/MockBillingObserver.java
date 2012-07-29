package net.robotmedia.billing.helper;

import android.app.PendingIntent;
import net.robotmedia.billing.BillingRequest.ResponseCode;
import net.robotmedia.billing.IBillingObserver;
import net.robotmedia.billing.model.Transaction.PurchaseState;

public class MockBillingObserver implements IBillingObserver {

	public void onBillingChecked(boolean supported) {
	}

	public void onSubscriptionChecked(boolean supported) {
	}

	public void onPurchaseIntent(String itemId, PendingIntent purchaseIntent) {
	}

	public void onPurchaseStateChanged(String itemId, PurchaseState state) {
	}

	public void onRequestPurchaseResponse(String itemId, ResponseCode response) {
	}

	public void onTransactionsRestored() {
	}

}
