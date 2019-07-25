package com.example.eric.easiermonashlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Eric on 26/4/17
 * Write a custom adapter extends from BaseAdapter, to implement displaying the
 * book object from ArrayList in listView.
 */
public class CustomAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Book> allBookList;
    private StorageReference mStorageRef,specificRef;

    /**
     * Default constructor for creating CustomAdapter object.
     */
    public CustomAdapter(Context context, ArrayList<Book> allBookList)
    {
        this.context = context;
        this.allBookList = allBookList;
    }

    /**
     * Get ArrayList size.
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount()
    {
        return allBookList.size();
    }

    /**
     * Get a specific object from list.
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Book getItem(int i)
    {
        return allBookList.get(i);
    }

    /**
     * Get itemId
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int i)
    {
        return i;
    }

    /**
     * Get each of the item's view. download the book's cover from firebase storage
     * and also judging the hard copy availability.
     * @see android.widget.Adapter#getView(int,View,ViewGroup)
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {

        // Check if the view has been created for the row. If not, inflate it
        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Reference list_monster_item layout here
            view = inflater.inflate(R.layout.item_list, null);
        }

        TextView bookName = (TextView) view.findViewById(R.id.bookTitle);
        TextView year = (TextView) view.findViewById(R.id.year);
        TextView author = (TextView) view.findViewById(R.id.author);
        TextView isbn = (TextView) view.findViewById(R.id.isbn);
        TextView period = (TextView) view.findViewById(R.id.period);
        TextView availability = (TextView) view.findViewById(R.id.ava);
        final ImageView imageView = (ImageView) view.findViewById(R.id.img);


        // Assign values to the TextViews using the Monster object
        bookName.setText(allBookList.get(i).getBookName());
        year.setText(String.valueOf(allBookList.get(i).getYear()));
        author.setText(allBookList.get(i).getAuthor());
        isbn.setText(String.valueOf(allBookList.get(i).getISBN()));
        period.setText(allBookList.get(i).getPeriod());

        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://monash-library.appspot.com");
        specificRef = mStorageRef.child("images/" + allBookList.get(i).getISBN() + ".jpg");

//        specificRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri)
//            {
//                // Got the download URL for 'users/me/profile.png'
//                String address = uri.toString();
//                new DownloadImageTask(imageView).execute(address);
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception)
//            {
//                // Handle any errors
//            }
//        });
            Book book = allBookList.get(i);
            mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://monash-library.appspot.com");
            specificRef = mStorageRef.child("images/" + book.getISBN() + ".jpg");

            final long ONE_MEGABYTE = 1024 * 1024;
            specificRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes)
                {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception)
                {
                    // Handle any errors
                }
            });

        String location = book.getLocation();
        String[] locationAndNumber = location.split(",");
        if (locationAndNumber.length == 6)
        {
            if (locationAndNumber[1].equals("0") && locationAndNumber[3].equals("0") && locationAndNumber[5].equals("0"))
            {
                availability.setText("Not Available");
                availability.setTextColor(Color.RED);
            }
        }
        else if (locationAndNumber.length == 4)
        {
            if (locationAndNumber[1].equals("0") && locationAndNumber[3].equals("0"))
            {
                availability.setText("Not Available");
                availability.setTextColor(Color.RED);
            }
        }
        else if (locationAndNumber.length == 2)
        {
            if (locationAndNumber[1].equals("0"))
            {
                availability.setText("Not Available");
                availability.setTextColor(Color.RED);
            }
        }
        return view;
    }

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
//    {
//        private final ImageView imgView;
//
//        public DownloadImageTask(ImageView imgView)
//        {
//            this.imgView = imgView;
//        }
//        @Override
//        protected Bitmap doInBackground(String...urls)
//        {
//            String url = urls[0];
//            Bitmap bitmap = null;
//            try
//            {
//                InputStream in = new URL(url).openStream();
//                bitmap = BitmapFactory.decodeStream(in);
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap last)
//        {
//            imgView.setImageBitmap(last);
//        }
//    }
}
