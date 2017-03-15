package com.hjz.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hjz.annotation.Refer;
import com.hjz.constants.ReferConstant;
import com.hjz.context.ContextUtils;
import com.hjz.cache.strategy.CacheObject;
import com.hjz.cache.strategy.JedisCacheTool;
import com.hjz.database.entity.SuperEntity;

/**
 * 
 * @author hupeng 2016年10月14日
 *
 */
public class ReferCacheTool {

	/**
	 * 添加参照缓存
	 * 
	 * @param entity
	 */
	public static void putReferCache(Object entity) {
		if (entity instanceof SuperEntity) {
			Refer refer = entity.getClass().getAnnotation(Refer.class);
			if (refer != null) {
				JedisCacheTool cacheTool = ContextUtils.getBean(JedisCacheTool.class);
				JSONObject json = new JSONObject();
				json.put(ReferConstant.REFER_ID, ((SuperEntity) entity).getAttributeValue(refer.id()));
				json.put(ReferConstant.REFER_CODE, ((SuperEntity) entity).getAttributeValue(refer.code()));
				json.put(ReferConstant.REFER_NAME, ((SuperEntity) entity).getAttributeValue(refer.name()));
				json.put(ReferConstant.REFER_PARENTID, ((SuperEntity) entity).getAttributeValue(refer.parentId()));
				cacheTool.put(refer.referCode()+((SuperEntity) entity).getAttributeValue(refer.id()), json);
				System.out.println("参照缓存新增保存" + cacheTool.get(((SuperEntity) entity).getAttributeValue(refer.referCode()+((SuperEntity) entity).getAttributeValue(refer.id()))));
			}
		}
	}

	public static void deleteReferCache(Object entity) {
		if (entity instanceof SuperEntity) {
			Refer refer = entity.getClass().getAnnotation(Refer.class);
			if (refer != null) {
				JedisCacheTool cacheTool = ContextUtils.getBean(JedisCacheTool.class);
				cacheTool.delete(refer.referCode()+((SuperEntity) entity).getAttributeValue(refer.id()));
				System.out.println("参照缓存删除" + cacheTool.get(refer.referCode()+((SuperEntity) entity).getAttributeValue(refer.id())));
			}
		}
	}

	/**
	 * 批量添加参照索引
	 * 
	 * @param lst
	 * @param clazz
	 */
	public static void putBatchReferCache(List<? extends SuperEntity> lst, Class<?> clazz) {

		if (lst != null && !lst.isEmpty()) {
			JedisCacheTool cacheTool = ContextUtils.getBean(JedisCacheTool.class);
			Refer refer = clazz.getAnnotation(Refer.class);
			if (refer != null) {
				List<CacheObject> cacheObjects = new ArrayList<CacheObject>();
				
				for (Object t : lst) {
					if (t instanceof SuperEntity) {
						CacheObject cacheObject= new CacheObject();
						JSONObject json = new JSONObject();
						String key=refer.referCode()+((SuperEntity) t).getAttributeValue(refer.id());
						cacheObject.setKey(key);
						json.put(ReferConstant.REFER_ID, ((SuperEntity) t).getAttributeValue(refer.id()));
						json.put(ReferConstant.REFER_CODE, ((SuperEntity) t).getAttributeValue(refer.code()));
						json.put(ReferConstant.REFER_NAME, ((SuperEntity) t).getAttributeValue(refer.name()));
						json.put(ReferConstant.REFER_PARENTID, ((SuperEntity) t).getAttributeValue(refer.parentId()));
						cacheObject.setValue(json);
						cacheObjects.add(cacheObject);
						
					}
				}
				long start = System.currentTimeMillis();
				cacheTool.putBatch(cacheObjects);
				long end = System.currentTimeMillis();
				System.out.println("hmset with pipeline used [" + (end - start) / 1000 + "] seconds ..");
				System.out.println("总数量：：：：：" + cacheObjects.size());
			}
		}
	}

}
