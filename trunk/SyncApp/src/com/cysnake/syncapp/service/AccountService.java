package com.cysnake.syncapp.service;

import com.cysnake.syncapp.po.AccountPO;

public interface AccountService {
	//FIXME maybe this interface isn't need 
	public AccountPO saveAccount();
	public void init();
}
