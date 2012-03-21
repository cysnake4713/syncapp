package com.cysnake.syncapp.act;

import com.cysnake.syncapp.adapter.GridAccountAdapter;
import com.cysnake.syncapp.adapter.GridContactAdapter;
import com.cysnake.syncapp.dao.AccountDao;
import com.cysnake.syncapp.dao.PersonDao;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

public class AccountAct extends Activity {

	Button newAccountButton;
	GridView accountInfoGirdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		init();
	}

	private void init() {
		newAccountButton = (Button) findViewById(R.id.account_bottom_button_newAccount);
		newAccountButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(AccountAct.this,
						NewAccountDialog.class);
				startActivity(intent);

			}
		});
		accountInfoGirdView = (GridView) findViewById(R.id.account_body_grid);
		AccountDao accountDao = new AccountDao(this);
		accountDao.open();
		Cursor mCursor = accountDao.findAll();
		GridAccountAdapter gridAdapter = new GridAccountAdapter(this,
				R.layout.grid_cell_contact, mCursor, new String[] {
						AccountDao.KEY_H_PHOTO, AccountDao.KEY_NAME },
				new int[] { R.id.grid_cell_imageview_photo,
						R.id.grid_cell_TextView_name });
		accountInfoGirdView.setAdapter(gridAdapter);
	}
}
