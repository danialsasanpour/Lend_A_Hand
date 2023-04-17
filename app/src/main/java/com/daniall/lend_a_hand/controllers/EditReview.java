package com.daniall.lend_a_hand.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daniall.lend_a_hand.R;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.ReviewRating;
import model.User;

public class EditReview extends AppCompatActivity implements View.OnClickListener {

    ReviewRating reviewRating;
    User currentUser;
    User toUser;
    TextView tvToUserUsername;

    EditText edReviewMessage;
    Button btnEditReview;
    Button btnDeleteReview;
    Slider sliderRating;

    ImageButton imageButtonHome, imageButtonMessage, imageButtonAccount;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    DatabaseReference reviewRatings = root.getReference("ReviewRatings");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);

        initialize();
    }

    private void initialize()
    {
        reviewRating = (ReviewRating) getIntent().getExtras().getSerializable("reviewRating");
        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");
        toUser = (User) getIntent().getExtras().getSerializable("toUser");

        tvToUserUsername = findViewById(R.id.tvToUserUsername);
        tvToUserUsername.setText(toUser.getUsername());

        edReviewMessage = findViewById(R.id.edReviewMessage);
        edReviewMessage.setText(reviewRating.getReview());

        sliderRating = findViewById(R.id.sliderRating);
        sliderRating.setValue(reviewRating.getRating());

        btnEditReview = findViewById(R.id.btnEditRating);
        btnDeleteReview = findViewById(R.id.btnDeleteRating);
        btnEditReview.setOnClickListener(this);
        btnDeleteReview.setOnClickListener(this);

        imageButtonHome = findViewById(R.id.imageButtonHome);
        imageButtonMessage = findViewById(R.id.imageButtonMessage);
        imageButtonAccount = findViewById(R.id.imageButtonAccount);

        imageButtonHome.setOnClickListener(this);
        imageButtonMessage.setOnClickListener(this);
        imageButtonAccount.setOnClickListener(this);
    }


    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btnEditRating:
                editReview(view);
                break;
            case R.id.btnDeleteRating:
                deleteReview(view);
                break;
        }

        Intent intent = new Intent(this, Home.class);
        switch(view.getId())
        {
            case R.id.imageButtonHome:
                intent = new Intent(this, Home.class);
                intent.putExtra("currentUser", currentUser);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
                break;
            case R.id.imageButtonMessage:
                intent = new Intent(this, Chat_List.class);
                intent.putExtra("currentUser", currentUser);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
                break;
            case R.id.imageButtonAccount:
                intent = new Intent(this, Account_Details.class);
                intent.putExtra("currentUser", currentUser);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
                break;
        }
    }

    private void editReview(View view)
    {
        reviewRating.setReview(edReviewMessage.getText().toString());
        reviewRating.setRating(sliderRating.getValue());
        reviewRatings.child(reviewRating.getReviewId()).setValue(reviewRating);
        finish();
    }

    private void deleteReview(View view)
    {
        reviewRatings.child(reviewRating.getReviewId()).removeValue();
        finish();
    }

}