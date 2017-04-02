package com.hjz.bo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.hjz.refer.annotation.ReferDeserialTransfer;
import com.hjz.refer.annotation.ReferSerialTransfer;

public class PersonBO implements Serializable{

	private static final long serialVersionUID = 3555209139853883996L;

	private String id;
	
	private int dr;

	private Timestamp creationtime;
	
	private String name;
	
	private int age;
	
	private Timestamp lastmodifiedtime;
	
	private String code;
	
	public int getDr() {
		return dr;
	}

	public void setDr(int dr) {
		this.dr = dr;
	}
	
	@ReferSerialTransfer
	public String getId() {
		return id;
	}
	
	@ReferDeserialTransfer
	public void setId(String id) {
		this.id = id;
	}

	public Timestamp getCreationtime() {
		return creationtime;
	}

	public void setCreationtime(Timestamp creationtime) {
		this.creationtime = creationtime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Timestamp getLastmodifiedtime() {
		return lastmodifiedtime;
	}

	public void setLastmodifiedtime(Timestamp lastmodifiedtime) {
		this.lastmodifiedtime = lastmodifiedtime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
