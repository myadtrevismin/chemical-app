package com.core.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
class CustomRestMvcConfiguration {

	@PersistenceContext
	EntityManager em;

	@Bean
	public RepositoryRestConfigurer repositoryRestConfigurer() {

		return new RepositoryRestConfigurer() {

			@Override
			public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
				config.setBasePath("/api");
				config.exposeIdsFor(em.getMetamodel().getEntities().stream().map(entityType -> entityType.getJavaType())
						.toArray(Class[]::new));
			}
		};
	}
}
