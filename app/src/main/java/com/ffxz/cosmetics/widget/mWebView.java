package com.ffxz.cosmetics.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.EditText;

/**
 * Created by Administrator on 2018/9/4 0004.
 */

public class mWebView extends WebView {
	public EditText mFocusDistraction;
	public Context mContext;
	public mWebView(Context context) {
		super(context);
		init(context);
	}

	public mWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public mWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	@SuppressLint("NewApi")
	public mWebView(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing) {
		super(context, attrs, defStyle, privateBrowsing);
		init(context);
	}

	public void init(Context context) {
		// This lets the layout editor display the view.
		if (isInEditMode()) return;

		mContext = context;

		mFocusDistraction = new EditText(context);
		mFocusDistraction.setBackgroundResource(android.R.color.transparent);
		this.addView(mFocusDistraction);
		mFocusDistraction.getLayoutParams().width = 1;
		mFocusDistraction.getLayoutParams().height = 1;
	}
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		invalidate();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
	                            int scrollRangeX, int scrollRangeY, int maxOverScrollX,
	                            int maxOverScrollY, boolean isTouchEvent) {
		return false;
	}
	/**
	 * 使WebView不可滚动
	 * */
	@Override
	public void scrollTo(int x, int y){
		super.scrollTo(0,0);
	}
}
