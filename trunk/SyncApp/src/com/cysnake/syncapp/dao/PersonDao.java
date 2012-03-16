package com.cysnake.syncapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap.CompressFormat;

import com.cysnake.syncapp.po.PersonPO;
import com.cysnake.syncapp.tools.CommonUtils;

public class PersonDao extends CommonDao {

	public static final String TABLE_NAME = "PERSON";
	public static final String KEY_ID = "_ID";
	public static final String KEY_CONTACT_ID = "CONTACT_ID";
	public static final String KEY_NAME = "NAME";
	public static final String KEY_L_PHOTO = "L_PHOTO";
	public static final String KEY_H_PHOTO = "H_PHOTO";

	public PersonDao(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	public Cursor findAll() {
		Cursor mCursor;
		mCursor = mDb.rawQuery("SELECT _ID as _id ,NAME ,L_PHOTO FROM PERSON",
				null);
		return mCursor;
	}

	public long insert(PersonPO person) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_CONTACT_ID, person.getContactId());
		cv.put(KEY_NAME, person.getName());
		cv.put(KEY_L_PHOTO, person.getlPhoto());
		return mDb.insert(TABLE_NAME, KEY_L_PHOTO, cv);
	}

	public boolean isExist(PersonPO person) {
		Cursor mCursor;
		mCursor = mDb.query(true, TABLE_NAME, new String[] { KEY_ID },
				KEY_CONTACT_ID + " = " + person.getContactId() + "", null,
				null, null, null, null);
		if (mCursor.getCount() > 0) {
			mCursor.moveToNext();
			int id = mCursor.getInt(mCursor.getColumnIndex(KEY_ID));
			person.setId(id);
			mCursor.close();
			return true;
		} else {
			mCursor.close();
			return false;
		}
	}

	public void update(PersonPO person) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, person.getName());
		cv.put(KEY_L_PHOTO, person.getlPhoto());
		mDb.update(TABLE_NAME, cv, KEY_ID + "=?",
				new String[] { String.valueOf(person.getId()) });
	}
}
