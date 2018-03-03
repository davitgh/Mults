package com.davidgh.mults.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davidgh.mults.R;
import com.davidgh.mults.models.Actor;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by davidgh on 3/3/18.
 */

public class ActorListAdapter extends RecyclerView.Adapter<ActorListAdapter.ActorItemHolder>{

    private List<Actor> actorList;
    private Context mContext;

    public ActorListAdapter(List<Actor> actorList, Context mContext) {
        this.actorList = actorList;
        this.mContext = mContext;
    }

    @Override
    public ActorItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.actor_single_card, parent, false);

        return new ActorItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ActorItemHolder holder, int position) {

        Actor actor = actorList.get(position);

        holder.actorName.setText(actor.getName());
        holder.actorRole.setText(actor.getRole());
        Picasso.with(mContext).load(actor.getImage()).into(holder.actorImg);
    }

    @Override
    public int getItemCount() {
        return actorList.size();
    }

    public class ActorItemHolder extends RecyclerView.ViewHolder {

        protected ImageView actorImg;
        protected TextView actorName, actorRole;

        public ActorItemHolder(View itemView) {
            super(itemView);

            actorImg = (ImageView) itemView.findViewById(R.id.actor_img);
            actorName = (TextView) itemView.findViewById(R.id.actor_name);
            actorRole = (TextView) itemView.findViewById(R.id.actor_role);
        }
    }
}
