package com.localli.deepak.cryptotips.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.localli.deepak.cryptotips.Objects.ApiNewsItem;
import com.localli.deepak.cryptotips.R;

import java.util.List;

/**
 * Created by Deepak Prasad on 27-01-2018.
 */

public class ApiNewsItemAdapter extends ArrayAdapter<ApiNewsItem> {

    public ApiNewsItemAdapter(@NonNull Context context, int resource, @NonNull List<ApiNewsItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_api_news, parent, false);

        ImageView imageView=(ImageView)convertView.findViewById(R.id.item_api_news_iv);
        TextView authorTv=convertView.findViewById(R.id.item_api_news_author_tv),
                dateTv=convertView.findViewById(R.id.item_api_news_date_tv),
                titleTv=convertView.findViewById(R.id.item_api_news_title_tv),
                bodyTv=convertView.findViewById(R.id.item_api_news_body_tv);

        ApiNewsItem item=getItem(position);
        String imageUrl=item.getUrlToImage(),
                author=item.getAuthor(),
                date=item.getPublishedAt(),
                title=item.getTitle(),
                body=item.getDescription();

        date=date.substring(0,10)+"  "+date.substring(11,date.length()-1);

        if(imageUrl!=null)
            Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
        else
            imageView.setVisibility(View.GONE);

        if(author.equals("null"))
            authorTv.setText("");
        else authorTv.setText(author);

        if(date!=null)
            dateTv.setText(date);
        else
            dateTv.setVisibility(View.GONE);

        if(title!=null)
            titleTv.setText(title);
        else
            titleTv.setText("");

        if(body!=null)
            bodyTv.setText(body);
        else
            bodyTv.setText("");
        return convertView;
    }
}
