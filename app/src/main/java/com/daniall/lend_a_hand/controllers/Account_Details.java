package com.daniall.lend_a_hand.controllers;

import androidx.appcompat.app.AppCompatActivity;
import com.daniall.lend_a_hand.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import model.User;

public class Account_Details extends AppCompatActivity implements View.OnClickListener {

    ImageButton imageButtonHome, imageButtonMessage, imageButtonAccount;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        initialize();
    }

    private void initialize() {

        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");

        imageButtonHome = findViewById(R.id.imageButtonHome);
        imageButtonMessage = findViewById(R.id.imageButtonMessage);
        imageButtonAccount = findViewById(R.id.imageButtonAccount);

        imageButtonHome.setOnClickListener(this);
        imageButtonMessage.setOnClickListener(this);
        imageButtonAccount.setOnClickListener(this);
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
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);

    }
}