package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class doctor_report extends AppCompatActivity {
    DoctorFragment updateFragmentDoctor = DoctorFragment.newInstance();
    FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_report);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragDoctor, updateFragmentDoctor).commit();
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
