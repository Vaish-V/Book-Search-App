package com.example.vaish.booksearch;

/**
 * Created by Vaish on 15-09-2016.
 */
public class Book {
    private String mAuthor,mTitle,mUrl;
    public Book(String title,String author,String url)
    {
        mTitle=title;
        mAuthor=author;
        mUrl=url;
    }
    public String getmTitle(){

        return mTitle;
    }
    public String getmAuthor(){

        return mAuthor;
    }
    public String getmUrl(){
        return mUrl;
    }

}
