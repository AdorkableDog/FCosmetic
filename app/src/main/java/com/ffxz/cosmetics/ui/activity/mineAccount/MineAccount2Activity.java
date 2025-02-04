package com.ffxz.cosmetics.ui.activity.mineAccount;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ffxz.cosmetics.R;
import com.ffxz.cosmetics.base.BaseActivity;
import com.ffxz.cosmetics.model.AccordMoneyEntity;
import com.ffxz.cosmetics.model.AccountEntity;
import com.ffxz.cosmetics.ui.activity.MineAccountWithdrawActivity;
import com.ffxz.cosmetics.ui.adapter.AccountProfitAdapter;
import com.ffxz.cosmetics.util.IntentUtils;
import com.ffxz.cosmetics.util.ToastUtils;
import com.ffxz.cosmetics.widget.Dialog.CustomProgressDialog;
import com.ffxz.cosmetics.widget.ProgressLayout;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Suo on 2018/3/12.
 *
 * @TODO 个人中心我的账户  账户余额 2.0修改界面
 */

public class MineAccount2Activity extends BaseActivity implements MineAccount_contract.View {

	@BindView(R.id.progress_layout)
	ProgressLayout mProgressLayout;
	@BindView(R.id.xrecyclerView)
	XRecyclerView mRecyclerView;
	@BindView(R.id.tv_cash)
	TextView tvCash;
	@BindView(R.id.tv_withdrawal)
	TextView tvWithdrawal;
	@BindView(R.id.tv_name)
	TextView tvName;
	@BindView(R.id.tv_money)
	TextView tvMoney;
	private TextView[] mTextView;

	AccountProfitAdapter mAdapter;
	List<AccountEntity.AccountData> mList;
	private int pageNum = 1;
	String userMoney = "0.00", userCash = "0.00";
	int type = 0;

	CustomProgressDialog mDialog;
	private MineAccount_contract.Presenter mineAccPresenter = new MineAccount_Presenter(this);


	@Override
	protected int getContentView() {
		return R.layout.activity_mine_account1;
	}

	@Override
	protected void initView() {
		setTitleInfo();
		transTitle();
		initDatas();
		mTextView = new TextView[]{tvCash, tvWithdrawal};
		selected(0);
	}

	private void initDatas() {
		mList = new ArrayList<>();
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
		mAdapter = new AccountProfitAdapter(this, mList);
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.setOnItemClickListener(new AccountProfitAdapter.ProfitDetialClickListener() {
			@Override
			public void onItemClick(View view, int postion) {

			}
		});
		mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				pageNum = 1;
				new Handler().postDelayed(new Runnable() {
					public void run() {
						mineAccPresenter.doRefreshData(type, pageNum, mProgressLayout, mRecyclerView, mList);
					}
				}, 500);
			}

			@Override
			public void onLoadMore() {
				pageNum++;
				new Handler().postDelayed(new Runnable() {
					public void run() {
						mRecyclerView.setPullRefreshEnabled(false);
						mineAccPresenter.doRequestData(type, pageNum, mProgressLayout, mRecyclerView, mList);
					}
				}, 500);
			}
		});
		mRecyclerView.refresh();
	}

	private void setTitleInfo() {
		setTitleText("我的钱包");
	}


	@TargetApi(21)
	private void transTitle() {
		if (Build.VERSION.SDK_INT >= 21) {
			View decorView = getWindow().getDecorView();
			int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
			decorView.setSystemUiVisibility(option);
			getWindow().setStatusBarColor(Color.TRANSPARENT);
		}
	}


	@Override
	protected void initData() {
		mDialog = new CustomProgressDialog(this);
		mineAccPresenter.doAsyncGetData(mUtils);
	}


	@OnClick({R.id.tv_cash, R.id.tv_withdrawal, R.id.ll_withdrawal})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.tv_withdrawal:
				selected(0);
				tvMoney.setText(userMoney);
				tvName.setText("账户余额");
				type = 0;
				mRecyclerView.refresh();
				break;
			case R.id.tv_cash:
				Log.e("-0-", "1tv" + userMoney);
				selected(1);
				tvMoney.setText(userCash);
				tvName.setText("提现金额");
				type = 1;
				mRecyclerView.refresh();
				break;
			case R.id.ll_withdrawal:
				if (mUtils.isLogin()) {
					Intent intentWithdraw = new Intent(this, MineAccountWithdrawActivity.class);
					intentWithdraw.putExtra("userMoney", userMoney);
					startActivity(intentWithdraw);
				} else {
					IntentUtils.IntentToLogin(this);
				}
				break;
		}
	}


	@Override
	public void dismissDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	@Override
	public void notifyDataSetChanged() {
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void pageNumReduce() {
		pageNum--;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissDialog();
		OkHttpUtils.getInstance().cancelTag(this);
	}

	@Override
	public void initDialog() {
		if (mDialog == null) {
			mDialog = new CustomProgressDialog(MineAccount2Activity.this);
			if (!isFinishing()) {
				mDialog.show();
			}
		} else {
			if (!isFinishing()) {
				mDialog.show();
			}
		}
	}

	@Override
	public void showToast(String s) {
		if (s != null) {
			ToastUtils.showToast(MineAccount2Activity.this, s);
		} else {
			ToastUtils.showToast(MineAccount2Activity.this, "网络故障,请稍后再试");
		}
	}

	@Override
	public void setDatas(AccordMoneyEntity.DataBean data) {
		userMoney = data.getUserMoney();
		userCash = data.getUserCash();
		tvMoney.setText(userMoney);
	}


	public void selected(int position) {
		for (int i = 0; i < mTextView.length; i++) {
			if (position == i) {
			} else {
				mTextView[i].setSelected(false);
			}
		}
		mTextView[position].setSelected(true);
	}
}
