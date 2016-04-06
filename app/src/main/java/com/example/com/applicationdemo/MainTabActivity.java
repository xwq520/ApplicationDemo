package com.example.com.applicationdemo;


import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;

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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("确认退出吗？")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        MainTabActivity.this.finish();
                        // 结束进程
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
// super.onBackPressed();
    }


    // 当继承TabActivity时，同学们是不是onKeyDown方法没用，那是应为冲突了，可以用dispatchKeyEvent方法
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK &&
                event.getRepeatCount() == 0 &&
                event.getAction() == KeyEvent.ACTION_DOWN)        {

            onBackPressed();

            return false;
        }else{
            return super.dispatchKeyEvent(event);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}