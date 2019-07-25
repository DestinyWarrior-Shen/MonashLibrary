package com.example.eric.easiermonashlibrary;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Eric on 27/5/17.
 * Fragment that display the acknowledgement and declaration.
 */
public class AboutPage extends Fragment
{
    /**
     * create the view of this fragment, show all content.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_about_page, container, false);
        return view;
    }
}
