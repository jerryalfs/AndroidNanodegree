package com.jackygwong.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jwong on 11/1/2015.
 */
public class MovieDetails implements Parcelable {
    public String original_title;
    public String poster_path;
    public String synopsis;
    public String user_rating;
    public String release_date;
    public String movie_id;

    public MovieDetails(String original_title, String poster_path, String synopsis, String user_rating, String release_date, String movie_id){
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.synopsis = synopsis;
        this.user_rating = user_rating;
        this.release_date = release_date;
        this.movie_id = movie_id;
    }

    private MovieDetails(Parcel in){
        original_title = in.readString();
        poster_path = in.readString();
        synopsis = in.readString();
        user_rating = in.readString();
        release_date = in.readString();
        movie_id = in.readString();
    }

    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(original_title);
        parcel.writeString(poster_path);
        parcel.writeString(synopsis);
        parcel.writeString(user_rating);
        parcel.writeString(release_date);
        parcel.writeString(movie_id);
    }

    public static final Parcelable.Creator<MovieDetails> CREATOR = new Parcelable.Creator<MovieDetails>(){
        @Override
        public MovieDetails createFromParcel(Parcel parcel) { return new MovieDetails(parcel);}

        @Override
        public MovieDetails[] newArray(int i) { return new MovieDetails[i]; }
    };
}
