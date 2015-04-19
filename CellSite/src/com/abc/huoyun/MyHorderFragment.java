package com.abc.huoyun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.abc.huoyun.cache.HorderType;
import com.abc.huoyun.net.CellSiteHttpClient;
import com.abc.huoyun.utility.CellSiteApplication;
import com.abc.huoyun.utility.CellSiteConstants;
import com.abc.huoyun.utility.CityDBReader;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MyHorderFragment extends Fragment {

	private final static String TAG = "MyHorderFragment";
	PullToRefreshListView mHorderLv;
	HorderType[] mHorderTypes = new HorderType[3];
	ViewGroup mHorderMore, mEmptyHorderView;
	TextView mHorderMoreTv;
	boolean isForceRefreshHorder = false;
	Boolean mHasExceptionHorder = false;
	int mLvHistoryPosHorder = 0;
	int mCurrRadioIdx = 0;

	LinearLayout waitLL, sentLL, historyLL;
	Button mCreateHorderBtn;

	ProgressDialog mProgressdialog;
	HorderDownLoadTask mHorderDownLoadTask;

	CellSiteApplication app;

	public static MyHorderFragment newInstance() {
		MyHorderFragment mHCFragment = new MyHorderFragment();
		return mHCFragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		app = (CellSiteApplication) this.getActivity().getApplication();

		mHorderTypes[0] = new HorderType(0, this.getActivity());
		mHorderTypes[1] = new HorderType(1, this.getActivity());
		mHorderTypes[2] = new HorderType(2, this.getActivity());

		initHorders();

		if (mProgressdialog == null || !mProgressdialog.isShowing()) {
			mProgressdialog = new ProgressDialog(this.getActivity());
			mProgressdialog.setMessage("正在加载数据");
			mProgressdialog.setIndeterminate(true);
			mProgressdialog.setCancelable(true);
			mProgressdialog.show();
		}

		initChooseHorders();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.main_tab_horder, container, false);

		return view;
	}

	// get horders
	public void initChooseHorders() {

		waitLL = (LinearLayout) this.getView().findViewById(R.id.waiting_ll);
		sentLL = (LinearLayout) this.getView().findViewById(R.id.sent_ll);
		historyLL = (LinearLayout) this.getView().findViewById(R.id.history_ll);

		ChooseStatusListener mChooseStatusListener = new ChooseStatusListener(
				this.getActivity());
		sentLL.setOnClickListener(mChooseStatusListener);
		waitLL.setOnClickListener(mChooseStatusListener);
		historyLL.setOnClickListener(mChooseStatusListener);

	}

	class ChooseStatusListener implements View.OnClickListener {
		public Context ctx;

		public ChooseStatusListener(Context _ctx) {
			this.ctx = _ctx;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.waiting_ll) {
				mCurrRadioIdx = 0;
			} else if (v.getId() == R.id.sent_ll) {
				mCurrRadioIdx = 1;
			} else if (v.getId() == R.id.history_ll) {
				mCurrRadioIdx = 2;
			}

			mHorderMore.setVisibility(View.INVISIBLE);
			mHorderMoreTv.setText(R.string.show_more);

			mHorderDownLoadTask = new HorderDownLoadTask();
			mHorderDownLoadTask.execute(CellSiteConstants.NORMAL_OPERATION);
		}
	}

	public void initHorders() {

		mHorderLv = (PullToRefreshListView) this.getView().findViewById(
				R.id.myPartyLv);
		mHorderLv.setMode(Mode.BOTH);

		mHorderMore = (ViewGroup) LayoutInflater.from(this.getActivity())
				.inflate(R.layout.more_list, null);
		mHorderMore.setVisibility(View.GONE);
		mEmptyHorderView = (ViewGroup) LayoutInflater.from(this.getActivity())
				.inflate(R.layout.empty_horder, null);

		mCreateHorderBtn = (Button) mEmptyHorderView
				.findViewById(R.id.create_horder_btn);

		mHorderMoreTv = (TextView) mHorderMore.getChildAt(0);
		HorderDetailListener mHorderDetailListener = new HorderDetailListener(
				this.getActivity());
		HorderRefreshListener mHorderRefreshListener = new HorderRefreshListener(
				this.getActivity());
		// mHorderLv.getRefreshableView().addFooterView(mHorderMore);
		mHorderLv.setOnItemClickListener(mHorderDetailListener);
		mHorderLv.setAdapter(mHorderTypes[mCurrRadioIdx].nHorderAdapter);
		// Set a listener to be invoked when the list should be refreshed.

		mHorderLv.setOnRefreshListener(mHorderRefreshListener);
		mHorderDownLoadTask = new HorderDownLoadTask();
		mHorderDownLoadTask.execute(CellSiteConstants.NORMAL_OPERATION);
		if (mProgressdialog == null || !mProgressdialog.isShowing()) {
			mProgressdialog = new ProgressDialog(this.getActivity());
			mProgressdialog.setMessage("正在加载数据");
			mProgressdialog.setIndeterminate(true);
			mProgressdialog.setCancelable(true);
			mProgressdialog.show();
		}

	}

	class HorderDetailListener implements OnItemClickListener {
		public Context ctx;

		public HorderDetailListener(Context _ctx) {
			ctx = _ctx;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			ArrayList<HashMap<String, Object>> aHorders = mHorderTypes[mCurrRadioIdx].nHorders;

			position--; // for the header view has taken the first position, so
						// all the data view must decrease 1.

			if (position == aHorders.size()) {
				if (mHorderTypes[mCurrRadioIdx].hasShowAllHorders) {
					mHorderMoreTv.setText(R.string.hasShowAll);
				} else {
					// mLvHistoryPosHorder =
					// mHorderLv.getFirstVisiblePosition();
					mHorderDownLoadTask = new HorderDownLoadTask();
					mHorderDownLoadTask
							.execute(CellSiteConstants.MORE_OPERATION);
				}
				return;
			}

			Intent intent = new Intent(ctx, HorderDetailActivity.class);
			intent.putExtra(
					CellSiteConstants.HORDER_ID,
					(String) aHorders.get(position).get(
							CellSiteConstants.HORDER_ID));
			intent.putExtra(
					CellSiteConstants.SHIPPER_USERNAME,
					(String) aHorders.get(position).get(
							CellSiteConstants.SHIPPER_USERNAME));
			intent.putExtra(
					CellSiteConstants.REPLIED_DRIVER_COUNT,
					(Integer) aHorders.get(position).get(
							CellSiteConstants.REPLIED_DRIVER_COUNT));
			intent.putExtra(
					CellSiteConstants.REPLIED_DRIVER_LIST,
					(ArrayList<String>) aHorders.get(position).get(
							CellSiteConstants.REPLIED_DRIVER_LIST));
			intent.putExtra(
					CellSiteConstants.SHIPPER_ADDRESS_NAME,
					(String) aHorders.get(position).get(
							CellSiteConstants.SHIPPER_ADDRESS_NAME));
			intent.putExtra(
					CellSiteConstants.CONSIGNEE_ADDRESS_NAME,
					(String) aHorders.get(position).get(
							CellSiteConstants.CONSIGNEE_ADDRESS_NAME));
			intent.putExtra(CellSiteConstants.CARGO_TYPE, (String) aHorders
					.get(position).get(CellSiteConstants.CARGO_TYPE));
			intent.putExtra(CellSiteConstants.CARGO_WEIGHT, (String) aHorders
					.get(position).get(CellSiteConstants.CARGO_WEIGHT));
			intent.putExtra(CellSiteConstants.CARGO_VOLUME, (String) aHorders
					.get(position).get(CellSiteConstants.CARGO_VOLUME));
			intent.putExtra(CellSiteConstants.TRUCK_TYPE, (String) aHorders
					.get(position).get(CellSiteConstants.TRUCK_TYPE));
			intent.putExtra(CellSiteConstants.HORDER_STATUS, (String) aHorders
					.get(position).get(CellSiteConstants.HORDER_STATUS));
			intent.putExtra(CellSiteConstants.SHIPPER_DATE, (String) aHorders
					.get(position).get(CellSiteConstants.SHIPPER_DATE));
			intent.putExtra(
					CellSiteConstants.SHIPPER_USERNAME,
					(String) aHorders.get(position).get(
							CellSiteConstants.SHIPPER_USERNAME));
			intent.putExtra(
					CellSiteConstants.HORDER_DESCRIPTION,
					(String) aHorders.get(position).get(
							CellSiteConstants.HORDER_DESCRIPTION));

			startActivity(intent);

		}
	}

	class HorderDownLoadTask extends AsyncTask<Integer, String, String> {
		static final String TAG_FAIL = "FAIL";
		static final String TAG_SUCC = "SUCCESS";

		List<HashMap<String, String>> nTmpNewsData;

		@Override
		protected String doInBackground(Integer... params) {
			int moreOperation = params[0];

			try {
				if (isForceRefreshHorder
						|| moreOperation == CellSiteConstants.MORE_OPERATION
						|| app.getHorderTypeCache(mCurrRadioIdx) == null) {
					Log.d(TAG,
							"Will connect the network and download the horders");
					getHorder(mCurrRadioIdx);

					if (mHorderTypes[mCurrRadioIdx].nHorders.size() < mHorderTypes[mCurrRadioIdx].nDisplayNum
							+ CellSiteConstants.PAGE_COUNT
							&& !mHasExceptionHorder) {
						mHorderTypes[mCurrRadioIdx].hasShowAllHorders = true;
					}
					Log.d(TAG, "after download the horder");
				} else {
					Log.d(TAG, "Will use the cache, current radio index "
							+ mCurrRadioIdx);
					;

					mHorderTypes[mCurrRadioIdx] = app
							.getHorderTypeCache(mCurrRadioIdx);
					Log.d(TAG, "++++++++++++++++Number of My horders :"
							+ mHorderTypes[mCurrRadioIdx].nHorders.size());
				}

			} catch (Exception e) {
				e.printStackTrace();
				return TAG_FAIL;
			}

			Log.d(TAG, "after download the horders: TAG_SUCC");
			return TAG_SUCC;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			Log.d(TAG, "HorderDownLoadTask onPostExecute()");
			if (!this.isCancelled()) {
				if (mProgressdialog != null) {
					Log.d(TAG, "Cancel the progress dialog");
					mProgressdialog.cancel();
				}

				if (isForceRefreshHorder) {
					isForceRefreshHorder = false;
					mHorderLv.onRefreshComplete();
				}
				mHorderTypes[mCurrRadioIdx].nHorderAdapter
						.setHorders(mHorderTypes[mCurrRadioIdx].nHorders);
				mHorderLv
						.setAdapter(mHorderTypes[mCurrRadioIdx].nHorderAdapter);
				mHorderTypes[mCurrRadioIdx].nHorderAdapter
						.notifyDataSetChanged();
				mHorderMore.setVisibility(View.VISIBLE);
				mHorderLv.setEmptyView(mEmptyHorderView);

				if (mHorderTypes[mCurrRadioIdx].hasShowAllHorders) {

					mHorderMoreTv.setText(R.string.hasShowAll);
				} else {
					mHorderMoreTv.setText(R.string.show_more);
				}

				mHorderTypes[mCurrRadioIdx].nDisplayNum = mHorderTypes[mCurrRadioIdx].nHorders
						.size();
				app.setHorderTypeCache(mHorderTypes[mCurrRadioIdx],
						mCurrRadioIdx);

				if (mHorderTypes[mCurrRadioIdx].nDisplayNum > 0) {
					Log.d(TAG, "set more tv to visible");

					mHorderMoreTv.setVisibility(View.VISIBLE);
				} else {
					Log.d(TAG, "set more tv to INVISIBLE");
					mHorderMoreTv.setVisibility(View.INVISIBLE);
				}
				if (mLvHistoryPosHorder > 0) {
					// mHorderLv.setSelectionFromTop(mLvHistoryPosHorder, 0);
					mLvHistoryPosHorder = 0;
				}

			}

		}
	}

	public JSONObject getHorder(Integer horder_status) {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		postParameters.add(new BasicNameValuePair(CellSiteConstants.USER_ID, ""
				+ app.getUser().getId()));
		postParameters.add(new BasicNameValuePair(
				CellSiteConstants.HORDER_STATUS, "" + horder_status));

		postParameters.add(new BasicNameValuePair("offset", String
				.valueOf(mHorderTypes[mCurrRadioIdx].nDisplayNum)));
		postParameters.add(new BasicNameValuePair("pagecount", String
				.valueOf(CellSiteConstants.PAGE_COUNT)));
		JSONObject response = null;
		try {
			response = CellSiteHttpClient.executeHttpPost(
					CellSiteConstants.GET_MY_HORDER_URL, postParameters);
			int resultCode = response.getInt(CellSiteConstants.RESULT_CODE);
			if (CellSiteConstants.RESULT_SUC == resultCode) {
				parseJson(response);
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * horder状态 {"result_code":"0","horders":[{"id":6,"shipper_username":
	 * "\u674e\u674e\u90bb\u5c45\u5929",
	 * "shipper_phone":"","shipper_date":"2015-03-15 00:00:00"
	 * ,"shipper_address_code":"110000-110100-110101",
	 * "consignee_username":"","consignee_phone"
	 * :"","consignee_address_code":"110000-110100-110101",
	 * "delivery_time":"0000-00-00 00:00:00"
	 * ,"truck_type":"","truck_length":"","cargo_type":"CT1",
	 * "cargo_volume":"\u6d4b\u8bd5",
	 * "cargo_weight":"\u6d4b\u8bd5\u554a","horder_desc"
	 * :"","user_id":9,"status":0, "created_at":"2015-03-15 03:30:50",
	 * "updated_at":"2015-03-15 03:30:50"},}]}
	 * 
	 * @param jsonResult
	 */
	public void parseJson(JSONObject jsonResult) {
		HashMap<String, Object> mHorder;
		try {
			if (jsonResult.get("horders") != JSONObject.NULL) {
				JSONArray results = jsonResult.getJSONArray("horders");
				if (results.length() > 0
						&& results.length() < CellSiteConstants.PAGE_COUNT) {
					mHorderTypes[mCurrRadioIdx].hasShowAllHorders = true;
				}

				for (int i = 0; i < results.length(); i++) {
					try {
						JSONObject resultObj = (JSONObject) results.get(i);
						mHorder = new HashMap<String, Object>();
						mHorder.put(
								CellSiteConstants.SHIPPER_USERNAME,
								resultObj
										.getString(CellSiteConstants.SHIPPER_USERNAME));
						mHorder.put(CellSiteConstants.HORDER_ID,
								(resultObj).getString(CellSiteConstants.ID));
						mHorder.put(CellSiteConstants.DRIVER_ID, (resultObj)
								.getString(CellSiteConstants.DRIVER_ID));

						CityDBReader dbReader = new CityDBReader(this
								.getActivity().getApplicationContext());
						mHorder.put(
								CellSiteConstants.SHIPPER_ADDRESS_NAME,
								dbReader.getNameFromCode((resultObj)
										.getString(CellSiteConstants.SHIPPER_ADDRESS_CODE_IN)));
						mHorder.put(
								CellSiteConstants.CONSIGNEE_ADDRESS_NAME,
								dbReader.getNameFromCode((resultObj)
										.getString(CellSiteConstants.CONSIGNEE_ADDRESS_CODE2)));
						mHorder.put(CellSiteConstants.CARGO_TYPE, (resultObj)
								.getString(CellSiteConstants.CARGO_TYPE));
						mHorder.put(CellSiteConstants.CARGO_WEIGHT, (resultObj)
								.getString(CellSiteConstants.CARGO_WEIGHT));
						mHorder.put(CellSiteConstants.CARGO_VOLUME, (resultObj)
								.getString(CellSiteConstants.CARGO_VOLUME));
						mHorder.put(CellSiteConstants.TRUCK_TYPE, (resultObj)
								.getString(CellSiteConstants.TRUCK_TYPE));
						mHorder.put(CellSiteConstants.HORDER_STATUS,
								(resultObj).getString(CellSiteConstants.STATUS));
						mHorder.put(CellSiteConstants.SHIPPER_DATE, (resultObj)
								.getString(CellSiteConstants.SHIPPER_DATE));
						mHorder.put(
								CellSiteConstants.HORDER_DESCRIPTION,
								(resultObj)
										.getString(CellSiteConstants.HORDER_DESCRIPTION));
						mHorder.put(
								CellSiteConstants.SHIPPER_USERNAME,
								(resultObj)
										.getString(CellSiteConstants.SHIPPER_USERNAME));

						JSONArray repliedDriversObj = null;
						try {

							repliedDriversObj = resultObj
									.getJSONArray(CellSiteConstants.REPLIED_DRIVERS);
							ArrayList<String> driverIdList = new ArrayList<String>();
							if (repliedDriversObj != null) {

								for (int j = 0; j < repliedDriversObj.length(); j++) {

									driverIdList
											.add(((JSONObject) repliedDriversObj
													.get(i))
													.getString(CellSiteConstants.DRIVER_ID));
								}
								mHorder.put(
										CellSiteConstants.REPLIED_DRIVER_LIST,
										driverIdList);
								mHorder.put(
										CellSiteConstants.REPLIED_DRIVER_COUNT,
										repliedDriversObj.length());
							}

						} catch (Exception e) {

						}

						// TODO :

						mHorderTypes[mCurrRadioIdx].nHorders.add(mHorder);
					} catch (Exception e) {
						mHasExceptionHorder = true;
						continue;
					}

				}

			}

		} catch (JSONException e) {
			Log.d(TAG, "JSONException" + e.toString());

		}

	}

	class HorderRefreshListener implements OnRefreshListener2<ListView> {
		public Context ctx;

		public HorderRefreshListener(Context _ctx) {
			ctx = _ctx;
		}

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			isForceRefreshHorder = true;

			String label = DateUtils.formatDateTime(ctx,
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);// 加上时间

			// mHorderTypes[mCurrRadioIdx] = new HorderType(mCurrRadioIdx);

			mHorderDownLoadTask = new HorderDownLoadTask();
			mHorderDownLoadTask.execute(CellSiteConstants.NORMAL_OPERATION);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isForceRefreshHorder = true;
			// mHorderTypes[mCurrRadioIdx] = new HorderType(mCurrRadioIdx);
			String label = DateUtils.formatDateTime(ctx,
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);// 加上时间

			mHorderDownLoadTask = new HorderDownLoadTask();
			mHorderDownLoadTask.execute(CellSiteConstants.NORMAL_OPERATION);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (app.getHorderTypeCache(mCurrRadioIdx) != null) {
			if (app.getHorderTypeCache(mCurrRadioIdx).nHorders.size() != mHorderTypes[mCurrRadioIdx].nHorders
					.size()) {
				mHorderTypes[mCurrRadioIdx].nHorders = app
						.getHorderTypeCache(mCurrRadioIdx).nHorders;
				mHorderTypes[mCurrRadioIdx].nHorderAdapter
						.notifyDataSetChanged();
			}
		}

	}

}