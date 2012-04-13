package com.cysnake.syncapp.act;

import com.cysnake.syncapp.adapter.GridFriendsAdapter;
import com.cysnake.syncapp.dao.FriendsDao;
import com.cysnake.syncapp.service.impl.FriendsService;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

public class FriendsAct extends Activity {
	private static final String TAG = "FriendsAct";
	GridView friendsGridView;
	FriendsService friendsService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		Intent intent = getIntent();
		String id = intent.getStringExtra("account_id");
		friendsService = new FriendsService(this);
		friendsService.getAllFriendsInfoFromNet(Integer.parseInt(id));
		init();
	}

	private void init() {
		friendsGridView = (GridView) findViewById(R.id.freinds_body_grid);
		FriendsDao friendsDao = new FriendsDao(this);
		friendsDao.open();
		Cursor mCursor = friendsDao.findAll();
		GridFriendsAdapter gridAdapter = new GridFriendsAdapter(this,
				R.layout.grid_cell_contact, mCursor, new String[] {
						FriendsDao.KEY_L_PHOTO, FriendsDao.KEY_NAME },
				new int[] { R.id.grid_cell_imageview_photo,
						R.id.grid_cell_TextView_name });
		friendsGridView.setAdapter(gridAdapter);
	}

}
