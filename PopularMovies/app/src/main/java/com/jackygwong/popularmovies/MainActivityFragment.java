package com.jackygwong.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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


public class MainActivityFragment extends Fragment {

    private ImageAdapter imageAdapter;
    private MovieDetails[] movieResults;

    public MainActivityFragment() {
    }

    @Override
    public void onStart(){
        super.onStart();
        updateMovies();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Enable menu for fragment
        setHasOptionsMenu(true);

        //If there is a saved instance, set the movie results array to the array saved
        if (savedInstanceState != null && savedInstanceState.containsKey("movies")){
            Parcelable[] parcels = savedInstanceState.getParcelableArray("movies");
            movieResults = new MovieDetails[parcels.length];
            for(int i = 0; i < parcels.length; i++){
                movieResults[i] = (MovieDetails)parcels[i];
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArray("movies", movieResults);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Set adapter to movie grid view
        GridView movieGrid = (GridView) rootView.findViewById(R.id.gridview_movies);
        imageAdapter = new ImageAdapter(getActivity());
        movieGrid.setAdapter(imageAdapter);
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent movieIntent = new Intent(getActivity(), DetailActivity.class);
                movieIntent.putExtra("MOVIEDETAILS", imageAdapter.getItem(position));
                startActivity(movieIntent);

            }
        });

        if(savedInstanceState != null && savedInstanceState.containsKey("movies")){
            imageAdapter.clearGrid();
            for (MovieDetails movie : movieResults) {
                imageAdapter.appendGrid(movie);
            }
            //Notify the adapter that the data has changed to refresh it
            imageAdapter.notifyDataSetChanged();
        }

        return rootView;
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, MovieDetails[]> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        //Function for retrieving movie data
        private MovieDetails[] getMovieData(String movieJsonStr)
                throws JSONException {

            JSONObject moviesJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray("results");
            movieResults = new MovieDetails[moviesArray.length()];

            for (int i = 0; i < moviesArray.length(); i++){
                JSONObject currentMovie = moviesArray.getJSONObject(i);
                movieResults[i] = new MovieDetails(
                        currentMovie.getString("original_title"),
                        currentMovie.getString("poster_path"),
                        currentMovie.getString("overview"),
                        currentMovie.getString("vote_average"),
                        currentMovie.getString("release_date"),
                        currentMovie.getString("id")
                        );
            }
            return movieResults;


        }

        @Override
        protected MovieDetails[] doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            String key = "REMOVED";

            try {

                final String MOVIE_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
                        .appendQueryParameter(API_KEY_PARAM, key)
                        .build();


                // Construct the URL for the TMDB query
                Log.v("rawrrr", builtUri.toString());
                URL url = new URL(builtUri.toString());

                // Create the request to TMDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    //Add newlines for easier debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //Create Json string from string buffer and output it to log
                moviesJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e("MainActivityFragment", "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("MainActivityFragment", "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieData(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieDetails[] result) {
            super.onPostExecute(result);
            if (result != null) {
                imageAdapter.clearGrid();
                for (MovieDetails movie : result) {
                    imageAdapter.appendGrid(movie);
                }
                //Notify the adapter that the data has changed to refresh it
                imageAdapter.notifyDataSetChanged();

            }

        }

    }

    private void updateMovies(){
        FetchMoviesTask movieTask = new FetchMoviesTask();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_by_value = sharedPref.getString(getString(R.string.sort_by_key), "0");
        if (sort_by_value.equals("0")){
            movieTask.execute("popularity.desc");
        }else if(sort_by_value.equals("1")){
            movieTask.execute("vote_average.desc");
        }else
            movieTask.execute("popularity.desc");
    }


}
