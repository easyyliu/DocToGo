package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class cashier_transactions_archive extends AppCompatActivity {
    DatabaseHelper dbh;
    private int transactionId;
    private String transactionDate;
    private int patientId;
    private String patientName;
    private String address;
    private int amount;
    private int msp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_transactions_archive);
        final TextView transactionTxt = (TextView)findViewById(R.id.cashier_archive_transIDTxt);
        final TextView transDateTxt = (TextView)findViewById(R.id.cashier_archive_transDateTxt);
        final TextView patientTxt = (TextView)findViewById(R.id.cashier_archive_patientTxt);
        final TextView addressTxt = (TextView)findViewById(R.id.cashier_archive_addressTxt);
        final TextView amountTxt = (TextView)findViewById(R.id.cashier_archive_amountTxt);
        final TextView mspTxt = (TextView)findViewById(R.id.cashier_archive_mspTxt);
        Intent intent = getIntent();
        if(intent!=null) {
            transactionId = intent.getIntExtra("payment", 0);
            transactionTxt.setText(Integer.toString(transactionId));
            dbh = new DatabaseHelper(this);
            Cursor c = dbh.viewPayment(transactionId);
            if (c.getCount() > 0) {
                while(c.moveToNext()) {
                    patientId = c.getInt(0);
                    transactionDate = c.getString(2);
                    amount = c.getInt(3);
                }
                transDateTxt.setText(transactionDate);
                amountTxt.setText("$"+Integer.toString(amount));
                Cursor u = dbh.getInformationUser(patientId);
                if(u.getCount()>0){
                    while(u.moveToNext()){
                        patientName = u.getString(4) +" "+ u.getString(5);
                        patientTxt.setText(patientName);
                        address = u.getString(6)+", "+u.getString(14);
                        patientTxt.setText(patientName);
                        addressTxt.setText(address);
                        msp = u.getInt(15);
                        if(msp == 0 ){
                            mspTxt.setText("n/a");
                        }
                        else {
                            mspTxt.setText(msp);
                        }
                    }
                }
            }
        }
    }
}
