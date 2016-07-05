package com.example.ayush.slider;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Ayush on 7/2/2016.
 */
public class FragmentA extends Fragment {

    private FloatingActionButton fab;
    private View root;
    private MediaRecorder mediaRecorder;
    private Chronometer chronometer;
    private boolean recording=false;
    public FragmentA(){}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        root=getView();
        fab = (FloatingActionButton) root.findViewById(R.id.fab);
        chronometer = (Chronometer) root.findViewById(R.id.chronos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(!recording){
                        recorderInit();
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        chronometer.setVisibility(View.VISIBLE);
                        chronometer.start();
                        recording=true;
                    }
                    else
                    {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        chronometer.stop();
                        recording=false;
                    }

                }
                catch (IOException e) {}
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.microphone_layout,container,false);
    }

    public void recorderInit(){
        boolean success=true;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+MainActivity.appFoldername);
        if(!folder.exists())
        {
            success = folder.mkdir();
        }
        if (success){
            mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath()
                    +File.separator
                    +MainActivity.appFoldername
                    +File.separator
                    +"/record"
                    +System.currentTimeMillis()
                    +".3gp");
        }
        chronometer.setBase(SystemClock.elapsedRealtime());
    }
}