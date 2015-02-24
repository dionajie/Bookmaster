package com.dionajie.root.test1.Model;

import com.dionajie.root.test1.MainActivity;

/**
 * Created by root on 11/02/15.
 */
public class BukuModel {
    private String bookTitle;
    private String authorName;
    private String CoverID;
    private String imageURL;

    public String getAuthorName() {
        return authorName;
    }


    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getCoverID() {
        return CoverID;
    }

    public void setCoverID(String coverID) {
        CoverID = coverID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
