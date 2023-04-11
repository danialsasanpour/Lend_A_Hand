package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daniall.lend_a_hand.R;
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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posts.child(post.getPostId()).removeValue();
            }
        });


        return oneItem;
    }
}
