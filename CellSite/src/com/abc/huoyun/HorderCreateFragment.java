package com.abc.huoyun;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.abc.huoyun.CargoWeightVolumeDialog.CargoWeightVolumeInputListener;
import com.abc.huoyun.CityDialog.InputListener;
import com.abc.huoyun.net.CellSiteHttpClient;
import com.abc.huoyun.utility.CellSiteApplication;
import com.abc.huoyun.utility.CellSiteConstants;

public class HorderCreateFragment extends Fragment {

	public static final String TAG = HorderCreateFragment.class.getSimpleName();
	private TextView mSAtv;
	private TextView mCAtv;
	private String mShipperAddressCode;
	private String mConsigneeAddressCode;
	private String mShipperDate;
	private String mCargoType;
	private String mCargoWeight;
	private String mCargoVolume;
	private String mTruckType;
	private String mShipperUsername;
	private String mHorderDesc;
	private String mTruckLength;
	ArrayList<HashMap<String, Object>> mCargoTypeList = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> mTruckLengthList = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> mTruckWeightList = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> mTruckTypeList = new ArrayList<HashMap<String, Object>>();

	private TextView mSDtv;
	private TextView mCTtv;
	private TextView mCWVtv; // 货物重量和体积
	private TextView mTTtv;

	private TextView mTLtv;

	CityDialog mCityDialog = null;
	InputListener listener1;
	CityChooseListener cityChooseListener1;

	InputListener listener2;
	CityChooseListener cityChooseListener2;

	private Button mCreateBtn;

	public static HorderCreateFragment newInstance() {
		HorderCreateFragment mHCFragment = new HorderCreateFragment();
		return mHCFragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		initChooseAddress();
		initChooseCargoWeightVolume();
		initChooseDate();
		initChooseCargoType();
		initChooseTruckType();
		initChooseTruckLength();
		initCreateHorder();

	}

	public void gotoMyhorder() {
		((MainFragmentActivity) this.getActivity()).gotoMyHorder();
	}

	public void initChooseAddress() {
		mSAtv = (TextView) this.getView().findViewById(R.id.shipper_address_tv);
		mCAtv = (TextView) this.getView().findViewById(
				R.id.consignee_address_tv);

		listener1 = new InputListener() {
			@Override
			public void getText(String str, String str2) {
				// TODO Auto-generated method stub
				mSAtv.setText(str);
				mShipperAddressCode = str2;
			}
		};
		cityChooseListener1 = new CityChooseListener(this.getActivity(),
				listener1);
		mSAtv.setOnClickListener(cityChooseListener1);

		listener2 = new InputListener() {
			@Override
			public void getText(String str, String str2) {
				// TODO Auto-generated method stub
				mCAtv.setText(str);
				mConsigneeAddressCode = str2;
			}
		};
		cityChooseListener2 = new CityChooseListener(this.getActivity(),
				listener2);
		mCAtv.setOnClickListener(cityChooseListener2);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_tab_horder_create,
				container, false);

