package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class patient_check_appointment extends AppCompatActivity {

    DatabaseHelper dbh;
    private String doctorName;
    private String doctorAddress;
    private String doctor;
    private ArrayList<String> doctorAddressList = new ArrayList<String>();
    private ArrayList<Integer> doctorIdList = new ArrayList<Integer>();
    private ArrayList<String> dateList = new ArrayList<String>();
    private ArrayList<Integer> appIdList = new ArrayList<Integer>();
    private int[] appIdArr;
    private int[] doctorIdArr;
    private String[] dateArr;
    private int a;
    TextView doctorNameTXT;

    TextView dateTXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_check_appointment);

        final SharedPreferences storage = getSharedPreferences("DOCTOGOSESSION", Context.MODE_PRIVATE);
        final int userID = storage.getInt("USERID", 0);
        dbh = new DatabaseHelper(this);
        Cursor c = dbh.getAppointmentPatient(userID);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                appIdList.add(c.getInt(0));
                doctorIdList.add(c.getInt(2));
                dateList.add(c.getString(3));
            }
            appIdArr = arrListtoarr(appIdList);
            doctorIdArr = arrListtoarr(doctorIdList);
            dateArr = arrStringListtoarr(dateList);
            List<HashMap<String, String>> newList = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < appIdArr.length; i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                //use ID find patient's name
                Cursor u = dbh.getInformationUser(doctorIdArr[i]);
                if (u.getCount() > 0) {
                    while (u.moveToNext()) {
                        doctorName = "Doctor Name: " + u.getString(4) + " " + u.getString(5);
                        doctorAddress = "Address: " + u.getString(6) + ", " + u.getString(14);
                        doctor = doctorName + "\n" + doctorAddress;
                    }
                }
                hm.put("txt1", doctor);
                hm.put("txt2", dateArr[i]);

                newList.add(hm);
            }
            String[] from = {"txt1", "txt2"};
            int[] to = {R.id.listview_patient_appointment_docinfo,  R.id.listview_patient_appointment_date};
            SimpleAdapter adapter = new SimpleAdapter(this, newList, R.layout.listview_patient_appointment, from, to);
            final ListView listView = (ListView)findViewById(R.id.patient_appointment_list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PopupMenu popup = new PopupMenu(getBaseContext(),listView);
                    popup.inflate(R.menu.popup_menu_patient_appoint);
                    popup.show();

                    a = appIdArr[position];
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.pop_messagebtn:
                                    Intent i = new Intent(patient_check_appointment.this, message.class);
                                    i.putExtra("appointment", a);
                                    startActivity(i);
                                    break;
                                case R.id.pop_cancel:
                                    dbh.cancelAppointment(a);
                                    finish();
                                    startActivity(getIntent());
                                    break;
                                default:
                                    return false;
                            }
                            return true;
                        }
                    });
                }
            });
        }
    }

    public static int[] arrListtoarr(List<Integer> arrList)
    {
        int[] arr = new int[arrList.size()];
        for (int i=0; i < arr.length; i++)
        {
            arr[i] = arrList.get(i).intValue();
        }
        return arr;
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
