package com.example.com.applicationdemo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.com.application.R;

public class RefreshListView extends ListView implements OnScrollListener,View.OnClickListener{

	private static final String TAG = "RefreshListView";
	private int firstVisibleItemPosition; // 屏幕显示在第一个的item的索引
	private int downY; // 按下时y轴的偏移量
	private int headerViewHeight; // 头布局的高度
	private View headerView; // 头布局的对象

	private final int DOWN_PULL_REFRESH = 0; // 下拉刷新状态
	private final int RELEASE_REFRESH = 1; // 松开刷新
	private final int REFRESHING = 2; // 正在刷新中
	private int currentState = DOWN_PULL_REFRESH; // 头布局的状态: 默认为下拉刷新状态

	private Animation upAnimation; // 向上旋转的动画
	private Animation downAnimation; // 向下旋转的动画

	private ImageView ivArrow; // 头布局的剪头
	private ProgressBar mProgressBar; // 头布局的进度条
	private TextView tvState; // 头布局的状态
	private TextView tvLastUpdateTime; // 头布局的最后更新时间

	private OnRefreshListener mOnRefershListener;
	private boolean isScrollToBottom; // 是否滑动到底部
	private View footerView; // 脚布局的对象
	private int footerViewHeight; // 脚布局的高度
	private boolean isLoadingMore = false; // 是否正在加载更多中

