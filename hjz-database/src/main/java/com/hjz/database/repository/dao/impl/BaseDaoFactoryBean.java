package com.hjz.database.repository.dao.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.hjz.database.entity.SuperEntity;

import javax.persistence.EntityManager;
import java.io.Serializable;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BaseDaoFactoryBean<R extends JpaRepository<T, Serializable>, T> extends JpaRepositoryFactoryBean<R, T, Serializable> {

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(final EntityManager entityManager) {
        return new JpaRepositoryFactory(entityManager) {

			protected SimpleJpaRepository<T, Serializable> getTargetRepository(
					RepositoryInformation information,	EntityManager entityManager) {
				Class<T> domainClass = (Class<T>) information.getDomainType();
				if(SuperEntity.class.isAssignableFrom(domainClass)) {
					return new BaseDaoImpl(domainClass, entityManager);
				} else { 
					return new SimpleJpaRepository(domainClass, entityManager);
				}
			}

            @Override
            protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            	return SuperEntity.class.isAssignableFrom(metadata.getDomainType()) ? BaseDaoImpl.class : SimpleJpaRepository.class;
            }
        };
    }
}
