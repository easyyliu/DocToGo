package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class patient_check_history extends AppCompatActivity {

    DatabaseHelper dbh;
    private ArrayList<String> dateList = new ArrayList<String>();
    private ArrayList<Integer> doctorIdList = new ArrayList<Integer>();
    private ArrayList<Integer> reportIdList = new ArrayList<Integer>();
    private int[] doctorIdArr;
    private String[] dateArr;
    private int[] reportIdArr;
    private String doctorName;
    private String date;
    private int r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_check_history);

        final SharedPreferences storage = getSharedPreferences("DOCTOGOSESSION", Context.MODE_PRIVATE);
        final int userID = storage.getInt("USERID", 0);
        dbh = new DatabaseHelper(this);
        Cursor c = dbh.viewReportwithPatient(userID);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                reportIdList.add(c.getInt(0));
                doctorIdList.add(c.getInt(2));
                dateList.add(c.getString(3));
            }
            reportIdArr = arrListtoarr(reportIdList);
            doctorIdArr = arrListtoarr(doctorIdList);
            dateArr = arrStringListtoarr(dateList);
            List<HashMap<String, String>> newList = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < reportIdArr.length; i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                //use ID find patient's name
                Cursor u = dbh.getInformationUser(doctorIdArr[i]);
                if (u.getCount() > 0) {
                    while (u.moveToNext()) {
                        doctorName = "Doctor Name: " + u.getString(4) + " " + u.getString(5);
                    }
                }
                date = "Date: " + dateArr[i];
                hm.put("txt1", date);
                hm.put("txt2", doctorName);

                newList.add(hm);
            }
            String[] from = {"txt1", "txt2"};
            int[] to = {R.id.listview_patient_history_date,  R.id.listview_patient_history_docName};
            SimpleAdapter adapter = new SimpleAdapter(this, newList, R.layout.listview_patient_history, from, to);
            final ListView listView = (ListView)findViewById(R.id.patient_history_listview);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    r = reportIdArr[position];
                    Intent i = new Intent(patient_check_history.this, patient_check_history_details.class);
                    i.putExtra("report", r);
                    startActivity(i);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }

}
