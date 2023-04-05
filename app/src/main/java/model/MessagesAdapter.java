package model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daniall.lend_a_hand.R;

import java.util.ArrayList;

public class MessagesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Message> listOfMessages;
    private User currentUser;

    public MessagesAdapter(Context context, ArrayList<Message> listOfMessages, User currentUser) {
        this.context = context;
        this.listOfMessages = listOfMessages;
        this.currentUser = currentUser;
    }


    @Override
    public int getCount() { return this.listOfMessages.size(); }

    @Override
    public Object getItem(int position) { return this.listOfMessages.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message message = (Message) getItem(position);
        View oneItem;

        TextView tvMessage;

        LayoutInflater inflater = LayoutInflater.from(context);
        if (message.getUsername().equals(currentUser.getUsername()))
            oneItem = inflater.inflate(R.layout.one_right_message, parent, false);
        else
            oneItem = inflater.inflate(R.layout.one_left_message, parent, false);

        //Put user's profile picture here

        tvMessage = oneItem.findViewById(R.id.tvMessage);
        tvMessage.setText(message.getMessage());


        return oneItem;
    }
}
