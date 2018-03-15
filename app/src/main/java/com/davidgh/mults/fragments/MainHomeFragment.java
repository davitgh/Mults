package com.davidgh.mults.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davidgh.mults.R;
import com.davidgh.mults.adapters.RecyclerViewDataAdapter;
import com.davidgh.mults.helpers.CommonSettings;
import com.davidgh.mults.helpers.NetworkUtils;
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

/**
 * Created by davidgh on 3/6/18.
 */

public class MainHomeFragment extends Fragment {

    private ArrayList<SectionMultsModel> allSampleData;

    // Adapter
    private RecyclerViewDataAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_main_home, container, false);


        allSampleData = new ArrayList<>();

        createDummyData();

        RecyclerView rvMain = (RecyclerView) v.findViewById(R.id.rv_main);
        rvMain.setHasFixedSize(true);
        adapter = new RecyclerViewDataAdapter(allSampleData, getContext());
        rvMain.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvMain.setAdapter(adapter);

        final SwipeRefreshLayout mSwipeToRefresh = (SwipeRefreshLayout) v.findViewById(R.id.main_swipe_refresh);

        mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createDummyData();
                mSwipeToRefresh.setRefreshing(false);
            }
        });

        return v;
    }

    private void createDummyData(){
        if (NetworkUtils.isNetworkAvailable(getContext())){
            new GetMults().execute(CommonSettings.API_ALL_MULTS);
        } else{
            // TODO : handel internet
        }
        // {now, popular, comming soon}
    }

    private class GetMults extends AsyncTask<String, Void, String>{

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

            // TODO : Make Progress Dialog which will wait for download process

            Gson gson = new Gson();
            Type type = new TypeToken<Mult>(){}.getType();

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("mults");

                for (int i = 0; i < 3; i++) {
                    SectionMultsModel mults = new SectionMultsModel();
                    mults.setHeader(CommonSettings.getMultHeader(i));
                    ArrayList<SingleMultModel> singleMultModels = new ArrayList<>();

                    for (int j = i * 17; j < (i+1) * 17; j++) {

                        JSONObject multObject = jsonArray.getJSONObject(j);
                        Mult m = gson.fromJson(String.valueOf(multObject), type);

                        singleMultModels.add(new SingleMultModel(m.getName(), m.getUrl(), j)); // TODO make rating float, and add second string
                    }

                    mults.setAllMultsInSection(singleMultModels);
                    allSampleData.add(mults);
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
