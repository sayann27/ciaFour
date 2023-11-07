package com.example.ciafour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void populateProductList(LinearLayout linearLayout) {
        for (Product product : productList) {
            View productView = LayoutInflater.from(context).inflate(R.layout.product_item, null);
            TextView productName = productView.findViewById(R.id.product_name);
            TextView productPrice = productView.findViewById(R.id.product_price);

            productName.setText(product.getName());
            productPrice.setText(String.valueOf(product.getPrice()));


            linearLayout.addView(productView);
        }
    }
}
