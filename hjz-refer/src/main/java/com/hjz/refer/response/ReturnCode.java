package com.hjz.refer.response;

/**
 * http请求返回码枚举
 */
public enum ReturnCode {

	/**
	 * 返回成功
	 */
	SUCCESS("success"),
	/**
	 * 返回失败
	 */
	FAILURE("failure"),
	/**
	 * 数据重复
	 */
	REPEAT("repeat");

	String value;

	ReturnCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
