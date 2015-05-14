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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.abc.huoyun.MyHorderFragment.HorderDownLoadTask;
import com.abc.huoyun.cache.TruckType;
import com.abc.huoyun.net.CellSiteHttpClient;
import com.abc.huoyun.utility.CellSiteApplication;
import com.abc.huoyun.utility.CellSiteConstants;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class TruckFragment extends Fragment {

	private static final String TAG = "TruckFragment";
	PullToRefreshListView mTruckLv;

	ViewGroup mTruckMore;
	TextView mTruckMoreTv;
	TruckType mTruckTypes = new TruckType(this.getActivity());
	boolean isForceRefreshTruck = false;
	Boolean mHasExceptionTruck = false;
	int mLvHistoryPosTruck = 0;

	TruckDownLoadTask mTruckDownLoadTask;
	ProgressDialog mProgressdialogTruck;

	private boolean isViewShown;
	private boolean isPrepared;

	CellSiteApplication app;

	public static TruckFragment newInstance() {
		TruckFragment mHCFragment = new TruckFragment();
		return mHCFragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		app = (CellSiteApplication) this.getActivity().getApplication();
		//initTrucks();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.main_tab_trucks, container, false);
		isPrepared = true;
		//lazyLoad();

		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if (this.getView() != null) {

			isViewShown = true;

			lazyLoad();

			// 相当于Fragment的onResume
		} else {
			isViewShown = false;
			// 相当于Fragment的onPause
		}
	}

	public void lazyLoad() {
		if (isViewShown && isPrepared) {
			Log.d(TAG, "lazyLoad");
			initTrucks();

		}
	}

	public void initTrucks() {

		mTruckTypes = new TruckType(this.getActivity());
		mTruckLv = (PullToRefreshListView) this.getView().findViewById(
				R.id.trucks_lv);

		mTruckMore = (ViewGroup) LayoutInflater.from(this.getActivity())
				.inflate(R.layout.more_list, null);
		mTruckMore.setVisibility(View.GONE);

		mTruckMoreTv = (TextView) mTruckMore.getChildAt(0);

		// mTruckLv.addFooterView(mTruckMore);
		TruckOnItemClickListener mTruckDetailListener = new TruckOnItemClickListener(
				this.getActivity());
		mTruckLv.setOnItemClickListener(mTruckDetailListener);
		mTruckLv.setAdapter(mTruckTypes.nTruckAdapter);
		// Set a listener to be invoked when the list should be refreshed.
		TruckRefreshListener mTruckRefreshListener = new TruckRefreshListener(
				this.getActivity());

		mTruckLv.setOnRefreshListener(mTruckRefreshListener);

		mTruckDownLoadTask = new TruckDownLoadTask();
		mTruckDownLoadTask.execute(CellSiteConstants.NORMAL_OPERATION);
		if (mProgressdialogTruck == null || !mProgressdialogTruck.isShowing()) {
			mProgressdialogTruck = new ProgressDialog(this.getActivity());
			mProgressdialogTruck.setMessage("正在加载数据");
			mProgressdialogTruck.setIndeterminate(true);
			mProgressdialogTruck.setCancelable(true);
			mProgressdialogTruck.show();
		}

	}

	class TruckRefreshListener implements OnRefreshListener2<ListView> {
		public Context ctx;

		public TruckRefreshListener(Context _ctx) {
			ctx = _ctx;
		}

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			isForceRefreshTruck = true;
			String label = DateUtils.formatDateTime(ctx,
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);// 加上时间
			mTruckDownLoadTask = new TruckDownLoadTask();
			mTruckDownLoadTask.execute(CellSiteConstants.MORE_OPERATION);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isForceRefreshTruck = true;
			String label = DateUtils.formatDateTime(ctx,
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);// 加上时间
			mTruckDownLoadTask = new TruckDownLoadTask();
			mTruckDownLoadTask.execute(CellSiteConstants.MORE_OPERATION);
		}
	}

	class TruckOnItemClickListener implements OnItemClickListener {
		private Context ctx;

		public TruckOnItemClickListener(Context _ctx) {
			this.ctx = _ctx;
		}

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ArrayList<HashMap<String, Object>> aTrucks = mTruckTypes.nTrucks;

			position--; // for the header view has taken the first position, so
						// all the data view must decrease 1.

			if (position == aTrucks.size()) {
				if (mTruckTypes.hasShowAllTrucks) {
					mTruckMoreTv.setText(R.string.hasShowAll);
				} else {
					// mLvHistoryPosTruck = mTruckLv.getFirstVisiblePosition();
					mTruckDownLoadTask = new TruckDownLoadTask();
					mTruckDownLoadTask
							.execute(CellSiteConstants.MORE_OPERATION);
				}
				return;
			}

			Intent intent = new Intent(ctx, TruckDetailActivity.class);

			intent.putExtra(
					CellSiteConstants.TRUCK_ID,
					(String) aTrucks.get(position).get(
							CellSiteConstants.TRUCK_ID));
			intent.putExtra(CellSiteConstants.TRUCK_LENGTH, (Integer) aTrucks
					.get(position).get(CellSiteConstants.TRUCK_LENGTH));
			intent.putExtra(CellSiteConstants.TRUCK_TYPE, (Integer) aTrucks
					.get(position).get(CellSiteConstants.TRUCK_TYPE));
			intent.putExtra(CellSiteConstants.TRUCK_IMAGE_URL, (String) aTrucks
					.get(position).get(CellSiteConstants.TRUCK_IMAGE_URL));
			intent.putExtra(
					CellSiteConstants.TRUCK_LICENSE_URL,
					(String) aTrucks.get(position).get(
							CellSiteConstants.TRUCK_LICENSE_URL));
			intent.putExtra(CellSiteConstants.TRUCK_PLATE, (String) aTrucks
					.get(position).get(CellSiteConstants.TRUCK_PLATE));
			intent.putExtra(
					CellSiteConstants.TRUCK_MOBILE_NUM,
					(String) aTrucks.get(position).get(
							CellSiteConstants.TRUCK_MOBILE_NUM));

			startActivity(intent);

		}

	}

	class TruckDownLoadTask extends AsyncTask<Integer, String, String> {
		static final String TAG_FAIL = "FAIL";
		static final String TAG_SUCC = "SUCCESS";

		List<HashMap<String, String>> nTmpNewsData;

		@Override
		protected String doInBackground(Integer... params) {
			int moreOperation = params[0];

			try {
				if (isForceRefreshTruck
						|| moreOperation == CellSiteConstants.MORE_OPERATION
						|| CellSiteApplication.getTrucksCache() == null) {
					getTrucks();
					if (mTruckTypes.nTrucks.size() < mTruckTypes.nDisplayNum
							+ CellSiteConstants.PAGE_COUNT
							&& !mHasExceptionTruck) {
						mTruckTypes.hasShowAllTrucks = true;
					}
				} else {
					mTruckTypes = CellSiteApplication.getTrucksCache();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return TAG_FAIL;
			}
			return TAG_SUCC;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (!this.isCancelled()) {
				if (mProgressdialogTruck != null) {
					mProgressdialogTruck.cancel();
				}

				if (isForceRefreshTruck) {
					isForceRefreshTruck = false;
					mTruckLv.onRefreshComplete();
				}
				mTruckTypes.nTruckAdapter.setTrucks(mTruckTypes.nTrucks);
				mTruckLv.setAdapter(mTruckTypes.nTruckAdapter);
				mTruckTypes.nTruckAdapter.notifyDataSetChanged();

				mTruckMore.setVisibility(View.VISIBLE);

				if (mTruckTypes.hasShowAllTrucks) {
					mTruckMoreTv.setText(R.string.hasShowAll);
				} else {
					mTruckMoreTv.setText(R.string.show_more);
				}

				mTruckTypes.nDisplayNum = mTruckTypes.nTrucks.size();

				CellSiteApplication.setTrucksCache(mTruckTypes);

				if (mTruckTypes.nDisplayNum > 0) {
					mTruckMoreTv.setVisibility(View.VISIBLE);
				} else {
					mTruckMoreTv.setVisibility(View.INVISIBLE);
				}
				if (mLvHistoryPosTruck > 0) {
					// mTruckLv.setSelectionFromTop(mLvHistoryPosTruck, 0);
					mLvHistoryPosTruck = 0;
				}

			}

		}
	}

	public JSONObject getTrucks() {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		postParameters.add(new BasicNameValuePair(CellSiteConstants.USER_ID, ""
				+ app.getUser().getId()));

		postParameters.add(new BasicNameValuePair("offset", String
				.valueOf(mTruckTypes.nDisplayNum)));
		postParameters.add(new BasicNameValuePair("pagecount", String
				.valueOf(CellSiteConstants.PAGE_COUNT)));
		JSONObject response = null;
		try {
			response = CellSiteHttpClient.executeHttpPost(
					CellSiteConstants.GET_TRUCKS_URL, postParameters);
			int resultCode = response.getInt(CellSiteConstants.RESULT_CODE);
			if (CellSiteConstants.RESULT_SUC == resultCode) {
				parseTruckJson(response);
			} else {
				Log.d(TAG, "QUERY RESULT FAILED");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * Trucks result
	 * trucks:[{"id":8,"user_id":9,"tstatus_id":0,"taudit_status_id":0,
	 * "tweight_id"
	 * :0,"tlength_id":11,"ttype_id":8,"tmobile_num":"","tlicense":"",
	 * "tl_image_url":"\/img\/users\/upload\/9_8_1425992634.jpg",
	 * "tphoto_image_url":null,"created_at":"2015-03-10 13:03:55",
	 * "updated_at":"2015-03-10 13:03:55"}]
	 * 
	 * @param jsonResult
	 */
	public void parseTruckJson(JSONObject jsonResult) {
		HashMap<String, Object> mTruck;
		try {
			if (jsonResult.get("trucks") != JSONObject.NULL) {
				JSONArray results = jsonResult.getJSONArray("trucks");
				if (results.length() < CellSiteConstants.PAGE_COUNT) {
					mTruckTypes.hasShowAllTrucks = true;
				}

				for (int i = 0; i < results.length(); i++) {
					try {
						JSONObject resultObj = (JSONObject) results.get(i);
						mTruck = new HashMap<String, Object>();
						mTruck.put(CellSiteConstants.TRUCK_ID,
								resultObj.getString(CellSiteConstants.ID));
						mTruck.put(
								CellSiteConstants.TRUCK_LENGTH,
								Integer.parseInt((resultObj)
										.getString(CellSiteConstants.TRUCK_LENGTH)));
						mTruck.put(
								CellSiteConstants.TRUCK_TYPE,
								Integer.parseInt((resultObj)
										.getString(CellSiteConstants.TRUCK_TYPE)));
						mTruck.put(
								CellSiteConstants.TRUCK_IMAGE_URL,
								(resultObj)
										.getString(CellSiteConstants.TRUCK_IMAGE_URL));
						mTruck.put(
								CellSiteConstants.TRUCK_LICENSE_URL,
								(resultObj)
										.getString(CellSiteConstants.TRUCK_LICENSE_URL));
						mTruck.put(CellSiteConstants.TRUCK_PLATE, (resultObj)
								.getString(CellSiteConstants.TRUCK_PLATE));
						mTruck.put(
								CellSiteConstants.TRUCK_MOBILE_NUM,
								(resultObj)
										.getString(CellSiteConstants.TRUCK_MOBILE_NUM));
						// down load image

						// get Truck
						JSONObject driverObj = (JSONObject) resultObj
								.get(CellSiteConstants.DRIVER);
						if (driverObj != null) {

						}
						try {
							JSONObject driverProfileObj = (JSONObject) resultObj
									.get(CellSiteConstants.PROFILE);
							if (driverProfileObj != null) {

								mTruck.put(CellSiteConstants.NAME, driverObj
										.getString(CellSiteConstants.NAME));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						mTruckTypes.nTrucks.add(mTruck);
					} catch (Exception e) {
						e.printStackTrace();
						mHasExceptionTruck = true;
						continue;
					}

				}

			}

		} catch (JSONException e) {
			Log.d(TAG, "JSONException" + e.toString());

		}

	}
}