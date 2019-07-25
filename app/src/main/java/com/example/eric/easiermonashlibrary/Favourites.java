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
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eric on 25/4/17.
 * A fragment which display all books in the user's favourite list.
 */
public class Favourites extends Fragment
{
    private TextView textView;
    private ListView listview;
    private CustomAdapter customAdapter;
    private ArrayList<Book> borrowerFavouriteBooks;
    private HashMap<String,Book> requestBooks;

    private StorageReference mStorageRef,specificRef;

    /**
     * Register all UI widget, create the view of the fragment. using a "CustomAdapter" object to display list of books
     * into listView from favourite book list.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        textView = (TextView) view.findViewById(R.id.tv_favourite_number);
        listview = (ListView) view.findViewById(R.id.listView_favourite);
        Bundle bundle = getArguments();
        borrowerFavouriteBooks = bundle.getParcelableArrayList("favourite");
        requestBooks = (HashMap)bundle.getSerializable("requestMaps");
        textView.setText(String.valueOf(borrowerFavouriteBooks.size())+" book in your favourite bag");
        customAdapter = new CustomAdapter(getContext(),borrowerFavouriteBooks);
        listview.setAdapter(customAdapter);

        return view;
    }

    /**
     * Called after view is created, adding listView item OnClickListener, once clicked, transfer
     * selected book item into "Book_detail" fragment.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null)
        {
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
                {
                    Book book = customAdapter.getItem(position);
//                    final HashMap<String,Object> rowItem = new HashMap<>();
//                    mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://monash-library.appspot.com");
//                    specificRef = mStorageRef.child("images/" + book.getISBN() + ".jpg");
//
//                    final long ONE_MEGABYTE = 1024 * 1024;
//                    specificRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                        @Override
//                        public void onSuccess(byte[] bytes)
//                        {
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                            rowItem.put("img",bitmap);
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception)
//                        {
//                            // Handle any errors
//                        }
//                    });
//                    rowItem.put("name",book.getBookName());
//                    rowItem.put("location",book.getLocation());
//                    rowItem.put("year", String.valueOf(book.getYear()));
//                    rowItem.put("author", book.getAuthor());
//                    rowItem.put("isbn", String.valueOf(book.getISBN()));
//                    rowItem.put("period", book.getPeriod());
//                    rowItem.put("description",book.getDescription());
                    Book_detail newBookDetailFragment = new Book_detail();
                    newBookDetailFragment.setObjectWithView(book,changeBookToString(borrowerFavouriteBooks),requestBooks);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragment_content,newBookDetailFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        }
    }


    /**
     * Change type of item in ArrayList from Book to String.
     */
    public ArrayList<String> changeBookToString(ArrayList<Book> source)
    {
        ArrayList<String> finalResult = new ArrayList<>();
        for (int j=0;j<source.size();j++)
        {
            String eachISBNString = String.valueOf(source.get(j).getISBN());
            finalResult.add(eachISBNString);
        }
        return finalResult;
    }
}
