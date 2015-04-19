package com.abc.huoyun;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.huoyun.model.Horder;
import com.abc.huoyun.net.CellSiteHttpClient;
import com.abc.huoyun.utility.CellSiteConstants;

public class HorderDetailActivity extends BaseActivity {

	Horder mHorder;
	TextView mCTtv; // 货物类型
	TextView mCVtv; // 货物体积
	TextView mCWtv; // 货物重量
	TextView mTTtv; // 货车类型
	TextView mTLtv; // 货车长度
	TextView mSAtv; // 发货地址
	TextView mCAtv; // 送货地址
	TextView mHSDtv; // 发送时间
	TextView mHDtv; // 货源描述
	TextView mHItv; // 货源id

	RelativeLayout mCWrl;// 货物重量
	RelativeLayout mCVrl;// 货物体积

	ProgressDialog mProgressDialog;

	String horderId = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.horder_detail);
		mHorder = new Horder();

		Intent intent = getIntent();
		horderId = intent.getStringExtra(CellSiteConstants.HORDER_ID);

		initView(intent);
	}

	public void initView(Intent intent) {
		mCTtv = (TextView) findViewById(R.id.cargo_type_tv);
		mCWtv = (TextView) findViewById(R.id.cargo_weight_tv);
		mCVtv = (TextView) findViewById(R.id.cargo_volume_tv);
		mTTtv = (TextView) findViewById(R.id.truck_type_tv);
		mTLtv = (TextView) findViewById(R.id.truck_length_tv);
		mSAtv = (TextView) findViewById(R.id.shipper_address_tv);
		mCAtv = (TextView) findViewById(R.id.consignee_address_tv);
		mHSDtv = (TextView) findViewById(R.id.shipper_date_tv);
		mHDtv = (TextView) findViewById(R.id.horder_desc_tv);
		mHItv = (TextView) findViewById(R.id.horder_id_tv);
		mCWrl = (RelativeLayout) findViewById(R.id.cargo_weight_rl);
		mCVrl = (RelativeLayout) findViewById(R.id.cargo_volume_rl);
		mCTtv.setText(CellSiteConstants.CargoTypes[Integer.valueOf(intent
				.getStringExtra(CellSiteConstants.CARGO_TYPE)) - 1]);
		mTTtv.setText(CellSiteConstants.TruckTypes[Integer.valueOf(intent
				.getStringExtra(CellSiteConstants.TRUCK_TYPE)) - 1]);
		if (Integer.valueOf(intent
				.getStringExtra(CellSiteConstants.CARGO_VOLUME)) == 0) {
			mCVrl.setVisibility(View.GONE);
		} else {
			mCVtv.setText(intent.getStringExtra(CellSiteConstants.CARGO_VOLUME)
					+ "方");
		}

		if (Integer.valueOf(intent
				.getStringExtra(CellSiteConstants.CARGO_WEIGHT)) == 0) {
			mCWrl.setVisibility(View.GONE);
		} else {
			mCWtv.setText(intent.getStringExtra(CellSiteConstants.CARGO_WEIGHT)
					+ "吨");
		}
		mSAtv.setText(intent
				.getStringExtra(CellSiteConstants.SHIPPER_ADDRESS_NAME));
		mCAtv.setText(intent
				.getStringExtra(CellSiteConstants.CONSIGNEE_ADDRESS_NAME));
		mHSDtv.setText(intent.getStringExtra(CellSiteConstants.SHIPPER_DATE));
		mHDtv.setText(intent
				.getStringExtra(CellSiteConstants.HORDER_DESCRIPTION));
		mTTtv.setText(CellSiteConstants.TruckTypes[Integer.valueOf(intent
				.getStringExtra(CellSiteConstants.TRUCK_TYPE)) - 1]);
		mHItv.setText(intent.getStringExtra(CellSiteConstants.HORDER_ID));

	}

	/**
	 * 删除操作 1.向后台请求删除 2. 删除成功后，返回上级页面 3. 更新horderlist， 并刷新页面
	 * 
	 * @param v
	 */
	public void delHorder(View v) {
		DeleteHorderTask mDeleteHorderTask = new DeleteHorderTask();
		mDeleteHorderTask.execute(horderId);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(res.getString(R.string.connecting));
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();

	}

	private class DeleteHorderTask extends AsyncTask<String, String, Integer> {
		@Override
		public Integer doInBackground(String... params) {
			return deleteHorder(params[0]);
		}

		@Override
		public void onPostExecute(Integer result) {
			if (mProgressDialog != null) {
				mProgressDialog.cancel();
			}
			if (this.isCancelled()) {
				return;
			}
			if (result == CellSiteConstants.RESULT_SUC) {

				for (HashMap<String, Object> horderData : app
						.getHorderTypeCache(0).nHorderAdapter.nHorders) {
					if (((String) horderData.get(CellSiteConstants.HORDER_ID))
							.equals(horderId)) {
						app.getHorderTypeCache(0).nHorderAdapter.nHorders
								.remove(horderData);
						finish();
					}

				}
			}
		}

		protected Integer deleteHorder(String _horderId) {

			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.USER_ID, "" + app.getUser().getId()));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.HORDER_ID, _horderId));

			JSONObject response = null;
			try {
				response = CellSiteHttpClient.executeHttpPost(
						CellSiteConstants.DELETE_HORDER_URL, postParameters);

				int resultCode = Integer.parseInt(response.get(
						CellSiteConstants.RESULT_CODE).toString());
				return resultCode;

			} catch (UnknownHostException e) {
				return CellSiteConstants.UNKNOWN_HOST_ERROR;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return CellSiteConstants.UNKNOWN_ERROR;
		}
	}

}