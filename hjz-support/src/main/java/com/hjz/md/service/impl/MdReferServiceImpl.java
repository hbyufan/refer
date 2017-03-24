package com.hjz.md.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hjz.database.repository.EntityNativeQuery;
import com.hjz.md.bo.MdReferBO;
import com.hjz.md.entity.MdReferEntity;
import com.hjz.md.repository.MdReferBaseDao;
import com.hjz.md.repository.MdReferDao;
import com.hjz.md.service.MdReferService;

@Service
public class MdReferServiceImpl implements MdReferService {
	@Autowired
	private MdReferDao mdReferDao;
	@Autowired
	private MdReferBaseDao mdReferBaseDao;

	@Autowired
	private EntityNativeQuery<MdReferEntity> query;

	@Override
	public JSONObject getReferInfo(String refercode) {
		JSONObject jsonobj = mdReferDao.getMdrefer(refercode);
		return jsonobj;
	}

	@Override
	public void saveReferInfo(List<MdReferBO> boLst) {
		List<MdReferEntity> entityLst = new ArrayList<>();
		List<String> beanNameLst = new ArrayList<>();
		for (MdReferBO bo : boLst) {
			MdReferEntity entity = new MdReferEntity();
			if (StringUtils.isEmpty(bo.getReferCode())) {
				continue;
			}
			if (!StringUtils.isEmpty(bo.getBeanName()))
				beanNameLst.add(bo.getBeanName());
			BeanUtils.copyProperties(bo, entity);
			entityLst.add(entity);
		}
		Map<String, Object> params = new HashMap<>();
		if (beanNameLst.size() > 0) {
			params.put("beanName", beanNameLst);
			List<MdReferEntity> lst = query.query(MdReferEntity.class, " and  bean_name in (:beanName)", null, params);
			if (lst != null && lst.size() > 0) {
				Map<String, MdReferEntity> mdMap = new HashMap<>();
				for (MdReferEntity mdReferEntity : lst) {
					mdMap.put(mdReferEntity.getBeanName(), mdReferEntity);
				}
				// 有变动项目启动就会自动同步
				for (MdReferEntity mdReferEntity : entityLst) {
					if (mdMap.get(mdReferEntity.getBeanName()) != null)
						mdReferEntity.setReferId(mdMap.get(mdReferEntity.getBeanName()).getReferId());
				}
			}
			mdReferBaseDao.save(entityLst);
		}
	}

}
