package com.example.vaish.booksearch;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static java.lang.System.exit;

public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Query code",": "+QueryGet.code);

        setContentView(R.layout.book_list);

        final BookAdapter adapter = new BookAdapter(this,MainActivity.books);
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book currentBook = adapter.getItem(i);
                Uri bookUri = Uri.parse(currentBook.getmUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW,bookUri);
                startActivity(intent);


            }
        });
    }
}
