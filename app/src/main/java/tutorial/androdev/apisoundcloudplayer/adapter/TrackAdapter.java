package tutorial.androdev.apisoundcloudplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tutorial.androdev.apisoundcloudplayer.R;
import tutorial.androdev.apisoundcloudplayer.model.Track;

/**
 * Created by Septian on 25-Feb-16.
 * edited on 14-May-16
 */
public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackAdapterViewHolder> {

    private Context context;
    private List<Track> trackList;

    public TrackAdapter(Context context, List<Track> trackList) {
        this.context = context;
        this.trackList = trackList;
    }

    //this gonna be main layout to convert main layout into list of CardView
    @Override
    public TrackAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext())
                .inflate(R.layout.track_list_row, parent, false);

        return new TrackAdapterViewHolder(view);
    }

    //this method gonna initialize element of layout in cardview with Model
    @Override
    public void onBindViewHolder(TrackAdapterViewHolder holder, int position) {
        Track track = trackList.get(position);

        holder.titleTextView.setText(track.getTitle());
        holder.subTitleTextView.setText(track.getGenre());

        Picasso.with(context)
                .load(track.getArtworkUrl())
                .skipMemoryCache()
                .into(holder.trackImageView);
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }


    /**
     * adapter uses the ViewHolder design pattern which improves a list view’s performance and
     * Making ListView Scrolling Smooth
     * A ViewHolder object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly
     * http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
     */
    static class TrackAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView trackImageView;
        TextView titleTextView;
        TextView subTitleTextView;

        public TrackAdapterViewHolder(View itemView) {
            super(itemView);

            trackImageView = (ImageView) itemView.findViewById(R.id.track_image);
            titleTextView = (TextView) itemView.findViewById(R.id.track_title);
            subTitleTextView = (TextView) itemView.findViewById(R.id.track_subtitle);
        }
    }
}
