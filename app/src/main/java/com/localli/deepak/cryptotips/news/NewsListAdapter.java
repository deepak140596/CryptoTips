package com.localli.deepak.cryptotips.news;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.localli.deepak.cryptotips.views.NewsView;

import java.util.List;

/**
 * Created by Deepak Prasad on 13-11-2018.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.holder> {

    private List<NewsItem> newsList;

    public NewsListAdapter(List<NewsItem> newsList){
        this.newsList = newsList;
    }

    public NewsListAdapter setData (List<NewsItem> newsList){
        this.newsList = newsList;
        this.notifyDataSetChanged();
        return  this;
    }

    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new holder(new NewsView(viewGroup.getContext()));
    }

    @Override
    public void onBindViewHolder(holder holder, int i) {
        holder.getView().setData(newsList.get(i));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class holder extends RecyclerView.ViewHolder{
        private holder(NewsView itemLayoutView){
            super(itemLayoutView);
        }

        public NewsView getView(){
            return (NewsView) itemView;
        }
    }
}
