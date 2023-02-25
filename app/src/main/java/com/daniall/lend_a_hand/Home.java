package com.daniall.lend_a_hand;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.ArrayList;

import model.Location;
import model.Post;
import model.PostAdapter;
import model.User;

public class Home extends AppCompatActivity implements View.OnClickListener{

    TextView tvLend;
    ListView lvPosts;
    ImageButton imageButtonHome, imageButtonSearch, imageButtonAccount;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    DatabaseReference posts = root.getReference("Posts");
    DatabaseReference locations = root.getReference("Location");

    Context context = this;
    ArrayList<Post> listOfPosts;
    PostAdapter adapter;
    User currentUser;
    Location currentUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();
    }

    private void initialize() {

        tvLend = findViewById(R.id.tvLend);
        lvPosts = findViewById(R.id.lvPosts);
        imageButtonHome = findViewById(R.id.imageButtonHome);
        imageButtonSearch = findViewById(R.id.imageButtonSearch);
        imageButtonAccount = findViewById(R.id.imageButtonAccount);

        imageButtonHome.setOnClickListener(this);
        imageButtonSearch.setOnClickListener(this);
        imageButtonAccount.setOnClickListener(this);



        currentUser = new User();
        currentUser.setName("Joseph");
        currentUser.setUsername("josephjoestar");
        currentUser.setLocation("l100");
        currentUser.setRadius(10);

        currentUserLocation = new Location();
        currentUserLocation.setLatitude(45.4917341388098);
        currentUserLocation.setLongitude(-73.58159979725461);





        // Currently the user josephjoestar
/*
        users.child("josephjoestar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    currentUser = new User();
                    currentUser = snapshot.getValue(User.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Current user location
        locations.child(currentUser.getLocation()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    currentUserLocation = snapshot.getValue(Location.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

*/

        findPostsBasedOnUserRadius();


    }


    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.imageButtonHome:

                break;
            case R.id.imageButtonSearch:

                break;
            case R.id.imageButtonAccount:

                break;
        }

    }



    private void findPostsBasedOnUserRadius(){

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

                            Location location = snapshot.getValue(Location.class);

                            if (snapshot.getKey().equals(post.getLocation()))
                            {
                                double distance = calculateDistance(new LatLng(location.getLatitude(), location.getLongitude()),
                                        new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude())
                                        );

                                if (distance < currentUser.getRadius())
                                {
                                    Toast.makeText(Home.this, post.toString(), Toast.LENGTH_LONG).show();
                                    listOfPosts.add(post);


                                    adapter = new PostAdapter(context, listOfPosts);
                                    Toast.makeText(context, String.valueOf(listOfPosts.size()), Toast.LENGTH_LONG).show();
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



    private void testingFirebase() {
        posts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Post post = ds.getValue(Post.class);
                    if (post.getCreatedBy().equals("sherrinfordh23"))
                        Toast.makeText(context, post.getPostId(), Toast.LENGTH_LONG).show();

                    /*
                    if(ds.getKey().equals("sherrinfordh23"))
                    {
                        User user = ds.getValue(User.class);
                        Toast.makeText(context, user.getEmail(), Toast.LENGTH_LONG).show();
                    }

                     */
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}