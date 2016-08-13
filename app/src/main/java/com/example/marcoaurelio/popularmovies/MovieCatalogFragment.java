/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca
 */

package com.example.marcoaurelio.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Show movies posters sorted by most popular or by top rate, provide a way to get more detailed
 * info from a specific movie clicking on it. The class provide an abstraction for a catalog of
 * movies posters.
 */
public class MovieCatalogFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView mGridView;
    private ArrayList<MovieData> mAllMoviesData;

    public MovieCatalogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_film_catalog, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.posters_gridview);
        mGridView.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAllMoviesData();
    }

    private void updateAllMoviesData() {
        (new FetchMoviesDataTask()).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        detailIntent.putExtra(getString(R.string.movie_data_key), mAllMoviesData.get(position));
        startActivity(detailIntent);
    }

    /**
     * Get the data of the movies as json, pass to MovieData format, set the adapter with the
     * data of all movies and set the adapter to the GridView
     */
    public class FetchMoviesDataTask extends AsyncTask<Void, Void, JSONObject> {

        private String LOG_TAG = FetchMoviesDataTask.class.getSimpleName();

        @Override
        protected JSONObject doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String responseJsonStr;

            final String SCHEME = "http";
            final String AUTHORITY = "api.themoviedb.org";
            final String ENCODED_PATH = "3/movie/" + getSortMode();
            final String API_KEY_QUERY_NAME = "api_key";
            final String API_KEY = "???????????????????";

            try {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme(SCHEME)
                        .authority(AUTHORITY)
                        .appendEncodedPath(ENCODED_PATH)
                        .appendQueryParameter(API_KEY_QUERY_NAME, API_KEY);

//                Log.v(LOG_TAG, builder.toString());

                URL url = new URL(builder.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                responseJsonStr = buffer.toString();

                try {
                    return new JSONObject(responseJsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private String getSortMode() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortMode = prefs.getString(getString(R.string.pref_sort_mode_key),
                    getString(R.string.by_popularity));

            final String POPULAR_PATH = "popular";
            final String TOP_RATED_PATH = "top_rated";

            if (sortMode.equals(getString(R.string.by_popularity))) {
                return POPULAR_PATH;
            } else {
                return TOP_RATED_PATH;
            }
        }

        private ArrayList<MovieData> getMovieDataFromJsonResponse(JSONObject jsonResponse)
                throws JSONException {
            ArrayList<MovieData> allMoviesData = new ArrayList<MovieData>();

            final String RESULTS_KEY = "results";
            final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342";
            final String POSTER_PATH_KEY = "poster_path";
            final String ORIGINAL_TITLE_KEY = "original_title";
            final String OVERVIEW_KEY = "overview";
            final String VOTE_AVERAGE_KEY = "vote_average";
            final String RELEASE_DATE_KEY = "release_date";

            JSONArray allMoviesDataAsJson = jsonResponse.getJSONArray(RESULTS_KEY);
            for (int i = 0; i < allMoviesDataAsJson.length(); i++) {
                JSONObject movieDataAsJson = allMoviesDataAsJson.getJSONObject(i);

                MovieData movieData = new MovieData();

                movieData.posterUrl = POSTER_BASE_URL + movieDataAsJson.getString(POSTER_PATH_KEY);
                movieData.originalTitle = movieDataAsJson.getString(ORIGINAL_TITLE_KEY);
                movieData.overview = movieDataAsJson.getString(OVERVIEW_KEY);
                movieData.userRating = movieDataAsJson.getDouble(VOTE_AVERAGE_KEY);
                movieData.releaseDate = movieDataAsJson.getString(RELEASE_DATE_KEY);

//                movieData.Log();

                allMoviesData.add(movieData);
            }

            return allMoviesData;
        }

        private ArrayList<String> getAllPostersUrl(ArrayList<MovieData> allMoviesData) {
            ArrayList<String> allPosterUrl = new ArrayList<String>();

            for (MovieData movieData : allMoviesData) {
                allPosterUrl.add(movieData.posterUrl);
            }

            return allPosterUrl;
        }

        private PosterImageAdapter createPosterImageAdapter() {
            return new PosterImageAdapter(getContext(), getAllPostersUrl(mAllMoviesData));
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject != null) {
                try {
                    mAllMoviesData = getMovieDataFromJsonResponse(jsonObject);
                    mGridView.setAdapter(createPosterImageAdapter());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "Error processing the json response from themoviedb.org API");
                }
            }
        }
    }
}
