package com.abc.huoyun.cache;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

public class HorderType {
	int nIndex;
	public ArrayList<HashMap<String, Object>> nHorders;
	public HorderAdapter nHorderAdapter;

	public int nDisplayNum;
	public Boolean hasShowAllHorders;

	public HorderType(int aIndex, Context ctx) {
		nHorders = new ArrayList<HashMap<String, Object>>();
		hasShowAllHorders = false;
		nIndex = aIndex;
		nHorderAdapter = new HorderAdapter(ctx);
	}

	public void setAdapter(HorderAdapter _adapter) {
		this.nHorderAdapter = _adapter;
	}
	public HorderAdapter getAdapter() {
		return this.nHorderAdapter;
	}
}