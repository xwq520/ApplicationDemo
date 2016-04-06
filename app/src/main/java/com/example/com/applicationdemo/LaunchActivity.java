package com.example.com.applicationdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.com.application.R;



/**
 *  app 启动页
 */
public class LaunchActivity extends Activity {

    private ImageView imgView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// hide titlebar of application
        // must be before setting the layout
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        // hide statusbar of Android
        // could also be done later
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_launch);

        SharedPreferences setting = getSharedPreferences("APP_PRE_FIRST", 0);
        Boolean user_first = setting.getBoolean("FIRST",true);
        if(user_first){//第一次
            setting.edit().putBoolean("FIRST", false).commit();

            // 启动页展示屏的核心代码
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 跳转到引导页
                    Intent intent = new Intent(LaunchActivity.this, SplachPageActivity.class);
                    startActivity(intent);
                    LaunchActivity.this.finish();
                }
            }, 2000); //启动动画持续2秒钟

        }else{ // 不是第一次
            // 启动页展示屏的核心代码
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //从启动动画ui跳转到主ui
                    Intent intent = new Intent(LaunchActivity.this, MainTabActivity.class);
                    startActivity(intent);
                    LaunchActivity.this.finish(); // 结束启动动画界面
                }
            }, 3000); //启动动画持续3秒钟
        }
    }
}
