package com.jetbet.util;

import javax.validation.Valid;

import com.jetbet.bean.UserBean;
import com.jetbet.dto.UserDetailsRequestDto;

public class JetbetMapper {
	public static UserBean convertUserRoleDtoToUserRoleBean(@Valid UserDetailsRequestDto userRolesRequestDto) {
		UserBean userBean=new UserBean();
		userBean.setUserId(ResourceConstants.checkNulString(userRolesRequestDto.getUserId()).toUpperCase().trim());
		userBean.setFullName(ResourceConstants.checkNulString(userRolesRequestDto.getFullName()).trim());
		userBean.setPassword(ResourceConstants.checkNulString(userRolesRequestDto.getPassword()).trim());
		userBean.setUserRole(ResourceConstants.checkNulString(userRolesRequestDto.getUserRole()).toUpperCase().trim());
		userBean.setParent(ResourceConstants.checkNulString(userRolesRequestDto.getParent()).toUpperCase().trim());
		userBean.setRemarks(ResourceConstants.checkNulString(userRolesRequestDto.getRemarks()).trim());
		userBean.setCreatedBy(ResourceConstants.checkNulString(userRolesRequestDto.getUserName()).toUpperCase().trim());
		userBean.setPartnership(userRolesRequestDto.getPartnership());
		userBean.setOddsCommission(userRolesRequestDto.getOddsCommission());
		userBean.setSessionCommission(userRolesRequestDto.getSessionCommission());
		userBean.setBetDelay(userRolesRequestDto.getBetDelay());
		userBean.setSessionDelay(userRolesRequestDto.getSessionDelay());
		userBean.setUserLimit(userRolesRequestDto.getUserLimit());
		userBean.setMaxProfit(userRolesRequestDto.getMaxProfit());
		userBean.setMaxLoss(userRolesRequestDto.getMaxLoss());
		userBean.setOddsMaxStake(userRolesRequestDto.getOddsMaxStake());
		userBean.setGoingInPlayStake(userRolesRequestDto.getGoingInPlayStake());
		userBean.setSessionMaxStake(userRolesRequestDto.getSessionMaxStake());
		return userBean;
		
	}

}
