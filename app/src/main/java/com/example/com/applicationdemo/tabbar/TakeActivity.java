package com.example.com.applicationdemo.tabbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.com.application.R;
import com.example.com.applicationdemo.OnRefreshListener;
import com.example.com.applicationdemo.RefreshListView;
import com.example.com.applicationdemo.ShowAdvertisementActivity;

import java.util.ArrayList;
import java.util.List;

public class TakeActivity extends AppCompatActivity implements View.OnClickListener ,OnRefreshListener {

    private ActionBar actionBar;
    private Boolean bool = true;
    private MyAdapter adapter;
    private List<String> textList;
    private RefreshListView rListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintab_take);

        actionBar = getSupportActionBar();

        // 自定义的ListView
        rListView = (RefreshListView) findViewById(R.id.refreshlistview);
        textList = new ArrayList<String>();
        for (int i = 0; i < 25; i++) {
            textList.add("这是一条ListView的数据" + i);
        }
        // 加载头部广告 ，5张广告
        rListView.showAdvertisement(4);
        adapter = new MyAdapter();
        rListView.setAdapter(adapter);
        rListView.setOnRefreshListener(this);

      }

    @Override
    public void onClick(View v) {
        if(bool){
            actionBar.hide();
            bool = false;
           // setHideAnimation(actionBar,1);

            //从启动动画ui跳转到主ui
//            Intent intent = new Intent(TakeActivity.this,PulltorefreshlistviewActivity.class);
//            startActivity(intent);

        }else{
            actionBar.show();
            bool = true;

        }

    }



    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return textList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return textList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView textView = new TextView(TakeActivity.this);
            textView.setText(textList.get(position));
         //   textView.setTextColor(Color.BLACK);
            textView.setTextSize(18.0f);
            return textView;
        }
    }

    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(2000);
                for (int i = 0; i < 2; i++) {
                    textList.add(0, "这是下拉刷新出来的数据" + i);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                adapter.notifyDataSetChanged();
                rListView.hideHeaderView();
            }
        }.execute(new Void[]{});
    }

    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(3000);
                textList.add("这是加载更多出来的数据1");
                textList.add("这是加载更多出来的数据2");
                textList.add("这是加载更多出来的数据3");

                //从启动动画ui跳转到主ui
                Intent intent = new Intent(TakeActivity.this,ShowAdvertisementActivity.class);
                startActivity(intent);

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                adapter.notifyDataSetChanged();
                // 控制脚布局隐藏
                rListView.hideFooterView();
            }
        }.execute(new Void[]{});
    }

}
