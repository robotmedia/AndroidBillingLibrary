package net.robotmedia.billing.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import net.robotmedia.billing.BillingController;
import net.robotmedia.billing.BillingRequest.ResponseCode;
import net.robotmedia.billing.dungeons.redux.R;
import net.robotmedia.billing.example.auxiliary.CatalogAdapter;
import net.robotmedia.billing.example.auxiliary.CatalogEntry;
import net.robotmedia.billing.example.auxiliary.CatalogEntry.Managed;
import net.robotmedia.billing.helper.AbstractBillingObserver;
import net.robotmedia.billing.model.Transaction;
import net.robotmedia.billing.model.Transaction.PurchaseState;

/**
 * A sample application based on the original Dungeons to demonstrate how to use
 * BillingController and implement IBillingObserver.
 */
public class Dungeons extends Activity {

	private static final String TAG = "Dungeons";

	private Button mBuyButton;
	private Spinner mSelectItemSpinner;
	private ListView mOwnedItemsTable;

	private static final int DIALOG_BILLING_NOT_SUPPORTED_ID = 2;

	private CatalogEntry mSelectedItem;

	private CatalogAdapter mCatalogAdapter;

	private AbstractBillingObserver mBillingObserver;

	private Dialog createDialog(int titleId, int messageId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(titleId).setIcon(android.R.drawable.stat_sys_warning).setMessage(messageId).setCancelable(
				false).setPositiveButton(android.R.string.ok, null);
		return builder.create();
	}

	public void onBillingChecked(boolean supported) {
		if (supported) {
			restoreTransactions();
			mBuyButton.setEnabled(true);
		} else {
			showDialog(DIALOG_BILLING_NOT_SUPPORTED_ID);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBillingObserver = new AbstractBillingObserver(this) {

			public void onBillingChecked(boolean supported) {
				Dungeons.this.onBillingChecked(supported);
			}

			public void onPurchaseStateChanged(String itemId, PurchaseState state, String orderId) {
				Dungeons.this.onPurchaseStateChanged(itemId, state, orderId);
			}

			public void onRequestPurchaseResponse(String itemId, ResponseCode response) {
				Dungeons.this.onRequestPurchaseResponse(itemId, response);
			}

			public void onSubscriptionChecked(boolean supported) {
				Dungeons.this.onSubscriptionChecked(supported);
			}
		
		};
		
		setContentView(R.layout.main);

		setupWidgets();
		BillingController.registerObserver(mBillingObserver);
		BillingController.checkBillingSupported(this);
		BillingController.checkSubscriptionSupported(this);
		updateOwnedItems();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_BILLING_NOT_SUPPORTED_ID:
			return createDialog(R.string.billing_not_supported_title, R.string.billing_not_supported_message);
		default:
			return null;
		}
	}

	@Override
	protected void onDestroy() {
		BillingController.unregisterObserver(mBillingObserver);
		super.onDestroy();
	}

	public void onPurchaseStateChanged(String itemId, PurchaseState state, String orderId) {
		Log.i(TAG, "onPurchaseStateChanged() itemId: " + itemId);
		updateOwnedItems();
	}

	public void onRequestPurchaseResponse(String itemId, ResponseCode response) {
	}
	
	public void onSubscriptionChecked(boolean supported) {
		
	}

	/**
	 * Restores previous transactions, if any. This happens if the application
	 * has just been installed or the user wiped data. We do not want to do this
	 * on every startup, rather, we want to do only when the database needs to
	 * be initialized.
	 */
	private void restoreTransactions() {
		if (!mBillingObserver.isTransactionsRestored()) {
			BillingController.restoreTransactions(this);
			Toast.makeText(this, R.string.restoring_transactions, Toast.LENGTH_LONG).show();
		}
	}

	private void setupWidgets() {
		mBuyButton = (Button) findViewById(R.id.buy_button);
		mBuyButton.setEnabled(false);
		mBuyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (mSelectedItem.managed != Managed.SUBSCRIPTION) {
					BillingController.requestPurchase(Dungeons.this, mSelectedItem.sku);
				} else {
					BillingController.requestSubscription(Dungeons.this, mSelectedItem.sku);
				}
			}
		});

		mSelectItemSpinner = (Spinner) findViewById(R.id.item_choices);
		mCatalogAdapter = new CatalogAdapter(this, CatalogEntry.CATALOG);
		mSelectItemSpinner.setAdapter(mCatalogAdapter);
		mSelectItemSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mSelectedItem = CatalogEntry.CATALOG[position];
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		mOwnedItemsTable = (ListView) findViewById(R.id.owned_items);
	}

	private void updateOwnedItems() {
		List<Transaction> transactions = BillingController.getTransactions(this);
		final ArrayList<String> ownedItems = new ArrayList<String>();
		for (Transaction t : transactions) {
			if (t.purchaseState == PurchaseState.PURCHASED) {
				ownedItems.add(t.productId);
			}
		}

		mCatalogAdapter.setOwnedItems(ownedItems);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_row, R.id.item_name,
				ownedItems);
		mOwnedItemsTable.setAdapter(adapter);
	}
}