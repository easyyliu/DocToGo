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

public class patient_payment extends AppCompatActivity {

    private final ArrayList<String> dueDateList = new ArrayList<>();
    private final ArrayList<Integer> paymentIdList = new ArrayList<>();
    private final ArrayList<Integer> pendingList = new ArrayList<>();
    private final ArrayList<String> transDateList = new ArrayList<>();
    private final ArrayList<Integer> amountList = new ArrayList<>();
    private final ArrayList<Integer> reportIdList = new ArrayList<>();

    private int[] pendingArr;
    private int[] paymentIdArr;
    private String doctorName;
    private int doctorId;
    private int payId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_payment);
        final SharedPreferences storage = getSharedPreferences("DOCTOGOSESSION", Context.MODE_PRIVATE);
        final int userID = storage.getInt("USERID", 0);
        DatabaseHelper dbh = new DatabaseHelper(this);
        Cursor c = dbh.viewPaymentByPatient(userID);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                paymentIdList.add(c.getInt(0));
                dueDateList.add(c.getString(2));
                transDateList.add(c.getString(3));
                amountList.add(c.getInt(4));
                reportIdList.add(c.getInt(5));
                pendingList.add(c.getInt(8));

            }
            paymentIdArr = arrListtoarr(paymentIdList);
            String[] dueDateArr = arrStringListtoarr(dueDateList);
            String[] transDateArr = arrStringListtoarr(transDateList);
            int[] amountArr = arrListtoarr(amountList);
            int[] reportIdArr = arrListtoarr(reportIdList);
            pendingArr = arrListtoarr(pendingList);
            List<HashMap<String, String>> newList = new ArrayList<>();
            for (int i = 0; i < paymentIdArr.length; i++) {
                Cursor z = dbh.viewReport(reportIdArr[i]);
                if (z.getCount() > 0) {
                    while (z.moveToNext()) {
                        doctorId = z.getInt(1);
                    }
                    Cursor y = dbh.getInformationUser(doctorId);
                    if(y.getCount() > 0){
                        while(y.moveToNext()){
                            doctorName = "Doctor Name: " + y.getString(4) + " " + y.getString(5);
                        }
                    }
                }
                String info = doctorName + "\n" + "Due Date: " + dueDateArr[i] + "\n" + "Amount: " + amountArr[i];

                if(transDateArr[i]==null || transDateArr[i].isEmpty()) {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("txt1", info);
                    if (pendingArr[i] == 0) {
                        hm.put("txt2", "New");
                    }else if((pendingArr[i] == 2)){
                        hm.put("txt2", "Credit Card decline, re-enter please");
                    }
                    else {
                        hm.put("txt2", "Processing");
                    }
                    newList.add(hm);
                }
            }
            String[] from = {"txt1", "txt2"};
            int[] to = {R.id.listview_patient_payment_appointInformation,  R.id.listview_patient_payment_pending};
            SimpleAdapter adapter = new SimpleAdapter(this, newList, R.layout.listview_patient_payment, from, to);
            final ListView listView = (ListView)findViewById(R.id.listview_patient_payment);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    payId = paymentIdArr[position];

                    if(pendingArr[position]!=1){
                        Intent i = new Intent(patient_payment.this, patient_creditCard.class);
                        i.putExtra("payment", payId);
                        startActivity(i);

                    }


                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }
    private static int[] arrListtoarr(List<Integer> arrList)
    {
        int[] arr = new int[arrList.size()];
        for (int i=0; i < arr.length; i++)
        {
            arr[i] = arrList.get(i);
        }
        return arr;
    }
    private static String[] arrStringListtoarr(List<String> arrList)
    {
        String[] arr2 = new String[arrList.size()];
        for (int i=0; i < arr2.length; i++)
        {
            arr2[i] = arrList.get(i);
        }
        return arr2;
    }
}



