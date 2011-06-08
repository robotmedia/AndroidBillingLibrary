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

package net.robotmedia.billing.utils;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;

public class Compatibility {
    private static Method startIntentSender;
    @SuppressWarnings("rawtypes")
	private static final Class[] START_INTENT_SENDER_SIG = new Class[] {
        IntentSender.class, Intent.class, int.class, int.class, int.class
    };
    
	static {
		initCompatibility();
	};

	private static void initCompatibility() {
        try {
        	startIntentSender = Activity.class.getMethod("startIntentSender",
                    START_INTENT_SENDER_SIG);
        } catch (SecurityException e) {
        	startIntentSender = null;
        } catch (NoSuchMethodException e) {
        	startIntentSender = null;
        }
	}
	
	public static void startIntentSender(Activity activity, IntentSender intentSender, Intent intent) {
       if (startIntentSender != null) {
    	    final Object[] args = new Object[5];
    	    args[0] = intentSender;
    	    args[1] = intent;
    	    args[2] = Integer.valueOf(0);
    	    args[3] = Integer.valueOf(0);
    	    args[4] = Integer.valueOf(0);
            try {
            	startIntentSender.invoke(activity, args);
			} catch (Exception e) {
				Log.e(Compatibility.class.getSimpleName(), "startIntentSender", e);
			}
       }
	}
	
	public static boolean isStartIntentSenderSupported() {
		return startIntentSender != null;
	}
}
