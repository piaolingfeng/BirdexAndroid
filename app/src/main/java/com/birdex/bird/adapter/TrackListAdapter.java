package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.Tracking;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hyj on 2016/4/7.
 */
public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.MyviewHolder>{

    private List<Tracking> trackings;
    private Context context;

    public TrackListAdapter(Context context, List<Tracking> trackings){
        this.context = context;
        this.trackings = trackings;
    }


    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyviewHolder(LayoutInflater.from(context).inflate(R.layout.track_adapter, null));
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {
        holder.describe.setText(trackings.get(position).getContext());
        holder.time.setText(trackings.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return trackings.size();
    }

    class MyviewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.context)
        TextView describe;
        @Bind(R.id.time)
        TextView time;

        public MyviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
