package com.hjz.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjz.constants.SupportUrlconstants;
import com.hjz.context.ContextUtils;
import com.hjz.cache.strategy.JedisCacheTool;

/**
 * 根据redis和http rest 托底获取参照实体信息
 * 
 * @author hupeng 2016年12月30日
 *
 */
public class ReferObjectUtil {

	/**
	 * 
	 * @param valueId
	 *            单个档案直接传:id，多个id传: id1,id2,id3
	 * @param referCode
	 * @return
	 */
	public static JSONArray getReferEntityValue(List<String> valueIds, String referCode) {

		JSONArray jsonArray = null;
		if (valueIds != null && valueIds.size() > 0) {
			// 走redis
			List<Object> objKeys = new ArrayList<>();
			for (String id : valueIds) {
				String key = referCode + id;
				objKeys.add(key);
			}
			JedisCacheTool cacheTool = ContextUtils.getBean(JedisCacheTool.class);
			Map<Object, Object> resultMap = cacheTool.getBatch(objKeys);
			// redis返回数量和参数数量一致
			if (resultMap.size() == valueIds.size()) {
				jsonArray = new JSONArray();
				for (String id : valueIds) {
					String key = referCode + id;
					JSONObject jsonObj = (JSONObject) resultMap.get(key);
					jsonArray.add(jsonObj);
				}
			} else {//走数据库
//				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
				SupportUrlconstants supportUrlconstants = ContextUtils.getBean(SupportUrlconstants.class);
				Map<String, String> params = new HashMap<>();
				params.put("refercode", referCode);
				//下面逻辑需要修改
				Map<String, Object> valueparams = new HashMap<>();
				StringBuffer valueIdBuffer = new StringBuffer();
				for (String id : valueIds) {
					if (id != null)
						valueIdBuffer.append(id + ",");
				}
				valueIdBuffer.deleteCharAt(valueIdBuffer.lastIndexOf(","));
				valueparams.put("valueIds", valueIdBuffer.toString());
				valueparams.put("referCode", referCode);
				String url = supportUrlconstants.getSupportBaseUrl()
						+ "/commonrefer/getrefervalueLst";
				RestTemplate restTemplate = new RestTemplate();
				String jsonbackstr = restTemplate.getForObject(url, String.class, valueparams);
				jsonArray = JSON.parseArray(jsonbackstr);
			}
		}
		return jsonArray;
	}
}
