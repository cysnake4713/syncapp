package com.cysnake.syncapp.service.impl;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.cysnake.syncapp.act.R;
import com.cysnake.syncapp.po.AccountConfigPO;
import com.cysnake.syncapp.po.AccountPO;
import com.cysnake.syncapp.service.AccountService;
import com.cysnake.syncapp.tools.CommonUtils;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.exception.RenrenException;
import com.renren.api.connect.android.users.UserInfo;
import com.renren.api.connect.android.users.UsersGetInfoRequestParam;
import com.renren.api.connect.android.users.UsersGetInfoResponseBean;

public class RenrenAccountServiceImpl extends AccountServiceImpl implements
		AccountService {
	private static final String RENREN_SDK_CONFIG = "renren_sdk_config";
	private static final String RENREN_SDK_CONFIG_PROP_ACCESS_TOKEN = "renren_sdk_config_prop_access_token";
	private static final String RENREN_SDK_CONFIG_PROP_SESSION_KEY = "renren_sdk_config_prop_session_key";
	private static final String RENREN_SDK_CONFIG_PROP_SESSION_SECRET = "renren_sdk_config_prop_session_secret";
	private static final String RENREN_SDK_CONFIG_PROP_CREATE_TIME = "renren_sdk_config_prop_create_time";
	private static final String RENREN_SDK_CONFIG_PROP_SESSION_CREATE_TIME = "renren_sdk_config_prop_session_create_time";
	private static final String RENREN_SDK_CONFIG_PROP_EXPIRE_SECONDS = "renren_sdk_config_prop_expire_secends";
	private static final String RENREN_SDK_CONFIG_PROP_USER_ID = "renren_sdk_config_prop_user_id";

	private static Renren renren;
	
	public RenrenAccountServiceImpl(Context context) {
		super(context);
		String apiKey = ctx.getString(R.string.renren_api_key);
		String secret = ctx.getString(R.string.renren_secret_key);
		String appId = ctx.getString(R.string.renren_app_id);
		renren = new Renren(apiKey, secret, appId, ctx);
	}
	
	public void initRenren(int id){
		accountDao.open();
		AccountPO account = accountDao.find(id);
		AccountConfigPO config=account.getConfig();
		Editor edit = ctx.getSharedPreferences(RENREN_SDK_CONFIG,
				Context.MODE_PRIVATE).edit();
		edit.putString(RENREN_SDK_CONFIG_PROP_SESSION_KEY, config.getSessionKey());
		edit.putString(RENREN_SDK_CONFIG_PROP_SESSION_SECRET, config.getSessionSecret());
		edit.putLong(RENREN_SDK_CONFIG_PROP_EXPIRE_SECONDS, config.getExpireSeconds());
		edit.putLong(RENREN_SDK_CONFIG_PROP_CREATE_TIME, config.getCreateTime());
		edit.putString(RENREN_SDK_CONFIG_PROP_ACCESS_TOKEN, config.getAccessToken());
		edit.putLong(RENREN_SDK_CONFIG_PROP_SESSION_CREATE_TIME, config.getSessionCreateTime());
		edit.putLong(RENREN_SDK_CONFIG_PROP_USER_ID, account.getUserId());

		//TODO seems not very perfect need update 
		
	}

	public AccountPO saveAccount() {

		AccountPO account = new AccountPO();
		AccountConfigPO aConfig = new AccountConfigPO();
		SharedPreferences sp = ctx.getSharedPreferences(RENREN_SDK_CONFIG,
				Context.MODE_PRIVATE);
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
		return account;
	}

	public void init() {

		renren.logout(ctx);
	}

	public Renren getRenren() {
		return renren;
	}

}
