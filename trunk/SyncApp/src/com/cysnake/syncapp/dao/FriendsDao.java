package com.cysnake.syncapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cysnake.syncapp.po.FriendPO;

public class FriendsDao extends CommonDao {

	public static final String TABLE_NAME = "FRIENDS";
	public static final String KEY_ID = "_ID";
	public static final String KEY_ACCOUNT_ID = "ACCOUNT_ID";
	public static final String KEY_FRIEND_ID = "FRIEND_ID";
	public static final String KEY_NAME = "NAME";
	public static final String KEY_SEX = "SEX";
	public static final String KEY_L_PHOTO = "L_PHOTO";
	public static final String KEY_H_PHOTO = "H_PHOTO";

	public FriendsDao(Context ctx) {
		super(ctx);
	}

	public Cursor findAll() {
		Cursor mCursor;
		mCursor = mDb.rawQuery("SELECT _ID as _id ,NAME ,L_PHOTO FROM FRIENDS",
				null);
		return mCursor;
	}

	public long insert(FriendPO friend) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_ACCOUNT_ID, friend.getAccountId());
		cv.put(KEY_FRIEND_ID, friend.getFriendId());
		cv.put(KEY_NAME, friend.getName());
		cv.put(KEY_SEX, friend.getSex());
		cv.put(KEY_L_PHOTO, friend.getlPhoto());
		return mDb.insert(TABLE_NAME, KEY_L_PHOTO, cv);
	}

	public boolean isExist(FriendPO friend) {
		Cursor mCursor;
		mCursor = mDb.query(true, TABLE_NAME, new String[] { KEY_ID },
				KEY_FRIEND_ID + " = " + friend.getFriendId() + "", null, null,
				null, null, null);
		if (mCursor.getCount() > 0) {
			mCursor.moveToNext();
			int id = mCursor.getInt(mCursor.getColumnIndex(KEY_ID));
			friend.setId(id);
			mCursor.close();
			return true;
		} else {
			mCursor.close();
			return false;
		}
	}

	public void update(FriendPO friend) {
		ContentValues cv = new ContentValues();
		// cv.put(KEY_ACCOUNT_ID, friend.getAccountId());
		// cv.put(KEY_FRIEND_ID, friend.getFriendId());
		cv.put(KEY_NAME, friend.getName());
		cv.put(KEY_SEX, friend.getSex());
		cv.put(KEY_L_PHOTO, friend.getlPhoto());
		mDb.update(TABLE_NAME, cv, KEY_ID + "=?",
				new String[] { String.valueOf(friend.getId()) });
	}

	public void createOrUpdate(FriendPO friend) {
		if (isExist(friend)) {
			update(friend);
		} else {
			insert(friend);
		}
	}
}