		return view;
	}

	public void initCreateHorder() {

		mCreateBtn = (Button) this.getView().findViewById(
				R.id.create_horder_btn);
		mShipperUsername = ((EditText) this.getView().findViewById(
				R.id.shipper_username_et)).getText().toString();
		mHorderDesc = ((EditText) this.getView().findViewById(
				R.id.horder_description_et)).getText().toString();
		mCreateBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				CreateHorderTask mCreateHorderTask = new CreateHorderTask();

				mCreateHorderTask.execute(mShipperAddressCode, mShipperDate,
						mConsigneeAddressCode, mShipperUsername, mCargoType,
						mCargoWeight, mCargoVolume, mTruckType, mTruckLength,
						mHorderDesc,
						"" + CellSiteApplication.getUser().getId(),
						CellSiteApplication.getUser().getMobileNum());
			}
		});

	}

	private class CreateHorderTask extends AsyncTask<String, String, Integer> {
		@Override
		public Integer doInBackground(String... params) {
			return createHorderTask(params[0], params[1], params[2], params[3],
					params[4], params[5], params[6], params[7], params[8],
					params[9], params[10], params[11]);
		}

		@Override
		public void onPostExecute(Integer result) {
			if (this.isCancelled()) {
				return;
			}
			Integer resCode = result;// Integer.parseInt(result);
			if (resCode == CellSiteConstants.RESULT_SUC) {
				// TODO: set for different user

				gotoMyhorder();

			}
		}

		protected Integer createHorderTask(String _shipperAddressCode,
				String _shipperDate, String _consigneeAddressCode,
				String _shipperUsername, String _cargoType,
				String _cargoWeight, String _cargoVolume, String _truckType,
				String _truckLength, String _orderDesc, String _userId,
				String _shipperPhone) {

			Log.d(TAG, "_shipperDate: " + _shipperDate + "\n_cargoType: "
					+ _cargoType + "\n_cargoWeight: " + _cargoWeight
					+ "\n_cargoVolume" + _cargoVolume + "\n_truckType:"
					+ _truckType + "_truckLength: " + _truckLength);
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

			postParameters
					.add(new BasicNameValuePair(
							CellSiteConstants.SHIPPER_ADDRESS_CODE,
							_shipperAddressCode));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.SHIPPER_DATE, _shipperDate));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.CONSIGNEE_ADDRESS_CODE,
					_consigneeAddressCode));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.SHIPPER_USERNAME, _shipperUsername));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.SHIPPER_PHONE, _shipperPhone));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.CARGO_TYPE, _cargoType));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.CARGO_WEIGHT, _cargoWeight));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.CARGO_VOLUME, _cargoVolume));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.TRUCK_TYPE, _truckType));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.TRUCK_LENGTH, _truckLength));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.HORDER_DESCRIPTION, _orderDesc));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.USER_ID, _userId));

			JSONObject response = null;
			try {
				response = CellSiteHttpClient.executeHttpPost(
						CellSiteConstants.CREATE_HORDER_URL, postParameters);

				int resultCode = Integer.parseInt(response.get(
						CellSiteConstants.RESULT_CODE).toString());
				if (resultCode == CellSiteConstants.RESULT_SUC) {
					// TODO
				}
				return resultCode;

			} catch (UnknownHostException e) {
				return CellSiteConstants.UNKNOWN_HOST_ERROR;
			} catch (Exception e) {
				// TODO
			}
			return CellSiteConstants.UNKNOWN_ERROR;
		}
	}

	/**
	 * 弹出对话框，输入重量和体积
	 * 
	 * @param v
	 */
	public void initChooseCargoWeightVolume() {
		mCWVtv = ((TextView) this.getView().findViewById(R.id.cargo_weight_tv));

		CargoWeightVolumeInputListener listener = new CargoWeightVolumeInputListener() {

			@Override
			public void getText(String str, String str2) {
				// TODO Auto-generated method stub
				String finalStr = "";
				if (!str.equals("0")) {
					finalStr = "  " + str + "吨  ";
				}
				if (!str2.equals("0")) {
					finalStr += str2 + "立方";
				}

				mCWVtv.setText(finalStr);
				mCargoWeight = str;
				mCargoVolume = str2;
			}
		};

		CargoWeightVolumeChooseListener chooseListener = new CargoWeightVolumeChooseListener(
				this.getActivity(), listener);

		mCWVtv.setOnClickListener(chooseListener);

	}

	class CargoWeightVolumeChooseListener implements View.OnClickListener {

		public Context ctx;
		private CargoWeightVolumeInputListener listener;

		public CargoWeightVolumeChooseListener(Context _ctx,
				CargoWeightVolumeInputListener _listener) {
			this.ctx = _ctx;
			this.listener = _listener;
		}

		@Override
		public void onClick(View v) {
			CargoWeightVolumeDialog mCWVD = new CargoWeightVolumeDialog(ctx,
					listener);
			mCWVD.setTitle(R.string.cargo_weight_vloume);
			mCWVD.show();
		}
	}

	class CityChooseListener implements View.OnClickListener {

		public Context ctx;
		private InputListener listener;

		public CityChooseListener(Context _ctx, InputListener _listener) {
			this.ctx = _ctx;
			this.listener = _listener;
		}

		@Override
		public void onClick(View v) {
			mCityDialog = new CityDialog(ctx, listener);
			mCityDialog.setTitle(R.string.choose_address);
			mCityDialog.show();
		}
	}

	public void initChooseDate() {
		mSDtv = (TextView) this.getView().findViewById(R.id.shipper_date_tv);
		DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				String tmpMonthOfYear, tmpDayofMonth;
				if (monthOfYear < 9) {
					tmpMonthOfYear = "0" + (monthOfYear + 1);
				} else {
					tmpMonthOfYear = "" + (monthOfYear + 1);
				}

				if (dayOfMonth < 9) {
					tmpDayofMonth = "0" + dayOfMonth;
				} else {
					tmpDayofMonth = "" + dayOfMonth;
				}

				mShipperDate = year + "-" + tmpMonthOfYear + "-"
						+ tmpDayofMonth;
				mSDtv.setText(mShipperDate);
			}
		};
		DateChooseListener mDateChooseListener = new DateChooseListener(
				this.getActivity(), dateListener);
		mSDtv.setOnClickListener(mDateChooseListener);

	}

	class DateChooseListener implements View.OnClickListener {

		public Context ctx;
		private DatePickerDialog.OnDateSetListener dateListener;
		Calendar calendar = Calendar.getInstance();

		public DateChooseListener(Context _ctx,
				DatePickerDialog.OnDateSetListener _dateListener) {
			this.ctx = _ctx;
			this.dateListener = _dateListener;
		}

		@Override
		public void onClick(View v) {
			DatePickerDialog dialog = new DatePickerDialog(ctx, dateListener,
					calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			dialog.show();
		}
	}

	/**
	 * 选择货物种类
	 */
	public void initChooseCargoType() {
		mCTtv = (TextView) this.getView().findViewById(R.id.cargo_type_tv);
		CargoChooseListener mCargoChooseListener = new CargoChooseListener(
				this.getActivity());
		mCTtv.setOnClickListener(mCargoChooseListener);
	}

	class CargoChooseListener implements View.OnClickListener {

		public Context ctx;

		public CargoChooseListener(Context _ctx) {
			this.ctx = _ctx;
		}

		@Override
		public void onClick(View v) {

			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

			GridView gridView1 = new GridView(ctx);
			gridView1.setNumColumns(3);
			// (GridView)findViewById(R.id.gridView1);
			SimpleAdapter adapter = new SimpleAdapter(ctx, mCargoTypeList,
					R.layout.cargo_type_griditem, new String[] { "TITLE",
							"CARGOTYPE" }, new int[] { R.id.griditem_title,
							R.id.griditem_type, });

			gridView1.setAdapter(adapter);
			builder.setTitle("Please Choose");
			builder.setInverseBackgroundForced(true);
			builder.setView(gridView1);
			final Dialog dialog = builder.create();

			gridView1.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long rowId) {
					// doing something in here and then close
					mCTtv.setText(((TextView) view
							.findViewById(R.id.griditem_title)).getText());
					mCargoType = ((TextView) view
							.findViewById(R.id.griditem_type)).getText()
							.toString();
					dialog.dismiss();
				}
			});
			dialog.show();

		}
	}

	/**
	 * 选择货车种类
	 * 
	 */

	public void initChooseTruckType() {
		mTTtv = (TextView) this.getView().findViewById(R.id.truck_type_tv);

		ChooseTruckTypeListener mChooseTruckTypeListener = new ChooseTruckTypeListener(
				this.getActivity());
		mTTtv.setOnClickListener(mChooseTruckTypeListener);
	}

	class ChooseTruckTypeListener implements View.OnClickListener {
		public Context ctx;

		public ChooseTruckTypeListener(Context _ctx) {
			this.ctx = _ctx;
		}

		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

			GridView gridView1 = new GridView(ctx);
			gridView1.setNumColumns(3);
			// (GridView)findViewById(R.id.gridView1);
			SimpleAdapter adapter = new SimpleAdapter(ctx, mTruckTypeList,
					R.layout.truck_type_griditem, new String[] { "PIC",
							"TITLE", "TTYPE" }, new int[] { R.id.griditem_pic,
							R.id.griditem_title, R.id.griditem_type, });

			gridView1.setAdapter(adapter);
			builder.setTitle("Please Choose");
			builder.setInverseBackgroundForced(true);
			builder.setView(gridView1);
			final Dialog dialog = builder.create();

			gridView1.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long rowId) {
					mTTtv.setText(((TextView) view
							.findViewById(R.id.griditem_title)).getText());
					mTruckType = ((TextView) view
							.findViewById(R.id.griditem_type)).getText()
							.toString();
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	}

	/**
	 *  选择货车长度
	 */

	public void initChooseTruckLength() {
		mTLtv = (TextView) this.getView().findViewById(R.id.truck_length_tv);
		ChooseTruckLengthListener mChooseTruckLengthListener = new ChooseTruckLengthListener(
				this.getActivity());
		mTLtv.setOnClickListener(mChooseTruckLengthListener);
	}

	class ChooseTruckLengthListener implements View.OnClickListener {
		public Context ctx;

		public ChooseTruckLengthListener(Context _ctx) {
			this.ctx = _ctx;
		}

		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

			GridView gridView1 = new GridView(ctx);
			gridView1.setNumColumns(3);
			// (GridView)findViewById(R.id.gridView1);
			SimpleAdapter adapter = new SimpleAdapter(ctx, mTruckLengthList,
					R.layout.truck_length_griditem, new String[] { "TITLE",
							"TLENGTH" }, new int[] { R.id.griditem_title,
							R.id.griditem_length, });

			gridView1.setAdapter(adapter);
			builder.setTitle("Please Choose");
			builder.setInverseBackgroundForced(true);
			builder.setView(gridView1);
			final Dialog dialog = builder.create();

			gridView1.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long rowId) {
					mTLtv.setText(((TextView) view
							.findViewById(R.id.griditem_title)).getText());
					mTruckLength = ((TextView) view
							.findViewById(R.id.griditem_length)).getText()
							.toString();
					dialog.dismiss();
				}
			});
			// */

			dialog.show();

		}
	}

	public void initData() {

		for (int i = 0; i < 14; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("PIC", R.drawable.ic_launcher);
			map.put("TITLE", CellSiteConstants.TruckTypes[i]);
			map.put("TTYPE", i + 1);
			mTruckTypeList.add(map);
		}

		for (int i = 0; i < 17; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("TITLE", CellSiteConstants.TruckLengths[i]);
			map.put("TLENGTH", i + 1);
			mTruckLengthList.add(map);
		}

		for (int i = 0; i < 15; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("TITLE", CellSiteConstants.CargoTypes[i]);
			map.put("CARGOTYPE", i + 1);
			mCargoTypeList.add(map);
		}

	}

}
