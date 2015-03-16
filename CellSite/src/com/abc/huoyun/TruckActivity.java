package com.abc.huoyun;

import java.io.File;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.huoyun.net.CellSiteHttpClient;
import com.abc.huoyun.utility.CellSiteConstants;
import com.abc.huoyun.utility.CropOption;
import com.abc.huoyun.utility.CropOptionAdapter;
import com.abc.huoyun.utility.Utils;

public class TruckActivity extends BaseActivity {

	private static final String TAG = "TruckActivity";

	Bitmap protraitBmp;

	ArrayList<HashMap<String, Object>> mTruckLengthList = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> mTruckTypeList = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> mTruckWeightList = new ArrayList<HashMap<String, Object>>();
	private TextView mTTtv;
	private TextView mTLtv;
	private TextView mTWtv;
	private ImageView mTLPiv; // ??????

	public String mTruckType;
	public String mTruckLength;
	public String mTruckWeight;
	public String mMobileNum;
	public String mTruckStatus;
	private Uri imageUri;

	final int IMAGE_WIDTH = 180;
	final int IMAGE_HEIGHT = 180;

	private UpdateTruckTask mUpdateTruckTask;
	private UpdateImageTask mUpdateImageTask;
	private DownloadImageTask mDownloadImageTask;
	Boolean isPortraitChanged = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.truck);

		initView();
		initData();

	}

	public void initView() {
		mTTtv = (TextView) this.findViewById(R.id.truck_type_tv);
		mTLtv = (TextView) this.findViewById(R.id.truck_length_tv);
		mTWtv = (TextView) this.findViewById(R.id.truck_weight_tv);
		mTLPiv = (ImageView) this.findViewById(R.id.truck_license_tv);
		
		mTLtv.setText(CellSiteConstants.TruckLengths[app.getUser().getMyTruck().getLengthId()]);
		mTLtv.setText(CellSiteConstants.TruckTypes[app.getUser().getMyTruck().getTypeId()]);
		
		setLicenseImage();
		
	}
	
	public void setLicenseImage() {

		String truckLicenseUrl = app.getUser().getMyTruck().getLicenseImageUrl();

		if (truckLicenseUrl == null
				|| truckLicenseUrl.equalsIgnoreCase("null")) {
			Log.d(TAG, " IT IS A NULL.");
			
			mTLPiv.setImageResource(R.drawable.ic_launcher);  //TODO: ??????????????????
			
		} else {
			mDownloadImageTask = new DownloadImageTask();
			mDownloadImageTask.execute(truckLicenseUrl,
					app.regUserPath);
		}
	}

	public void initData() {
          
		for (int i = 0; i < 14; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("PIC", R.drawable.ic_launcher);
			map.put("TITLE", CellSiteConstants.TruckTypes[i]);
			map.put("TTYPE", i+1);
			mTruckTypeList.add(map);
		}
		
		for (int i = 0; i < 17; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("TITLE", CellSiteConstants.TruckLengths[i]);
			map.put("TLENGTH", i+1);
			mTruckLengthList.add(map);
		}
		
		for (int i = 0; i < 10; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("TITLE", "Test Title");
			map.put("TWEIGHT", "TW1");
			mTruckWeightList.add(map);
		}

	}

	public void chooseTruckType(View v) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		GridView gridView1 = new GridView(this);
		gridView1.setNumColumns(3);
		// (GridView)findViewById(R.id.gridView1);
		SimpleAdapter adapter = new SimpleAdapter(this, mTruckTypeList,
				R.layout.truck_type_griditem, new String[] { "PIC", "TITLE",
						"TTYPE" }, new int[] { R.id.griditem_pic,
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
				mTruckType = ((TextView) view.findViewById(R.id.griditem_type))
						.getText().toString();

				mUpdateTruckTask = new UpdateTruckTask();
				mUpdateTruckTask.execute(""
						+ app.getUser().getMyTruck().getTruckId(), mTruckType,
						"" + app.getUser().getMyTruck().getLengthId(), ""
								+ app.getUser().getMyTruck().getWeightId(), ""
								+ app.getUser().getMyTruck().getStatusId(), ""
								+ app.getUser().getId());
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void chooseTruckLength(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		GridView gridView1 = new GridView(this);
		gridView1.setNumColumns(3);
		// (GridView)findViewById(R.id.gridView1);
		SimpleAdapter adapter = new SimpleAdapter(this, mTruckLengthList,
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
				mUpdateTruckTask = new UpdateTruckTask();
				mUpdateTruckTask.execute(""
						+ app.getUser().getMyTruck().getTruckId(), ""
						+ app.getUser().getMyTruck().getTypeId(), mTruckLength,
						"" + app.getUser().getMyTruck().getWeightId(), ""
								+ app.getUser().getMyTruck().getStatusId(), ""
								+ app.getUser().getId());

				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void chooseTruckWeight(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		GridView gridView1 = new GridView(this);
		gridView1.setNumColumns(3);
		SimpleAdapter adapter = new SimpleAdapter(this, mTruckWeightList,
				R.layout.truck_weight_griditem, new String[] { "TITLE",
						"TWEIGHT" }, new int[] { R.id.griditem_title,
						R.id.griditem_weight, });

		gridView1.setAdapter(adapter);
		builder.setTitle("Please Choose");
		builder.setInverseBackgroundForced(true);
		builder.setView(gridView1);
		final Dialog dialog = builder.create();

		gridView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long rowId) {
				mTWtv.setText(((TextView) view
						.findViewById(R.id.griditem_title)).getText());
				mTruckWeight = ((TextView) view
						.findViewById(R.id.griditem_weight)).getText()
						.toString();
				mUpdateTruckTask = new UpdateTruckTask();
				mUpdateTruckTask.execute(""
						+ app.getUser().getMyTruck().getTruckId(), ""
						+ app.getUser().getMyTruck().getTypeId(), ""
						+ app.getUser().getMyTruck().getLengthId(),
						mTruckWeight, ""
								+ app.getUser().getMyTruck().getStatusId(), ""
								+ app.getUser().getId());

				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void updateMobile(View v) {
		Intent intent = new Intent(TruckActivity.this,
				UpdateTruckMobileActivity.class);
		startActivity(intent);
	}

	private class UpdateTruckTask extends AsyncTask<String, String, Integer> {
		@Override
		public Integer doInBackground(String... params) {
			return updateTruck(params[0], params[1], params[2], params[3],
					params[4], params[5]);
		}

		@Override
		public void onPostExecute(Integer result) {
			if (this.isCancelled()) {
				return;
			}
			Integer resCode = result;// Integer.parseInt(result);
			if (resCode == CellSiteConstants.RESULT_SUC) {
				// TODO: set for different user
			}
		}

		protected Integer updateTruck(String _truckId, String _truckType,
				String _truckLength, String _truckWeight, String _truckStatus,
				String _userId) {

			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.TRUCK_ID, _truckId));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.TRUCK_STATUS, _truckStatus));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.TRUCK_WEIGHT, _truckWeight));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.TRUCK_TYPE, _truckType));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.TRUCK_LENGTH, _truckLength));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.USER_ID, _userId));

			JSONObject response = null;
			try {
				response = CellSiteHttpClient.executeHttpPost(
						CellSiteConstants.UPDATE_TRUCK_URL, postParameters);

				int resultCode = Integer.parseInt(response.get(
						CellSiteConstants.RESULT_CODE).toString());
				if (resultCode == CellSiteConstants.RESULT_SUC) {
					// ??????????
					int truckId = Integer.parseInt(response.get(
							CellSiteConstants.TRUCK_ID).toString());
					app.getUser().getMyTruck().setTruckId(truckId);
					app.getUser().getMyTruck()
							.setLengthId(Integer.parseInt(_truckLength));
					app.getUser().getMyTruck()
							.setTypeId(Integer.parseInt(_truckType));
					app.getUser().getMyTruck()
							.setWeightId(Integer.parseInt(_truckWeight));

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

	class DownloadImageTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			Log.d(TAG, "URL=" + (String) params[0]);
			protraitBmp = app.downloadBmpByUrl((String) params[0], params[1]);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if (!this.isCancelled()) {
				if (protraitBmp != null) {
					mTLPiv.setImageDrawable(new BitmapDrawable(protraitBmp));
					// TODO
				}
			}
		}
	}

	/**
	 * edit portait
	 * 
	 * @param v
	 */
	public void editDriverLicense(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(res.getString(R.string.choose_portrait));
		builder.setItems(
				new String[] { res.getString(R.string.getPhotoFromCamera),
						res.getString(R.string.getPhotoFromMemory) },
				new DialogInterface.OnClickListener() {
					// @Override
					public void onClick(DialogInterface dialog, int id) {
						switch (id) {
						case 0:
							startToCameraActivity();
							break;
						case 1:
							startToMediaActivity();
							break;
						}
					}

				});
		AlertDialog alert = builder.create();
		alert.show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CellSiteConstants.TAKE_PICTURE
				|| requestCode == CellSiteConstants.PICK_PICTURE) {
			Uri uri = null;
			if (requestCode == CellSiteConstants.TAKE_PICTURE) {
				uri = imageUri;

			} else if (requestCode == CellSiteConstants.PICK_PICTURE) {
				uri = data.getData();

			}

			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(uri, filePathColumn,
					null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();
				Log.d(TAG, "filePath =" + filePath);
				Log.d(TAG, "uri=" + uri.toString());
				imageUri = Uri.fromFile(new File(filePath));
			} else // This is a bug, in some cases, some images like
			{
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					Toast.makeText(this, R.string.sdcard_occupied,
							Toast.LENGTH_SHORT).show();
					return;
				}

				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(new Date());
				File tmpFile = new File(app.regUserPath + File.separator
						+ "IMG_" + timeStamp + ".jpg");
				File srcFile = new File(uri.getPath());
				if (srcFile.exists()) {
					try {
						Utils.copyFile(srcFile, tmpFile);
						app.getUser().getMyTruck().setLicenseImageUrl(tmpFile.getAbsolutePath());
					} catch (Exception e) {
						Toast.makeText(this, R.string.create_tmp_file_fail,
								Toast.LENGTH_SHORT).show();
						return;
					}
				} else {
					Log.d(TAG, "Logic error, should not come to here");
					Toast.makeText(this, R.string.file_not_found,
							Toast.LENGTH_SHORT).show();
					return;
				}

				imageUri = Uri.fromFile(tmpFile);
			}

			doCrop();
			Log.d(TAG, "onActivityResult PICK_PICTURE");

		} else if (requestCode == CellSiteConstants.CROP_PICTURE) {
			Log.d(TAG, "crop picture");
			// processFile();

			if (data != null) {
				Bundle extras = data.getExtras();
				Bitmap photo = extras.getParcelable("data");

				protraitBmp = photo;
				mTLPiv.setImageBitmap(protraitBmp);
				mUpdateImageTask = new UpdateImageTask();
				mUpdateImageTask.execute("" + app.getUser().getId(), ""
						+ app.getUser().getMyTruck().getTruckId(),
						Utils.bitmap2String(protraitBmp),
						CellSiteConstants.UPDATE_DRIVER_LICENSE_URL);

				isPortraitChanged = true;
			}
		}
	}

	private void doCrop() {
		Log.d(TAG, "doCrop()");
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = this.getPackageManager()
				.queryIntentActivities(intent, 0);

		int size = list.size();

		if (size == 0) {
			Log.d(TAG, " Crop activity is not found.  List size is zero.");
			Bitmap tmpBmp = BitmapFactory.decodeFile(imageUri.getPath(), null);
			protraitBmp = Bitmap.createScaledBitmap(tmpBmp, IMAGE_WIDTH,
					IMAGE_HEIGHT, false);

			mTLPiv.setImageBitmap(protraitBmp);
			isPortraitChanged = true;

			Log.d(TAG, "set bitmap");

			return;
		} else {
			Log.d(TAG, "found the crop activity.");
			intent.setData(imageUri);

			intent.putExtra("outputX", IMAGE_WIDTH);
			intent.putExtra("outputY", IMAGE_HEIGHT);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);

			if (size == 1) {
				Log.d(TAG, "Just one as choose it as crop activity.");
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);
				i.setComponent(new ComponentName(res.activityInfo.packageName,
						res.activityInfo.name));

				startActivityForResult(i, CellSiteConstants.CROP_PICTURE);
			} else {
				Log.d(TAG,
						"More that one activity for crop  is found . will chooose one");
				for (ResolveInfo res : list) {
					final CropOption co = new CropOption();

					co.title = getPackageManager().getApplicationLabel(
							res.activityInfo.applicationInfo);
					co.icon = getPackageManager().getApplicationIcon(
							res.activityInfo.applicationInfo);
					co.appIntent = new Intent(intent);

					co.appIntent
							.setComponent(new ComponentName(
									res.activityInfo.packageName,
									res.activityInfo.name));

					cropOptions.add(co);
				}

				CropOptionAdapter adapter = new CropOptionAdapter(
						getApplicationContext(), cropOptions);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Choose Crop App");

				builder.setAdapter(adapter,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								startActivityForResult(
										cropOptions.get(item).appIntent,
										CellSiteConstants.CROP_PICTURE);
							}
						});

				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					// @Override
					public void onCancel(DialogInterface dialog) {
						if (imageUri != null) {
							getContentResolver().delete(imageUri, null, null);
							imageUri = null;
							isPortraitChanged = false;
						}
					}
				});
				AlertDialog alert = builder.create();

				alert.show();
			}
		}
	}

	/**
	 * start to take picture
	 */
	private void startToCameraActivity() {
		Intent localIntent = new Intent("android.media.action.IMAGE_CAPTURE");

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile = new File(app.regUserPath + File.separator + "IMG_"
				+ timeStamp + ".png");
		localIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
		imageUri = Uri.fromFile(mediaFile);

		// isCameraCapture = true;

		startActivityForResult(localIntent, CellSiteConstants.TAKE_PICTURE);
	}

	/**
	 * start to choose pictue
	 */
	private void startToMediaActivity() {
		Intent localIntent = new Intent("android.intent.action.PICK");
		Uri localUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
		localIntent.setDataAndType(localUri, "image/*");
		startActivityForResult(localIntent, CellSiteConstants.PICK_PICTURE);
	}

	private class UpdateImageTask extends AsyncTask<String, String, Integer> {
		@Override
		public Integer doInBackground(String... params) {
			return updateImage(params[0], params[1], params[2], params[3]);
		}

		@Override
		public void onPostExecute(Integer result) {
			if (this.isCancelled()) {
				return;
			}
			Integer resCode = result;// Integer.parseInt(result);
			if (resCode == CellSiteConstants.RESULT_SUC) {
				// TODO: set for different user
			}
		}

		protected Integer updateImage(String _userId, String _truckId,
				String _bitmap, String _updateUrl) {

			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("bitmap", _bitmap));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.TRUCK_ID, _truckId));
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.USER_ID, _userId));

			JSONObject response = null;
			try {
				response = CellSiteHttpClient.executeHttpPost(_updateUrl,
						postParameters);

				int resultCode = Integer.parseInt(response.get(
						CellSiteConstants.RESULT_CODE).toString());
				if (resultCode == CellSiteConstants.RESULT_SUC) {
					// ??????????
					int truckId = Integer.parseInt(response.get(
							CellSiteConstants.TRUCK_ID).toString());
					app.getUser().getMyTruck().setTruckId(truckId);

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

}
