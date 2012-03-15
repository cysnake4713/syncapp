package com.cysnake.syncapp.po;

public class PersonPO {

	private int id;
	private int contactId;
	private String name;
	private byte[] lPhoto;
	private byte[] HPhoto;

	public PersonPO(ContactPO contact) {
		contactId = contact.getId();
		name = contact.getName();
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

	public byte[] getlPhoto() {
		return lPhoto;
	}

	public void setlPhoto(byte[] lPhoto) {
		this.lPhoto = lPhoto;
	}

	public byte[] getHPhoto() {
		return HPhoto;
	}

	public void setHPhoto(byte[] hPhoto) {
		HPhoto = hPhoto;
	}

}
