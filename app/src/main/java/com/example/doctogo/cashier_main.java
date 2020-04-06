package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class cashier_main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_main);
        TextView name_cashier = (TextView)findViewById(R.id.name_cashier);
        Button btn_newTrans = (Button)findViewById(R.id.btn_cashier_addNewTransaction);
        Button btn_viewTrans = (Button)findViewById(R.id.btn_cashier_viewTransactions);
        Button btn_archTrans = (Button)findViewById(R.id.btn_cashier_archiveTransaction);
        Button btn_logout = (Button)findViewById(R.id.btn_cashier_LogOut);
        final SharedPreferences storage = getSharedPreferences("DOCTOGOSESSION", Context.MODE_PRIVATE);
        int userID = storage.getInt("USERID",0);
        DatabaseHelper dbh = new DatabaseHelper(this);
        Cursor c = dbh.getInformationUser(userID);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String cashier_name = "Hi, " + c.getString(4) + " " + c.getString(5);
                name_cashier.setText(cashier_name);
            }
        }
        btn_newTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(cashier_main.this, cashier_list_new.class));
            }
        });

        btn_viewTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(cashier_main.this, cashier_list_view.class));
            }
        });
        btn_archTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(cashier_main.this, cashier_list_archive.class));
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(cashier_main.this, MainActivity.class));
            }
        });
    }
}
