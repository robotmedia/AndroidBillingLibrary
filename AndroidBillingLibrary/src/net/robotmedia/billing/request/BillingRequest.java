/*   Copyright 2011 Robot Media SL (http://www.robotmedia.net)
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
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
    protected static final String KEY_REQUEST_ID = "REQUEST_ID";
	private static final String KEY_NONCE = "NONCE";
    public static final long IGNORE_REQUEST_ID = -1;
	
    private String packageName;
    private boolean success;
    private long nonce;
    
    public BillingRequest(String packageName) {
    	this.packageName = packageName;
    }
        
    protected void addParams(Bundle request) {
    	// Do nothing by default
    }
    
    public long getNonce() {
		return nonce;
	}
	public abstract String getRequestType();	
    
    public boolean hasNonce() {
    	return false;
    }
	public boolean isSuccess() {
    	return success;
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
    
	public void onResponseCode(int responseCode) {
    	// Do nothing by default
	}

    protected void processOkResponse(Bundle response) {    	
    	// Do nothing by default
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
    
    public void setNonce(long nonce) {
		this.nonce = nonce;
	}
    
    protected boolean validateResponse(Bundle response) {
    	final int responseCode = response.getInt(KEY_RESPONSE_CODE);
    	success = ResponseCode.isResponseOk(responseCode);
    	if (!success) {
    		Log.w(this.getClass().getSimpleName(), "Error with response code " + ResponseCode.valueOf(responseCode));
    	}
    	return success;
    }
    
}