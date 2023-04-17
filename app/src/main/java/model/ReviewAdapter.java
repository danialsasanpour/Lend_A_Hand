package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daniall.lend_a_hand.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class ReviewAdapter extends BaseAdapter {

    Context context;
    ArrayList<ReviewRating> listOfReviews;

    public ReviewAdapter(Context context, ArrayList<ReviewRating> listOfReviews){
        this.context = context;
        this.listOfReviews = listOfReviews;
    }


    @Override
    public int getCount() {
        return listOfReviews.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ReviewRating review = (ReviewRating) getItem(position);
        View oneItem;

        TextView tvReview, tvName;
        ImageView imageProfilePicture;

        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.one_review, parent, false);

        tvReview = oneItem.findViewById(R.id.tvReview);
        tvName = oneItem.findViewById(R.id.tvName);
        imageProfilePicture = oneItem.findViewById(R.id.imageProfilePicture);

        tvReview.setText(review.getReview());
        tvName.setText(review.getByUser());


        return oneItem;
    }
}
