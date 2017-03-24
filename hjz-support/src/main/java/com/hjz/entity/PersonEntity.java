package com.hjz.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.hjz.database.entity.SuperEntity;
import com.hjz.refer.annotation.Refer;

@Refer(id="id",code="code",name="name")
@Entity
@Table(name = "person")
public class PersonEntity extends SuperEntity{
	private static final long serialVersionUID = -4713417642252641105L;

	public String getId() {
		return id;
	}

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
	
	@Id
    @GenericGenerator(name="uuid", strategy="uuid")
    @GeneratedValue(generator="uuid")
	@Column(name="id")
	private String id;
	@Column(name="creationtime")
	private Timestamp creationtime;
	@Column(name="name")
	private String name;
	@Column(name="age")
	private int age;
	@Column(name="lastmodifiedtime")
	private Timestamp lastmodifiedtime;
	@Column(name="code")
	private String code;
}
