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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BillingReceiver extends BroadcastReceiver {

    static final String ACTION_NOTIFY = "com.android.vending.billing.IN_APP_NOTIFY";
    static final String ACTION_RESPONSE_CODE =
        "com.android.vending.billing.RESPONSE_CODE";
    static final String ACTION_PURCHASE_STATE_CHANGED =
        "com.android.vending.billing.PURCHASE_STATE_CHANGED";
	
    static final String EXTRA_NOTIFICATION_ID = "notification_id";
    static final String EXTRA_INAPP_SIGNED_DATA = "inapp_signed_data";
    static final String EXTRA_INAPP_SIGNATURE = "inapp_signature";
    static final String EXTRA_REQUEST_ID = "request_id";
    static final String EXTRA_RESPONSE_CODE = "response_code";
    
	@Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_PURCHASE_STATE_CHANGED.equals(action)) {
            purchaseStateChanged(context, intent);
        } else if (ACTION_NOTIFY.equals(action)) {
            notify(context, intent);
        } else if (ACTION_RESPONSE_CODE.equals(action)) {
        	responseCode(context, intent);
        } else {
            Log.w(this.getClass().getSimpleName(), "Unexpected action: " + action);
        }
    }

	private void purchaseStateChanged(Context context, Intent intent) {
        final String signedData = intent.getStringExtra(EXTRA_INAPP_SIGNED_DATA);
        final String signature = intent.getStringExtra(EXTRA_INAPP_SIGNATURE);
        BillingController.onPurchaseStateChanged(context, signedData, signature);
	}
	
	private void notify(Context context, Intent intent) {
        String notifyId = intent.getStringExtra(EXTRA_NOTIFICATION_ID);
        BillingController.onNotify(context, notifyId);
	}
	
	private void responseCode(Context context, Intent intent) {
        final long requestId = intent.getLongExtra(EXTRA_REQUEST_ID, -1);
        final int responseCode = intent.getIntExtra(EXTRA_RESPONSE_CODE, 0);
        BillingController.onResponseCode(context, requestId, responseCode);
	}
	
}
