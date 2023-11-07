package com.example.ciafour;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static final String COLUMN_DESCRIPTION = "description";
    private static final String DATABASE_NAME = "ExpenseTrackerDB";
    private static final int DATABASE_VERSION = 1;

    // Define the table schema and column names
    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_ID = "_id";

    public static final String COLUMN_AMOUNT = "amount";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the expenses table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_AMOUNT + " REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades, if needed
    }

    // Method to fetch all expenses from the database
    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_EXPENSES, null, null, null, null, null, null);
    }

    public long insertExpense(String description, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_AMOUNT, amount);

        try {
            // Insert the expense data into the database
            long newRowId = db.insert(TABLE_EXPENSES, null, values);
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
