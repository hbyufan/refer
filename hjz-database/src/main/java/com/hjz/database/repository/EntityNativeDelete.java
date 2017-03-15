package com.hjz.database.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import com.hjz.database.entity.SuperEntity;
import com.hjz.exception.BusinessException;

@Component
public class EntityNativeDelete<T extends SuperEntity> extends EntityNativeUpdate<T> {
//	@Autowired
//	public EntityNativeDelete(EntityManager entityManager) {
//		super(entityManager);
//	}
	public void delete(List<String> idLst, Class<T> clazz) throws BusinessException {
		initOperator(clazz);
		List<T> entityLst = null;
		try {
			entityLst = createEntityLst(idLst, clazz);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		EntityNativeQuery<T> nativeQuery = new EntityNativeQuery<T>();
		List<T> orginEntityLst = nativeQuery.queryByPrimary(getClazz(), idLst, null);
		setOrginEntityMap(orginEntityLst);
		StringBuffer sql = getUpdateSql(entityLst, getDbFiledNames());
		Query rquery = entityManager.createNativeQuery(sql.toString());
		rquery.executeUpdate();
	}

	private List<T> createEntityLst(List<String> idLst, Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
		List<T> entityLst = new ArrayList<T>();
		for (String id : idLst) {
			T entity = clazz.newInstance();
			entity.setAttributeValue(getPrimaryFiled(), id);
			entity.setAttributeValue("dr", 1);
			entityLst.add(entity);
		}
		return entityLst;

	}
}
