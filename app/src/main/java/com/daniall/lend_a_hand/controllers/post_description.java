package com.daniall.lend_a_hand.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.daniall.lend_a_hand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import model.Location;
import model.Post;
import model.User;

public class post_description extends AppCompatActivity implements View.OnClickListener {

    TextView tvDescription, tvLocation, tvDateTimeFrom, tvDateTimeTo;
    Button btnReply;
    ImageButton imageButtonHome, imageButtonMessage, imageButtonAccount;
    ImageView imageProfilePicture;


    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference locations = root.getReference("Location");
    DatabaseReference users = root.getReference("Users");

    User currentUser, user;
    Post currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_description);

        initialize();
    }

    private void initialize() {
        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");
        currentPost = (Post) getIntent().getExtras().getSerializable("currentPost");

        tvDescription = findViewById(R.id.tvDescription);
        tvLocation = findViewById(R.id.tvLocation);
        tvDateTimeFrom = findViewById(R.id.tvDateTimeFrom);
        tvDateTimeTo = findViewById(R.id.tvDateTimeTo);
        btnReply = findViewById(R.id.btnReply);
        imageProfilePicture = findViewById(R.id.imageProfilePicture);

        if (currentUser.getProfilePicture() != null)
        {
            Picasso.with(this).load(currentUser.getProfilePicture()).into(imageProfilePicture);
        }

        imageButtonHome = findViewById(R.id.imageButtonHome);
        imageButtonMessage = findViewById(R.id.imageButtonMessage);
        imageButtonAccount = findViewById(R.id.imageButtonAccount);

        imageButtonHome.setOnClickListener(this);
        imageButtonMessage.setOnClickListener(this);
        imageButtonAccount.setOnClickListener(this);

        btnReply.setOnClickListener(this);

        locations.child(currentPost.getLocation()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Location location = snapshot.getValue(Location.class);
                tvLocation.setText(location.displayLocation());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tvDescription.setText(currentPost.getDescription());
        tvDateTimeFrom.setText(currentPost.getDateTimeFrom());
        tvDateTimeTo.setText(currentPost.getDateTimeTo());

        users.child(currentPost.getCreatedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    user = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            case R.id.btnReply:
                intent = new Intent(this, Chat_Page.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("currentPost", currentPost);
                intent.putExtra("recipientUser", user);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }
}