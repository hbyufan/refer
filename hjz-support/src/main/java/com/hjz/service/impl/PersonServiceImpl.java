package com.hjz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hjz.bo.PersonBO;
import com.hjz.entity.PersonEntity;
import com.hjz.repository.PersonDao;
import com.hjz.service.IPersonService;

@Service
public class PersonServiceImpl implements IPersonService {
	@Autowired
	private PersonDao personDao;
	
	@Override
	public List<PersonBO> findAllPersons() {
		List<PersonEntity> entities = personDao.findAllPersons();
		List<PersonBO> bos = new ArrayList<>();
		for(PersonEntity entity : entities) {
			PersonBO bo = new PersonBO();
			BeanUtils.copyProperties(entity, bo);
			bos.add(bo);
		}
		return bos;
	}
}
