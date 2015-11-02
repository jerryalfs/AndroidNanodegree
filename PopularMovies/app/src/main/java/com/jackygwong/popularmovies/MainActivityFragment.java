package com.jackygwong.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Enable menu for fragment
        setHasOptionsMenu(true);
        FetchMoviesTask movieTask = new FetchMoviesTask();
        movieTask.execute("popularity.desc");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchMoviesTask movieTask = new FetchMoviesTask();
            movieTask.execute("popularity.desc");
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
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
                Intent movieIntent = new Intent(getActivity(), DetailActivity.class);
                movieIntent.putExtra("TITLE", imageAdapter.getItem(position).original_title);
                movieIntent.putExtra("SYNOPSIS", imageAdapter.getItem(position).synopsis);
                movieIntent.putExtra("RATING", imageAdapter.getItem(position).user_rating);
                movieIntent.putExtra("RELEASE", imageAdapter.getItem(position).release_date);
                movieIntent.putExtra("POSTER", imageAdapter.getItem(position).poster_path);
                startActivity(movieIntent);

            }
        });

        return rootView;
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, MovieDetails[]> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        //Function for retrieving backdrop codes for movies
        private MovieDetails[] getMovieData(String movieJsonStr)
                throws JSONException {

            JSONObject moviesJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray("results");
            MovieDetails[] movieResults = new MovieDetails[moviesArray.length()];

            for (int i = 0; i < moviesArray.length(); i++){
                JSONObject currentMovie = moviesArray.getJSONObject(i);
                movieResults[i] = new MovieDetails();
                movieResults[i].poster_path = currentMovie.getString("poster_path");
                movieResults[i].original_title = currentMovie.getString("original_title");
                movieResults[i].release_date = currentMovie.getString("release_date");
                movieResults[i].synopsis = currentMovie.getString("overview");
                movieResults[i].user_rating = currentMovie.getString("vote_average");
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


}
