package net.robotmedia.billing.example;

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
	
}
