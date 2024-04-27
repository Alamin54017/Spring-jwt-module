package com.dipti.JWT_Test.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dipti.JWT_Test.Model.*;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	User findByUsernameIgnoreCase(String username);

}
