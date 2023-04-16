package com.daniall.lend_a_hand.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daniall.lend_a_hand.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.User;

public class ReviewRating extends AppCompatActivity implements View.OnClickListener {

    User byUser;
    User toUser;

    EditText edReview;
    Button btnPost;

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

    public void initialize() {
        byUser = (User) getIntent().getExtras().getSerializable("byUser");
        toUser = (User) getIntent().getExtras().getSerializable("toUser");

        edReview = findViewById(R.id.edReview);

        btnPost = findViewById(R.id.btnPost);
        btnPost.setOnClickListener(this);

        //imageButtonHome = findViewById(R.id.imageButtonHome);
        //imageButtonMessage = findViewById(R.id.imageButtonMessage);
        //imageButtonAccount = findViewById(R.id.imageButtonAccount);

        //imageButtonHome.setOnClickListener(this);
        //imageButtonMessage.setOnClickListener(this);
        //imageButtonAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnPost:
                postReview(view);
                break;
        }
    }

    private void postReview(View view)
    {
        if (edReview.getText().toString() == "")
        {
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }

//        ReviewRating review = new ReviewRating(
//                byUser.getUsername(),
//                toUser.getUsername(),
//                edReview.getText().toString(),
//                // float rating
//        );
//        reviewRatings.child(review.getReviewId()).setValue(review);

    }
}