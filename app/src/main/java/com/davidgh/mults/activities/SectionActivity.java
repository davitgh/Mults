package com.davidgh.mults.activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.davidgh.mults.R;
import com.davidgh.mults.adapters.CardListAdapter;
import com.davidgh.mults.helpers.CommonSettings;
import com.davidgh.mults.helpers.NetworkUtils;
import com.davidgh.mults.helpers.RecyclerItemTouchHelper;
import com.davidgh.mults.models.Mult;
import com.davidgh.mults.models.SectionMultsModel;
import com.davidgh.mults.models.SingleMultModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SectionActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private Toolbar mToolbar;
    private RecyclerView mRvSection;
    private RelativeLayout mLayout;

    private List<Mult> multsList;
    private CardListAdapter mAdapter;

    private int sectionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        mLayout = (RelativeLayout) findViewById(R.id.relative_layout);

        mToolbar = (Toolbar) findViewById(R.id.section_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sectionTitle = getIntent().getIntExtra("section_name", 0);
        getSupportActionBar().setTitle(CommonSettings.getMultHeader(sectionTitle));

        // Android Layout
        mRvSection = (RecyclerView) findViewById(R.id.rv_section);
        multsList = new ArrayList<>();
        mAdapter = new CardListAdapter(this, multsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRvSection.setLayoutManager(mLayoutManager);
        mRvSection.setItemAnimator(new DefaultItemAnimator());
        mRvSection.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRvSection.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRvSection);

        downloadMults();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof CardListAdapter.ViewHolder){

            // get the removed item name to display into snack bar
            String name = multsList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Mult deletedItem = multsList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from RecyclerView
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snackbar with undo option
            Snackbar snackbar = Snackbar.make(mLayout, name + "Removed from List!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapter.restoreItem(deletedIndex, deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void downloadMults() {

        new GetMults().execute(CommonSettings.API_ALL_MULTS);
    }

    private class GetMults extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String stream = null;
            String urlString = urls[0];

            NetworkUtils networkUtils = new NetworkUtils();

            stream = networkUtils.getHttpData(urlString);

            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Gson gson = new Gson();
            Type type = new TypeToken<Mult>(){}.getType();

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("mults");

                    for (int j = sectionTitle * 17; j < (sectionTitle + 1) * 17; j++) {

                        JSONObject multObject = jsonArray.getJSONObject(j);
                        Mult m = gson.fromJson(String.valueOf(multObject), type);
                        multsList.add(m);
                        //singleMultModels.add(new SingleMultModel(m.getName(), m.getUrl(), j)); // TODO make rating float, and add second string
                    }

                    mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
