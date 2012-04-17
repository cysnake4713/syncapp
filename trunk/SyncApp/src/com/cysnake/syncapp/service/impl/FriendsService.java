package com.cysnake.syncapp.service.impl;

import java.util.ArrayList;

import com.cysnake.syncapp.dao.FriendsDao;
import com.cysnake.syncapp.po.AccountPO;
import com.cysnake.syncapp.po.FriendPO;
import com.cysnake.syncapp.tools.CommonUtils;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.exception.RenrenException;
import com.renren.api.connect.android.friends.FriendsGetFriendsRequestParam;
import com.renren.api.connect.android.friends.FriendsGetFriendsResponseBean;
import com.renren.api.connect.android.friends.FriendsGetFriendsResponseBean.Friend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class FriendsService extends BaseService {

	private FriendsDao friendsDao;
	private RenrenAccountServiceImpl renrenService;

	public FriendsService(Context ctx) {
		super(ctx);
		friendsDao = new FriendsDao(ctx);
		renrenService = new RenrenAccountServiceImpl(ctx);
	}

	public void getAllFriendsInfoFromNet(int id) {
		renrenService.initRenren(id);
		FriendsGetFriendsRequestParam param = new FriendsGetFriendsRequestParam();
		// TODO finish the renren friend import system
		try {
			FriendsGetFriendsResponseBean friends = renrenService.getRenren()
					.getFriends(param);
			ArrayList<Friend> friendList = friends.getFriendList();
			// TODO update the way download headphoto and reset to load all
			// friends
			for (int i = 0; i < 15; i++) {
				Friend friend = friendList.get(i);
				Bitmap photoByNet = CommonUtils.getPhotoByNet(friend
						.getHeadurl());
				FriendPO friendPO = new FriendPO();
				friendPO.setlPhoto(CommonUtils.getBitePhoto(photoByNet,
						CompressFormat.JPEG, 100));
				friendPO.setFriendId(friend.getUid());
				friendPO.setName(friend.getName());
				friendPO.setAccountId(id);
				friendsDao.open();
				friendsDao.createOrUpdate(friendPO);
				friendsDao.close();
			}
			// for (Friend friend : friendList) {
			// Bitmap photoByNet = CommonUtils.getPhotoByNet(friend
			// .getHeadurl());
			// FriendPO friendPO = new FriendPO();
			// friendPO.setlPhoto(CommonUtils.getBitePhoto(photoByNet,
			// CompressFormat.JPEG, 100));
			// friendPO.setFriendId((int) friend.getUid());
			// friendPO.setName(friend.getName());
			// friendPO.setAccountId(id);
			//
			// }
		} catch (RenrenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
