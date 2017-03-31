package com.hjz.service;

import java.util.List;

import com.hjz.controller.bo.PersonBO;

public interface IPersonService {

	List<PersonBO> findAllPersons();

}
