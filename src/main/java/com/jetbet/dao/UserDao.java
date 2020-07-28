package com.jetbet.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.jetbet.bean.ChipsBean;
import com.jetbet.bean.UserBean;
import com.jetbet.dto.ChangePasswordDto;
import com.jetbet.dto.ChipsDto;
import com.jetbet.dto.UserControlsDto;
import com.jetbet.dto.UserResponseDto;
import com.jetbet.dto.UserRolesResponseDto;
import com.jetbet.repository.ChipsRepository;
import com.jetbet.repository.UserRepository;
import com.jetbet.util.QueryListConstant;
import com.jetbet.util.ResourceConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDao {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ChipsRepository chipsRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	public List<UserBean> getResponse() {
		return userRepository.findAll();
	}

	@Transactional
	public List<UserRolesResponseDto> getUserRoles(String role, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE addUserRole CLASS UserDao*************************");
		log.info("["+transactionId+"] Role: "+role);
		String getUserRolesSql = QueryListConstant.GET_USER_ROLES_SQL;
		return jdbcTemplate.query(
				getUserRolesSql, new Object[]{role.toUpperCase()},
	            (rs, rowNum) ->
	                    new UserRolesResponseDto(
	                            rs.getString("user_Role")
	                    )
	    );
	}

	@Transactional
	public List<UserRolesResponseDto> getParentList(String role, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE addUserRole CLASS UserDao*************************");
		log.info("["+transactionId+"] Role: "+role);
		String getParentListSql = QueryListConstant.GET_PARENT_LIST_SQL;
		return jdbcTemplate.query(
				getParentListSql, new Object[]{role.toUpperCase()},
	            (rs, rowNum) ->
	                    new UserRolesResponseDto(
	                            rs.getString("USER_ID")
	                    )
	    );
	}

	@Transactional
	public Boolean checkUserNameAvailability(String userName, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE addUserRole CLASS UserDao*************************");
		log.info("["+transactionId+"] userName: "+userName);
		Long count = userRepository.countByUserId(userName);
		log.info("count of existing User: "+count);
		return (count==0) ? true : false;
	}

	@Transactional
	public UserResponseDto addUserDetails(UserBean userBean, String transactionId) {
		UserResponseDto userResponseDto= new UserResponseDto();
		log.info("["+transactionId+"]*************************INSIDE addUserRole CLASS UserDao*************************");
		log.info("["+transactionId+"] USER_ID: "+userBean.getUserId());
		log.info("["+transactionId+"] FULL_NAME: "+userBean.getFullName());
		log.info("["+transactionId+"] USER_ROLE: "+userBean.getUserRole());
		log.info("["+transactionId+"] PARENT: "+userBean.getParent());
		log.info("["+transactionId+"] REMARKS: "+userBean.getRemarks());
		log.info("["+transactionId+"] CREATED_BY: "+userBean.getCreatedBy());
		try {
			userBean=userRepository.findByUserId(userBean.getParent());
			Long parentCount = userRepository.countByUserId(userBean.getParent());
			log.info("["+transactionId+"] User Limit: "+userBean.getUserLimit());
			log.info("["+transactionId+"] Parent Count: "+parentCount);
			if(userBean.getUserLimit()<parentCount) {
				Long count = userRepository.countByUserId(userBean.getUserId());
				if(count == 0) {
					UserBean userRes=userRepository.save(userBean);
					if(userRes.getUserId()==userBean.getUserId()) {
						userResponseDto.setStatus(ResourceConstants.SUCCESS);
						userResponseDto.setErrorMsg(ResourceConstants.INSERTED);
					}else {
						userResponseDto.setStatus(ResourceConstants.FAILED);
						userResponseDto.setErrorCode(ResourceConstants.ERR_002);
						userResponseDto.setErrorMsg(ResourceConstants.INSERTION_FAILED);
					}
				}else {
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_001);
					userResponseDto.setErrorMsg(ResourceConstants.EXIST);
				}
			}else {
				userResponseDto.setStatus(ResourceConstants.FAILED);
				userResponseDto.setErrorCode(ResourceConstants.ERR_008);
				userResponseDto.setErrorMsg(ResourceConstants.USER_LIMIT_EXCEED);
			}
			
		}catch (Exception e) {
			userResponseDto.setStatus(ResourceConstants.EXCEPTION);
			userResponseDto.setErrorCode(ResourceConstants.ERR_EXCEPTION);
			userResponseDto.setErrorMsg(e.getMessage());
			e.printStackTrace();
		}
		return userResponseDto;
	}

	@Transactional
	public UserResponseDto userControls(UserControlsDto userControlsDto, String transactionId) {
		UserResponseDto userResponseDto= new UserResponseDto();
		log.info("["+transactionId+"]*************************INSIDE userControls CLASS UserDao*************************");
		log.info("["+transactionId+"] userId: "+userControlsDto.getUserId());
		log.info("["+transactionId+"] Action: "+userControlsDto.getAction());
		log.info("["+transactionId+"] userName: "+userControlsDto.getUserName());
		String actString=userControlsDto.getAction().toUpperCase();
		String userString=userControlsDto.getUserId().toUpperCase();
		String updateByString=userControlsDto.getUserName().toUpperCase();
		try {
			Map <String,String> actionMap = new HashMap<String,String>();
			actionMap.put(ResourceConstants.LOCK_USER, "ISUSERLOCK='Y'");
			actionMap.put(ResourceConstants.UNLOCK_USER, "ISUSERLOCK='N'");
			actionMap.put(ResourceConstants.LOCK_BETTING, "ISBETTINGLOCK='Y'");
			actionMap.put(ResourceConstants.UNLOCK_BETTING, "ISBETTINGLOCK='N'");
			actionMap.put(ResourceConstants.OPEN_ACC, "ISACTIVE='Y'");
			actionMap.put(ResourceConstants.CLOSE_ACC, "ISACTIVE='N'");
			
			if(actionMap.containsKey(actString)) {
				long userCount=userRepository.countByUserId(userString);
				if(userCount>0) {
					String sqlString=" WITH RECURSIVE TAB AS( SELECT USER_ID, PARENT FROM JETBET.JB_USER_DETAILS WHERE USER_ID =? "
							+ "UNION ALL SELECT J.USER_ID, J.PARENT FROM JETBET.JB_USER_DETAILS J, TAB WHERE J.PARENT = TAB.USER_ID ) "
							+ "UPDATE JETBET.JB_USER_DETAILS J1 SET "+actionMap.get(actString)+" , LASTUPDATEDDATE=CURRENT_TIMESTAMP, LASTUPDATEBY=? "
							+ "WHERE J1.USER_ID IN ( SELECT USER_ID FROM TAB)";
					
					log.info("["+transactionId+"] sqlString: "+sqlString);
					int count=jdbcTemplate.update(sqlString, new Object[]{userString,updateByString});
					if(count==0) {
						userResponseDto.setStatus(ResourceConstants.FAILED);
						userResponseDto.setErrorCode(ResourceConstants.ERR_003);
						userResponseDto.setErrorMsg(ResourceConstants.UPDATION_FAILED);
					}else {
						userResponseDto.setStatus(ResourceConstants.SUCCESS);
						userResponseDto.setErrorMsg(ResourceConstants.UPDATED);
					}
				}else {
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_010);
					userResponseDto.setErrorMsg(ResourceConstants.USER_NOT_EXIST);
				}
			}else {
				userResponseDto.setStatus(ResourceConstants.FAILED);
				userResponseDto.setErrorCode(ResourceConstants.ERR_009);
				userResponseDto.setErrorMsg(ResourceConstants.ACTION_NOT_ALLOWED);
			}
		}catch (Exception e) {
			userResponseDto.setStatus(ResourceConstants.EXCEPTION);
			userResponseDto.setErrorCode(ResourceConstants.ERR_EXCEPTION);
			userResponseDto.setErrorMsg(e.getMessage());
			e.printStackTrace();
		}
		return userResponseDto;
	}

	/** This is method id used to maintain chips withdraw and deposit and all chips calculation
	**  fromUser -- LoggedInUser
	**	toUser -- AnotherUser 
	**	chips -- chips to be deposited or withdrawn
	**	action -- withdraw/deposit
	*/
	@Transactional
	public UserResponseDto chipsAllocations(@Valid ChipsDto chipsDto, String transactionId) {
		UserResponseDto userResponseDto= new UserResponseDto();
		ChipsBean FromUserAccBean= new ChipsBean();
		ChipsBean toUserAccBean= new ChipsBean();
		ChipsBean FromUserAccBeanRes= new ChipsBean();
		ChipsBean toUserAccBeanRes= new ChipsBean();
		double currentChipsInFromUserAcc= 0;
		double currentChipsInToUserAcc= 0;
		double totalChipsInFromAcc = 0;
		double totalChipsInToAcc = 0;
		double depositWithdrawChips;
		String updateFromUserAccChipsSql="";
		String updateToUserAccChipsSql="";
		String returnMsgString="";
		boolean errorRes=false;
		log.info("["+transactionId+"]*************************INSIDE chipsAllocations CLASS UserDao*************************");
		log.info("["+transactionId+"] Chips: "+chipsDto.getChips());
		log.info("["+transactionId+"] Action: "+chipsDto.getAction());
		log.info("["+transactionId+"] fromUser: "+chipsDto.getFromUser());
		log.info("["+transactionId+"] toUser: "+chipsDto.getToUser());
		log.info("["+transactionId+"] userName: "+chipsDto.getUserName());
		String actString=chipsDto.getAction().toUpperCase();
		String createdByString=chipsDto.getUserName().toUpperCase();
		String fromUser=chipsDto.getFromUser().toUpperCase();
		String toUser=chipsDto.getToUser().toUpperCase();
		try {
			depositWithdrawChips=chipsDto.getChips();
			log.info("["+transactionId+"] depositWithdrawChipst: "+depositWithdrawChips);
			
			UserBean fromUserRes=userRepository.findByUserId(fromUser);
			currentChipsInFromUserAcc=fromUserRes.getChips();
			log.info("["+transactionId+"] Chips in From User Account: "+currentChipsInFromUserAcc);
			
			UserBean toUserRes=userRepository.findByUserId(toUser);
			currentChipsInToUserAcc=toUserRes.getChips();
			log.info("["+transactionId+"] Chips in To User Account: "+currentChipsInToUserAcc);
			
			if (actString.equalsIgnoreCase(ResourceConstants.DEPOSIT)) {
				log.info("["+transactionId+"] INSIDE Deposit");
				returnMsgString="Chips Deposited Successfully";
				if(depositWithdrawChips>currentChipsInFromUserAcc) {
					errorRes=true;
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_004);
					userResponseDto.setErrorMsg(ResourceConstants.INSUFFICIENT_AMOUNT);
				}else if(depositWithdrawChips<=currentChipsInFromUserAcc) {
					totalChipsInFromAcc=currentChipsInFromUserAcc-depositWithdrawChips;
					totalChipsInToAcc=currentChipsInToUserAcc+depositWithdrawChips;
					
					toUserAccBean.setDeposit(depositWithdrawChips);
					toUserAccBean.setTotalChips(totalChipsInToAcc);
					
					FromUserAccBean.setWithdraw(depositWithdrawChips);
					FromUserAccBean.setTotalChips(totalChipsInFromAcc);
				}
			}else if (actString.equalsIgnoreCase(ResourceConstants.WITHDRAW)) {
				log.info("["+transactionId+"] INSIDE Withdraw");
				returnMsgString="Chips Withdrawn Successfully";
				if(depositWithdrawChips>currentChipsInToUserAcc) {
					errorRes=true;
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_004);
					userResponseDto.setErrorMsg(ResourceConstants.INSUFFICIENT_AMOUNT);
				}else if(depositWithdrawChips<=currentChipsInToUserAcc) {
					totalChipsInFromAcc=currentChipsInFromUserAcc+depositWithdrawChips;
					totalChipsInToAcc=currentChipsInToUserAcc-depositWithdrawChips;
					
					toUserAccBean.setWithdraw(depositWithdrawChips);
					toUserAccBean.setTotalChips(totalChipsInToAcc);
					
					FromUserAccBean.setDeposit(depositWithdrawChips);
					FromUserAccBean.setTotalChips(totalChipsInFromAcc);
				}
			}
			
			log.info("["+transactionId+"] errorRes:: "+errorRes);
			
			if(!errorRes) {
				log.info("["+transactionId+"] totalChipsInFromAcc:: "+totalChipsInFromAcc);
				log.info("["+transactionId+"] totalChipsInToAcc:: "+totalChipsInToAcc);
				
				updateFromUserAccChipsSql=QueryListConstant.UPDATE_FROM_USER_ACC_CHIPS_SQL;
				int updateFromUserAccChipsrowCount=jdbcTemplate.update(updateFromUserAccChipsSql, new Object[]{
						totalChipsInFromAcc,createdByString,fromUser});
				log.info("["+transactionId+"] updateFromUserAccChipsrowCount:: "+updateFromUserAccChipsrowCount);
				
				
				updateToUserAccChipsSql=QueryListConstant.UPDATE_TO_USER_ACC_CHIPS_SQL;
				int updateToUserAccChipsrowCount=jdbcTemplate.update(updateToUserAccChipsSql, new Object[]{
						totalChipsInToAcc,createdByString,toUser});
				log.info("["+transactionId+"] updateToUserAccChipsrowCount:: "+updateToUserAccChipsrowCount);
				
				
				toUserAccBean.setUserId(toUser);
				toUserAccBean.setFromUser(fromUser); 
				toUserAccBean.setCreatedBy(createdByString);
				toUserAccBeanRes=chipsRepository.saveAndFlush(toUserAccBean);
				
				FromUserAccBean.setUserId(fromUser);
				FromUserAccBean.setFromUser(toUser);
				FromUserAccBean.setCreatedBy(createdByString);
				FromUserAccBeanRes=chipsRepository.saveAndFlush(FromUserAccBean);
				
				if(updateFromUserAccChipsrowCount>0 && updateToUserAccChipsrowCount>0 
						&& toUserAccBeanRes.getTotalChips()==toUserAccBeanRes.getTotalChips()
							&& FromUserAccBeanRes.getTotalChips()==FromUserAccBeanRes.getTotalChips()) {
					userResponseDto.setStatus(ResourceConstants.SUCCESS);
					userResponseDto.setErrorMsg(returnMsgString);
				}else {
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_005);
					userResponseDto.setErrorMsg(ResourceConstants.CHIPS_TRANSFER_FAILED);
				}
			}
		}catch (Exception e) {
			userResponseDto.setStatus(ResourceConstants.EXCEPTION);
			userResponseDto.setErrorCode(ResourceConstants.ERR_EXCEPTION);
			userResponseDto.setErrorMsg(e.getMessage());
			e.printStackTrace();
		}
		return userResponseDto;
	}

	@Transactional
	public List<ChipsDto> chipsBalance(String userId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE chipsBalance CLASS UserDao*************************");
		log.info("["+transactionId+"] Chips: "+userId);
		String getChipsBalance=QueryListConstant.GET_CHIPS_BALANCE_SQL;
		List<Map<String, Object>> chipsMap=jdbcTemplate.queryForList(getChipsBalance, new Object[]{
				userId,userId,userId});
		 List<ChipsDto> chipsList = new ArrayList<ChipsDto>();
		for (Map<String, Object> row : chipsMap) {
			ChipsDto chipsDto = new ChipsDto();
			chipsDto.setToUser(row.get("USER_NAME").toString());
			chipsDto.setChips(Long.parseLong(row.get("CHIPS").toString()));
			chipsList.add(chipsDto);
	    } 
		log.info("["+transactionId+"] chipsMap:: "+chipsMap);
		return chipsList;
	}

	public UserResponseDto changePassword(ChangePasswordDto changePasswordDto, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE changePassword CLASS UserDao*************************");
		UserResponseDto userResponseDto = new UserResponseDto();
		log.info("["+transactionId+"] UserId: "+changePasswordDto.getUserId());
		log.info("["+transactionId+"] Password: "+changePasswordDto.getPassword());
		log.info("["+transactionId+"] ConfirmPassword: "+changePasswordDto.getConfirmPassword());
		log.info("["+transactionId+"] LoggedInUser: "+changePasswordDto.getLoggedInUser());
		try {
			String loggedInUserString=changePasswordDto.getLoggedInUser().toUpperCase();
			String userId=changePasswordDto.getUserId().toUpperCase();
			String passwoord=changePasswordDto.getPassword();
			String confirmPass=changePasswordDto.getConfirmPassword();
			if(passwoord.equals(confirmPass)) {
				String updatePasswordSql=QueryListConstant.UPDATE_PASSWORD_SQL;
				int updatePasswordCount=jdbcTemplate.update(updatePasswordSql, new Object[]{
						confirmPass,loggedInUserString,userId});
				log.info("["+transactionId+"] updatePasswordCount:: "+updatePasswordCount);
				if(updatePasswordCount>0 && updatePasswordCount==1) {
					userResponseDto.setStatus(ResourceConstants.SUCCESS);
					userResponseDto.setErrorMsg(ResourceConstants.PASSWORD_UPDATED);
				}else {
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_006);
					userResponseDto.setErrorMsg(ResourceConstants.PASSWORD_UPDATE_FAILED);
				}
			}else {
				userResponseDto.setStatus(ResourceConstants.FAILED);
				userResponseDto.setErrorCode(ResourceConstants.ERR_007);
				userResponseDto.setErrorMsg(ResourceConstants.PASSWORD_NOT_MATCH);
			}
		}catch (Exception e) {
			userResponseDto.setStatus(ResourceConstants.EXCEPTION);
			userResponseDto.setErrorCode(ResourceConstants.ERR_EXCEPTION);
			userResponseDto.setErrorMsg(e.getMessage());
			e.printStackTrace();
		}
		return userResponseDto;
	}
	
	
}
