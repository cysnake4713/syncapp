package com.cysnake.dbtools;



import com.cysnake.syncapp.act.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BaseDao {

	private static final String TAG = "BaseDao";

	private static final String DATABASE_NAME = "renrensync";
	private static final int DATABASE_VERSION = 1;
	private static final String PATH = "/data/data/com.cysnake.syncapp.act/databases/";
	private static final int id=R.raw.renrensync;

	public class DatabaseHelper extends MySQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION,
					id);
			setPath(PATH);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

	}
}
