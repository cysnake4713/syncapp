package com.cysnake.syncapp.adapter;

import com.cysnake.syncapp.act.R;
import com.cysnake.syncapp.dao.AccountDao;
import com.cysnake.syncapp.dao.FriendsDao;
import com.cysnake.syncapp.tools.CommonUtils;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class GridFriendsAdapter extends SimpleCursorAdapter {

	public GridFriendsAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ImageView photo = (ImageView) view
				.findViewById(R.id.grid_cell_imageview_photo);
		TextView name = (TextView) view
				.findViewById(R.id.grid_cell_TextView_name);
		byte[] photoT = cursor.getBlob(cursor
				.getColumnIndex(FriendsDao.KEY_L_PHOTO));
		String nameT = cursor.getString(cursor
				.getColumnIndex(FriendsDao.KEY_NAME));
		if (photoT != null) {
			photo.setImageBitmap(CommonUtils.getBitmap(photoT));
		} else {
			photo.setImageResource(android.R.drawable.star_on);
		}
		name.setText(nameT);
	}

}
