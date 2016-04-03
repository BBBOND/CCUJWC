package com.kim.ccujwc.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kim.ccujwc.R;
import com.kim.ccujwc.common.MyHttpUtil;
import com.kim.ccujwc.model.News;
import com.kim.ccujwc.view.utils.LoadingView;
import com.kim.ccujwc.view.utils.NewsAdapter;
import com.kim.ccujwc.view.utils.ShapeLoadingView;

import org.apache.commons.httpclient.HttpClient;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.util.List;

public class MainFragment extends BaseFragment {

    private ListView lvNews;
    private LoadingView loadView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, null);
        lvNews = (ListView) view.findViewById(R.id.lv_news);
        loadView = (LoadingView) view.findViewById(R.id.loadView);

        new GetNews().execute();
        return view;
    }

    class GetNews extends AsyncTask<Void, Void, List<News>> {

        @Override
        protected List<News> doInBackground(Void... params) {
            try {
                HttpClient client = new HttpClient();
                return MyHttpUtil.getNewsList(client);
            } catch (Exception e) {
                e.printStackTrace();
                Message message = GetNewsErrorHandler.obtainMessage();
                message.what = 1;
                message.sendToTarget();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            loadView.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final List<News> result) {
            try {
                if (result != null) {
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
                loadView.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

}
