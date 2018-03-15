package com.davidgh.mults.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.davidgh.mults.R;
import com.davidgh.mults.adapters.LikeListAdapter;
import com.davidgh.mults.helpers.CommonSettings;
import com.davidgh.mults.helpers.NetworkUtils;
import com.davidgh.mults.models.Mult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by davidgh on 3/4/18.
 */

public class ProfileLikeFragment extends Fragment {

    // Firebase
    private DatabaseReference mDatabase;

    private ArrayList<Mult> likedMults;
    private String multLists;

    private LikeListAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_profile_like, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    multLists = dataSnapshot.getValue().toString();
                    new GetLikedMults().execute(CommonSettings.API_LIKED_MULTS + "/" + multLists);
                }
                else
                    Toast.makeText(getContext(), "Handel this", Toast.LENGTH_SHORT).show();
                // TODO: handel This situation
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        likedMults = new ArrayList<>();

        RecyclerView rvLike = (RecyclerView) v.findViewById(R.id.rv_liked_mults);
        rvLike.setHasFixedSize(true);
        mAdapter = new LikeListAdapter(getContext(), likedMults);
        rvLike.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvLike.setAdapter(mAdapter);

        return v;
    }

    private class GetLikedMults extends AsyncTask<String, Void, String> {

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

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject multObj = jsonArray.getJSONObject(i);
                    Mult m = gson.fromJson(String.valueOf(multObj), type);

                    likedMults.add(m);
                }
                mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
