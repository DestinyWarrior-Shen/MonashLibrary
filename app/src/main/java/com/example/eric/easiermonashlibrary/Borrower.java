package com.example.eric.easiermonashlibrary;

import java.util.ArrayList;

/**
 * Created by Eric on 22/4/17.
 * This class define all attributes related to borrower, include three ArrayLists for each of borrower object.
 */

public class Borrower
{
    private ArrayList<String> borrowedBook,favouriteBooks,requestBooks;
    private String email;
    /**
     * This is default constructor for creating an borrower object.
     */
    public Borrower()
    {
        email = "";
        borrowedBook = new ArrayList<>();
        favouriteBooks = new ArrayList<>();
        requestBooks = new ArrayList<>();
    }

    /**
     * This is non-default constructor for creating an borrower object, the parameters is email address.
     */
    public Borrower(String email)
    {
        this.email = email;
        borrowedBook = new ArrayList<>();
        favouriteBooks = new ArrayList<>();
        requestBooks = new ArrayList<>();
    }


    /**
     * get borrower object's email.
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * set borrower object's email.
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * set borrower object's borrowedBook list.
     */
    public void setBorrowedBook(ArrayList<String> borrowedBook)
    {
        this.borrowedBook = borrowedBook;
    }

    /**
     * get borrower object's borrowedBook list.
     */
    public ArrayList<String> getBorrowedBook()
    {
        return borrowedBook;
    }

    /**
     * set borrower object's favouriteBooks list.
     */
    public void setFavouriteBooks(ArrayList<String> favouriteBooks)
    {
        this.favouriteBooks = favouriteBooks;
    }

    /**
     * get borrower object's favouriteBooks list.
     */
    public ArrayList<String> getFavouriteBooks()
    {
        return favouriteBooks;
    }

    /**
     * set borrower object's requestBooks list.
     */
    public void setRequestBooks(ArrayList<String> requestBooks)
    {
        this.requestBooks = requestBooks;
    }

    /**
     * get borrower object's requestBooks list.
     */
    public ArrayList<String> getRequestBooks()
    {
        return requestBooks;
    }

}

