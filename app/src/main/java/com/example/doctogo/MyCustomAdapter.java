package com.example.doctogo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyCustomAdapter extends BaseAdapter{

    Context context;
    List<String> doctorName = new ArrayList<>();
    List<String> doctorAddress = new ArrayList<>();
    List<String> doctorCity = new ArrayList<>();
    List<Integer> doctorID = new ArrayList<>();
    ArrayList<String>dateList = new ArrayList<String>();
    String[] dateArr;
    String[] dateTemp;
    String[] dateTemp2;
    int userID;
    String dateSelected = "";
    String time;
    DatePickerDialog.OnDateSetListener dListener;
    TimePickerDialog picker;
    String setDate;
    DatabaseHelper dbh;
    int timeNum;
    int[] tempArr;
    String output="";
    int[] timeSet;


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
            convertView = layoutInflater.inflate(R.layout.listview_doctor,parent,false);
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
                                //
                                numberPicker(year, month, dayOfMonth, (int)getItemId(position));
                                /*
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


                                 */
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

    public void numberPicker(int year, int month, int dayOfMonth, final int x){
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.number_timepicker, null);
        final LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.pickerLayout);
        final NumberPicker picker = (NumberPicker) linearLayout.findViewById(R.id.picker);

        month++;
        setDate = year + "-";
        if(month > 9){
            setDate += month + "-";
        }
        else{
            setDate += "0" + month + "-";
        }
        if(dayOfMonth > 9){
            setDate += dayOfMonth;
        }
        else{
            setDate += "0" + dayOfMonth;
        }
        String[] setTempArr = setDate.split("-",3);
        String setTempString = setTempArr[0]+setTempArr[1]+setTempArr[2];
        timeSet = new int[18];
        int a = 0;
        for(int j = 16; j < 34; j++){
            timeSet[a] = j;
            a++;
        }
        dbh = new DatabaseHelper(context);
        Cursor c = dbh.getAppointmentDoctor(doctorID.get(x));
        dateList = new ArrayList<String>();
        if(c.getCount()>0) {
            while (c.moveToNext()) {
                dateList.add(c.getString(3));
            }
            dateArr = arrStringListtoarr(dateList);
            String[] dateTempArr = new String[3];
            String dateTempString;
            for(int i=0; i< dateArr.length; i++){
                dateTemp = dateArr[i].split(" ",2);
                dateTempArr = dateTemp[0].split("-",3);
                dateTempString = dateTempArr[0]+dateTempArr[1]+dateTempArr[2];
                if(Integer.parseInt(setTempString) == Integer.parseInt(dateTempString)){
                    dateTemp2 = dateTemp[1].split(":",2);
                    timeNum = Integer.parseInt(dateTemp2[0])*2;
                    if(Integer.parseInt(dateTemp2[1]) > 1){
                        timeNum++;
                    }
                    a = 0;
                    if(timeSet.length-1 == 0){
                        Toast.makeText(context,"Appointment is full",Toast.LENGTH_SHORT).show();
                    }
                    tempArr = new int[timeSet.length-1];
                    for(int z=0; z<timeSet.length; z++){
                        if(timeSet[z]!=timeNum){
                            tempArr[a] = timeSet[z];
                            a++;
                        }
                    }
                   timeSet = tempArr;
                }
            }
        }
        String[] timeSetString = new String[timeSet.length];
        int temp;
        for(int l = 0; l < timeSet.length; l++) {
            if(timeSet[l] % 2 == 1){
                temp = timeSet[l]/2;
                if(temp > 9){
                    timeSetString[l] = temp + ":30";
                }
                else{
                    timeSetString[l] = "0" + temp + ":30";
                }
            }
            else{
                temp = timeSet[l]/2;
                if(temp > 9){
                    timeSetString[l] = temp + ":00";
                }
                else{
                    timeSetString[l] = "0" + temp + ":00";
                }
            }
        }
        output = "";
        picker.setMinValue(0);
        picker.setMaxValue(timeSet.length-1);
        picker.setDisplayedValues(timeSetString);
        final AlertDialog pickerAlert = new AlertDialog.Builder(context)
                .setPositiveButton("Submit", null)
                .setNegativeButton("Cancel", null)
                .setView(linearLayout)
                .setCancelable(false)
                .create();
        pickerAlert.show();
        pickerAlert.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tempInt = timeSet[Integer.parseInt(String.valueOf(picker.getValue()))];
                if(tempInt/2 <= 9){
                    output = "0";
                }
                if(tempInt%2==1){

                    output += tempInt/2 +":30";
                }
                else{
                    output += tempInt/2 +":00";
                }
                boolean bookAppointment = dbh.bookAppointment(userID, doctorID.get(x), setDate+ " " +output);
                if (bookAppointment) {
                    Toast.makeText(context, "Your appointment is scheduled on " + setDate+ " " +output, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Error to book an appointment ", Toast.LENGTH_SHORT).show();
                }
                pickerAlert.dismiss();;
            }
        });
    }

    public static String[] arrStringListtoarr(List<String> arrList)
    {
        String[] arr2 = new String[arrList.size()];
        for (int i=0; i < arr2.length; i++)
        {
            arr2[i] = arrList.get(i);
        }
        return arr2;
    }

}
