package tutorial.androdev.apisoundcloudplayer.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tutorial.androdev.apisoundcloudplayer.Config;
import tutorial.androdev.apisoundcloudplayer.InterfaceAPISoundcloud;
import tutorial.androdev.apisoundcloudplayer.R;
import tutorial.androdev.apisoundcloudplayer.adapter.RecyclerItemClickListener;
import tutorial.androdev.apisoundcloudplayer.adapter.TrackAdapter;
import tutorial.androdev.apisoundcloudplayer.model.Track;

public class MainActivity extends AppCompatActivity {

    private List<Track> listItems;
    private TrackAdapter trackAdapter;
    private ImageView selectedTrackImage;
    private TextView selectedTrackTitle;
    private MediaPlayer mediaPLayer;
    private ImageView playerControl;
    private Context context;

    private static String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.track_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        //inflate view
        trackAdapter = new TrackAdapter(this, listItems);

        //set layout
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(trackAdapter);

        selectedTrackImage = (ImageView) findViewById(R.id.selected_track_image);
        selectedTrackTitle = (TextView) findViewById(R.id.selected_track_title);
        playerControl = (ImageView) findViewById(R.id.player_control);

        //handle clicked item on list
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClickListener(View view, int position) {
                                Track track = listItems.get(position);
                                selectedTrackTitle.setText(track.getTitle());

                                //load image
                                Picasso.with(MainActivity.this)
                                        .load(track.getArtworkUrl())
                                        .into(selectedTrackImage);

                                /**
                                 * control media player
                                 * check is made to see if the player is playing. If a track had been playing,
                                 * it’s stopped and the media player is reset before the selected track can be played.
                                 */
                                if (mediaPLayer.isPlaying()) {
                                    mediaPLayer.stop();
                                    //media player is reset before the selected track can be played
                                    mediaPLayer.reset();
                                }
                                try {
                                    //set data source to stream
                                    mediaPLayer.setDataSource(track.getStreamUrl() + "?client_id=" + Config.CLIENT_ID);

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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                )
        );

        /*
        retrofit ver. 2.0
         */
        /**
         * makes downloading JSON or XML data from a web API straightforward
         * Once the data is downloaded, it is parsed into a Plain Old Java Object (POJO)
         * which must be defined for each resource in the response.
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //create service
        InterfaceAPISoundcloud service = retrofit.create(InterfaceAPISoundcloud.class);
        //call service
        final Call<List<Track>> trackCall = service.getRecentTracks(
                new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date())
        );

        //starting to implement method of service
        trackCall.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> trackListCall, Response<List<Track>> response) {
                loadTracks(response.body());
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {

            }
        });
    }

    private void togglePlayPause() {
        if (mediaPLayer.isPlaying()) {
            mediaPLayer.pause();
            playerControl.setImageResource(R.drawable.ic_play);
        } else {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPLayer != null) {
            if (mediaPLayer.isPlaying()) {
                mediaPLayer.stop();
            }
            mediaPLayer.release();
            mediaPLayer = null;
        }
    }
}
