package com.core.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
class CustomRestMvcConfiguration {

	@PersistenceContext
	EntityManager em;

	@Bean
	public RepositoryRestConfigurer repositoryRestConfigurer() {

		return new RepositoryRestConfigurer() {

			@Override
			public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
				config.setBasePath("/api");
				config.exposeIdsFor(em.getMetamodel().getEntities().stream().map(entityType -> entityType.getJavaType())
						.toArray(Class[]::new));
			}
		};
	}
}
