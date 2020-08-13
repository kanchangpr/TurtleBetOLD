package com.jetbet.auth.authentication;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jetbet.bean.UserBean;
import com.jetbet.repository.UserRepository;



@Service
public class JwtInMemoryUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
//	@Autowired
//	private RoleRepository roleRepository;
	//static List<JwtUserDetails> inMemoryUserList = new ArrayList<>();

	/*static {
		inMemoryUserList.add(new JwtUserDetails(1L, "nsmsuper",
				"$2a$10$czgPAxgOSW62LvdGnB8JCetMLz5YB.O.OOc7wOilqUvNfeMh.mAs6", "ROLE_USER_2"));

		//$2a$10$IetbreuU5KihCkDB6/r1DOJO0VyU9lSiBcrMDT.biU7FOt2oqZDPm
	}*/

	/*	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<JwtUserDetails> findFirst = inMemoryUserList.stream()
				.filter(user -> user.getUsername().equals(username)).findFirst();

		if (!findFirst.isPresent()) {
			throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username));
		}

		return findFirst.get();
	}
*/

	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserBean user = userRepository.findByUserId(userName);
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		//return new JwtUserDetails(user.getUserName(), user.getPassword(), getAuthority(user));
		return new JwtUserDetails(user.getUserId(), getAuthority(user));
		//return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthority(user));
	}

	private Set<SimpleGrantedAuthority> getAuthority(UserBean user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		//user.getRole();
		//.forEach(role -> {
			//authorities.add(new SimpleGrantedAuthority(role.getName()));
           // authorities.add(new SimpleGrantedAuthority("ROLE_" + role.get));
          //  System.out.println("Roles from DB" + role.getRoleName());
		//});
		authorities.add(new SimpleGrantedAuthority(user.getUserRole()));
		authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getUserRole()));
		System.out.println("Roles from DB" + user.getUserRole());
		return authorities;
		//return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}



	/*public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(userName);
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		//return new JwtUserDetails(user.getUserName(), user.getPassword(), getAuthority(user));
		return new JwtUserDetails(user.getUserName(), getAuthorities(user.getRoles()));
		//return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthority(user));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
		return getGrantedAuthorities(getPrivileges(roles));
	}

	private List<String> getPrivileges(Collection<Role> roles) {

		List<String> privileges = new ArrayList<>();
		List<View> collection = new ArrayList<>();
		for (Role role : roles) {
			collection.addAll(role.getViews());
		}
		for (View item : collection) {
			privileges.add(item.getViewName());
		}
		return privileges;
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}*/

}
