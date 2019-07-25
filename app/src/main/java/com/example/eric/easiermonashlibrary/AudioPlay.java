package com.example.eric.easiermonashlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;

/**
 * Created by Eric on 17/5/17.
 * A fragment which display all details information of book that user selected, providing add to favourite list function
 * and also provide the path to read book and get hard copy function.
 */
public class AudioPlay extends Fragment
{
    private Button play,pause,stop;
    private ImageView image;
    private TextView bookName,author,year,isbn;
    private HashMap<String,Object> specificBook;
    //private Uri uri;
    private MediaPlayer music;

    /**
     * Register all UI widget, and set these widgets contents.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_audio_play, container, false);
        play = (Button) view.findViewById(R.id.btn_play);
        pause = (Button) view.findViewById(R.id.btn_pause);
        stop = (Button) view.findViewById(R.id.btn_stop);
        image = (ImageView) view.findViewById(R.id.image_audio);
        bookName = (TextView) view.findViewById(R.id.bName);
        author = (TextView) view.findViewById(R.id.tvAudioAuthor);
        year = (TextView) view.findViewById(R.id.tvAudioYear);
        isbn = (TextView) view.findViewById(R.id.tvAudioISBN);

        image.setImageBitmap((Bitmap)specificBook.get("img"));
        bookName.setText((String)specificBook.get("name"));
        year.setText((String)specificBook.get("year"));
        author.setText((String)specificBook.get("author"));
        isbn.setText((String)specificBook.get("isbn"));

        return view;
    }

    /**
     * Called after view is created, adding listeners, and implement play, pause, stop music functions.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null)
        {
            music = MediaPlayer.create(getContext(),R.raw.example);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //music = MediaPlayer.create(getContext(),uri)
                    music.start();
                    play.setEnabled(false);
                    pause.setEnabled(true);
                    stop.setEnabled(true);
                }
            });

            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (music.isPlaying())
                    {
                        music.pause();
                        play.setEnabled(true);
                        pause.setEnabled(false);
                        stop.setEnabled(true);
                    }
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (music != null)
                    {
                        music.stop();
                        music = MediaPlayer.create(getContext(),R.raw.example);
                        play.setEnabled(true);
                        pause.setEnabled(true);
                        stop.setEnabled(false);
                    }
                }
            });
        }
    }

    /**
     * This method used for transfer the a book's details map from previous
     * fragment to this fragment.
     */
    public void transmitItemToAudioPlay(HashMap<String,Object> specificBook)
    {
        this.specificBook = specificBook;
    }
}
