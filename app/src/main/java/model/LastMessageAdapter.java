package model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daniall.lend_a_hand.R;
import com.daniall.lend_a_hand.controllers.Chat_Page;

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
        oneItem = inflater.inflate(R.layout.one_last_message, parent, false);

        imageMessageReceiver = oneItem.findViewById(R.id.imageMessageReceiver);
        tvMessageReceiverName = oneItem.findViewById(R.id.tvMessageReceiverName);
        tvLastMessage = oneItem.findViewById(R.id.tvLastMessage);

        //Put receiver's profile picture
        if (!chatLog.getUser1().equals(currentUser.getUsername()))
            tvMessageReceiverName.setText(chatLog.getUser1());
        else
            tvMessageReceiverName.setText(chatLog.getUser2());


        if (chatLog.getLastMessage().length() > 35)
        {
            if (chatLog.getMessages().get(chatLog.getMessages().size() - 1).getUsername().equals(currentUser.getUsername()))
            {
                tvLastMessage.setText("You: " + chatLog.getLastMessage().substring(0, 35) + "...");
            }
            else {
                tvLastMessage.setText(chatLog.getLastMessage().substring(0, 35) + "...");
            }
        } else {
            if (chatLog.getMessages().get(chatLog.getMessages().size() - 1).getUsername().equals(currentUser.getUsername()))
            {
                tvLastMessage.setText("You: " + chatLog.getLastMessage());
            }
            else {
                tvLastMessage.setText(chatLog.getLastMessage());
            }

        }








        oneItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat_Page.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("currentChatLog", chatLog);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((Activity)context).finish();
                context.startActivity(intent);
            }
        });

        return oneItem;
    }
}








