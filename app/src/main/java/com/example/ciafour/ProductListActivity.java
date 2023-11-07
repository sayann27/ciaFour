package com.example.ciafour;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    private Button viewOrdersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        dbHelper = new DbHelper(this);
//        dbHelper.insertValues("Apple", 45);
        List<Product> productList = dbHelper.getAllProducts();



        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        ProductAdapter productAdapter = new ProductAdapter(this, productList);
        productAdapter.populateProductList(linearLayout);
        viewOrdersButton = findViewById(R.id.view_orders_button);
        viewOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start a new activity to view orders
                Intent intent = new Intent(ProductListActivity.this, ViewOrdersActivity.class);
                startActivity(intent);
            }
        });
    }
}
