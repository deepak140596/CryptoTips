package com.localli.deepak.cryptotips.Objects;

import java.io.Serializable;

/**
 * Created by Deepak Prasad on 16-01-2018.
 */

public class NewsItem implements Serializable {
    String title,metaTitle,body1,body2,body3,metaBody,date,views,imageUrl1,imageUrl2,imageUrl3,metaImageUrl,source;
    String postId;
    public NewsItem(){}

    public NewsItem(String postId,
                    String title,String metaTitle,
                    String body1,String body2,String body3,String metaBody,
                    String date,String views,
                    String imageUrl1,String imageUrl2,String imageUrl3, String metaImageUrl,String source){
        this.postId=postId;
        this.title=title;
        this.metaTitle=metaTitle;
        this.body1=body1;
        this.body2=body2;
        this.body3=body3;
        this.metaBody=metaBody;
        this.date=date;
        this.views=views;
        this.imageUrl1=imageUrl1;
        this.imageUrl2=imageUrl2;
        this.imageUrl3=imageUrl3;
        this.metaImageUrl=metaImageUrl;
        this.source=source;
    }

    public String getMetaImageUrl(){return metaImageUrl;}
    public String getPostId(){return postId;}
    public String getNewsTitle(){return title;}
    public String getMetaTitle(){return metaTitle;}
    public String getBody1(){return body1;}

    public String getBody2() {
        return body2;
    }

    public String getBody3() {
        return body3;
    }

    public String getMetaBody(){return metaBody;}
    public String getDate(){return date;}
    public String getViews(){return views;}
    public String getImageUrl1(){return imageUrl1;}

    public String getImageUrl2() {
        return imageUrl2;
    }

    public String getImageUrl3() {
        return imageUrl3;
    }

    public String getSource() {
        return source;
    }
}
