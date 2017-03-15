package com.hjz.database.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;

import com.hjz.database.entity.BeanHelper;
import com.hjz.database.entity.SuperEntity;

public abstract class AbstractEntityOperator<T extends SuperEntity> {

	private Class<? extends SuperEntity> clazz;
	protected String primaryDbFiled = null;
	protected String primaryFiled = null;

	public String getPrimaryDbFiled() {
		return primaryDbFiled;
	}

	public void setPrimaryDbFiled(String primaryDbFiled) {
		this.primaryDbFiled = primaryDbFiled;
	}

	public String getPrimaryFiled() {
		return primaryFiled;
	}

	public void setPrimaryFiled(String primaryFiled) {
		this.primaryFiled = primaryFiled;
	}

	private String talbeName;

	protected Class<? extends SuperEntity> getClazz() {
		return clazz;
	}

	// 插入的字段
	private List<String> fieldNames = new ArrayList<>();
	// DB字段
	private List<String> dbFiledNames = new ArrayList<>();

	protected List<String> getFieldNames() {
		return fieldNames;
	}

	protected void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

	protected List<String> getDbFiledNames() {
		return dbFiledNames;
	}

	protected void setDbFiledNames(List<String> dbFiledNames) {
		this.dbFiledNames = dbFiledNames;
	}

	protected String getTalbeName() {
		return talbeName;
	}

	protected void setTalbeName() {
		Table table = getClazz().getAnnotation(Table.class);
		this.talbeName = table.name();
	}
	
	/**
	 * 考虑动态创建的表
	 * 
	 * @author xg
	 * @param tableName
	 */
	protected void setTableName(String tableName){
		this.talbeName = tableName;
	}

	private Map<String, String> filedMap = new HashMap<String, String>();
	private Map<String, String> dbFiledMap = new HashMap<String, String>();

	@PersistenceContext
	protected EntityManager entityManager;

	// public AbstractEntityOperator(EntityManager entityManager) {
	// super();
	// this.entityManager = entityManager;
	// }

	protected void initOperator(Class<? extends SuperEntity> clazz) {
		if (fieldNames.size() == 0 || clazz != this.getClazz()) {
			fieldNames.clear();
			dbFiledNames.clear();
			setFieldName(clazz, fieldNames, dbFiledNames);
			for (int i = 0; i < fieldNames.size(); i++) {
				dbFiledMap.put(fieldNames.get(i), dbFiledNames.get(i));
				filedMap.put(dbFiledNames.get(i), fieldNames.get(i));
			}
		}
		initTable(clazz);
	}

	/**
	 * 考虑到动态创建的临时表,相同Entity对应的表名不一致
	 * 
	 * @author xg
	 * @param clazz
	 */
	protected void initOperator(Class<? extends SuperEntity> clazz,String tableName) {
		if (fieldNames.size() == 0 || clazz != this.getClazz()) {
			fieldNames.clear();
			dbFiledNames.clear();
			setFieldName(clazz, fieldNames, dbFiledNames);
			for (int i = 0; i < fieldNames.size(); i++) {
				dbFiledMap.put(fieldNames.get(i), dbFiledNames.get(i));
				filedMap.put(dbFiledNames.get(i), fieldNames.get(i));
			}
		}
		this.clazz = clazz;
		setTableName(tableName);
	}

	protected void initTable(Class<? extends SuperEntity> clazz) {
		this.clazz = clazz;
		setTalbeName();
	}

	/**
	 * 根据数据库字段获取实体字段
	 * 
	 * @param dbFiledName
	 */
	protected String getFiledname(String dbFiledName) {
		return filedMap.get(dbFiledName);
	}

	/**
	 * 根据数据库字段获取实体字段
	 * 
	 * @param filedName
	 */
	protected String getDbFiledname(String filedName) {
		return dbFiledMap.get(filedName);
	}

