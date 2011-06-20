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
