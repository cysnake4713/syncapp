package com.cysnake.dbtools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.cysnake.syncapp.act.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseDao {

	private final Context mCtx;

	private static final String TAG = "BaseDao";

	private static final String DATABASE_NAME = "renrensync";
	private static final int DATABASE_VERSION = 4;
	private static final String PATH = "/data/data/com.cysnake.syncapp.act/databases/";
	private static final String NAME = "renrensync";
	private static final int DBSource = R.raw.renrensync;

	public BaseDao(Context ctx) {
		this.mCtx = ctx;
	}

	public class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				copyDataBase();
				Log.i(TAG, "create database");
			} catch (IOException e) {
				Log.e(TAG, "unable to create database!", e);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				copyDataBase();
				Log.i(TAG, "upgrade database from oldversion:" + oldVersion
						+ " to new Version:" + newVersion);
			} catch (IOException e) {
				Log.e(TAG, "unable to upgrade database!", e);
			}

		}

		/**
		 * Check if the database already exist to avoid re-copying the file each
		 * time you open the application.
		 * 
		 * @return true if it exists, false if it doesn't
		 */
		public boolean checkDataBase() {

			SQLiteDatabase checkDB = null;
			String myPath = PATH + NAME;
			try {

				checkDB = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READONLY);

			} catch (SQLiteException e) {

				// database does't exist yet.

			}

			if (checkDB != null) {

				checkDB.close();
				// File targetFile=new File(myPath);
				// try {
				// FileInputStream target=new FileInputStream(targetFile);
				// InputStream source = mContext.getResources().openRawResource(
				// R.raw.easyyi);
				//
				// if(target.available()!=source.available()){
				// checkDB=null;
				// }
				// } catch (FileNotFoundException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			}

			return checkDB != null ? true : false;
		}

		/**
		 * Copies your database from your local assets-folder to the just
		 * created empty database in the system folder, from where it can be
		 * accessed and handled. This is done by transfering bytestream.
		 * 
		 * @throws IOException
		 * */
		private void copyDataBase() throws IOException {

			// Open your local db as the input stream
			InputStream myInput = mCtx.getResources().openRawResource(DBSource);

			// Path to the just created empty db
			String outFileName = PATH + NAME;

			File dir = new File(PATH);
			if (!dir.exists())
				dir.mkdir();

			FileOutputStream myOutput = new FileOutputStream(outFileName);
			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();

		}

	}
}
