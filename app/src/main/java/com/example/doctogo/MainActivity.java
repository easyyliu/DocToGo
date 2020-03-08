package com.example.doctogo;

/*
    Login functions by saving an eligible user to the storage.
    Then, start new page activity. New activity can then get the username of
    the user, then consult database to acquire data.
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.btn_LogIn);
        loginButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //login function:
                //Check login info
                //Save credentials
                //Start new activity

                //PLACEHOLDER: Popup with 4 functions: admin, patient, doctor, cashier
                Spinner targetRoleSpn = findViewById(R.id.spn_Role);
                int targetRole = targetRoleSpn.getSelectedItemPosition();
                switch(targetRole)
                {
                    case 0:
                        startActivity(new Intent(MainActivity.this,Admin_main.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this,Patient_main.class));
                        break;
                    case 2:
                        //startActivity(new Intent(MainActivity.this,Doctor_main.class));
                        break;
                    case 3:
                        //startActivity(new Intent(MainActivity.this,Cashier_main.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
