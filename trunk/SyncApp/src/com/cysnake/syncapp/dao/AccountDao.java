package com.cysnake.syncapp.dao;

import com.cysnake.syncapp.po.AccountConfigPO;
import com.cysnake.syncapp.po.AccountPO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class AccountDao extends CommonDao {
	public static final String TABLE_NAME = "ACCOUNT";
	public static final String KEY_ID = "_ID";
	public static final String KEY_TYPE = "TYPE";
	public static final String KEY_NAME = "NAME";
	public static final String KEY_L_PHOTO = "L_PHOTO";
	public static final String KEY_H_PHOTO = "H_PHOTO";
	public static final String KEY_USER_ID = "USER_ID";

	public static final String CONFIG_TABLE_NAME = "ACCOUNT_CONFIG";
	public static final String KEY_CONFIG_ID = "_ID";
	public static final String KEY_CONFIG_ACCOUNT_ID = "ACCOUNT_ID";
	public static final String KEY_CONFIG_ACCESS_TOKEN = "ACCESS_TOKEN";
	public static final String KEY_CONFIG_CREATE_TIME = "CREATE_TIME";
	public static final String KEY_CONFIG_SESSION_KEY = "SESSION_KEY";
	public static final String KEY_CONFIG_EXPIRE_SECONDS = "EXPIRE_SECONDS";
	public static final String KEY_CONFIG_SESSION_CREATE_TIME = "SESSION_CREATE_TIME";
	public static final String KEY_CONFIG_SESSION_SECRET = "SESSION_SECRET";

	public AccountDao(Context ctx) {
		super(ctx);
	}

	public void createOrUpdate(AccountPO account) {
		if (isExist(account)) {
			update(account);
		} else {
			insert(account);
		}
	}

	public AccountPO find(int id) {

		Cursor cursor = mDb
				.rawQuery(
						"select ACCOUNT._ID as aid, ACCOUNT_CONFIG._ID as cid,* from ACCOUNT,ACCOUNT_CONFIG where ACCOUNT._ID=ACCOUNT_CONFIG.ACCOUNT_ID and ACCOUNT._ID=?",
						new String[] { String.valueOf(id) });
		AccountPO account = new AccountPO();
		if (cursor.moveToFirst()) {
			account.setId(cursor.getInt(cursor.getColumnIndex("aid")));
			account.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
			account.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
			account.setlPhoto(cursor.getBlob(cursor.getColumnIndex(KEY_L_PHOTO)));
			account.sethPhoto(cursor.getBlob(cursor.getColumnIndex(KEY_H_PHOTO)));
			account.setUserId(cursor.getLong(cursor.getColumnIndex(KEY_USER_ID)));
			AccountConfigPO config = new AccountConfigPO();
			config.setId(cursor.getInt(cursor.getColumnIndex("cid")));
			config.setAccessToken(cursor.getString(cursor
					.getColumnIndex(KEY_CONFIG_ACCESS_TOKEN)));
			config.setCreateTime(cursor.getLong(cursor
					.getColumnIndex(KEY_CONFIG_CREATE_TIME)));
			config.setSessionKey(cursor.getString(cursor
					.getColumnIndex(KEY_CONFIG_SESSION_KEY)));
			config.setExpireSeconds(cursor.getLong(cursor
					.getColumnIndex(KEY_CONFIG_EXPIRE_SECONDS)));
			config.setSessionCreateTime(cursor.getLong(cursor
					.getColumnIndex(KEY_CONFIG_SESSION_CREATE_TIME)));
			config.setSessionSecret(cursor.getString(cursor
					.getColumnIndex(KEY_CONFIG_SESSION_SECRET)));
			account.setConfig(config);
			return account;
		} else {
			return null;
		}
	}

	public void update(AccountPO account) {
		if (account.getConfig() != null) {
			AccountConfigPO config = account.getConfig();
			ContentValues cv = new ContentValues();
			cv.put(KEY_CONFIG_ACCESS_TOKEN, config.getAccessToken());
			cv.put(KEY_CONFIG_CREATE_TIME, config.getCreateTime());
			cv.put(KEY_CONFIG_EXPIRE_SECONDS, config.getExpireSeconds());
			cv.put(KEY_CONFIG_SESSION_KEY, config.getSessionKey());
			cv.put(KEY_CONFIG_SESSION_CREATE_TIME,
					config.getSessionCreateTime());
			cv.put(KEY_CONFIG_SESSION_SECRET, config.getSessionKey());
			mDb.update(CONFIG_TABLE_NAME, cv, KEY_CONFIG_ACCOUNT_ID + "=?",
					new String[] { String.valueOf(account.getId()) });
		}
		ContentValues cv = new ContentValues();
		cv.put(KEY_TYPE, account.getType());
		cv.put(KEY_NAME, account.getName());
		cv.put(KEY_L_PHOTO, account.getlPhoto());
		cv.put(KEY_H_PHOTO, account.gethPhoto());
		cv.put(KEY_USER_ID, account.getUserId());
		mDb.update(TABLE_NAME, cv, KEY_ID + "=?",
				new String[] { String.valueOf(account.getId()) });
	}

	public void insert(AccountPO account) {
		ContentValues cv1 = new ContentValues();
		cv1.put(KEY_TYPE, account.getType());
		cv1.put(KEY_NAME, account.getName());
		cv1.put(KEY_L_PHOTO, account.getlPhoto());
		cv1.put(KEY_H_PHOTO, account.gethPhoto());
		cv1.put(KEY_USER_ID, account.getUserId());
		long accountId = mDb.insert(TABLE_NAME, KEY_USER_ID, cv1);
		account.setId((int) accountId);
		if (account.getConfig() != null) {
			AccountConfigPO config = account.getConfig();
			ContentValues cv = new ContentValues();
			cv.put(KEY_CONFIG_ACCOUNT_ID, accountId);
			cv.put(KEY_CONFIG_ACCESS_TOKEN, config.getAccessToken());
			cv.put(KEY_CONFIG_CREATE_TIME, config.getCreateTime());
			cv.put(KEY_CONFIG_EXPIRE_SECONDS, config.getExpireSeconds());
			cv.put(KEY_CONFIG_SESSION_KEY, config.getSessionKey());
			cv.put(KEY_CONFIG_SESSION_CREATE_TIME,
					config.getSessionCreateTime());
			cv.put(KEY_CONFIG_SESSION_SECRET, config.getSessionKey());
			long acId = mDb.insert(CONFIG_TABLE_NAME, null, cv);
			config.setId((int) acId);
		}
	}

	public boolean isExist(AccountPO account) {
		Cursor mCursor;
		mCursor = mDb.query(
				true,
				TABLE_NAME,
				new String[] { KEY_ID },
				KEY_TYPE + " =? and " + KEY_USER_ID + " =?",
				new String[] { account.getType(),
						String.valueOf(account.getUserId()) }, null, null,
				null, null);
		if (mCursor.getCount() > 0) {
			mCursor.moveToFirst();
			int id = mCursor.getInt(mCursor.getColumnIndex(KEY_ID));
			account.setId(id);
			mCursor.close();
			return true;
		} else {
			account.setId(-1);
			mCursor.close();
			return false;
		}
	}

	public Cursor findAll() {
		Cursor mCursor;
		mCursor = mDb.rawQuery("SELECT _ID as _id,H_PHOTO, NAME FROM ACCOUNT",
				null);
		return mCursor;
	}

}
