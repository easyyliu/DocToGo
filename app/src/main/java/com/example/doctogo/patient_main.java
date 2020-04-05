package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class patient_main extends AppCompatActivity {

    patientInformationFragment updateFragment = patientInformationFragment.newInstance();
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
        Button btnPatientAppointment = findViewById(R.id.btncheckAppointment);
        Button btnPatientUpdateInfo = findViewById(R.id.btnUpdateInformation);

        btnPatientUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToPatientUpdateInfo = new Intent(patient_main.this,patientUpdateInformation.class);
                startActivity(goToPatientUpdateInfo);
            }
        });

        btnPatientHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToPatientHistory = new Intent(patient_main.this, patient_check_history.class);
                startActivity(goToPatientHistory);
            }
        });

        btnPatientPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToPatientPayment = new Intent(patient_main.this,patient_payment.class);
                startActivity(goToPatientPayment);
            }
        });

        btnPatientAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToPatientAppointment = new Intent(patient_main.this,patient_check_appointment.class);
                startActivity(goToPatientAppointment);
            }
        });



        btnLocateDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToLocateDoctor = new Intent(patient_main.this, patient_locate_doctor.class);
                startActivity(goToLocateDoctor);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(updateFragment).commit();
            updateFragment = patientInformationFragment.newInstance();
            FragmentTransaction trans1 = manager.beginTransaction();
            trans1.add(R.id.fragMainMenu,updateFragment).commit();
    }
}
