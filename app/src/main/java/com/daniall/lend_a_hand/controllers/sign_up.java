package com.daniall.lend_a_hand.controllers;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daniall.lend_a_hand.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import model.User;

public class sign_up extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    EditText edName, edEmail, edUsername, edPassword, edConfirmPassword, edPhoneNumber;
    Button btnSignUp;
    Context context = this;
    ProgressBar progressBar;
    ImageButton imageButtonUploadImage;
    ImageView imageViewSignIn;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    StorageReference rootStorage = FirebaseStorage.getInstance().getReference();
    StorageReference newImageReference;

    ActivityResultLauncher activityResultLauncher;
    Uri filePath;
    User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize();
    }

    private void initialize() {

        newUser = new User();

        edName = findViewById(R.id.editTextName);
        edEmail = findViewById(R.id.editTextEmail);
        edPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        edUsername = findViewById(R.id.editTextUsername);
        edPassword = findViewById(R.id.editTextPassword);
        edConfirmPassword = findViewById(R.id.editTextTextPasswordConfirm);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        imageButtonUploadImage = findViewById(R.id.imageButtonUploadPicture);
        imageButtonUploadImage.setOnClickListener(this);
        imageViewSignIn = findViewById(R.id.imageViewSignIn);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    filePath = result.getData().getData();

                    try {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), filePath);
                        Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                        saveProfilePicture(bitmap);
                    }
                    catch (IOException e) {
                        Toast.makeText(context, "" + e.toString(), Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e) {
                        Toast.makeText(context, "" + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
                createUser();
                break;
            case R.id.imageButtonUploadPicture:
                clickAndSaveProfilePicture();
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

    private void saveProfilePicture(Bitmap bitmap){
        if (filePath != null)
        {
            progressBar.setVisibility(View.VISIBLE);
            newImageReference = rootStorage.child("/images/" + UUID.randomUUID());
            newImageReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Image uploaded successfully", + Toast.LENGTH_LONG).show();
                    newImageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String newImageUrl = task.getResult().toString();
                            newUser.setProfilePicture(newImageUrl);
                            imageViewSignIn.setImageBitmap(bitmap);
                        }
                    });
                }
            });

            newImageReference.putFile(filePath).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    private void clickAndSaveProfilePicture() {

        Intent intent;
        intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent, "Please select a photo"));


    }
}