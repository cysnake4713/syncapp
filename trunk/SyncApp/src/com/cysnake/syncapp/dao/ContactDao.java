package com.cysnake.syncapp.dao;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.cysnake.syncapp.po.ContactPO;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class ContactDao {

	private Uri contactUri = Contacts.CONTENT_URI;
	private Uri rawContactUri = RawContacts.CONTENT_URI;
	private Activity activity;
	private ContentResolver resolver;

	public ContactDao(Activity act) {
		this.activity = act;
		resolver = act.getContentResolver();
	}

	public List<ContactPO> findAll() {
		List<ContactPO> list = new ArrayList<ContactPO>();

		Cursor cursor = resolver.query(contactUri, new String[] { Contacts._ID,
				Contacts.DISPLAY_NAME }, null, null, null);

		while (cursor.moveToNext()) {
			ContactPO contactPO = new ContactPO();
			String name = cursor.getString(cursor
					.getColumnIndex(Contacts.DISPLAY_NAME));
			int id = cursor.getInt(cursor.getColumnIndex(Contacts._ID));
			contactPO.setId(id);
			contactPO.setName(name);
			Cursor rawCursor = resolver.query(rawContactUri,
					new String[] { RawContacts._ID }, RawContacts.CONTACT_ID
							+ " =? and " + RawContacts.DELETED + "= 0",
					new String[] { String.valueOf(id) }, null);
			if (rawCursor.moveToFirst()) {
				contactPO.setRawId(rawCursor.getInt(rawCursor
						.getColumnIndex(RawContacts._ID)));
			}
			rawCursor.close();
			Log.d("ContactDao", "contact is " + contactPO);
			list.add(contactPO);
		}
		cursor.close();

		return list;
	}

	public ContactPO findByName(String name) {
		ContactPO contactPO = new ContactPO();
		Cursor cursor = resolver.query(contactUri, new String[] { Contacts._ID,
				Contacts.DISPLAY_NAME }, Contacts.DISPLAY_NAME + " = ? ",
				new String[] { name }, null);
		if (cursor.moveToFirst()) {
			int id = cursor.getInt(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			Cursor rawCursor = resolver.query(rawContactUri,
					new String[] { RawContacts._ID }, RawContacts.CONTACT_ID
							+ " =? and " + RawContacts.DELETED + "= 0",
					new String[] { String.valueOf(id) }, null);
			if (rawCursor.moveToFirst()) {
				contactPO.setRawId(rawCursor.getInt(rawCursor
						.getColumnIndex(RawContacts._ID)));
			}
			rawCursor.close();
			contactPO.setId(id);
			contactPO.setName(name);
		}
		cursor.close();
		return contactPO;
	}

	public void update(ContactPO contactPO) {
		ArrayList<ContentProviderOperation> list = new ArrayList<ContentProviderOperation>();

		if (contactPO.getId() > 0) {
			if (contactPO.getBirthday() != null) {
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String birthDay = format.format(contactPO.getBirthday());
				if (isBirthdayExist(contactPO)) {
					list.add(ContentProviderOperation
							.newUpdate(ContactsContract.Data.CONTENT_URI)
							.withSelection(
									ContactsContract.Data.CONTACT_ID
											+ "=?"
											+ " AND "
											+ ContactsContract.Data.MIMETYPE
											+ "=?"
											+ " AND "
											+ ContactsContract.CommonDataKinds.Event.TYPE
											+ "=?",
									new String[] {
											contactPO.getId() + "",
											ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
											String.valueOf(ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY) })
							.withValue(
									ContactsContract.CommonDataKinds.Event.DATA,
									birthDay).build());
					Log.d("ContactDao", "update update birthday for contact "
							+ contactPO);
				} else {
					list.add(ContentProviderOperation
							.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValue(ContactsContract.Data.RAW_CONTACT_ID,
									contactPO.getRawId())
							.withValue(
									ContactsContract.Data.MIMETYPE,
									ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
							.withValue(
									ContactsContract.CommonDataKinds.Event.TYPE,
									String.valueOf(ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY))
							.withValue(
									ContactsContract.CommonDataKinds.Event.DATA,
									birthDay).build());
					Log.d("ContactDao", "update insert birthday for contact "
							+ contactPO);
				}
			}
			if (contactPO.getPhoto() != null) {

				Bitmap photoMap = contactPO.getPhoto();
				ByteArrayOutputStream streamy = new ByteArrayOutputStream();
				photoMap.compress(CompressFormat.JPEG, 100, streamy);
				byte[] photo = streamy.toByteArray();
				if (isPhotoExist(contactPO)) {

					list.add(ContentProviderOperation
							.newUpdate(ContactsContract.Data.CONTENT_URI)
							.withSelection(
									ContactsContract.Data.CONTACT_ID + "=?"
											+ " AND "
											+ ContactsContract.Data.MIMETYPE
											+ "=?",
									new String[] {
											contactPO.getId() + "",
											ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE })
							.withValue(
									ContactsContract.CommonDataKinds.Photo.PHOTO,
									photo).build());
					Log.d("ContactDao", "update update photo for contact "
							+ contactPO);
				} else {
					list.add(ContentProviderOperation
							.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValue(ContactsContract.Data.RAW_CONTACT_ID,
									contactPO.getRawId())
							.withValue(
									ContactsContract.Data.MIMETYPE,
									ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
							.withValue(
									ContactsContract.CommonDataKinds.Photo.PHOTO,
									photo).build());
					Log.d("ContactDao", "update insert photo for contact "
							+ contactPO);
				}

			}
			try {
				resolver.applyBatch(ContactsContract.AUTHORITY, list);
			} catch (RemoteException e) {
				Log.e("ContactDao", "update:", e);
			} catch (OperationApplicationException e) {
				Log.e("ContactDao", "update:", e);
			}

		}

	}

	private boolean isPhotoExist(ContactPO contactPO) {
		boolean flag = false;
		Cursor cursor = resolver
				.query(ContactsContract.Data.CONTENT_URI,
						new String[] { ContactsContract.CommonDataKinds.Event.DATA },
						ContactsContract.Data.CONTACT_ID + "=?" + " AND "
								+ ContactsContract.Data.MIMETYPE + "=?",
						new String[] {
								contactPO.getId() + "",
								ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE },
						null);
		int count = cursor.getCount();
		if (count == 1) {
			flag = true;
		}
		cursor.close();
		Log.d("ContactDao", "image is exsit? " + flag);

		return flag;
	}

	private boolean isBirthdayExist(ContactPO contactPO) {
		boolean flag = false;
		Cursor cursor = resolver
				.query(ContactsContract.Data.CONTENT_URI,
						new String[] { ContactsContract.CommonDataKinds.Event.DATA },
						ContactsContract.Data.CONTACT_ID + "=?" + " AND "
								+ ContactsContract.Data.MIMETYPE + "=?"
								+ " AND "
								+ ContactsContract.CommonDataKinds.Event.TYPE
								+ "=?",
						new String[] {
								contactPO.getId() + "",
								ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
								String.valueOf(ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY) },
						null);
		int count = cursor.getCount();
		if (count == 1) {
			flag = true;
		}
		cursor.close();
		Log.d("ContactDao", "birthday is exsit? " + flag);

		return flag;
	}

	public int total() {
		int total = 0;
		this.resolver = activity.getContentResolver();
		Cursor cursor = resolver.query(contactUri, null, null, null, null);
		total = cursor.getCount();
		cursor.close();
		Log.d("ContactDao", "you got total:" + total);
		return total;
	}
}
