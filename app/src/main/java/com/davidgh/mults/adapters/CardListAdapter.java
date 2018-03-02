package com.davidgh.mults.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.davidgh.mults.R;
import com.davidgh.mults.models.Mult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by davidgh on 2/28/18.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    private Context mContext;
    private List<Mult> multList;

    public CardListAdapter(Context mContext, List<Mult> multList) {
        this.mContext = mContext;
        this.multList = multList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_section_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mult mult = multList.get(position);

        holder.name.setText(mult.getName());
        holder.description.setText("TODO: Description"); // TODO; add description into Mult Object

        holder.date.setText(mult.getDate());

        Picasso.with(mContext).load(mult.getUrl()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return multList.size();
    }

    public void removeItem(int position){
        multList.remove(position);

        notifyItemRemoved(position);
    }

    public void restoreItem(int position, Mult item){
        multList.add(position, item);

        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, description, date;
        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public ViewHolder(View v) {
            super(v);

            name = v.findViewById(R.id.name);
            description = v.findViewById(R.id.description);
            date = v.findViewById(R.id.date);
            thumbnail = v.findViewById(R.id.thumbnail);
            viewBackground = v.findViewById(R.id.view_background);
            viewForeground = v.findViewById(R.id.view_foreground);
        }
    }


}
