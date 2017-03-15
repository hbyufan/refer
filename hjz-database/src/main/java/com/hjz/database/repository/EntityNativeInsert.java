package com.hjz.database.repository;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import com.hjz.database.entity.SuperEntity;
import com.hjz.exception.BusinessException;

/**
 * JPA效率太低，采取原生SQL解决批量插入问题,参照实体保存请使用EntityReferNativeInsert
 * @param <T>
 */
@Component
public class EntityNativeInsert<T extends SuperEntity> extends AbstractEntityOperator<T> {
	// @Autowired
	// public EntityNativeInsert(EntityManager entityManager) {
	// super(entityManager);
	// }

	/**
	 * 得到UUID
	 * 
	 * @return
	 * @throws BusinessException
	 */
	private String getPrimaryId() throws BusinessException {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			UUID uuid = UUID.randomUUID();
			String guidStr = uuid.toString();
			md.update(guidStr.getBytes(), 0, guidStr.length());
			return new BigInteger(1, md.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * 
	 * @param entityLst
	 * @param clazz
	 *            如果实体对应有@refer注解要采用EntityReferNativeInsert
	 * @throws BusinessException
	 */
	public List<T> save(List<T> entityLst, Class<? extends SuperEntity> clazz) throws BusinessException {
		if (entityLst == null || entityLst.size() == 0) {
			return null;
		}
		initOperator(clazz);
		StringBuffer sql = getInsertSql(entityLst);

		Query rquery = entityManager.createNativeQuery(sql.toString());
		rquery.executeUpdate();
		return entityLst;
	}

	/**
	 * 批量保存数据(考虑动态创建的临时表)
	 * 
	 * @author xg
	 * @param entityLst
	 * @param clazz
	 * @param tableName
	 * @return
	 * @throws BusinessException
	 */
	public List<T> save(List<T> entityList, Class<? extends SuperEntity> clazz, String tableName)
			throws BusinessException {
		if (entityList == null || entityList.size() == 0) {
			return null;
		}
		initOperator(clazz, tableName);
		StringBuffer sql = getInsertSql(entityList);

		Query rquery = entityManager.createNativeQuery(sql.toString());
		rquery.executeUpdate();
		return entityList;
	}

	private StringBuffer getInsertSql(List<T> entityLst) throws BusinessException {

		StringBuffer sql = new StringBuffer();
		sql.append("insert into   " + getTalbeName() + "(");
		for (int i = 0; i < getDbFiledNames().size(); i++) {
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
			for (int j = 0; j < getFieldNames().size(); j++) {
				// 主键取UUID
				sql.append(getItemSql(entity, getFieldNames().get(j)));
				if (getFieldNames().size() - 1 == j) {
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

	private Object getItemSql(T entity, String fieldName) throws BusinessException {
		if (fieldName.equalsIgnoreCase(getPrimaryFiled())) {
			String uuid = getPrimaryId();
			entity.setAttributeValue(fieldName, uuid);
			return "'" + uuid + "'";
		} else if ("dr".equals(fieldName)) {
			return "0";
		} else if ("ts".equals(fieldName)) {
			return "'" + getTimeStampString() + "'";
		} else if ("isActive".equals(fieldName)) {
			return entity.getAttributeValue(fieldName) != null ? entity.getAttributeValue(fieldName) : "1";
		} else {
			// return entity.getAttributeValue(fieldName) != null ? "'" +
			// entity.getAttributeValue(fieldName) + "'" : null;
			if (entity.getAttributeValue(fieldName) != null) {
				if (entity.getAttributeValue(fieldName).getClass().isAssignableFrom(boolean.class)
						|| entity.getAttributeValue(fieldName).getClass().isAssignableFrom(Boolean.class)) {
					return ((Boolean) entity.getAttributeValue(fieldName)).booleanValue() ? "1" : "0";
				} else {
					return "'" + entity.getAttributeValue(fieldName) + "'";
				}
			} else {
			}
			return null;
		}

	}

}
