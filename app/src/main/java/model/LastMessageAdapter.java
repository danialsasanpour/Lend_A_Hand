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

import androidx.annotation.NonNull;

import com.daniall.lend_a_hand.R;
import com.daniall.lend_a_hand.controllers.Chat_Page;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LastMessageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ChatLog> listOfChatLogs;
    private User currentUser, user;
    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference().child("Users");

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

        imageMessageReceiver = oneItem.findViewById(R.id.imageProfilePicture);
        tvMessageReceiverName = oneItem.findViewById(R.id.tvMessageReceiverName);
        tvLastMessage = oneItem.findViewById(R.id.tvLastMessage);

        //Put receiver's profile picture
        if (!chatLog.getUser1().equals(currentUser.getUsername()))
        {
            tvMessageReceiverName.setText(chatLog.getUser1());
            users.child(chatLog.getUser1()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        user = snapshot.getValue(User.class);
                        if (user.getProfilePicture() != null)
                        {
                            Picasso.with(context).load(user.getProfilePicture()).into(imageMessageReceiver);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            tvMessageReceiverName.setText(chatLog.getUser2());
            users.child(chatLog.getUser2()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        user = snapshot.getValue(User.class);
                        if (user.getProfilePicture() != null)
                        {
                            Picasso.with(context).load(user.getProfilePicture()).into(imageMessageReceiver);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


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
                intent.putExtra("recipientUser", user);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((Activity)context).finish();
                context.startActivity(intent);
            }
        });

        return oneItem;
    }
}








