package com.example.eric.easiermonashlibrary;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Eric on 15/5/17.
 * A fragment which display the PDF file.
 *
 * The coding in this class is reference to a YouTube tutorial: "Android Studio Tutorial: PDF Viewer"
 * https://www.youtube.com/watch?v=-Ld1IoOF_uk
 */
public class readPDF extends Fragment
{
    private PDFView pdfView;
    private String address;

    /**
     * Register the pdfView widget, create a anonymous RetrievePDFStream object, then execute the
     * URL from "Book_detail" fragment.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_read_pd, container, false);
        pdfView = (PDFView) view.findViewById(R.id.pdfView);

        new RetrievePDFStream().execute(address);
        return view;
    }

    /**
     * This method used for transfer the URL from "Book_detail" fragment to this fragment.
     */
    public void setUrlToReadPdf(String address)
    {
        this.address = address;
    }

    /**
     * This class extends the AsyncTask, implement the HttpURLConnnection to download the PDF file
     * from FireBase storage.
     */
    private class RetrievePDFStream extends AsyncTask<String,Void,InputStream>
    {
        @Override
        protected InputStream doInBackground(String...strings)
        {
            InputStream inputStream = null;
            try
            {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                if (urlConnection.getResponseCode() == 200)
                {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }
            catch (IOException e)
            {
                return null;
            }

            return inputStream;

        }

        /**
         * This method execute after the "doInBackground" method, it reads pdf file and display it
         * on the pdfView.
         */
        @Override
        protected void onPostExecute(InputStream inputStream)
        {
            pdfView.fromStream(inputStream).load();
        }
    }
}
