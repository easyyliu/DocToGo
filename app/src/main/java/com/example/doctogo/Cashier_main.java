package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Cashier_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_main);
        Button btn_newTrans = (Button)findViewById(R.id.btn_cashier_addNewTransaction);
        Button btn_viewTrans = (Button)findViewById(R.id.btn_cashier_viewTransactions);
        Button btn_logout = (Button)findViewById(R.id.btn_cashier_LogOut);
        btn_newTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cashier_main.this, cashier_list_new_report.class));
            }
        });

        btn_viewTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cashier_main.this, cashier_list_unpayed.class));
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cashier_main.this, MainActivity.class));
            }
        });
    }
}
