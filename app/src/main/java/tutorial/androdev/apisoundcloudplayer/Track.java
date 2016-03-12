package tutorial.androdev.apisoundcloudplayer;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 23-Feb-16.
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

}
