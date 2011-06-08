/*	Copyright 2011 Robot Media SL <http://www.robotmedia.net>. All rights reserved.
*
*	This file is part of Android Billing Library.
*
*	Android Billing Library is free software: you can redistribute it and/or modify
*	it under the terms of the GNU Lesser Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*	Android Billing Library is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU Lesser Public License for more details.
*
*	You should have received a copy of the GNU Lesser Public License
*	along with Android Billing Library.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.robotmedia.billing;

import android.app.Activity;
import android.app.PendingIntent;

public abstract class AbstractBillingActivity extends Activity implements IBillingObserver, BillingController.IConfiguration {

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BillingController.registerObserver(this);
		BillingController.setConfiguration(this);
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BillingController.unregisterObserver(this);
		BillingController.setConfiguration(null);
	};
	
	@Override
	public void onPurchaseIntent(String itemId, PendingIntent purchaseIntent) {
		BillingController.startPurchaseIntent(this, purchaseIntent, null);
	}

}
