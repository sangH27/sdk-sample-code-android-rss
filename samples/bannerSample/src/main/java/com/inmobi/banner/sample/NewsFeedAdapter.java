package com.inmobi.banner.sample;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class NewsFeedAdapter extends ArrayAdapter<NewsSnippet> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<NewsSnippet> mItems;

    public NewsFeedAdapter(Context context, List<NewsSnippet> items) {
        super(context, R.layout.news_headline_view, items);
        mContext = context;
        mInflater = LayoutInflater.from(context);//BannerAdsActivity의 layout 객체를 반환
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    //그냥 getView원형과 똑같은 형태 매개변수 변수명,순서 모두
        View rowView = convertView;
        ViewHolder viewHolder;
        if (null == rowView || null == convertView.getTag()) {
            rowView = mInflater.inflate(R.layout.news_headline_view, parent, false);
            viewHolder = new ViewHolder();
//            viewHolder.headline = (TextView) rowView.findViewById(R.id.caption);
            viewHolder.content = (TextView) rowView.findViewById(R.id.content);
            viewHolder.icon = (SimpleDraweeView) rowView.findViewById(R.id.photo);
            rowView.setTag(viewHolder); //View 객체가 하위 View객체 3개를 setTag 함. setTag/getTag 아직 이해안됨. 잘은 모르겠지만 rowView 객체를 setTag 했다고 보는게 더 맞는듯
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        NewsSnippet newsSnippet = mItems.get(position);
//        viewHolder.headline.setText(newsSnippet.content);
        viewHolder.content.setText(newsSnippet.content);
        viewHolder.icon.setImageURI(Uri.parse(newsSnippet.imageUrl));
        return rowView;
    }


    private static class ViewHolder {
//        TextView headline;
        TextView content;
        SimpleDraweeView icon;
    }
}