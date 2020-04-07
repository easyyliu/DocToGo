package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class patient_check_history_details extends AppCompatActivity {

    private int doctorId;
    private String date;
    private String desc;
    private int paymentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_check_history_details);

        final TextView dateTxt = (TextView)findViewById(R.id.patient_payment_history_dateTXT);
        final TextView docNameTxt = (TextView)findViewById(R.id.patient_payment_history_doctorNameTXT);
        final TextView addressTxt = (TextView)findViewById(R.id.patient_payment_history_addressTXT);
        final TextView descTxt = (TextView)findViewById(R.id.patient_payment_history_descTXT);
        final TextView amountTxt = (TextView)findViewById(R.id.patient_payment_history_finalAmountTXT);

        Intent intent = getIntent();
        if(intent!=null) {
            int reportId = intent.getIntExtra("report", 0);

            DatabaseHelper dbh = new DatabaseHelper(this);
            Cursor c = dbh.viewReport(reportId);
            if (c.getCount() > 0) {
                while(c.moveToNext()) {
                    doctorId = c.getInt(1);
                    date = c.getString(2);
                    desc = c.getString(3);
                    paymentId = c.getInt(4);
                }
                dateTxt.setText(date);
                descTxt.setText(desc);
                Cursor u = dbh.getInformationUser(doctorId);
                if(u.getCount()>0){
                    while(u.moveToNext()){
                        String doctorName = u.getString(4) + " " + u.getString(5);
                        docNameTxt.setText(doctorName);
                        String address = u.getString(6) + ", " + u.getString(14);
                        addressTxt.setText(address);
                    }
                }
                if(paymentId==0){
                    TextView amountToBlank = (TextView)findViewById(R.id.patient_payment_history_finalAmount);
                    amountToBlank.setText("");
                    amountTxt.setText("");
                }
                else {
                    Cursor v = dbh.viewPayment(paymentId);
                    if (v.getCount() > 0) {
                        while (v.moveToNext()) {
                            int amount = v.getInt(3);
                            amountTxt.setText("$" + amount);
                        }
                    }
                }
            }
        }
    }
}
