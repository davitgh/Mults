package com.davidgh.mults.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidgh.mults.activities.DetailActivity;
import com.davidgh.mults.R;
import com.davidgh.mults.models.SingleMultModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by davidgh on 2/16/18.
 */

public class SectionListMultsAdapter extends RecyclerView.Adapter<SectionListMultsAdapter.MultItemHolder> {

    private ArrayList<SingleMultModel> multModels;
    private Context mContext;

    public SectionListMultsAdapter(ArrayList<SingleMultModel> multModels, Context mContext) {
        this.multModels = multModels;
        this.mContext = mContext;
    }

    @Override
    public MultItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mult_single_card, null);
        MultItemHolder multItemHolder = new MultItemHolder(v);

        return multItemHolder;
    }

    @Override
    public void onBindViewHolder(MultItemHolder holder, int position) {
        SingleMultModel multModel = multModels.get(position);

        holder.multName.setText(multModel.getName());
        Picasso.with(mContext).load(multModel.getImg()).into(holder.multImage);
    }

    @Override
    public int getItemCount() {
        return (null != multModels ? multModels.size() : 0);
    }

    public class MultItemHolder extends RecyclerView.ViewHolder {

        protected TextView multName;
        protected ImageView multImage;

        public MultItemHolder(final View itemView) {
            super(itemView);
            this.multName = (TextView) itemView.findViewById(R.id.mult_name);
            this.multImage = (ImageView) itemView.findViewById(R.id.mult_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 5: Important, add animation
                    Intent detailIntent = new Intent(mContext, DetailActivity.class);
                    detailIntent.putExtra("mult_id", getLayoutPosition() + 1);
                    mContext.startActivity(detailIntent);
                }
            });
        }
    }
}
