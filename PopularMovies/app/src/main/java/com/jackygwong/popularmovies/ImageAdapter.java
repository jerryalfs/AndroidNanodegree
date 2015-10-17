package com.jackygwong.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by jwong on 10/4/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        //Use picasso to load the image from the provided URL
        Picasso.with(mContext).load(mThumbIds[position]).into(imageView);
        return imageView;
    }

    // references to our images
    private String[] mThumbIds = {
            "http://i.imgur.com/DvpvklR.png",
            "http://i.imgur.com/DvpvklR.png",
            "http://i.imgur.com/DvpvklR.png",
            "http://i.imgur.com/DvpvklR.png",
            "http://i.imgur.com/DvpvklR.png"
    };
}
