package com.example.vaish.booksearch;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Vaish on 15-09-2016.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_layout, parent, false);
        }

        Book currentBook = getItem(position);

        TextView t1 = (TextView) listItemView.findViewById(R.id.t);
        t1.setText(currentBook.getmTitle());

        TextView t2 = (TextView) listItemView.findViewById(R.id.a);
        t2.setText(currentBook.getmAuthor());




        return listItemView;
    }


}
