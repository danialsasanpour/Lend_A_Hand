package com.daniall.lend_a_hand.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daniall.lend_a_hand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import model.Location;
import model.Post;
import model.User;

public class PostEditorDelete extends AppCompatActivity implements View.OnClickListener {

    EditText edDescription, edLocation;
    TextView tvTimeFrom, tvTimeTo, tvDateFrom, tvDateTo, tvEditOrDelete;
    Button btnEditOrDelete;
    ImageButton btnTimeTo, btnTimeFrom, btnDateFrom, btnDateTo,
            imageButtonHome, imageButtonMessage, imageButtonAccount;
    User currentUser;
    Post currentPost;
    String mode;
    Location foundLocation = null;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    DatabaseReference posts = root.getReference("Posts");
    DatabaseReference locations = root.getReference("Location");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_editor_delete);

        initialize();
    }

    private void initialize()
    {
        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");
        currentPost = (Post) getIntent().getExtras().getSerializable("currentPost");
        mode = getIntent().getStringExtra("mode");

        imageButtonHome = findViewById(R.id.imageButtonHome);
        imageButtonMessage = findViewById(R.id.imageButtonMessage);
        imageButtonAccount = findViewById(R.id.imageButtonAccount);

        imageButtonHome.setOnClickListener(this);
        imageButtonMessage.setOnClickListener(this);
        imageButtonAccount.setOnClickListener(this);

        edDescription = findViewById(R.id.edDescription);
        edLocation = findViewById(R.id.edLocation);

        tvDateFrom = findViewById(R.id.tvDateFrom);
        tvEditOrDelete = findViewById(R.id.tvEditorDelete);
        btnDateFrom = findViewById(R.id.imgBtnDateFrom);
        btnDateFrom.setOnClickListener(this);
        tvTimeFrom = findViewById(R.id.tvTimeFrom);
        btnTimeFrom = findViewById(R.id.imgBtnTimeFrom);
        btnTimeFrom.setOnClickListener(this);

        tvDateTo = findViewById(R.id.tvDateTo);
        btnDateTo = findViewById(R.id.imgBtnDateTo);
        btnDateTo.setOnClickListener(this);
        tvTimeTo = findViewById(R.id.tvTimeTo);
        btnTimeTo = findViewById(R.id.imgBtnTimeTo);
        btnTimeTo.setOnClickListener(this);

        btnEditOrDelete = findViewById(R.id.btnEditorDelete);
        btnEditOrDelete.setOnClickListener(this);

        initializeFields();

    }

    private void initializeFields() {
        tvEditOrDelete.setText(mode + " post");
        edDescription.setText(currentPost.getDescription());
        locations.child(currentPost.getLocation()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Location location = snapshot.getValue(Location.class);
                edLocation.setText(location.displayLocation());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tvDateFrom.setText(currentPost.getDateFrom());
        tvTimeFrom.setText(currentPost.getTimeFrom());
        tvDateTo.setText(currentPost.getDateTo());
        tvTimeTo.setText(currentPost.getTimeTo());

        if (mode.equals("Edit"))
            btnEditOrDelete.setText("Edit");
        else
        {
            btnEditOrDelete.setText("Delete");
            edDescription.setEnabled(false);
            edLocation.setEnabled(false);
            btnDateFrom.setEnabled(false);
            btnDateTo.setEnabled(false);
            btnTimeFrom.setEnabled(false);
            btnTimeTo.setEnabled(false);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnEditorDelete:
               editOrDelete(view);
                break;
            case R.id.imgBtnTimeFrom:
                setTimes(view, tvTimeFrom);
                break;
            case R.id.imgBtnTimeTo:
                setTimes(view, tvTimeTo);
                break;
            case R.id.imgBtnDateFrom:
                setDates(view, tvDateFrom);
                break;
            case R.id.imgBtnDateTo:
                setDates(view, tvDateTo);
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



    private void setDates(View view, TextView tvDate)
    {


        final Calendar c = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        // on below line we are setting date to our text view.
                        tvDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    }
                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);
        // at last we are calling show to
        // display our date picker dialog.

        if (view.getId() == R.id.imgBtnDateFrom) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        } else {
            if (!tvDateFrom.getText().equals(""))
            {
                String strDate = tvDateFrom.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date date = dateFormat.parse(strDate);
                    datePickerDialog.getDatePicker().setMinDate(date.getTime());
                    Log.d("REACHED HERE", date.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }

        datePickerDialog.show();
    }

    private void setTimes(View view, TextView tvTime)
    {

        final Calendar c = Calendar.getInstance();

        // on below line we are getting our hour, minute.
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // on below line we are initializing our Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // on below line we are setting selected time
                        // in our text view.

                        tvTime.setText(hourOfDay + ":" + minute);
/*
                        if(view.getId() == R.id.imgBtnTimeFrom) {
                            timeFromSetHour = hourOfDay;
                            return;
                        }

                        if (view.getId() == R.id.imgBtnTimeTo) {
                            if (tvDateFrom.getText().equals("") && tvDateTo.getText().equals("") && tvTimeFrom.getText().equals("")){
                                return;
                            }

                            if (tvDateFrom.getText().equals(tvDateTo.getText())) {
                                if (hourOfDay < timeFromSetHour) {
                                    view.setHour(timeFromSetHour);
                                }
                            }
                        }
*/



                    }
                }, hour, minute, false);
        // at last we are calling show to
        // display our time picker dialog.


        timePickerDialog.show();

    }

    private void editOrDelete(View view)
    {
        Post post = currentPost;
        if (mode.equals("Delete"))
        {
            posts.child(currentPost.getPostId()).removeValue();
            locations.child(currentPost.getLocation()).removeValue();
            Intent intent = new Intent(this, Posts_Created_By.class);
            intent.putExtra("currentUser", currentUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(intent);
            return;
        }

        if(edDescription.getText().toString().equals("") ||
                edLocation.getText().toString().equals("") ||
                tvDateFrom.getText().toString().equals("") ||
                tvTimeFrom.getText().toString().equals("") ||
                tvTimeTo.getText().toString().equals("") ||
                tvDateTo.getText().toString().equals("")
        )
        {
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }


        String address = edLocation.getText().toString();
        Geocoder coder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = coder.getFromLocationName(address, 1);
            Address location = addresses.get(0);

            foundLocation = new Location(
                    location.getSubThoroughfare() + " " + location.getThoroughfare(),
                    location.getLocality(),
                    location.getAdminArea(),
                    location.getPostalCode(),
                    location.getCountryName(),
                    location.getLatitude(),
                    location.getLongitude()
            );


            locations.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        Location location = ds.getValue(Location.class);
                        if (foundLocation.compareTo(location))
                        {
                            foundLocation = location;
                            HashMap<String, Object> locationHashMap = new HashMap<String, Object>();
                            locationHashMap.put("street", foundLocation.getStreet());
                            locationHashMap.put("city", foundLocation.getCity());
                            locationHashMap.put("state", foundLocation.getState());
                            locationHashMap.put("postalCode", foundLocation.getPostalCode());
                            locationHashMap.put("country", foundLocation.getCountry());
                            locationHashMap.put("latitude", foundLocation.getLatitude());
                            locationHashMap.put("longitude", foundLocation.getLongitude());
                            locationHashMap.put("locationId", foundLocation.getLocationId());

                            locations.child(foundLocation.getLocationId()).updateChildren(locationHashMap);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Address is not valid or not found", Toast.LENGTH_LONG).show();
            return;
        }


        post = new Post(
                currentUser.getUsername().toString(),
                edDescription.getText().toString(),
                foundLocation.getLocationId(),
                tvDateFrom.getText().toString(),
                tvDateTo.getText().toString(),
                tvTimeFrom.getText().toString(),
                tvTimeTo.getText().toString()
        );
        post.setPostId(currentPost.getPostId());

        HashMap<String, Object> postHashMap = new HashMap<String, Object>();
        postHashMap.put("createdBy", post.getCreatedBy());
        postHashMap.put("description", post.getDescription());
        postHashMap.put("location", post.getLocation());
        postHashMap.put("dateFrom", post.getDateFrom());
        postHashMap.put("dateTo", post.getDateTo());
        postHashMap.put("timeFrom", post.getTimeFrom());
        postHashMap.put("timeTo", post.getTimeTo());
        postHashMap.put("postId", post.getPostId());

        posts.child(post.getPostId()).updateChildren(postHashMap);

        Intent intent = new Intent(this, Posts_Created_By.class);
        intent.putExtra("currentUser", currentUser);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }



}