package com.jetbet.service;

import org.springframework.stereotype.Service;

import com.jetbet.auth.authentication.resources.JwtTokenRequest;
import com.jetbet.bean.ResponseBean;



@Service
public interface UserAuthService {

	ResponseBean authUser(JwtTokenRequest userRequest, String transactionId);
}
