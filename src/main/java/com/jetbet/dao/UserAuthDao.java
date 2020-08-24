package com.jetbet.dao;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.jetbet.auth.authentication.JwtTokenUtil;
import com.jetbet.auth.authentication.resources.JwtAuthenticationRestController;
import com.jetbet.auth.authentication.resources.JwtTokenRequest;
import com.jetbet.auth.authentication.resources.JwtTokenResponse;
import com.jetbet.bean.ResponseBean;
import com.jetbet.bean.UserBean;
import com.jetbet.bean.UserLoginBean;
import com.jetbet.repository.UserLoginRepository;
import com.jetbet.repository.UserRepository;
import com.jetbet.util.ApplicationConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserAuthDao {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserLoginRepository userLoginRepository;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Transactional
	public ResponseBean authUser(JwtTokenRequest userRequest,String transactionId) {
		log.info("["+transactionId+"]************authUserDAO*************************");
		boolean isValid=true;
		UserLoginBean userLoginBean=new UserLoginBean();
		JwtAuthenticationRestController jwtAuthenticationRestController=new JwtAuthenticationRestController();
		String user=userRequest.getUsername().toUpperCase();
		String password=userRequest.getPassword();
		
		log.info("["+transactionId+"] UserName:  "+user);
		log.info("["+transactionId+"] Password:  "+password);
		
		ResponseBean responseBean = new ResponseBean();
		UserBean userBean=userRepository.findByUserId(user);
		if(userBean!=null) {
			UserBean userBean1=userRepository.findByUserIdAndPassword(user, password);
			if(userBean1!=null) {
				if(userBean1.getUserId().equalsIgnoreCase(user) && userBean1.getPassword().equals(password)) {
					if(userBean1.getIsActive().equalsIgnoreCase("N")) {
						isValid=false;
						responseBean.setStatus(ApplicationConstants.FAILED);
						responseBean.setErrorCode(ApplicationConstants.ERROR_CODE_003);
						responseBean.setErrorMsg(ApplicationConstants.ERROR_MSG_003);
					}
					if(userBean1.getIsUserLock().equalsIgnoreCase("Y")){
						isValid=false;
						responseBean.setStatus(ApplicationConstants.FAILED);
						responseBean.setErrorCode(ApplicationConstants.ERROR_CODE_004);
						responseBean.setErrorMsg(ApplicationConstants.ERROR_MSG_004);
					}
					
					if(isValid) {
						
						
						userLoginBean.setUserId(userBean1.getUserId());
						userLoginBean.setUserRole(userBean1.getUserRole());
						userLoginBean.setUserParent(userBean1.getParent());
						
						userLoginRepository.save(userLoginBean);
						log.info(userLoginBean.toString());
						JwtTokenRequest authenticationRequest=new JwtTokenRequest();
						authenticationRequest.setUsername(userLoginBean.getUserId());
						final String token = jwtTokenUtil.generateTokenUsingData(userLoginBean.getUserId(),userLoginBean.getUserRole(),userLoginBean.getUserParent());
						log.info("token::"+token);
						if(userBean1.getIsPwdUpdated().equalsIgnoreCase("N")) {
							responseBean.setStatus(ApplicationConstants.FAILED);
							responseBean.setErrorCode(ApplicationConstants.ERROR_CODE_006);
							responseBean.setErrorMsg(ApplicationConstants.ERROR_MSG_006);
						}else {
							responseBean.setStatus(ApplicationConstants.SUCCESS);
							responseBean.setErrorMsg(ApplicationConstants.ERROR_MSG_005);
							responseBean.setUserName(userLoginBean.getUserId());
							responseBean.setUserRole(userLoginBean.getUserRole());
							responseBean.setUserParent(userLoginBean.getUserParent());
						}
						
						responseBean.setToken(token);
						
					}
				}else {
					responseBean.setStatus(ApplicationConstants.FAILED);
					responseBean.setErrorCode(ApplicationConstants.ERROR_CODE_002);
					responseBean.setErrorMsg(ApplicationConstants.ERROR_MSG_002);
				}
			}else {
				responseBean.setStatus(ApplicationConstants.FAILED);
				responseBean.setErrorCode(ApplicationConstants.ERROR_CODE_002);
				responseBean.setErrorMsg(ApplicationConstants.ERROR_MSG_002);
			}
		}else {
			responseBean.setStatus(ApplicationConstants.FAILED);
			responseBean.setErrorCode(ApplicationConstants.ERROR_CODE_001);
			responseBean.setErrorMsg(ApplicationConstants.ERROR_MSG_001);
		}
		log.info("responseBean:: "+responseBean);
		return responseBean;
	}


}
