package model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.daniall.lend_a_hand.R;
import com.daniall.lend_a_hand.controllers.All_Job_Posting;
import com.daniall.lend_a_hand.controllers.Home;
import com.daniall.lend_a_hand.controllers.PostEditorDelete;
import com.daniall.lend_a_hand.controllers.post_description;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PostAdapter extends BaseAdapter {

    DatabaseReference posts = FirebaseDatabase.getInstance().getReference("Posts");
    DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");

    private Context context;
    private ArrayList<Post> listOfPosts;
    private User currentUser;

    public PostAdapter(Context context, ArrayList<Post> listOfPosts, User user){
        this.context = context;
        this.listOfPosts = listOfPosts;
        this.currentUser = user;
    }


    @Override
    public int getCount() {
        return listOfPosts.size();
    }

    @Override
    public Object getItem(int i) {
        return listOfPosts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Post post = (Post)getItem(i);
        View oneItem;

        ImageView imageProfilePicture;
        TextView tvName, tvDescription;
        Button btnDetails, btnEdit, btnDelete;

        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.one_post, viewGroup, false);

        tvDescription = oneItem.findViewById(R.id.tvDescription);
        tvName = oneItem.findViewById(R.id.tvName);
        imageProfilePicture = oneItem.findViewById(R.id.imageProfilePicture);
        btnDetails = oneItem.findViewById(R.id.btnDetails);
        btnEdit = oneItem.findViewById(R.id.btnEdit);
        btnDelete = oneItem.findViewById(R.id.btnDelete);

        tvDescription.setText(post.getDescription());
        tvName.setText(post.getCreatedBy());
        // Need to add profile picture
        users.child(post.getCreatedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    User user = snapshot.getValue(User.class);
                    if (user.getProfilePicture() != null)
                        Picasso.with(context).load(user.getProfilePicture()).into(imageProfilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (!currentUser.getUsername().equals(post.getCreatedBy()))
        {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = oneItem.getLayoutParams();
            params.height = 275;
            oneItem.setLayoutParams(params);
        }
        else {
            btnDetails.setVisibility(View.GONE);
        }

        Intent intent = new Intent(context, PostEditorDelete.class);
        intent.putExtra("currentUser", currentUser);
        intent.putExtra("currentPost", post);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("mode", "Edit");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((Activity)context).finish();
                context.startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("mode", "Delete");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((Activity)context).finish();
                context.startActivity(intent);
            }
        });



        // Need to implement the button
        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, post_description.class);
                intent.putExtra("currentPost", post);
                intent.putExtra("currentUser", currentUser);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((Activity)context).finish();
                context.startActivity(intent);
            }
        });


        return oneItem;
    }
}























