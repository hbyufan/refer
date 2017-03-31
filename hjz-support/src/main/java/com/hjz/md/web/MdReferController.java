package com.hjz.md.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hjz.md.bo.MdReferBO;
import com.hjz.md.service.MdReferService;
import com.hjz.response.ObjectResponse;
import com.hjz.response.ReturnCode;

@Controller
@RequestMapping(value = "mdrefer")
public class MdReferController {
	@Autowired
	private MdReferService mdReferService;

	@ResponseBody
	@RequestMapping(value = "getrefer")
	public ObjectResponse<MdReferBO> getReferBaseInfo(@RequestParam String refercode) {
		MdReferBO vo = mdReferService.getReferInfo(refercode);
		ObjectResponse<MdReferBO> result = new ObjectResponse<>();
		result.setCode(ReturnCode.SUCCESS);
		result.setMsg("refer 获取成功");
		result.setData(vo);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "saverefer")
	public void saveReferInfo(@RequestBody List<MdReferBO> boLst) {
		mdReferService.saveReferInfo(boLst);
	}

}
