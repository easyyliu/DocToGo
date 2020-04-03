package com.example.doctogo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends ArrayAdapter {

    private TextView chatText;
    private ArrayList<SingleMessage> chatMessageList = new ArrayList();
    private LinearLayout singleMessageContainer;

    public void add(SingleMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public MessageAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public SingleMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_message, parent, false);
        }
        singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
        SingleMessage chatMessageObj = getItem(position);
        chatText = (TextView) row.findViewById(R.id.singleMessage);
        chatText.setText(chatMessageObj.message);
        singleMessageContainer.setGravity(chatMessageObj.position ? Gravity.LEFT : Gravity.RIGHT);
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}