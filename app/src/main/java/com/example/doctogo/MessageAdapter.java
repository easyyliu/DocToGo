package com.example.doctogo;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;



class MessageAdapter extends ArrayAdapter {

    private final ArrayList<SingleMessage> messageList = new ArrayList<SingleMessage>();


    public MessageAdapter(Context context, int id) {
        super(context, id);
    }

    public int getCount() {
        return this.messageList.size();
    }

    public SingleMessage getItem(int index) {
        return this.messageList.get(index);
    }

    public void add(SingleMessage singleMessage) {
        messageList.add(singleMessage);
        super.add(singleMessage);
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        SingleMessage singleMessage = getItem(position);
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_message, viewGroup, false);
        }
        TextView text = (TextView) row.findViewById(R.id.singleMessage);
        LinearLayout singleMessageLayout = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
        text.setText(singleMessage.message);
        singleMessageLayout.setGravity(singleMessage.position ? Gravity.START : Gravity.END);
        return row;
    }


}
