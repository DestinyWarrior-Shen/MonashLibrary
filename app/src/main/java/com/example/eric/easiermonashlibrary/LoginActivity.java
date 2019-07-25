package com.example.eric.easiermonashlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;


import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

/**
 * Created by Eric on 22/4/17.
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private Button forgetButton, signUpButton, mEmailSignInButton;
    private FirebaseAuth mAuth;
    private FirebaseAuthListener mAuthListener;
    //setting a AuthStateListener to response to users login status changed：
    private EditText mEmailView,mPasswordView;
    private View mProgressView,mLoginFormView;

    /**
     * Create the activity, register all UI widgets, adding OnClickListeners. Within listener,
     * call the appropriate methods defined in the following context.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuthListener();


        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_NULL)
                {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        forgetButton = (Button) findViewById(R.id.email_forget_button);
        forgetButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth auth = FirebaseAuth.getInstance();

                String emailAddress = mEmailView.getText().toString();

                auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful())
                        {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(LoginActivity.this, "Email sent successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        signUpButton = (Button) findViewById(R.id.email_sign_up_button);
        signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    /**
     * Execute after onCreate method, adding FireBase authentication state listener to monitor the
     * authentication state.
     */
    @Override
    public void onStart()
    {
        super.onStart();

        if(mAuth != null)
            mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Execute when activity is terminated, remove FireBase authentication state listener.
     */
    @Override
    public void onStop()
    {
        super.onStop();

        if(mAuth != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }


    /**
     * Attempts to sign in, if there are form errors (invalid email, invalid passwords), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for email address if is empty.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            // Show a progress spinner
            showProgress(true);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(LoginActivity.this, R.string.Login_success, Toast.LENGTH_SHORT).show();
                    }
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified to jump to Home activity.
                    if (!task.isSuccessful())
                    {
                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                        Toast.makeText(LoginActivity.this, R.string.Login_failed, Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                }
            });
        }
    }

    /**
     * Judging the availability of the email
     */
    public boolean isEmailValid(String email)
    {
        boolean status = false;
        if (email.contains("@")) {
            status = true;
        }
        return status;
    }

    /**
     * Judging the availability of the passwords
     */
    public boolean isPasswordValid(String password)
    {
        boolean judge = false;
        if (password.length() > 6) {
            judge = true;
        }
        return judge;
    }

    /**
     * Attempts register the account specified by the login form.
     * If there are form errors (invalid email, invalid passwords), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignUp()
    {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean interrupt = false;
        View focusView = null;

        // Check for a email address if is empty.
        if (TextUtils.isEmpty(email))
        {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            interrupt = true;
        }
        else if (!isEmailValid(email))
        {
            mEmailView.setError(getString(R.string.error_not_monash_email));
            focusView = mEmailView;
            interrupt = true;
        }

        // Check for password, if the user entered one.
        if (TextUtils.isEmpty(password))
        {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            interrupt = true;
        }
        else if (!isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            interrupt = true;
        }

        if (interrupt)
        {
            // There was an error; not attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(LoginActivity.this, R.string.SignUp_success,Toast.LENGTH_SHORT).show();
                    }
                    if (!task.isSuccessful())
                    {
                        Toast.makeText(LoginActivity.this, R.string.SignUp_failed,Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                }
            });
//            ArrayList<Book> book = new ArrayList<Book>();
//            Book book1 = new Book();
//            book.add(book1);
//            String test = new Gson().toJson(book);
//            writeNewUser(userId, email, password, book);
        }
    }
//    private void writeNewUser(String tmpUserId, String tmpEmail, String tmpPassword, ArrayList<Book> bbk,
//                              ArrayList<Book> fbk, ArrayList<Book> rbk)
//    {
//        Borrower borrower = new Borrower(tmpUserId, tmpEmail, tmpPassword, bbk, fbk, rbk);
//        String result="{\"borrowedBook\":[{\"author\":\"James\",\"avalibility\":true,\"bookName\":\"Science\",\"location\":\"Melbourne\",\"peroid\":\"1 Week\",\"year\":2008}],\"email\":\"ty@monash.edu\",\"id\":\"4i85YUcfXbYqLRTw6tdER0g2wP22\",\"password\":\"76544311\"}";
//        Borrower b = new Gson().fromJson(result, Borrower.class);
//        mRootRef.child("Users").child(tmpUserId).setValue(new Gson().toJson(borrower));
//    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            //int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            int shortAnimTime = 200;
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        else
        {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




    /**
     * Define a class to implement the AuthStateListener interface. Define the state change behaviour
     * if user has logged in, jumping to Home activity.
     */
    private class FirebaseAuthListener implements FirebaseAuth.AuthStateListener
    {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
        {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if(user != null)
            {
                String currentUserId = user.getUid();
                String email = user.getEmail();

                Intent newIntent = new Intent(LoginActivity.this, Home.class);
                newIntent.putExtra("UID", currentUserId);
                newIntent.putExtra("Email",email);
                startActivity(newIntent);
            }
            else
            {
                // User just signed out
                //Toast.makeText(LoginActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

