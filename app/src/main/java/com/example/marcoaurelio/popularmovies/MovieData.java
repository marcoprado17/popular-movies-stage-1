/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies;

import android.util.Log;

import java.io.Serializable;

/**
 * Store movie data brought from themoviedb.org API as java class.
 */
public class MovieData implements Serializable {
    private final String LOG_TAG = MovieData.class.getSimpleName();

    public String posterUrl;
    public String originalTitle;
    public String overview;
    public double userRating;
    public String releaseDate;

    public void Log() {
        Log.v(LOG_TAG, "posterUrl: " + posterUrl);
        Log.v(LOG_TAG, "originalTitle: " + originalTitle);
        Log.v(LOG_TAG, "overview: " + overview);
        Log.v(LOG_TAG, "userRating: " + userRating);
        Log.v(LOG_TAG, "releaseDate: " + releaseDate);
    }
}
