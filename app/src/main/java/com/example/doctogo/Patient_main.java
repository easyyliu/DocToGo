package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Patient_main extends AppCompatActivity {

    PatientInformationFragment updateFragment = PatientInformationFragment.newInstance();
    FragmentManager manager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragMainMenu, updateFragment).commit();

        Button btnLocateDoctor = findViewById(R.id.btnLocateDoctor);
        Button btnPatientPayment = findViewById(R.id.btnPayments);
        Button btnPatientHistory = findViewById(R.id.btncheckhistory);
        Button btnPatientUpdateInfo = findViewById(R.id.btnUpdateInformation);

        btnPatientUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToPatientUpdateInfo = new Intent(Patient_main.this,patientUpdateInformation.class);
                startActivity(goToPatientUpdateInfo);
            }
        });

        btnPatientHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToPatientHistory = new Intent(Patient_main.this,checkHistory.class);
                startActivity(goToPatientHistory);
            }
        });

        btnPatientPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToPatientPayment = new Intent(Patient_main.this,patient_payment.class);
                startActivity(goToPatientPayment);
            }
        });

        btnLocateDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToLocateDoctor = new Intent(Patient_main.this,locate_doctor.class);
                startActivity(goToLocateDoctor);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(updateFragment).commit();
            updateFragment = PatientInformationFragment.newInstance();
            FragmentTransaction trans1 = manager.beginTransaction();
            trans1.add(R.id.fragMainMenu,updateFragment).commit();
    }
}
