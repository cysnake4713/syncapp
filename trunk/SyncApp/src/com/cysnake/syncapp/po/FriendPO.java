package com.cysnake.syncapp.po;

public class FriendPO {

	private int id;
	private int accountId;
	private int friendId;
	private String name;
	private int sex;
	private byte[] lPhoto;
	private byte[] hPhoto;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getFriendId() {
		return friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
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

}
