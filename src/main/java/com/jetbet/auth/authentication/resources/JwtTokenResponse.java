package com.jetbet.auth.authentication.resources;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class JwtTokenResponse implements Serializable {

	
	private static final long serialVersionUID = 8317676219297719109L;

	private final String token;
	private final String userName;
	private final String userRole;
	private final String userParent;
//	private final Collection<? extends GrantedAuthority> roles;
	
}