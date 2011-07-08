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

import net.robotmedia.billing.model.Purchase.PurchaseState;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BillingDB {
    static final String DATABASE_NAME = "billing.db";
    static final int DATABASE_VERSION = 1;
    static final String TABLE_PURCHASES = "purchases";

    public static final String COLUMN__ID = "_id";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_PRODUCT_ID = "productId";
    public static final String COLUMN_PURCHASE_TIME = "purchaseTime";
    public static final String COLUMN_DEVELOPER_PAYLOAD = "developerPayload";

    private static final String[] TABLE_PURCHASES_COLUMNS = {
    	COLUMN__ID, COLUMN_PRODUCT_ID, COLUMN_STATE,
    	COLUMN_PURCHASE_TIME, COLUMN_DEVELOPER_PAYLOAD
    };

    SQLiteDatabase mDb;
    private DatabaseHelper mDatabaseHelper;

    public BillingDB(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
        mDb = mDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        mDatabaseHelper.close();
    }

    public void insert(Purchase purchase) {
        ContentValues values = new ContentValues();
        values.put(COLUMN__ID, purchase.orderId);
        values.put(COLUMN_PRODUCT_ID, purchase.productId);
        values.put(COLUMN_STATE, purchase.purchaseState.ordinal());
        values.put(COLUMN_PURCHASE_TIME, purchase.purchaseTime);
        values.put(COLUMN_DEVELOPER_PAYLOAD, purchase.developerPayload);
        mDb.replace(TABLE_PURCHASES, null /* nullColumnHack */, values);
    }
    
    public Cursor queryPurchases() {
        return mDb.query(TABLE_PURCHASES, TABLE_PURCHASES_COLUMNS, null,
                null, null, null, null);
    }
    
    public Cursor queryPurchases(String productId, PurchaseState state) {
        return mDb.query(TABLE_PURCHASES, TABLE_PURCHASES_COLUMNS, COLUMN_PRODUCT_ID + " = ? AND " + COLUMN_STATE + " = ?", 
                new String[] {productId, String.valueOf(state.ordinal())}, null, null, null);
    }
    
    protected static final Purchase createPurchase(Cursor cursor) {
    	final Purchase purchase = new Purchase();
    	purchase.orderId = cursor.getString(0);
    	purchase.productId = cursor.getString(1);
    	purchase.purchaseState = PurchaseState.valueOf(cursor.getInt(2));
    	purchase.purchaseTime = cursor.getLong(3);
    	purchase.developerPayload = cursor.getString(4);
    	return purchase;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createPurchaseTable(db);
        }

        private void createPurchaseTable(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_PURCHASES + "(" +
            		COLUMN__ID + " TEXT PRIMARY KEY, " +
            		COLUMN_PRODUCT_ID + " INTEGER, " +
            		COLUMN_STATE + " TEXT, " +
            		COLUMN_PURCHASE_TIME + " TEXT, " +
            		COLUMN_DEVELOPER_PAYLOAD + " INTEGER)");
        }

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    }
}
