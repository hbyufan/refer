package com.hjz.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hjz.annotation.Refer;
import com.hjz.utils.ReferCacheTool;
import com.hjz.exception.BusinessException;
import com.hjz.database.entity.SuperEntity;
import com.hjz.database.repository.EntityNativeInsert;

/**
 * 重写Native插入的原因在于EmptyInterceptor无法拦截原生sql操作数据库
 * 
 * @author hupeng 2016年10月14日
 *
 * @param <T>
 */
@Component
public class EntityReferNativeInsert<T extends SuperEntity> {
	

	@Autowired
	private EntityNativeInsert<T> entityNativeInsert;

	
	public List<T> save(List<T> entityLst, Class<T> clazz) throws BusinessException  {
		entityNativeInsert.save(entityLst, clazz);
		Refer refer = clazz.getAnnotation(Refer.class);
		if (refer != null)
			ReferCacheTool.putBatchReferCache(entityLst, clazz);
		return entityLst;

	}

}
