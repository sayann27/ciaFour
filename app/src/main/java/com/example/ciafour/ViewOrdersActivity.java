package com.example.ciafour;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ViewOrdersActivity extends Activity {
    private ListView orderListView;
    private Button confirmOrderButton;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        orderListView = findViewById(R.id.orderListView);
        confirmOrderButton = findViewById(R.id.confirmOrderButton);
        dbHelper = new DbHelper(this);

        // Fetch and display orders from the "orders" table
        displayOrders();

        // Handle the "Confirm Order" button click event
        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewOrdersActivity.this, OrderSummaryActivity.class);
                String passer = OrderSummary();
                intent.putExtra("passer", passer);
                startActivity(intent);

                Toast.makeText(ViewOrdersActivity.this, "Order confirmed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String OrderSummary(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Query the database to retrieve orders (simplified example)
        String query = "SELECT * FROM orders";
        Cursor cursor = db.rawQuery(query, null);
        String passer = "";
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String[] orders = new String[cursor.getCount()];
                int i = 0;
                double amount = 0;
                do {
                    // Extract order information from the cursor
                    String name = cursor.getString(0);
                    String price = cursor.getString(1);

                    // Create an order summary for display
                    String orderSummary = "Name: " + name + "\nPrice: " + price;
                    amount += Double.parseDouble(price);
                    orders[i] = orderSummary;
                    i++;
                } while (cursor.moveToNext());

                for(int j = 0;j<i;j++){
                    passer.concat("\n" +orders[j] + "\n");
                    if (j == i-1){
                        passer.concat("\n" + "Total: " + amount);
                    }
                }

            }

            cursor.close();

        }
        return passer;
    }
    private void displayOrders() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query the database to retrieve orders (simplified example)
        String query = "SELECT * FROM orders";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String[] orders = new String[cursor.getCount()];
                int i = 0;

                do {
                    // Extract order information from the cursor
                    String name = cursor.getString(0);
                    String price = cursor.getString(1);

                    // Create an order summary for display
                    String orderSummary = "Name: " + name + "\nPrice: " + price;
                    orders[i] = orderSummary;
                    i++;
                } while (cursor.moveToNext());

                // Use an ArrayAdapter to display the orders in the ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orders);
                orderListView.setAdapter(adapter);
            }

            cursor.close();
        }
    }
}
