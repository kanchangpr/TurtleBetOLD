package com.jetbet.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jetbet.bean.JwtTokenRequest;
import com.jetbet.bean.ResponseBean;
import com.jetbet.service.UserAuthService;
import com.jetbet.util.ApplicationConstants;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/Auth")
@Slf4j
public class UserAuthController {
	
	@Autowired
	private UserAuthService userAuthService;
	
	@CrossOrigin(origins = "*")
	@RequestMapping(path = ApplicationConstants.AUTH_USER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<ResponseBean> authUser(@Valid @RequestBody JwtTokenRequest userRequest) {
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*******************************AuthController***************************************");
		ResponseBean response= userAuthService.authUser(userRequest,transactionId);
		return new ResponseEntity<ResponseBean>(response, HttpStatus.OK);
	}
}
