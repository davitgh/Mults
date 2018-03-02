package com.davidgh.mults.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.davidgh.mults.R;
import com.davidgh.mults.activities.SectionActivity;
import com.davidgh.mults.models.SectionMultsModel;

import java.util.ArrayList;

/**
 * Created by davidgh on 2/16/18.
 */

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {

    private ArrayList<SectionMultsModel> dataList;
    private Context mContext;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public RecyclerViewDataAdapter(ArrayList<SectionMultsModel> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);

        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, int position) {
        final String sectionTitle = dataList.get(position).getHeader();
        holder.sectionTitle.setText(sectionTitle);

        ArrayList singleSectionItems = dataList.get(position).getAllMultsInSection();
        SectionListMultsAdapter adapter = new SectionListMultsAdapter(singleSectionItems, mContext);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setRecycledViewPool(recycledViewPool);

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sectionIntent = new Intent(mContext, SectionActivity.class);
                sectionIntent.putExtra("section_name", holder.getAdapterPosition());
                mContext.startActivity(sectionIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView sectionTitle;
        protected RecyclerView recyclerView;
        protected Button btnMore;

        public ItemRowHolder(View itemView) {
            super(itemView);
            this.sectionTitle = itemView.findViewById(R.id.section_title);
            this.recyclerView = itemView.findViewById(R.id.rv_category);
            this.btnMore = itemView.findViewById(R.id.btn_more);
        }
    }

}
