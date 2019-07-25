package com.example.eric.easiermonashlibrary;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 29/4/17.
 * A fragment which display all books in the category that user select in "Category" fragment,
 * show them in a listView.
 */
public class Category_result extends Fragment
{
    private ListView listView;
    private ArrayList<Book> categoryResultList;
    private ArrayList<String> borrowerFavouriteBooks;
    private HashMap<String,Book> requestBooks;
    private CustomAdapter customAdapter;
//    private ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    private StorageReference mStorageRef,specificRef;

    /**
     * Create the view of the fragment. using a "CustomAdapter" object to display list of book
     * into listView.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category_result, container, false);
        listView = (ListView) view.findViewById(R.id.listView_category_result);

        customAdapter = new CustomAdapter(getContext(),categoryResultList);
//        for(int i=0; i<categoryResultList.size(); i++)
//        {
//            final Map<String,Object> eachRowItem = new HashMap<String,Object>();
//            Book book = categoryResultList.get(i);
//            eachRowItem.put("name",book.getBookName());
//            eachRowItem.put("location",book.getLocation());
//            eachRowItem.put("year", String.valueOf(book.getYear()));
//            eachRowItem.put("author", book.getAuthor());
//            eachRowItem.put("isbn", String.valueOf(book.getISBN()));
//            eachRowItem.put("period", book.getPeriod());
//            eachRowItem.put("description",book.getDescription());
//
//            mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://monash-library.appspot.com");
//            specificRef = mStorageRef.child("images/"+book.getISBN() +".jpg");
//
//            final long ONE_MEGABYTE = 1024 * 1024;
//            specificRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                @Override
//                public void onSuccess(byte[] bytes)
//                {
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    eachRowItem.put("img",bitmap);
//                    //image.setImageBitmap(bitmap);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle any errors
//                }
//            });
////            if (book.getLocation().equals("Sir Louis Matheson Library"))
////            {
////                eachRowItem.put("img", R.drawable.b);
////            }
////            else
////            {
////                eachRowItem.put("img", R.drawable.c);
////            }
//            mData.add(eachRowItem);
//        }
//        SimpleAdapter adapter = new SimpleAdapter(getActivity(),mData,R.layout.item_list,
//                new String[] {"name","year","author","isbn","period","img"},
//                new int[]{R.id.title,R.id.year,R.id.author,R.id.isbn,R.id.period,R.id.img});
//
//        adapter.setViewBinder(new ViewBinder(){
//
//            @Override
//            public boolean setViewValue(View view, Object data, String textRepresentation)
//            {
//                if( (view instanceof ImageView) && (data instanceof Bitmap) )
//                {
//                    ImageView iv = (ImageView) view;
//                    Bitmap bm = (Bitmap) data;
//                    iv.setImageBitmap(bm);
//                    return true;
//                }
//                return false;
//            }
//        });
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
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
                {
                    Book book = (Book) customAdapter.getItem(position);
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
                    newBookDetailFragment.setObjectWithView(book,borrowerFavouriteBooks,requestBooks);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragment_content,newBookDetailFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
//                {
// //                   Book book = new Book();
//                    HashMap<String,Object> singleBook= new HashMap<>();
//                    singleBook = (HashMap<String,Object>) listView.getAdapter().getItem(position);
//
////                    book.setBookName((String)singleBook.get("name"));
////                    book.setLocation((String)singleBook.get("location"));
////                    book.setYear(Integer.parseInt((String)singleBook.get("year")));
////                    book.setAuthor((String)singleBook.get("author"));
////                    book.setISBN(Integer.parseInt((String)singleBook.get("isbn")));
////                    book.setPeroid((String)singleBook.get("period"));
////                    book.setDescription((String)singleBook.get("description"));
//
//                    Book_detail newBookDetailFragment = new Book_detail();
//                    newBookDetailFragment.setObjectWithView(singleBook);
//                    getActivity().getFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_content,newBookDetailFragment).commit();
//                }
//
//            });
        }
    }

    /**
     * This method used for transfer the corresponding bookList, borrower's favourite book list and request list from
     * "Category" fragment to this fragment.
     */
    public void transmitItemToCategoryResult(ArrayList<Book> searchResultList,ArrayList<String> borrowerFavouriteBooks,HashMap<String,Book> requestBooks)
    {
        this.categoryResultList = searchResultList;
        this.borrowerFavouriteBooks = borrowerFavouriteBooks;
        this.requestBooks = requestBooks;
    }
}
