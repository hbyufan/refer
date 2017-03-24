package com.hjz.refer.common.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface CommonReferService {

	/**
	 * 根据参照id得到
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	JSONObject getReferEntity(String valueId, String refercode);

	/**
	 * 
	 * @param valueIds
	 * @param referCode
	 * @return
	 */
	JSONArray getReferEntityLst(List<String> valueIds, String referCode);

}
