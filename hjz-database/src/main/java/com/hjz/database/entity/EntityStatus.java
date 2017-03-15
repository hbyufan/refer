package com.hjz.database.entity;

public enum EntityStatus {
	/**
	 * 初始状态
	 */
	INIT("I"),
	/**
	 * 新增状态
	 */
	ADD("A"),
	/**
	 * 更新状态
	 */
	UPDATE("U"),
	/**
	 * 删除状态（标记删除）
	 */
	DEL("F"),
	/**
	 * 删除状态（物理删除）
	 */
	DELETE("D");

	private String status;

	private EntityStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return String.valueOf(this.status);
	}
}