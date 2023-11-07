package com.example.ciafour;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;


public class OrderSummaryActivity extends Activity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        textView = findViewById(R.id.textViewSummary);
        Intent intent = getIntent();
        String passer = intent.getStringExtra("passer");
        textView.setText(passer);

    }


}
