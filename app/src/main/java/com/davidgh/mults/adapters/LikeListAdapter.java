package com.davidgh.mults.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.davidgh.mults.R;
import com.davidgh.mults.activities.DetailActivity;
import com.davidgh.mults.models.Mult;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by davidgh on 3/4/18.
 */

public class LikeListAdapter extends RecyclerView.Adapter<LikeListAdapter.ViewHolder> {

    private Context mContext;
    private List<Mult> multList;

    public LikeListAdapter(Context mContext, List<Mult> multList) {
        this.mContext = mContext;
        this.multList = multList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.like_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Mult mult = multList.get(position);

        holder.nameLike.setText(mult.getName());
        holder.dateLike.setText(mult.getDate());
        Picasso.with(mContext).load(mult.getUrl()).into(holder.imgLike);

        holder.btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Pressed Dislike", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(mContext, DetailActivity.class);
                detailsIntent.putExtra("mult_id", String.valueOf(mult.getId()));
                mContext.startActivity(detailsIntent);
            }
        });
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Pressed Add WatchList", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return multList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout header, expandable;
        public TextView nameLike, dateLike;
        public CircleImageView imgLike;

        public Button btnDislike;
        public Button btnDetails;
        public Button btnAdd;

        public ViewHolder(View v) {
            super(v);

            nameLike = v.findViewById(R.id.like_name);
            dateLike = v.findViewById(R.id.like_date);
            imgLike = v.findViewById(R.id.like_img);

            header = v.findViewById(R.id.header);
            expandable = v.findViewById(R.id.expandable);

            btnDislike = (Button) expandable.findViewById(R.id.btn_dislike);
            btnDetails = (Button) expandable.findViewById(R.id.btn_details);
            btnAdd = (Button) expandable.findViewById(R.id.btn_add_watchlist);

            // Expand
            expandable.setVisibility(View.GONE);
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (expandable.getVisibility() == View.GONE){
                        // Expand
                        expandable.setVisibility(View.VISIBLE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        expandable.measure(widthSpec, heightSpec);

                        ValueAnimator animator = slideAnimator(0, expandable.getMeasuredHeight());
                        animator.start();
                    } else {
                        //collapse();
                        int finalHeight = expandable.getHeight();

                        ValueAnimator animator = slideAnimator(finalHeight, 0);
                        animator.start();
                        expandable.setVisibility(View.GONE);
                    }
                }
            });
        }

        private ValueAnimator slideAnimator(int start, int end){
            ValueAnimator animator = ValueAnimator.ofInt(start, end);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = expandable.getLayoutParams();
                    layoutParams.height = value;
                    expandable.setLayoutParams(layoutParams);
                }
            });

            return animator;
        }
    }
}
