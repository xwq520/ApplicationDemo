package com.example.com.applicationdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.com.application.R;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);// 让进度条显示在标题栏上

        //实例化WebView对象
        WebView webview = new WebView(this);
        //设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        // 取消滚动条
        // webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        // 获取传入的URL和标题
        Intent intent1 = getIntent();
        // 用intent1.getStringExtra()来得到上一个ACTIVITY发过来的字符串。
        String WebView_URL = intent1.getStringExtra("WebView_URL");
        String WebView_TITLE = intent1.getStringExtra("WebView_TITLE");


        //设置Web视图
        setContentView(webview);

        // 在自己的app里打开URL连接
        webview.setWebViewClient(new webViewClient());
        // h会提示选择浏览器打开URL资源
        // webview.setWebChromeClient(new webChromeClient());
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                // 当加载到100%的时候 进度条自动消失
              //  Toast.makeText(WebViewActivity.this,progress+"",Toast.LENGTH_LONG).show();
                WebViewActivity.this.setProgress(progress * 100);
            }
        });
        //加载需要显示的网页
        webview.loadUrl(WebView_URL);

        ActionBar actionBar = getSupportActionBar();
        //这两句就可以让actionBar的图标可以响应点击事件
        actionBar.setDisplayShowHomeEnabled(true);
        // 显示头部返回的图标
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(WebView_TITLE);  // 返回按钮后面的页面名称
    }


    class webViewClient extends WebViewClient {

        //网页页面开始加载的时候
        @Override
        public void onPageStarted(WebView view, String url,Bitmap favicon) {
           /* if (progressDialog == null) {
                progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setMessage("数据加载中，请稍后。。。");
                progressDialog.show();
                // 当加载网页的时候将网页进行隐藏
                view.setEnabled(false);
            }*/
            super.onPageStarted(view, url, favicon);
        }

            //重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //如果不需要其他对点击链接事件的处理返回true，否则返回false
                return true;
            }
           //页面加载完成后执行
           @Override
            public void onPageFinished(WebView view, String url) {
               /*if (progressDialog != null && progressDialog.isShowing()) {
                   progressDialog.dismiss();
                   progressDialog = null;
                   view.setEnabled(true);
               }*/
               super.onPageFinished(view, url);
           }

        // 是否对URL进行拦截
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }
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