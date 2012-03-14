package com.cysnake.syncapp.act;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class ContactAct extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		GridView grid = (GridView) findViewById(R.id.homepage_body_grid);
		grid.setAdapter(new SimpleAdapter(this, getData(),
				R.xml.person_grid_cell, new String[] { "id" },
				new int[] { R.id.person_grid_cell_text }));
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
