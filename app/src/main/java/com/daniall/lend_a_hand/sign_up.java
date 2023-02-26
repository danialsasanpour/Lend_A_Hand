package com.daniall.lend_a_hand;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class sign_up extends AppCompatActivity implements View.OnClickListener {

    EditText edFirstName, edLastName, edEmail, edUsername, edPassword, edConfirmPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize();
    }

    private void initialize() {

        edFirstName = findViewById(R.id.editTextFirstName);
        edLastName = findViewById(R.id.editTextLastName);
        edEmail = findViewById(R.id.editTextEmail);
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
                edFirstName.getText().toString().equals("") ||
                edLastName.getText().toString().equals("") ||
                edUsername.getText().toString().equals("") ||
                edPassword.getText().toString().equals("") ||
                edConfirmPassword.getText().toString().equals("")
        )
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
        else{
            String username = edUsername.getText().toString();
            // Users.child(username).addListenerForSingleValueEvent(this);
        }
    }
}