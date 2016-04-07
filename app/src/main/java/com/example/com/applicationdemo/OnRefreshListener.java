package com.example.com.applicationdemo;

import android.view.View;
import android.widget.AdapterView;

public interface OnRefreshListener {

	/**
	 * 下拉刷新
	 */
	void onDownPullRefresh();

	/**
	 * 上拉加载更多
	 */
	void onLoadingMore();

}
