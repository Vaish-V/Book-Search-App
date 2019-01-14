package com.example.vaish.booksearch;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vaish on 16-09-2016.
 */
public final class QueryGet extends MainActivity {


    public static int code=1;

    private QueryGet(){

    }

    public static ArrayList<Book> extractInfo(){
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        String authors="";
        String title="";
        String url="";
        String info="";
        StringBuilder a=new StringBuilder();
        ArrayList<Book> books = new ArrayList<Book>();
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            Log.v("error:"," " +jsonResponse);
            int errorcode = baseJsonResponse.getInt("totalItems");
            Log.v("errorcode",": "+errorcode);
            if(errorcode==0)
            {
                code=0;
                return null;
            }
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");
            Log.v("items",":" +itemsArray);
            if (itemsArray.length() > 0) {



                for(int i=0;i<itemsArray.length();i++) {
                    JSONObject curObject = itemsArray.getJSONObject(i);

                    Log.v("curObject",":" +curObject);
                    JSONObject volumeInfoObj = curObject.getJSONObject("volumeInfo");
                    Log.v("volumeInfoObj",":"+volumeInfoObj);
                    a.setLength(0);

                    title = volumeInfoObj.getString("title");
                    Log.v("Title",":" +title);

                    info = volumeInfoObj.getString("previewLink");
                    //info = curObject.getString("selfLink");

                    JSONObject linkInfoObj = volumeInfoObj.getJSONObject("imageLinks");
                    url=linkInfoObj.getString("txhumbnail");

                    if(volumeInfoObj.has("authors")) {
                        JSONArray authorsArray = volumeInfoObj.getJSONArray("authors");
                        if (authorsArray != null && authorsArray.length() > 0) {
                            for (int j = 0; j < authorsArray.length(); j++) {
                                String curAuthor = authorsArray.getString(j);
                                if(j<authorsArray.length()-1) {
                                    a.append(curAuthor);
                                    a.append(", ");
                                }
                                else
                                    a.append(curAuthor);

                            }
                            authors = a.toString();
                            Log.v("Authors", ":" + authors);
                            books.add(new Book(title, authors,url));
                        }

                    }
                    else
                    {
                        books.add(new Book(title,"Author details not available.",""));
                        continue;

                    }
                }
            }

        } catch (JSONException e) {

            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }
        return books;
    }

}
