package net.robotmedia.billing.example;

import net.robotmedia.billing.BillingController;

public class Application extends android.app.Application {

	@Override
	public void onCreate() {
		super.onCreate();
		BillingController.setDebug(true);
		BillingController.setConfiguration(new BillingController.IConfiguration() {
			
			public byte[] getObfuscationSalt() {
				return new byte[] {41, -90, -116, -41, 66, -53, 122, -110, -127, -96, -88, 77, 127, 115, 1, 73, 57, 110, 48, -116};
			}

			public String getPublicKey() {
				return "your public key here";
			}
		});
	}
	
}
