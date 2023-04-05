package com.daniall.lend_a_hand.controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.daniall.lend_a_hand.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.Post;
import model.PostAdapter;
import model.User;

public class All_Job_Posting extends AppCompatActivity implements View.OnClickListener{


    ImageButton imageButtonRefresh;
    ListView lvPosts;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    DatabaseReference posts = root.getReference("Posts");
    DatabaseReference locations = root.getReference("Location");

    Context context = this;
    ArrayList<Post> listOfPosts;
    PostAdapter adapter;
    User currentUser;

    FusedLocationProviderClient mFusedLocationClient;
    final int PERMISSION_ID = 4444;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_job_posting);

        initialize();
    }

    private void initialize() {

        imageButtonRefresh = findViewById(R.id.imageButtonRefresh);
        lvPosts = findViewById(R.id.lvPosts);

        imageButtonRefresh.setOnClickListener(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");
        getLastLocation();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonRefresh:
                getLastLocation();
                break;
        }
    }



    @SuppressLint("MissingPermission")
    public void getLastLocation() {


        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        android.location.Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        }
                        else {
                            Log.d("Location", "Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude());
                            findPostsBasedOnUserRadius(location);
                        }

                    }
                });

            } else {
                Toast.makeText(this, "Please turn on your location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMaxUpdates(1).build();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallBack, Looper.myLooper());

    }

    private LocationCallback mLocationCallBack = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            android.location.Location lastLocation = locationResult.getLastLocation();
            Log.d("Location", "Latitude: " + lastLocation.getLatitude() + "\nLongitude: " + lastLocation.getLongitude());
            findPostsBasedOnUserRadius(lastLocation);
        }
    };

    public boolean checkPermissions() {

        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions() {

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // Checking if location is enabled
    public boolean isLocationEnabled() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private void findPostsBasedOnUserRadius(android.location.Location currentUserLocation){

        listOfPosts = new ArrayList<Post>();

        posts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Post post = ds.getValue(Post.class);
                    locations.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            model.Location location = snapshot.getValue(model.Location.class);


                            if (location.getLocationId().equals(post.getLocation()))
                            {

                                double distance = calculateDistance(new LatLng(location.getLatitude(), location.getLongitude()),
                                        new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude())
                                );

                                if (distance < currentUser.getRadius() && !post.getCreatedBy().equals(currentUser.getUsername()))
                                {
                                    listOfPosts.add(post);
                                    adapter = new PostAdapter(context, listOfPosts, currentUser);
                                    lvPosts.setAdapter(adapter);
                                }

                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private double calculateDistance(LatLng locationA, LatLng locationB) {

        double latARadian = Math.toRadians(locationA.latitude);
        double longARadian = Math.toRadians(locationA.longitude);
        double latBRadian = Math.toRadians(locationB.latitude);
        double longBRadian = Math.toRadians(locationB.longitude);

        double latDifference = latBRadian - latARadian;
        double longDifference = longBRadian - longARadian;

        double a = Math.pow(Math.sin(latDifference / 2), 2) + Math.cos(latARadian) * Math.cos(latBRadian) * Math.pow(Math.sin(longDifference / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = 6371 * c;


        return distance;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissions()) {
            return;
        }
    }
}



