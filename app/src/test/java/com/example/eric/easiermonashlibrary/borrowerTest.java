package com.example.eric.easiermonashlibrary;

import static junit.framework.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * Created by Eric on 10/6/17.
 */

public class borrowerTest
{
    @org.testng.annotations.Test
    public void testSetGetEmail() throws Exception
    {
        Borrower borrower = new Borrower("123@gmail.com");
        assertEquals("123@gmail.com",borrower.getEmail());
        borrower.setEmail("010101@monash.edu");
        assertEquals("010101@monash.edu",borrower.getEmail());
    }

    @org.testng.annotations.Test
    public void testSetGetBorrowedBook() throws Exception
    {
        Borrower borrower = new Borrower();
        assertEquals(0,borrower.getBorrowedBook().size());
        ArrayList<String> bbl = new ArrayList<>();
        bbl.add("abc");
        bbl.add("def");
        bbl.add("ghi");
        borrower.setBorrowedBook(bbl);
        assertEquals(3, borrower.getBorrowedBook().size());
    }

    @org.testng.annotations.Test
    public void testSetGetFavouriteBook() throws Exception
    {
        Borrower borrower = new Borrower();
        assertEquals(0,borrower.getFavouriteBooks().size());
        ArrayList<String> fbl = new ArrayList<>();
        fbl.add("what");
        fbl.add("when");
        fbl.add("where");
        fbl.add("which");
        borrower.setFavouriteBooks(fbl);
        assertEquals(4, borrower.getFavouriteBooks().size());
    }

    @org.testng.annotations.Test
    public void testSetGetRequestBook() throws Exception
    {
        Borrower borrower = new Borrower();
        assertEquals(0,borrower.getRequestBooks().size());
        ArrayList<String> rbl = new ArrayList<>();
        rbl.add("Hello");
        rbl.add("Computer");
        borrower.setRequestBooks(rbl);
        assertEquals(4, borrower.getRequestBooks().size());
    }
}
