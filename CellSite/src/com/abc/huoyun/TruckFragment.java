package com.abc.huoyun;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TruckFragment extends Fragment {

	public static TruckFragment newInstance() {
		TruckFragment mHCFragment = new TruckFragment();
		return mHCFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.main_tab_trucks, container, false);

		return view;
	}
}