package com.hjz.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjz.common.service.CommonReferService;


/**
 * id转换code，name的托底rest服务
 * 
 *
 */
@Controller
@RequestMapping(value = "commonrefer")
public class CommonReferController {

	@Autowired
	private CommonReferService commonReferService;

	@ResponseBody
	@RequestMapping(value = "getrefervalue")
	public JSONObject getCommonReferData(@RequestParam String valueId, @RequestParam String referCode) {		
		return commonReferService.getReferEntity(valueId, referCode);
	}
	
	@ResponseBody
	@RequestMapping(value = "getrefervalueLst")
	public JSONArray getCommonReferData(@RequestParam List<String> valueIds, @RequestParam String referCode) {		
		return commonReferService.getReferEntityLst(valueIds, referCode);
	}
	
}
