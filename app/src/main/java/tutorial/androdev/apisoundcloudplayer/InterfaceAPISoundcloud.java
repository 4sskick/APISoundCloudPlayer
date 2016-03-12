package tutorial.androdev.apisoundcloudplayer;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Administrator on 23-Feb-16.
 */
public interface InterfaceAPISoundcloud {

    /**
     * @GET & @QUERY is interface from retrofit
     */
    @GET("/tracks?client_id=" + Config.CLIENT_ID)
    public void getRecentTracks(@Query("created_at") String date, Callback<List<Track>> cb);
}
