package com.jetbet.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jetbet.auth.authentication.resources.JwtTokenRequest;
import com.jetbet.bean.ResponseBean;
import com.jetbet.dao.UserAuthDao;
import com.jetbet.service.UserAuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserAuthServiceImpl implements UserAuthService{

	@Autowired
	UserAuthDao userAuthDao;
	
	@Override
	public ResponseBean authUser(JwtTokenRequest userRequest, String transactionId) {
		ResponseBean response=userAuthDao.authUser(userRequest,transactionId);
		return response;
	}
}
