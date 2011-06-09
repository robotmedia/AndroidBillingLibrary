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

package net.robotmedia.billing.request;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.android.vending.billing.IMarketBillingService;

public abstract class BillingRequest {

	private static final String KEY_BILLING_REQUEST = "BILLING_REQUEST";
    private static final String KEY_API_VERSION = "API_VERSION";
    private static final String KEY_PACKAGE_NAME = "PACKAGE_NAME";
    private static final String KEY_RESPONSE_CODE = "RESPONSE_CODE";
    private static final String KEY_REQUEST_ID = "REQUEST_ID";
	private static final String KEY_NONCE = "NONCE";
    public static final long IGNORE_REQUEST_ID = -1;
	
    public abstract String getRequestType();
    protected abstract void addParams(Bundle request);
    protected abstract void processOkResponse(Bundle response);
    
    public boolean hasNonce() {
    	return false;
    }
        
    private String packageName;
    private boolean success;
	private long nonce;	
    
    public long getNonce() {
		return nonce;
	}
	public void setNonce(long nonce) {
		this.nonce = nonce;
	}
	public BillingRequest(String packageName) {
    	this.packageName = packageName;
    }
    
	public long run(IMarketBillingService mService) throws RemoteException {
        final Bundle request = makeRequestBundle();
        addParams(request);
        final Bundle response = mService.sendBillingRequest(request);
        if (validateResponse(response)) {
        	processOkResponse(response);
        	return response.getLong(KEY_REQUEST_ID, IGNORE_REQUEST_ID);
        } else {
        	return IGNORE_REQUEST_ID;
        }
	}

    protected Bundle makeRequestBundle() {
        final Bundle request = new Bundle();
        request.putString(KEY_BILLING_REQUEST, getRequestType());
        request.putInt(KEY_API_VERSION, 1);
        request.putString(KEY_PACKAGE_NAME, packageName);
        if (hasNonce()) {
    		request.putLong(KEY_NONCE, nonce);
        }
        return request;
    }
	
    protected boolean validateResponse(Bundle response) {
    	final int responseCode = response.getInt(KEY_RESPONSE_CODE);
    	success = ResponseCode.isResponseOk(responseCode);
    	if (!success) {
    		Log.w(this.getClass().getSimpleName(), "Error with response code " + ResponseCode.valueOf(responseCode));
    	}
    	return success;
    }
    
    public boolean isSuccess() {
    	return success;
    }
    
}