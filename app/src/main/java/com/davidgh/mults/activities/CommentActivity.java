package com.davidgh.mults.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.davidgh.mults.R;
import com.davidgh.mults.adapters.ReviewListAdapter;
import com.davidgh.mults.models.Review;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Review> reviewList = new ArrayList<>();

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
}
