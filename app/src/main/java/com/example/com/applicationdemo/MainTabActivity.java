package com.example.com.applicationdemo;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import com.example.com.application.R;
import com.example.com.applicationdemo.tabbar.AddressActivity;
import com.example.com.applicationdemo.tabbar.MeActivity;
import com.example.com.applicationdemo.tabbar.TakeActivity;
import com.example.com.applicationdemo.tabbar.FindActivity;

/**
 * 主页Main的底部菜单栏
 */
public class MainTabActivity extends TabActivity implements
        OnCheckedChangeListener {
    private TabHost tabHost;
    private Intent addressIntent;
    private Intent meIntent;
    private Intent takeIntent;
    private Intent findIntent;

    private RadioButton findBt;
    private RadioButton addressBt;
    private RadioButton meBt;
    private RadioButton takeBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        addressIntent = new Intent(this, AddressActivity.class);
        meIntent = new Intent(this, MeActivity.class);
        takeIntent = new Intent(this, TakeActivity.class);
        findIntent = new Intent(this, FindActivity.class);

        findBt = (RadioButton) findViewById(R.id.find);
        addressBt = (RadioButton) findViewById(R.id.address);
        meBt = (RadioButton) findViewById(R.id.me);
        takeBt = (RadioButton) findViewById(R.id.talk);

        tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("take").setIndicator("take").setContent(takeIntent));
        tabHost.addTab(tabHost.newTabSpec("address").setIndicator("address").setContent(addressIntent));
        tabHost.addTab(tabHost.newTabSpec("find").setIndicator("find").setContent(findIntent));
        tabHost.addTab(tabHost.newTabSpec("me").setIndicator("me").setContent(meIntent));

        findBt.setOnCheckedChangeListener(this);
        meBt.setOnCheckedChangeListener(this);
        takeBt.setOnCheckedChangeListener(this);
        addressBt.setOnCheckedChangeListener(this);
        /*显示App icon左侧的back键
         ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean ischeak) {
        if (ischeak) {
            switch (view.getId()) {
                case R.id.talk:
                    tabHost.setCurrentTabByTag("take");
                    break;
                case R.id.find:
                    tabHost.setCurrentTabByTag("find");
                    break;
                case R.id.me:
                    tabHost.setCurrentTabByTag("me");
                    break;
                case R.id.address:
                    tabHost.setCurrentTabByTag("address");
                    break;
                default:
                    break;
            }
        }

    }
}