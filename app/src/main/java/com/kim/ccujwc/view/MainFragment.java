package com.kim.ccujwc.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.kim.ccujwc.R;
import com.kim.ccujwc.common.App;
import com.kim.ccujwc.common.MyHttpUtil;
import com.kim.ccujwc.model.News;
import com.kim.ccujwc.view.utils.MySharedPreferences;
import com.kim.ccujwc.view.utils.NewsAdapter;
import com.kim.ccujwc.view.utils.XListView;

import java.util.List;
import java.util.Map;

public class MainFragment extends BaseFragment implements XListView.IXListViewListener {

    private static final String TAG = "MainFragment";
    private XListView lvNews;

    private int requestCount = 0;

    Handler GetNewsErrorHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                if (requestCount < 4) {
                    new GetNews().execute();
                    requestCount++;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("提示");
                    builder.setMessage("获取新闻失败，是否重试？");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestCount = 0;
                            new GetNews().execute();
                        }
                    });
                    builder.create().show();
                }
            }
            return false;
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, null);
        lvNews = (XListView) view.findViewById(R.id.lv_news);
        lvNews.setPullRefreshEnable(true);
        lvNews.setPullLoadEnable(false);
        lvNews.setXListViewListener(this);
        MySharedPreferences msp = MySharedPreferences.getInstance(getContext());
        if ((boolean) msp.readLoginInfo().get("isAutoLogin"))
            new GetLocalNews().execute();
        else
            onRefresh();
        return view;
    }

    @Override
    public void onRefresh() {
        App.clearLocalNews();
        new GetNews().execute();
    }

    @Override
    public void onLoadMore() {

    }

    class GetLocalNews extends AsyncTask<Void, Void, List<News>> {

        @Override
        protected List<News> doInBackground(Void... params) {
            try {
                List<News> newsList = null;
                MySharedPreferences msp = MySharedPreferences.getInstance(getContext());
                Map<String, Object> map = msp.readNewsList();
                newsList = (List<News>) map.get("newslist");
                if (newsList.size() > 0) {
                    return newsList;
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<News> result) {
            try {
                MySharedPreferences msp = MySharedPreferences.getInstance(getContext());
                Map<String, Object> map = msp.readNewsList();
                if (result != null) {
                    Log.d(TAG, "获取本地新闻!");
                    setNewsView(result);
                    String saveTime = (String) map.get("savetime");
                    lvNews.setRefreshTime(saveTime);
                    lvNews.stopRefresh();
                } else {
                    new GetNews().execute();
                }
            } catch (Exception e) {
                new GetNews().execute();
            }
            super.onPostExecute(result);
        }
    }

    class GetNews extends AsyncTask<Void, Void, List<News>> {
        @Override
        protected List<News> doInBackground(Void... params) {
            if (App.getLocalNews() != null) {
                return (List<News>) App.getLocalNews().get("newslist");
            } else {
                try {
                    return MyHttpUtil.getNewsList();
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = GetNewsErrorHandler.obtainMessage();
                    message.what = 1;
                    message.sendToTarget();
                    return null;
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final List<News> result) {
            try {
                if (result != null) {
                    setNewsView(result);
                    if (App.getLocalNews() != null) {
                        lvNews.setRefreshTime((String) App.getLocalNews().get("savetime"));
                    } else {
                        lvNews.setRefreshTime("刚刚");
                    }
                    MySharedPreferences msp = MySharedPreferences.getInstance(getContext());
                    if ((boolean) msp.readLoginInfo().get("isAutoLogin"))
                        msp.saveNews(result);
                    else
                        App.setLocalNews(result);
                    lvNews.stopRefresh();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    private void setNewsView(final List<News> result) {
        NewsAdapter adapter = new NewsAdapter(getContext(), R.layout.news_list_item, result);
        lvNews.setAdapter(adapter);
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), NewsActivity.class);
                intent.putExtra("newsTag", result.get(position).getNewsTag());
                startActivity(intent);
            }
        });
    }
}
