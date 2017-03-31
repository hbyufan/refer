package com.hjz.md.service;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hjz.md.bo.MdReferBO;

@Controller
@RequestMapping(value = "commonrefer")
public interface MdReferService {

	MdReferBO getReferInfo(String refercode);
	
	void saveReferInfo(List<MdReferBO> boLst);
	
}
