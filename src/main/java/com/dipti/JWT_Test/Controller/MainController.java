package com.dipti.JWT_Test.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dipti.JWT_Test.Model.JwtResponse;
import com.dipti.JWT_Test.Model.User;
import com.dipti.JWT_Test.Service.UserService;
import com.dipti.JWT_Test.Utils.JwtUtils;

@RestController
public class MainController {

	private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    
    Logger logger=LoggerFactory.getLogger(MainController.class);
	
    
    public MainController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.userService = userService;
	}
    
    
    @GetMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) {
        Authentication authentication;
        System.out.println(user.getUsername()+"/n"+user.getPassword());

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect credentials!", HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        JwtResponse jwtResponse = JwtResponse.builder()
                .type("Bearer")
                .username(user.getUsername())
                .token(jwt)
                .build();

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    
    
    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User existingUser = userService.findUserByUsername(user.getUsername());

        if (existingUser != null) {
            return ResponseEntity.badRequest().body("User already exists!");
        }
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    
    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody User user) {
        User existingUser = userService.findUserByUsername(user.getUsername());

        if (existingUser != null)
            return ResponseEntity.badRequest().body("User already exists!");

        userService.saveAdmin(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    
    
    @GetMapping("/user")
    public String welcomeUser() {
        return "Welcome to user controller";
    }

    
    @GetMapping("/admin")
    public String welcomeAdmin() {
        return "Welcome to admin controller";
    }
}
