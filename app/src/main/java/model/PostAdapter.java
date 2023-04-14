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

import com.daniall.lend_a_hand.R;
import com.daniall.lend_a_hand.controllers.All_Job_Posting;
import com.daniall.lend_a_hand.controllers.Home;
import com.daniall.lend_a_hand.controllers.post_description;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PostAdapter extends BaseAdapter {

    DatabaseReference posts = FirebaseDatabase.getInstance().getReference("Posts");

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

        ImageView imgProfilePicture;
        TextView tvName, tvDescription;
        Button btnDetails, btnEdit, btnDelete;

        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.one_post, viewGroup, false);

        tvDescription = oneItem.findViewById(R.id.tvDescription);
        tvName = oneItem.findViewById(R.id.tvName);
        imgProfilePicture = oneItem.findViewById(R.id.imgProfilePicture);
        btnDetails = oneItem.findViewById(R.id.btnDetails);
        btnEdit = oneItem.findViewById(R.id.btnEdit);
        btnDelete = oneItem.findViewById(R.id.btnDelete);

        tvDescription.setText(post.getDescription());
        tvName.setText(post.getCreatedBy());
        // Need to add profile picture

        if (!currentUser.getUsername().equals(post.getCreatedBy()))
        {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = oneItem.getLayoutParams();
            params.height = 275;
            oneItem.setLayoutParams(params);
        }


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posts.child(post.getPostId()).removeValue();
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























