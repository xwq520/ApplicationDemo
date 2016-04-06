package com.example.com.applicationdemo.tabbar;

import android.content.Intent;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.com.application.R;
import com.example.com.applicationdemo.PlayActivity;

public class FindActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintab_find);
         /*显示App icon左侧的back键*/
        ActionBar actionBar = getSupportActionBar();
        // actionBar.setDisplayHomeAsUpEnabled(true);

        //ActionBar 设置自定义组件
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                       Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        View customNav = LayoutInflater.from(this).inflate(R.layout.maintab_find_heard_menu, null);
        actionBar.setCustomView(customNav, lp);
        actionBar.setDisplayShowCustomEnabled(true);

        View imageView2 = customNav.findViewById(R.id.imageView2);
        View imageView3 = customNav.findViewById(R.id.imageView3);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
       //  setTitle("计划");  返回按钮后面的页面名称
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imageView2){
            //从启动动画ui跳转到主ui
            Intent intent = new Intent(FindActivity.this,PlayActivity.class);
            startActivity(intent);
            // SplachPageActivity.this.finish(); // 结束引导界面
        }else if(v.getId() == R.id.imageView3){
            Toast.makeText(getApplicationContext(), "imageView3", Toast.LENGTH_LONG).show();
        }
    }

}
