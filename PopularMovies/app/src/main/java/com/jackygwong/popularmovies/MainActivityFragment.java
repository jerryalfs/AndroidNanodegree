package com.jackygwong.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into((ImageView)(rootView.findViewById(R.id.imageView)));

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
        List<String> titleArrayList = new ArrayList<String>(
                Arrays.asList(movieTitlesArray));

        ArrayAdapter<String> moviesAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.grid_item_text,
                R.id.grid_item_textview,
                titleArrayList);

        GridView movieGrid = (GridView) rootView.findViewById(R.id.gridview_movies);
        movieGrid.setAdapter(moviesAdapter);

        return rootView;
    }
}
