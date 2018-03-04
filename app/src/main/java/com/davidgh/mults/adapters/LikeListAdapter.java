package com.davidgh.mults.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davidgh.mults.R;
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
        Mult mult = multList.get(position);

        holder.nameLike.setText(mult.getName());
        holder.dateLike.setText(mult.getDate());
        Picasso.with(mContext).load(mult.getUrl()).into(holder.imgLike);

        // Expand
        holder.expandable.setVisibility(View.GONE);
        holder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.expandable.getVisibility() == View.GONE){
                    holder.expandable.setVisibility(View.VISIBLE);

                    int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    holder.expandable.measure(widthSpec, heightSpec);

                    ValueAnimator animator = slideAnimator(0, holder.expandable.getMeasuredHeight(), holder);
                    animator.start();
                } else {
                    //collapse();
                    int finalHeight = holder.expandable.getHeight();

                    ValueAnimator animator = slideAnimator(finalHeight, 0, holder);
                    animator.start();
                }
            }
        });
    }

    private ValueAnimator slideAnimator(int start, int end, final ViewHolder holder){
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = holder.expandable.getLayoutParams();
                layoutParams.height = value;
                holder.expandable.setLayoutParams(layoutParams);
            }
        });

        return animator;
    }

    @Override
    public int getItemCount() {
        return multList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout header, expandable;
        public TextView nameLike, dateLike;
        public CircleImageView imgLike;

        public ViewHolder(View v) {
            super(v);

            nameLike = v.findViewById(R.id.like_name);
            dateLike = v.findViewById(R.id.like_date);
            imgLike = v.findViewById(R.id.like_img);

            header = v.findViewById(R.id.header);
            expandable = v.findViewById(R.id.expandable);
        }
    }


}
