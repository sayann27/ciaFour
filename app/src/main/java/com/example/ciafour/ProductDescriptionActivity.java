package com.example.ciafour;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDescriptionActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    public String[] orderNames = {};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_description);

        dbHelper = new DbHelper(this);

        // Get product name from the intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("product_name");

        // Fetch product details from the database
        Product product = getProductDetails(productName);

        if (product != null) {


            TextView nameView = findViewById(R.id.product_name);
            TextView priceView = findViewById(R.id.product_price);
            TextView descriptionView = findViewById(R.id.product_description);

            // Load the product image using Glide or Picasso


            nameView.setText(product.getName());
            priceView.setText("Price: Rs" + product.getPrice());
            if(product.getName().equals("Banana")){
                descriptionView.setText("It is a fruit loved by monkeys");
            }
            else if(product.getName().equals("Apple")){
                descriptionView.setText("An apple a day keeps the doctor away");
            }


            // Handle the "Add to Cart" button click
            Button addToCartButton = findViewById(R.id.add_to_cart_button);
            addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbHelper.insertOrders(product.getName(), product.getPrice());
                    finish();
                }
            });
        }
    }

    private Product getProductDetails(String productName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DbHelper.COLUMN_NAME,
                DbHelper.COLUMN_PRICE,
        };

        String selection = DbHelper.COLUMN_NAME + " = ?";
        String[] selectionArgs = { productName };

        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE name = ?", new String[]{productName}, null);



        Product product = null;

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            double price = cursor.getDouble(2);
            product = new Product(id, name, price);
            cursor.close();
        }

        return product;
    }
}
