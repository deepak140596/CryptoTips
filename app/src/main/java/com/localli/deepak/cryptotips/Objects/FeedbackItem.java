package com.localli.deepak.cryptotips.Objects;

/**
 * Created by Deepak Prasad on 22-01-2018.
 */

public class FeedbackItem {
    String id,body,subject;
    public FeedbackItem(){}
    public FeedbackItem(String id,String subject,String body){
        this.body=body;
        this.subject=subject;
        this.id=id;
    }
    public String getBody(){return body;}
    public String getSubject(){return subject;}

    public String getId() {
        return id;
    }
}
