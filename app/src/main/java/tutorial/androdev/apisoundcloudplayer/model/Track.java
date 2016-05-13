package tutorial.androdev.apisoundcloudplayer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Septian on 23-Feb-16.
 * edited on 14-May-16
 * code to grab the data from JSON and create a Track object
 */
public class Track {
    /**
     * @SerializedName("")is a notation from gson lib
     * to serialize data to JSON format
     * followed by variable to assign
     */
    @SerializedName("title")
    private String mTitle;

    @SerializedName("id")
    private int mId;

    @SerializedName("stream_url")
    private String mStreamUrl;

    @SerializedName("artwork_url")
    private String mArtworkUrl;

    @SerializedName("genre")
    private String mGenre;

    public String getTitle(){
        return mTitle;
    }

    public int getId(){
        return mId;
    }

    public String getStreamUrl(){
        return mStreamUrl;
    }

    public String getArtworkUrl(){
        return mArtworkUrl;
    }

    public String getGenre(){
        return mGenre;
    }

}
