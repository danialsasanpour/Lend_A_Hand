package com.daniall.lend_a_hand.controllers;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.daniall.lend_a_hand.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import model.ReviewAdapter;
import model.ReviewRating;
import model.User;

public class Account_Details extends AppCompatActivity implements View.OnClickListener, Slider.OnSliderTouchListener, ValueEventListener {

    ImageButton imageButtonHome, imageButtonMessage, imageButtonAccount, imageButtonFilter;
    ListView lvPosts;
    Button btnPosts, btnLeaveReviewRating, btnCancel, btnEditSave;
    TextView tvUsername, tvRadius, tvRate;
    Slider sliderRadius;
    ImageView imageProfilePicture;

    Boolean imageButtonFilterClicked = false;
    User currentUser, recipientUser;
    Double currentRadius;
    ArrayList<ReviewRating> listOfReviewRatings, listOfRemovedReviewRatings;
    float totalrating = 0;
    ReviewAdapter adapter;
    Context context = this;

    ActivityResultLauncher activityResultLauncher;
    Uri filePath;
    StorageReference newImageReference;


    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    DatabaseReference reviews = root.getReference("ReviewRatings");
    StorageReference rootStorage = FirebaseStorage.getInstance().getReference();

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
        tvRate = findViewById(R.id.tvRate);
        imageButtonHome = findViewById(R.id.imageButtonHome);
        imageButtonMessage = findViewById(R.id.imageButtonMessage);
        imageButtonAccount = findViewById(R.id.imageButtonAccount);
        imageButtonFilter = findViewById(R.id.imageButtonFilter);
        btnPosts = findViewById(R.id.btnPosts);
        btnLeaveReviewRating = findViewById(R.id.btnLeaveReviewRating);
        sliderRadius = findViewById(R.id.sliderRadius);
        tvRadius = findViewById(R.id.tvRadius);
        btnCancel = findViewById(R.id.btnCancel);
        btnEditSave = findViewById(R.id.btnEditSave);
        imageProfilePicture = findViewById(R.id.imageProfilePicture);
        imageProfilePicture.setOnClickListener(this);

        imageButtonHome.setOnClickListener(this);
        imageButtonMessage.setOnClickListener(this);
        imageButtonAccount.setOnClickListener(this);
        imageButtonFilter.setOnClickListener(this);
        btnPosts.setOnClickListener(this);
        btnLeaveReviewRating.setOnClickListener(this);
        sliderRadius.addOnSliderTouchListener(this);
        btnCancel.setOnClickListener(this);
        btnEditSave.setOnClickListener(this);

        if (recipientUser != null)
        {
            if (recipientUser.getProfilePicture() != null)
                Picasso.with(this).load(recipientUser.getProfilePicture()).into(imageProfilePicture);
        }
        else {
            if (currentUser.getProfilePicture() != null)
                Picasso.with(this).load(currentUser.getProfilePicture()).into(imageProfilePicture);
        }


        checkIfIsCurrentUser();

        sliderRadius.setValue((float)currentUser.getRadius());
        tvRadius.setText("Radius: " + currentUser.getRadius() + " km");


        reviews.addValueEventListener(this);

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

        if (v.getId() == R.id.imageButtonFilter)
        {
            imageButtonFilterClicked = !imageButtonFilterClicked;
            reviews.addValueEventListener(this);
            return;
        }

        if (v.getId() == R.id.imageProfilePicture)
        {
            if (recipientUser == null)
            {
                clickAndSaveProfilePicture();
            }

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
            imageButtonFilter.setVisibility(View.GONE);
        }
        btnCancel.setVisibility(View.GONE);
        lvPosts.setLayoutParams(params);
        lvPosts.requestLayout();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        {
            listOfReviewRatings = new ArrayList<ReviewRating>();
            for (DataSnapshot ds : snapshot.getChildren())
            {

                ReviewRating reviewRating = ds.getValue(ReviewRating.class);

                totalrating = 0;
                if (reviewRating.getToUser().equals(tvUsername.getText()))
                {

                    listOfReviewRatings.add(reviewRating);
                    if (imageButtonFilterClicked && !reviewRating.getByUser().equals(currentUser.getUsername()))
                        listOfReviewRatings.remove(reviewRating);
                    adapter = new ReviewAdapter(context, listOfReviewRatings);
                    lvPosts.setAdapter(adapter);

                    for (ReviewRating oneReview : listOfReviewRatings)
                        totalrating += oneReview.getRating();

                }
                tvRate.setText((Math.round(totalrating/listOfReviewRatings.size()*10.0))/10.0 + "/5.0");

            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    private void saveProfilePicture(Bitmap bitmap){
        if (filePath != null)
        {
            newImageReference = rootStorage.child("/images/" + UUID.randomUUID());
            newImageReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(context, "Image uploaded successfully", + Toast.LENGTH_LONG).show();
                    newImageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String newImageUrl = task.getResult().toString();
                            currentUser.setProfilePicture(newImageUrl);
                            users.child(currentUser.getUsername() + "/profilePicture").setValue(currentUser.getProfilePicture());
                            imageProfilePicture.setImageBitmap(bitmap);
                        }
                    });
                }
            });

            newImageReference.putFile(filePath).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
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