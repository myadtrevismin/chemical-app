package com.core.location.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.core.location.AvailableCities;

@Repository
@RepositoryRestResource(collectionResourceRel = "cities", path = "cities")
public interface AvailableCitiesRepo extends JpaRepository<AvailableCities, Long>{
	

}
