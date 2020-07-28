package com.jetbet.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jetbet.bean.UserBean;
import com.jetbet.dto.ChangePasswordDto;
import com.jetbet.dto.ChipsDto;
import com.jetbet.dto.UserControlsDto;
import com.jetbet.dto.UserDetailsRequestDto;
import com.jetbet.dto.UserResponseDto;
import com.jetbet.dto.UserRolesResponseDto;
import com.jetbet.service.UserService;
import com.jetbet.util.JetbetMapper;
import com.jetbet.util.ResourceConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/")
public class UserController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(value=ResourceConstants.USER_ROLES, method=RequestMethod.GET)
	public ResponseEntity<List<UserRolesResponseDto>> getUserRoles(@PathVariable String role) {
		String transactionId = "JB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE USER ROLE METHOD GET*************************");
		List<UserRolesResponseDto> response = userService.getUserRoles(role,transactionId);
		return new ResponseEntity<List<UserRolesResponseDto>>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value=ResourceConstants.PARENT_LIST, method=RequestMethod.GET)
	public ResponseEntity<List<UserRolesResponseDto>> getParentList(@PathVariable String role) {
		String transactionId = "JB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE PARENT LIST METHOD GET*************************");
		List<UserRolesResponseDto> response = userService.getParentList(role,transactionId);
		return new ResponseEntity<List<UserRolesResponseDto>>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value=ResourceConstants.USER_NAME_AVAILABILITY, method=RequestMethod.GET)
	public ResponseEntity<Boolean> checkUserNameAvailability(@PathVariable String userName) {
		String transactionId = "JB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE PARENT LIST METHOD POST*************************");
		//Boolean response=true;
		Boolean response = userService.checkUserNameAvailability(userName,transactionId);
		return new ResponseEntity<Boolean>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value=ResourceConstants.USER_DETAILS, method=RequestMethod.POST)
	public ResponseEntity<UserResponseDto> userRoles(@Valid @RequestBody UserDetailsRequestDto userDetailsRequestDto) {
		String transactionId = "JB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE USER DETAILS METHOD POST*************************");
		UserBean userBean = JetbetMapper.convertUserRoleDtoToUserRoleBean(userDetailsRequestDto);
		UserResponseDto response = userService.addUserDetails(userBean,transactionId);
		return new ResponseEntity<UserResponseDto>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value=ResourceConstants.USER_CONTROLS, method=RequestMethod.PUT)
	public ResponseEntity<UserResponseDto> userControls(@Valid @RequestBody UserControlsDto userControlsDto) {
		String transactionId = "JB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE USER CONTROLS METHOD POST*************************");
		UserResponseDto response = userService.userControls(userControlsDto,transactionId);
		return new ResponseEntity<UserResponseDto>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value=ResourceConstants.CHIPS_ALLOCATIONS, method=RequestMethod.POST)
	public ResponseEntity<UserResponseDto> chipsAllocations(@Valid @RequestBody ChipsDto chipsDto) {
		String transactionId = "JB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE chipsAllocations METHOD POST*************************");
		UserResponseDto response = userService.chipsAllocations(chipsDto,transactionId);
		return new ResponseEntity<UserResponseDto>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value=ResourceConstants.CHIPS_BALANCE, method=RequestMethod.GET)
	public ResponseEntity<List<ChipsDto>> chipsBalance(@PathVariable String userId) {
		String transactionId = "JB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE chipsBalance METHOD POST*************************");
		List<ChipsDto> response = userService.chipsBalance(userId.toUpperCase(),transactionId);
		return new ResponseEntity<List<ChipsDto>>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value=ResourceConstants.CHANGE_PASSWORD, method=RequestMethod.PUT)
	public ResponseEntity<UserResponseDto> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
		String transactionId = "JB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE chipsBalance METHOD POST*************************");
		UserResponseDto response = userService.changePassword(changePasswordDto,transactionId);
		return new ResponseEntity<UserResponseDto>(response,HttpStatus.OK);
	}
	
}
