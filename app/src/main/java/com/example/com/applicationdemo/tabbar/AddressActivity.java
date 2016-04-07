package com.example.com.applicationdemo.tabbar;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.com.application.R;
import com.example.com.applicationdemo.OnRefreshListener;
import com.example.com.applicationdemo.RefreshListView;
import com.example.com.applicationdemo.com.example.com.applicationdemo.adapter.AddressListRowCsAdapter;

import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressActivity extends AppCompatActivity implements OnItemClickListener,OnRefreshListener {

    private RefreshListView rListView;
    private AddressListRowCsAdapter adapter ;
    private List<Map<String, Object>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintab_address);

        // 自定义的ListView
        rListView = (RefreshListView) findViewById(R.id.addresshlistview);

        // 加载头部广告 ，5张广告
        //  rListView.showAdvertisement(4);
        adapter = new AddressListRowCsAdapter(this,getData());
        rListView.setAdapter(adapter);
        // 注册刷新
        rListView.setOnRefreshListener(this);
        // rListView.setOnItemClickListener(this);
        //添加点击事件
        rListView.setOnItemClickListener(this);

    }

    public List<Map<String, Object>> getData(){
            list = new ArrayList<Map<String,Object>>();
            for (int i = 0; i < 10; i++) {
                   Map<String, Object> map=new HashMap<String, Object>();
                   map.put("image", R.mipmap.ic_launcher);
                   map.put("title", "这是一个标题"+i);
                    map.put("info", "这是一个详细信息" + i);
                   list.add(map);
             }
             return list;
     }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //获得选中项的HashMap对象
        HashMap<String,Object> map= (HashMap<String,Object>)rListView.getItemAtPosition(position);
        String title= map.get("title").toString();
        String content= map.get("info").toString();
        Toast.makeText(getApplicationContext(),
                "你选择了第" + position + "个Item，itemTitle的值是：" + title + "itemContent的值是:" + content,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(2000);
                for (int i = 0; i < 2; i++) {
                    Map<String, Object> map=new HashMap<String, Object>();
                    map.put("image", R.mipmap.ic_launcher);
                    map.put("title", "PullRefresh这是一个标题"+i);
                    map.put("info", "PullRefresh这是一个详细信息" + i);
                    list.add(0,map); // 始终往最前加载数据
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
                SystemClock.sleep(2000);
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("image", R.mipmap.ic_launcher);
                map.put("title", "onLoadingMore这是一个标题" );
                map.put("info", "onLoadingMore这是一个详细信息" );
                list.add(map);
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
