package com.cysnake.syncapp.act;

import com.cysnake.syncapp.adapter.GridAccountAdapter;
import com.cysnake.syncapp.dao.AccountDao;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class AccountFragment extends Fragment {
	private static final String TAG = "AccountFragment";

	Button newAccountButton;
	GridView accountInfoGridView;
	AccountDao accountDao;
	Cursor mCursor;
	View tempView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_account, container,
				false);
		// register button
		newAccountButton = (Button) root
				.findViewById(R.id.account_bottom_button_newAccount);
		newAccountButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						NewAccountDialog.class);
				startActivity(intent);

			}
		});

		// register gridView
		accountInfoGridView = (GridView) root
				.findViewById(R.id.account_body_grid);

		return root;

	}

	@Override
	public void onStart() {
		super.onStart();
		accountDao = new AccountDao(getActivity());
		accountDao.open();
		mCursor = accountDao.findAll();
		GridAccountAdapter gridAdapter = new GridAccountAdapter(getActivity(),
				R.layout.grid_cell_contact, mCursor, new String[] {
						AccountDao.KEY_H_PHOTO, AccountDao.KEY_NAME },
				new int[] { R.id.grid_cell_imageview_photo,
						R.id.grid_cell_TextView_name });
		accountInfoGridView.setAdapter(gridAdapter);
		accountInfoGridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				getFriends(arg0.getContext(), view);
			}

		});
		accountInfoGridView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						MenuInflater inflater = getActivity().getMenuInflater();
						inflater.inflate(
								R.menu.menu_account_gridview_longclick, menu);
						tempView = v;

					}
				});

	}

	@Override
	public void onStop() {
		mCursor.close();
		accountDao.close();
		super.onStop();

	}

	private void getFriends(Context context, View view) {
		TextView idView = (TextView) view
				.findViewById(R.id.grid_cell_textview_id);
		Intent intent = new Intent(context, FriendsAct.class);
		intent.putExtra("account_id", idView.getText());
		startActivity(intent);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.account_gridview_contextmenu_friends:
			getFriends(tempView.getContext(), tempView);
			break;
		case R.id.account_gridview_contextmenu_detail:
			// TODO finish this account context menu
		default:
			break;
		}

		return super.onContextItemSelected(item);

	}
}
