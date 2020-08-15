package com.jetbet.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jetbet.bean.ChipsBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.PartnershipBean;
import com.jetbet.bean.PlaceBetsBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.bean.StakesBean;
import com.jetbet.bean.UserBean;
import com.jetbet.dto.ChangePasswordDto;
import com.jetbet.dto.ChipsDto;
import com.jetbet.dto.UserControlsDto;
import com.jetbet.dto.UserDetailsRequestDto;
import com.jetbet.dto.UserHomeDto;
import com.jetbet.dto.UserResponseDto;
import com.jetbet.dto.UserRolesResponseDto;
import com.jetbet.service.UserService;
import com.jetbet.util.JetbetMapper;
import com.jetbet.util.ResourceConstants;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "http://localhost:8080")
@Slf4j
@RestController
@RequestMapping(value = "/User")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = ResourceConstants.USER_ROLES, method = RequestMethod.GET)
	public ResponseEntity<List<UserRolesResponseDto>> getUserRoles(
			@RequestParam(value = "role", required = true) String role) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info(
				"[" + transactionId + "]*************************INSIDE USER ROLE METHOD GET*************************");
		List<UserRolesResponseDto> response = userService.getUserRoles(role, transactionId);
		return new ResponseEntity<List<UserRolesResponseDto>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.PARENT_LIST, method = RequestMethod.GET)
	public ResponseEntity<List<UserRolesResponseDto>> getParentList(
			@RequestParam(value = "role", required = true) String role) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE PARENT LIST METHOD GET*************************");
		List<UserRolesResponseDto> response = userService.getParentList(role, transactionId);
		return new ResponseEntity<List<UserRolesResponseDto>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.USER_NAME_AVAILABILITY, method = RequestMethod.GET)
	public ResponseEntity<Boolean> checkUserNameAvailability(
			@RequestParam(value = "userName", required = true) String userName) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE PARENT LIST METHOD POST*************************");
		Boolean response = userService.checkUserNameAvailability(userName, transactionId);
		return new ResponseEntity<Boolean>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.USER_DETAILS, method = RequestMethod.POST)
	public ResponseEntity<UserResponseDto> addUserDetails(
			@Valid @RequestBody UserDetailsRequestDto userDetailsRequestDto) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE USER DETAILS METHOD POST*************************");
		UserBean userBean = JetbetMapper.convertUserRoleDtoToUserRoleBean(userDetailsRequestDto);
		UserResponseDto response = userService.addUserDetails(userBean, transactionId);
		return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.USER_CONTROLS, method = RequestMethod.PUT)
	public ResponseEntity<UserResponseDto> userControls(@Valid @RequestBody UserControlsDto userControlsDto) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE USER CONTROLS METHOD POST*************************");
		UserResponseDto response = userService.userControls(userControlsDto, transactionId);
		return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.CHIPS_ALLOCATIONS, method = RequestMethod.POST)
	public ResponseEntity<UserResponseDto> chipsAllocations(@Valid @RequestBody ChipsDto chipsDto) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE chipsAllocations METHOD POST*************************");
		UserResponseDto response = userService.chipsAllocations(chipsDto, transactionId);
		return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.CHIPS_BALANCE, method = RequestMethod.GET)
	public ResponseEntity<List<ChipsDto>> chipsBalance(@RequestParam(value = "userId", required = true) String userId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE chipsBalance METHOD POST*************************");
		List<ChipsDto> response = userService.chipsBalance(userId.toUpperCase(), transactionId);
		return new ResponseEntity<List<ChipsDto>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.RESET_PASSWORD, method = RequestMethod.PUT)
	public ResponseEntity<UserResponseDto> resetPassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE chipsBalance METHOD POST*************************");
		UserResponseDto response = userService.changePassword(changePasswordDto, transactionId);
		return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.LIST_OF_SPORTS, method = RequestMethod.GET)
	public ResponseEntity<List<SportsBean>> activeSportsList() {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<SportsBean> response = userService.activeSportsList(transactionId);
		return new ResponseEntity<List<SportsBean>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.LIST_OF_SERIES, method = RequestMethod.GET)
	public ResponseEntity<List<SeriesBean>> activeSeriesList(
			@RequestParam(value = "sportsId", required = false) String sportsId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<SeriesBean> response = userService.activeSeriesList(sportsId, transactionId);
		return new ResponseEntity<List<SeriesBean>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.LIST_OF_MATHCES, method = RequestMethod.GET)
	public ResponseEntity<List<MatchBean>> activeMatchList(
			@RequestParam(value = "sportsId", required = false) String sportsId,
			@RequestParam(value = "seriesId", required = false) String seriesId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<MatchBean> response = userService.activeMatchList(sportsId, seriesId, transactionId);
		return new ResponseEntity<List<MatchBean>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.USER_DETAILS, method = RequestMethod.GET)
	public ResponseEntity<List<UserBean>> getUserDetails(
			@RequestParam(value = "master", required = false) String master,
			@RequestParam(value = "parent", required = false) String parent,
			@RequestParam(value = "userId", required = false) String userId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE GET USER DETAILS METHOD POST*************************");
		List<UserBean> response = userService.getUserDetails(master, parent, userId, transactionId);
		return new ResponseEntity<List<UserBean>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.PARTNERSHIP, method = RequestMethod.GET)
	public ResponseEntity<PartnershipBean> getPartnershipDetails(
			@RequestParam(value = "userName", required = true) String userId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE getPartnershipDetails METHOD POST*************************");
		PartnershipBean response = userService.getPartnershipDetails(userId, transactionId);
		return new ResponseEntity<PartnershipBean>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.PARTNERSHIP, method = RequestMethod.PUT)
	public ResponseEntity<PartnershipBean> updatePartnershipDetails(@RequestBody PartnershipBean psBean) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE getPartnershipDetails METHOD POST*************************");
		PartnershipBean response = userService.updatePartnershipDetails(psBean, transactionId);
		return new ResponseEntity<PartnershipBean>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.PARTNERSHIP_PERCENTAGE, method = RequestMethod.POST)
	public ResponseEntity<Map<Integer, Boolean>> psPercentage(@RequestBody PartnershipBean psBean) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE getPartnershipDetails METHOD POST*************************");
		Map<Integer, Boolean> response = userService.psPercentage(psBean, transactionId);
		return new ResponseEntity<Map<Integer, Boolean>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.USER_DETAILS, method = RequestMethod.PUT)
	public ResponseEntity<UserBean> updateUserDetails(@Valid @RequestBody UserDetailsRequestDto userDetailsRequestDto) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE GET USER DETAILS METHOD POST*************************");
		UserBean response = userService.updateUserDetails(userDetailsRequestDto, transactionId);
		return new ResponseEntity<UserBean>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.CHIPS_HISTORY, method = RequestMethod.GET)
	public ResponseEntity<List<ChipsBean>> getChipsHistory(
			@RequestParam(value = "userId", required = true) String userId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info(
				"[" + transactionId + "]*************************INSIDE USER ROLE METHOD GET*************************");
		List<ChipsBean> response = userService.getChipsHistory(userId, transactionId);
		return new ResponseEntity<List<ChipsBean>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.PLACE_BETS, method = RequestMethod.POST)
	public ResponseEntity<UserResponseDto> placeBets(@Valid @RequestBody PlaceBetsBean placeBetsBean) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info(
				"[" + transactionId + "]*************************INSIDE USER ROLE METHOD GET*************************");
		UserResponseDto response = userService.placeBets(placeBetsBean, transactionId);
		return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.CHANGE_PASSWORD, method = RequestMethod.PUT)
	public ResponseEntity<UserResponseDto> changeUserPassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId + "]*********************INSIDE chipsBalance METHOD POST*******************");
		UserResponseDto response = userService.changeUserPassword(changePasswordDto, transactionId);
		return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.STAKES_DETAILS, method = RequestMethod.GET)
	public ResponseEntity<StakesBean> getStakesDetails(@RequestParam(value = "userId", required = true) String userId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId + "]*****************INSIDE chipsBalance METHOD POST*******************");
		StakesBean response = userService.geStakesDetails(userId.toUpperCase(), transactionId);
		return new ResponseEntity<StakesBean>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.STAKES_DETAILS, method = RequestMethod.PUT)
	public ResponseEntity<StakesBean> updateStakesDetails(@Valid @RequestBody StakesBean stakesBean) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId + "]***************INSIDE chipsBalance METHOD POST******************");
		StakesBean response = userService.updateStakesDetails(stakesBean, transactionId);
		return new ResponseEntity<StakesBean>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.LIABILITY, method = RequestMethod.GET)
	public ResponseEntity<Double> getLiability(@RequestParam(value = "odds", required = true) double odds,
			@RequestParam(value = "stakes", required = true) double stakes) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId + "]*************INSIDE chipsBalance METHOD POST**************");
		Double response = userService.getLiability(odds,stakes, transactionId);
		return new ResponseEntity<Double>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = ResourceConstants.USER_REPORT, method = RequestMethod.GET)
	public ResponseEntity<List<Object>> userReport(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId + "]*************INSIDE chipsBalance METHOD POST**************");
		List<Object> response = userService.userReport(type,userId.toUpperCase(),fromDate,toDate, transactionId);
		return new ResponseEntity<List<Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = ResourceConstants.USER_HOME, method = RequestMethod.GET)
	public ResponseEntity<List<UserHomeDto>> userHome(@RequestParam(value = "sportsId", required = true) String sportsId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId + "]*************INSIDE userHome METHOD POST**************");
		List<UserHomeDto> response = userService.userHome(sportsId, transactionId);
		return new ResponseEntity<List<UserHomeDto>>(response, HttpStatus.OK);
	}

}
