package com.jackygwong.popularmovies;

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
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if(intent != null && intent.hasExtra("TITLE")) {
            String movie_title = intent.getStringExtra("TITLE");
            ((TextView) rootView.findViewById(R.id.movie_title)).setText(movie_title);
        }
        if(intent != null && intent.hasExtra("SYNOPSIS")) {
            String movie_synopsis = intent.getStringExtra("SYNOPSIS");
            ((TextView) rootView.findViewById(R.id.movie_synopsis)).setText(movie_synopsis);
        }
        if(intent != null && intent.hasExtra("RATING")) {
            String movie_rating = intent.getStringExtra("RATING");
            ((TextView) rootView.findViewById(R.id.movie_user_rating)).setText("Rating: " + movie_rating);
        }
        if(intent != null && intent.hasExtra("RELEASE")) {
            String movie_release = intent.getStringExtra("RELEASE");
            ((TextView) rootView.findViewById(R.id.movie_release_date)).setText("Release date: " + movie_release);
        }
        if(intent != null && intent.hasExtra("POSTER")) {
            String poster_url = "http://image.tmdb.org/t/p/w185/" + intent.getStringExtra("POSTER");
            ImageView poster = (ImageView)rootView.findViewById(R.id.movie_poster);
            Picasso.with(getContext()).load(poster_url).into(poster);
        }

        return rootView;
    }
}