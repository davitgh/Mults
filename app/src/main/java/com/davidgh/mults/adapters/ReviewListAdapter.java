package com.davidgh.mults.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.davidgh.mults.R;
import com.davidgh.mults.models.Review;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by davidgh on 3/13/18.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewHolder> {

    private Context mContext;
    private List<Review> reviewList;

    public ReviewListAdapter(Context mContext, List<Review> reviewList) {
        this.mContext = mContext;
        this.reviewList = reviewList;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_single_item, parent, false);

        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review = reviewList.get(position);

        Picasso.with(mContext).load(review.getImage()).into(holder.reviewerImage);
        holder.reviewerName.setText(review.getName());
        holder.reviewerDate.setText(review.getDate());
        holder.reviewerRating.setRating(review.getRating());
        holder.reviewerReview.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        protected CircleImageView reviewerImage;
        protected TextView reviewerName, reviewerDate, reviewerReview;
        protected RatingBar reviewerRating;

        public ReviewHolder(View v) {
            super(v);

            reviewerImage = v.findViewById(R.id.reviewer_img);
            reviewerName = v.findViewById(R.id.reviewer_name);
            reviewerDate = v.findViewById(R.id.reviewer_date);
            reviewerRating = v.findViewById(R.id.reviewer_rating);
            reviewerReview = v.findViewById(R.id.reviewer_review);
        }
    }
}
