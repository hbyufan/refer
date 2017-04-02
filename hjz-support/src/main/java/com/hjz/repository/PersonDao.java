package com.hjz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.hjz.database.repository.dao.BaseDao;
import com.hjz.entity.PersonEntity;

public interface PersonDao extends BaseDao<PersonEntity>{
	
	@Query(value = "select p from PersonEntity p where p.dr = 0")
	List<PersonEntity> findAllPersons();
}
