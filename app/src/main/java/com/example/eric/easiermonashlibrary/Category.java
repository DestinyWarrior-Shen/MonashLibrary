package com.example.eric.easiermonashlibrary;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eric on 25/4/17.
 * A fragment which display total six categories.
 */
public class Category extends Fragment
{
    private ArrayList<Book> artBookList,biologyBookList,itBookList,designBookList,
                            financeBookList,medicalBookList;
    private ArrayList<String> borrowerFavouriteBooks;
    private HashMap<String,Book> requestBooks;
    private TextView artTv,bioTv,itTv,designTv,fiTv,mediTv;

    /**
     * Get six ArrayLists from home activity, then create the view of this fragment.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        Bundle bundleGet = getArguments();
        artBookList = bundleGet.getParcelableArrayList("art");
        biologyBookList = bundleGet.getParcelableArrayList("biology");
        itBookList = bundleGet.getParcelableArrayList("it");
        designBookList = bundleGet.getParcelableArrayList("design");
        financeBookList = bundleGet.getParcelableArrayList("finance");
        medicalBookList = bundleGet.getParcelableArrayList("medical");
        borrowerFavouriteBooks = bundleGet.getStringArrayList("favourite");
        requestBooks = (HashMap) bundleGet.getSerializable("requestMaps");

        return view;
    }

    /**
     * This method is called after view created. Adding the OnClickListener for each of category,
     * once clicked, it will transmit to "Category_result" fragment.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null)
        {
            artTv = (TextView) view.findViewById(R.id.tv_art);
            bioTv = (TextView) view.findViewById(R.id.tv_biology);
            itTv = (TextView) view.findViewById(R.id.tv_it);
            designTv = (TextView) view.findViewById(R.id.tv_design);
            fiTv = (TextView) view.findViewById(R.id.tv_finance);
            mediTv = (TextView) view.findViewById(R.id.tv_medical);

            artTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Category_result category_result = new Category_result();
                    category_result.transmitItemToCategoryResult(artBookList,borrowerFavouriteBooks,requestBooks);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragment_content, category_result);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            bioTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Category_result category_result = new Category_result();
                    category_result.transmitItemToCategoryResult(biologyBookList,borrowerFavouriteBooks,requestBooks);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragment_content, category_result);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            itTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Category_result category_result = new Category_result();
                    category_result.transmitItemToCategoryResult(itBookList,borrowerFavouriteBooks,requestBooks);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragment_content, category_result);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            designTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Category_result category_result = new Category_result();
                    category_result.transmitItemToCategoryResult(designBookList,borrowerFavouriteBooks,requestBooks);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragment_content, category_result);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            fiTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Category_result category_result = new Category_result();
                    category_result.transmitItemToCategoryResult(financeBookList,borrowerFavouriteBooks,requestBooks);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragment_content, category_result);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            mediTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Category_result category_result = new Category_result();
                    category_result.transmitItemToCategoryResult(medicalBookList,borrowerFavouriteBooks,requestBooks);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragment_content, category_result);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        }
    }
}
