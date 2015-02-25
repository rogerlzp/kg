package com.abc.utility;

import android.content.Context;
import android.provider.BaseColumns;

import com.abc.cellsite.dao.DBHelper;

public class CacheData {

		private static final String TAG = CacheData.class.getSimpleName();

		// Variables for table columns
		public static final String CID = BaseColumns._ID;
		public final DBHelper dbHelper;

		public CacheData(Context context) {
			this.dbHelper = new DBHelper(context);
		}

		public void close() {
			// Will close the db connection
			this.dbHelper.close();
		}


}
