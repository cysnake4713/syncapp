package com.cysnake.syncapp.po;

import android.graphics.Bitmap;

public class PersonPO {
	
	
	public PersonPO(ContactPO contact) {
		contactId=contact.getId();
		name=contact.getName();
		lPhoto=contact.getPhoto();
	}

	public PersonPO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bitmap getlPhoto() {
		return lPhoto;
	}

	public void setlPhoto(Bitmap lPhoto) {
		this.lPhoto = lPhoto;
	}

	public Bitmap getHPhoto() {
		return HPhoto;
	}

	public void setHPhoto(Bitmap hPhoto) {
		HPhoto = hPhoto;
	}

	private int id;
	private int contactId;
	private String name;
	private Bitmap lPhoto;
	private Bitmap HPhoto;

}
