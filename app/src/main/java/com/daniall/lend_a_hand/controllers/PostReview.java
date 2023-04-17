package com.daniall.lend_a_hand.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daniall.lend_a_hand.R;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.User;
import model.ReviewRating;

public class PostReview extends AppCompatActivity implements View.OnClickListener {

    User byUser;
    User toUser;

    EditText edReviewMessage;
    Button btnPostReview;
    Slider sliderRating;

    ImageButton imageButtonHome, imageButtonMessage, imageButtonAccount;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    DatabaseReference reviewRatings = root.getReference("ReviewRatings");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_rating);

        initialize();
    }

    private void initialize()
    {
        byUser = (User) getIntent().getExtras().getSerializable("currentUser");
        toUser = (User) getIntent().getExtras().getSerializable("toUser");

        edReviewMessage = findViewById(R.id.edReviewMessage);
        sliderRating = findViewById(R.id.sliderRating);

        btnPostReview = findViewById(R.id.btnPostRating);
        btnPostReview.setOnClickListener(this);

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
            case R.id.btnPostRating:
                postReview();
                break;
        }
    }

    private void postReview()
    {
        if (edReviewMessage.getText().toString() == "")
        {
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }

        model.ReviewRating reviewRating = new ReviewRating(
                byUser.getUsername(),
                toUser.getUsername(),
                edReviewMessage.getText().toString(),
                sliderRating.getValue()
        );
        reviewRatings.child(reviewRating.getReviewId()).setValue(reviewRating);

    }
}