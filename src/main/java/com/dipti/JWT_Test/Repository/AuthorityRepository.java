package com.dipti.JWT_Test.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dipti.JWT_Test.Model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

	Authority findByAuthority(String authority);
}
