package com.daniall.lend_a_hand.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.daniall.lend_a_hand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.ChatLog;
import model.LastMessageAdapter;
import model.Message;
import model.User;

public class Chat_List extends AppCompatActivity {

    ListView lvChatList;

    User currentUser;
    ArrayList<ChatLog> listOfChatLogs;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    DatabaseReference chatLogs = root.getReference("ChatLogs");

    Context context = this;
    LastMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        initialize();
    }

    private void initialize() {

        lvChatList = findViewById(R.id.lvChatList);
        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");

        listOfChatLogs = new ArrayList<ChatLog>();

        users.child(currentUser.getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.child("listOfChatLogs").getChildren())
                {
                    String chatLogId = ds.getValue().toString();
                    chatLogs.child(chatLogId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                ChatLog chatLog = new ChatLog();
                                chatLog.setChatId(snapshot.child("chatId").getValue().toString());
                                chatLog.setUser1(snapshot.child("user1").getValue().toString());
                                chatLog.setUser2(snapshot.child("user2").getValue().toString());
                                chatLog.setLastMessage(snapshot.child("lastMessage").getValue().toString());
                                chatLog.setInterestedPost(snapshot.child("interestedPost").getValue().toString());

                                ArrayList<Message> listOfMessages = new ArrayList<Message>();
                                for (DataSnapshot oneMessage : snapshot.child("messages").getChildren())
                                {
                                    Message message = new Message();
                                    message.setMessage(oneMessage.child("message").getValue().toString());
                                    message.setUsername(oneMessage.child("username").getValue().toString());
                                    message.setTimeSent(Long.parseLong(oneMessage.child("timeSent").getValue().toString()));
                                    listOfMessages.add(message);
                                }



                                chatLog.setMessages(listOfMessages);
                                listOfChatLogs.add(chatLog);
                                adapter = new LastMessageAdapter(context, listOfChatLogs, currentUser);
                                lvChatList.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}