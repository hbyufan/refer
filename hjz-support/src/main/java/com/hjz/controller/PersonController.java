package com.hjz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hjz.controller.bo.PersonBO;
import com.hjz.response.ObjectResponse;
import com.hjz.response.ReturnCode;
import com.hjz.service.IPersonService;

@Controller
@RequestMapping("refer")
public class PersonController {
	
	@Autowired
	private IPersonService personService;
	
	public ObjectResponse<List<PersonBO>> findAllPersons() {
		ObjectResponse<List<PersonBO>> result = new ObjectResponse<>();
		List<PersonBO> bos = personService.findAllPersons();
		result.setData(bos);
		result.setCode(ReturnCode.SUCCESS);
		result.setMsg("获取人员信息成功");
		return result;
	}
}
