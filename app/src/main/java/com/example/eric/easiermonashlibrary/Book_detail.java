package com.example.eric.easiermonashlibrary;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eric on 1/5/17.
 * A fragment which display all details information of book that user selected, providing add to favourite list function
 * and also provide the path to read book and get hard copy function.
 */
public class Book_detail extends Fragment
{
    private ImageView image;
    private TextView bookName,ISBN,author,year,period,description,availability;
    private Button read,audio_play,hard_copy,add;
    private HashMap<String,Object> bookMap;
    private Book book;
    private ArrayList<String> borrowerFavouriteBooks,artList,biologyList,designList,financeList,itList,medicalList;
    private HashMap<ArrayList<String>,String> whole;
    private HashMap<String,Book> requestBooks;
    private boolean repeat,status;

    private FirebaseAuth auth;
    private StorageReference mStorageRef,onePdfRef,specificRef,oneMp3Ref;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRootRef,favouriteBookRef,bookRef;
    private FirebaseUser user;
    private String uid;

    /**
     * Create the view of this fragment, show all contents. Before display the view, this method also implement
     * (1) Download book's cover from firebase storage, so that transmit image to following fragment, no need to download again.
     * (2) Identify the book's categories, so that locate book's location in the firebase database.
     * (3) Judge book's hard copy current availability.
     * (4) Judge whether this book is already in user's favourite list, if so, change add button text to "Remove"
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        image = (ImageView) view.findViewById(R.id.thumbnails);
        bookName = (TextView) view.findViewById(R.id.tv_book_name);
        year = (TextView) view.findViewById(R.id.tv_year);
        author = (TextView) view.findViewById(R.id.tv_author);
        ISBN = (TextView) view.findViewById(R.id.tv_isbn);
        period = (TextView) view.findViewById(R.id.tv_period);
        description = (TextView) view.findViewById(R.id.tfDescription);
        availability = (TextView) view.findViewById(R.id.tv_availability);

        add = (Button) view.findViewById(R.id.addToFavourite);
        read = (Button) view.findViewById(R.id.btnRead);
        audio_play = (Button) view.findViewById(R.id.btnAudioPlay);
        hard_copy = (Button) view.findViewById(R.id.btnHardcopy);

        bookMap = new HashMap<>();
        artList = new ArrayList<>();
        biologyList = new ArrayList<>();
        designList = new ArrayList<>();
        financeList = new ArrayList<>();
        itList = new ArrayList<>();
        medicalList = new ArrayList<>();

        whole = new HashMap<>();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRootRef = mDatabase.getReference();

        bookRef = mDatabaseRootRef.child("Books");
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if (snapshot.getKey().equals("art"))
                    {
                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            artList.add(snapshot1.getKey());
                        }
                        whole.put(artList,snapshot.getKey());
                    }
                    if (snapshot.getKey().equals(("biology")))
                    {
                        for (DataSnapshot snapshot2 : snapshot.getChildren())
                        {
                            biologyList.add(snapshot2.getKey());
                        }
                        whole.put(biologyList,snapshot.getKey());
                    }
                    if (snapshot.getKey().equals("it"))
                    {
                        for (DataSnapshot snapshot3 : snapshot.getChildren())
                        {
                            itList.add(snapshot3.getKey());
                        }
                        whole.put(itList,snapshot.getKey());
                    }
                    if (snapshot.getKey().equals("design"))
                    {
                        for (DataSnapshot snapshot4 : snapshot.getChildren())
                        {
                            designList.add(snapshot4.getKey());
                        }
                        whole.put(designList,snapshot.getKey());
                    }
                    if (snapshot.getKey().equals("finance"))
                    {
                        for (DataSnapshot snapshot5 : snapshot.getChildren())
                        {
                            financeList.add(snapshot5.getKey());
                        }
                        whole.put(financeList,snapshot.getKey());
                    }
                    if (snapshot.getKey().equals("medical"))
                    {
                        for (DataSnapshot snapshot6 : snapshot.getChildren())
                        {
                            medicalList.add(snapshot6.getKey());
                        }
                        whole.put(medicalList,snapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });


        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://monash-library.appspot.com");
        specificRef = mStorageRef.child("images/" + book.getISBN() + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        specificRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes)
            {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image.setImageBitmap(bitmap);
                bookMap.put("img",bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception)
            {
                // Handle any errors
            }
        });
        bookMap.put("name",book.getBookName());
        bookMap.put("location",book.getLocation());
        bookMap.put("year", String.valueOf(book.getYear()));
        bookMap.put("author", book.getAuthor());
        bookMap.put("isbn", String.valueOf(book.getISBN()));
        bookMap.put("period", book.getPeriod());
        bookMap.put("description",book.getDescription());


        repeat = false;
        status = true;
        String location = (String)bookMap.get("location");
        String[] locationAndNumber = location.split(",");
        if (locationAndNumber.length == 6)
        {
            if (locationAndNumber[1].equals("0") && locationAndNumber[3].equals("0") && locationAndNumber[5].equals("0"))
            {
                hard_copy.setText("Request Hard Copy");
                availability.setText("Not Available");
                availability.setTextColor(Color.RED);
                status = false;
            }
        }
        else if (locationAndNumber.length == 4)
        {
            if (locationAndNumber[1].equals("0") && locationAndNumber[3].equals("0"))
            {
                hard_copy.setText("Request Hard Copy");
                availability.setText("Not Available");
                availability.setTextColor(Color.RED);
                status = false;
            }
        }
        else if (locationAndNumber.length == 2)
        {
            if (locationAndNumber[1].equals("0"))
            {
                hard_copy.setText("Request Hard Copy");
                availability.setText("Not Available");
                availability.setTextColor(Color.RED);
                status = false;
            }
        }

        for (int n=0;n<borrowerFavouriteBooks.size();n++)
        {
            if (((String)bookMap.get("isbn")).equals(borrowerFavouriteBooks.get(n)))
            {
                repeat = true;
            }
        }

        if (repeat)
        {
            add.setText("Remove");
        }

        bookName.setText((String)bookMap.get("name"));
        year.setText((String)bookMap.get("year"));
        author.setText((String)bookMap.get("author"));
        ISBN.setText((String)bookMap.get("isbn"));
        period.setText((String)bookMap.get("period"));
        description.setText((String)bookMap.get("description"));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
//        specificRef = mStorageRef.child("images/"+recievedBook.getISBN() +".jpg");
//
//        final long ONE_MEGABYTE = 1024 * 1024;
//        specificRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes)
//            {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                image.setImageBitmap(bitmap);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });

//        if (recievedBook.getLocation().equals("Sir Louis Matheson Library"))
//        {
//            image.setImageResource(R.drawable.b);
//        }
//        else
//        {
//            image.setImageResource(R.drawable.c);
//        }
        return view;
    }


    /**
     * This method is called after view created. It is implement:
     * (1) According to book's ISBN, get the corresponding pdf file's download URL from firebase storage, and sent to "readPDF"
     *     fragment to display PDF.
     * (2) Jump to the "Pickup_Request" fragment to view pickup or request details.
     * (3) Add or remove the book to or from the favourite list, and store the result into firebase database.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null)
        {
            mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://monash-library.appspot.com");

            onePdfRef = mStorageRef.child("pdf/" + ((String)bookMap.get("isbn")) + ".pdf");
            read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    onePdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            // Got the download URL for 'users/me/profile.png'
                            String address = uri.toString();
                            readPDF read = new readPDF();
                            read.setUrlToReadPdf(address);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            ft.replace(R.id.fragment_content,read);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception)
                        {
                            // Handle any errors
                        }
                    });
                }
            });

//            oneMp3Ref = mStorageRef.child("mp3/" + ((String)bookMap.get("isbn")) + ".mp3");
            audio_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
//                    oneMp3Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri)
//                        {
//                            AudioPlay audio = new AudioPlay();
//                            audio.transmitItemToAudioPlay(uri,bookMap);
//                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                            ft.replace(R.id.fragment_content,audio);
//                            ft.addToBackStack(null);
//                            ft.commit();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                        }
//                    });
                    AudioPlay audio = new AudioPlay();
                    audio.transmitItemToAudioPlay(bookMap);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragment_content,audio);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            hard_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    PickUp_Request pickUp_request = new PickUp_Request();
                    pickUp_request.setItemToRequest(bookMap,requestBooks,status);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragment_content,pickUp_request);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (!repeat)
                    {
                            borrowerFavouriteBooks.add((String) bookMap.get("isbn"));
                            favouriteBookRef = mDatabaseRootRef.child("Users").child(uid).child("favouriteBooks");
                            favouriteBookRef.setValue(borrowerFavouriteBooks);

                            add.setText("Remove");
                            repeat = true;

                            String category = "";

                            ArrayList<ArrayList<String>> keySet = new ArrayList<>(whole.keySet());
                            for (int a=0;a<keySet.size();a++)
                            {
                                for (int b=0;b<keySet.get(a).size();b++)
                                {
                                    if (book.getISBN() == Integer.parseInt(keySet.get(a).get(b)))
                                    {
                                        category = whole.get(keySet.get(a));
                                        break;
                                    }
                                }
                            }
                            book.addOneTime();
                            int finalTimes = book.getAddTimes();
                            bookRef.child(category).child(String.valueOf(book.getISBN())).child("addTimes").setValue(finalTimes);
                    }
                    else
                    {
                        removeBookFromFavouriteList((String)bookMap.get("isbn"));
                        favouriteBookRef = mDatabaseRootRef.child("Users").child(uid).child("favouriteBooks");
                        favouriteBookRef.setValue(borrowerFavouriteBooks);
                        add.setText("ADD");
                        repeat = false;
                    }
                }
            });
        }
    }

    /**
     * This method used for transfer the book object, borrower's favourite book list and request list from previous
     * fragment to this fragment.
     */
    public void setObjectWithView(Book book, ArrayList<String> borrowerFavouriteBooks, HashMap<String,Book> requestBooks)
    {
        this.book = book;
        this.borrowerFavouriteBooks = borrowerFavouriteBooks;
        this.requestBooks = requestBooks;
    }

    /**
     * Giving a book's isbn, so that to remove the books from the favourite book list and also from the database.
     */
    public void removeBookFromFavouriteList(String isbn)
    {
        for (int m=0;m<borrowerFavouriteBooks.size();m++)
        {
            if (isbn.equals(borrowerFavouriteBooks.get(m)))
            {
                borrowerFavouriteBooks.remove(m);
            }
        }
    }
}
