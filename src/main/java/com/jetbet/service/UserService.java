package com.jetbet.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.jetbet.bean.UserBean;
import com.jetbet.dto.ChangePasswordDto;
import com.jetbet.dto.ChipsDto;
import com.jetbet.dto.UserControlsDto;
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

}
