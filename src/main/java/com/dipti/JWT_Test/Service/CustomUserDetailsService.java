package com.dipti.JWT_Test.Service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dipti.JWT_Test.Model.Authority;
import com.dipti.JWT_Test.Model.User;
import com.dipti.JWT_Test.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.findByUsernameIgnoreCase(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("No user found with this username"+username);
		}
		Set<GrantedAuthority> authorities=new HashSet<>();
		for(Authority authority: user.getAuthorities()) {
			authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}

}
