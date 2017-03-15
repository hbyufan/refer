package com.hjz.database.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.hjz.database.entity.SuperEntity;
import com.hjz.exception.BusinessException;
/**
 * 原生SQL分页查询
 * @param <T>
 */
@Component
public class EntityNativePageQuery <T extends SuperEntity> extends AbstractEntityOperator<T>{
//	@Autowired
//	public EntityNativePageQuery(EntityManager entityManager) {
//		super(entityManager);
//	}

	/**
	 * 根据SQL语句查询列表
	 *
	 * @param sql
	 * @param paras
	 * @return
	 * @throws BusinessException 
	 */
	@SuppressWarnings("unchecked")
	private List<T> findListBySql(String sql, Map<String, Object> paras) throws BusinessException {
		Query rquery = entityManager.createNativeQuery(sql);
		setParameters(rquery, paras);
		try {
			return convertResult(rquery.getResultList());
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}
	/**
	 * 根据SQL语句查询分页记录
	 *
	 * @param sql
	 *            已拼装好"?"的SQL语句
	 * @param paras
	 * @param pageable
	 * @param
	 * @return
	 * @throws BusinessException 
	 */
	public Page<T> queryPage(Class<? extends SuperEntity> clazz, Map<String, Object> paras,String conditionSql,String order ,Pageable pageable) throws BusinessException {
		initOperator(clazz);
		StringBuffer basesql=constructSQL(getDbFiledNames(), conditionSql, order);
		if (pageable == null) {
			return new PageImpl<T>(findListBySql(basesql.toString(), paras));
		}
		String csql = "select count(1) from (" + basesql.toString() + ") t";
		return findListBySql(basesql.toString(), csql, paras, pageable);
	}
	
	/**
	 * 根据SQL语句查询分页记录
	 *
	 * @param qsql
	 *            用于查询返回记录的SQL语句
	 * @param csql
	 *            用于查询记录条数的SQL语句
	 * @param paras
	 * @param pageable
	 * @return
	 * @throws BusinessException 
	 */
	@SuppressWarnings("unchecked")
	private Page<T> findListBySql(String qsql, String csql, Map<String, Object> paras, Pageable pageable) throws BusinessException {
		if (pageable == null) {
			return new PageImpl<T>(findListBySql(qsql, paras));
		}
		Query rquery = entityManager.createNativeQuery(qsql);
		Query cquery = entityManager.createNativeQuery(csql);
		setParameters(rquery, paras);
		setParameters(cquery, paras);

		rquery.setFirstResult(pageable.getOffset());
		rquery.setMaxResults(pageable.getPageSize());

		
		long total = ((BigInteger) cquery.getSingleResult()).longValue();
		List<T> result=new ArrayList<T>();
		try {
			result = convertResult(rquery.getResultList());
		} catch (InstantiationException e) {
			throw new BusinessException(e);
		} catch (IllegalAccessException e) {
			throw new BusinessException(e);
		}
		return new PageImpl<T>(result, pageable, total);
	}
}
