package com.hjz.md.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.hjz.md.bo.MdReferBO;
import com.hjz.md.service.MdReferService;

@Controller
@RequestMapping(value = "mdrefer")
public class MdReferController {
	@Autowired
	private MdReferService mdReferService;

	@ResponseBody
	@RequestMapping(value = "getrefer")
	public JSONObject getReferBaseInfo(@RequestParam String refercode) {
		return mdReferService.getReferInfo(refercode);
	}

	@ResponseBody
	@RequestMapping(value = "saverefer")
	public void saveReferInfo(@RequestBody List<MdReferBO> boLst) {
		mdReferService.saveReferInfo(boLst);
	}

}
