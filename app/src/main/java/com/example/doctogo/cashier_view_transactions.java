package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class cashier_view_transactions extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    DatabaseHelper dbh;
    private int transId;
    private int patientId;
    private String due_Date;
    private String datePicker;
    private int amount;
    private String patientName;
    private int tempAmount;
    private Date tempDate;
    private Date currDate;
    private int selectOptionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_view_transactions);
        DatabaseHelper dbh = new DatabaseHelper(this);
        final TextView transNumberTxt = (TextView)findViewById(R.id.cashier_view_TranscationNumberTxt);
        final TextView dueTxt = (TextView)findViewById(R.id.cashier_view_dueDateTxt);
        final TextView patientTxt = (TextView)findViewById(R.id.cashier_view_patientNameTxt);
        final TextView amountTxt = (TextView)findViewById(R.id.cashier_view_amountTxt);
        Button btn_Edit = (Button)findViewById(R.id.cashier_view_Edit);
        Button btn_reminder = (Button)findViewById(R.id.cashier_view_send_Reminder);
        Intent intent = getIntent();
        if(intent!=null) {
            transId = intent.getIntExtra("payment", 0);
            dbh = new DatabaseHelper(this);
            Cursor c = dbh.viewPayment(transId);
            if (c.getCount() > 0) {
                while(c.moveToNext()) {
                    patientId = c.getInt(0);
                    due_Date = c.getString(1);
                    amount = c.getInt(3);
                }
                transNumberTxt.setText(Integer.toString(patientId));
                dueTxt.setText(due_Date);
                amountTxt.setText("$"+Integer.toString(amount));
                Cursor u = dbh.getInformationUser(patientId);
                if(u.getCount()>0){
                    while(u.moveToNext()){
                        patientName = u.getString(4) +" "+ u.getString(5);
                        patientTxt.setText(patientName);
                    }
                }
            }
        }
    }
    public void Popup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.popup_menu);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.pop_due_date:
                        selectOptionNumber = 1;
                        pickDate();
                        break;
                    case R.id.pop_amount:
                        editAmount();
                        break;
                    case R.id.pop_trans_date:
                        selectOptionNumber = 3;
                        pickDate();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        datePicker = month+1 + "-" + dayOfMonth + "-" + year;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        try {
            if(selectOptionNumber == 1) {
                tempDate = dateFormat.parse(datePicker);
                currDate = Calendar.getInstance().getTime();
                if (!(tempDate.compareTo(currDate) > 0)) {
                    Toast.makeText(getBaseContext(), "please select the a due date later than today", Toast.LENGTH_LONG).show();
                } else {
                    dbh = new DatabaseHelper(this);
                    dbh.updatePaymentWithdueDate(transId, datePicker);
                    finish();
                    startActivity(getIntent());
                }
            }
            else if(selectOptionNumber == 3){
                tempDate = dateFormat.parse(datePicker);
                currDate = Calendar.getInstance().getTime();
                if (tempDate.compareTo(currDate) > 0) {
                    Toast.makeText(getBaseContext(), "please select the a due date not later than today", Toast.LENGTH_LONG).show();
                } else {
                    dbh = new DatabaseHelper(this);
                    dbh.updatePaymentWithtransDate(transId, datePicker);
                    onBackPressed();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    private void pickDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (DatePickerDialog.OnDateSetListener) this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private void editAmount(){
        dbh = new DatabaseHelper(this);
        AlertDialog.Builder editlog = new AlertDialog.Builder(cashier_view_transactions.this);
        editlog.setTitle("Enter new Amount");
        final EditText amountInput = new EditText(cashier_view_transactions.this);
        amountInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        editlog.setView(amountInput);
        editlog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempAmount = Integer.parseInt(amountInput.getText().toString());
                if(tempAmount >= 0){
                    dbh.updatePaymentWithAmount(transId,tempAmount);
                    finish();
                    startActivity(getIntent());
                }
                else{
                    Toast.makeText(cashier_view_transactions.this,"Enter valid amount", Toast.LENGTH_LONG).show();
                }
            }
        });
        editlog.show();

    }
}
