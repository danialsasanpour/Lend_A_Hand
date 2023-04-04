package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daniall.lend_a_hand.R;

import java.util.ArrayList;

public class LastMessageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ChatLog> listOfChatLogs;
    private User currentUser;

    public LastMessageAdapter(Context context, ArrayList<ChatLog> listOfChatLogs, User user) {
        this.context = context;
        this.listOfChatLogs = listOfChatLogs;
        this.currentUser = user;
    }

    @Override
    public int getCount() { return this.listOfChatLogs.size(); }

    @Override
    public Object getItem(int i) { return this.listOfChatLogs.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        ChatLog chatLog = (ChatLog) getItem(i);
        View oneItem;

        ImageView imageMessageReceiver;
        TextView tvMessageReceiverName, tvLastMessage;

        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.one_last_message, parent);

        imageMessageReceiver = oneItem.findViewById(R.id.imageMessageReceiver);
        tvMessageReceiverName = oneItem.findViewById(R.id.tvMessageReceiverName);
        tvLastMessage = oneItem.findViewById(R.id.tvLastMessage);

        //Puts receiver's profile picture
        if (!chatLog.getUser1().equals(currentUser.getUsername()))
            tvMessageReceiverName.setText(chatLog.getUser1());
        else
            tvMessageReceiverName.setText(chatLog.getUser2());

        tvLastMessage.setText(chatLog.getLastMessage());


        return oneItem;
    }
}








