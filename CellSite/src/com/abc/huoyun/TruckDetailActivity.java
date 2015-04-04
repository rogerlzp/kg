package com.abc.huoyun;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.huoyun.utility.CellSiteConstants;

public class TruckDetailActivity extends BaseActivity {

	String truckId;
	ImageView mDPiv, mTIiv;
	TextView mNtv;
	TextView mTTtv;
	TextView mTLtv;
	TextView mTPNtv; 
	
	
	Bitmap dpBitmap, tiBitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.truck_detail);
		Intent intent = getIntent();

		truckId = intent.getStringExtra(CellSiteConstants.TRUCK_ID);
		initView(intent);
		
	}
	
	public void initView(Intent intent) {
		
		 mDPiv = (ImageView)findViewById(R.id.driver_portrait_iv);
		 mTIiv = (ImageView)findViewById(R.id.truck_image_iv);
		 mNtv = (TextView)findViewById(R.id.name_tv);
		 mTTtv = (TextView)findViewById(R.id.truck_type_tv);
		 mTLtv = (TextView)findViewById(R.id.truck_length_tv);
		 mTPNtv = (TextView)findViewById(R.id.truck_plate_tv);
		 
		 mTTtv.setText(intent.getStringExtra(CellSiteConstants.TRUCK_TYPE));
		 mTLtv.setText(intent.getStringExtra(CellSiteConstants.TRUCK_LENGTH));
		 mTPNtv.setText(intent.getStringExtra(CellSiteConstants.TRUCK_LICENSE));
		 mTPNtv.setText(intent.getStringExtra(CellSiteConstants.TRUCK_LICENSE));
		 

	}
}
