package com.abc.huoyun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.abc.huoyun.MainActivity.HorderDownLoadTask;
import com.abc.huoyun.MainActivity.HorderType;
import com.abc.huoyun.net.CellSiteHttpClient;
import com.abc.huoyun.utility.CellSiteConstants;
import com.abc.huoyun.utility.CityDBReader;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CheckReqDriverActivity extends BaseActivity {

	private static final String TAG = "CheckReqDriverActivity";
	// 货单
	PullToRefreshListView mDriverLv;
	HorderDriverType mHorderDriverType;// = new HorderDriverType(); //
										// 每个HorderId 对应一个 DriverType
	boolean isForceRefreshDriver = false;
	Boolean mHasExceptionDriver = false;
	int mLvHistoryPosDriver = 0;
	private int horderId = 0;

	ProgressDialog mProgressdialog;
	HorderDriverDownLoadTask mHorderDriverDownLoadTask;

	OnItemClickListener mDriverDetailListener = new OnItemClickListener() {
		// / @Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ArrayList<HashMap<String, Object>> aDrivers = mHorderDriverType.nDrivers;

			position--; // for the header view has taken the first position, so
						// all the data view must decrease 1.

			if (position == aDrivers.size()) {
				if (mHorderDriverType.hasShowAllDrivers) {
					// mHorderMoreTv.setText(R.string.hasShowAll);
				} else {
					// mLvHistoryPosHorder =
					// mHorderLv.getFirstVisiblePosition();
					mHorderDriverDownLoadTask = new HorderDriverDownLoadTask();
					mHorderDriverDownLoadTask
							.execute(CellSiteConstants.MORE_OPERATION);
				}
				return;
			}

			Intent intent = new Intent(CheckReqDriverActivity.this,
					CheckReqDriverDetailActivity.class);

			intent.putExtra(CellSiteConstants.ID,
					(String) aDrivers.get(position).get(CellSiteConstants.ID));

			startActivity(intent);

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_req);

		Intent intent = getIntent();
		horderId = Integer.valueOf(intent
				.getStringExtra(CellSiteConstants.HORDER_ID));
		Log.d(TAG, "horderId=" + horderId);

		mHorderDriverType = new HorderDriverType(horderId);
		initHorders();
		mHorderDriverDownLoadTask = new HorderDriverDownLoadTask();
		mHorderDriverDownLoadTask.execute(CellSiteConstants.NORMAL_OPERATION);
	}

	public void initHorders() {

		mDriverLv = (PullToRefreshListView) findViewById(R.id.driverLv);
		mDriverLv.setMode(Mode.BOTH);

		// mHorderLv.getRefreshableView().addFooterView(mHorderMore);
		mDriverLv.setOnItemClickListener(mDriverDetailListener);
		mDriverLv.setAdapter(mHorderDriverType.nHorderDriverAdapter);
		// Set a listener to be invoked when the list should be refreshed.
		mDriverLv.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isForceRefreshDriver = true;

				String label = DateUtils.formatDateTime(
						getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);// 加上时间

				// mHorderTypes[mCurrRadioIdx] = new HorderType(mCurrRadioIdx);

				mHorderDriverDownLoadTask = new HorderDriverDownLoadTask();
				mHorderDriverDownLoadTask
						.execute(CellSiteConstants.NORMAL_OPERATION);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isForceRefreshDriver = true;

				String label = DateUtils.formatDateTime(
						getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);// 加上时间

				// mHorderTypes[mCurrRadioIdx] = new HorderType(mCurrRadioIdx);

				mHorderDriverDownLoadTask = new HorderDriverDownLoadTask();
				mHorderDriverDownLoadTask
						.execute(CellSiteConstants.NORMAL_OPERATION);
			}

		});

	}

	public class HorderDriverType {
		int horderId;
		public ArrayList<HashMap<String, Object>> nDrivers;
		public HorderDriverAdapter nHorderDriverAdapter;

		int nDisplayNum;
		Boolean hasShowAllDrivers;

		public HorderDriverType(int _horderId) {
			nDrivers = new ArrayList<HashMap<String, Object>>();
			hasShowAllDrivers = false;
			horderId = _horderId;
			nHorderDriverAdapter = new HorderDriverAdapter(
					CheckReqDriverActivity.this);
		}

	}

	class HorderDriverAdapter extends BaseAdapter {
		public ArrayList<HashMap<String, Object>> nDrivers = new ArrayList<HashMap<String, Object>>();

		public HorderDriverAdapter(Context context) {
		}

		public void setDrivers(ArrayList<HashMap<String, Object>> drivers) {
			nDrivers = drivers;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = (ViewGroup) LayoutInflater.from(
						CheckReqDriverActivity.this).inflate(
						R.layout.driver_req_item, null);
				holder = new ViewHolder();
				holder.tv_truck_type = (TextView) convertView
						.findViewById(R.id.truck_type_tv);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.name_tv);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			HashMap<String, Object> driverData = nDrivers.get(position);

			holder.tv_name.setText((String) driverData
					.get(CellSiteConstants.ID));

			return convertView;
		}

		private class ViewHolder {
			ImageView organizerPortrait;
			TextView tv_name;
			TextView tv_truck_license;
			TextView tv_truck_type;
			TextView tv_truck_length;
			TextView tv_truck;
			TextView tv_request;
			ViewGroup progress;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return nDrivers.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	// 查看 horder

	class HorderDriverDownLoadTask extends AsyncTask<Integer, String, String> {
		static final String TAG_FAIL = "FAIL";
		static final String TAG_SUCC = "SUCCESS";

		List<HashMap<String, String>> nTmpNewsData;

		@Override
		protected String doInBackground(Integer... params) {
			int moreOperation = params[0];

			try {
				if (isForceRefreshDriver
						|| moreOperation == CellSiteConstants.MORE_OPERATION
						|| app.getHorderDriverTypeCache(horderId) == null // TODO:
																			// 增加类型
				) {

					getHorderDrivers(horderId);

					if (mHorderDriverType.nDrivers.size() < mHorderDriverType.nDisplayNum
							+ CellSiteConstants.PAGE_COUNT
							&& !mHasExceptionDriver) {
						mHorderDriverType.hasShowAllDrivers = true;
					}

				} else {
					mHorderDriverType = app.getHorderDriverTypeCache(horderId);

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
				if (mProgressdialog != null) {
					mProgressdialog.cancel();
				}

				if (isForceRefreshDriver) {
					isForceRefreshDriver = false;
					mDriverLv.onRefreshComplete();
				}
				mHorderDriverType.nHorderDriverAdapter
						.setDrivers(mHorderDriverType.nDrivers);
				mDriverLv.setAdapter(mHorderDriverType.nHorderDriverAdapter);
				mHorderDriverType.nHorderDriverAdapter.notifyDataSetChanged();
				// mHorderMore.setVisibility(View.VISIBLE);
				// mHorderLv.setEmptyView(mEmptyHorderView);

				if (mHorderDriverType.hasShowAllDrivers) {

					// mHorderMoreTv.setText(R.string.hasShowAll);
				} else {
					// mHorderMoreTv.setText(R.string.show_more);
				}

				mHorderDriverType.nDisplayNum = mHorderDriverType.nDrivers
						.size();

				app.setHorderDriverTypeCache(mHorderDriverType, horderId);

				if (mHorderDriverType.nDisplayNum > 0) { // TODO:  增加显示更多1
					// mHorderMoreTv.setVisibility(View.VISIBLE);
				} else {

					// mHorderMoreTv.setVisibility(View.INVISIBLE);
				}
				if (mLvHistoryPosDriver > 0) {
					// mHorderLv.setSelectionFromTop(mLvHistoryPosHorder, 0);
					// mLvHistoryPosHorder = 0;
				}

			}

		}
	}

	public JSONObject getHorderDrivers(Integer horderId) {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		postParameters.add(new BasicNameValuePair(CellSiteConstants.DRIVER_ID,
				"" + app.getUser().getId()));
		postParameters.add(new BasicNameValuePair(CellSiteConstants.HORDER_ID,
				"" + horderId));

		postParameters.add(new BasicNameValuePair("offset", String
				.valueOf(mHorderDriverType.nDisplayNum)));
		postParameters.add(new BasicNameValuePair("pagecount", String
				.valueOf(CellSiteConstants.PAGE_COUNT)));
		JSONObject response = null;
		try {
			response = CellSiteHttpClient.executeHttpPost(
					CellSiteConstants.GET_DRIVER_FROM_HORDER_URL,
					postParameters);
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
	 * "users":[{"id":"4","username":"18675561091","email":null,"mobile":
	 * "18675561091","photo":null,"is_admin":"0",
	 * "remember_token":null,"created_at"
	 * :"2015-04-06 14:40:31","updated_at":"2015-04-06 14:40:31",
	 * "device_token":
	 * "6bd170689b152361427ca2cb2fa5fec6fb729d18","access_token":"",
	 * "profile":null,"truck":{},"pivot":{"horder_id":"4","driver_id":"4"}}]}
	 * 
	 * @param jsonResult
	 */
	public void parseJson(JSONObject jsonResult) {
		HashMap<String, Object> mHorder;
		try {
			if (jsonResult.get("users") != JSONObject.NULL) {
				JSONArray results = jsonResult.getJSONArray("users");
				if (results.length() > 0
						&& results.length() < CellSiteConstants.PAGE_COUNT) {
					mHorderDriverType.hasShowAllDrivers = true;
				}

				for (int i = 0; i < results.length(); i++) {
					try {
						JSONObject resultObj = (JSONObject) results.get(i);
						mHorder = new HashMap<String, Object>();
						mHorder.put(CellSiteConstants.ID,
								resultObj.getString(CellSiteConstants.ID));

						mHorderDriverType.nDrivers.add(mHorder);
					} catch (Exception e) {
						mHasExceptionDriver = true;
						continue;
					}

				}

			}

		} catch (JSONException e) {

		}

	}
}
