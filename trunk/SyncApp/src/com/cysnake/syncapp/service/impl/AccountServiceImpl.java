package com.cysnake.syncapp.service.impl;

import com.cysnake.syncapp.dao.AccountDao;

import android.content.Context;

public class AccountServiceImpl extends BaseService {

	protected AccountDao accountDao;

	public AccountServiceImpl(Context context) {
		super(context);
		accountDao = new AccountDao(context);
	}

}
