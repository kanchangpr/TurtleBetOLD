package com.jetbet.auth.authentication.resources;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jetbet.auth.authentication.JwtTokenUtil;



@RestController
@CrossOrigin(origins = "*")
public class JwtAuthenticationRestController {

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	//@RequestMapping(value = "${jwt.get.token.uri}", method = RequestMethod.POST)
	///public ResponseEntity<JwtTokenResponse> createAuthenticationToken( JwtTokenRequest authenticationRequest)
//	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLoginBean authenticationRequest)
		//	throws AuthenticationException {
		///System.out.println("username: "+authenticationRequest.getUsername());
//		System.out.println("userRole: "+authenticationRequest.getUserRole());
//		System.out.println("userParent: "+authenticationRequest.getUserParent());
		//final String token = jwtTokenUtil.generateTokenUsingData(authenticationRequest.getUserId(),authenticationRequest.getUserRole(),authenticationRequest.getUserParent());
		//final String token = jwtTokenUtil.generateToken(authenticationRequest.getUsername());
		//JwtTokenResponse jwtTokenResponse=new JwtTokenResponse(token,authenticationRequest.getUserId(),authenticationRequest.getUserRole(),authenticationRequest.getUserParent());
		//JwtTokenResponse jwtTokenResponse=new JwtTokenResponse(token,authenticationRequest.getUsername());
		//return ResponseEntity.ok();
	//}

	@RequestMapping(value = "${jwt.refresh.token.uri}", method = RequestMethod.GET)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		String userRole = jwtTokenUtil.getAudienceFromToken(token);
		String userParent = jwtTokenUtil.getIssuerFromToken(token);
		System.out.println("username: "+username);
		System.out.println("userRole: "+userRole);
		System.out.println("userParent: "+userParent);
		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(username);
		if (jwtTokenUtil.canTokenBeRefreshed(token)) {
			JwtTokenResponse jwtTokenResponse=new JwtTokenResponse(token,username,userRole,userParent);
			//JwtTokenResponse jwtTokenResponse=new JwtTokenResponse(token,username);
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(jwtTokenResponse);
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	private void authenticate(String username, String password) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("INVALID_CREDENTIALS", e);
		}
	}
}
