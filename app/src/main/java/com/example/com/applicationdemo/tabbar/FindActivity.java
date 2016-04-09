package com.example.com.applicationdemo.tabbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.com.application.R;
import com.example.com.applicationdemo.PlayActivity;
import com.example.com.applicationdemo.dialog.CustomProgressDialog;
import com.example.com.applicationdemo.dialog.SweetAlertDialog;
import com.example.com.applicationdemo.pay.PayDemoActivity;

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

          /*  SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            // 设置加载的的速度：值越小越快
            // pDialog.getProgressHelper().setSpinSpeed(10.0f);

            pDialog.setTitleText(getResources().getString(R.string.LOADING));
            pDialog.setCancelable(true);
            pDialog.show();
           // pDialog.cancel();*/
           // pDialog.showCancelButton(true);


     /*       ProgressDialog xh_pDialog  = new ProgressDialog(this);
            // 设置进度条风格，风格为圆形，旋转的
            xh_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // 设置ProgressDialog 标题
            //xh_pDialog.setTitle("提示");
            // 设置ProgressDialog提示信息
            xh_pDialog.setMessage(getResources().getString(R.string.LOADING));
            // 设置ProgressDialog标题图标
            xh_pDialog.setIcon(R.mipmap.msp_icon);
            // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
            xh_pDialog.setIndeterminate(false);
            // 设置ProgressDialog 是否可以按退回键取消
            xh_pDialog.setCancelable(true);
            // 设置ProgressDialog 的一个Button
            // xh_pDialog.setButton("确定", new Bt1DialogListener());
            // 让ProgressDialog显示
            xh_pDialog.show();*/

            CustomProgressDialog dialog  =  CustomProgressDialog.createDialog(this);
            dialog.setMessage("正在加载中...");
            dialog.show();
            // dialog.dismiss();


            /*xh_count = 0;
            // 创建ProgressDialog对象
            xh_pDialog = new ProgressDialog(Activity01.this);
            // 设置进度条风格，风格为圆形，旋转的
            xh_pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // 设置ProgressDialog 标题
            xh_pDialog.setTitle("提示");
            // 设置ProgressDialog提示信息
            xh_pDialog.setMessage("这是一个长形进度条对话框");
            // 设置ProgressDialog标题图标
            xh_pDialog.setIcon(R.drawable.img2);
            // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
            xh_pDialog.setIndeterminate(false);
            // 设置ProgressDialog 进度条进度
            xh_pDialog.setProgress(100);
            // 设置ProgressDialog 是否可以按退回键取消
            xh_pDialog.setCancelable(true);
            // 让ProgressDialog显示
            xh_pDialog.show();
            new Thread() {
                @Override
                public void run() {
                    try {
                        while (xh_count <= 100) {
                            // 由线程来控制进度
                            xh_pDialog.setProgress(xh_count++);
                            Thread.sleep(100);
                        }
                        xh_pDialog.cancel();
                    } catch (Exception e) {
                        xh_pDialog.cancel();
                    }
                }
            }.start();

        }*/

        //从启动动画ui跳转到主ui
            Intent intent = new Intent(FindActivity.this,PayDemoActivity.class);
            // startActivity(intent);

          //   Toast.makeText(getApplicationContext(), "imageView3", Toast.LENGTH_LONG).show();
        }
    }

}
