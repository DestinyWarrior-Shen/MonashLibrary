package com.example.eric.easiermonashlibrary;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

/**
 * Created by Eric on 22/4/17.
 * This class define all attributes related to book, implement "Parcelable" and "Serializable" interface
 */
public class Book implements Parcelable,Serializable
{
    private String bookName,location,author,period,description;
    private int ISBN,year,addTimes;

    /**
     * This is default construtor for creating book object.
     */
    public Book()
    {
        bookName = "Science";
        location = "Caulfield Library";
        ISBN = 90285371;
        author = "James";
        year = 2008;
        period = "1 Week";
        description = "This is a book related to the world's the biggest computer";
        addTimes = 1;
    }

    /**
     * This is non-default constructor for creating book object, including eight attributes.
     */
    public Book(String bookName, String location, int ISBN, String author, int year, String peroid, String description, int totalAddTimes)
    {
        this.bookName = bookName;
        this.location = location;
        this.ISBN = ISBN;
        this.author = author;
        this.year = year;
        this.period = peroid;
        this.description = description;
        this.addTimes = totalAddTimes;
    }

    /**
     * used for create book object from parcel
     */
    public Book(Parcel in)
    {
        bookName = in.readString();
        location = in.readString();
        ISBN = in.readInt();
        author = in.readString();
        year = in.readInt();
        period = in.readString();
        description = in.readString();
        addTimes = in.readInt();
    }

    /**
     * Instantiate static object CREATOR to implement interface "Parcelable.Creator"
     */
    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    /**
     * @see #CONTENTS_FILE_DESCRIPTOR
     */
    @Override
    public int describeContents()
    {
        return 0;
    }

    /**
     * Write the object into the parcel.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(bookName);
        dest.writeString(location);
        dest.writeInt(ISBN);
        dest.writeString(author);
        dest.writeInt(year);
        dest.writeString(period);
        dest.writeString(description);
        dest.writeInt(addTimes);
    }

    /**
     * get book object's bookName attribute
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * set book object's bookName attribute
     */
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    /**
     * get book object's location attribute
     */
    public String getLocation() {
        return location;
    }

    /**
     * set book object's location attribute
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * get book object's ISBN attribute
     */
    public int getISBN() {
        return ISBN;
    }

    /**
     * set book object's ISBN attribute
     */
    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * get book object's author attribute
     */
    public String getAuthor() {
        return author;
    }

    /**
     * set book object's author attribute
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * get book object's year attribute
     */
    public int getYear()
    {
        return year;
    }

    /**
     * set book object's year attribute
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * get book object's period attribute
     */
    public String getPeriod() {
        return period;
    }

    /**
     * set book object's period attribute
     */
    public void setPeroid(String peroid) {
        this.period = peroid;
    }

    /**
     * get book object's description attribute
     */
    public String getDescription() {
        return description;
    }

    /**
     * set book object's description attribute
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get book object's addTimes attribute
     */
    public Integer getAddTimes() {
        return addTimes;
    }

    /**
     * set book object's addTimes attribute
     */
    public void setAddTimes(int addTimes) {
        this.addTimes = addTimes;
    }

    /**
     * add book object's addTimes attribute increment 1
     */
    public void addOneTime()
    {
        addTimes += 1;
    }
}
