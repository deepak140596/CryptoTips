package com.localli.deepak.cryptotips.views;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.news.NewsItem;
import com.localli.deepak.cryptotips.news.WebViewActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Deepak Prasad on 13-11-2018.
 */

public class NewsView extends ConstraintLayout {

    private TextView articleTitleTv, articeAgeTv, sourceNameTv;
    private CircleImageView articleIv;

    private NewsItem newsItem;

    public NewsView(Context context){
        super(context);
        inflateComponents();
    }

    public NewsView(Context context, AttributeSet attrs){
        super(context, attrs);
        inflateComponents();
    }

    public NewsView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        inflateComponents();
    }

    protected void inflateComponents(){
        inflate(getContext(), R.layout.row_news, this);

        articleTitleTv = findViewById(R.id.row_news_meta_title);
        articeAgeTv = findViewById(R.id.row_news_time_tv);
        articleIv = findViewById(R.id.row_news_meta_image);
        sourceNameTv = findViewById(R.id.row_news_source_tv);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(getContext(), WebViewActivity.class);
                browserIntent.putExtra("url", newsItem.articleURL);
                browserIntent.putExtra("title","News");
                getContext().startActivity(browserIntent);
            }
        });

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.orientation = LayoutParams.VERTICAL;
        setLayoutParams(params);
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,outValue,true);
        setBackgroundResource(outValue.resourceId);
        setFocusable(true);
        setClickable(true);
    }

    public NewsView setData(NewsItem newsItem){
        this.newsItem = newsItem;
        if(this.newsItem != null){
            articleTitleTv.setText(this.newsItem.articleTitle);
            String publishTimeString = (String) DateUtils.getRelativeTimeSpanString(this.newsItem.publishedOn * 1000, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
            articeAgeTv.setText(publishTimeString);
            sourceNameTv.setText(this.newsItem.sourceName);
            Glide.with(getContext()).load(this.newsItem.imageURL).into(articleIv);
        }
        return this;
    }
}
