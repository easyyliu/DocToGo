package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class message extends AppCompatActivity {

    DatabaseHelper dbh;
    private ListView listView;
    private ImageView imageView;
    private MessageAdapter adapter;
    private EditText messageEditText;
    private int appointment_ID;
    private int sender_id;
    private String context;
    private int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        SharedPreferences storageIDUser = getApplicationContext().getSharedPreferences("DOCTOGOSESSION", Context.MODE_PRIVATE);
        userID = storageIDUser.getInt("USERID",0);
        imageView = (ImageView) findViewById(R.id.message_sendImg);
        listView = (ListView) findViewById(R.id.message_listview);

        refreshMessageList();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });
        refreshEveryFiveSeconds();
    }

    private void refreshMessageList(){
        adapter = new MessageAdapter(getApplicationContext(), R.layout.single_message);
        listView.setAdapter(adapter);
        messageEditText = (EditText) findViewById(R.id.message_inputText);
        messageEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            appointment_ID = intent.getIntExtra("appointment", 0);
            dbh = new DatabaseHelper(this);
            Cursor c = dbh.viewMessagesByAppointID(appointment_ID);
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    sender_id = c.getInt(1);
                    context = c.getString(2);
                    if(userID == sender_id){
                        adapter.add(new SingleMessage(false, context));
                    }
                    else{
                        adapter.add(new SingleMessage(true, context));
                    }
                }
            }
        }
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(adapter);
    }
    private void refresh(){
        refreshMessageList();
        refreshEveryFiveSeconds();
    }
    private void refreshEveryFiveSeconds(){
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        },5000);
    }
    private boolean sendChatMessage(){
        String cont = messageEditText.getText().toString().trim();
        if(!cont.isEmpty()) {
            dbh = new DatabaseHelper(this);
            dbh.newMessages(userID, cont, appointment_ID);
        }
        messageEditText.setText("");
        refreshMessageList();
        return true;
    }



}
