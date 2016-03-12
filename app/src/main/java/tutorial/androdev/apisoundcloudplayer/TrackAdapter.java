package tutorial.androdev.apisoundcloudplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 25-Feb-16.
 */
public class TrackAdapter extends BaseAdapter {

    private Context context;
    private List<Track> tracks;

    public TrackAdapter(Context context, List<Track> tracks){
        this.context = context;
        this.tracks = tracks;
    }

    @Override
    public int getCount() {
        return this.tracks.size();
    }

    @Override
    public Object getItem(int position) {
        return this.tracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * in getView(int, View, ViewGroup) we populate the ViewHolder and store it inside the layout.
         * After this, each view can now be accessed without the need for the look-up, saving valuable processor cycles.
         */
        Track track = (Track) getItem(position);
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(this.context).inflate(R.layout.track_list_row, parent, false);
            holder = new ViewHolder();
            holder.trackImageView = (ImageView)convertView.findViewById(R.id.track_image);
            holder.titleTextView = (TextView)convertView.findViewById(R.id.track_title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleTextView.setText(track.getTitle());

        //trigger to download of URL asynchronously into imageView
        Picasso.with(this.context).load(track.getArtworkUrl()).into(holder.trackImageView);
        return convertView;
    }

    /**
     * adapter uses the ViewHolder design pattern which improves a list view’s performance and
     * Making ListView Scrolling Smooth
     * A ViewHolder object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly
     * http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
     */
    static class ViewHolder{
        ImageView trackImageView;
        TextView titleTextView;
    }
}
