package com.hjz.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjz.annotation.Refer;
import com.hjz.constants.ReferConstant;
import com.hjz.context.ContextUtils;
import com.hjz.database.entity.SuperEntity;
import com.hjz.database.repository.EntityNativeQuery;

@Service
public class CommonReferServiceImpl implements CommonReferService {

	@Autowired
	private EntityNativeQuery<? extends SuperEntity> query;

	@Override
	public JSONObject getReferEntity(String id, String refercode) {
		JSONObject json = null;
		Map<String, Object> beansWithAnnotationMap = ContextUtils.getApplicationContext()
				.getBeansWithAnnotation(Refer.class);
		for (String key : beansWithAnnotationMap.keySet()) {
			Object bean = beansWithAnnotationMap.get(key);
			Refer refer = bean.getClass().getAnnotation(Refer.class);
			if (refer != null && refer.referCode().equals(refercode)) {
				List<String> idLst = new ArrayList<>();
				idLst.add(id);
				List<SuperEntity> lst = (List<SuperEntity>) query
						.queryByPrimary((Class<? extends SuperEntity>) bean.getClass(), idLst, null);
				if (lst != null && lst.size() > 0) {
					SuperEntity t = lst.get(0);
					json = new JSONObject();
					json.put(ReferConstant.REFER_ID, ((SuperEntity) t).getAttributeValue(refer.id()));
					json.put(ReferConstant.REFER_CODE, ((SuperEntity) t).getAttributeValue(refer.code()));
					json.put(ReferConstant.REFER_NAME, ((SuperEntity) t).getAttributeValue(refer.name()));
					json.put(ReferConstant.REFER_PARENTID, ((SuperEntity) t).getAttributeValue(refer.parentId()));
					break;
				}
			}
		}
		return json;
	}

	@Override
	public JSONArray getReferEntityLst(List<String> valueIds, String referCode) {
		if (valueIds == null || valueIds.isEmpty()) {

			return null;
		}
		JSONArray jSONArray = new JSONArray();
		Map<String, Object> beansWithAnnotationMap = ContextUtils.getApplicationContext()
				.getBeansWithAnnotation(Refer.class);
		for (String key : beansWithAnnotationMap.keySet()) {
			Object bean = beansWithAnnotationMap.get(key);
			Refer refer = bean.getClass().getAnnotation(Refer.class);
			if (refer != null && refer.referCode().equals(referCode)) {
				List<SuperEntity> lst = (List<SuperEntity>) query
						.queryByPrimary((Class<? extends SuperEntity>) bean.getClass(), valueIds, null);
				if (lst != null && lst.size() > 0) {
					for (SuperEntity entity : lst) {
						JSONObject json = new JSONObject();
						json.put(ReferConstant.REFER_ID, ((SuperEntity) entity).getAttributeValue(refer.id()));
						json.put(ReferConstant.REFER_CODE, ((SuperEntity) entity).getAttributeValue(refer.code()));
						json.put(ReferConstant.REFER_NAME, ((SuperEntity) entity).getAttributeValue(refer.name()));
						json.put(ReferConstant.REFER_PARENTID,
								((SuperEntity) entity).getAttributeValue(refer.parentId()));
						jSONArray.add(json);
					}
				}
			}
		}
		return jSONArray;
	}
}
