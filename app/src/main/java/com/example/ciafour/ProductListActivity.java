package com.example.ciafour;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private DbHelper dbHelper;

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
    }
}
