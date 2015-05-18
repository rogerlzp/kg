package com.abc.huoyun;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.huoyun.utility.CellSiteConstants;
import com.abc.huoyun.utility.image.ImageLoad;
import com.abc.huoyun.utility.image.ImageLoad.ImageDownloadedCallBack;

public class TruckDetailActivity extends BaseActivity {

	String truckId;
	ImageView mDriverProfileIv, mTruckImageIv;
	TextView mNtv;
	TextView mTTtv;
	TextView mTLtv;
	TextView mTPNtv;
	String phoneNum;
	int selectedDriverId, newSelectedDriverId;
	int currentDriverId;

	Bitmap dpBitmap, tiBitmap;

	String portraitImageUrl, truckImageUrl;
	ImageLoad mImageLoad;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.truck_detail);
		Intent intent = getIntent();
		mImageLoad = new ImageLoad(this);
		phoneNum = intent.getStringExtra(CellSiteConstants.MOBILE);

		currentDriverId = intent.getIntExtra(CellSiteConstants.DRIVER_ID, 1);
		portraitImageUrl = intent
				.getStringExtra(CellSiteConstants.PROFILE_IMAGE_URL);
		truckImageUrl = intent
				.getStringExtra(CellSiteConstants.TRUCK_IMAGE_URL);
		truckId = intent.getStringExtra(CellSiteConstants.TRUCK_ID);
		initView(intent);

	}

	public void initView(Intent intent) {

		mDriverProfileIv = (ImageView) findViewById(R.id.driver_portrait_iv);
		mTruckImageIv = (ImageView) findViewById(R.id.truck_image_iv);
		mNtv = (TextView) findViewById(R.id.name_tv);
		mTTtv = (TextView) findViewById(R.id.truck_type_tv);
		mTLtv = (TextView) findViewById(R.id.truck_length_tv);
		mTPNtv = (TextView) findViewById(R.id.truck_plate_tv);

		mTTtv.setText(CellSiteConstants.TruckTypes[intent.getIntExtra(
				CellSiteConstants.TRUCK_TYPE, 1)]);
		mTLtv.setText(CellSiteConstants.TruckLengths[intent.getIntExtra(
				CellSiteConstants.TRUCK_LENGTH, 1)]);
		mTPNtv.setText(intent.getStringExtra(CellSiteConstants.TRUCK_PLATE));

		if (portraitImageUrl != null && !portraitImageUrl.equals("")) {
			showImage(mDriverProfileIv, portraitImageUrl);
		}
		if (truckImageUrl != null && !truckImageUrl.equals("")) {
			showImage(mTruckImageIv, truckImageUrl);
		}

	}

	private void showImage(ImageView imageView, String avatar) {
		// imageView.setTag(avatar);
		Bitmap bitmap = mImageLoad.loadImage(imageView, avatar,
				new ImageDownloadedCallBack() {
					@Override
					public void onImageDownloaded(ImageView imageView,
							Bitmap bitmap) {
						imageView.setImageBitmap(bitmap);
					}
				});
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		}
	}

	// contact driver
	public void contactDriver(View v) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ phoneNum));
		startActivity(intent);

	}
}
