package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class cashier_transaction_new extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText dueDateTxt;
    int reportNum;
    int patient_ID;
    int doctor_ID;
    int amount_due;
    String due_Date;
    String amount_dueString;
    String Date;
    String desc;
    String patientName;
    String doctorName;
    String address;
    DatabaseHelper dbh;
    java.util.Date due_DateFormat;
    java.util.Date current_DateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_transaction_new);
        final TextView reportIdTxt = (TextView)findViewById(R.id.cashier_new_ReportnumTxt);
        final TextView dateTxt = (TextView)findViewById(R.id.cashier_new_CaseDateTxt);
        final TextView patientTxt = (TextView)findViewById(R.id.cashier_new_patientTxt);
        final TextView doctorTxt = (TextView)findViewById(R.id.cashier_new_doctorTxt);
        final TextView caseDescTxt = (TextView)findViewById(R.id.cashier_patient_desc);
        final TextView addressTxt = (TextView)findViewById(R.id.cashier_patient_address);
        final EditText AmountTxt = (EditText)findViewById(R.id.cashier_new_patient_payment_amount);
        dueDateTxt = (EditText)findViewById(R.id.cashier_new_trans_duedate);
        dueDateTxt.setInputType(InputType.TYPE_NULL);
        Button btndone = (Button)findViewById(R.id.cashier_new_btn_done);
        Button btncancel = (Button)findViewById(R.id.cashier_new_btn_cancel);
        Intent intent = getIntent();
        if(intent!=null){
            reportNum = intent.getIntExtra("report",0);
            dbh = new DatabaseHelper(this);
            Cursor c = dbh.viewReport(reportNum);
            if(c.getCount()>0) {
                while(c.moveToNext()) {
                    patient_ID = c.getInt(0);
                    doctor_ID = c.getInt(1);
                    Date = c.getString(2);
                    desc = c.getString(3);
                }
                dateTxt.setText(Date);
                caseDescTxt.setText(desc);
                reportIdTxt.setText(Integer.toString(reportNum));
            }
            Cursor u = dbh.getInformationUser(patient_ID);
            if(u.getCount()>0){
                while(u.moveToNext()){
                    patientName = u.getString(4) +" "+ u.getString(5);
                    address = u.getString(6)+", "+u.getString(14);
                    patientTxt.setText(patientName);
                    addressTxt.setText(address);
                }
            }
            Cursor v = dbh.getInformationUser(doctor_ID);
            if(v.getCount()>0){
                while(v.moveToNext()){
                    doctorName = v.getString(4) +" "+ v.getString(5);
                    doctorTxt.setText(doctorName);
                }
            }

        }
        dueDateTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    pickDate();
                }
            }
        });
        dueDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_dueString = AmountTxt.getText().toString();
                due_Date = dueDateTxt.getText().toString();
                if(amount_dueString.matches("")){
                    Toast.makeText(getBaseContext(),"please fill amount input",Toast.LENGTH_SHORT).show();
                }
                else {
                    amount_due = Integer.parseInt(amount_dueString);
                    if(amount_due < 0) {
                        Toast.makeText(getBaseContext(), "please fill a valid amount input", Toast.LENGTH_SHORT).show();
                    }
                    else if((due_Date.matches(""))||(due_Date.matches("Click to enter"))){
                        Toast.makeText(getBaseContext(),"please select the due date",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                        try {
                            due_DateFormat =  dateFormat.parse(due_Date);
                            current_DateFormat =  Calendar.getInstance().getTime();
                            if(!(due_DateFormat.compareTo(current_DateFormat)>0)){
                                Toast.makeText(getBaseContext(),"please select the a due date later than today",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                long pId = dbh.paymentInsert(patient_ID,due_Date,amount_due,reportNum);
                                int paymentID = (int)pId;
                                dbh.updateReportWithPaymentID(paymentID,reportNum);
                                onBackPressed();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }

    private void pickDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String output = month+1 + "-" + dayOfMonth + "-" + year;
        dueDateTxt.setText(output);
    }
}
