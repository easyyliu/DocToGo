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
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorFragment extends Fragment {

    public DoctorFragment() {
        // Required empty public constructor
    }

    public static DoctorFragment newInstance(){
        return new DoctorFragment();
    }

    //Spinner scrollMenu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_doctor, container, false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor, container, false);
        try{
            TextView doctorName = view.findViewById(R.id.doctorName);
            TextView doctorAge = view.findViewById(R.id.doctorAge);
            TextView doctorAddress = view.findViewById(R.id.doctorAddress);
            TextView doctorEmail = view.findViewById(R.id.doctorEmail);
            ImageButton exitButton = view.findViewById(R.id.btnExitAppDoctor);
            //ImageButton menuButton = view.findViewById(R.id.btnMainMenuDoctor);
            //scrollMenu = view.findViewById(R.id.spScrollMenuDoctor);

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
            /*
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
                                    startActivity(new Intent(getContext(), doctor_main.class));
                                    break;
                                case 2:
                                    startActivity(new Intent(getContext(), doctor_report.class));
                                    break;
                                case 3:
//                                    startActivity(new Intent(getContext(),checkHistory.class));
                                    break;
                                case 4:
//                                    startActivity(new Intent(getContext(),patientUpdateInformation.class));
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
            */
            //Query get doctor information
            Cursor getInformation = db.getInformationUser(userID);


            if(getInformation.getCount() == 1){
                //Information is added to text views in the fragment
                getInformation.moveToNext();
                doctorName.setText(doctorName.getText()+" "+getInformation.getString(4) +" " +getInformation.getString(5));
                doctorAge.setText(doctorAge.getText()+" "+getInformation.getInt(12));
                doctorAddress.setText(doctorAddress.getText()+" "+getInformation.getString(6) + ", "+getInformation.getString(14));
                doctorEmail.setText(doctorEmail.getText()+" "+getInformation.getString(7));
            }
        }catch (Exception e){
            Log.e("Query Doctor",e.getMessage());
        }
        return view;

    }




}
