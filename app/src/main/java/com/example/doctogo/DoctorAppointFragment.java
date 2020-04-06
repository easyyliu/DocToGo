package com.example.doctogo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorAppointFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorAppointFragment extends Fragment {

    private final ArrayList<Integer> appIdList = new ArrayList<>();
    private final ArrayList<Integer> patiIdList = new ArrayList<>();
    private final ArrayList<String> dateList = new ArrayList<>();
    private int[] appIdArr;
    private String patientNames;
    private int a;


    public DoctorAppointFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment DoctorAppointFragment.
     */
    public static DoctorAppointFragment newInstance() {
        return new DoctorAppointFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_appoint, container, false);
        try{
            final SharedPreferences storage = getContext().getSharedPreferences("DOCTOGOSESSION", Context.MODE_PRIVATE);
            int userID = storage.getInt("USERID",0);
            DatabaseHelper dbh = new DatabaseHelper(getContext());
            Cursor c = dbh.getAppointmentDoctor(userID);
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    appIdList.add(c.getInt(0));
                    patiIdList.add(c.getInt(1));
                    dateList.add(c.getString(3));
                }
                appIdArr = arrListtoarr(appIdList);
                int[] patientsIdArr = arrListtoarr(patiIdList);
                String[] dateArr = arrStringListtoarr(dateList);
                List<HashMap<String, String>> newList = new ArrayList<>();
                for (int i = 0; i < appIdArr.length; i++) {
                    HashMap<String, String> hm = new HashMap<>();
                    //use ID find patient's name
                    Cursor u = dbh.getInformationUser(patientsIdArr[i]);
                    if (u.getCount() > 0) {
                        while (u.moveToNext()) {
                            patientNames = u.getString(4) + " " + u.getString(5);
                        }
                    }
                    hm.put("txt1", patientNames);
                    hm.put("txt2", dateArr[i]);

                    newList.add(hm);
                }
                String[] from = {"txt1", "txt2"};
                int[] to = {R.id.appointment_listview1, R.id.appointment_listview2};
                SimpleAdapter adapter = new SimpleAdapter(getActivity(), newList, R.layout.listview_doctor_appointment, from, to);
                final ListView listView = (ListView) view.findViewById(R.id.AppointmentList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PopupMenu popup = new PopupMenu(getActivity(),listView);
                        popup.inflate(R.menu.popup_menu_doctor_appoint);
                        popup.show();
                        a = appIdArr[position];

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.pop_message:
                                        Intent i = new Intent(getActivity().getApplication(), message.class);
                                        i.putExtra("appointment", a);

                                        startActivity(i);
                                        break;
                                    case R.id.pop_report:
                                        Intent j = new Intent(getActivity().getApplication(), doctor_report.class);
                                        j.putExtra("appointment", a);

                                        startActivity(j);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
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

