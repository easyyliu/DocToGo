package com.example.doctogo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyCustomAdapter extends BaseAdapter {

    Context context;
    List<String> doctorName = new ArrayList<>();
    List<String> doctorAddress = new ArrayList<>();
    List<String> doctorCity = new ArrayList<>();
    List<Integer> doctorID = new ArrayList<>();
    int userID;
    String dateSelected = "";
    String time;
    DatePickerDialog.OnDateSetListener dListener;
    TimePickerDialog picker;


    Calendar today = Calendar.getInstance();
    Calendar chosenDate = Calendar.getInstance();

    public MyCustomAdapter(Context context, List<String> doctorName, List<String> doctorAddress,List<String> doctorCity,List<Integer> doctorID,int userID) {
        this.context = context;
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.doctorAddress = doctorAddress;
        this.doctorCity = doctorCity;
        this.userID = userID;
    }

    @Override
    public int getCount() {
        return doctorName.size();
    }

    @Override
    public Object getItem(int position) {
        return doctorName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.listviewdoctor,parent,false);
        }


        final TextView name = convertView.findViewById(R.id.layDoctorName);
        final TextView address = convertView.findViewById(R.id.layDoctorAddress);
        final Button bookAppointment = convertView.findViewById(R.id.btnBookAppointment);

        name.setText("Doctor Name: " + doctorName.get(position));
        address.setText("Address: " + doctorAddress.get(position) + ", " +doctorCity.get(position));


        bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        chosenDate.set(Calendar.YEAR, year);
                        chosenDate.set(Calendar.MONTH, month);
                        chosenDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        if (chosenDate.compareTo(today) <= 0) {
                            Toast.makeText(name.getContext(), "Reservation has to be a future date", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                dateSelected = dateFormat.format(chosenDate.getTime());

                                final Calendar calendar = Calendar.getInstance();
                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                int minutes = calendar.get(Calendar.MINUTE);

                                // time picker dialog
                                picker = new TimePickerDialog(name.getContext(), android.R.style.Theme_Holo_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                                if((sHour < 8) || (sHour >= 17)){
                                                    Toast.makeText(name.getContext(), "Appointments are scheduled from 8:00 am to 4:59 pm ", Toast.LENGTH_LONG).show();
                                                }
                                                else {
                                                    String newHour = sHour+"";
                                                    String newMin = sMinute+"";
                                                    if((sHour >= 0) && (sHour <=9) ){
                                                        newHour = "0" + sHour;
                                                    }
                                                    if((sMinute >= 0) && (sMinute <=9) ){
                                                        newMin = "0" + sMinute;
                                                    }
                                                    time = newHour + ":" + newMin + ":00";
                                                    int currentDoctorPosition = (int) getItemId(position);
                                                    String totalDate = dateSelected + " " + time;
                                                    DatabaseHelper db = new DatabaseHelper(name.getContext());
                                                    //query to book an appointment
                                                    boolean bookAppointment = db.bookAppointment(userID, doctorID.get(currentDoctorPosition), totalDate);
                                                    if (bookAppointment) {
                                                        Toast.makeText(name.getContext(), "Your appointment is scheduled on " + dateSelected + " at " + time, Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(name.getContext(), "Error to book an appointment ", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }, hour, minutes, true);
                                picker.setTitle("Please select time");
                                picker.show();

                            }catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(name.getContext(), dListener, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        return convertView;
    }
}
