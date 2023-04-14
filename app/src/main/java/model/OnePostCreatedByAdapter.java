package model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daniall.lend_a_hand.R;
import com.daniall.lend_a_hand.controllers.Home;
import com.daniall.lend_a_hand.controllers.PostEditorDelete;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OnePostCreatedByAdapter extends BaseAdapter {

    DatabaseReference posts = FirebaseDatabase.getInstance().getReference("Posts");

    Context context;
    ArrayList<Post> listOfPosts;
    User currentUser;

    public OnePostCreatedByAdapter(Context context, ArrayList<Post> listOfPosts, User currentUser)
    {
        this.context = context;
        this.listOfPosts = listOfPosts;
        this.currentUser = currentUser;
    }

    @Override
    public int getCount() {
        return listOfPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Post post = (Post) getItem(position);
        View oneItem;

        TextView tvDescription;
        Button btnEdit, btnDelete;

        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.one_post_created_by, parent, false);

        tvDescription = oneItem.findViewById(R.id.tvDescription);
        btnEdit = oneItem.findViewById(R.id.btnEdit);
        btnDelete = oneItem.findViewById(R.id.btnDelete);

        if (!currentUser.getUsername().equals(post.getCreatedBy()))
        {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        tvDescription.setText(post.getDescription());
        //Implement btnEdit onClickListener here

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


        return oneItem;
    }
}
