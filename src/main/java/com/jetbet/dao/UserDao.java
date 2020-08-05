package com.jetbet.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
import com.jetbet.repository.ChipsRepository;
import com.jetbet.repository.MatchRepository;
import com.jetbet.repository.PartnershipRepository;
import com.jetbet.repository.SeriesRepository;
import com.jetbet.repository.SportsRepository;
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
	
	@Autowired
	private SportsRepository sportsRepository;
	
	@Autowired
	private SeriesRepository seriesRepository;
	
	@Autowired
	private MatchRepository matchRepository;
	
	@Autowired
	private PartnershipRepository partnershipRepository;
	
	
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
		log.info("["+transactionId+"] userName: "+userName.toUpperCase());
		Long count;
		count = userRepository.countByUserId(userName.toUpperCase());
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
		
		String userId=userBean.getUserId();
		String userParent=userBean.getParent();
		String userRole=userBean.getUserRole();
		int partnership=userBean.getPartnership();
		String createdBy=userBean.getCreatedBy();
		PartnershipBean psBean= new PartnershipBean();
		try {
			UserBean parentBean=userRepository.findFirst1ByUserIdOrderByFullName(userBean.getParent());
			Long userCountofParent = userRepository.countByParent(userBean.getParent());
			log.info("["+transactionId+"] Parent's User Limit: "+parentBean.getUserLimit());
			log.info("["+transactionId+"] Parent's User Count: "+userCountofParent);
			if(parentBean.getUserLimit()>userCountofParent) {
				Long userCount = userRepository.countByUserId(userBean.getUserId());
				final int totalStake=100;
				int adminStake=0;
				int supermasterStake=0;
				int masterStake=0;
				boolean isValid=true;
				if(userCount == 0) {
					
					psBean.setUserId(userId);
					psBean.setUserRole(userRole);
					psBean.setParent(userParent);
					psBean.setCreatedBy(createdBy);
					
					if(userRole.equalsIgnoreCase(ResourceConstants.SUPERMASTER)) {
						adminStake=totalStake-partnership;
						supermasterStake=partnership;
						psBean.setMastrerStake(masterStake);
						psBean.setSupermasterStake(supermasterStake);
						psBean.setAdminStake(adminStake);
						if(masterStake+supermasterStake+adminStake!=100) {
							isValid=false;
						}
					}else if(userRole.equalsIgnoreCase(ResourceConstants.MASTER)) {
						PartnershipBean psBeanObj= new PartnershipBean();
						psBeanObj=partnershipRepository.findByUserId(userParent);
						int admStake=psBeanObj.getAdminStake();
						int smStake=psBeanObj.getSupermasterStake();
						masterStake=partnership;
						adminStake=admStake;
						supermasterStake=smStake-masterStake;
						psBean.setMastrerStake(masterStake);
						psBean.setSupermasterStake(supermasterStake);
						psBean.setAdminStake(adminStake);
						if(masterStake+supermasterStake+adminStake!=100) {
							isValid=false;
						}
					}
					
					log.info("["+transactionId+"] adminStake: "+adminStake);
					log.info("["+transactionId+"] supermasterStake: "+supermasterStake);
					log.info("["+transactionId+"] masterStake: "+masterStake);
					
					if(isValid) {
						PartnershipBean psBeanRes= partnershipRepository.saveAndFlush(psBean);
						if(psBeanRes.getUserId()==userBean.getUserId()) {
							
							userBean.setPartnership(psBeanRes.getId());
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
							userResponseDto.setErrorCode(ResourceConstants.ERR_002);
							userResponseDto.setErrorMsg(ResourceConstants.INSERTION_FAILED);
						}
					}else {
						userResponseDto.setStatus(ResourceConstants.FAILED);
						userResponseDto.setErrorCode(ResourceConstants.ERR_011);
						userResponseDto.setErrorMsg(ResourceConstants.PARTNERSHIP_INVALID);
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
			
			UserBean fromUserRes=userRepository.findFirst1ByUserIdOrderByFullName(fromUser);
			currentChipsInFromUserAcc=fromUserRes.getChips();
			log.info("["+transactionId+"] Chips in From User Account: "+currentChipsInFromUserAcc);
			
			UserBean toUserRes=userRepository.findFirst1ByUserIdOrderByFullName(toUser);
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
			chipsDto.setUserRole(row.get("USER_ROLE").toString());
			chipsDto.setToUser(row.get("USER_ID").toString());
			chipsDto.setChips(Long.parseLong(row.get("CHIPS").toString()));
			chipsList.add(chipsDto);
	    } 
		log.info("["+transactionId+"] chipsMap:: "+chipsMap);
		return chipsList;
	}

	@Transactional
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
	
	@Transactional
	public List<SportsBean> activeSportsList(String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE sportsList CLASS UserDao*************************");
		List<SportsBean> responseBeanList = new ArrayList<SportsBean>();
		responseBeanList=sportsRepository.findByIsActiveOrderBySportsName("Y");
		log.info("["+transactionId+"] responseBeanList:  "+responseBeanList);
		return responseBeanList;
	}
	
	@Transactional
	public List<SeriesBean> activeSeriesList(String sportsId,String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE seriesList CLASS UserDao*************************");
		log.info("["+transactionId+"]sportId:: "+sportsId);
		String isActive="Y";
		List<SeriesBean> responseBeanList = new ArrayList<SeriesBean>();
		if(StringUtils.isBlank(sportsId)) {
			responseBeanList=seriesRepository.findByIsActiveOrderBySportId(isActive);
		}else {
			responseBeanList=seriesRepository.findBySportIdAndIsActiveOrderBySportId(sportsId, isActive);
		}
		log.info("["+transactionId+"] responseBeanList:  "+responseBeanList);
		return responseBeanList;
	}
	
	@Transactional
	public List<MatchBean> activeMatchList(String sportId,String seriesId,String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE matchList CLASS UserDao*************************");
		log.info("["+transactionId+"]sportId:: "+sportId);
		log.info("["+transactionId+"]seriesId:: "+seriesId);
		String isActive="Y";
		List<MatchBean> responseBeanList = new ArrayList<MatchBean>();
		if(!StringUtils.isBlank(sportId) && !StringUtils.isBlank(seriesId)) {
			responseBeanList=matchRepository.findBySportIdAndSeriesIdAndIsActive(sportId, seriesId, isActive);
		}else if(!StringUtils.isBlank(sportId)) {
			responseBeanList=matchRepository.findBySportIdAndIsActive(sportId, isActive);
		}else if(!StringUtils.isBlank(seriesId)) {
			responseBeanList=matchRepository.findBySeriesIdAndIsActive(seriesId, isActive);
		} else if(StringUtils.isBlank(sportId) && StringUtils.isBlank(seriesId)) {
			responseBeanList=matchRepository.findByIsActive(isActive);
		}
		log.info("["+transactionId+"] responseBeanList:  "+responseBeanList);
		return responseBeanList;
	}

	public List<UserBean> getUserDetails(String master, String parent, String userId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getUserDetails CLASS UserDao*************************");
		log.info("["+transactionId+"]parent:: "+master);
		log.info("["+transactionId+"]parent:: "+parent);
		log.info("["+transactionId+"]userId:: "+userId);
		List<UserBean> responseBeans = new ArrayList<UserBean>();
		if(!StringUtils.isBlank(master)) {
			
		responseBeans= jdbcTemplate.query(
					QueryListConstant.GET_USER_DETAILS_BY_MASTER, new Object[]{master.toUpperCase(),master.toUpperCase()},
		            (rs, rowNum) ->
		                    new UserBean(
		                            rs.getLong("id"),
		                            rs.getString("user_id"),
		                            rs.getString("full_name"),
		                            rs.getString("user_Role"),
		                            rs.getString("parent"),
		                            rs.getDate("Reg_Date"),
		                            rs.getFloat("Odds_Commission"),
		                            rs.getFloat("Session_Commission"),
		                            rs.getInt("Bet_Delay"),
		                            rs.getInt("Session_Delay"),
		                            rs.getLong("User_Limit"),
		                            rs.getDouble("Max_Profit"),
		                            rs.getDouble("Max_Loss"),
		                            rs.getDouble("Odds_Max_Stake"),
		                            rs.getDouble("Going_In_Play_Stake"),
		                            rs.getDouble("Session_Max_Stake"),
		                            rs.getDouble("chips"),
		                            rs.getString("isactive"),
		                            rs.getString("isuserlock"),
		                            rs.getString("isbettinglock"),
		                            rs.getString("remarks"),
		                            rs.getDate("lastupdateddate"),
		                            rs.getString("lastupdateby"),
		                            rs.getDate("createddate"),
		                            rs.getString("createdby")
		                    )
		    );
		}else if(!StringUtils.isBlank(parent)) {
			responseBeans=userRepository.findByParentOrderByFullName(parent.toUpperCase());
		}else if(!StringUtils.isBlank(userId)) {
			responseBeans=userRepository.findByUserIdOrderByFullName(userId.toUpperCase());
		}else {
			responseBeans=userRepository.findAllByOrderByFullName();
		}
		log.info("responseBeans:  "+responseBeans);
		return responseBeans;
	}

	public PartnershipBean getPartnershipDetails(String userId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getPartnershipDetails CLASS UserDao*************************");
		log.info("["+transactionId+"]userId:: "+userId);
		return partnershipRepository.findByUserId(userId.toUpperCase());
	}

	public PartnershipBean updatePartnershipDetails(PartnershipBean psBean, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE updatePartnershipDetails CLASS UserDao*************************");
		String userId=psBean.getUserId().toUpperCase();
		String userRole=psBean.getUserRole();
		int admStake=psBean.getAdminStake();
		int smStake=psBean.getSupermasterStake();
		int masStake=psBean.getMastrerStake();
		PartnershipBean responseBean=null;
		log.info("["+transactionId+"] userId:: "+userId);
		log.info("["+transactionId+"] userRole:: "+userRole);
		log.info("["+transactionId+"] admStake:: "+admStake);
		log.info("["+transactionId+"] smStake:: "+smStake);
		log.info("["+transactionId+"] masStake:: "+masStake);
		
		if(StringUtils.isBlank(psBean.getRemarks())) {
			psBean.setRemarks("Partnership updated By "+psBean.getCreatedBy().toUpperCase());
		}
		
		PartnershipBean psUpdBean = partnershipRepository.findByUserId(userId);
		int totUpdStake= psUpdBean.getAdminStake()+smStake+masStake;
		log.info("["+transactionId+"]totUpdStake:: "+totUpdStake);
		if(totUpdStake==100) {
			psUpdBean.setMastrerStake(masStake);
			psUpdBean.setSupermasterStake(smStake);
			psUpdBean.setLastUpdateBy(psBean.getCreatedBy().toUpperCase());
			psUpdBean.setRemarks(psBean.getRemarks());
			responseBean=partnershipRepository.save(psUpdBean);
		}
		log.info("["+transactionId+"]responseBean:: "+responseBean);
		return responseBean;
	}

	public Map<Integer, Boolean> psPercentage(PartnershipBean psBean, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE psPercentage CLASS UserDao*************************");
		Map<Integer, Boolean> psPercRes= new HashMap<Integer, Boolean>();
		
		String userId=psBean.getUserId().toUpperCase();
		String userRole=psBean.getUserRole();
		int admStake=psBean.getAdminStake();
		int smStake=psBean.getSupermasterStake();
		int masStake=psBean.getMastrerStake();
		log.info("["+transactionId+"] userId:: "+userId);
		log.info("["+transactionId+"] userRole:: "+userRole);
		log.info("["+transactionId+"] admStake:: "+admStake);
		log.info("["+transactionId+"] smStake:: "+smStake);
		log.info("["+transactionId+"] masStake:: "+masStake);
		
		PartnershipBean psUpdBean = partnershipRepository.findByUserId(userId);
		int totUpdStake= psUpdBean.getAdminStake()+smStake+masStake;
		log.info("["+transactionId+"]totUpdStake:: "+totUpdStake);
		
		if(totUpdStake==100) {
			psPercRes.put(totUpdStake, true);
		}else {
			psPercRes.put(totUpdStake, false);
		}
		return psPercRes;
	}

	public UserBean updateUserDetails(@Valid UserDetailsRequestDto userDetailsRequestDto, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE psPercentage CLASS UserDao*************************");
		String userId=userDetailsRequestDto.getUserId().toUpperCase();
		String fullName=userDetailsRequestDto.getFullName();
		float oddsCommission=userDetailsRequestDto.getOddsCommission();
		float sessionCommission=userDetailsRequestDto.getSessionCommission();
		int betDelay=userDetailsRequestDto.getBetDelay();
		int sessionDelay=userDetailsRequestDto.getSessionDelay();
		long userLimit=userDetailsRequestDto.getUserLimit();
		double maxProfit=userDetailsRequestDto.getMaxProfit();
		double maxLoss=userDetailsRequestDto.getMaxLoss();
		double oddsMaxStake=userDetailsRequestDto.getOddsMaxStake();
		double goingInPlayStake=userDetailsRequestDto.getGoingInPlayStake();
		double sessionMaxStake=userDetailsRequestDto.getSessionMaxStake();
		String remarks= userDetailsRequestDto.getRemarks();
		
		log.info("["+transactionId+"] userId:: "+userId);
		log.info("["+transactionId+"] fullName:: "+fullName);
		log.info("["+transactionId+"] oddCommision:: "+oddsCommission);
		log.info("["+transactionId+"] sessionCommision:: "+sessionCommission);
		log.info("["+transactionId+"] betDelay:: "+betDelay);
		log.info("["+transactionId+"] sessionDelay:: "+sessionDelay);
		log.info("["+transactionId+"] userLimit:: "+userLimit);
		log.info("["+transactionId+"] maxProfit:: "+maxProfit);
		log.info("["+transactionId+"] maxLoss:: "+maxLoss);
		log.info("["+transactionId+"] oddsMaxStake:: "+oddsMaxStake);
		log.info("["+transactionId+"] goingInPlayStake:: "+goingInPlayStake);
		log.info("["+transactionId+"] sessionMaxStake:: "+sessionMaxStake);
		log.info("["+transactionId+"] remarks:: "+remarks);
		
		UserBean userUpdBean = userRepository.findFirst1ByUserIdOrderByFullName(userId);
		if(userUpdBean!=null) {
			log.info("["+transactionId+"] userUpdBean:: "+userUpdBean);
			userUpdBean.setFullName(fullName);
			userUpdBean.setOddsCommission(oddsCommission);
			userUpdBean.setSessionCommission(sessionCommission);
			userUpdBean.setBetDelay(betDelay);
			userUpdBean.setSessionDelay(sessionDelay);
			userUpdBean.setUserLimit(userLimit);
			userUpdBean.setMaxProfit(maxProfit);
			userUpdBean.setMaxLoss(maxLoss);
			userUpdBean.setOddsMaxStake(oddsMaxStake);
			userUpdBean.setGoingInPlayStake(goingInPlayStake);
			userUpdBean.setSessionMaxStake(sessionMaxStake);
			userUpdBean.setRemarks(remarks);
			userUpdBean=userRepository.save(userUpdBean);
		}
		log.info("["+transactionId+"] userUpdBean:: "+userUpdBean);
		
		return userUpdBean;
	}

	public List<ChipsBean> getChipsHistory(String userId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getChipsHistory CLASS UserDao*************************");
		List<ChipsBean> res=chipsRepository.findByUserIdOrderById(userId.toUpperCase());
		log.info("["+transactionId+"] Chips History : "+res);
		return res;
	}
	
}
