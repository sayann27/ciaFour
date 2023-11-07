package com.example.ciafour;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.database.sqlite.SQLiteDatabase;

import java.util.Properties;

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
                sendEmail(passer);
                intent.putExtra("passer", passer);
                startActivity(intent);

                Toast.makeText(ViewOrdersActivity.this, "Order placed and Email sent!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendEmail(String passer) {
        final String to = RegistrationActivity.emailId;


        final String subject = "Order placed";
        final String message = "Your order summary is:\n"+ passer;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Replace these with your email credentials and server settings
                    final String username = "sayanmandal274@gmail.com";
                    final String password = "llhuxdvdxmdsyqsi"; // Use the app password
                    String host = "smtp.gmail.com";
                    int port = 587;

                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", host);
                    props.put("mail.smtp.port", port);

                    Session session = Session.getInstance(props,
                            new javax.mail.Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                                }
                            });

                    Message emailMessage = new MimeMessage(session);
                    emailMessage.setFrom(new InternetAddress(username));
                    emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));// Add BCC recipients
                    emailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(""));
                    emailMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(""));
                    emailMessage.setSubject(subject);
                    emailMessage.setText(message);

                    Transport.send(emailMessage);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (MessagingException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Error sending email. Please check your credentials and try again.", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "An unexpected error occurred. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
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
                    passer = passer.concat("\n" +orders[j] + "\n");
                    if (j == i-1){
                        passer = passer.concat("\n" + "Total: " + amount);
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
