package net.robotmedia.billing.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import net.robotmedia.billing.IBillingObserver;
import net.robotmedia.billing.BillingController.IConfiguration;
import net.robotmedia.billing.example.R;
import net.robotmedia.billing.example.CatalogEntry.Managed;
import net.robotmedia.billing.model.Transaction;
import net.robotmedia.billing.model.Transaction.PurchaseState;

/**
 * A sample application based on the original Dungeons to demonstrate how to use
 * BillingController and implement IBillingObserver.
 */
public class Dungeons extends Activity implements IBillingObserver, IConfiguration {

	private static final String TAG = "Dungeons";

	/**
	 * The SharedPreferences key for recording whether we initialized the
	 * database. If false, then we perform a RestoreTransactions request to get
	 * all the purchases for this user.
	 */
	private static final String KEY_TRANSACTIONS_RESTORED = "transactionsRestored";
	private Button mBuyButton;
	private Spinner mSelectItemSpinner;
	private ListView mOwnedItemsTable;

	private static final int DIALOG_BILLING_NOT_SUPPORTED_ID = 2;

	/** An array of product list entries for the products that can be purchased. */
	private static final CatalogEntry[] CATALOG = new CatalogEntry[] {
			new CatalogEntry("sword_001", R.string.two_handed_sword, Managed.MANAGED),
			new CatalogEntry("potion_001", R.string.potions, Managed.UNMANAGED),
			new CatalogEntry("android.test.purchased", R.string.android_test_purchased, Managed.UNMANAGED),
			new CatalogEntry("android.test.canceled", R.string.android_test_canceled, Managed.UNMANAGED),
			new CatalogEntry("android.test.refunded", R.string.android_test_refunded, Managed.UNMANAGED),
			new CatalogEntry("android.test.item_unavailable", R.string.android_test_item_unavailable, Managed.UNMANAGED), };
	private String mSku;

	private CatalogAdapter mCatalogAdapter;

	private Dialog createDialog(int titleId, int messageId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(titleId).setIcon(android.R.drawable.stat_sys_warning).setMessage(messageId).setCancelable(
				false).setPositiveButton(android.R.string.ok, null);
		return builder.create();
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
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_row, R.id.item_name, ownedItems);
		mOwnedItemsTable.setAdapter(adapter);
	}

	@Override
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
		setContentView(R.layout.main);

		setupWidgets();
		BillingController.setDebug(true);
		BillingController.setConfiguration(this);
		BillingController.registerObserver(this);
		BillingController.checkBillingSupported(this);
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
	public void onPurchaseCancelled(String itemId) {
		Log.i(TAG, "onPurchaseCancelled() itemId: " + itemId);
	}

	@Override
	public void onPurchaseExecuted(String itemId) {
		Log.i(TAG, "onPurchaseExecuted() itemId: " + itemId);
		updateOwnedItems();
	}

	@Override
	public void onPurchaseIntent(String itemId, PendingIntent purchaseIntent) {
		BillingController.startPurchaseIntent(this, purchaseIntent, new Intent());
	}

	@Override
	public void onPurchaseRefunded(String itemId) {
		Log.i(TAG, "onPurchaseRefunded() itemId: " + itemId);
	}

	@Override
	protected void onDestroy() {
		BillingController.unregisterObserver(this);
		BillingController.setConfiguration(null);
		super.onDestroy();
	}

	@Override
	public void onTransactionsRestored() {
		Log.d(TAG, "completed RestoreTransactions request");
		// Update the shared preferences so that we don't perform
		// a restore transactions again.
		SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putBoolean(KEY_TRANSACTIONS_RESTORED, true);
		edit.commit();
	}

	/**
	 * Restores previous transactions, if any. This
	 * happens if the application has just been installed or the user wiped
	 * data. We do not want to do this on every startup, rather, we want to do
	 * only when the database needs to be initialized.
	 */
	private void restoreTransactions() {
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		boolean initialized = prefs.getBoolean(KEY_TRANSACTIONS_RESTORED, false);
		if (!initialized) {
			BillingController.restoreTransactions(this);
			Toast.makeText(this, R.string.restoring_transactions, Toast.LENGTH_LONG).show();
		}
	}

	private void setupWidgets() {
		mBuyButton = (Button) findViewById(R.id.buy_button);
		mBuyButton.setEnabled(false);
		mBuyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BillingController.requestPurchase(Dungeons.this, mSku, true /*confirm*/);
			}
		});

		mSelectItemSpinner = (Spinner) findViewById(R.id.item_choices);
		mCatalogAdapter = new CatalogAdapter(this, CATALOG);
		mSelectItemSpinner.setAdapter(mCatalogAdapter);
		mSelectItemSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mSku = CATALOG[position].sku;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		mOwnedItemsTable = (ListView) findViewById(R.id.owned_items);
	}

	@Override
	public byte[] getObfuscationSalt() {
		return new byte[] {41, -90, -116, -41, 66, -53, 122, -110, -127, -96, -88, 77, 127, 115, 1, 73, 57, 110, 48, -116};
	}

	@Override
	public String getPublicKey() {
		return "your public key here";
	}
}
