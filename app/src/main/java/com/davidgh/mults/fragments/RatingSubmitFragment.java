package com.davidgh.mults.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.davidgh.mults.R;
import com.davidgh.mults.helpers.ActivityCommunicator;

/**
 * Created by davidgh on 3/12/18.
 */

public class RatingSubmitFragment extends Fragment implements ActivityCommunicator {

    private ActivityCommunicator activityComunicator;
    // TODO: Make the layout very smooth
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_rating_submit, container, false);

        RatingBar multRating = v.findViewById(R.id.rating_bar);
        final TextView mRatingText = v.findViewById(R.id.rating_text);

        multRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                getRating(v);
                switch ((int) v){
                    case 1:
                        mRatingText.setText("Hated it");
                        break;
                    case 2:
                        mRatingText.setText("Disliked it");
                        break;
                    case 3:
                        mRatingText.setText("It's OK");
                        break;
                    case 4:
                        mRatingText.setText("Liked it");
                        break;
                    case 5:
                        mRatingText.setText("Loved it");
                        break;
                    default:
                        mRatingText.setText("");
                }
            }
        });


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityComunicator =(ActivityCommunicator) context;
    }

    @Override
    public void getRating(float rating) {
        activityComunicator.getRating(rating);
    }
}
