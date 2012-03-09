package com.cysnake.dbtools;

/**
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/***
 * A helper class to manage database creation and version management. You create
 * a subclass implementing {@link #onCreate}, {@link #onUpgrade} and optionally
 * {@link #onOpen}, and this class takes care of opening the database if it
 * exists, creating it if it does not, and upgrading it as necessary.
 * Transactions are used to make sure the database is always in a sensible
 * state.
 * <p>
 * For an example, see the NotePadProvider class in the NotePad sample
 * application, in the <em>samples/</em> directory of the SDK.
 * </p>
 */
public abstract class MySQLiteOpenHelper {
	private static final String TAG = "MySQLiteOpenHelper";

	private final Context mContext;
	private final String mName;
	private final CursorFactory mFactory;
	private final int mNewVersion;
	private final int fid;

	private SQLiteDatabase mDatabase = null;
	private boolean mIsInitializing = false;
	private static String DB_PATH = null;
	private boolean copyFlag = false;

	/***
	 * Create a helper object to create, open, and/or manage a database. The
	 * database is not actually created or opened until one of
	 * {@link #getWritableDatabase} or {@link #getReadableDatabase} is called.
	 * 
	 * @param context
	 *            to use to open or create the database
	 * @param name
	 *            of the database file, or null for an in-memory database
	 * @param factory
	 *            to use for creating cursor objects, or null for the default
	 * @param version
	 *            number of the database (starting at 1); if the database is
	 *            older, {@link #onUpgrade} will be used to upgrade the database
	 */
	public MySQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version,int id) {
		if (version < 1)
			throw new IllegalArgumentException("Version must be >= 1, was "
					+ version);

		mContext = context;
		mName = name;
		mFactory = factory;
		mNewVersion = version;
		fid=id;
	}

	/***
	 * Create and/or open a database that will be used for reading and writing.
	 * Once opened successfully, the database is cached, so you can call this
	 * method every time you need to write to the database. Make sure to call
	 * {@link #close} when you no longer need it.
	 * 
	 * <p>
	 * Errors such as bad permissions or a full disk may cause this operation to
	 * fail, but future attempts may succeed if the problem is fixed.
	 * </p>
	 * 
	 * @throws SQLiteException
	 *             if the database cannot be opened for writing
	 * @return a read/write database object valid until {@link #close} is called
	 */
	public synchronized SQLiteDatabase getWritableDatabase() {
		if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
			return mDatabase; // The database is already open for business
		}

		if (mIsInitializing) {
			throw new IllegalStateException(
					"getWritableDatabase called recursively");
		}

		// If we have a read-only database open, someone could be using it
		// (though they shouldn't), which would cause a lock to be held on
		// the file, and our attempts to open the database read-write would
		// fail waiting for the file lock. To prevent that, we acquire the
		// lock on the read-only database, which shuts out other users.

		boolean success = false;
		SQLiteDatabase db = null;
		try {
			mIsInitializing = true;
			if (mName == null) {
				db = SQLiteDatabase.create(null);
			} else {
				if (!copyFlag) {
					boolean dbExist = checkDataBase();
					if (!dbExist) {
						try {

							copyDataBase();
						} catch (FileNotFoundException e) {
							Log.i(TAG, "Database file not find!");
							// throw new Error("Database file not find!");

						} catch (IOException e) {
							Log.i(TAG, "File read error!");
							// throw new Error("File read error!");
						}
					}
					copyFlag = true;
				}
				db = mContext.openOrCreateDatabase(mName, 0, mFactory);
			}

			int version = db.getVersion();
			if (version != mNewVersion) {
				db.beginTransaction();
				try {
					if (version == 0) {
						onCreate(db);
					} else {
						onUpgrade(db, version, mNewVersion);
					}
					db.setVersion(mNewVersion);
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
			}

			onOpen(db);
			success = true;
			return db;
		} finally {
			mIsInitializing = false;
			if (success) {
				if (mDatabase != null) {
					try {
						mDatabase.close();
					} catch (Exception e) {
					}
				}
				mDatabase = db;
			} else {
				if (mDatabase != null)
					if (db != null)
						db.close();
			}
		}
	}

	/***
	 * Create and/or open a database. This will be the same object returned by
	 * {@link #getWritableDatabase} unless some problem, such as a full disk,
	 * requires the database to be opened read-only. In that case, a read-only
	 * database object will be returned. If the problem is fixed, a future call
	 * to {@link #getWritableDatabase} may succeed, in which case the read-only
	 * database object will be closed and the read/write object will be returned
	 * in the future.
	 * 
	 * @throws SQLiteException
	 *             if the database cannot be opened
	 * @return a database object valid until {@link #getWritableDatabase} or
	 *         {@link #close} is called.
	 */
	public synchronized SQLiteDatabase getReadableDatabase() {
		if (mDatabase != null && mDatabase.isOpen()) {
			return mDatabase; // The database is already open for business
		}

		if (mIsInitializing) {
			throw new IllegalStateException(
					"getReadableDatabase called recursively");
		}

		try {
			return getWritableDatabase();
		} catch (SQLiteException e) {
			if (mName == null)
				throw e; // Can't open a temp database read-only!
			Log.e(TAG, "Couldn't open " + mName
					+ " for writing (will try read-only):", e);
		}

		SQLiteDatabase db = null;
		try {
			mIsInitializing = true;
			if (!copyFlag) {

				boolean dbExist = checkDataBase();

				if (!dbExist) {
					try {
						copyDataBase();
					} catch (FileNotFoundException e) {
						Log.i(TAG, "Database file not find!");
						// throw new Error("Database file not find!");
					} catch (IOException e) {
						Log.i(TAG, "File read error!");
						// throw new Error("File read error!");
					}
				}

				copyFlag = true;
			}
			String path = DB_PATH + mName;
			db = SQLiteDatabase.openDatabase(path, mFactory,
					SQLiteDatabase.OPEN_READONLY);
			if (db.getVersion() != mNewVersion) {
				throw new SQLiteException(
						"Can't upgrade read-only database from version "
								+ db.getVersion() + " to " + mNewVersion + ": "
								+ path);
			}

			onOpen(db);
			Log.w(TAG, "Opened " + mName + " in read-only mode");
			mDatabase = db;
			return mDatabase;
		} finally {
			mIsInitializing = false;
			if (db != null && db != mDatabase)
				db.close();
		}
	}

	/***
	 * Close any open database object.
	 */
	public synchronized void close() {
		if (mIsInitializing)
			throw new IllegalStateException("Closed during initialization");

		if (mDatabase != null && mDatabase.isOpen()) {
			mDatabase.close();
			mDatabase = null;
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
		String myPath = DB_PATH + mName;
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
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * 
	 * @throws IOException
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = mContext.getResources().openRawResource(
				fid);

		// Path to the just created empty db
		String outFileName = DB_PATH + mName;

		File dir = new File(DB_PATH);
		if (!dir.exists())
			dir.mkdir();
		if (!(new File(outFileName)).exists()) {
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

	/**
	 * do this method before getWritableDatabase()
	 * 
	 * @param path
	 *            where the database file should be copy to
	 */
	public void setPath(String path) {
		DB_PATH = path;
	}

	/***
	 * Called when the database is created for the first time. This is where the
	 * creation of tables and the initial population of the tables should
	 * happen.
	 * 
	 * @param db
	 *            The database.
	 */
	public abstract void onCreate(SQLiteDatabase db);

	/***
	 * Called when the database needs to be upgraded. The implementation should
	 * use this method to drop tables, add tables, or do anything else it needs
	 * to upgrade to the new schema version.
	 * 
	 * <p>
	 * The SQLite ALTER TABLE documentation can be found <a
	 * href="http://sqlite.org/lang_altertable.html">here</a>. If you add new
	 * columns you can use ALTER TABLE to insert them into a live table. If you
	 * rename or remove columns you can use ALTER TABLE to rename the old table,
	 * then create the new table and then populate the new table with the
	 * contents of the old table.
	 * 
	 * @param db
	 *            The database.
	 * @param oldVersion
	 *            The old database version.
	 * @param newVersion
	 *            The new database version.
	 */
	public abstract void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion);

	/***
	 * Called when the database has been opened. Override method should check
	 * {@link SQLiteDatabase#isReadOnly} before updating the database.
	 * 
	 * @param db
	 *            The database.
	 */
	public void onOpen(SQLiteDatabase db) {
	}

}
