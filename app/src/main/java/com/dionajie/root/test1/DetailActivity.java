package com.dionajie.root.test1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by root on 05/02/15.
 */

public class DetailActivity extends ActionBarActivity {
    ShareActionProvider mShareActionProvider; // 14
    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/"; // 13
    String mImageURL; // 13

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the activity which XML layout is right
        setContentView(R.layout.activity_detail);

        // Enable the "Up" button for more navigation options : tombom < atas kiri
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Access the imageview from XML
        ImageView imageView = (ImageView) findViewById(R.id.img_cover);
        TextView tvTitle= (TextView) findViewById(R.id.text_title);
        TextView tvAuthor= (TextView) findViewById(R.id.text_author);


        // 13. unpack the coverID from its trip inside your Intent
        String coverID = this.getIntent().getExtras().getString("coverID");
        String authorname = this.getIntent().getExtras().getString("authorname");
        String booktitle = this.getIntent().getExtras().getString("booktitle");

        tvTitle.setText("Judul Buku : " +booktitle+ "");
        tvAuthor.setText("Nama Pengarang : " +authorname+ "");
        // See if there is a valid coverID = bila punya cover
        if (coverID.length() > 0) {

            // Use the ID to construct an image URL
            mImageURL = IMAGE_URL_BASE + coverID + "-L.jpg";

            // Use Picasso to load the image
            Picasso.with(this).load(mImageURL).placeholder(R.drawable.img_books_loading).into(imageView);
        }
    }

    // share
    private void setShareIntent() {

        // create an Intent with the contents of the TextView
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Book Recommendation!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mImageURL);

        // Make sure the provider knows
        // it should work with that Intent
        mShareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu
        // this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Access the Share Item defined in menu XML
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        // Access the object responsible for
        // putting together the sharing submenu
        if (shareItem != null) {
            mShareActionProvider
                    = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        }

        setShareIntent();

        return true;
    }


}
