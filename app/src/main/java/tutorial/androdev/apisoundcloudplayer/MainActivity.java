package tutorial.androdev.apisoundcloudplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * http://www.sitepoint.com/develop-music-streaming-android-app/
 */

public class MainActivity extends AppCompatActivity {

    private List<Track> listItems;
    private TrackAdapter trackAdapter;
    private ImageView selectedTrackImage;
    private TextView selectedTrackTitle;
    private MediaPlayer mediaPLayer;
    private ImageView playerControl;

    private static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        /**
//         * makes downloading JSON or XML data from a web API straightforward
//         * Once the data is downloaded, it is parsed into a Plain Old Java Object (POJO)
//         * which must be defined for each resource in the response.
//         */
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.API_URL).build();
//        InterfaceAPISoundcloud interfaceAPISoundcloud = restAdapter.create(InterfaceAPISoundcloud.class);
//        interfaceAPISoundcloud.getRecentTracks(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()), new Callback<List<Track>>() {
//            @Override
//            public void success(List<Track> tracks, Response response) {
//                Log.d(TAG, "First track title: "+tracks.get(0).getTitle());
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.d(TAG, "Error: "+error);
//            }
//        });
        /**
         * create object of media player
         */
        mediaPLayer = new MediaPlayer();
        mediaPLayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPLayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                togglePlayPause();
            }
        });

        /**
         * set adapter that was created in TrackAdapter class
         */
        listItems = new ArrayList<Track>();
        ListView listView = (ListView)findViewById(R.id.track_list_view);
        //inflate view
        trackAdapter = new TrackAdapter(this, listItems);
        listView.setAdapter(trackAdapter);

        selectedTrackImage = (ImageView)findViewById(R.id.selected_track_image);
        selectedTrackTitle = (TextView)findViewById(R.id.selected_track_title);
        playerControl = (ImageView)findViewById(R.id.player_control);


        //handle clicked item on list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = listItems.get(position);
                selectedTrackTitle.setText(track.getTitle());
                Picasso.with(MainActivity.this).load(track.getArtworkUrl()).into(selectedTrackImage);

                /**
                 * control media player
                 * check is made to see if the player is playing. If a track had been playing,
                 * it’s stopped and the media player is reset before the selected track can be played.
                 */
                if(mediaPLayer.isPlaying()){
                    mediaPLayer.stop();
                    //media player is reset before the selected track can be played
                    mediaPLayer.reset();
                }
                try{
                    //set data source to stream
                    mediaPLayer.setDataSource(track.getStreamUrl()+"?client_id="+Config.CLIENT_ID);
                    /**
                     * you can either call prepare() or prepareAsync() here,
                     * but for streams, you should call prepareAsync() which returns immediately,
                     * rather than blocking until enough data has buffered.
                     * For files, it’s OK to call prepare() which blocks until
                     * MediaPlayer is ready for playback.
                     */
                    mediaPLayer.prepareAsync();
                    playerControl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            togglePlayPause();
                        }
                    });

                    mediaPLayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playerControl.setImageResource(R.drawable.ic_play);
                        }
                    });
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        });

        InterfaceAPISoundcloud service = SoundCloud.getService();
        service.getRecentTracks(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()), new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                Log.d(TAG, "First track title: "+tracks.get(0).getTitle());
                loadTracks(tracks);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Error: "+error);
            }
        });
        /**
         * code above is new implementation with SoundCloud class
         * that was explained in it.
         */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPLayer != null){
            if(mediaPLayer.isPlaying()){
                mediaPLayer.stop();
            }
            mediaPLayer.release();
            mediaPLayer = null;
        }
    }

    private void togglePlayPause() {
        if(mediaPLayer.isPlaying()){
            mediaPLayer.pause();
            playerControl.setImageResource(R.drawable.ic_play);
        }else{
            mediaPLayer.start();
            playerControl.setImageResource(R.drawable.ic_pause);
        }
    }

    private void loadTracks(List<Track> tracks) {
        listItems.clear();
        listItems.addAll(tracks);
        //refresh data when data changed
        trackAdapter.notifyDataSetChanged();
    }
}
