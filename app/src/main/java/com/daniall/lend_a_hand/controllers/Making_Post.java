package com.daniall.lend_a_hand.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import model.Post;
import model.User;

public class Making_Post extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    EditText edDescription, edLocation, edDateFrom, edDateTo;
    TextView tvTimeFrom, tvTimeTo;
    Button btnPost;
    ImageButton btnTimeTo, btnTimeFrom, btnDateFrom, btnDateTo;
    User currentUser;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    DatabaseReference posts = root.getReference("Posts");

    String newPostId;

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

        edDateFrom = findViewById(R.id.dateFrom);
        btnDateFrom = findViewById(R.id.btnDateFrom);
        tvTimeFrom = findViewById(R.id.tvTimeFrom);
        btnTimeFrom = findViewById(R.id.imgBtnTimeFrom);
        btnTimeFrom.setOnClickListener(this);

        edDateTo = findViewById(R.id.dateTo);
        btnDateTo = findViewById(R.id.btnDateTo);
        tvTimeFrom = findViewById(R.id.tvTimeFrom);
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
            case R.id.imgBtnTimeTo:
                setTimes(view);
                break;
            case R.id.btnDateFrom:
            case R.id.btnDateTo:
                setDates(view);
                break;
        }
    }

    private void setDates(View view)
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
                        edDateFrom.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    }
                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.show();
    }

    private void setTimes(View view)
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
                        tvTimeFrom.setText(hourOfDay + ":" + minute);
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
                edDateFrom.getText().toString().equals("") ||
                tvTimeFrom.getText().toString().equals("") ||
                tvTimeTo.getText().toString().equals("") ||
                edDateTo.getText().toString().equals("")
        )
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
        else{
            Post post = new Post();
            newPostId = UUID.randomUUID().toString();
            post.setPostId(newPostId);
            posts.child(newPostId).addListenerForSingleValueEvent(this);
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot)
    {
        if (!snapshot.exists()) {
            try {
                String location = edLocation.getText().toString();
                String description = edDescription.getText().toString();

            }
            catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}