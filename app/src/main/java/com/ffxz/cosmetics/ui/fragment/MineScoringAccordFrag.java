package com.ffxz.cosmetics.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ffxz.cosmetics.R;
import com.ffxz.cosmetics.base.Key;
import com.ffxz.cosmetics.base.LazyLoadFragment;
import com.ffxz.cosmetics.base.URLBuilder;
import com.ffxz.cosmetics.model.ScoringDetailEntity;
import com.ffxz.cosmetics.ui.activity.MineScoringAccordDetialActivity;
import com.ffxz.cosmetics.ui.adapter.MineScoringAccordAdapter;
import com.ffxz.cosmetics.util.LogUtils;
import com.ffxz.cosmetics.util.ToastUtils;
import com.ffxz.cosmetics.util.Utils;
import com.ffxz.cosmetics.widget.ProgressLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Suo on 2018/3/24.
 */

public class MineScoringAccordFrag extends LazyLoadFragment {

	@BindView(R.id.xrecyclerView)
	XRecyclerView mRecyclerView;
	@BindView(R.id.progress_layout)
	ProgressLayout mProgressLayout;

	MineScoringAccordAdapter mAdapter;
	List<ScoringDetailEntity.ScoringDetialData> mList;
	private int flag;
	private int pageNum = 1;


	public static MineScoringAccordFrag instant(int flag) {
		MineScoringAccordFrag fragment = new MineScoringAccordFrag();
		fragment.flag = flag;
		return fragment;
	}

	@Override
	protected int setContentView() {
		return R.layout.fragment_mine_scoring_detail;
	}

	@Override
	protected void initView() {
		mList = new ArrayList<>();
//        mDialog = new CustomProgressDialog(getActivity());
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
		mAdapter = new MineScoringAccordAdapter(getActivity(), mList);
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.setOnItemClickListener(new MineScoringAccordAdapter.SpendDetialClickListener() {
			@Override
			public void onItemClick(View view, int postion) {
				Intent intent = new Intent(getActivity(), MineScoringAccordDetialActivity.class);
				intent.putExtra("applyId", mList.get(postion - 1).getApplyId());
				startActivity(intent);
			}
		});

		mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				pageNum = 1;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						doRefreshData();
					}
				}, 500);

			}

			@Override
			public void onLoadMore() {
				pageNum++;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						doRequestData();
						mRecyclerView.setPullRefreshEnabled(false);
					}
				}, 500);
			}
		});
		mRecyclerView.refresh();
	}

	@Override
	protected void initData() {
	}

	@Override
	protected void lazyLoad() {
	}

	private void doRefreshData() {
		mProgressLayout.showContent();
		Map<String, String> map = new HashMap<>();
		map.put("userId", mUtils.getUid());
		map.put("pageNum", pageNum + "");
		map.put("types", "3");
		map.put("state", flag + "");
		LogUtils.i("传输的值" + URLBuilder.format(map));
		OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/user/searchScore").tag(this)
				.addParams(Key.data, URLBuilder.format(map))
				.build().execute(new Utils.MyResultCallback<ScoringDetailEntity>() {

			@Override
			public void onError(Call call, Exception e) {
				super.onError(call, e);
				mRecyclerView.refreshComplete();
				if (call.isCanceled()) {
					call.cancel();
				} else {
					mProgressLayout.showNetError(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (mList != null && !mList.isEmpty()) {
								mList.clear();
								mAdapter.notifyDataSetChanged();
							}
							mRecyclerView.refresh();
						}
					});
				}
			}

			@Override
			public ScoringDetailEntity parseNetworkResponse(Response response) throws Exception {
				String json = response.body().string().trim();
				LogUtils.i("json的值" + json);
				return new Gson().fromJson(json, ScoringDetailEntity.class);
			}

			@Override
			public void onResponse(ScoringDetailEntity response) {

				if (response != null && response.HTTP_OK.equals(response.getCode())) {
					if (response.getData().size() != 0) {
						mList.clear();
						mList.addAll(response.getData());
						mAdapter.notifyDataSetChanged();
						mProgressLayout.showContent();
					} else if (response.getData().size() == 0) {
						mProgressLayout.showNone(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
							}
						});
					}

				} else {
//                    ToastUtils.showToast(getActivity(),"故障"+response.getMsg());
					mProgressLayout.showNetError(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (mList != null && !mList.isEmpty()) {
								mList.clear();
								mAdapter.notifyDataSetChanged();
							}
							mRecyclerView.refresh();
						}
					});
				}
				mRecyclerView.refreshComplete();
			}
		});
	}

	private void doRequestData() {
		Map<String, String> map = new HashMap<>();
		map.put("userId", mUtils.getUid());
		map.put("pageNum", pageNum + "");
		map.put("types", "3");
		map.put("state", flag + "");
		LogUtils.i("传输的值" + URLBuilder.format(map));
		OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/user/searchScore").tag(this)
				.addParams("data", URLBuilder.format(map))
				.build().execute(new Utils.MyResultCallback<ScoringDetailEntity>() {
			@Override
			public ScoringDetailEntity parseNetworkResponse(Response response) throws Exception {
				String json = response.body().string().trim();
				LogUtils.i("json的值" + json);
				return new Gson().fromJson(json, ScoringDetailEntity.class);
			}

			@Override
			public void onResponse(ScoringDetailEntity info) {
				if (info != null && info.HTTP_OK.equals(info.getCode())) {
					if (info.getData().size() != 0) {
						mList.addAll(info.getData());
						mAdapter.notifyDataSetChanged();
						mRecyclerView.setPullRefreshEnabled(true);
						mRecyclerView.loadMoreComplete();
					} else if (info.getData().size() == 0) {
						mRecyclerView.setNoMore(true);
						mRecyclerView.setPullRefreshEnabled(true);
						pageNum--;
					}
					mProgressLayout.showContent();
				} else {
					ToastUtils.showToast(getActivity(), "网络异常");
					pageNum--;
					mRecyclerView.setPullRefreshEnabled(true);
					mRecyclerView.loadMoreComplete();
				}
			}

			@Override
			public void onError(Call call, Exception e) {
				super.onError(call, e);
				mRecyclerView.loadMoreComplete();
				mRecyclerView.setPullRefreshEnabled(true);
				if (call.isCanceled()) {
					LogUtils.i("我进入到加载更多cancel了");
					call.cancel();
				} else if (pageNum != 1) {
					LogUtils.i("加载更多的Log");
					ToastUtils.showToast(getActivity(), "网络故障,请稍后再试");
					pageNum--;
				}
			}
		});
	}
}
