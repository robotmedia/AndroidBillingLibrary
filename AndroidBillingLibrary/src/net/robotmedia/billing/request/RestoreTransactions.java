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

public class RestoreTransactions extends BillingRequest {
	
	@Override public boolean hasNonce() { return true; }
	
	public RestoreTransactions(String packageName) {
		super(packageName);
	}

	@Override
	public String getRequestType() {
		return "RESTORE_TRANSACTIONS";
	}

	@Override
	protected void addParams(Bundle request) {}

	@Override
	protected void processOkResponse(Bundle response) {}
	
}
