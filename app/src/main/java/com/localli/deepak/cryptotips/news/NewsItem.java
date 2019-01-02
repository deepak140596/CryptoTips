package com.localli.deepak.cryptotips.news;

/**
 * Created by Deepak Prasad on 13-11-2018.
 */

public class NewsItem {

    public String articleTitle;
    public String articleURL;
    public String articleBody;
    public String imageURL;
    public String sourceName;
    public long publishedOn;

    public NewsItem(){}

    public NewsItem(String articleTitle, String articleURL, String articleBody, String imageURL, String sourceName, long publishedOn) {
        this.articleTitle = articleTitle;
        this.articleURL = articleURL;
        this.articleBody = articleBody;
        this.imageURL = imageURL;
        this.sourceName = sourceName;
        this.publishedOn = publishedOn;
    }

    // check if two articles are same or not (based on their titles) with handling null values

    @Override
    public boolean equals(Object obj) {
        if( this == obj) return true;
        if( obj == null || getClass() != obj.getClass()) return false;

        NewsItem newsItem = (NewsItem)obj;
        if(articleTitle == null || newsItem.articleTitle == null)
            return false;

        return articleTitle.equals(newsItem.articleTitle);
    }

    // return hash code for a particular news item
    @Override
    public int hashCode() {
        int result = articleTitle!=null ? articleTitle.hashCode() : 0;
        result = 31 * result + (articleURL != null ? articleURL.hashCode() : 0);
        result = 31 * result + (articleBody != null ? articleBody.hashCode() : 0);

        return result;
    }
}
