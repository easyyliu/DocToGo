package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class patient_locate_doctor extends AppCompatActivity {

    List<String> doctorName = new ArrayList<>();
    List<String> doctorAddress = new ArrayList<>();
    List<String> doctorCity = new ArrayList<>();
    List<Integer> doctorID = new ArrayList<>();

    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_locate_doctor);

        ListView listViewShowDoctor = findViewById(R.id.listViewShowDoctor);


            SharedPreferences storageIDUser = getApplicationContext().getSharedPreferences("DOCTOGOSESSION", Context.MODE_PRIVATE);
            final int userID = storageIDUser.getInt("USERID",0);

            Cursor getInformationUser = db.getInformationUser(userID);
            try{
                getInformationUser.moveToNext();
                if(getInformationUser.getCount()== 1) {
                    Cursor getDoctors = db.searchDoctor(getInformationUser.getString(14));
                    if (getDoctors.getCount() >= 1) {
                        for(int count =0; count < getDoctors.getCount(); count++ ) {
                            getDoctors.moveToNext();
                            doctorID.add(getDoctors.getInt(0));
                            doctorName.add(getDoctors.getString(4) + " " + getDoctors.getString(5));
                            doctorAddress.add(getDoctors.getString(6));
                            doctorCity.add(getDoctors.getString(14));
                        }
                    } else {
                        Toast.makeText(this, "There are not doctors in your area", Toast.LENGTH_LONG).show();
                    }


                }

                MyCustomAdapter adapter = new MyCustomAdapter(this,doctorName,doctorAddress,doctorCity,doctorID,userID);
                listViewShowDoctor.setAdapter(adapter);

            }catch (Exception e){
                        Log.e("QueryGetDoctor",e.getMessage());
                                    }
            }


}
