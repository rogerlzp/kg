package com.abc.huoyun.cache;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.huoyun.CheckReqDriverActivity;
import com.abc.huoyun.R;
import com.abc.huoyun.utility.CellSiteConstants;

public class HorderAdapter extends BaseAdapter {
	public ArrayList<HashMap<String, Object>> nHorders = new ArrayList<HashMap<String, Object>>();
	HorderTextListener htListener = null;
	public Context ctx;

	public HorderAdapter(Context context) {
		ctx = context;
	}

	public void setHorders(ArrayList<HashMap<String, Object>> horders) {
		nHorders = horders;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = (ViewGroup) LayoutInflater.from(ctx).inflate(
					R.layout.horder_item, null);
			holder = new ViewHolder();
			holder.tv_horder_id = (TextView) convertView
					.findViewById(R.id.horder_id_tv);
			holder.tv_truck = (TextView) convertView
					.findViewById(R.id.truck_tv);
			holder.tv_cargo = (TextView) convertView
					.findViewById(R.id.cargo_tv);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.shipper_time_tv);
			holder.tv_location = (TextView) convertView
					.findViewById(R.id.location_tv);
			holder.tv_request = (TextView) convertView
					.findViewById(R.id.req_horder_tv);

			htListener = new HorderTextListener();
			holder.tv_request.setOnClickListener(htListener);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HashMap<String, Object> horderData = nHorders.get(position);

		holder.tv_horder_id.setText((String) horderData
				.get(CellSiteConstants.HORDER_ID));

		holder.tv_location.setText((String) horderData
				.get(CellSiteConstants.SHIPPER_ADDRESS_NAME)
				+ "~"
				+ (String) horderData
						.get(CellSiteConstants.CONSIGNEE_ADDRESS_NAME));

		holder.tv_truck
				.setText(CellSiteConstants.TruckTypes[Integer
						.valueOf((String) horderData
								.get(CellSiteConstants.TRUCK_TYPE)) - 1]);

		int cargoVolumeValue = Integer.valueOf((String) horderData
				.get(CellSiteConstants.CARGO_VOLUME));
		int cargoWeightValue = Integer.valueOf((String) horderData
				.get(CellSiteConstants.CARGO_WEIGHT));
		String cargoVolume = cargoVolumeValue != 0 ? cargoVolumeValue + "方"
				: "";
		String cargoWeight = cargoWeightValue != 0 ? cargoWeightValue + "吨"
				: "";
		holder.tv_cargo
				.setText((CellSiteConstants.CargoTypes[Integer
						.valueOf((String) horderData
								.get(CellSiteConstants.CARGO_TYPE)) - 1])
						+ cargoWeight + cargoVolume);

		holder.tv_time.setText((String) horderData
				.get(CellSiteConstants.SHIPPER_DATE));

		holder.tv_request.setText(ctx.getResources().getString(
				R.string.request_driver)
				+ (Integer) horderData
						.get(CellSiteConstants.REPLIED_DRIVER_COUNT));
		htListener.setHorderId((String) horderData
				.get(CellSiteConstants.HORDER_ID));
		htListener.setSelectedDriverId((String) horderData
				.get(CellSiteConstants.DRIVER_ID));

		/*
		 * holder.tv_request.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Intent intent = new Intent(MainActivity.this,
		 * CheckReqDriverActivity.class);
		 * Toast.makeText(getApplicationContext(), "have a test in number",
		 * Toast.LENGTH_SHORT).show();
		 * intent.putExtra(CellSiteConstants.HORDER_ID, tmpHorderId);
		 * intent.putExtra(CellSiteConstants.REPLIED_DRIVER_LIST, tmpArrayList);
		 * startActivity(intent);
		 * 
		 * } });
		 */

		return convertView;
	}

	private class ViewHolder {
		ImageView organizerPortrait;
		TextView tv_location;
		TextView tv_horder_id;
		TextView tv_distance;
		TextView tv_time;
		TextView tv_cargo;
		TextView tv_truck;
		TextView tv_request;
		ViewGroup progress;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return nHorders.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	class HorderTextListener implements View.OnClickListener {
		String horderId;
		String selectedDriverId;

		public void setHorderId(String _horderId) {
			this.horderId = _horderId;
		}

		public void setSelectedDriverId(String _driverId) {
			this.selectedDriverId = _driverId;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ctx, CheckReqDriverActivity.class);
			intent.putExtra(CellSiteConstants.HORDER_ID, this.horderId);
			intent.putExtra(CellSiteConstants.DRIVER_ID, this.selectedDriverId);
			ctx.startActivity(intent);
		}
	}

}
