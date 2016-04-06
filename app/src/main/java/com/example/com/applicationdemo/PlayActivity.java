package com.example.com.applicationdemo;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.com.application.R;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

       /* View customNav = LayoutInflater.from(this).inflate(R.layout.titlebtn, null);
        Toolbar mToolbar = (Toolbar) customNav.findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);*/

        ActionBar actionBar = getSupportActionBar();
        //这两句就可以让actionBar的图标可以响应点击事件
        actionBar.setDisplayShowHomeEnabled(true);
        // 显示头部返回的图标
        actionBar.setDisplayHomeAsUpEnabled(true);

       Button button = (Button)findViewById(R.id.button);
       button.setOnClickListener(this);
        setTitle("计划画面");  // 返回按钮后面的页面名称
    }

    // 返回上一个画面的功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
               final Intent intent = new Intent();
               intent.setClass(this,MainActivity.class);
               startActivity(intent);
               // finish();
            default:
        }
    }

}