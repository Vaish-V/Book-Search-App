/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.vaish.booksearch;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity {


    public static String jsonResponse="";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static String REQUEST_URL = "";
    StringBuilder kb = new StringBuilder();
    StringBuilder kb1 = new StringBuilder();
    StringBuilder kb2 = new StringBuilder();
    private String keyword1="";
    private String keyword="";
    public static ArrayList<Book> books;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.clickSearch);
        button.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            ConnectivityManager cm =
                    (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {


                keyword1 = ((EditText) findViewById(R.id.eti2)).getText().toString();
                if (keyword1.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, DefaultActivity.class);
                    startActivity(intent);
                    return;
                }
                String k[] = keyword1.split(" ");
                for (int i = 0; i < k.length; i++) {
                    if (i < k.length - 1) {
                        kb.append(k[i]).append("+");
                    } else {
                        kb.append(k[i]);
                        kb2 = kb;
                        kb = kb1;
                    }
                }
                keyword = kb2.toString();
                Log.v("keyword:", " " + keyword);
                REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + keyword;
                kb2.setLength(0);//next search entry
                keyword = kb2.toString();


                BookAsyncTask task = new BookAsyncTask();
                task.execute();
            }
            else
            {

                Toast.makeText(MainActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
            }


        }
        });

    }

    private class BookAsyncTask extends AsyncTask<URL, Void, ArrayList<Book> >{

        @Override
        protected ArrayList<Book> doInBackground(URL... urls) {

            URL url = createUrl(REQUEST_URL);


            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
            }

            books = QueryGet.extractInfo();
            if(QueryGet.code==0)
            {
                Intent intent1 = new Intent(MainActivity.this,DefaultActivity.class);
                startActivity(intent1);
            }

            return books;
        }


        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            if (books == null) {
                return;
            }

            Intent intent = new Intent(MainActivity.this,BookActivity.class);
            startActivity(intent);


        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {


            if(url==null)
            {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                Log.v("code",": "+urlConnection.getResponseCode());

                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
                else {

                    Log.e(LOG_TAG, "Response Code: " + urlConnection.getResponseCode());
                }
            }catch(IOException e){
                Intent i = new Intent(MainActivity.this,DefaultActivity.class);
                startActivity(i);
                Log.e(LOG_TAG,"IOException occurred");
            }finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
