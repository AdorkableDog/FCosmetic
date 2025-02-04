package com.ffxz.cosmetics.ui.fragment.HomeGoodsListFrag;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ffxz.cosmetics.R;
import com.ffxz.cosmetics.base.LazyLoadFragment;
import com.ffxz.cosmetics.base.URLBuilder;
import com.ffxz.cosmetics.function.RecyclerSpace;
import com.ffxz.cosmetics.model.GoodsListEntity;
import com.ffxz.cosmetics.ui.activity.goodDetail.GoodsDetailActivity;
import com.ffxz.cosmetics.ui.adapter.HomeGoodsListAdapter;
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

import static com.ffxz.cosmetics.ui.adapter.HomeGoodsListAdapter.SPAN_COUNT_ONE;
import static com.ffxz.cosmetics.ui.adapter.HomeGoodsListAdapter.SPAN_COUNT_TWO;


/**
 * Created by Suo on 2016/12/12.
 */

public class HomeGoodsListFrag extends LazyLoadFragment {

	@BindView(R.id.xrecyclerView)
	XRecyclerView mRecyclerView;
	@BindView(R.id.progress_layout)
	ProgressLayout mProgressLayout;
//	@BindView(R.id.goods_list_tv_default)
//	TextView tvDefault;
//	@BindView(R.id.goods_list_tv_count)
//	TextView tvCount;
//	@BindView(R.id.goods_list_tv_price)
//	TextView tvPrice;
//	@BindView(R.id.goods_list_iv_count)
//	ImageView ivCount;
//	@BindView(R.id.goods_list_iv_icon)
//	ImageView ivClassIcon;
//	@BindView(R.id.goods_list_iv_price)
//	ImageView ivPrice;
	//	@BindView(R.id.goods_list_tv_info)
//	TextView tvInfo;
//	@BindView(R.id.goods_list_ll_pr)
//	LinearLayout goods_list_ll_pr;

	private String countTag = "default";
	private String priceTag = "default";

	private String orderby = "recommend";
	private String classifyId;
	private String type;
	private String name;
	private String TAG = null;
	private GridLayoutManager gridLayoutManager;

	HomeGoodsListAdapter mAdapter;
	List<GoodsListEntity.DataBean.ProductListBean> mList;

	private String productIds;
	//	private int flag;
	private int pageNum = 1;
	private static HomeGoodsListFrag fragment;
	private String shopId = null;


	//    CustomProgressDialog mDialog;


	public static HomeGoodsListFrag instant(String shopId, String classifyId, String type, String name, String TAG, String productIds) {
		if (fragment == null) {
			fragment = new HomeGoodsListFrag();
		}
		fragment.shopId = shopId;
		fragment.classifyId = classifyId;
		fragment.type = type;
		fragment.name = name;
		fragment.TAG = TAG;
		fragment.productIds = productIds;
		return fragment;
	}

	@Override
	protected int setContentView() {
		return R.layout.fragment_mine_store_list;
	}

	@Override
	protected void initView() {

//		mList = new ArrayList<>();
//        mDialog = new CustomProgressDialog(getActivity());
//		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//		mRecyclerView.setLayoutManager(layoutManager);
//		mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//		mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
//		mAdapter = new MineStoreListAdapter((StoreDetailActivity) getActivity(), mList, mUtils, flag);
//		mRecyclerView.setAdapter(mAdapter);


		mList = new ArrayList<>();
//		tvDefault.setSelected(true);

//		classifyId = getIntent().getStringExtra("classifyId");
//		productIds = getIntent().getStringExtra("productIds");
//		type = getIntent().getStringExtra("type");
//		name = getIntent().getStringExtra("name");
//		TAG = getIntent().getStringExtra("TAG");

		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
		gridLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT_ONE);
//		mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
		RecyclerSpace decor = new RecyclerSpace(2);
		decor.setItemOffets(1);
		mRecyclerView.addItemDecoration(decor);
//		mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

		mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);

		mAdapter = new HomeGoodsListAdapter(getActivity(), mList, gridLayoutManager);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setLayoutManager(gridLayoutManager);


		mAdapter.setOnItemClickListener(new HomeGoodsListAdapter.SpendDetialClickListener() {
			@Override
			public void onItemClick(View view, int postion) {
				Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
				intent.putExtra("productId", mList.get(postion - 1).getProduct_id());
//				intent.putExtra("sproductId", mList.get(postion - 1).getSproductId());
				startActivity(intent);
			}
		});

