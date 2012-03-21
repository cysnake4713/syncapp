package com.cysnake.syncapp.act;

import java.util.ArrayList;

import com.cysnake.syncapp.dao.AccountDao;
import com.cysnake.syncapp.po.AccountConfigPO;
import com.cysnake.syncapp.po.AccountPO;
import com.cysnake.syncapp.tools.CommonUtils;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.exception.RenrenException;
import com.renren.api.connect.android.users.UserInfo;
import com.renren.api.connect.android.users.UsersGetInfoRequestParam;
import com.renren.api.connect.android.users.UsersGetInfoResponseBean;
import com.renren.api.connect.android.view.RenrenAuthListener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NewAccountDialog extends Activity {

	Button renrenButton;
	Renren renren;
	private Handler handler;

	private static final String RENREN_SDK_CONFIG = "renren_sdk_config";

	private static final String RENREN_SDK_CONFIG_PROP_ACCESS_TOKEN = "renren_sdk_config_prop_access_token";

	private static final String RENREN_SDK_CONFIG_PROP_SESSION_KEY = "renren_sdk_config_prop_session_key";

	private static final String RENREN_SDK_CONFIG_PROP_SESSION_SECRET = "renren_sdk_config_prop_session_secret";

	private static final String RENREN_SDK_CONFIG_PROP_CREATE_TIME = "renren_sdk_config_prop_create_time";

	private static final String RENREN_SDK_CONFIG_PROP_SESSION_CREATE_TIME = "renren_sdk_config_prop_session_create_time";

	private static final String RENREN_SDK_CONFIG_PROP_EXPIRE_SECONDS = "renren_sdk_config_prop_expire_secends";

	private static final String RENREN_SDK_CONFIG_PROP_USER_ID = "renren_sdk_config_prop_user_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_newaccount);
		setTitle(R.string.new_account_title);

		init();

	}

	private void saveAccount() {
		AccountDao accountDao = new AccountDao(this);
		AccountPO account = new AccountPO();
		AccountConfigPO aConfig = new AccountConfigPO();
		SharedPreferences sp = NewAccountDialog.this.getSharedPreferences(
				RENREN_SDK_CONFIG, Context.MODE_PRIVATE);
		aConfig.setSessionKey(sp.getString(RENREN_SDK_CONFIG_PROP_SESSION_KEY,
				null));
		aConfig.setSessionSecret(sp.getString(
				RENREN_SDK_CONFIG_PROP_SESSION_SECRET, null));
		aConfig.setExpireSeconds(sp.getLong(
				RENREN_SDK_CONFIG_PROP_EXPIRE_SECONDS, 0));
		aConfig.setCreateTime(sp.getLong(RENREN_SDK_CONFIG_PROP_CREATE_TIME, 0));
		aConfig.setAccessToken(sp.getString(
				RENREN_SDK_CONFIG_PROP_ACCESS_TOKEN, null));
		aConfig.setSessionCreateTime(sp.getLong(
				RENREN_SDK_CONFIG_PROP_SESSION_CREATE_TIME, 0));

		account.setUserId(sp.getLong(RENREN_SDK_CONFIG_PROP_USER_ID, 0));
		account.setType("R");

		account.setConfig(aConfig);
		UsersGetInfoRequestParam param = new UsersGetInfoRequestParam(
				new String[] { String.valueOf(account.getUserId()) });

		try {
			UsersGetInfoResponseBean usersInfoList = renren.getUsersInfo(param);
			ArrayList<UserInfo> usersInfo = usersInfoList.getUsersInfo();
			UserInfo myInfo = usersInfo.get(0);
			account.setName(myInfo.getName());
			Bitmap photoByNet = CommonUtils.getPhotoByNet(myInfo.getHeadurl());
			account.sethPhoto(CommonUtils.getBitePhoto(photoByNet,
					CompressFormat.JPEG, 100));

			accountDao.open();
			accountDao.createOrUpdate(account);
		} catch (RenrenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			accountDao.close();
		}
	}

	private void initRenren() {
		String apiKey = getString(R.string.renren_api_key);
		String secret = getString(R.string.renren_secret_key);
		String appId = getString(R.string.renren_app_id);
		renren = new Renren(apiKey, secret, appId, this);
		renren.logout(this);
	}

	private void init() {
		handler = new Handler();
		initRenren();
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
				saveAccount();
				handler.post(new Runnable() {

					public void run() {
						Toast.makeText(
								NewAccountDialog.this,
								NewAccountDialog.this
										.getString(R.string.auth_success),
								Toast.LENGTH_SHORT).show();
					}
				});

			}

			public void onCancelLogin() {
			}

			public void onCancelAuth(Bundle values) {
			}
		};

		renrenButton = (Button) findViewById(R.id.new_account_renren_button);
		renrenButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				renren.authorize(NewAccountDialog.this, renAuthLtr);
			}
		});

	}
}
