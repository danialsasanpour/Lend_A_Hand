package com.daniall.lend_a_hand.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daniall.lend_a_hand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import model.OnePostCreatedByAdapter;
import model.Post;
import model.User;

public class Posts_Created_By extends AppCompatActivity implements View.OnClickListener {

    TextView tvPostsCreatedBy;
    ListView lvPosts;
    ImageButton imageButtonAdd, imageButtonHome, imageButtonMessage, imageButtonAccount;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference posts = root.getReference("Posts");

    User currentUser, recipientUser;
    ArrayList<Post> listOfPosts;
    OnePostCreatedByAdapter adapter;
    Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_created_by);

        initialize();
    }

    private void initialize() {
        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");
        recipientUser = (User) getIntent().getExtras().getSerializable("recipientUser");

        tvPostsCreatedBy = findViewById(R.id.tvPostsCreatedBy);
        lvPosts = findViewById(R.id.lvPosts);
        imageButtonAdd = findViewById(R.id.imageButtonAdd);
        imageButtonHome = findViewById(R.id.imageButtonHome);
        imageButtonMessage = findViewById(R.id.imageButtonMessage);
        imageButtonAccount = findViewById(R.id.imageButtonAccount);

        imageButtonAdd.setOnClickListener(this);
        imageButtonHome.setOnClickListener(this);
        imageButtonMessage.setOnClickListener(this);
        imageButtonAccount.setOnClickListener(this);
        tvPostsCreatedBy.setText("Posts created by: ");
        listOfPosts = new ArrayList<Post>();

        if (recipientUser != null) {
            tvPostsCreatedBy.setText(tvPostsCreatedBy.getText() + recipientUser.getUsername());
            imageButtonAdd.setVisibility(View.GONE);
            posts.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        if (recipientUser.getUsername().equals(ds.child("createdBy").getValue().toString()))
                        {
                            Post post = ds.getValue(Post.class);
                            listOfPosts.add(post);
                            adapter = new OnePostCreatedByAdapter(context, listOfPosts, currentUser);
                            lvPosts.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            tvPostsCreatedBy.setText(tvPostsCreatedBy.getText() + currentUser.getUsername());
            posts.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        if (currentUser.getUsername().equals(ds.child("createdBy").getValue().toString()))
                        {
                            Post post = ds.getValue(Post.class);
                            listOfPosts.add(post);
                            adapter = new OnePostCreatedByAdapter(context, listOfPosts, currentUser);
                            lvPosts.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }





    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, Home.class);
        switch(v.getId())
        {
            case R.id.imageButtonHome:
                intent = new Intent(this, Home.class);
                intent.putExtra("currentUser", currentUser);
                break;
            case R.id.imageButtonMessage:
                intent = new Intent(this, Chat_List.class);
                intent.putExtra("currentUser", currentUser);
                break;
            case R.id.imageButtonAccount:
                intent = new Intent(this, Account_Details.class);
                intent.putExtra("currentUser", currentUser);
                break;
            case R.id.imageButtonAdd:
                intent = new Intent(this, Making_Post.class);
                intent.putExtra("currentUser", currentUser);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }
}