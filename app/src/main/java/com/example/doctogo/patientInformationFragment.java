package com.example.doctogo;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class patientInformationFragment extends Fragment {

    public patientInformationFragment() {
        // Required empty public constructor
    }

    public static patientInformationFragment newInstance(){
        return new patientInformationFragment();
    }

    private Spinner scrollMenu;

    @Override
    public void onPause() {
        //Remove spinner while user goes to another activity
        super.onPause();
        scrollMenu.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_information, container, false);
        try{
        TextView patientName = view.findViewById(R.id.patientName);
        TextView patientAge = view.findViewById(R.id.patientAge);
        TextView patientAddress = view.findViewById(R.id.patientAddress);
        TextView patientEmail = view.findViewById(R.id.patientEmail);
        ImageButton exitButton = view.findViewById(R.id.btnExitAppDoctor);
        ImageButton menuButton = view.findViewById(R.id.btnMainMenuDoctor);
        scrollMenu = view.findViewById(R.id.spScrollMenuDoctor);

        //Create db(databaseHelper) to execute query to get user information
            DatabaseHelper db = new DatabaseHelper(getContext());

        final SharedPreferences storage = getContext().getSharedPreferences("DOCTOGOSESSION", Context.MODE_PRIVATE);
        int userID = storage.getInt("USERID",0);

        //Exit button to log out from application
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove stored data
                SharedPreferences.Editor memoryMaster = storage.edit();
                memoryMaster.remove("USERID");
                //finish activity
                getActivity().finish();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        });

        //Button to display main menu to navigate into patient activities
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollMenu.setVisibility(View.VISIBLE);
                scrollMenu.performClick();

                scrollMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                Toast.makeText(getContext(),"Choose an option",Toast.LENGTH_LONG).show();
                            break;
                            case 1:
                                startActivity(new Intent(getContext(),patient_locate_doctor.class));
                            break;
                            case 2:
                                startActivity(new Intent(getContext(),patient_payment.class));
                            break;
                            case 3:
                                startActivity(new Intent(getContext(), patient_check_history.class));
                            break;
                            case 4:
                                startActivity(new Intent(getContext(),patientUpdateInformation.class));
                            break;
                            default:
                                scrollMenu.setVisibility(View.INVISIBLE);
                            break;
                    }}


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        scrollMenu.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        //Query get user information
        Cursor getInformation = db.getInformationUser(userID);


        if(getInformation.getCount() == 1){
            //Information is added to text views in the fragment
            getInformation.moveToNext();
            patientName.setText(patientName.getText()+" "+getInformation.getString(4) +" " +getInformation.getString(5));
            patientAge.setText(patientAge.getText()+" "+getInformation.getInt(12));
            patientAddress.setText((patientAddress.getText()+" "+getInformation.getString(6)+", "+getInformation.getString(14)));
            patientEmail.setText(patientEmail.getText()+" "+getInformation.getString(7));
        }
        }catch (Exception e){
            Log.e("Query Patient",e.getMessage());
        }
        return view;

    }



}
