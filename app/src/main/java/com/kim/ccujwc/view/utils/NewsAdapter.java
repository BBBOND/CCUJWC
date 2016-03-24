package com.kim.ccujwc.view.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kim.ccujwc.R;
import com.kim.ccujwc.model.News;

import java.util.List;

/**
 * Created by kim on 16-3-24.
 */
public class NewsAdapter extends BaseAdapter {

    private List<News> newsList;
    private LayoutInflater inflater;
    private int viewId;

    public NewsAdapter(Context context, int viewId, List<News> newsList) {
        inflater = LayoutInflater.from(context);
        this.viewId = viewId;
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(newsList.get(position).getNewsTag());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        News news = newsList.get(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(viewId, null);
            holder = new ViewHolder();
            holder.tvNewsTitle = (TextView) convertView.findViewById(R.id.tv_newsTitle);
            holder.tvNewsType = (TextView) convertView.findViewById(R.id.tv_newsType);
            holder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendTime);
            convertView.setTag(holder);
        }
        holder.tvNewsTitle.setText(news.getNewsTitle());
        holder.tvNewsType.setText(news.getNewsType());
        holder.tvSendTime.setText(news.getSendTime());
        return convertView;
    }

    class ViewHolder {
        TextView tvNewsTitle;
        TextView tvNewsType;
        TextView tvSendTime;
    }
}
