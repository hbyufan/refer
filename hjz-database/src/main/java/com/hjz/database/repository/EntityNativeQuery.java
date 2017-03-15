package com.hjz.database.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import com.hjz.database.entity.SuperEntity;
import com.hjz.exception.BusinessException;

@Component
public class EntityNativeQuery<T extends SuperEntity> extends AbstractEntityOperator<T> {

//	@Autowired
//	public EntityNativeQuery(EntityManager entityManager) {
//		super(entityManager);
//	}

	/**
	 * 
	 * @param entityLst
	 * @param clazz
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<T> query(Class<? extends SuperEntity> clazz, String order) throws BusinessException {
		initOperator(clazz);
		StringBuffer sql = constructSQL(getDbFiledNames(), null, order);
		Query rquery = entityManager.createNativeQuery(sql.toString());
		List<Object> objs = rquery.getResultList();
		try {
			return convertResult(objs);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * 
	 * @param entityLst
	 * @param clazz
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<T> query(Class<? extends SuperEntity> clazz, List<String> filedNames,String conditionSql,  String order)
			throws BusinessException {
		getFieldNames().clear();
		getDbFiledNames().clear();
		initOperator(clazz);

		List<String> dbfiledNames = new ArrayList<>();
		for (String filedname : filedNames) {
			dbfiledNames.add(getDbFiledname(filedname));

		}

		StringBuffer sql = constructSQL(dbfiledNames, conditionSql, order);
		Query rquery = entityManager.createNativeQuery(sql.toString());
		List<Object> objs = rquery.getResultList();
		try {
			return convertResult(objs,filedNames);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * 根据SQL语句查询记录
	 *
	 * @param sql
	 *            已拼装好"?"的SQL语句
	 * @param paras
	 * @param pageable
	 * @param
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<T> query(Class<T> clazz,  String conditionSql, String order)
			throws BusinessException {
		initOperator(clazz);
		StringBuffer basesql = constructSQL(getDbFiledNames(), conditionSql, order);
		Query rquery = entityManager.createNativeQuery(basesql.toString());
		List<Object> objs = rquery.getResultList();
		try {
			return convertResult(objs);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public List<T> query(Class<T> clazz,  String conditionSql, String order, Map<String,Object> params)
			throws BusinessException {
		initOperator(clazz);
		StringBuffer basesql = constructSQL(getDbFiledNames(), conditionSql, order);
		Query rquery = entityManager.createNativeQuery(basesql.toString());
		setParameters(rquery,params);
		List<Object> objs = rquery.getResultList();
		try {
			return convertResult(objs);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * 按照主键查询
	 * 
	 * @param clazz
	 * @param idLst
	 * @param order
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<T> queryByPrimary(Class<? extends SuperEntity> clazz, List<String> idLst, String order)
			throws BusinessException {
		initOperator(clazz);
		// buf_sql.append("and defdoclist_id=:defdoclistId");
		StringBuffer sql = constructSQL(getDbFiledNames(),
				" and " + getPrimaryDbFiled() + " in(:" + getPrimaryDbFiled() + ") ", order);
		Query rquery = entityManager.createNativeQuery(sql.toString());
		setPrimaryParameters(rquery, idLst);
		List<Object> objs = rquery.getResultList();
		try {
			return convertResult(objs);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * 按顺序设置Query参数
	 *
	 * @param query
	 * @param paras
	 */
	protected void setParameters(Query query, Map<String, Object> paras) {
		if (paras == null || paras.isEmpty()) {
			return;
		}
		Set<String> set = paras.keySet();
		for (String key : set) {
			query.setParameter(key, paras.get(key));
		}
	}

	/**
	 * 按顺序设置Query参数
	 *
	 * @param query
	 * @param paras
	 */
	protected void setPrimaryParameters(Query query, List<String> idLst) {
		query.setParameter(getPrimaryDbFiled(), idLst);
	}

	/**
	 * 查询单个字段
	 * 
	 * @author xg
	 * @date 2016-12-15
	 * @param clazz
	 * @param filedName
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<Object> querySingleField(Class<? extends SuperEntity> clazz, String fieldName, String condition,
			String order) throws BusinessException {
		initTable(clazz);
		List<String> fieldNames = new ArrayList<String>(1);
		fieldNames.add(fieldName);
		StringBuffer sql = constructSQL(fieldNames, condition, order);
		Query rquery = entityManager.createNativeQuery(sql.toString());
		return rquery.getResultList();
	}

	public List<Object> querySingleField(Class<? extends SuperEntity> clazz, String fieldName, String condition,
			String order,Map<String,Object> params) throws BusinessException {
		initTable(clazz);
		List<String> fieldNames = new ArrayList<String>(1);
		fieldNames.add(fieldName);
		StringBuffer sql = constructSQL(fieldNames, condition, order);
		Query rquery = entityManager.createNativeQuery(sql.toString());
		setParameters(rquery,params);
		return rquery.getResultList();
	}

}
