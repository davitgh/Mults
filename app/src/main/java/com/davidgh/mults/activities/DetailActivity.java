package com.davidgh.mults.activities;
;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.davidgh.mults.R;
import com.davidgh.mults.adapters.ActorListAdapter;
import com.davidgh.mults.adapters.ReviewListAdapter;
import com.davidgh.mults.adapters.ViewPagerAdapter;
import com.davidgh.mults.fragments.RatingFinishFragment;
import com.davidgh.mults.fragments.RatingSubmitFragment;
import com.davidgh.mults.helpers.ActivityCommunicator;
import com.davidgh.mults.helpers.CommonSettings;
import com.davidgh.mults.helpers.NetworkUtils;
import com.davidgh.mults.models.Actor;
import com.davidgh.mults.models.Detail;
import com.davidgh.mults.models.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements ActivityCommunicator {

    private float myRating;

    //firebase
    private DatabaseReference databaseReference;

    //
    private int multId;
    private ImageView multImage;
    private TextView multName, multAttribute, multDescription;
    //private YouTubePlayerView multVideo;
    private RatingBar multRating;
    private RecyclerView multActors;
    private Button btnSubmit;
    private ImageButton mAddWatchlist;

    //
    private ViewPager mRatingPager;
    private LinearLayout mDotsIndicator;
    //
    private int dotsCount;
    private ImageView[] dots;

    //
    private ActorListAdapter adapter;
    private List<Actor> actorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        multId = Integer.valueOf(getIntent().getStringExtra("mult_id"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initLayout();

        downloadData(multId);
        setupRecyclerView();

        // ViewPager for RatingBar
        setupViewPager();

        //Video View
        //multVideo.initialize(CommonSettings.DEVELOPER_KEY, DetailActivity.this);

        ImageButton playMult = (ImageButton) findViewById(R.id.play_video);
        playMult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                String videoId = "Fee5vbFLYM4";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+videoId));
                intent.putExtra("VIDEO_ID", videoId);
                startActivity(intent);*/
                Intent player = new Intent(getBaseContext(), PlayerActivity.class);
                startActivity(player);
            }
        });
        /* TODO: Get Thumbnail here, */
/*
        YouTubeThumbnailView multThumb = (YouTubeThumbnailView) findViewById(R.id.video_mult);
        multThumb.initialize(CommonSettings.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(CommonSettings.YOUTUBE_VIDEO_CODE);

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        Toast.makeText(DetailActivity.this, "Thumbnail Error!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(DetailActivity.this, "Initialize ERROR!!!", Toast.LENGTH_SHORT).show();
            }
        });
*/

        // Click Listeners
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRatingPager.setCurrentItem(1);
            }
        });

        mAddWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Watchlist").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String data;
                        if (dataSnapshot.exists()){
                            data = dataSnapshot.getValue().toString();
                            if (data.equals(multId + "") || data.contains(multId + ",") || data.contains("," + multId)){
                            }else{
                                data = data + "," + multId;
                            }
                        } else {
                            data = multId + "";
                        }

                        databaseReference.child("Watchlist").child(FirebaseAuth.getInstance().getUid()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(DetailActivity.this, "Added to Watchlist!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        downloadReview();
    }

    private void setupRecyclerView() {
        actorList = new ArrayList<>();

        multActors.setHasFixedSize(true);
        adapter = new ActorListAdapter(actorList, this);
        multActors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        multActors.setAdapter(adapter);
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

    private void downloadReview() {
        ArrayList<Review> reviewList = new ArrayList<>();

        // TODO: Download real data
        reviewList.add(new Review(0, "url", "Davit Ghukasyan", "10/11/2018", "Es inch lav review er", 1.5f));
        reviewList.add(new Review(1, "url", "Alisa Muradyan", "11/11/2018", "Es inch lav review er, Lorem ipsum dolores heredia eli u tenc", 2.5f));
        reviewList.add(new Review(2, "url", "Kirakos", "10/01/2018", "Es inch lav review er", 3.5f));
        reviewList.add(new Review(3, "url", "Armen Grigoryan", "10/01/2000", "Es inch lav review er", 4.5f));
        reviewList.add(new Review(4, "url", "Chinacu meky", "15/08/2011", "Es inch lav review er", 0.5f));

        RecyclerView reviewListLayout = (RecyclerView) findViewById(R.id.review_list);
        reviewListLayout.setHasFixedSize(true);

        ReviewListAdapter reviewListAdapter = new ReviewListAdapter(this, reviewList);
        reviewListLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewListLayout.setAdapter(reviewListAdapter);

    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new RatingSubmitFragment());
        adapter.addFragment(new RatingFinishFragment());

        mRatingPager.setAdapter(adapter);

        dotsCount = 2; // mRatingPager.getCount();

        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(6, 0, 6, 0);
            mDotsIndicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        btnSubmit.setEnabled(false);

        mRatingPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (btnSubmit.isEnabled()) {
                    for (int i = 0; i < dotsCount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                    }

                    dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                    if (mRatingPager.getCurrentItem() == 0){
                        btnSubmit.setText("submit");
                    } else {
                        btnSubmit.setText("finish");
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void downloadData(int multId) {
        new GetDetails().execute(CommonSettings.API_ALL_MULTS + "/" + multId + "/details");
    }

    private void initLayout() {
        multImage = (ImageView) findViewById(R.id.img_mult);
        multName = (TextView) findViewById(R.id.name_mult);
        multAttribute = (TextView) findViewById(R.id.attribute_mult);
        multDescription = (TextView) findViewById(R.id.description_mult);
        //multVideo = (YouTubePlayerView) findViewById(R.id.video_mult);
        multRating = (RatingBar) findViewById(R.id.rating_mult);
        multActors = (RecyclerView) findViewById(R.id.mult_actors);
        //
        mRatingPager = (ViewPager) findViewById(R.id.rating_pager);
        mDotsIndicator = (LinearLayout) findViewById(R.id.slider_dots);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        mAddWatchlist = (ImageButton) findViewById(R.id.ib_add_watchlist);
    }

    @Override
    public void getRating(float rating) {
        myRating = rating;

        if (myRating == 0f) {
            btnSubmit.setEnabled(false);
        } else {
            btnSubmit.setEnabled(true);
        }
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
            Type actorListType = new TypeToken<List<Actor>>(){}.getType();

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonDetails = jsonObject.getJSONObject("details");

                Detail detail = gson.fromJson(String.valueOf(jsonDetails), type);

                Picasso.with(getApplicationContext()).load(detail.getImage()).into(multImage);
                // multRating = (RatingBar) findViewById(R.id.mult_rating);
                multName.setText(detail.getName());
                multAttribute.setText(detail.getAttribute());
                multDescription.setText(detail.getDescription());

                JSONArray jsonActors = jsonObject.getJSONArray("actors");
                //TODO:
                // actorList = gson.fromJson(String.valueOf(jsonActors), actorListType);
                for (int i = 0; i < jsonActors.length(); i++){
                    Actor actor = gson.fromJson(jsonActors.get(i).toString(), actorType);
                    actorList.add(actor);
                }
                adapter.notifyDataSetChanged();

                // android Layout mapping

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
