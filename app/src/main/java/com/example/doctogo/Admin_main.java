package com.example.doctogo;

/*
    Main menu for admin control.
    Go to make new account for someone
    Go to view all user accounts
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Admin_main extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        //fill header with admin username
        final SharedPreferences storage = getApplicationContext().getSharedPreferences("DOCTOGOSESSION",MODE_PRIVATE);
        TextView usernameDisplay = findViewById(R.id.lbl_AdminUserName);
        usernameDisplay.setText(storage.getString("USERNAME","NOT FOUND"));

        Button toRegister = findViewById(R.id.btn_AdminToRegisterNew);
        Button toViewAccs = findViewById(R.id.btn_AdminToViewAll);
        Button logout = findViewById(R.id.bbtn_AdminLogOut);
        toRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { startActivity(new Intent(Admin_main.this, Admin_Register.class)); }
        });
        toViewAccs.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { startActivity(new Intent(Admin_main.this, Admin_AccountsView.class)); }
        });

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //remove stored data
                SharedPreferences.Editor memoryMaster = storage.edit();
                memoryMaster.remove("USERID");
                memoryMaster.remove("USERNAME");
                memoryMaster.remove("USERROLE");
                //finish activity
                finish();
            }
        });
    }
}
