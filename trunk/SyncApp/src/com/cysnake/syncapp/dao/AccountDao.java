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
		mDb.update(TABLE_NAME, cv, KEY_ID,
				new String[] { String.valueOf(account.getId()) });
	}

	public void insert(AccountPO account) {
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
			mDb.insert(CONFIG_TABLE_NAME, null, cv);
		}
		ContentValues cv = new ContentValues();
		cv.put(KEY_TYPE, account.getType());
		cv.put(KEY_NAME, account.getName());
		cv.put(KEY_L_PHOTO, account.getlPhoto());
		cv.put(KEY_H_PHOTO, account.gethPhoto());
		cv.put(KEY_USER_ID, account.getUserId());
		mDb.insert(TABLE_NAME, KEY_USER_ID, cv);
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

}
