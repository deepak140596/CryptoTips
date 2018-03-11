package com.localli.deepak.cryptotips.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.localli.deepak.cryptotips.MainActivity;
import com.localli.deepak.cryptotips.NewsActivity;
import com.localli.deepak.cryptotips.Objects.NewsItem;
import com.localli.deepak.cryptotips.R;

import java.util.List;

/**
 * Created by Deepak Prasad on 16-01-2018.
 */

public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.NewsViewHolder> {
    private List<NewsItem> newsList;

    public class NewsViewHolder extends RecyclerView.ViewHolder{
        public TextView metaTitleTv,metaBodyTv,dateTv,viewsTv;
        public ImageView metaNewsIv;

        public NewsViewHolder(View view){
            super(view);
            metaTitleTv=(TextView)view.findViewById(R.id.item_news_meta_title);
            metaBodyTv=(TextView)view.findViewById(R.id.item_news_meta_body);
            dateTv=(TextView)view.findViewById(R.id.item_news_publish_date);
            viewsTv=(TextView)view.findViewById(R.id.item_news_hits);
            metaNewsIv=(ImageView)view.findViewById(R.id.item_news_meta_image);
        }

    }

    public NewsItemAdapter(List<NewsItem> newsList){
        this.newsList=newsList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_news, parent, false);

        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, final int position) {
        final NewsItem newsItem=newsList.get(position);
        holder.metaTitleTv.setText(newsItem.getMetaTitle());
        if(newsItem.getMetaBody()!=null)
            holder.metaBodyTv.setText(newsItem.getMetaBody());
        else
            holder.metaBodyTv.setVisibility(View.GONE);
        holder.dateTv.setText(newsItem.getDate());
        holder.viewsTv.setText(newsItem.getViews());

        String metaPicUrl=newsItem.getMetaImageUrl();
        if(metaPicUrl!=null)
            Glide.with(holder.metaNewsIv.getContext())
                .load(metaPicUrl).into(holder.metaNewsIv);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "Clicked on "+position, Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(view.getContext(), NewsActivity.class);
                intent.putExtra("key",newsItem);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
