package com.cysnake.syncapp.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AccountAct extends Activity {

	Button newAccountButton;

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
				Intent intent = new Intent(AccountAct.this, NewAccountAct.class);
				startActivity(intent);

			}
		});

	}
}
