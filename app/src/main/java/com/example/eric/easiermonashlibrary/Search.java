package com.example.eric.easiermonashlibrary;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eric on 25/4/17.
 * A fragment which display a search interface to user.
 */
public class Search extends Fragment
{
    private EditText search_bookName,search_authorName,search_isbn;
    private Button btnSearch1;
    private ArrayList<Book> bookListSearch,searchResultList;
    private ArrayList<String> borrowerFavouriteBooks;
    private HashMap<String,Book> requestBooks;

    /**
     * Get the ArrayLists which has all books in from home activity, then create the view of this fragment.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Bundle bundle = getArguments();
        bookListSearch = bundle.getParcelableArrayList("totalbooks");
        borrowerFavouriteBooks = bundle.getStringArrayList("favourite");
        requestBooks = (HashMap) bundle.getSerializable("requestMaps");
        return view;
    }

    /**
     * This method is called after view created. Adding the OnClickListener for search button, and
     * write the search logic.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null)
        {
            searchResultList = new ArrayList<>();

            search_bookName = (EditText) view.findViewById(R.id.search_book_name);
            search_authorName = (EditText) view.findViewById(R.id.search_author_name);
            search_isbn = (EditText) view.findViewById(R.id.search_ISBN);
            btnSearch1 = (Button) view.findViewById(R.id.btnsearch);

            btnSearch1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    boolean status = false;
                    boolean cancel = false;

                    if (search_bookName.getText().toString().trim().length() == 0 &&
                            search_authorName.getText().toString().trim().length() == 0 &&
                            search_isbn.getText().toString().trim().length() == 0)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Error!");
                        builder.setMessage("Please input at least one of the fields");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        { @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();
                            }
                        });
                        builder.create().show();
                    }
                    else if(search_isbn.getText().toString().trim().length() != 0)
                    {
                        try
                        {
                            Integer.parseInt(search_isbn.getText().toString().trim());
                        }
                        catch (Exception e)
                        {
                            cancel = true;
                            search_isbn.setError(getString(R.string.error_field_type));
                            search_isbn.requestFocus();
                        }
                        if (!cancel)
                        {
                            searchResultList = new ArrayList<>();
                            for(int i=0;i<bookListSearch.size();i++)
                            {
                                if (bookListSearch.get(i).getISBN() == Integer.parseInt(search_isbn.getText().toString().trim()))
                                {
                                    searchResultList.add(bookListSearch.get(i));
                                    status = true;
                                }
                            }
                            if (!status)
                            {
                                if (search_bookName.getText().toString().trim().length() != 0)
                                {
                                    for(int i=0;i<bookListSearch.size();i++)
                                    {
                                        if (bookListSearch.get(i).getBookName().toUpperCase().contains(search_bookName.getText().toString().trim().toUpperCase()))
                                        {
                                            searchResultList.add(bookListSearch.get(i));
                                            status = true;
                                        }
                                    }
                                    if (!status)
                                    {
                                        if (search_authorName.getText().toString().trim().length() != 0)
                                        {
                                            for(int i=0;i<bookListSearch.size();i++)
                                            {
                                                if (bookListSearch.get(i).getAuthor().toUpperCase().contains(search_authorName.getText().toString().trim().toUpperCase()))
                                                {
                                                    searchResultList.add(bookListSearch.get(i));
                                                    status = true;
                                                }
                                            }
                                            if (!status)
                                            {
                                                Toast.makeText(getActivity(),"Sorry, no match book found!",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(getActivity(),"Sorry, no match book found!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                else if (search_authorName.getText().toString().trim().length() != 0)
                                {
                                    for(int i=0;i<bookListSearch.size();i++)
                                    {
                                        if (bookListSearch.get(i).getAuthor().toUpperCase().contains(search_authorName.getText().toString().trim().toUpperCase()))
                                        {
                                            searchResultList.add(bookListSearch.get(i));
                                            status = true;
                                        }
                                    }
                                    if (!status)
                                    {
                                        Toast.makeText(getActivity(),"Sorry, no match book found!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Sorry, no match book found!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    else if (search_bookName.getText().toString().trim().length() != 0)
                    {
                        searchResultList = new ArrayList<>();
                        for(int i=0;i<bookListSearch.size();i++)
                        {
                            if (bookListSearch.get(i).getBookName().toUpperCase().contains(search_bookName.getText().toString().trim().toUpperCase()))
                            {
                                searchResultList.add(bookListSearch.get(i));
                                status = true;
                            }
                        }
                        if (!status)
                        {
                            if(search_authorName.getText().toString().trim().length() != 0)
                            {
                                for(int i=0;i<bookListSearch.size();i++)
                                {
                                    if (bookListSearch.get(i).getAuthor().toUpperCase().contains(search_authorName.getText().toString().trim().toUpperCase()))
                                    {
                                        searchResultList.add(bookListSearch.get(i));
                                        status = true;
                                    }
                                }
                                if (!status)
                                {
                                    Toast.makeText(getActivity(),"Sorry, no match book found!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"Sorry, no match book found!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else if (search_authorName.getText().toString().trim().length() != 0)
                    {
                        searchResultList = new ArrayList<>();
                        for(int i=0;i<bookListSearch.size();i++)
                        {
                            if (bookListSearch.get(i).getAuthor().toUpperCase().contains(search_authorName.getText().toString().trim().toUpperCase()))
                            {
                                searchResultList.add(bookListSearch.get(i));
                                status = true;
                            }
                        }
                        if (!status)
                        {
                            Toast.makeText(getActivity(),"Sorry, no match book found!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }


                    if (status && !cancel)
                    {
                        Search_result searchFragment = new Search_result();
                        searchFragment.transmitItemToSearchResult(searchResultList,borrowerFavouriteBooks,requestBooks);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.replace(R.id.fragment_content, searchFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }
            });
        }
    }
}
