package com.example.ayush.slider;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ayush on 7/4/2016.
 */
public class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.CustomViewHolder> {

    LayoutInflater layoutInflater;
    Context context;
    ArrayList<File> recordings;

    RecordingsAdapter(Context context, ArrayList<File> recordings){
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
        this.recordings = recordings;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(layoutInflater.inflate(R.layout.child_recordinglayout,parent,false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.child.setText(recordings.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return recordings.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{

        TextView child;
        public CustomViewHolder(View itemView) {
            super(itemView);
            child = (TextView) itemView.findViewById(R.id.childrecord);
        }
//
//        @Override
//        public void onClick(View view) {
//
//            if(mediaPlayer.isPlaying())
//            {
//                mediaPlayer.reset();
//            }
//            else
//            {
//
//            }
//            try
//            {
//                mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()
//                        +File.separator
//                        +MainActivity.appFoldername
//                        +File.separator
//                        +recordings.get(getAdapterPosition()).getName());
//                mediaPlayer.prepare();
//                mediaPlayer.start();
//            }
//            catch (IOException e) {
//                Log.d("test", "onClick: "+e.getMessage());
//            }
//        }

    }
}