	// 是否显示头部的广告栏
	private int imageIds[];
	private int dotsTds[];
	private String[] titles;
	private ArrayList<ImageView> images;
	private ArrayList<View> dots;
	private TextView title;
	private ViewPager mViewPager;
	private ViewPagerAdapter adapter;
	private int oldPosition = 0;//记录上一次点的位置
	private int currentItem; //当前页面
	private ScheduledExecutorService scheduledExecutorService;

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
		this.setOnScrollListener(this);
	}

	/**
	 * 初始化脚布局
	 */
	private void initFooterView() {
		footerView = View.inflate(getContext(), R.layout.listview_footer, null);
		footerView.measure(0, 0);
		footerViewHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, -footerViewHeight, 0, 0);
		this.addFooterView(footerView);
	}

	/**
	 * 初始化头布局
	 */
	private void initHeaderView() {
		headerView = View.inflate(getContext(), R.layout.listview_header, null);
		ivArrow = (ImageView) headerView.findViewById(R.id.iv_listview_header_arrow);
		mProgressBar = (ProgressBar) headerView.findViewById(R.id.pb_listview_header);
		tvState = (TextView) headerView.findViewById(R.id.tv_listview_header_state);
		tvLastUpdateTime = (TextView) headerView.findViewById(R.id.tv_listview_header_last_update_time);

		// 设置最后刷新时间
		tvLastUpdateTime.setText("最后刷新时间: " + getLastUpdateTime());

		headerView.measure(0, 0); // 系统会帮我们测量出headerView的高度
		headerViewHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -headerViewHeight, 0, 0);
		this.addHeaderView(headerView); // 向ListView的顶部添加一个view对象
		initAnimation();
	}

	/**
	 * 获得系统的最新时间
	 *
	 * @return
	 */
	private String getLastUpdateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(System.currentTimeMillis());
	}

	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		upAnimation = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(500);
		upAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上

		downAnimation = new RotateAnimation(-180f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		downAnimation.setDuration(500);
		downAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downY = (int) ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				int moveY = (int) ev.getY();
				// 移动中的y - 按下的y = 间距.
				int diff = (moveY - downY) / 2;
				// -头布局的高度 + 间距 = paddingTop
				int paddingTop = -headerViewHeight + diff;
				// 如果: -头布局的高度 > paddingTop的值 执行super.onTouchEvent(ev);
				if (firstVisibleItemPosition == 0 && -headerViewHeight < paddingTop) {
					if (paddingTop > 0 && currentState == DOWN_PULL_REFRESH) { // 完全显示了.
						Log.i(TAG, "松开刷新");
						currentState = RELEASE_REFRESH;
						refreshHeaderView();
					} else if (paddingTop < 0 && currentState == RELEASE_REFRESH) { // 没有显示完全
						Log.i(TAG, "下拉刷新");
						currentState = DOWN_PULL_REFRESH;
						refreshHeaderView();
					}
					// 下拉头布局
					headerView.setPadding(0, paddingTop, 0, 0);
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				// 判断当前的状态是松开刷新还是下拉刷新
				if (currentState == RELEASE_REFRESH) {
					Log.i(TAG, "刷新数据.");
					// 把头布局设置为完全显示状态
					headerView.setPadding(0, 0, 0, 0);
					// 进入到正在刷新中状态
					currentState = REFRESHING;
					refreshHeaderView();

					if (mOnRefershListener != null) {
						mOnRefershListener.onDownPullRefresh(); // 调用使用者的监听方法
					}
				} else if (currentState == DOWN_PULL_REFRESH) {
					// 隐藏头布局
					headerView.setPadding(0, -headerViewHeight, 0, 0);
				}
				break;
			default:
				break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 根据currentState刷新头布局的状态
	 */
	private void refreshHeaderView() {
		switch (currentState) {
			case DOWN_PULL_REFRESH: // 下拉刷新状态
				tvState.setText("下拉刷新");
				ivArrow.startAnimation(downAnimation); // 执行向下旋转
				break;
			case RELEASE_REFRESH: // 松开刷新状态
				tvState.setText("松开刷新");
				ivArrow.startAnimation(upAnimation); // 执行向上旋转
				break;
			case REFRESHING: // 正在刷新中状态
				ivArrow.clearAnimation();
				ivArrow.setVisibility(View.GONE);
				mProgressBar.setVisibility(View.VISIBLE);
				tvState.setText("正在刷新中...");
				break;
			default:
				break;
		}
	}

	/**
	 * 当滚动状态改变时回调
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
			// 判断当前是否已经到了底部
			if (isScrollToBottom && !isLoadingMore) {
				isLoadingMore = true;
				// 当前到底部
				Log.i(TAG, "加载更多数据");
				footerView.setPadding(0, 0, 0, 0);
				this.setSelection(this.getCount());

				if (mOnRefershListener != null) {
					mOnRefershListener.onLoadingMore();
				}
			}
		}
	}

	/**
	 * 当滚动时调用
	 *
	 * @param firstVisibleItem
	 *            当前屏幕显示在顶部的item的position
	 * @param visibleItemCount
	 *            当前屏幕显示了多少个条目的总数
	 * @param totalItemCount
	 *            ListView的总条目的总数
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		firstVisibleItemPosition = firstVisibleItem;

		if (getLastVisiblePosition() == (totalItemCount - 1)) {
			isScrollToBottom = true;
		} else {
			isScrollToBottom = false;
		}
	}

	/**
	 * 设置刷新监听事件
	 *
	 * @param listener
	 */
	public void setOnRefreshListener(OnRefreshListener listener) {
		mOnRefershListener = listener;
	}

	/**
	 * 隐藏头布局
	 */
	public void hideHeaderView() {
		headerView.setPadding(0, -headerViewHeight, 0, 0);
		ivArrow.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
		tvState.setText("下拉刷新");
		tvLastUpdateTime.setText("最后刷新时间: " + getLastUpdateTime());
		currentState = DOWN_PULL_REFRESH;
	}

	public void hideFooterView() {
		footerView.setPadding(0, -footerViewHeight, 0, 0);
		isLoadingMore = false;
	}



	// 显示list头部的广告
	public void showAdvertisement(final int numb){

		View headerView2 = View.inflate(getContext(), R.layout.heard_advertisement_layout, null);
		this.addHeaderView(headerView2); // 向ListView的顶部添加一个view对象
		//图片ID
		imageIds = new int[]{
				R.mipmap.splash_page1,
				R.mipmap.splash_page2,
				R.mipmap.splash_page3,
				R.mipmap.splash_page4,
				R.mipmap.splash_page1,
		};

		dotsTds = new int[]{
				R.id.dot_0,
				R.id.dot_1,
				R.id.dot_2,
				R.id.dot_3,
				R.id.dot_4,
		};
		// 被选择的图标

		//图片标题
		titles = new String[]{
				"巩俐不低俗，我就不能低俗",
				"扑树又回来啦！再唱经典老歌引万人大合唱",
				"揭秘北京电影如何升级",
				"乐视网TV版大派送",
				"热血屌丝的反杀"
		};
		// 显示的广告图片
		images = new ArrayList<ImageView>();
		for(int i =0; i < numb; i++){
			ImageView imageView = new ImageView(getContext());
			imageView.setOnClickListener(RefreshListView.this);
			imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,100));
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setBackgroundResource(imageIds[i]);
			images.add(imageView);
		}
		// 显示的点
		dots = new ArrayList<View>();
		for(int i =0; i < numb; i++){
			View view = headerView2.findViewById(dotsTds[i]);
			dots.add(view);
		}

		title = (TextView) headerView2.findViewById(R.id.title_heard);
		title.setText(titles[0]);
		mViewPager = (ViewPager) headerView2.findViewById(R.id.viewpagerHeard);
		adapter = new ViewPagerAdapter(images);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				title.setText(titles[position]);
			    /* dots.get(oldPosition).setBackgroundResource(R.mipmap.point_normal);
				 dots.get(position).setBackgroundResource(R.mipmap.point_select);
                 */
				dots.get(position).setEnabled(false);
				dots.get(oldPosition).setEnabled(true);

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
			}
		});

		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		//每隔2秒钟切换一张图片
		scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 2, 2, TimeUnit.SECONDS);
		// 停止轮播图切换  scheduledExecutorService.shutdown();
	}

	// 点击某张广告的事件
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.putExtra("WebView_URL","https://www.baidu.com");
		intent.putExtra("WebView_TITLE", "活动详情");
		intent.setClass(getContext(), WebViewActivity.class);
		getContext().startActivity(intent);
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
