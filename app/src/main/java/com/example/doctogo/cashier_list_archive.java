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

public class cashier_list_archive extends AppCompatActivity {
    DatabaseHelper dbh;
    private ArrayList<Integer> paymentIdList = new ArrayList<Integer>();
    private ArrayList<Integer> patiIdList = new ArrayList<Integer>();
    private int[] payIdArr;
    private int[] patientsIdArr;
    private TextView title;
    private String patientNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_list_archive);
        dbh = new DatabaseHelper(this);
        Cursor c = dbh.viewPaymentPayedOrNot(" IS NOT NULL ");
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                paymentIdList.add(c.getInt(0));
                patiIdList.add(c.getInt(1));

            }
            payIdArr = arrListtoarr(paymentIdList);
            patientsIdArr = arrListtoarr(patiIdList);
            List<HashMap<String, String>> newList = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < payIdArr.length; i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                //use ID find patient's name
                Cursor u = dbh.getInformationUser(patientsIdArr[i]);
                if (u.getCount() > 0) {
                    while (u.moveToNext()) {
                        patientNames = u.getString(4) + " " + u.getString(5);
                    }
                }
                hm.put("txt1", Integer.toString(payIdArr[i]));
                hm.put("txt2", patientNames);

                newList.add(hm);
            }
            String[] from = {"txt1", "txt2"};
            int[] to = {R.id.cashier_listview1_reportNum, R.id.cashier_listview1_patient};
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), newList, R.layout.cashier_listview1, from, to);
            ListView listView = (ListView) findViewById(R.id.cashier_archive_list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(cashier_list_archive.this, cashier_transactions_archive.class);
                    int p = payIdArr[position];
                    i.putExtra("payment", p);
                    startActivity(i);
                }
            });

        }
        else{
            title = (TextView)findViewById(R.id.cashier_archiveTitle);
            title.setText("Empty, no transaction in Archive");
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
