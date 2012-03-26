package com.cysnake.syncapp.act;

import com.cysnake.syncapp.adapter.GridAccountAdapter;
import com.cysnake.syncapp.dao.AccountDao;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

public class AccountFragment extends Fragment {

	Button newAccountButton;
	GridView accountInfoGirdView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_account, container,
				false);
		newAccountButton = (Button) root
				.findViewById(R.id.account_bottom_button_newAccount);
		newAccountButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						NewAccountDialog.class);
				startActivity(intent);

			}
		});

		accountInfoGirdView = (GridView) root
				.findViewById(R.id.account_body_grid);
		AccountDao accountDao = new AccountDao(getActivity());
		accountDao.open();
		Cursor mCursor = accountDao.findAll();
		GridAccountAdapter gridAdapter = new GridAccountAdapter(getActivity(),
				R.layout.grid_cell_contact, mCursor, new String[] {
						AccountDao.KEY_H_PHOTO, AccountDao.KEY_NAME },
				new int[] { R.id.grid_cell_imageview_photo,
						R.id.grid_cell_TextView_name });
		accountInfoGirdView.setAdapter(gridAdapter);
		return root;

	}

}
