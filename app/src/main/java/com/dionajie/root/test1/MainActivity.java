package com.dionajie.root.test1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    TextView mainTextView;
    Button mainButton;
    EditText mainEditText;
    ListView mainListView;
    JSONAdapter mJSONAdapter;
    ArrayList mNameList = new ArrayList();
    ShareActionProvider mShareActionProvider;

    ProgressDialog mDialog;

    private static final String PREFS = "prefs";
    private static final String PREFS_NAME = "nama";
    SharedPreferences mSharePreferences;

    private static final String QUERY_URL = "http://openlibrary.org/search.json?q=";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayWelcome();

        mainTextView = (TextView) findViewById(R.id.main_textview);
        mainButton = (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(this);

        mainEditText = (EditText) findViewById(R.id.main_edittext);

        mainListView = (ListView) findViewById(R.id.main_listView);

        mainListView.setOnItemClickListener(this);

        mJSONAdapter = new JSONAdapter(this, getLayoutInflater());
        mainListView.setAdapter(mJSONAdapter);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Searching for Book");
        mDialog.setCancelable(false);



    }

    private void queryBooks(String searchString) {

        // Prepare your search string to be put in a URL
        // It might have reserved characters or something
        String urlString = "";
        try {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {

            // if this fails for some reason, let the user know why
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();
        mDialog.show();

        // Have the client get a JSONArray of data
        // and define how to respond
        client.get(QUERY_URL + urlString,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        mDialog.dismiss();
                        // to announce your success
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();

                        // 8. For now, just log results
                       mJSONAdapter.updateData(jsonObject.optJSONArray("docs"));
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        mDialog.dismiss();
                        // Display a "Toast" message
                        // to announce the failure
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();

                        // Log error message
                        // to help solve any problems
                        Log.e("omg android", statusCode + " " + throwable.getMessage());
                    }
                });
    }

    // Menampilkan Pesan Display
    private void displayWelcome() {
        // Access the device's key-value storage
        mSharePreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        // Read the user's name,
        // or an empty string if nothing found
        String nama = mSharePreferences.getString(PREFS_NAME, "");

        if(nama.length() > 0) {
            Toast.makeText(this, "Welcome back " + nama + "!" ,Toast.LENGTH_LONG).show();
        }
        else {
            //Toast.makeText(this, "Welcome back " ,Toast.LENGTH_LONG).show();
            // Membuat kotak dialog
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Selamat Datang!");
            alert.setMessage("Silahkan isi nama anda.");

            // Editor untuk input
           final EditText input= new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {

                    // input nama ke memory
                    String inputname = input.getText().toString();
                    SharedPreferences.Editor e = mSharePreferences.edit();
                    e.putString(PREFS_NAME, inputname);
                    e.commit();

                    //SET TO DIALOG BOX
                    Toast.makeText(getApplicationContext(), "Selamat Datang"  +inputname+ "!", Toast.LENGTH_LONG).show();
                }
            });

            alert.show();


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu.
        // Adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Access the Share Item defined in menu XML
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        // Access the object responsible for
        // putting together the sharing submenu
        if (shareItem != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        }

        // Create an Intent to share your content
        setShareIntent();

        return true;
    }

    private void setShareIntent() {

        if (mShareActionProvider != null) {

            // create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Development");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mainTextView.getText());

            // Make sure the provider knows
            // it should work with that Intent
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void onClick(View v) {
        // 9. Take what was typed into the EditText and use in search
        queryBooks(mainEditText.getText().toString());

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 12. Now that the user's chosen a book, grab the cover data
        JSONObject jsonObject = (JSONObject) mJSONAdapter.getItem(position);
        String coverID = jsonObject.optString("cover_i","");

        // create an Intent to take you over to a new DetailActivity
                Intent detailIntent = new Intent(this, DetailActivity.class);

        // pack away the data about the cover
        // into your Intent before you head out
                detailIntent.putExtra("coverID", coverID);

        // TODO: add any other data you'd like as Extras

        // start the next Activity using your prepared Intent
                startActivity(detailIntent);
            }
}
