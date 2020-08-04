package com.jetbet.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.jetbet.bean.ChipsBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.PartnershipBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.bean.UserBean;
import com.jetbet.dto.ChangePasswordDto;
import com.jetbet.dto.ChipsDto;
import com.jetbet.dto.UserControlsDto;
import com.jetbet.dto.UserDetailsRequestDto;
import com.jetbet.dto.UserResponseDto;
import com.jetbet.dto.UserRolesResponseDto;

@Service
public interface UserService {

	List<UserBean> getResponse();

	List<UserRolesResponseDto> getUserRoles(String role, String transactionId);

	List<UserRolesResponseDto> getParentList(String role, String transactionId);

	Boolean checkUserNameAvailability(String userName, String transactionId);

	UserResponseDto addUserDetails(UserBean userBean, String transactionId);

	UserResponseDto userControls(UserControlsDto userControlsDto, String transactionId);

	UserResponseDto chipsAllocations(@Valid ChipsDto chipsDto, String transactionId);

	List<ChipsDto> chipsBalance(String userId, String transactionId);

	UserResponseDto changePassword(@Valid ChangePasswordDto changePasswordDto, String transactionId);
	
	List<SportsBean> activeSportsList(String transactionId);

	List<SeriesBean> activeSeriesList(String sportsId , String transactionId);

	List<MatchBean> activeMatchList(String sportsId ,String seriesId,String transactionId);

	List<UserBean> getUserDetails(String parent, String userId, String transactionId);

	PartnershipBean getPartnershipDetails(String userId, String transactionId);

	PartnershipBean updatePartnershipDetails(PartnershipBean psBean, String transactionId);

	Map<Integer, Boolean> psPercentage(PartnershipBean psBean, String transactionId);

	UserBean updateUserDetails(@Valid UserDetailsRequestDto userDetailsRequestDto, String transactionId);

	List<ChipsBean> getChipsHistory(String userId, String transactionId);

}
