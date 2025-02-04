package com.ffxz.cosmetics.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2018/8/9 0009.
 */

public class MTextView extends android.support.v7.widget.AppCompatTextView {


	private DrawableLeftListener mLeftListener;
	private DrawableRightListener mRightListener;


	final int DRAWABLE_LEFT = 0;
	final int DRAWABLE_TOP = 1;
	final int DRAWABLE_RIGHT = 2;
	final int DRAWABLE_BOTTOM = 3;


	public MTextView(Context context) {
		super(context);
	}

	public MTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public MTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setDrawableLeftListener(DrawableLeftListener listener) {
		this.mLeftListener = listener;
	}

	public void setDrawableRightListener(DrawableRightListener listener) {
		this.mRightListener = listener;

	}


	public interface DrawableLeftListener {
		void onDrawableLeftClick(View view);
	}

	public interface DrawableRightListener {
		void onDrawableRightClick(View view);

	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (mRightListener != null) {
					Drawable drawableRight = getCompoundDrawables()[DRAWABLE_RIGHT];
					if (drawableRight != null && event.getRawX() >= (getRight() - drawableRight.getBounds().width())) {
						mRightListener.onDrawableRightClick(this);
						return true;
					}
				}
				if (mLeftListener != null) {
					Drawable drawableLeft = getCompoundDrawables()[DRAWABLE_LEFT];
					if (drawableLeft != null && event.getRawX() <= (getLeft() + drawableLeft.getBounds().width()))
						mLeftListener.onDrawableLeftClick(this);
					return true;
				}
				break;
		}
		return super.onTouchEvent(event);

	}
}
