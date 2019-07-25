package com.example.eric.easiermonashlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by Eric on 12/5/17
 * Write a custom Loan adapter extends from BaseAdapter, to implement displaying the
 * book name and borrow date information in listView from HashMap.
 */
public class LoanAdapter extends BaseAdapter
{
    private Context context;
    private HashMap<String,Book> borrowedBookMap;

    /**
     * Default constructor for creating LoanAdapter object, define HashMap as parameter rather than
     * ArrayList.
     */
    public LoanAdapter(Context context, HashMap<String,Book> borrowedBookMap)
    {
        this.context = context;
        this.borrowedBookMap = borrowedBookMap;
    }

    /**
     * Get HashMap size.
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount()
    {
        return borrowedBookMap.size();
    }

    /**
     * Grabbing HashMap's ValueSet as an ArrayList, then through input index, return Book object.
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Book getItem(int i)
    {
        Book book = ((ArrayList<Book>)borrowedBookMap.values()).get(i);
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
     * Get each of the item's view. Getting borrow date from HashMap, and then based on the borrow
     * period to calculate the expire date, display on UI.
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
            view = inflater.inflate(R.layout.profile_item_list, null);
        }
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView startDate = (TextView) view.findViewById(R.id.borrowDate);
        TextView endDate = (TextView) view.findViewById(R.id.expireDate);

        ArrayList<Book> allValues = new ArrayList<>(borrowedBookMap.values());
        ArrayList<String> allKeys = new ArrayList<>(borrowedBookMap.keySet());
        Book book = allValues.get(i);
        title.setText(book.getBookName());

        startDate.setText(allKeys.get(i));

        Date dateStart = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try
        {
            dateStart = df.parse(allKeys.get(i));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        GregorianCalendar gc = new GregorianCalendar();

        gc.setTime(dateStart);

        Date dateEnd = new Date();

        if (book.getPeriod().equals("One Week"))
        {
            gc.add(Calendar.WEEK_OF_YEAR,+1);
        }
        else if (book.getPeriod().equals("Two Weeks"))
        {
            gc.add(Calendar.WEEK_OF_YEAR,+2);
        }
        else if (book.getPeriod().equals("Three Days"))
        {
            gc.add(Calendar.DATE,+3);
        }
        else if (book.getPeriod().equals("One Month"))
        {
            gc.add(Calendar.MONTH,+1);
        }

        String expireDate = df.format(gc.getTime());
        endDate.setText(expireDate);
        return view;
    }

}
