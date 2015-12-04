package com.jackygwong.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        FetchTrailersTask trailerTask = new FetchTrailersTask();
        //trailerTask.execute("135397");
        if (intent != null && intent.hasExtra("MOVIEDETAILS")) {
            MovieDetails details = intent.getParcelableExtra("MOVIEDETAILS");
            ((TextView) rootView.findViewById(R.id.movie_title)).setText(details.original_title);
            ((TextView) rootView.findViewById(R.id.movie_synopsis)).setText(details.synopsis);
            ((TextView) rootView.findViewById(R.id.movie_user_rating)).setText("User rating: " + details.user_rating);
            ((TextView) rootView.findViewById(R.id.movie_release_date)).setText("Release date: " + details.release_date);

            String poster_url = "http://image.tmdb.org/t/p/w185/" + details.poster_path;
            ImageView poster = (ImageView) rootView.findViewById(R.id.movie_poster);
            Picasso.with(getContext()).load(poster_url).into(poster);


            trailerTask.execute(details.movie_id);

        }

        return rootView;
    }


    public class FetchTrailersTask extends AsyncTask<String, Void, TrailerDetails[]> {


        //Function for retrieving trailer data for movies
        private TrailerDetails[] getTrailers(String trailerJsonStr)
                throws JSONException {

            JSONObject trailerJson = new JSONObject(trailerJsonStr);
            JSONArray trailerJsonArray = trailerJson.getJSONArray("results");
            TrailerDetails[] trailersArray = new TrailerDetails[trailerJsonArray.length()];

            for (int i = 0; i < trailerJsonArray.length(); i++) {
                JSONObject currentTrailer = trailerJsonArray.getJSONObject(i);
                trailersArray[i] = new TrailerDetails(currentTrailer.getString("key"),
                        currentTrailer.getString("name"),
                        currentTrailer.getString("type"));
            }
            return trailersArray;

        }


        @Override
        protected TrailerDetails[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String trailerJsonStr = null;
            String key = "REMOVED";

            try {
                final String TRAILER_BASE_URL = "http://api.themoviedb.org/3/movie";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(TRAILER_BASE_URL).buildUpon()
                        .appendPath(params[0])
                        .appendPath("videos")
                        .appendQueryParameter(API_KEY_PARAM, key)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v("daf", builtUri.toString());
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

                trailerJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e("DetailsActivityFragment", "Error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("DetailsActivityFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                return getTrailers(trailerJsonStr);
            } catch (JSONException e) {
                Log.e("DetailsActivityFragment", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(TrailerDetails[] result) {
            super.onPostExecute(result);
            LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_details);
            if (result != null) {
                int trailer_num = 1;
                for (TrailerDetails trailer : result) {

                    if (trailer.type.equals("Trailer")) {
                        final String trailer_key = trailer.key;

                        //Access inflater outside of activity
                        //http://stackoverflow.com/questions/7803771/call-to-getlayoutinflater-in-places-not-in-activity
                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        //Inflate view from layout
                        //http://stackoverflow.com/questions/3142067/android-set-style-in-code
                        LinearLayout newLinearLayout = (LinearLayout) inflater.inflate(R.layout.trailer_layout, null);
                        //Set trailer text
                        TextView textView = (TextView) newLinearLayout.findViewById(R.id.list_item_trailer_textview);
                        textView.setText("Trailer " + String.valueOf(trailer_num++));
                        //Set on click to start youtube trailer intent
                        ImageView imageView = (ImageView) newLinearLayout.findViewById(R.id.trailer_play_button);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
                                final String KEY_PARAMETER = "v";

                                Uri builtUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                                        .appendQueryParameter(KEY_PARAMETER, trailer_key)
                                        .build();

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(builtUri);
                                startActivity(intent);
                            }
                        });
                        linearLayout.addView(newLinearLayout);
                    }

                }

            }
        }


    }


}
