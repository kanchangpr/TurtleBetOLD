package com.jetbet.auth.authentication;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JwtUserDetails implements UserDetails {

	private static final long serialVersionUID = 5155720064139820502L;

	//private final Long id;
	private final String username;
	//private final String password;
	private final Collection<? extends GrantedAuthority> authorities;

	/*public JwtUserDetails(Long id, String username, String password, String role) {
		this.id = id;
		this.username = username;
		this.password = password;

		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(role));

		this.authorities = authorities;
	}*/

	public JwtUserDetails( String username,  Set<SimpleGrantedAuthority> authorities) {
		this.username = username;
		//this.password = password;
		this.authorities = authorities;
	}

	/*public JwtUserDetails( String username,Collection<? extends GrantedAuthority> collection) {
		this.username = username;
		this.authorities = collection;
	}*/



	@Override
	public String getUsername() {
		return username;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

}
