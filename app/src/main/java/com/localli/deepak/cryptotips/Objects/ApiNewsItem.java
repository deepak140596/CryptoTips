package com.localli.deepak.cryptotips.Objects;

import java.io.Serializable;

/**
 * Created by Deepak Prasad on 27-01-2018.
 */

public class ApiNewsItem implements Serializable {
    String author,title,description,url,urlToImage,publishedAt;

    public ApiNewsItem(){}
    public ApiNewsItem(String author,String title,String description,String url,String urlToImage,String publishedAt){
        this.author=author;
        this.title=title;
        this.description=description;
        this.url=url;
        this.urlToImage=urlToImage;
        this.publishedAt=publishedAt;

    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;

    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

}
