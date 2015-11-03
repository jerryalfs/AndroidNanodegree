package com.jackygwong.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if(intent != null && intent.hasExtra("MOVIEDETAILS")){
            MovieDetails details = intent.getParcelableExtra("MOVIEDETAILS");
            ((TextView) rootView.findViewById(R.id.movie_title)).setText(details.original_title);
            ((TextView) rootView.findViewById(R.id.movie_synopsis)).setText(details.synopsis);
            ((TextView) rootView.findViewById(R.id.movie_user_rating)).setText("User rating: " + details.user_rating);
            ((TextView) rootView.findViewById(R.id.movie_release_date)).setText("Release date: " + details.release_date);

            String poster_url = "http://image.tmdb.org/t/p/w185/" + details.poster_path;
            ImageView poster = (ImageView)rootView.findViewById(R.id.movie_poster);
            Picasso.with(getContext()).load(poster_url).into(poster);

        }

        return rootView;
    }
}
