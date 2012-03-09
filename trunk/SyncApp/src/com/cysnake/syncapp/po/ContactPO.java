package com.cysnake.syncapp.po;

import java.util.Date;

import android.graphics.Bitmap;

public class ContactPO {


	private int id;
	private int rawId;
	private String name;
	private Date birthday;
	private Bitmap photo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRawId() {
		return rawId;
	}

	public void setRawId(int rawId) {
		this.rawId = rawId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "id=" + id + " rawId="+rawId+" name=" + name + " birthday=" + birthday;
	}
}