//        mRecyclerView.refresh();
//        MyApplication.isReserve = false;
	}

	public void setmRecyclerView() {

//		mRecyclerView.refresh();

//		if (mRecyclerView != null) {
//			mRecyclerView.smoothScrollToPosition(0);
//		}
	}

	@Override
	protected void initData() {
	}


	//@TODO　　
	@Override
	protected void lazyLoad() {
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


	public void setOnclick(int i) {
		switch (i) {
			case 1:
				countTag = "default";
				priceTag = "default";
//				tvCount.setSelected(false);
//				tvPrice.setSelected(false);
//				ivCount.setImageResource(R.mipmap.sort_by_default);
//				ivPrice.setImageResource(R.mipmap.sort_by_default);
//				tvDefault.setSelected(true);
				orderby = "recommend";
				mRecyclerView.refresh();
				break;
			case 2:
				priceTag = "default";
//				tvDefault.setSelected(false);
//				tvPrice.setSelected(false);
//				ivPrice.setImageResource(R.mipmap.sort_by_default);
//				tvCount.setSelected(true);
				switch (countTag) {
					case "default":
						countTag = "high";
//						ivCount.setImageResource(R.mipmap.high_to_low);
						orderby = "salesVolumeHigh";
						break;
					case "high":
						countTag = "low";
//						ivCount.setImageResource(R.mipmap.low_to_high);
						orderby = "salesVolumeLow";
						break;
					case "low":
						countTag = "high";
//						ivCount.setImageResource(R.mipmap.high_to_low);
						orderby = "salesVolumeHigh";
						break;
				}
				mRecyclerView.refresh();
				break;
			case 3:
				countTag = "default";
//				tvDefault.setSelected(false);
//				tvCount.setSelected(false);
//				ivCount.setImageResource(R.mipmap.sort_by_default);
//				tvPrice.setSelected(true);
				switch (priceTag) {
					case "default":
						priceTag = "high";
//						ivPrice.setImageResource(R.mipmap.high_to_low);
						orderby = "priceHigh";
						break;
					case "high":
						priceTag = "low";
//						ivPrice.setImageResource(R.mipmap.low_to_high);
						orderby = "priceLow";
						break;
					case "low":
						priceTag = "high";
//						ivPrice.setImageResource(R.mipmap.high_to_low);
						orderby = "priceHigh";
						break;
				}
				mRecyclerView.refresh();
				break;
			case 4:
				if (gridLayoutManager.getSpanCount() == SPAN_COUNT_ONE) {
					gridLayoutManager.setSpanCount(SPAN_COUNT_TWO);
//					ivClassIcon.setBackgroundResource(R.mipmap.shangpinliebao_fenlei_1);
				} else {
					gridLayoutManager.setSpanCount(SPAN_COUNT_ONE);
//					ivClassIcon.setBackgroundResource(R.mipmap.shangpinliebao_fenlei);
				}
				mAdapter.notifyDataSetChanged();
				break;
		}
	}


	private void doRefreshData() {
		mProgressLayout.showContent();
		Map<String, String> map = new HashMap<>();
		map.put("pageNum", pageNum + "");
		map.put("orderby", orderby);

		if (!TextUtils.isEmpty(shopId)) {
			map.put("shopId", shopId);
		}
		if (!TextUtils.isEmpty(name)) {
			map.put("name", name);
		} else if (TextUtils.isEmpty(type)) {
			map.put("classifyId", classifyId);
		} else {
			map.put("type", type);
		}
		if (!TextUtils.isEmpty(productIds)) {
			map.put("productIds", productIds);
		}
		LogUtils.i("productList 传输的值" + URLBuilder.format(map));
		OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/homePage/productList")
				.addParams("data", URLBuilder.format(map))
				.tag(this).build().execute(new Utils.MyResultCallback<GoodsListEntity>() {
			@Override
			public GoodsListEntity parseNetworkResponse(Response response) throws Exception {
				String json = response.body().string().trim();
				LogUtils.i("productList json的值。" + json);
				return new Gson().fromJson(json, GoodsListEntity.class);
			}

			@Override
			public void onResponse(GoodsListEntity response) {
				if (response != null && response.getCode().equals(response.HTTP_OK)) {
					if (response.getData() != null) {
						notifyActivity(response.getData());
						if (response.getData().getProductList() != null && response.getData().getProductList().size() != 0) {
							mList.clear();
							mList.addAll(response.getData().getProductList());
							mAdapter.notifyDataSetChanged();
							mProgressLayout.showContent();

						} else {
							mProgressLayout.showNone(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
								}
							});

						}

					} else {
						mProgressLayout.showNone(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
							}
						});
					}

				} else {

					LogUtils.i("返回错误了" + response.getMsg() + response.getCode());
					if (response.getCode().equals(response.HTTP_OK_)) {
						mProgressLayout.showNone(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
							}
						});
						return;
					}

					if ("1".equals(TAG)) {
						mProgressLayout.showSearch(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (mList != null && !mList.isEmpty()) {
									mList.clear();
									mAdapter.notifyDataSetChanged();
								}
								mRecyclerView.refresh();
							}
						});
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
				mRecyclerView.setPullRefreshEnabled(true);
				mRecyclerView.refreshComplete();
			}

			@Override
			public void onError(Call call, Exception e) {
				super.onError(call, e);
				LogUtils.i("网络故障了" + e);
				mRecyclerView.refreshComplete();
				mRecyclerView.setPullRefreshEnabled(true);
				if (call.isCanceled()) {
					call.cancel();
				} else {
					if (TAG != null) {
						if (TAG.equals("1")) {
							mProgressLayout.showSearch(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									if (mList != null && !mList.isEmpty()) {
										mList.clear();
										mAdapter.notifyDataSetChanged();
									}
									mRecyclerView.refresh();
								}
							});

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
				}
			}
		});
	}


	private void notifyActivity(GoodsListEntity.DataBean data) {

/*	"receipt": 1,
				"serviceStartime": 90,
				"shopId": 12,
				"shopImg": "/upload/shop/2018/08/06/985f3c16-0100-499a-ac44-655beddbbc47.png",
				"shopName": "优果加盟",
				"shopType": 2,
				"shopTypeName": "优果"*/


		Intent intent1 = new Intent();
		if (data != null) {
			if (data.getShopId() != null && !data.getShopId().equals("")) {
				if (data.getShopName() != null && !data.getShopName().equals("")) {
					intent1.setAction("CN.YJ.ROBUST.NOTIFYACTIVITY");
					intent1.putExtra("shopInfo", "yes");
//					intent1.putExtra("serviceStartime", data.getServiceStartime());
					intent1.putExtra("shopId", data.getShopId());
					intent1.putExtra("shopImg", data.getShopLogo());
					intent1.putExtra("shopName", data.getShopName());
					intent1.putExtra("shopNotice", data.getShopNotice());
//					intent1.putExtra("shopTypeName", data.getShopTypeName());
//					intent1.putExtra("receipt", data.getReceipt());
					intent1.putExtra("NumMonth", data.getNumMonth());
					intent1.putExtra("shopNumber", data.getShopNumber());
					getContext().sendBroadcast(intent1);
				} else {
					intent1.setAction("CN.YJ.ROBUST.NOTIFYACTIVITY");
					intent1.putExtra("shopInfo", "no");
					getContext().sendBroadcast(intent1);
				}
			} else {
				intent1.setAction("CN.YJ.ROBUST.NOTIFYACTIVITY");
				intent1.putExtra("shopInfo", "no");
				getContext().sendBroadcast(intent1);
			}
		}
	}



	private void doRequestData() {
		Map<String, String> map = new HashMap<>();
		map.put("pageNum", pageNum + "");
		map.put("orderby", orderby);

		if (!TextUtils.isEmpty(shopId)) {
			map.put("shopId", shopId);
		}


		if (!TextUtils.isEmpty(name)) {
			map.put("name", name);
		} else if (TextUtils.isEmpty(type)) {
			map.put("classifyId", classifyId);
		} else {
			map.put("type", type);
		}
		if (!TextUtils.isEmpty(productIds)) {
			map.put("productIds", productIds);
		}
		LogUtils.i("传输的值" + URLBuilder.format(map));
		OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/homePage/productList")
				.addParams("data", URLBuilder.format(map))
				.tag(this).build().execute(new Utils.MyResultCallback<GoodsListEntity>() {
			@Override
			public GoodsListEntity parseNetworkResponse(Response response) throws Exception {
				String json = response.body().string().trim();
				LogUtils.i(" productList json的值。" + json);
				return new Gson().fromJson(json, GoodsListEntity.class);
			}

			@Override
			public void onResponse(GoodsListEntity response) {
				if (response != null && response.getCode().equals(response.HTTP_OK)) {
					if (response.getData() != null) {
						if (response.getData().getProductList().size() != 0) {
							mList.addAll(response.getData().getProductList());
							mAdapter.notifyDataSetChanged();
							mRecyclerView.loadMoreComplete();
						} else if (response.getData().getProductList().size() == 0) {
							mRecyclerView.setNoMore(true);
							pageNum--;
						}
					}
					mProgressLayout.showContent();
				} else {
					ToastUtils.showToast(getContext(), "网络异常");
					pageNum--;
					mRecyclerView.loadMoreComplete();
				}
				mRecyclerView.setPullRefreshEnabled(true);
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
					ToastUtils.showToast(getContext(), "网络故障,请稍后再试");
					pageNum--;
				}
//                disMissDialog();
			}
		});
	}


	public void doRefresh() {
		if (mRecyclerView != null) {
			mRecyclerView.refresh();
		}
	}

	/* private void dismissDialog(){
		 if(mDialog != null){
			 mDialog.dismiss();
			 mDialog = null;
		 }
	 }*/
	@Override
	public void onDestroy() {
		super.onDestroy();
//        dismissDialog();
//        OkHttpUtils.getInstance().cancelTag(this);
	}


}
