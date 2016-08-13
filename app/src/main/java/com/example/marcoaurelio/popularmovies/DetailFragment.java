/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Shows detailed information of the selected movie.
 */
public class DetailFragment extends Fragment {

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent i = getActivity().getIntent();
        MovieData movieData = (MovieData) i.getSerializableExtra(getString(R.string.movie_data_key));

        TextView tempTextView = (TextView) rootView.findViewById(R.id.movie_title);
        tempTextView.setText(movieData.originalTitle);

        ImageView tempImageView = (ImageView) rootView.findViewById(R.id.movie_poster);
        Picasso.with(getContext())
                .load(movieData.posterUrl)
                .into(tempImageView);

        tempTextView = (TextView) rootView.findViewById(R.id.movie_overview);
        tempTextView.setText(getString(R.string.overview_label)+" "+movieData.overview);

        tempTextView = (TextView) rootView.findViewById(R.id.movie_average);
        tempTextView.setText(getString(R.string.user_rating_label)+" "+Double.toString(movieData.userRating));

        tempTextView = (TextView) rootView.findViewById(R.id.movie_relase_date);
        tempTextView.setText(getString(R.string.release_date_label)+" "+movieData.releaseDate);

        return rootView;
    }
}
