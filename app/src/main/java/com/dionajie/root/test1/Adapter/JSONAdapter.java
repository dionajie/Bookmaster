package com.dionajie.root.test1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dionajie.root.test1.Model.BukuModel;
import com.dionajie.root.test1.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by root on 04/02/15.
 */

public class JSONAdapter extends BaseAdapter {
    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;
    BukuModel mBook = new BukuModel();

    public JSONAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        // your particular dataset uses String IDs
        // but you have to put something in this method
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.row_book, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.img_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.text_title);
            holder.authorTextView = (TextView) convertView.findViewById(R.id.text_author);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current book's data in JSON form
        JSONObject jsonObject = (JSONObject) getItem(position);

        // Bila buku mempunya thumbnails
        if (jsonObject.has("cover_i")) {

            // If so, grab the Cover ID out from the object
            mBook.setCoverID(jsonObject.optString("cover_i"));

            // Construct the image URL (specific to API)
            mBook.setImageURL(IMAGE_URL_BASE + mBook.getCoverID() + "-S.jpg");

            // Use Picasso to load the image
            // Temporarily have a placeholder in case it's slow to load
            Picasso.with(mContext).load(mBook.getImageURL()).placeholder(R.drawable.ic_books).into(holder.thumbnailImageView);
        } else {

            // If there is no cover ID in the object, use a placeholder
            holder.thumbnailImageView.setImageResource(R.drawable.ic_books);
        }

        if (jsonObject.has("title")) {
            mBook.setBookTitle(jsonObject.optString("title"));
        }

        if (jsonObject.has("author_name")) {
            mBook.setAuthorName(jsonObject.optJSONArray("author_name").optString(0));
        }


        // Send these Strings to the TextViews for display
        holder.titleTextView.setText(mBook.getBookTitle());
        holder.authorTextView.setText(mBook.getAuthorName());

        return convertView;
    }

    public void updateData(JSONArray jsonArray) {
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }

    // this is used so you only ever have to do
// inflation and finding by ID once ever per View
    private static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView authorTextView;
    }
}
