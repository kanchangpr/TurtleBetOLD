package com.jetbet.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jetbet.bean.JwtTokenRequest;
import com.jetbet.bean.ResponseBean;
import com.jetbet.bean.UserAuthBean;
import com.jetbet.repository.UserAuthRepository;
import com.jetbet.util.ApplicationConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserAuthDao {
	
	@Autowired
	UserAuthRepository userRepository;
	
	public ResponseBean authUser(JwtTokenRequest userRequest,String transactionId) {
		log.info("["+transactionId+"]************authUserDAO*************************");
		boolean isValid=true;
		
		String user=userRequest.getUsername().toUpperCase();
		String password=userRequest.getPassword();
		
		log.info("["+transactionId+"] UserName:  "+user);
		log.info("["+transactionId+"] Password:  "+password);
		
		ResponseBean responseBean = new ResponseBean();
		UserAuthBean userBean=userRepository.findByUserId(user);
		if(userBean!=null) {
			UserAuthBean userBean1=userRepository.findByUserIdAndPassword(user, password);
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
						responseBean.setStatus(ApplicationConstants.SUCCESS);
						responseBean.setErrorMsg("call Authorization API to fetch token");
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