	/**
	 * 递归设置的原因因为有部分字段is_delete等放到了父类
	 * 
	 * @param clazz
	 * @param fieldNames
	 * @param methods
	 */
	protected void setFieldName(Class<?> clazz, List<String> fieldNames, List<String> dbFiledNames) {
		Field[] fields = clazz.getDeclaredFields();
		List<String> currentFieldNames = new ArrayList<String>();
		for (Field field : fields) {
			currentFieldNames.add(field.getName());
		}
		if (currentFieldNames.size() > 0) {
			BeanHelper beanHelper = new BeanHelper();
			Method[] currentMethods = beanHelper.getAllGetMethod(clazz, currentFieldNames.toArray(new String[0]));

			for (int i = 0; i < fields.length; i++) {
				Column column = null;
				// 优先考虑注解放在字段上的
				if (fields[i].getAnnotation(Column.class) != null) {
					column = fields[i].getAnnotation(Column.class);

				} else {
					if (currentMethods[i] != null)
						column = currentMethods[i].getAnnotation(Column.class);
				}

				if (column != null) {
					setPrimaryColumn(fields[i], currentMethods[i], column);
					fieldNames.add(fields[i].getName());
					dbFiledNames.add(column.name());
				}
			}
		}
		// 递归调用把父级数据
		if (clazz.getSuperclass() != null)
			setFieldName(clazz.getSuperclass(), fieldNames, dbFiledNames);

	}

	/**
	 * 设置主键字段
	 * 
	 * @param field
	 * @param method
	 * @param column
	 */
	private void setPrimaryColumn(Field field, Method method, Column column) {
		Id id = null;
		if (field.getAnnotation(Id.class) != null)
			id = field.getAnnotation(Id.class);
		if (method.getAnnotation(Id.class) != null)
			id = method.getAnnotation(Id.class);
		if (id != null) {
			primaryDbFiled = column.name();
			primaryFiled = field.getName();
		}

	}

	/**
	 * 此处插入方法描述。 创建日期：(2002-7-5 11:08:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTimeStampString() {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
		// return getTimeStampString;
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

	protected List<T> convertResult(List<Object> objs) throws InstantiationException, IllegalAccessException {
		List<T> result = new ArrayList<T>();
		for (Object obj : objs) {
			Object[] item = (Object[]) obj;
			@SuppressWarnings("unchecked")
			T entity = (T) getClazz().newInstance();
			for (int i = 0; i < item.length; i++) {
				entity.setAttributeValue(getFieldNames().get(i), item[i]);
			}
			result.add(entity);
		}
		return result;
	}

	protected List<T> convertResult(List<Object> objs, List<String> fieldNames)
			throws InstantiationException, IllegalAccessException {
		List<T> result = new ArrayList<T>();
		for (Object obj : objs) {
			Object[] item = (Object[]) obj;
			@SuppressWarnings("unchecked")
			T entity = (T) getClazz().newInstance();
			for (int i = 0; i < item.length; i++) {
				entity.setAttributeValue(fieldNames.get(i), item[i]);
			}
			result.add(entity);
		}
		return result;
	}

	protected StringBuffer constructSQL(List<String> FiledNames, String condition, String order) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		// 要查询的字段
		sql.append(this.constructQueryField(FiledNames));
		sql.append(" from ");
		sql.append(getTalbeName());
		sql.append(" where dr=0 ");

		// 额外的条件
		if (condition != null) {
			sql.append(" ");
			sql.append(condition);
		}
		// 排序语句
		if (order != null) {
			sql.append(" ");
			sql.append(order);
		}
		return sql;
	}

	private StringBuffer constructQueryField(List<String> filedNames) {
		StringBuffer sql = new StringBuffer();
		for (int i = 0; i < filedNames.size(); i++) {
			sql.append(filedNames.get(i));
			if (filedNames.size() - 1 == i) {
				sql.append("");
			} else {
				sql.append(",");
			}
		}
		return sql;
	}
}
