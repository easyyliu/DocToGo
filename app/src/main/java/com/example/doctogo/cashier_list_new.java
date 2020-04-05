package com.example.doctogo;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class cashier_list_new extends AppCompatActivity {
    DatabaseHelper dbh;
    private ArrayList<Integer> ReportList = new ArrayList<Integer>();
    private ArrayList<Integer> patiIDList = new ArrayList<Integer>();
    private int[] reportID;
    private int[] patientsId;
    private TextView title;
    private String patientName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_list_new);
        dbh = new DatabaseHelper(this);
        Cursor c = dbh.viewReportWithoutPaymentId(-1);
        if(c.getCount()>0) {
            while (c.moveToNext()) {
                ReportList.add(c.getInt(0));
                patiIDList.add(c.getInt(1));
            }
            reportID = arrListtoarr(ReportList);
            patientsId = arrListtoarr(patiIDList);
            List<HashMap<String, String>> newList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i < reportID.length; i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("txt1", Integer.toString(reportID[i]));
                //use ID find patient's name
                Cursor u = dbh.getInformationUser(patientsId[i]);
                if(u.getCount()>0){
                    while (u.moveToNext()){
                        patientName = u.getString(4) +" "+ u.getString(5);
                    }
                }
                hm.put("txt2", patientName);
                newList.add(hm);
            }
            String[] from = {"txt1", "txt2"};
            int[] to = {R.id.cashier_listview1_reportNum, R.id.cashier_listview1_patient};
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), newList, R.layout.listview_cashier1, from, to);
            ListView listView = (ListView) findViewById(R.id.cashier_newReport_list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(cashier_list_new.this, cashier_transaction_new.class);
                    int r = reportID[position];
                    i.putExtra("report", r);
                    startActivity(i);
                }
            });
        }
        else{
            title = (TextView)findViewById(R.id.cashier_newReportTitle);
            title.setText("Empty, all new transactions have been processed");
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
    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }

}
