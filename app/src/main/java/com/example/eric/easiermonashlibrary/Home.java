package com.example.eric.easiermonashlibrary;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Color;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View.OnClickListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Eric on 24/4/17.
 * Home activity which support all other fragments, getting all books and current logged in user's information from
 * firebase database into corresponding lists. Creating a tab bar which provides the paths to go to following fragments,
 * with corresponding list to transmit.
 */
public class Home extends AppCompatActivity implements OnClickListener
{
    private Button searchBtn, categoryBtn, topBtn, favouriteBtn, profileBtn;
    private List<Button> btnList;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseBookRef,mDatabaseUserRef;

    private ValueEventListener bookListener,userListener;

    private String currentUserId,email;
    private ArrayList<Book> totalBookList,artBookList,biologyBookList,itBookList,designBookList,
                            financeBookList,medicalBookList;
    private ArrayList<String> currentBorrowerBorrowedBooks,currentBorrowerFavouriteBooks,
                               currentBorrowerRequestBooks;

    private Borrower currentBorrower;

    /**
     * Create the activity, register all UI widgets, adding OnClickListeners, getting the userID
     * from firebase Authentication, initialize all lists and firebase references.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Hide ActionBar
        getSupportActionBar().hide();

        setContentView(R.layout.activity_home1);

        btnList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseBookRef = mDatabase.getReference("Books");
        mDatabaseUserRef = mDatabase.getReference("Users");

        totalBookList = new ArrayList<>();
        artBookList = new ArrayList<>();
        biologyBookList = new ArrayList<>();
        itBookList = new ArrayList<>();
        designBookList = new ArrayList<>();
        financeBookList = new ArrayList<>();
        medicalBookList = new ArrayList<>();
        currentBorrowerBorrowedBooks = new ArrayList<>();
        currentBorrowerFavouriteBooks = new ArrayList<>();
        currentBorrowerRequestBooks = new ArrayList<>();

        findById();

        Intent intent = getIntent();
        currentUserId = intent.getStringExtra("UID");
        email = intent.getStringExtra("Email");
    }

    /**
     * Register All UI widgets.
     */
    private void findById()
    {
        searchBtn = (Button)this.findViewById(R.id.search_btn);
        categoryBtn = (Button)this.findViewById(R.id.category_btn);
        topBtn = (Button) this.findViewById(R.id.top_btn);
        favouriteBtn = (Button)this.findViewById(R.id.favourite_btn);
        profileBtn = (Button)this.findViewById(R.id.profile_btn);

        searchBtn.setOnClickListener(this);
        categoryBtn.setOnClickListener(this);
        topBtn.setOnClickListener(this);
        favouriteBtn.setOnClickListener(this);
        profileBtn.setOnClickListener(this);

        btnList.add(searchBtn);
        btnList.add(categoryBtn);
        btnList.add(topBtn);
        btnList.add(favouriteBtn);
        btnList.add(profileBtn);
    }

