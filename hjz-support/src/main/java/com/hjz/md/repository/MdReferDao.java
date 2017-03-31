package com.hjz.md.repository;

import org.springframework.data.jpa.repository.Query;

import com.hjz.database.repository.dao.BaseDao;
import com.hjz.md.entity.MdReferEntity;

public interface MdReferDao extends BaseDao<MdReferEntity>{
	
	@Query(value="select * from md_refer where refer_code = ?1", nativeQuery=true)
	MdReferEntity getMdrefer(String refercode);
}
