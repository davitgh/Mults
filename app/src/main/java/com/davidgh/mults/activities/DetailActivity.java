package com.davidgh.mults.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.davidgh.mults.R;
import com.davidgh.mults.adapters.ActorListAdapter;
import com.davidgh.mults.helpers.CommonSettings;
import com.davidgh.mults.helpers.NetworkUtils;
import com.davidgh.mults.models.Actor;
import com.davidgh.mults.models.Detail;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends YouTubeBaseActivity {

    private YouTubePlayerView multVideo;
    private ImageView multImg;
    private RatingBar multRating;
    private TextView multName, multAttribute, multDescription;
    private RecyclerView multActors;

    //
    private ActorListAdapter adapter;
    private List<Actor> actorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int multId = getIntent().getIntExtra("mult_id", 0);

        initLayout();
        downloadData(multId);

        actorList = new ArrayList<>();

        multActors = (RecyclerView) findViewById(R.id.mult_actors);
        multActors.setHasFixedSize(true);
        adapter = new ActorListAdapter(actorList, this);
        multActors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        multActors.setAdapter(adapter);
    }

    private void initLayout() {
        multVideo = (YouTubePlayerView) findViewById(R.id.mult_video);
        multImg = (ImageView) findViewById(R.id.mult_img);
        multRating = (RatingBar) findViewById(R.id.mult_rating);
        multName = (TextView) findViewById(R.id.mult_name);
        multAttribute = (TextView) findViewById(R.id.mult_attribute);
        multDescription = (TextView) findViewById(R.id.mult_decription);
    }

    private void downloadData(int multId) {
        new GetDetails().execute(CommonSettings.API_ALL_MULTS + "/" + multId + "/details");
    }

    private class GetDetails extends AsyncTask<String, Void, String> {

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
            Type type = new TypeToken<Detail>(){}.getType();
            Type actorType = new TypeToken<Actor>(){}.getType();
            // TODO:
            // Type actorListType = new TypeToken<List<Actor>>(){}.getType();

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonDetails = jsonObject.getJSONObject("details");

                JSONArray jsonActors = jsonObject.getJSONArray("actors");

                Detail detail = gson.fromJson(String.valueOf(jsonDetails), type);

                Picasso.with(getApplicationContext()).load(detail.getImage()).into(multImg);
                // multRating = (RatingBar) findViewById(R.id.mult_rating);
                multName.setText(detail.getName());
                multAttribute.setText(detail.getAttribute());
                multDescription.setText(detail.getDescription());

                //TODO:
                // actorList = gson.fromJson(String.valueOf(jsonActors), actorListType);
                for (int i = 0; i < jsonActors.length(); i++){
                    Actor actor = gson.fromJson(jsonActors.get(i).toString(), actorType);
                    actorList.add(actor);
                }
                //Actor a = new Actor()
                adapter.notifyDataSetChanged();

                // android Layout mapping

                //multVideo.setVideoPath(detail.getVideo());
                multVideo.initialize(CommonSettings.DEVELOPER_KEY,
                        new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                YouTubePlayer youTubePlayer, boolean b) {

                                // do any work here to cue video, play video, etc.
                                // TODO : https://www.sitepoint.com/using-the-youtube-api-to-embed-video-in-an-android-app/
                                //youTubePlayer.cueVideo("5xVh-7ywKpE");
                                youTubePlayer.loadVideo("5xVh-7ywKpE");
                                youTubePlayer.play();
                            }
                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                YouTubeInitializationResult youTubeInitializationResult) {

                            }
                        });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
