package com.core.location.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.core.location.State;

@Repository
@RepositoryRestResource(collectionResourceRel = "states", path = "states")
public interface StateRepo extends JpaRepository<State, Long>{
	

}
