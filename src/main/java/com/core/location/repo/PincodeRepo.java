package com.core.location.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.core.location.Pincode;

@Repository
@RepositoryRestResource(collectionResourceRel = "pincodes", path = "pincodes")
public interface PincodeRepo extends JpaRepository<Pincode, Long>, QuerydslPredicateExecutor<Pincode> {

}
