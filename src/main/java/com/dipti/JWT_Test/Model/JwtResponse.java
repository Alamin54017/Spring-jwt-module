package com.dipti.JWT_Test.Model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JwtResponse {

	private String type;
    private String token;
    private String username;
}
