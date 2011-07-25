package net.robotmedia.billing.example.auxiliary;

import net.robotmedia.billing.example.R;

public class CatalogEntry {
	
	/**
	 * Each product in the catalog is either MANAGED or UNMANAGED. MANAGED means
	 * that the product can be purchased only once per user (such as a new level
	 * in a game). The purchase is remembered by Android Market and can be
	 * restored if this application is uninstalled and then re-installed.
	 * UNMANAGED is used for products that can be used up and purchased multiple
	 * times (such as poker chips). It is up to the application to keep track of
	 * UNMANAGED products for the user.
	 */
	public enum Managed {
		MANAGED, UNMANAGED
	}
	
	public String sku;
	public int nameId;
	public Managed managed;

	public CatalogEntry(String sku, int nameId, Managed managed) {
		this.sku = sku;
		this.nameId = nameId;
		this.managed = managed;
	}
	
	/** An array of product list entries for the products that can be purchased. */
	public static final CatalogEntry[] CATALOG = new CatalogEntry[] {
			new CatalogEntry("sword_001", R.string.two_handed_sword, Managed.MANAGED),
			new CatalogEntry("potion_001", R.string.potions, Managed.UNMANAGED),
			new CatalogEntry("android.test.purchased", R.string.android_test_purchased, Managed.UNMANAGED),
			new CatalogEntry("android.test.canceled", R.string.android_test_canceled, Managed.UNMANAGED),
			new CatalogEntry("android.test.refunded", R.string.android_test_refunded, Managed.UNMANAGED),
			new CatalogEntry("android.test.item_unavailable", R.string.android_test_item_unavailable, Managed.UNMANAGED), };
	
}
