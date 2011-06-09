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

import net.robotmedia.billing.BillingController;
import android.os.Bundle;

public class CheckBillingSupported extends BillingRequest {
		
	public CheckBillingSupported(String packageName) {
		super(packageName);
	}

	@Override
	public String getRequestType() {
		return "CHECK_BILLING_SUPPORTED";
	}

	@Override
	protected void addParams(Bundle request) {}

	@Override
	protected void processOkResponse(Bundle response) {
		final boolean supported = this.isSuccess();
		BillingController.onBillingChecked(supported);
	}
	
}
