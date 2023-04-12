package com.daniall.lend_a_hand.controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.daniall.lend_a_hand.R;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import model.ReviewAdapter;
import model.ReviewRating;
import model.User;

public class Account_Details extends AppCompatActivity implements View.OnClickListener, Slider.OnSliderTouchListener {

    ImageButton imageButtonHome, imageButtonMessage, imageButtonAccount;
    ListView lvPosts;
    Button btnPosts, btnLeaveReviewRating, btnCancel, btnEditSave;
    TextView tvUsername, tvRadius;
    Slider sliderRadius;

    User currentUser, recipientUser;
    Double currentRadius;
    ArrayList<ReviewRating> listOfReviewRatings;
    ReviewAdapter adapter;
    Context context = this;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    DatabaseReference reviews = root.getReference("ReviewRatings");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        initialize();
    }

    private void initialize() {

        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");
        recipientUser = (User) getIntent().getExtras().getSerializable("recipientUser");

        lvPosts = findViewById(R.id.lvPosts);
        tvUsername = findViewById(R.id.tvUsername);
        imageButtonHome = findViewById(R.id.imageButtonHome);
        imageButtonMessage = findViewById(R.id.imageButtonMessage);
        imageButtonAccount = findViewById(R.id.imageButtonAccount);
        btnPosts = findViewById(R.id.btnPosts);
        btnLeaveReviewRating = findViewById(R.id.btnLeaveReviewRating);
        sliderRadius = findViewById(R.id.sliderRadius);
        tvRadius = findViewById(R.id.tvRadius);
        btnCancel = findViewById(R.id.btnCancel);
        btnEditSave = findViewById(R.id.btnEditSave);

        imageButtonHome.setOnClickListener(this);
        imageButtonMessage.setOnClickListener(this);
        imageButtonAccount.setOnClickListener(this);
        btnPosts.setOnClickListener(this);
        btnLeaveReviewRating.setOnClickListener(this);
        sliderRadius.addOnSliderTouchListener(this);
        btnCancel.setOnClickListener(this);
        btnEditSave.setOnClickListener(this);


        checkIfIsCurrentUser();

        sliderRadius.setValue((float)currentUser.getRadius());
        tvRadius.setText("Radius: " + currentUser.getRadius() + " km");

        listOfReviewRatings = new ArrayList<ReviewRating>();
        reviews.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ReviewRating reviewRating = snapshot.getValue(ReviewRating.class);
                if (reviewRating.getToUser().equals(tvUsername.getText()))
                {
                    listOfReviewRatings.add(reviewRating);
                    adapter = new ReviewAdapter(context, listOfReviewRatings);
                    lvPosts.setAdapter(adapter);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (ReviewRating oneReview : listOfReviewRatings)
                {
                    if (oneReview.getReviewId().equals(snapshot.child("reviewId").getValue().toString()))
                    {
                        oneReview.setReview(snapshot.child("review").toString());
                        adapter = new ReviewAdapter(context, listOfReviewRatings);
                        lvPosts.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnEditSave)
        {
            if (sliderRadius.isEnabled())
            {
                sliderRadius.setEnabled(false);
                btnCancel.setVisibility(View.GONE);
                btnEditSave.setText("Edit");

                currentUser.setRadius(currentRadius);
                users.child(currentUser.getUsername() + "/radius").setValue(currentUser.getRadius());

            } else {
                sliderRadius.setEnabled(true);
                btnCancel.setVisibility(View.VISIBLE);
                btnEditSave.setText("Save");
            }
            return;
        }

        if (v.getId() == R.id.btnCancel)
        {
            sliderRadius.setEnabled(false);
            btnCancel.setVisibility(View.GONE);
            btnEditSave.setText("Edit");

            return;
        }



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
            case R.id.btnPosts:
                intent = new Intent(this, Posts_Created_By.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("recipientUser", recipientUser);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);

    }

    @Override
    public void onStartTrackingTouch(@NonNull Slider slider) {

    }

    @Override
    public void onStopTrackingTouch(@NonNull Slider slider) {
        currentRadius = Math.round(slider.getValue() * 10.0) / 10.0;
        tvRadius.setText("Radius: " + currentRadius + " km");
    }

    private void checkIfIsCurrentUser(){
        ViewGroup.LayoutParams params = lvPosts.getLayoutParams();
        if (recipientUser != null) {
            tvUsername.setText(recipientUser.getUsername());
            tvRadius.setVisibility(View.GONE);
            sliderRadius.setVisibility(View.GONE);
            btnEditSave.setVisibility(View.GONE);
            params.height = params.height + 200;

        } else {
            tvUsername.setText(currentUser.getUsername());
            btnLeaveReviewRating.setVisibility(View.GONE);
            params.height = params.height + 200;
            sliderRadius.setEnabled(false);
        }
        btnCancel.setVisibility(View.GONE);
        lvPosts.setLayoutParams(params);
        lvPosts.requestLayout();
    }
}