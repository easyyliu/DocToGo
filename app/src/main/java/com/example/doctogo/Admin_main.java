package com.example.doctogo;

/*
    Main menu for admin control.
    Go to make new account for someone
    Go to view all user accounts
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin_main extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Button toRegister = findViewById(R.id.btn_AdminToRegisterNew);
        Button toViewAccs = findViewById(R.id.btn_AdminToViewAll);
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
    }
}
