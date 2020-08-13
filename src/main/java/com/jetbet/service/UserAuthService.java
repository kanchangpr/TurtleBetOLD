package com.jetbet.service;

import org.springframework.stereotype.Service;

import com.jetbet.bean.JwtTokenRequest;
import com.jetbet.bean.ResponseBean;



@Service
public interface UserAuthService {

	ResponseBean authUser(JwtTokenRequest userRequest, String transactionId);
}
