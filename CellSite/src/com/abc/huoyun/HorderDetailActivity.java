package com.abc.huoyun;

import android.content.Intent;
import android.os.Bundle;

import com.abc.huoyun.model.Horder;

public class HorderDetailActivity extends BaseActivity {

	Horder mHorder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.horder_detail);
		mHorder = new Horder();

	//	initView(intent);


	}

	public void initView(Intent intent) {
		

		
	}

}