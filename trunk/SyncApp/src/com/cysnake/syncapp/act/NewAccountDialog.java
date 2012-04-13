package com.cysnake.syncapp.act;

import com.cysnake.syncapp.service.impl.RenrenAccountServiceImpl;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.view.RenrenAuthListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NewAccountDialog extends Activity {

	private Button renrenButton;
	private Handler handler;
	private RenrenAccountServiceImpl renrenService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_newaccount);
		setTitle(R.string.new_account_title);
		init();
	}

	private void init() {
		handler = new Handler();
		initRenren();

	}

	private void initRenren() {
		renrenService = new RenrenAccountServiceImpl(this);
		renrenService.init();
		final RenrenAuthListener renAuthLtr = new RenrenAuthListener() {

			public void onRenrenAuthError(RenrenAuthError renrenAuthError) {

				handler.post(new Runnable() {

					public void run() {
						Toast.makeText(
								NewAccountDialog.this,
								NewAccountDialog.this
										.getString(R.string.auth_fail),
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			public void onComplete(Bundle values) {
				renrenService.saveAccount();
				handler.post(new Runnable() {

					public void run() {
						Toast.makeText(
								NewAccountDialog.this,
								NewAccountDialog.this
										.getString(R.string.auth_success),
								Toast.LENGTH_SHORT).show();
					}
				});
				//FIXME it seems the order and the cursor refresh problem still 
				Intent intent=new Intent(NewAccountDialog.this,MainViewPager.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				// TODO call friendsService.getAllFriendsInfo(AccountPO )

				// Intent intent = new Intent(NewAccountDialog.this,
				// FriendsAct.class);
				// startActivity(intent);

			}

			public void onCancelLogin() {
			}

			public void onCancelAuth(Bundle values) {
			}
		};

		renrenButton = (Button) findViewById(R.id.new_account_renren_button);
		renrenButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				renrenService.getRenren().authorize(NewAccountDialog.this,
						renAuthLtr);
			}
		});
	}
}
