package com.hjz.database.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hjz.database.entity.SuperEntity;
import com.hjz.exception.BusinessException;

@Component
public class EntityNativeUpdate<T extends SuperEntity> extends AbstractEntityOperator<T> {

	// @Autowired
	// public EntityNativeUpdate(EntityManager entityManager) {
	// super(entityManager);
	// }
	@Autowired
	private EntityNativeQuery<T> nativeQuery;
	/**
	 * 数据库原始数据
	 */
	private Map<String, T> orginEntityMap = null;

	public void update(List<T> entityLst, Class<? extends SuperEntity> clazz) throws BusinessException {
		initOperator(clazz);
		List<String> idLst = getIdLst(entityLst);
		List<T> orginEntityLst = nativeQuery.queryByPrimary(getClazz(), idLst, null);
		setOrginEntityMap(orginEntityLst);
		StringBuffer sql = getUpdateSql(entityLst, getDbFiledNames());
		Query rquery = entityManager.createNativeQuery(sql.toString());
		rquery.executeUpdate();
	}

	/**
	 * 按照字段和wheresql更新字段
	 * 
	 * @param entity
	 * @param clazz
	 * @param fileds
	 * @param wheresql
	 */
	public void update(T entity, Class<T> clazz, List<String> fileds, String wheresql) {
		initOperator(clazz);
		if (fileds == null)
			fileds = getDbFiledNames();
		StringBuffer sql = getSingleUpdateSql(entity, fileds, wheresql);
		Query rquery = entityManager.createNativeQuery(sql.toString());
		rquery.executeUpdate();
	}

	/**
	 * 按照主键更新实体
	 * 
	 * @param entity
	 * @param clazz
	 */
	public void updateByPriamry(T entity, Class<T> clazz) {
		initOperator(clazz);
		StringBuffer sql = getSingleUpdateSql(entity, getDbFiledNames(), null);
		Query rquery = entityManager.createNativeQuery(sql.toString());
		rquery.executeUpdate();
	}

	private StringBuffer getSingleUpdateSql(T entity, List<String> dbfileds, String wheresql) {
		StringBuffer sql = new StringBuffer();
		sql.append("update  " + getTalbeName() + "( set ");
		for (int i = 0; i < dbfileds.size(); i++) {
			String dbFiledname = getDbFiledNames().get(i);
			sql.append(dbFiledname + "=" + entity.getAttributeValue(dbFiledname) + " ");
			if (getDbFiledNames().size() - 1 == i) {
				sql.append(" ");
			} else {
				sql.append(",");
			}
		}
		if (wheresql == null) {
			sql.append(" where is_delete=0 and " + getPrimaryDbFiled() + "="
					+ entity.getAttributeValue(getPrimaryDbFiled()));
		} else {
			sql.append(" where is_delete=0 " + wheresql);
		}
		return sql;
	}

	protected void setOrginEntityMap(List<T> orginEntityLst) {
		orginEntityMap = new HashMap<String, T>();
		for (T entity : orginEntityLst) {
			orginEntityMap.put((String) entity.getAttributeValue(getPrimaryFiled()), entity);
		}
	}

	private Object getOrginEntityAttributeValue(String id, String key) {
		if (orginEntityMap.containsKey(id))
			return orginEntityMap.get(id).getAttributeValue(key);
		return null;
	}

	private List<String> getIdLst(List<T> entityLst) {
		List<String> idLst = new ArrayList<>();
		for (T entity : entityLst) {
			if (entity.getAttributeValue(getPrimaryFiled()) != null)
				idLst.add((String) entity.getAttributeValue(getPrimaryFiled()));
		}
		return idLst;
	}

	protected StringBuffer getUpdateSql(List<T> entityLst, List<String> dbFiledNames) {
		StringBuffer sql = new StringBuffer();
		sql.append("replace  into   " + getTalbeName() + "(");
		for (int i = 0; i < dbFiledNames.size(); i++) {
			sql.append(getDbFiledNames().get(i));
			if (getDbFiledNames().size() - 1 == i) {
				sql.append("");
			} else {
				sql.append(",");
			}
		}
		sql.append(") VALUES");
		for (int i = 0; i < entityLst.size(); i++) {
			T entity = entityLst.get(i);
			sql.append("(");
			for (int j = 0; j < dbFiledNames.size(); j++) {
				// 主键取UUID
				sql.append(getItemSql(entity, getFiledname(dbFiledNames.get(j))));
				if (dbFiledNames.size() - 1 == j) {
					sql.append("");
				} else {
					sql.append(",");
				}
			}
			if (entityLst.size() - 1 == i) {
				sql.append(")");
			} else {
				sql.append("),");
			}

		}
		return sql;
	}

	/**
	 * 优先取传进来更改的值，取不到取数据库原存的值，否则replace into 的时候会取数据库默认值
	 * 
	 * @param entity
	 * @param fieldName
	 * @return
	 */
	private Object getItemSql(T entity, String fieldName) {
		Object attributeValue = entity.getAttributeValue(fieldName);
		if (attributeValue == null) {
			return getOrginEntityAttributeValue((String) entity.getAttributeValue(getPrimaryFiled()), fieldName) != null
					? "'" + getOrginEntityAttributeValue((String) entity.getAttributeValue(getPrimaryFiled()),
							fieldName) + "'"
					: null;
		} else {
			if (attributeValue instanceof Boolean) { // boolean类型特殊处理
				Boolean boolValue = (Boolean) attributeValue;
				if (boolValue.booleanValue()) {
					return "'" + 1 + "'";
				} else {
					return "'" + 0 + "'";
				}
			} else {
				return "'" + attributeValue + "'";
			}
		}

	}
}
