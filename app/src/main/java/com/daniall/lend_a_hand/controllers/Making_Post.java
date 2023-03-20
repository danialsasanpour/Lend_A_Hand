package com.daniall.lend_a_hand.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.daniall.lend_a_hand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.Post;
import model.User;

public class Making_Post extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    EditText edDescription, edLocation;
    TextView txtDescription;
    Button btnMakePost;
    User currentUser;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    DatabaseReference posts = root.getReference("Posts");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_post);

        initialize();
    }

    private void initialize()
    {
        txtDescription = findViewById(R.id.edDescription);
        edLocation = findViewById(R.id.edLocation);

        //btnMakePost = findViewById(R.id.btnMakePost);
        btnMakePost.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
//            case R.id.btnMakePost():
//                makePost(view);
//                break;
        }
    }

    private void makePost()
    {
        if(edDescription.getText().toString().equals("") ||
                edLocation.getText().toString().equals("")
        )
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
        else{
            Post post = new Post();
            String uid = post.getPostId();
            posts.child(uid).addListenerForSingleValueEvent(this);
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}