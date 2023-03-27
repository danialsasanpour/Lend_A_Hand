package com.daniall.lend_a_hand.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.daniall.lend_a_hand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.widget.TextView;

import model.Location;
import model.Post;
import model.User;

public class post_description extends AppCompatActivity {

    TextView tvDescription, tvLocation, tvDateTimeFrom, tvDateTimeTo;


    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference locations = root.getReference("Location");

    User currentUser;
    Post currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_description);

        initialize();
    }

    private void initialize() {
        tvDescription = findViewById(R.id.tvDescription);
        tvLocation = findViewById(R.id.tvLocation);
        tvDateTimeFrom = findViewById(R.id.tvDateTimeFrom);
        tvDateTimeTo = findViewById(R.id.tvDateTimeTo);

        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");
        currentPost = (Post) getIntent().getExtras().getSerializable("currentPost");

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
    }


}