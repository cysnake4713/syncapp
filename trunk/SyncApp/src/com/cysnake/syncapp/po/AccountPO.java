package com.cysnake.syncapp.po;

public class AccountPO {
	private int id;
	private String type;
	private String name;
	private byte[] lPhoto;
	private byte[] hPhoto;
	private long userId;
	private AccountConfigPO config;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getlPhoto() {
		return lPhoto;
	}

	public void setlPhoto(byte[] lPhoto) {
		this.lPhoto = lPhoto;
	}

	public byte[] gethPhoto() {
		return hPhoto;
	}

	public void sethPhoto(byte[] hPhoto) {
		this.hPhoto = hPhoto;
	}

	public AccountConfigPO getConfig() {
		return config;
	}

	public void setConfig(AccountConfigPO config) {
		this.config = config;
	}

}
