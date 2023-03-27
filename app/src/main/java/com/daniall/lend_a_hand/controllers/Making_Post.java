package com.daniall.lend_a_hand.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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
import com.google.android.material.timepicker.TimeFormat;
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
import java.util.List;
import java.util.UUID;

import model.Location;
import model.Post;
import model.User;

public class Making_Post extends AppCompatActivity implements View.OnClickListener {

    EditText edDescription, edLocation;
    TextView tvTimeFrom, tvTimeTo, tvDateFrom, tvDateTo;
    Button btnPost;
    ImageButton btnTimeTo, btnTimeFrom, btnDateFrom, btnDateTo;
    User currentUser;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    DatabaseReference posts = root.getReference("Posts");
    DatabaseReference locations = root.getReference("Location");

    int timeFromSetHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_post);

        initialize();
    }

    private void initialize()
    {
        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");

        edDescription = findViewById(R.id.edDescription);
        edLocation = findViewById(R.id.edLocation);

        tvDateFrom = findViewById(R.id.tvDateFrom);
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

        btnPost = findViewById(R.id.btnPost);
        btnPost.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnPost:
                makePost(view);
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

    private void makePost(View view)
    {



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


        Location foundLocation = null;
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

            locations.child(foundLocation.getLocationId()).setValue(foundLocation);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Address is not valid or not found", Toast.LENGTH_LONG).show();
            return;
        }


        Post post = new Post(
                currentUser.getUsername().toString(),
                edDescription.getText().toString(),
                foundLocation.getLocationId(),
                tvDateFrom.getText().toString(),
                tvDateTo.getText().toString(),
                tvTimeFrom.getText().toString(),
                tvTimeTo.getText().toString()
        );
        posts.child(post.getPostId()).setValue(post);

        Intent intent = new Intent(this, Home.class);
        intent.putExtra("currentUser", currentUser);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }



}