package com.example.ayush.slider;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Ayush on 7/2/2016.
 */
public class FragmentB extends Fragment{

    View view;
    RecyclerView recyclerView;
    SeekBar seekBar;
    Context context;
    ArrayList<File> recordings;
    MediaPlayer mediaPlayer;
    RelativeLayout relativeLayout;
    Button stop,playpause;
    private int noOfFiles;
    public FragmentB(){
        mediaPlayer = new MediaPlayer();
        scanAppFolder();
        noOfFiles=0;
     }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view = getView();
        context = getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.recorded);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        playpause = (Button) view.findViewById(R.id.playpause);
        stop = (Button) view.findViewById(R.id.stopcontrol);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.controls);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new RecordingsAdapter(context,recordings));
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
            {
                View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(child!=null && mGestureDetector.onTouchEvent(e))
                {
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.reset();
                    }
                    if(relativeLayout.getVisibility() == View.INVISIBLE){
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                    try
                    {
                        mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()
                                +File.separator
                                +MainActivity.appFoldername
                                +File.separator
                                +recordings.get(recyclerView.getChildAdapterPosition(child)).getName());
                        mediaPlayer.prepare();
                        seekBar.setProgress(0);
                        seekBar.setMax(mediaPlayer.getDuration());
                        mediaPlayer.start();
                        trackseeker();
                    }
                    catch (IOException ex)
                    {
                        Log.d("test", "onClick: "+ex.getMessage());
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.release();
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    void scanAppFolder(){
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+MainActivity.appFoldername);
        if(directory.isDirectory())
        {
            recordings = new ArrayList<>(Arrays.asList(directory.listFiles()));
        }
        if(noOfFiles<recordings.size() && recyclerView!=null)
        {
            Log.d("scanner", "scanAppFolder: ");
            recyclerView.getAdapter().notifyDataSetChanged();
        }
        noOfFiles = recordings.size();


    }


    final GestureDetector mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
    });

    void trackseeker(){
        Observable<Integer> tracker = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try
                {
                    while (true)
                    {
                        if(mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition()<1000) break;
                        subscriber.onNext(mediaPlayer.getCurrentPosition());
                        SystemClock.sleep(1000);
                        Log.d("ReactiveX", "call: "+mediaPlayer.isPlaying());
                    }
                    subscriber.onCompleted();
                }
                catch (Exception e)
                {
                    subscriber.onError(e);
                }
            }
        });

        tracker.subscribeOn(Schedulers.newThread())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Observer<Integer>() {
                   @Override
                   public void onCompleted() {
                       Log.d("ReactiveX", "onCompleted: completed");
                       mediaPlayer.reset();
                       relativeLayout.setVisibility(View.INVISIBLE);
                   }

                   @Override
                   public void onError(Throwable e) {
                       Log.d("ReactiveX", "onError: "+e.getMessage());
                   }

                   @Override
                   public void onNext(Integer integer) {
                        seekBar.setProgress(integer);
                   }
               });
    }
}
