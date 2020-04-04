package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class doctor_report extends AppCompatActivity {
    DoctorFragment updateFragmentDoctor = DoctorFragment.newInstance();
    FragmentManager manager = getSupportFragmentManager();
    DatabaseHelper dbh;
    private int appointment_ID;
    private int patient_ID;
    private String age;
    private String patientName;
    private String dateString;
    private String dateReport;
    private String desc;
    private int doctorID;
    EditText descEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_report);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragDoctor, updateFragmentDoctor).commit();

        TextView patientNameTxt = (TextView)findViewById(R.id.doctor_report_patientName);
        TextView ageTxt = (TextView)findViewById(R.id.doctor_report_age);
        TextView dateTxt = (TextView)findViewById(R.id.doctor_report_date);
        descEdit = (EditText)findViewById(R.id.doctor_report_descEdit);
        Button btn_submit = (Button)findViewById(R.id.btn_doctor_report_submit);
        SharedPreferences storageIDUser = getApplicationContext().getSharedPreferences("DOCTOGOSESSION", Context.MODE_PRIVATE);
        doctorID = storageIDUser.getInt("USERID",0);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        dateReport = df.format(date);
        dateString = dateTxt.getText().toString() +" "+ df.format(date);
        Intent intent = getIntent();
        if (intent != null) {
            appointment_ID = intent.getIntExtra("appointment", 0);
            dbh = new DatabaseHelper(this);
            Cursor c = dbh.getAppointmentByID(appointment_ID);
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    patient_ID = c.getInt(1);
                }
            }
            Cursor u = dbh.getInformationUser(patient_ID);
            if (u.getCount() > 0) {
                while (u.moveToNext()) {
                    patientName = patientNameTxt.getText().toString() +" "+ u.getString(4) +" "+ u.getString(5);
                    age = ageTxt.getText().toString() +" "+u.getInt(12);
                }
            }
        }
        patientNameTxt.setText(patientName);
        ageTxt.setText(age);
        dateTxt.setText(dateString);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desc = descEdit.getText().toString();
                long reportID = dbh.reportInsert(patient_ID, doctorID, dateReport, desc, appointment_ID);
                dbh.setReportIDtoAppointment(appointment_ID,(int)reportID);
                dbh.messagesDel(appointment_ID);
                onBackPressed();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(updateFragmentDoctor).commit();
        updateFragmentDoctor = DoctorFragment.newInstance();
        FragmentTransaction trans1 = manager.beginTransaction();
        trans1.add(R.id.fragDoctor,updateFragmentDoctor).commit();
    }

}
