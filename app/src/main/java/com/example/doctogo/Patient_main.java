package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.view.View;
import android.widget.Button;

public class Patient_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
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
}
