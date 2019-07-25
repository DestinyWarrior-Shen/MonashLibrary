package com.example.eric.easiermonashlibrary;

import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by Eric on 10/6/17.
 */

public class bookTest
{
    @org.testng.annotations.Test
    public void testSetGetBookName() throws Exception
    {
        Book book = new Book();
        book.setBookName("Sun");
        assertEquals("Sun", book.getBookName());
    }

    @org.testng.annotations.Test
    public void testSetGetLocation() throws Exception
    {
        Book book = new Book();
        book.setLocation("Melbourne");
        assertEquals("Melbourne", book.getLocation());
    }

    @org.testng.annotations.Test
    public void testSetGetISBN() throws Exception
    {
        Book book = new Book();
        book.setISBN(12345);
        assertEquals(12345, book.getISBN());
    }

    @org.testng.annotations.Test
    public void testSetGetAuthor() throws Exception
    {
        Book book = new Book();
        book.setAuthor("Tim");
        assertEquals("Tim", book.getAuthor());
    }

    @org.testng.annotations.Test
    public void testSetGetYear() throws Exception
    {
        Book book = new Book();
        book.setYear(2005);
        assertEquals(2005, book.getYear());
    }

    @org.testng.annotations.Test
    public void testSetGetPeriod() throws Exception
    {
        Book book = new Book();
        book.setPeroid("One Month");
        assertEquals("One Month", book.getPeriod());
    }

    @org.testng.annotations.Test
    public void testSetGetDescription() throws Exception
    {
        Book book = new Book();
        book.setDescription("Very good book");
        assertEquals("Very good book", book.getDescription());
    }

    @org.testng.annotations.Test
    public void testSetGetAddingTime() throws Exception
    {
        Book book = new Book();
        book.setAddTimes(15);
        assertEquals((Integer)15, book.getAddTimes());
    }

    @org.testng.annotations.Test
    public void testAddOneTime() throws Exception
    {
        Book book = new Book();
        book.setAddTimes(5);
        book.addOneTime();
        assertEquals((Integer)6, book.getAddTimes());
    }
}
