package com.hjz.database.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@SuppressWarnings("serial")
public class SuperEntity implements Serializable {
	/**
	 * 是否删除. 0是未删除，1是标记删除.
	 */
	protected int dr = IS_DELETE_NO;

	public final static int IS_DELETE_YES = 1;

	public final static int IS_DELETE_NO =0;


	@Column(name = "DR")
	public int getDr() {
		return dr;
	}

	public void setDr(int dr) {
		this.dr = dr;
	}
	

	public Object getAttributeValue(String name) {
		return BeanHelper.getProperty(this, name);
	}

	public void setAttributeValue(String name, Object value) {
		String key = name;
		BeanHelper.setProperty(this, key, value);
	}

}
