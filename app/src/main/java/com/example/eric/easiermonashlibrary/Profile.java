package com.example.eric.easiermonashlibrary;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Eric on 25/4/17.
 * A fragment which display all information about user, including he or she current loan, request,
 * userID etc.
 */
public class Profile extends Fragment
{
    private ImageView imageUser;
    private TextView currentUserId,noLoan,noRequest;
    private ListView listView_onLoan,listView_onRequest;
    private Button btn_logout,btn_about;
    private LoanAdapter loanAdapter;
    private RequestAdapter requestAdapter;
    private HashMap<String,Book> borrowedBooks,requestBooks;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String uid;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRootRef,requestBookRef;

    /**
     * Register all UI widget(exclude button), create the view of the fragment. If current logged in user do not have
     * any records for loan or request, corresponding areas will show empty information.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
        String userEmail = user.getEmail();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRootRef = mDatabase.getReference();

        imageUser = (ImageView) view.findViewById(R.id.img_user);
        currentUserId = (TextView) view.findViewById(R.id.tv_currentUserID);
        noLoan = (TextView) view.findViewById(R.id.noLoan);
        noRequest = (TextView) view.findViewById(R.id.noRequest);
        listView_onLoan = (ListView) view.findViewById(R.id.listView_OnLoan);
        listView_onRequest = (ListView) view.findViewById(R.id.listView_OnRequest);

        currentUserId.setText(userEmail);

        Bundle bundle = getArguments();
        borrowedBooks = (HashMap)bundle.getSerializable("BorrowedMap");
        requestBooks = (HashMap)bundle.getSerializable("RequestMap");

        if (borrowedBooks.size() == 0)
        {
            listView_onLoan.setVisibility(View.GONE);
            noLoan.setVisibility(View.VISIBLE);
        }
        else
        {
            listView_onLoan.setVisibility(View.VISIBLE);
            noLoan.setVisibility(View.GONE);
        }

        if (requestBooks.size() == 0)
        {
            listView_onRequest.setVisibility(View.GONE);
            noRequest.setVisibility(View.VISIBLE);
        }
        else
        {
            listView_onRequest.setVisibility(View.VISIBLE);
            noRequest.setVisibility(View.GONE);
        }

        loanAdapter = new LoanAdapter(getContext(),borrowedBooks);
        requestAdapter = new RequestAdapter(getContext(),requestBooks);

        listView_onLoan.setAdapter(loanAdapter);
        listView_onRequest.setAdapter(requestAdapter);


        return view;
    }

    /**
     * Called after view is created, adding OnClickListener for logout button and about page button,
     * once clicked, the corresponding jumping will be occurred.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null)
        {
            btn_logout = (Button) view.findViewById(R.id.button_logout);
            btn_about = (Button) view.findViewById(R.id.btn_ap);
            btn_logout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }
            });

            btn_about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    AboutPage ap = new AboutPage();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragment_content,ap);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            listView_onRequest.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
                {
                    // Build a dialog to delete item
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Cancel Request?");
                    builder.setMessage("Are you sure you want to cancel this request?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            // Remove monster from list and database
                            ArrayList<String> keySet = new ArrayList<>(requestBooks.keySet());
                            String targetKey = keySet.get(position);
                            requestBooks.remove(targetKey);
                            writeRequestToDatabase(requestBooks);
                            // Update ListView
                            requestAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "Request has been canceled", Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                    { @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                    }
                    });
                    builder.create().show();
                    return false;
                }
            });
        }
    }

    /**
     * This method used for write the request list into FireBase database after cancel a request.
     */
    private void writeRequestToDatabase(HashMap<String,Book> temp)
    {
        requestBookRef = mDatabaseRootRef.child("Users").child(uid).child("requestBooks");
        if (temp.size() == 0)
        {
            requestBookRef.setValue(null);
            listView_onRequest.setVisibility(View.GONE);
            noRequest.setVisibility(View.VISIBLE);
        }
        else
        {
            ArrayList<String> afterRemoveResults = new ArrayList<>();

            Iterator iterator = temp.entrySet().iterator();
            while (iterator.hasNext()) {
                HashMap.Entry entry = (HashMap.Entry) iterator.next();
                String key = entry.getKey().toString();
                String isbn = String.valueOf(((Book) entry.getValue()).getISBN());
                String record = isbn + "," + key;
                afterRemoveResults.add(record);
                requestBookRef.setValue(afterRemoveResults);
            }
        }
    }
}
