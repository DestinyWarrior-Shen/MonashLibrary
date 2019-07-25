package com.example.eric.easiermonashlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by Eric on 12/5/17
 * Write a custom Request adapter extends from BaseAdapter, to implement displaying the
 * book name and borrow date information in listView from HashMap.
 */

public class RequestAdapter extends BaseAdapter
{
    private Context context;
    private HashMap<String,Book> requestBookMap;
    /**
     * Default constructor for creating RequestAdapter object, define HashMap as parameter rather than
     * ArrayList.
     */
    public RequestAdapter(Context context, HashMap<String,Book> requestBookMap)
    {
        this.context = context;
        this.requestBookMap = requestBookMap;
    }

    /**
     * Get HashMap size.
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount()
    {
        return requestBookMap.size();
    }

    /**
     * Grabbing HashMap's ValueSet as an ArrayList, then through input index, return Book object.
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Book getItem(int i)
    {
        Book book = ((ArrayList<Book>)requestBookMap.values()).get(i);
        return book;
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
     * Get each of the item's view. Getting request date from HashMap, display on UI.
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
            view = inflater.inflate(R.layout.profile_item_list2, null);
        }
        TextView title = (TextView) view.findViewById(R.id.title2);
        TextView requestDate = (TextView) view.findViewById(R.id.requestDate);

        ArrayList<Book> allValues = new ArrayList<>(requestBookMap.values());
        ArrayList<String> allKeys = new ArrayList<>(requestBookMap.keySet());

        final Book book = allValues.get(i);

        title.setText(book.getBookName());
        requestDate.setText(allKeys.get(i));

        return view;
    }
}
