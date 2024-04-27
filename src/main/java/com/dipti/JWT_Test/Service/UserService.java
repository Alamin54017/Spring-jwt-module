package com.dipti.JWT_Test.Service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dipti.JWT_Test.Model.Authority;
import com.dipti.JWT_Test.Model.User;
import com.dipti.JWT_Test.Repository.AuthorityRepository;
import com.dipti.JWT_Test.Repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository repository;
	private final AuthorityRepository authorityRepository;
	private final PasswordEncoder passwordEncoder;
	
	
	public UserService(UserRepository repository, AuthorityRepository authorityRepository,
			PasswordEncoder passwordEncoder) {
		super();
		this.repository = repository;
		this.authorityRepository = authorityRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User findUserByUsername(String username) {
		return repository.findByUsernameIgnoreCase(username);
	}
	
	public void saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Authority authority=authorityRepository.findByAuthority("ROLE_USER");
		user.setAuthorities(Set.of(authority));
		repository.save(user);
	}
	
	public void saveAdmin(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Authority authority=authorityRepository.findByAuthority("ROLE_ADMIN");
		user.setAuthorities(Set.of(authority));
		repository.save(user);
	}
	
	
	

}
