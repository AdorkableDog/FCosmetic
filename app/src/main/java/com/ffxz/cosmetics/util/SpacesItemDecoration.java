package com.ffxz.cosmetics.util;

/**
 * Created by yulu on 2019/6/11.
 */

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by Au61 on 2016/1/15.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

	private SpacesItemDecorationEntrust mEntrust;
	private int mColor;
	private int leftRight;
	private int topBottom;

	private int currentChildPosition;//指定位置添加分割线


	public SpacesItemDecoration(int leftRight, int topBottom) {
		this.leftRight = leftRight;
		this.topBottom = topBottom;
	}

	public SpacesItemDecoration(int leftRight, int topBottom, int mColor,int currentChildPosition) {
		this(leftRight, topBottom);
		this.mColor = mColor;
		this.currentChildPosition = currentChildPosition;
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
		if (mEntrust == null) {
			mEntrust = getEntrust(parent.getLayoutManager());
		}
		mEntrust.onDraw(c, parent, state);
		super.onDraw(c, parent, state);
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		if (mEntrust == null) {
			mEntrust = getEntrust(parent.getLayoutManager());
		}

		mEntrust.getItemOffsets(outRect, view, parent, state);
	}

	private SpacesItemDecorationEntrust getEntrust(RecyclerView.LayoutManager manager) {
		SpacesItemDecorationEntrust entrust = null;
		//要注意这边的GridLayoutManager是继承LinearLayoutManager，所以要先判断GridLayoutManager

		if (manager instanceof GridLayoutManager) {
			entrust = new GridEntrust(leftRight, topBottom, mColor,currentChildPosition);
		} else if (manager instanceof StaggeredGridLayoutManager) {
			entrust = new StaggeredGridEntrust(leftRight, topBottom, mColor);
		} else {//其他的都当做Linear来进行计算
			entrust = new LinearEntrust(leftRight, topBottom, mColor);
		}
		return entrust;
	}

}
