package com.cysnake.syncapp.act;

import java.util.ArrayList;
import java.util.HashMap;

import com.cysnake.syncapp.adapter.GridContactAdapter;
import com.cysnake.syncapp.dao.PersonDao;

import android.app.Activity;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class ContactAct extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		GridView grid = (GridView) findViewById(R.id.homepage_body_grid);
		PersonDao personDao = new PersonDao(this);
		personDao.open();
		Cursor mCurosr = personDao.findAll();
		// personDao.close();
		GridContactAdapter gridAdapter = new GridContactAdapter(this,
				R.layout.grid_cell_contact, mCurosr, new String[] {
						PersonDao.KEY_L_PHOTO, PersonDao.KEY_NAME }, new int[] {
						R.id.grid_cell_imageview_photo,
						R.id.grid_cell_TextView_name });
		grid.setAdapter(gridAdapter);
	}

	private ArrayList<HashMap<String, String>> getData() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 11; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", "asd");
			list.add(map);
		}

		return list;
	}
}
