package com.hjz.database.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.hjz.database.entity.SuperEntity;

@Component
@Transactional
public class BaseEntityDao<T extends SuperEntity> {

	@PersistenceContext
	protected EntityManager em;

	public void batchInsert(List<T> list) {
		for (int i = 0; i < list.size(); i++) {
			em.persist(list.get(i));
			if ((i % 30000 == 0 && i >= 30000) || i == list.size() - 1) {
				//System.out.println("em.flush();");
				em.flush();
				em.clear();
			}
		}
	}

	public void batchUpdate(List<T> list) {
		for (int i = 0; i < list.size(); i++) {
			em.merge(list.get(i));
			if (i % 30 == 0 && i != 0) {
				em.flush();
				em.clear();
			}
		}
	}
	public void batchDelete(List<T> list) {
		for (int i = 0; i < list.size(); i++) {
			em.remove(list.get(i));
			if (i % 30 == 0 && i != 0) {
				em.flush();
				em.clear();
			}
		}
	}
}
