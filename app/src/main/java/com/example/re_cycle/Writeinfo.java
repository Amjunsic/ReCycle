package com.example.re_cycle;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;

public class Writeinfo
{
    private String title;
    private String contents;
    private String  publisher;
    private Date createdAt;
    private String photoUrl;
    private String id;

    public Writeinfo(String title, String contents, String publisher,Date createdAt, String photoUrl, String id)
    {
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.photoUrl = photoUrl;
        this.id = id;
    }

    public Writeinfo(String title, String contents, String publisher,Date createdAt, String photoUrl)
    {
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.photoUrl = photoUrl;
    }

    public String getTitle()
    {
        return this.title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContents()
    {
        return this.contents;
    }
    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public String getPublisher()
    {
        return this.publisher;
    }
    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    public String  getPhotoUrl()
    {
        return  this.photoUrl;
    }
    public void setPhotoUrl(String photoUrl)
    {
        this.photoUrl = photoUrl;
    }

    public Date getCreatedAt()
    {
        return  this.createdAt;
    }
    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getId()
    {
        return  this.id;
    }
    public void setId(String createdAt)
    {
        this.id = id;
    }


}
