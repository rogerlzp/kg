package com.abc.huoyun;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.huoyun.utility.CellSiteApplication;
import com.abc.huoyun.utility.image.ImageLoad;
import com.abc.huoyun.utility.image.ImageLoad.ImageDownloadedCallBack;

public class MeFragment extends Fragment {

	private final static String TAG = "MeFragment";
	private TextView mNameTv;
	private TextView mMobileTv;
	private RelativeLayout shipperUserRL;
	private RelativeLayout settingRL;
	private RelativeLayout verifyRL;

	String name_current;
	String phoneNum_current;
	String portraitImageUrl;
	private ImageView avatarIv;
	ImageLoad mImageLoad;

	CellSiteApplication app;

	private boolean isViewShown;
	private boolean isPrepared;

	public static MeFragment newInstance() {
		Log.d(TAG, "newInstance");
		MeFragment mHCFragment = new MeFragment();

		return mHCFragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);

		app = (CellSiteApplication) this.getActivity().getApplication();
		lazyLoad();
	}

	class SettingListener implements View.OnClickListener {
		private Context ctx;

		public SettingListener(Context _ctx) {
			this.ctx = _ctx;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ctx, SettingActivity.class);
			startActivity(intent);
		}
	}

	class VerifyListener implements View.OnClickListener {
		private Context ctx;

		public VerifyListener(Context _ctx) {
			this.ctx = _ctx;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ctx, VerifyActivity.class);
			startActivity(intent);
		}
	}

	private void showUserAvator(ImageView imageView, String avatar) {
		imageView.setTag(avatar);
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

	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		lazyLoad();

		if (isViewShown && isPrepared) {
			String name = app.getUser().getName();
			String phoneNum = app.getUser().getMobileNum();
			if (name != null && !name.equals(name_current)) {
				name_current = name;
				mNameTv.setText(name);
			}
			if (phoneNum != null && !phoneNum.equals(phoneNum_current)) {
				phoneNum_current = phoneNum;
				mMobileTv.setText(phoneNum);
			}

			String profileImageTmp = app.getUser().profileImageUrl;
			if (profileImageTmp != null
					&& !profileImageTmp.equals(portraitImageUrl)) {
				showUserAvator(avatarIv, profileImageTmp);
			}
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		View view = inflater.inflate(R.layout.main_tab_me, container, false);
		isPrepared = true;

		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		Log.d(TAG, "setUserVisibleHint");
		super.setUserVisibleHint(isVisibleToUser);

		if (isVisibleToUser) {

			isViewShown = true;

			// 相当于Fragment的onResume
		} else {
			isViewShown = false;
			// 相当于Fragment的onPause
		}
	}

	public void lazyLoad() {
		if (isViewShown && isPrepared) {
			Log.d(TAG, "lazyLoad");
			mImageLoad = new ImageLoad(this.getActivity());
			mNameTv = (TextView) this.getActivity().findViewById(R.id.name_tv);
			mMobileTv = (TextView) this.getActivity().findViewById(
					R.id.me_mobile_tv);
			settingRL = (RelativeLayout) this.getActivity().findViewById(
					R.id.me_setting_rl);
			SettingListener mSettingListener = new SettingListener(
					this.getActivity());

			settingRL.setOnClickListener(mSettingListener);

			avatarIv = (ImageView) this.getActivity().findViewById(
					R.id.portrait_iv);
			verifyRL = (RelativeLayout) this.getActivity().findViewById(
					R.id.verify_rl);
			verifyRL.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getActivity(),
							VerifyActivity.class));

				}
			});

			name_current = app.getUser().getName();
			if (name_current != null) {
				mNameTv.setText(name_current);
			}
			phoneNum_current = app.getUser().getMobileNum();
			if (app.getUser().getMobileNum() != null) {
				mMobileTv.setText(phoneNum_current);
			}
			shipperUserRL = (RelativeLayout) this.getActivity().findViewById(
					R.id.shipper_user);
			shipperUserRL.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getActivity(),
							PersonalActivity.class));

				}
			});
			portraitImageUrl = app.getUser().profileImageUrl;
			if (portraitImageUrl != null && !portraitImageUrl.equals("")) {
				showUserAvator(avatarIv, portraitImageUrl);
			}
		}
	}

}