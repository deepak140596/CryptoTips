package com.localli.deepak.cryptotips.news;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.grizzly.rest.Model.RestResults;
import com.localli.deepak.cryptotips.NavigationActivity;
import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.models.News;
import com.localli.deepak.cryptotips.rest.NewsService;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Deepak Prasad on 12-11-2018.
 */

public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    String TAG = "NEWS_LIST_FRAGMENT";

    private NewsListAdapter adapter;
    private RecyclerView recyclerView;
    private AppCompatActivity activity;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Observable newsObservable;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        activity = (AppCompatActivity)getActivity();
        context = getContext();
        return inflater.inflate(R.layout.fragment_news_list,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar_fragment_news_list);
        activity.setSupportActionBar(toolbar);
        activity.setTitle("News");

        // set swipe refresh layout
        swipeRefreshLayout = view.findViewById(R.id.fragment_news_list_swipe_layout);

        // set recycler view
        recyclerView = view.findViewById(R.id.fragment_news_list_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        // set adapter
        adapter = new NewsListAdapter(new ArrayList<NewsItem>());

        // override on refresh for swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(this);

        // set scroll listener
        addScrollListenerToRecyclerView();
    }

    public void addScrollListenerToRecyclerView(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy>0){
                    ((NavigationActivity)getActivity()).setBottomNavigationViewVisibility(false);
                }else if(dy<0){
                    ((NavigationActivity)getActivity()).setBottomNavigationViewVisibility(true);
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_news_frag,menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        getNewsObservable(2,false);
    }


    public void getNewsObservable(int whatToDo, boolean cache){

        Action1<News[]> subscriber = new Action1<News[]>() {
            @Override
            public void call(News[] newsRestResults) {

                List<NewsItem> myNews = new ArrayList<>();

                if(newsRestResults != null && newsRestResults.length > 0){

                    Parcelable recyclerViewState;
                    recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

                    for(News news : newsRestResults){
                        NewsItem newsItem = new NewsItem(news.getTitle(),
                                news.getUrl(), news.getBody(),
                                news.getImageurl(), news.getSource(),
                                news.getPublishedOn());
                        if(!myNews.contains(newsItem)) myNews.add(newsItem);
                    }

                    adapter.setData(myNews);
                    recyclerView.setAdapter(adapter);
                    if(swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                    recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    Log.e(TAG,"call successful");
                }

                else{
                    if(swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                    Log.e(TAG,"call failed");
                }
            }
        };

        switch (whatToDo){
            case 1:
                NewsService.getObservableNews(activity, true, new Action1<RestResults<News[]>>() {
                    @Override
                    public void call(RestResults<News[]> newsRestResults) {
                        Parcelable recyclerViewState;
                        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

                        if(newsRestResults.isSuccessful()){
                            List<NewsItem> myNews = new ArrayList<>();

                            for(News news : newsRestResults.getResultEntity()){
                                NewsItem newsItem = new NewsItem(news.getTitle(),
                                        news.getUrl(),news.getBody(),
                                        news.getImageurl(),news.getSource(),
                                        news.getPublishedOn());

                                if(!myNews.contains(newsItem))
                                    myNews.add(newsItem);
                            }
                            adapter.setData(myNews);
                            recyclerView.setAdapter(adapter);
                            if(swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);
                            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);


                        }
                    }
                });
                break;


            case 2:
                // Observable instance from EasyRest
                if(newsObservable == null) {
                    newsObservable = NewsService.getPlainObservableNews(activity, cache)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
                newsObservable.subscribe(subscriber);
                break;


            default:
                // Wrapped observable call
                NewsService.getObservableNews(activity, true, new Action1<RestResults<News[]>>() {
                    @Override
                    public void call(RestResults<News[]> newsRestResults) {
                        Parcelable recyclerViewState;
                        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                        if(newsRestResults.isSuccessful()){
                            List<NewsItem> myNews = new ArrayList<>();
                            for(News news: newsRestResults.getResultEntity()){
                                NewsItem newsItem = new NewsItem(news.getTitle(),
                                        news.getUrl(), news.getBody(),
                                        news.getImageurl(), news.getSource(),
                                        news.getPublishedOn());
                                if(!myNews.contains(newsItem)) myNews.add(newsItem);
                            }
                            adapter.setData(myNews);
                            recyclerView.setAdapter(adapter);
                            swipeRefreshLayout.setRefreshing(false);
                            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                        }else{
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // tell RX to ignore any subsriptions done in UI
        if(newsObservable != null)
            newsObservable.unsubscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onResume() {
        super.onResume();

        // make activity call the news every time it comes back

        if(swipeRefreshLayout != null){
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    getNewsObservable(2,true);
                }
            });
        }else{
            onRefresh();
        }
    }
}
