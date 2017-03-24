package com.hjz.md.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class MdReferDao {
	protected EntityManager entityManager;

	@Autowired
	public MdReferDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public JSONObject getMdrefer(String refercode) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT r.refer_code,m.base_url,m.module_name from bd_refer_info b INNER JOIN bd_module m on b.group_id =m.module_id INNER JOIN md_refer r on b.ref_code=r.refer_code where r.refer_code=:refer_code  ");
		Query rquery = entityManager.createNativeQuery(sql.toString());
		rquery.setParameter("refer_code", refercode);
		List<Object> objs = rquery.getResultList();
		JSONObject jsonobj= new JSONObject();
		Object[] items =null;
		if (objs != null && objs.size() > 0) {
			 items = (Object[]) objs.get(0);
			 jsonobj.put("referCode", items[0]);
			 jsonobj.put("baseUrl", items[1]);
			 jsonobj.put("moduleName", items[2]);
		}
		return jsonobj;
	}
}
