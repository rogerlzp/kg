package com.abc.huoyun.cache;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

public class WorkHorderType {
	int nIndex;
	public ArrayList<HashMap<String, Object>> nHorders;
	public WorkHorderAdapter nHorderAdapter;

	public int nDisplayNum;
	public Boolean hasShowAllHorders;

	public WorkHorderType(int aIndex, Context ctx) {
		nHorders = new ArrayList<HashMap<String, Object>>();
		hasShowAllHorders = false;
		nIndex = aIndex;
		nHorderAdapter = new WorkHorderAdapter(ctx);
	}
}
