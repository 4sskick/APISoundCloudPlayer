package tutorial.androdev.apisoundcloudplayer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tutorial.androdev.apisoundcloudplayer.model.Track;

/**
 * Created by Septian on 23-Feb-16.
 * edited on 14-May-16
 */
public interface InterfaceAPISoundcloud {

    /**
     * @GET & @QUERY is interface from retrofit
     */
    @GET("/tracks?client_id="+BuildConfig.API_SOUNDCLOUD)
    Call<List<Track>> getRecentTracks(@Query("created_at") String date);
}
