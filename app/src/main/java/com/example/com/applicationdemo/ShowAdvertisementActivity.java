package com.example.com.applicationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.application.R;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @功能描述：引导页主程序入口类
 */
public class ShowAdvertisementActivity extends AppCompatActivity {

    // 是否显示头部的广告栏
    private int imageIds[];
    private String[] titles;
    private ArrayList<ImageView> images;
    private ArrayList<View> dots;
    private TextView title;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private int oldPosition = 0;//记录上一次点的位置
    private int currentItem; //当前页面
    private ScheduledExecutorService scheduledExecutorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heard_advertisement_layout);
    //图片ID
    imageIds = new int[]{
        R.mipmap.splash_page1,
                R.mipmap.splash_page2,
                R.mipmap.splash_page3,
                R.mipmap.splash_page4,
                R.mipmap.splash_page1,
    };
    //图片标题
    titles = new String[]{
        "巩俐不低俗，我就不能低俗",
                "扑树又回来啦！再唱经典老歌引万人大合唱",
                "揭秘北京电影如何升级",
                "乐视网TV版大派送",
                "热血屌丝的反杀"
    };
    //显示的图片
    images = new ArrayList<ImageView>();
    for(int i =0; i < imageIds.length; i++){
        ImageView imageView = new ImageView(getApplicationContext());

        imageView.setBackgroundResource(imageIds[i]);
        images.add(imageView);
    }
    //显示的点
    dots = new ArrayList<View>();
    dots.add(findViewById(R.id.dot_0));
    dots.add(findViewById(R.id.dot_1));
    dots.add(findViewById(R.id.dot_2));
    dots.add(findViewById(R.id.dot_3));
    dots.add(findViewById(R.id.dot_4));
    title = (TextView) findViewById(R.id.title_heard);
    title.setText(titles[0]);
    mViewPager = (ViewPager) findViewById(R.id.viewpagerHeard);
    adapter = new ViewPagerAdapter(images);
    mViewPager.setAdapter(adapter);
    mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            title.setText(titles[position]);
			    /* dots.get(oldPosition).setBackgroundResource(R.mipmap.point_normal);
				 dots.get(position).setBackgroundResource(R.mipmap.point_select);
                 */
            dots.get(oldPosition).setEnabled(false);
            dots.get(position).setEnabled(true);

            oldPosition = position;
            currentItem = position;

        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        /**
         * ViewPager的监听器
         * 当ViewPager中页面的状态发生改变时调用
         *
         */
        @Override
        public void onPageScrollStateChanged(int arg0) {
            boolean isAutoPlay = false;
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        mViewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (mViewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        mViewPager.setCurrentItem(mViewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }
    });

    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    //每隔2秒钟切换一张图片
    scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 2, 2, TimeUnit.SECONDS);
    // 停止轮播图切换  scheduledExecutorService.shutdown();
}

// 头部广告的处理
private class ViewPagerAdapter extends PagerAdapter {
    //界面列表
    private ArrayList<ImageView> views;

    public ViewPagerAdapter (ArrayList<ImageView> views){
        this.views = views;
    }

    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }
    //是否是同一张图片
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
    /**
     * 销毁position位置的界面
     */
    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView(views.get(position));
    }

    /**
     * 初始化position位置的界面
     */
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        view.addView(views.get(position));

        return views.get(position);
    }
}

//切换图片
private class ViewPagerTask implements Runnable {
    @Override
    public void run() {
        currentItem = (currentItem +1) % imageIds.length;
        //更新界面
        handler.obtainMessage().sendToTarget();
    }
}
private Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        //设置当前页面
        mViewPager.setCurrentItem(currentItem);
    }
};

}
