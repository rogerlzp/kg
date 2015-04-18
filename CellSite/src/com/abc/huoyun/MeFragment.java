package com.abc.huoyun;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.huoyun.utility.CellSiteApplication;
import com.abc.huoyun.utility.CellSiteConstants;
import com.abc.huoyun.utility.LocalUser;
import com.abc.huoyun.utility.image.ImageLoad;
import com.abc.huoyun.utility.image.ImageLoad.ImageDownloadedCallBack;

public class MeFragment extends Fragment {

	private final static String TAG = "MeFragment";
	private TextView mNameTv;
	private TextView mMobileTv;
	private RelativeLayout shipperUserRL;

	String name_current;
	String phoneNum_current;
	String portraitImageUrl;
	private ImageView avatarIv;
	ImageLoad mImageLoad;

	public static MeFragment newInstance() {
		MeFragment mHCFragment = new MeFragment();
		return mHCFragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mImageLoad = new ImageLoad(this.getActivity());
		mNameTv = (TextView) this.getActivity().findViewById(R.id.name_tv);
		mMobileTv = (TextView) this.getActivity().findViewById(R.id.mobile_tv);

		avatarIv = (ImageView) this.getActivity()
				.findViewById(R.id.portrait_iv);

		name_current = CellSiteApplication.getUser().getName();
		if (name_current != null) {
			mNameTv.setText(name_current);
		}
		phoneNum_current = CellSiteApplication.getUser().getMobileNum();
		if (CellSiteApplication.getUser().getMobileNum() != null) {
			mMobileTv.setText(phoneNum_current);
		}
		shipperUserRL = (RelativeLayout) this.getActivity().findViewById(
				R.id.shipper_user);
		shipperUserRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), PersonalActivity.class));

			}
		});
		portraitImageUrl = CellSiteApplication.getUser().profileImageUrl;
		if (portraitImageUrl != null && !portraitImageUrl.equals("")) {
			showUserAvator(avatarIv, portraitImageUrl);
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
		super.onResume();

		String name = CellSiteApplication.getUser().getName();
		String phoneNum = CellSiteApplication.getUser().getMobileNum();
		if (!name.equals(name_current)) {
			name_current = name;
			mNameTv.setText(name);
		}
		if (!phoneNum.equals(phoneNum_current)) {
			phoneNum_current = phoneNum;
			mMobileTv.setText(phoneNum);
		}

		String profileImageTmp = CellSiteApplication.getUser().profileImageUrl;
		if (!profileImageTmp.equals(portraitImageUrl)) {
			showUserAvator(avatarIv, profileImageTmp);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_tab_me, container, false);

		return view;
	}
}