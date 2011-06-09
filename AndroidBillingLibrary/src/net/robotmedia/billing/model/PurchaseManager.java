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

package net.robotmedia.billing.model;

import java.util.ArrayList;
import java.util.List;

import net.robotmedia.billing.model.Purchase.PurchaseState;
import android.content.Context;
import android.database.Cursor;

public class PurchaseManager {
	
	public synchronized static void addPurchase(Context context, Purchase purchase) {
		BillingDB db = new BillingDB(context);
		db.insert(purchase);
		db.close();
	}
	
	public synchronized static boolean isPurchased(Context context, String itemId) {
		return countPurchases(context, itemId) > 0;
	}
	
	public synchronized static int countPurchases(Context context, String itemId) {
		BillingDB db = new BillingDB(context);
		final Cursor c = db.queryPurchases(itemId, PurchaseState.PURCHASED);
		int count = 0;
        if (c != null) {
        	count = c.getCount();
        	c.close();
        }
		db.close();
		return count;
	}
	
	public synchronized static List<Purchase> getPurchases(Context context) {
		BillingDB db = new BillingDB(context);
		final Cursor c = db.queryPurchases();
		final List<Purchase> purchases = new ArrayList<Purchase>();
        if (c != null) {
        	while (c.moveToNext()) {
        		final Purchase purchase = BillingDB.createPurchase(c);
        		purchases.add(purchase);
        	}
        	c.close();
        }
		db.close();
		return purchases;
	}
	
}
