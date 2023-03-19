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

public class sign_up extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    EditText edName, edEmail, edUsername, edPassword, edConfirmPassword, edPhoneNumber;
    Button btnSignUp;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize();
    }

    private void initialize() {

        edName = findViewById(R.id.editTextName);
        edEmail = findViewById(R.id.editTextEmail);
        edPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        edUsername = findViewById(R.id.editTextUsername);
        edPassword = findViewById(R.id.editTextPassword);
        edConfirmPassword = findViewById(R.id.editTextTextPasswordConfirm);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
                createUser();
                break;
        }
    }

    private void createUser()
    {
        if(edEmail.getText().toString().equals("") ||
                edName.getText().toString().equals("") ||
                edUsername.getText().toString().equals("") ||
                edPassword.getText().toString().equals("") ||
                edConfirmPassword.getText().toString().equals("")
        )
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
        else{
            String username = edUsername.getText().toString();
            users.child(username).addListenerForSingleValueEvent(this);
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(!snapshot.exists())
        {
            try {
                User newUser = new User();
                newUser.setName(edName.getText().toString());
                newUser.setEmail(edEmail.getText().toString());
                newUser.setPhoneNumber(edPhoneNumber.getText().toString());
                newUser.setUsername(edUsername.getText().toString());

                if (edPassword.getText().toString().equals(edConfirmPassword.getText().toString()))
                    newUser.setPassword(edPassword.getText().toString());
                else
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();

                users.child(edUsername.getText().toString()).setValue(newUser);
                Toast.makeText(this,"Your account has been created successfully!", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(this, sign_in.class);
                startActivity(i);

            } catch (Exception e) {
                Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "The email or username "
                    + edUsername.getText().toString()
                    + " already exists", Toast.LENGTH_SHORT).show();

            clearWidgets();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    public void clearWidgets(){
        edEmail.setText(null);
        edPhoneNumber.setText(null);
        edUsername.setText(null);
        edPassword.setText(null);
        edConfirmPassword.setText(null);
        edName.requestFocus();
    }

}