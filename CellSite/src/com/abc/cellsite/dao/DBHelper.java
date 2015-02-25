package com.abc.cellsite.dao;

import java.io.IOException;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	static final String TAG = DBHelper.class.getSimpleName();
	private SQLiteDatabase mDatabase;
	Context context;
	
	private static String DB_PATH = "/data/data/com.abc.cellsite/databases/"; 
	
	static final String DB_NAME = "cellsite.db";
	static final int DB_VERSION = 1;
	public static final String LAST_UPDATE = "LastUpdate"; // last update time
	
	public static final String TABLE_NAME1 = "TABLE1"; // TODO: modified later
	public static final String COL_KEY = "KEY1"; //TODO:added
	public static final String COL_CONTENT = "CONTENT1";// TODO: added 
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;

		try {
			createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CreateSystemSql = "create table " + TABLE_NAME1
				+ "(" + COL_KEY + " text primary key, " + COL_CONTENT
				+ " text)";
		try {
			db.execSQL(CreateSystemSql);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Can use the sql syntax "ALTER TABLE .....";
		try {
			db.execSQL("drop table if exists " + TABLE_NAME1);
		} catch (SQLException e) {

			e.printStackTrace();
		}

		onCreate(db);
	}
	
	
	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are able to overwrite that
			// database with our database.
			this.getReadableDatabase();
		}

	}
	
	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;
		Boolean isDBExist = false;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
			if (checkDB != null) {
				isDBExist = true;
			}
		} catch (SQLiteException e) {
			// database does't exist yet.
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return isDBExist;
	}
	
	
	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		mDatabase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}
	

	@Override
	public synchronized void close() {
		if (mDatabase != null)
			mDatabase.close();
		super.close();

	}
	
}
