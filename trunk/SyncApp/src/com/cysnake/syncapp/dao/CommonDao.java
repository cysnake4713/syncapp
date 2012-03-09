package com.cysnake.syncapp.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cysnake.dbtools.BaseDao;
import com.cysnake.dbtools.MySQLiteOpenHelper;

public class CommonDao extends BaseDao {
	private static final String TAG = "CommonDao";
	private MySQLiteOpenHelper mDbHelper;
	protected SQLiteDatabase mDb;
	private final Context mCtx;

	public CommonDao(Context ctx) {
		this.mCtx = ctx;
	}

	public CommonDao open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		Log.i(TAG, "open the database.");
		return this;
	}

	public void close() {
		mDbHelper.close();
		Log.i(TAG, "close the database.");
	}

	public void beginTransaction() {
		mDb.beginTransaction();
		Log.i(TAG, "begin transaction.");
	}
	
	public void setTransactionSuccessful(){
		mDb.setTransactionSuccessful();
	}

	public void endTransaction() {
		mDb.endTransaction();
		Log.i(TAG, "end transaction.");
	}
}
