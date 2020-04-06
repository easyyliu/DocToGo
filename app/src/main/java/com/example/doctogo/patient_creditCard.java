package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class patient_creditCard extends AppCompatActivity {
    private DatabaseHelper dbh;
    private int paymentID;
    private int amount;
    private String dueDate;
    private String creditNum;
    private String expiry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_credit_card);
        TextView amountTXT = (TextView)findViewById(R.id.patient_payment_credit_amountTXT);
        TextView dueDateTXT = (TextView)findViewById(R.id.patient_payment_credit_dueDateTXT);
        final EditText creditTXT = (EditText)findViewById(R.id.patient_payment_credit_numberTXT);
        final EditText expiryTXT = (EditText)findViewById(R.id.patient_payment_credit_expiryTXT);
        Button btn_submit = (Button)findViewById(R.id.btn_credit);

        Intent intent = getIntent();
        if(intent!=null){
            paymentID = intent.getIntExtra("payment",0);
            dbh = new DatabaseHelper(this);
            Cursor c = dbh.viewPayment(paymentID);
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    amount = c.getInt(3);
                    dueDate = c.getString(1);
                }
                amountTXT.setText("$"+ amount);
                dueDateTXT.setText(dueDate);
            }
        }
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creditNum = creditTXT.getText().toString();
                expiry = expiryTXT.getText().toString();
                if(creditNum.length()!= 16){
                    Toast.makeText(getBaseContext(),"Credit Card number should be 12 digits",Toast.LENGTH_SHORT).show();
                }
                else if(expiry.length()!=4){
                    Toast.makeText(getBaseContext(),"Expiry date should be 4 digits",Toast.LENGTH_SHORT).show();
                }
                else {
                    dbh = new DatabaseHelper(getBaseContext());
                    dbh.updatePaymentWithCredit(paymentID, creditNum, expiry);
                    onBackPressed();
                }
            }
        });
    }
}
