package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class admin_register extends AppCompatActivity
{
    DatabaseHelper dbh = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__register);

        Button createAccountBtn = findViewById(R.id.btn_AdminRegisterCreate);
        createAccountBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Spinner roleSelectionSpn = findViewById(R.id.spn_AdminRegRole);
                EditText newAccUsername = findViewById(R.id.txt_AdminRegUsername);
                EditText newAccPass = findViewById(R.id.txt_AdminRegPassword);
                EditText newAccEmail = findViewById(R.id.txt_AdminRegEmail);

                //get datas
                int accRole = 0;
                switch ( roleSelectionSpn.getSelectedItem().toString() )
                {
                    case "Admin":
                        accRole = 1;
                        break;
                    case "Patient":
                        accRole = 2;
                        break;
                    case "Doctor":
                        accRole = 3;
                        break;
                    case "Cashier":
                        accRole = 4;
                        break;
                }
                String accName = newAccUsername.getText().toString();
                String accPass = newAccPass.getText().toString();
                String accEmail = newAccEmail.getText().toString();

                //validation: if fields are empty, post toast and do nothing
                if(accName.isEmpty() || accPass.isEmpty() || accEmail.isEmpty())
                {
                    //if one of the req fields are empty, end the op + toast message
                    Toast.makeText(getBaseContext(),"One or more required fields are empty.",Toast.LENGTH_LONG);
                    return;
                }

                //put into db
                if( dbh.adminRegister(accName,accPass,accRole,accEmail) )
                //Display results
                {
                    Toast.makeText(getBaseContext(), "User created.", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                { Toast.makeText(getBaseContext(), "Cannot create user.", Toast.LENGTH_LONG).show(); }

            }
        });
    }
}
