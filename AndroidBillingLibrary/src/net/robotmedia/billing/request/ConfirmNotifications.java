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

public class ConfirmNotifications extends BillingRequest {

	private String[] notifyIds;
	
	private static final String KEY_NOTIFY_IDS = "NOTIFY_IDS";
	
	public ConfirmNotifications(String packageName, String[] notifyIds) {
		super(packageName);
		this.notifyIds = notifyIds;
	}

	@Override
	public String getRequestType() {
		return "CONFIRM_NOTIFICATIONS";
	}

	@Override
	protected void addParams(Bundle request) {
		request.putStringArray(KEY_NOTIFY_IDS, notifyIds);
	}
	
}
