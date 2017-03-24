package com.hjz.md.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.hjz.database.entity.SuperEntity;


@Entity
@Table(name = "md_refer")
public class MdReferEntity extends SuperEntity {
	private static final long serialVersionUID = 1815176839320027983L;
	
	//refer_id
	private String referId;
	//refer_code
	private String referCode;
	//id_column
	private String idColumn;
	//code_column
	private String codeColumn;
	//name_column
	private String nameColumn;
	//entity_id
	private String entityId;
	//bean_name
	private String beanName;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "refer_id", unique = true, nullable = false, length = 32)
	public String getReferId() {
		return referId;
	}
	public void setReferId(String referId) {
		this.referId = referId;
	}
	@Column(name = "refer_code")
	public String getReferCode() {
		return referCode;
	}
	public void setReferCode(String referCode) {
		this.referCode = referCode;
	}
	@Column(name = "id_column")
	public String getIdColumn() {
		return idColumn;
	}
	public void setIdColumn(String idColumn) {
		this.idColumn = idColumn;
	}
	@Column(name = "code_column")
	public String getCodeColumn() {
		return codeColumn;
	}
	public void setCodeColumn(String codeColumn) {
		this.codeColumn = codeColumn;
	}
	@Column(name = "name_column")
	public String getNameColumn() {
		return nameColumn;
	}
	public void setNameColumn(String nameColumn) {
		this.nameColumn = nameColumn;
	}
	@Column(name = "entity_id")
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	@Column(name = "bean_name")
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
}
