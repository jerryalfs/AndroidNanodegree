package com.jackygwong.popularmovies;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Enable menu for fragment
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_refresh){
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

        String[] movieTitlesArray = {
                "Movie1",
                "Movie2",
                "Movie3",
                "Movie3",
                "Movie3",
                "Movie3",
                "Movie3",
                "Movie3",
                "Movie3",
                "Movie3",
                "Movie3",
                "Movie3",
                "Movie3",
                "Movie3",
                "Movie3",
                "Movie3",
                "Movie3",
                "Movie4"
        };
        /*
        List<String> titleArrayList = new ArrayList<String>(
                Arrays.asList(movieTitlesArray));

        ArrayAdapter<String> moviesAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.grid_item_text,
                R.id.grid_item_textview,
                titleArrayList);
*/
        GridView movieGrid = (GridView) rootView.findViewById(R.id.gridview_movies);
        movieGrid.setAdapter(new ImageAdapter(getActivity()));

        return rootView;
    }



    public class FetchMoviesTask extends AsyncTask<String, Void, String[]>{
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        //Function for retrieving backdrop codes for movies
        private String[] getMoviePosterData(String movieJsonStr)
                throws JSONException {

            JSONObject moviesJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray("results");
            String[] resultsStr = new String[moviesArray.length()];
            for(int i = 0; i < moviesArray.length(); i++){
                JSONObject currentMovie = moviesArray.getJSONObject(i);
                resultsStr[i] = currentMovie.getString("backdrop_path");
            }

            for (String s : resultsStr){
                Log.v(LOG_TAG, "Movie backdrop: " + s);
            }
            return resultsStr;




        }

        @Override
        protected String[] doInBackground(String... params){

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            String key = "***REMOVED***";

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
                //URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=***REMOVED***");
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());


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
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //Create Json string from string buffer and output it to log
                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Movie String:" + moviesJsonStr);

            } catch (IOException e) {
                Log.e("MainActivityFragment", "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
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

            try{
                return getMoviePosterData(moviesJsonStr);
            }catch(JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

    }


}
