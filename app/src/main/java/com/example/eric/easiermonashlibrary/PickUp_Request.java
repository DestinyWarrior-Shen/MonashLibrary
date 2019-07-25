package com.example.eric.easiermonashlibrary;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Eric on 10/5/17.
 * A fragment which display all detail information about a book, including the book location.
 */
public class PickUp_Request extends Fragment
{
    private Spinner spinner;
    private ArrayList<String> availableLocation;
    private HashMap<String,Book> requestBooks;
    private HashMap<String,Object> receivedMap;
    private ArrayAdapter<String> arrayAdapter;
    private boolean status;
    private ImageView image;
    private TextView bookName,year,author,ISBN,period;
    private Button viewDetails;
    private String itemSelected;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRootRef,requestBookRef;
    private FirebaseUser user;
    private String uid;
    private FirebaseAuth auth;


    /**
     * Create the view of this fragment, show all contents. Before display the view, this method also implement
     * (1) Setting the items in the spinner, based on the books information.
     * (2) Setting button text to "Send Request"if hard copy currently not available.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_pick_up__request, container, false);
        spinner = (Spinner) view.findViewById(R.id.sp_location);
        availableLocation = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRootRef = mDatabase.getReference();

        image = (ImageView) view.findViewById(R.id.ima);
        bookName = (TextView) view.findViewById(R.id.tv_bookName1);
        year = (TextView) view.findViewById(R.id.tv_year1);
        author = (TextView) view.findViewById(R.id.tv_author1);
        ISBN = (TextView) view.findViewById(R.id.tv_isbn1);
        period = (TextView) view.findViewById(R.id.tv_period1);
        viewDetails = (Button) view.findViewById(R.id.btn_details);


        image.setImageBitmap((Bitmap)receivedMap.get("img"));
        bookName.setText((String)receivedMap.get("name"));
        year.setText((String)receivedMap.get("year"));
        author.setText((String)receivedMap.get("author"));
        ISBN.setText((String)receivedMap.get("isbn"));
        period.setText((String)receivedMap.get("period"));


        if (!status)
        {
            viewDetails.setText("Send request");
        }

        String locationMix = (String)receivedMap.get("location");
        String [] locaitonList = locationMix.split(",");
        int number = locaitonList.length;

        int i = 0;
        while (i<number)
        {
            availableLocation.add(locaitonList[i]);
            i = i + 2;
        }

        arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,availableLocation);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        return view;
    }

    /**
     * Execute after view is created, implement:
     * (1) Adding the listener in the spinner, once clicked, getting item information.
     * (2) Adding the listener for view details/Request button, implementing the logic based on the
     * hardcopy availability.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null)
        {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    itemSelected = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });

            viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (status)
                    {
                        Location location = new Location();
                        location.setItemToLocation(itemSelected);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.replace(R.id.fragment_content,location);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                    else
                    {
                        Book book = new Book();
                        book.setBookName((String) receivedMap.get("name"));
                        book.setAuthor((String) receivedMap.get("author"));
                        book.setYear(Integer.parseInt((String) receivedMap.get("year")));
                        book.setISBN(Integer.parseInt((String) receivedMap.get("isbn")));
                        book.setPeroid((String) receivedMap.get("period"));
                        book.setDescription((String) receivedMap.get("description"));
                        book.setLocation(itemSelected);

                        //String eachISBNString = String.valueOf(book.getISBN());
                        Date date = new Date();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        String today = df.format(date);

                        boolean alreadyHave = false;
                        ArrayList<Book> currentRequestBook = new ArrayList<>(requestBooks.values());
                        for (int k=0;k<currentRequestBook.size();k++)
                        {
                            if (book.getISBN() == currentRequestBook.get(k).getISBN())
                            {
                                alreadyHave = true;
                                break;
                            }
                        }

                        if (!alreadyHave)
                        {
                            requestBooks.put(today,book);

                            writeRequestToFireBase(requestBooks);

                            AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
                            builder.setTitle("Dialogue");
                            builder.setMessage("You have successfully send a request! If you want to cancel this request," +
                                    "you can go to profile page, long click the request record, then you can delete it.");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                            { @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();
                            }
                            });
                            builder.create().show();
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
                            builder.setTitle("Dialogue");
                            builder.setMessage("You have sent this request before, please do not re-send");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                            { @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();
                            }
                            });
                            builder.create().show();
                        }

                    }
                }
            });
        }
    }

    /**
     * This method used for transfer the book information HashMap, request book HashMap and book
     * availability status from "Book_detail" fragment to this fragment.
     */
    public void setItemToRequest(HashMap<String,Object> receivedMap, HashMap<String,Book> requestBooks, boolean status)
    {
        this.receivedMap = receivedMap;
        this.status = status;
        this.requestBooks = requestBooks;
    }

    /**
     * This method used for write the request list into FireBase database after send a request.
     */
    private void writeRequestToFireBase(HashMap<String,Book> temp)
    {
        ArrayList<String> afterAddResults = new ArrayList<>();

        Iterator iterator = temp.entrySet().iterator();
        while(iterator.hasNext())
        {
            HashMap.Entry entry = (HashMap.Entry) iterator.next();
            String key = entry.getKey().toString();
            String isbn = String.valueOf(((Book)entry.getValue()).getISBN());
            String record = isbn +","+key;
            afterAddResults.add(record);

            requestBookRef = mDatabaseRootRef.child("Users").child(uid).child("requestBooks");
            requestBookRef.setValue(afterAddResults);
        }
    }
}
