package com.localli.deepak.cryptotips.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.localli.deepak.cryptotips.Adapters.ApiNewsItemAdapter;
import com.localli.deepak.cryptotips.ApiNewsActivity;
import com.localli.deepak.cryptotips.Objects.ApiNewsItem;
import com.localli.deepak.cryptotips.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepak Prasad on 27-01-2018.
 */

public class fragApiNews extends Fragment {

    private static String API_KEY="813823ac07404d1faf013712724bd26a";
    private static String TOP_HEADLINES="https://newsapi.org/v2/top-headlines?sources=crypto-coins-news&apiKey=";
    private static String ALL_RECENT_ARTICLES="https://newsapi.org/v2/everything?sources=crypto-coins-news&apiKey=";
    ProgressDialog progressDialog;

    ListView listView;
    List<ApiNewsItem> list;
    ApiNewsItemAdapter adapter;
    private AdView ad_ban_bottom;

    String json;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_api_news,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("News");

        initialiseViews(view);
        initialiseAds(view);

    }
    private void initialiseAds(View view){
        ad_ban_bottom=view.findViewById(R.id.adView_frag_api_news_bottom);
        AdRequest adRequest=new AdRequest.Builder().build();
        ad_ban_bottom.loadAd(adRequest);

    }
    public void initialiseViews(View view){
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();

        listView=view.findViewById(R.id.frag_api_news_listview);
        list=new ArrayList<>();
        adapter=new ApiNewsItemAdapter(getContext(),R.layout.item_api_news,list);
        listView.setAdapter(adapter);


        Ion.with(getContext())
                .load(TOP_HEADLINES+API_KEY)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        json=result.toString();
                        Log.e("TOP HEADLINES: ",json);

                        try {
                            extractDataFromJson(json);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ApiNewsItem item =(ApiNewsItem)adapterView.getItemAtPosition(i);

                Intent intent=new Intent(getActivity(), ApiNewsActivity.class);
                intent.putExtra("key",item.getUrl());
                startActivity(intent);
            }
        });


    }
    private void extractDataFromJson(String data) throws JSONException{
        JSONObject baseJsonObject=new JSONObject(data);
        JSONArray newsArray=baseJsonObject.getJSONArray("articles");

        for(int i=0;i<newsArray.length();i++){
            JSONObject currentNews=newsArray.getJSONObject(i);
            String author=currentNews.getString("author");
            String title=currentNews.getString("title");
            String description=currentNews.getString("description");
            String publishedAt=currentNews.getString("publishedAt");
            String url=currentNews.getString("url");
            String urlToImage=currentNews.getString("urlToImage");

            ApiNewsItem item=new ApiNewsItem(author,title,description,url,urlToImage,publishedAt);
            adapter.add(item);
        }

        progressDialog.dismiss();

    }
}
