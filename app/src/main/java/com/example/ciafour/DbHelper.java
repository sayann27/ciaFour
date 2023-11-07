package com.example.ciafour;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    public static final String COLUMN_NAME = "name";
    private static final String DATABASE_NAME = "GroceryDB";
    private static final int DATABASE_VERSION = 1;

    // Define the table schema and column names

    private static final String COLUMN_ID = "_id";

    public static final String COLUMN_PRICE = "price";
    private static final String TABLE_PRODUCTS = "products";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the expenses table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades, if needed
    }

    // Method to fetch all expenses from the database
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                productList.add(new Product(id, name, price));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
    }


    public long insertValues(String description, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, description);
        values.put(COLUMN_PRICE, amount);

        try {
            // Insert the expense data into the database
            long newRowId = db.insert(TABLE_PRODUCTS, null, values);
            return newRowId;
        } catch (SQLException e) {
            // Handle any database insertion errors here
            e.printStackTrace();
            return -1; // Return -1 to indicate an error
        } finally {
            db.close(); // Close the database connection
        }
    }
}
