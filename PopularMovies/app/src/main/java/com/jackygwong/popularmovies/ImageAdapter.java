package com.jackygwong.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwong on 10/4/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public MovieDetails getItem(int position) {
        return mThumbIds.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public void clearGrid(){
        mThumbIds.clear();
    }

    public void appendGrid(MovieDetails movie){
        mThumbIds.add(movie);
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }


        //Use picasso to load the image from the provided URL
        String poster_url = "http://image.tmdb.org/t/p/w185/" + mThumbIds.get(position).poster_path;
        Picasso.with(mContext).load(poster_url).into(imageView);
        return imageView;
    }

    // references to our images
    //private List<String> mThumbIds = new ArrayList<String>();
    private List<MovieDetails> mThumbIds = new ArrayList<MovieDetails>();

}
