package com.localli.deepak.cryptotips;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.localli.deepak.cryptotips.Objects.NewsItem;

public class NewsActivity extends AppCompatActivity {

    SharedPreferences pref;

    NewsItem newsItem;
    private AdView ad_ban_top,ad_ban_bottom,ad_ban_middle;
    private InterstitialAd interstitialAd;
    boolean adShown=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        newsItem=(NewsItem)b.getSerializable("key");

        pref=this.getSharedPreferences("show_ad_in_news",0);
        setTitle(newsItem.getMetaTitle());

        incrementReadCount();
        initialiseAds();
        initialiseViews();
        incrementViews();

    }
    private void incrementReadCount(){
        SharedPreferences.Editor editor=pref.edit();
        int count=pref.getInt("read_news_count",0)+1;
        Log.e("NEWS READ COUNT1",count+"");
        editor.putInt("read_news_count",count);
        editor.apply();
    }
    private void initialiseAds(){
        ad_ban_top=findViewById(R.id.adView_act_news_top);
        ad_ban_bottom=findViewById(R.id.adView_act_news_bottom);
        //ad_ban_middle=findViewById(R.id.adView_act_news_middle);

        AdRequest adRequest=new AdRequest.Builder().build();
        ad_ban_top.loadAd(adRequest);

        adRequest=new AdRequest.Builder().build();
        //ad_ban_middle.loadAd(adRequest);

        adRequest=new AdRequest.Builder().build();
        ad_ban_bottom.loadAd(adRequest);

        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.ad_interstitial_activity_news));
        adRequest=new AdRequest.Builder().build();

        interstitialAd.loadAd(adRequest);
    }
    private void initialiseViews(){

        /*TextView newsBodyTv1=(TextView)findViewById(R.id.news_body_tv1);
        newsBodyTv1.setText(newsItem.getBody1());*/
        ImageView newsImageV=(ImageView)findViewById(R.id.news_image_view);
        if(newsItem.getMetaImageUrl()!=null)
            Glide.with(newsImageV.getContext()).load(newsItem.getMetaImageUrl()).into(newsImageV);

        TextView bodyTV1=(TextView)findViewById(R.id.news_body_tv1),
                bodyTV2=(TextView)findViewById(R.id.news_body_tv2),
                bodyTV3=(TextView)findViewById(R.id.news_body_tv3),
                titleTV=(TextView)findViewById(R.id.news_title_tv),
                newsSourceTV=(TextView)findViewById(R.id.news_source_tv);
        ImageView iv1=(ImageView)findViewById(R.id.news_image_view1),
                iv2=(ImageView)findViewById(R.id.news_image_view2),
                iv3=(ImageView)findViewById(R.id.news_image_view3);

        String body1=newsItem.getBody1(),
                body2=newsItem.getBody2(),
                body3=newsItem.getBody3(),
                title=newsItem.getNewsTitle(),
                url1=newsItem.getImageUrl1(),
                url2=newsItem.getImageUrl2(),
                url3=newsItem.getImageUrl3(),
                source=newsItem.getSource();
        if(title!=null){
            titleTV.setText(title);
            titleTV.setVisibility(View.VISIBLE);
        }else if(title==null)
            titleTV.setVisibility(View.GONE);
        if(body1!=null){
            bodyTV1.setVisibility(View.VISIBLE);
            bodyTV1.setText(body1);
        }else if(body1==null)
            bodyTV1.setVisibility(View.GONE);

        if(body2!=null){
            bodyTV2.setVisibility(View.VISIBLE);
            bodyTV2.setText(body2);
        }else if(body2==null)
            bodyTV2.setVisibility(View.GONE);

        if(body3!=null){
            bodyTV3.setVisibility(View.VISIBLE);
            bodyTV3.setText(body3);
        }else if(body3==null)
            bodyTV3.setVisibility(View.GONE);

        if(url1!=null){
            Glide.with(iv1.getContext()).load(newsItem.getImageUrl1()).into(iv1);
            iv1.setVisibility(View.VISIBLE);
        }else if(url1==null)
            iv1.setVisibility(View.GONE);

        if(url2!=null){
            Glide.with(iv2.getContext()).load(newsItem.getImageUrl2()).into(iv2);
            iv2.setVisibility(View.VISIBLE);
        }else if(url2==null)
            iv2.setVisibility(View.GONE);

        if(url3!=null){
            Glide.with(iv3.getContext()).load(newsItem.getImageUrl3()).into(iv3);
            iv3.setVisibility(View.VISIBLE);
        }
        else if(url3==null)
            iv3.setVisibility(View.GONE);
        if(source!=null)
            newsSourceTV.setText(source);
        else if(source==null) {
            newsSourceTV.setVisibility(View.GONE);
            findViewById(R.id.s1tv).setVisibility(View.GONE);
        }

    }
    private void incrementViews(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        String itemType=newsItem.getPostId().trim();
        Log.e("SUBSTRING",itemType.substring(0,3));
        if(itemType.substring(0,3).equals("pos"))
            databaseReference=databaseReference.child("news");
        else
            databaseReference=databaseReference.child("tips");
        int viewCount=Integer.parseInt(newsItem.getViews());
        viewCount++;
        databaseReference.child(newsItem.getPostId()).child("views").setValue(viewCount+"");


    }

    @Override
    public void onBackPressed() {

        SharedPreferences.Editor editor=pref.edit();
        int count=pref.getInt("read_news_count",0);
        Log.e("NEWS READ COUNT",count+"");
        if(count>2) {
            if (interstitialAd.isLoaded())
                interstitialAd.show();
            else
                Log.e("AD ERROR LOG", "news interstitial ad wasn't loaded.");
            // this.adShown=true;
            editor.putInt("read_news_count",0);
            editor.apply();
        }
        finish();
        super.onBackPressed();

    }
}
