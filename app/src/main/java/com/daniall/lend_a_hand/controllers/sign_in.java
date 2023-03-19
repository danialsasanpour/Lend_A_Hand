package com.daniall.lend_a_hand.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daniall.lend_a_hand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.User;

public class sign_in extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    EditText edUsername, edPassword;
    Button btnSignUp, btnSignIn;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialize();
    }

    private void initialize()
    {
        edUsername = findViewById(R.id.editTextUsername);
        edPassword = findViewById(R.id.editTextPassword);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
                signUp();
                break;
            case R.id.btnSignIn:
                signIn();
                break;
        }
    }

    private void signIn()
    {
        if(edUsername.getText().toString().equals("") ||
                edPassword.getText().toString().equals("")
        )
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
        else
            users.child(edUsername.getText().toString()).addListenerForSingleValueEvent(this);
    }

    private void signUp() {
        Intent i = new Intent(this, sign_up.class);
        startActivity(i);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists())
        {
            User userFound = snapshot.getValue(User.class);
            if (userFound.getPassword().equals(edPassword.getText().toString()))
            {
                Intent i = new Intent(this, Home.class);
                i.putExtra("currentUser", userFound);
                startActivity(i);
            }
            else
                Toast.makeText(this, "Username not found or password is invalid", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Username not found or password is invalid", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}