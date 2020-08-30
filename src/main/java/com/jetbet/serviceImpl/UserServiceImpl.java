package com.jetbet.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jetbet.bean.ChipsBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.PartnershipBean;
import com.jetbet.bean.PlaceBetsBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.bean.StakesBean;
import com.jetbet.bean.UserBean;
import com.jetbet.dao.UserDao;
import com.jetbet.dto.ChangePasswordDto;
import com.jetbet.dto.ChipsDto;
import com.jetbet.dto.UserControlsDto;
import com.jetbet.dto.UserDetailsRequestDto;
import com.jetbet.dto.UserHomeDto;
import com.jetbet.dto.UserResponseDto;
import com.jetbet.dto.UserRolesResponseDto;
import com.jetbet.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Override
	public List<UserBean> getResponse() {
		return userDao.getResponse();
	}

	@Override
	public List<UserRolesResponseDto> getUserRoles(String role, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getUserRoles CLASS UserServiceImpl*************************");
		return userDao.getUserRoles(role,transactionId);
	}

	@Override
	public List<UserRolesResponseDto> getParentList(String userId,String role, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getParentList CLASS UserServiceImpl*************************");
		return userDao.getParentList(userId,role,transactionId);
	}

	@Override
	public Boolean checkUserNameAvailability(String userName, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE checkUserNameAvailability CLASS UserServiceImpl*************************");
		return userDao.checkUserNameAvailability(userName,transactionId);
	}

	@Override
	public UserResponseDto addUserDetails(UserBean userBean, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE addUserDetails CLASS UserServiceImpl*************************");
		return userDao.addUserDetails(userBean,transactionId);
	}

	@Override
	public UserResponseDto userControls(UserControlsDto userControlsDto, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE userControls CLASS UserServiceImpl*************************");
		return userDao.userControls(userControlsDto,transactionId);
	}

	@Override
	public UserResponseDto chipsAllocations(@Valid ChipsDto chipsDto, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE chipsAllocations CLASS UserServiceImpl*************************");
		return userDao.chipsAllocations(chipsDto,transactionId);
	}

	@Override
	public List<ChipsDto> chipsBalance(String userId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE chipsAllocations CLASS UserServiceImpl*************************");
		return userDao.chipsBalance(userId,transactionId);
	}

	@Override
	public UserResponseDto changePassword(ChangePasswordDto changePasswordDto, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE changePassword CLASS UserServiceImpl*************************");
		return userDao.changePassword(changePasswordDto,transactionId);
	}
	@Override
	public List<SportsBean> activeSportsList(String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE activeSportsList CLASS AdminServiceImpl*************************");
		return userDao.activeSportsList(transactionId);
	}

	@Override
	public List<SeriesBean> activeSeriesList(String sportsId,String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE activeSeriesList CLASS AdminServiceImpl*************************");
		return userDao.activeSeriesList(sportsId,transactionId);
	}

	@Override
	public List<MatchBean> activeMatchList(String sportsId ,String seriesId,String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE activeMatchList CLASS AdminServiceImpl*************************");
		return userDao.activeMatchList(sportsId,seriesId,transactionId);
	}

	@Override
	public List<UserBean> getUserDetails(String master, String parent, String userId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getUserDetails CLASS AdminServiceImpl*************************");
		return userDao.getUserDetails(master,parent,userId,transactionId);
	}

	@Override
	public PartnershipBean getPartnershipDetails(String userId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getPartnershipDetails CLASS AdminServiceImpl*************************");
		return userDao.getPartnershipDetails(userId,transactionId);
	}

	@Override
	public PartnershipBean updatePartnershipDetails(PartnershipBean psBean, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE updatePartnershipDetails CLASS AdminServiceImpl*************************");
		return userDao.updatePartnershipDetails(psBean,transactionId);
	}

	@Override
	public Map<Integer, Boolean> psPercentage(PartnershipBean psBean, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE updatePartnershipDetails CLASS AdminServiceImpl*************************");
		return userDao.psPercentage(psBean,transactionId);
	}

	@Override
	public UserResponseDto updateUserDetails(@Valid UserDetailsRequestDto userDetailsRequestDto, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE updateUserDetails CLASS AdminServiceImpl*************************");
		return userDao.updateUserDetails(userDetailsRequestDto,transactionId);
	}

	@Override
	public List<ChipsBean> getChipsHistory(String userId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getChipsHistory CLASS AdminServiceImpl*************************");
		return userDao.getChipsHistory(userId,transactionId);
	}

	@Override
	public UserResponseDto changeUserPassword(@Valid ChangePasswordDto changePasswordDto, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE changePassword CLASS UserServiceImpl*************************");
		return userDao.changeUserPassword(changePasswordDto,transactionId);
	}

	@Override
	public StakesBean geStakesDetails(String userId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE geStakesDetails CLASS UserServiceImpl*************************");
		return userDao.geStakesDetails(userId,transactionId);
	}

	@Override
	public StakesBean updateStakesDetails(@Valid StakesBean stakesBean, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE updateStakesDetails CLASS UserServiceImpl*************************");
		return userDao.updateStakesDetails(stakesBean,transactionId);
	}

	@Override
	public Double getLiability(String isBackLay, double odds, double stakes, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getLiability CLASS UserServiceImpl*************************");
		return userDao.getLiability(isBackLay,odds,stakes,transactionId);
	}

	@Override
	public UserResponseDto placeBets(@Valid PlaceBetsBean placeBetsBean, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE placeBets CLASS UserServiceImpl*************************");
		return userDao.placeBets(placeBetsBean,transactionId);
	}

	@Override
	public List<Object> userReport(String type, String userId, String fromDate, String toDate,String searchParam, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE userReport CLASS UserServiceImpl*************************");
		return userDao.userReport( type,  userId,  fromDate,  toDate, searchParam, transactionId);
	}

	@Override
	public List<UserHomeDto> userHome(String sportsId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE userHome CLASS UserServiceImpl*************************");
		return userDao.userHome( sportsId,transactionId);
	}

	@Override
	public List<PlaceBetsBean> openPlacedBets(String userId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE userHome CLASS UserServiceImpl*************************");
		return userDao.openPlacedBets( userId,transactionId);
	}


}
