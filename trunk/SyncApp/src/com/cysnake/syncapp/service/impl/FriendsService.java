package com.cysnake.syncapp.service.impl;

import com.cysnake.syncapp.dao.FriendsDao;
import com.cysnake.syncapp.po.AccountPO;
import com.renren.api.connect.android.friends.FriendsGetFriendsRequestParam;

import android.content.Context;

public class FriendsService extends BaseService {

	private FriendsDao friendsDao;
	private RenrenAccountServiceImpl renrenService;

	public FriendsService(Context ctx) {
		super(ctx);
		friendsDao = new FriendsDao(ctx);
		renrenService=new RenrenAccountServiceImpl(ctx);
	}

	public void getAllFriendsInfoFromNet(int id) {
		renrenService.initRenren(id);
		FriendsGetFriendsRequestParam param=new FriendsGetFriendsRequestParam();
		param.
		renrenService.getRenren().getFriends(param);
	}

}
