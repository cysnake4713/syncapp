package com.cysnake.syncapp.act;

import com.cysnake.syncapp.adapter.GridContactAdapter;
import com.cysnake.syncapp.dao.PersonDao;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class ContactFragment extends Fragment {
	PersonDao personDao;
	Cursor mCurosr;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_contact, container,
				false);
		GridView grid = (GridView) root.findViewById(R.id.homepage_body_grid);
		personDao = new PersonDao(getActivity());
		personDao.open();
		mCurosr = personDao.findAll();
		// personDao.close();
		GridContactAdapter gridAdapter = new GridContactAdapter(getActivity(),
				R.layout.grid_cell_contact, mCurosr, new String[] {
						PersonDao.KEY_L_PHOTO, PersonDao.KEY_NAME }, new int[] {
						R.id.grid_cell_imageview_photo,
						R.id.grid_cell_TextView_name });
		grid.setAdapter(gridAdapter);
		return root;
	}

	@Override
	public void onStop() {
		mCurosr.close();
		personDao.close();
		super.onStop();
	}

}
