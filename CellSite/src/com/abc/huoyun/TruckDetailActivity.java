package com.abc.huoyun;

import android.content.Intent;
import android.os.Bundle;

import com.abc.huoyun.utility.CellSiteConstants;

public class TruckDetailActivity extends BaseActivity {

	String truckId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.truck_detail);
		Intent intent = getIntent();

		truckId = intent.getStringExtra(CellSiteConstants.TRUCK_ID);

	}
}
