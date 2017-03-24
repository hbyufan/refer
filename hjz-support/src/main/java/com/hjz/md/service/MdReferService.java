package com.hjz.md.service;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.hjz.md.bo.MdReferBO;

@Controller
@RequestMapping(value = "commonrefer")
public interface MdReferService {

	JSONObject  getReferInfo(String refercode);
	
	
	void saveReferInfo(List<MdReferBO> boLst);
	
}
