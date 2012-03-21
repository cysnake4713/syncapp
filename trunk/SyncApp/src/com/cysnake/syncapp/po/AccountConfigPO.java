package com.cysnake.syncapp.po;

public class AccountConfigPO {
	private int id;
	private String accessToken;
	private long createTime;
	private long sessionCreateTime;
	private String sessionKey;
	private String sessionSecret;
	private long expireSeconds;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getCreateTime() {
		return createTime;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public long getExpireSeconds() {
		return expireSeconds;
	}

	public void setExpireSeconds(long expireSeconds) {
		this.expireSeconds = expireSeconds;
	}

	public String getSessionSecret() {
		return sessionSecret;
	}

	public void setSessionSecret(String sessionSecret) {
		this.sessionSecret = sessionSecret;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getSessionCreateTime() {
		return sessionCreateTime;
	}

	public void setSessionCreateTime(long sessionCreateTime) {
		this.sessionCreateTime = sessionCreateTime;
	}

}
