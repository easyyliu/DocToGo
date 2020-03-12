package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class Admin_AccountsView extends AppCompatActivity
{
    DatabaseHelper dbh = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__accounts_view);

        //TODO: PLACEHOLDER TO VIEW ALL ACCOUNTS (TEXTVIEW)
        TextView plchldr = findViewById(R.id.ADMINVIEWPLACEHOLDER);
        Cursor c = dbh.viewAllAccounts();
        StringBuilder sb = new StringBuilder();
        if(c.getCount()>0)
        {
            sb.append("ID## | Name | Password | Role\n");
            while(c.moveToNext())
            {
                sb.append(c.getInt(0) + " | " +
                        c.getString(1) + " | " +
                        c.getString(2) +  " | " +
                        c.getInt(3)+"\n");
            }
            plchldr.setText(sb.toString());
        }
    }
}
