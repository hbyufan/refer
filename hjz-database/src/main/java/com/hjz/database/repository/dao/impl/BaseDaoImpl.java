package com.hjz.database.repository.dao.impl;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.hjz.database.entity.SuperEntity;
import com.hjz.database.repository.dao.BaseDao;

/**
 * 继承SimpleJpaRepository，重写删除逻辑为逻辑删除
 *
 * @param <T>
 */
@Transactional
public class BaseDaoImpl<T extends SuperEntity> extends SimpleJpaRepository<T, Serializable> implements BaseDao<T> {

	private final EntityManager entityManager;
	
	private  JpaEntityInformation<T, ?> entityInformation;

	public BaseDaoImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.entityManager = entityManager;
	}

	public BaseDaoImpl(JpaEntityInformation<T, Serializable> information, EntityManager entityManager) {
		super(information, entityManager);
		this.entityManager = entityManager;
		this.entityInformation = information;
	}

	@Override
	public <S extends T> S save(S entity) {
		entity.setAttributeValue("modificationTimestamp", new Timestamp(System.currentTimeMillis()));
		return super.save(entity);
	}

	/**
	 * 只做逻辑删除
	 */
	@Override
	public void delete(T entity) {
		entity.setDr(1);
		save(entity);
	}

	@Override
	public void delete(Serializable id) {
		T entity = findOne(id);
		entity.setDr(1);
		this.save(entity);
	}
	@Transactional
	@Override
	public void deleteInBatch(Iterable<T> entities) {
		if (!(entities.iterator().hasNext())) {
			return;
		}

		QueryUtils.applyAndBind(QueryUtils.getQueryString("update %s x set dr = 1 ", this.entityInformation.getEntityName()),
				entities, this.entityManager).executeUpdate();
	}

	@Transactional
	@Override
	public void deleteAll() {
		for (T t : findAll()) {
			t.setDr(1);
			save(t);
		}
	}
}