    /**
     * Setting tab bar button's background colours when clicked.
     */
    private void setBackgroundColorById(int btnId)
    {
        for (Button btn : btnList)
        {
            if (btn.getId() == btnId)
            {
                btn.setBackgroundColor(Color.DKGRAY);
            }
            else
            {
                btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    /**
     * Execute after onCreate method, implementing download books and user's book lists from the
     * FireBase database, and put the objects into appropriate ArrayLists. Finally setting a
     * default landing page after user logged in.
     */
    @Override
    public void onStart()
    {
        super.onStart();

        if(mDatabaseBookRef != null)
        {
            bookListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    totalBookList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        if (snapshot.getKey().equals("art"))
                        {
                            for (DataSnapshot snapshot1 : snapshot.getChildren())
                            {
                                Book book = snapshot1.getValue(Book.class);
                                artBookList.add(book);
                                totalBookList.add(book);
                            }
                        }
                        if (snapshot.getKey().equals(("biology")))
                        {
                            for (DataSnapshot snapshot2 : snapshot.getChildren())
                            {
                                Book book = snapshot2.getValue(Book.class);
                                biologyBookList.add(book);
                                totalBookList.add(book);
                            }
                        }
                        if (snapshot.getKey().equals("it"))
                        {
                            for (DataSnapshot snapshot3 : snapshot.getChildren())
                            {
                                Book book = snapshot3.getValue(Book.class);
                                itBookList.add(book);
                                totalBookList.add(book);
                            }
                        }
                        if (snapshot.getKey().equals("design"))
                        {
                            for (DataSnapshot snapshot4 : snapshot.getChildren())
                            {
                                Book book = snapshot4.getValue(Book.class);
                                designBookList.add(book);
                                totalBookList.add(book);
                            }
                        }
                        if (snapshot.getKey().equals("finance"))
                        {
                            for (DataSnapshot snapshot5 : snapshot.getChildren())
                            {
                                Book book = snapshot5.getValue(Book.class);
                                financeBookList.add(book);
                                totalBookList.add(book);
                            }
                        }
                        if (snapshot.getKey().equals("medical"))
                        {
                            for (DataSnapshot snapshot6 : snapshot.getChildren())
                            {
                                Book book = snapshot6.getValue(Book.class);
                                medicalBookList.add(book);
                                totalBookList.add(book);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            };
            mDatabaseBookRef.addValueEventListener(bookListener);
        }


        if (mDatabaseUserRef != null)
        {
            userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        currentBorrower = dataSnapshot.getValue(Borrower.class);
                        currentBorrowerBorrowedBooks = currentBorrower.getBorrowedBook();
                        currentBorrowerFavouriteBooks = currentBorrower.getFavouriteBooks();
                        currentBorrowerRequestBooks = currentBorrower.getRequestBooks();
                    }
                    else
                    {
                        Borrower borrower = new Borrower(email);
                        mDatabaseUserRef.child(currentUserId).setValue(borrower);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            };

            mDatabaseUserRef.child(currentUserId).addValueEventListener(userListener);
        }

        // setting default fragment
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        setBackgroundColorById(R.id.search_btn);
        Search search = new Search();
        Bundle bundleToSearch = new Bundle();
        bundleToSearch.putParcelableArrayList("totalbooks",totalBookList);
        bundleToSearch.putStringArrayList("favourite",currentBorrowerFavouriteBooks);
        HashMap<String,Book> requestMap = findBookByIsbnForHashMap(currentBorrowerRequestBooks);
        bundleToSearch.putSerializable("requestMaps",requestMap);
        search.setArguments(bundleToSearch);
        ft.replace(R.id.fragment_content, search);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * Execute when Home activity is terminated. Here, the FireBase database listeners for books
     * and users are both removed when activity is destroyed.
     */
    @Override
    public void onStop()
    {
        super.onStop();

        if(mDatabaseBookRef != null)
        {
            mDatabaseBookRef.removeEventListener(bookListener);
        }
        if (mDatabaseUserRef != null)
        {
            mDatabaseUserRef.child(currentUserId).removeEventListener(userListener);
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
    }

    /**
     * This method defines the behaviour when back button is clicked.
     */
    @Override
    public void onBackPressed()
    {
        fm.popBackStack();
        Fragment f = fm.findFragmentById(R.id.fragment_content);
        setBtnColor(f);
    }

    /**
     * This method defines the behaviours when each of the button in the tab bar is clicked, what
     * fragment should be created, and what data should be transmitted to the corresponding fragment
     */
    @Override
    public void onClick(View v)
    {
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        switch (v.getId())
        {

            case R.id.search_btn:
                setBackgroundColorById(R.id.search_btn);
                Search search = new Search();
                Bundle bundleToSearch = new Bundle();
                bundleToSearch.putParcelableArrayList("totalbooks",totalBookList);
                bundleToSearch.putStringArrayList("favourite",currentBorrowerFavouriteBooks);
                HashMap<String,Book> requestMap = findBookByIsbnForHashMap(currentBorrowerRequestBooks);
                bundleToSearch.putSerializable("requestMaps",requestMap);
                search.setArguments(bundleToSearch);
                ft.replace(R.id.fragment_content, search);
                break;

            case R.id.category_btn:
                setBackgroundColorById(R.id.category_btn);
                Category category = new Category();
                Bundle bundleToCategory = new Bundle();
                bundleToCategory.putParcelableArrayList("art",artBookList);
                bundleToCategory.putParcelableArrayList("biology",biologyBookList);
                bundleToCategory.putParcelableArrayList("it",itBookList);
                bundleToCategory.putParcelableArrayList("design",designBookList);
                bundleToCategory.putParcelableArrayList("finance",financeBookList);
                bundleToCategory.putParcelableArrayList("medical",medicalBookList);
                bundleToCategory.putStringArrayList("favourite", currentBorrowerFavouriteBooks);
                HashMap<String,Book> requestMap1 = findBookByIsbnForHashMap(currentBorrowerRequestBooks);
                bundleToCategory.putSerializable("requestMaps",requestMap1);
                category.setArguments(bundleToCategory);
                ft.replace(R.id.fragment_content,category);
                break;

            case R.id.top_btn:
                setBackgroundColorById(R.id.top_btn);
                Top top = new Top();
                Bundle bundleToTop = new Bundle();
                bundleToTop.putParcelableArrayList("totalBookList",totalBookList);
                bundleToTop.putStringArrayList("favourite", currentBorrowerFavouriteBooks);
                HashMap<String,Book> requestMap2 = findBookByIsbnForHashMap(currentBorrowerRequestBooks);
                bundleToTop.putSerializable("requestMaps",requestMap2);
                top.setArguments(bundleToTop);
                ft.replace(R.id.fragment_content,top);
                break;

            case R.id.favourite_btn:
                setBackgroundColorById(R.id.favourite_btn);
                Favourites favourites = new Favourites();
                Bundle bundleToFavourites = new Bundle();
                bundleToFavourites.putParcelableArrayList("favourite",findBookByIsbnForArrayList(currentBorrowerFavouriteBooks));
                HashMap<String,Book> requestMap3 = findBookByIsbnForHashMap(currentBorrowerRequestBooks);
                bundleToFavourites.putSerializable("requestMaps",requestMap3);
                favourites.setArguments(bundleToFavourites);
                ft.replace(R.id.fragment_content,favourites);
                break;

            case R.id.profile_btn:
                setBackgroundColorById(R.id.profile_btn);
                Profile profile = new Profile();
                HashMap<String,Book> resultMapBorrowed = findBookByIsbnForHashMap(currentBorrowerBorrowedBooks);
                HashMap<String,Book> resultMapRequest = findBookByIsbnForHashMap(currentBorrowerRequestBooks);
                Bundle bundleToProfile = new Bundle();
                bundleToProfile.putSerializable("BorrowedMap",resultMapBorrowed);
                bundleToProfile.putSerializable("RequestMap",resultMapRequest);
                profile.setArguments(bundleToProfile);
                ft.replace(R.id.fragment_content,profile);
                break;

            default:
                break;
        }
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * This method implement a ArrayList to ArrayList conversion, from a String type ArrayList to
     * Book type ArrayList.
     */
    public ArrayList<Book> findBookByIsbnForArrayList(ArrayList<String> tempBookList)
    {
        ArrayList<Book> resultBookList = new ArrayList<>();
        for (int i=0;i<tempBookList.size();i++)
        {
            String[] split = tempBookList.get(i).split(",");
            String conditionIsbn = split[0];
            for (int j=0;j<totalBookList.size();j++)
            {
                if (Integer.parseInt(conditionIsbn) == totalBookList.get(j).getISBN())
                {
                    resultBookList.add(totalBookList.get(j));
                }
            }
        }
        return resultBookList;
    }

    /**
     * This method implement a ArrayList to HashMap conversion, from a String type ArrayList to
     * a String-Book type HashMap, through split the record in the ArrayList.
     */
    public HashMap<String,Book> findBookByIsbnForHashMap(ArrayList<String> tempBookList)
    {
        HashMap<String,Book> resultBookMap = new HashMap<>();
        for (int i=0;i<tempBookList.size();i++)
        {
            String[] split = tempBookList.get(i).split(",");
            for (int j=0;j<totalBookList.size();j++)
            {
                if (Integer.parseInt(split[0]) == totalBookList.get(j).getISBN())
                {
                    resultBookMap.put(split[1],totalBookList.get(j));
                }
            }
        }
        return resultBookMap;
    }

    public void setBtnColor(Fragment fragment)
    {
        if (fragment instanceof Search)
        {
            searchBtn.setBackgroundColor(Color.DKGRAY);
            categoryBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            topBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            favouriteBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            profileBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else if (fragment instanceof Category)
        {
            searchBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            categoryBtn.setBackgroundColor(Color.DKGRAY);
            topBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            favouriteBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            profileBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else if (fragment instanceof Top)
        {
            searchBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            categoryBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            topBtn.setBackgroundColor(Color.DKGRAY);
            favouriteBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            profileBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else if (fragment instanceof Favourites)
        {
            searchBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            categoryBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            topBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            favouriteBtn.setBackgroundColor(Color.DKGRAY);
            profileBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else if (fragment instanceof Profile)
        {
            searchBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            categoryBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            topBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            favouriteBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            profileBtn.setBackgroundColor(Color.DKGRAY);
        }
    }
}

