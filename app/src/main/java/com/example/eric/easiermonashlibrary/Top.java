package com.example.eric.easiermonashlibrary;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 25/4/17.
 * A fragment which display all books in the total book list with descending order based on book's
 * added times.
 */
public class Top extends Fragment
{
    private ListView listView;
    private ArrayList<Book> bookList;
    private ArrayList<String> borrowerFavouriteBooks;
    private HashMap<String,Book> requestBooks;
    private ArrayList<Map<String, Object>> mData;

    private CustomAdapter customAdapter;

    /**
     * Create the view of the fragment. getting the total book list from parent activity, using
     * collection.sort function to sort the total book list into descending order, then using a
     * "CustomAdapter" object to display list of books into listView.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_top, null);
        listView = (ListView) view.findViewById(R.id.listView);

        Bundle bundle = getArguments();
        bookList = bundle.getParcelableArrayList("totalBookList");
        borrowerFavouriteBooks = bundle.getStringArrayList("favourite");
        requestBooks = (HashMap)bundle.getSerializable("requestMaps");

        Collections.sort(bookList, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                //return 0;
                return o2.getAddTimes().compareTo(o1.getAddTimes());
            }
        });

        customAdapter = new CustomAdapter(getContext(),bookList);
        listView.setAdapter(customAdapter);
        return view;
    }

    /**
     * Called after view is created, adding the listView item selected listener, once clicked,
     * grabbing the book object from list and transmit to the "Book_detail" fragment.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if (view != null)
        {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
                {

                    Book book = customAdapter.getItem(position);

                    Book_detail newBookDetailFragment = new Book_detail();
                    newBookDetailFragment.setObjectWithView(book,borrowerFavouriteBooks,requestBooks);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragment_content,newBookDetailFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        }
    }
}

