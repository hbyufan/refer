package com.hjz.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * support业务支撑组件
 */
@Component
public class SupportUrlconstants {
	/**
	 * 参照保存url
	 */
	public final static String RERERBASE_SAVE_URL="mdrefer/saverefer";
	/**
	 * 参照get
	 */
	public final static String RERERBASE_GET_URL="mdrefer/getrefer";
	
	@Value("${support.http.url:http://127.0.0.1:9002/hjz-support/}")
	private String supportBaseUrl;

	public String getSupportBaseUrl() {
		return supportBaseUrl;
	}

	public void setSupportBaseUrl(String supportBaseUrl) {
		this.supportBaseUrl = supportBaseUrl;
	}
	
}
