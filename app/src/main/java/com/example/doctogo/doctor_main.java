package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class doctor_main extends AppCompatActivity {

    DoctorFragment updateFragmentDoctor = DoctorFragment.newInstance();
    DoctorAppointFragment updateFragmentAppoint = DoctorAppointFragment.newInstance();
    FragmentManager manager = getSupportFragmentManager();
    FragmentManager manager2 = getSupportFragmentManager();
    TextView column1, column2, column3;
    TableLayout mainTable;
    TableRow row;
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        FragmentTransaction transaction = manager.beginTransaction();
        FragmentTransaction transaction2 = manager2.beginTransaction();
        transaction.add(R.id.fragDoctor, updateFragmentDoctor).commit();
        transaction2.add(R.id.fragAppoint,updateFragmentAppoint).commit();
/*
        mainTable = findViewById(R.id.tableAppointment);
        mainTable.setColumnStretchable(0,true);
        mainTable.setColumnStretchable(1,true);
        mainTable.setColumnStretchable(2,true);

        SharedPreferences storageIDUser = getApplicationContext().getSharedPreferences("DOCTOGOSESSION", Context.MODE_PRIVATE);
        final int doctorID = storageIDUser.getInt("USERID",0);

        Cursor getAppointments = db.getAppointments(doctorID);
        try{
            if(getAppointments.getCount() >= 1) {
                Toast.makeText(this, "There are " + getAppointments.getCount() + " scheduled appointment(s)", Toast.LENGTH_LONG).show();
                    for(int count = 1 ; count <= getAppointments.getCount(); count++) {
                        getAppointments.moveToNext();
                        Cursor getInformationUser = db.getInformationUser(getAppointments.getInt(1));
                        getInformationUser.moveToNext();
                        row = new TableRow(this);
                        column1 = new TextView(this);
                        column2 = new TextView(this);
                        column3 = new TextView(this);
                        column1.setText(Integer.toString(count));
                        column1.setTextSize(18);
                        column1.setGravity(Gravity.CENTER);
                        column1.setTextColor(getResources().getColor(R.color.colorText));
                        column2.setText(getInformationUser.getString(4) +" " +getInformationUser.getString(5));
                        column2.setTextSize(18);
                        column2.setGravity(Gravity.CENTER);
                        column2.setTextColor(getResources().getColor(R.color.colorText));
                        column3.setText(getAppointments.getString(3));
                        column3.setTextSize(18);
                        column3.setGravity(Gravity.CENTER);
                        column3.setTextColor(getResources().getColor(R.color.colorText));
                        row.addView(column1);
                        row.addView(column2);
                        row.addView(column3);
                        mainTable.addView(row);
                    }
            }
            else {
                Toast.makeText(this, "There are not scheduled appointments", Toast.LENGTH_LONG).show();
            }


        }catch (Exception e){
            Log.e("QueryGetDoctor",e.getMessage());
        }
     */

    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentTransaction trans = manager.beginTransaction();
        FragmentTransaction trans1 = manager2.beginTransaction();
        trans.remove(updateFragmentDoctor).commit();
        trans1.remove(updateFragmentAppoint).commit();
            updateFragmentDoctor = DoctorFragment.newInstance();
            updateFragmentAppoint = DoctorAppointFragment.newInstance();
            FragmentTransaction trans2 = manager.beginTransaction();
            FragmentTransaction trans3 = manager.beginTransaction();
            trans2.add(R.id.fragDoctor,updateFragmentDoctor).commit();
            trans3.add(R.id.fragAppoint,updateFragmentAppoint).commit();
    }
}
