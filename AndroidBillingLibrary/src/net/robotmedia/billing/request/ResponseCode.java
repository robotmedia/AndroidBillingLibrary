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

public enum ResponseCode {
	RESULT_OK, // 0
	RESULT_USER_CANCELED, // 1 
	RESULT_SERVICE_UNAVAILABLE, // 2 
	RESULT_BILLING_UNAVAILABLE, // 3
	RESULT_ITEM_UNAVAILABLE, // 4
	RESULT_DEVELOPER_ERROR, // 5
	RESULT_ERROR; // 6

	// Converts from an ordinal value to the ResponseCode
	public static ResponseCode valueOf(int index) {
		ResponseCode[] values = ResponseCode.values();
		if (index < 0 || index >= values.length) {
			return RESULT_ERROR;
		}
		return values[index];
	}

	public static boolean isResponseOk(int response) {
		return ResponseCode.RESULT_OK.ordinal() == response;
	}
}
