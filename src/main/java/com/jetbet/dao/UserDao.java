package com.jetbet.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.jetbet.bean.FancyBean;
import com.jetbet.bean.LookupTableBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.PartnershipBean;
import com.jetbet.bean.PlaceBetsBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.bean.StakesBean;
import com.jetbet.bean.UserBean;
import com.jetbet.bean.UserLoginBean;
import com.jetbet.dto.ChangePasswordDto;
import com.jetbet.dto.ChipsDto;
import com.jetbet.dto.MarketTypeDetailsDto;
import com.jetbet.dto.UserControlsDto;
import com.jetbet.dto.UserDetailsRequestDto;
import com.jetbet.dto.UserHomeDto;
import com.jetbet.dto.UserResponseDto;
import com.jetbet.dto.UserRolesResponseDto;
import com.jetbet.repository.ChipsRepository;
import com.jetbet.repository.FancyRepository;
import com.jetbet.repository.LookupTableRepository;
import com.jetbet.repository.MatchRepository;
import com.jetbet.repository.PartnershipRepository;
import com.jetbet.repository.PlaceBetsRepository;
import com.jetbet.repository.SeriesRepository;
import com.jetbet.repository.SportsRepository;
import com.jetbet.repository.StakesRepository;
import com.jetbet.repository.UserLoginRepository;
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
	private FancyRepository fancyRepository;

	@Autowired
	private PartnershipRepository partnershipRepository;

	@Autowired
	private StakesRepository stakesRepository;

	@Autowired
	private PlaceBetsRepository placeBetsRepository;

	@Autowired
	private UserLoginRepository userLoginRepository;

	@Autowired
	private LookupTableRepository lookupTableRepository;

	public static DecimalFormat df = new DecimalFormat("0.00");

	public List<UserBean> getResponse() {
		return userRepository.findAll();
	}

	@Transactional
	public List<UserRolesResponseDto> getUserRoles(String role, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE addUserRole CLASS UserDao*************************");
		log.info("[" + transactionId + "] Role: " + role);
		String getUserRolesSql = QueryListConstant.GET_USER_ROLES_SQL;
		return jdbcTemplate.query(getUserRolesSql, new Object[] { role.toUpperCase() },
				(rs, rowNum) -> new UserRolesResponseDto(rs.getString("user_Role")));
	}

	@Transactional
	public List<UserRolesResponseDto> getParentList(String userId, String role, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE addUserRole CLASS UserDao*************************");
		log.info("[" + transactionId + "] Role: " + role);
		String getParentListSql = QueryListConstant.GET_PARENT_LIST_SQL;
		return jdbcTemplate.query(getParentListSql, new Object[] { userId.toUpperCase(), role.toUpperCase() },
				(rs, rowNum) -> new UserRolesResponseDto(rs.getString("USER_ID")));
	}

	@Transactional
	public Boolean checkUserNameAvailability(String userName, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE addUserRole CLASS UserDao*************************");
		log.info("[" + transactionId + "] userName: " + userName.toUpperCase());
		Long count;
		count = userRepository.countByUserId(userName.toUpperCase());
		log.info("count of existing User: " + count);
		return (count == 0) ? true : false;
	}

	@Transactional
	public UserResponseDto addUserDetails(UserBean userBean, String transactionId) {
		UserResponseDto userResponseDto = new UserResponseDto();
		log.info("[" + transactionId
				+ "]*************************INSIDE addUserRole CLASS UserDao*************************");
		log.info("[" + transactionId + "] USER_ID: " + userBean.getUserId());
		log.info("[" + transactionId + "] FULL_NAME: " + userBean.getFullName());
		log.info("[" + transactionId + "] USER_ROLE: " + userBean.getUserRole());
		log.info("[" + transactionId + "] PARENT: " + userBean.getParent());
		log.info("[" + transactionId + "] REMARKS: " + userBean.getRemarks());
		log.info("[" + transactionId + "] CREATED_BY: " + userBean.getCreatedBy());
		userBean.setIsPwdUpdated("N");
		String userId = userBean.getUserId();
		String userParent = userBean.getParent();
		String userRole = userBean.getUserRole();
		int partnership = userBean.getPartnership();
		String createdBy = userBean.getCreatedBy();
		PartnershipBean psBean = new PartnershipBean();
		try {
			UserBean parentBean = userRepository.findFirst1ByUserId(userBean.getParent());
			Long userCountofParent = userRepository.countByParent(userBean.getParent());
			log.info("[" + transactionId + "] Parent's User Limit: " + parentBean.getUserLimit());
			log.info("[" + transactionId + "] Parent's User Count: " + userCountofParent);
			if (parentBean.getUserLimit() > userCountofParent) {
				Long userCount = userRepository.countByUserId(userBean.getUserId());
				final int totalStake = 100;
				int adminStake = 0;
				int supermasterStake = 0;
				int masterStake = 0;
				boolean isValid = true;
				if (userCount == 0) {

					psBean.setUserId(userId);
					psBean.setUserRole(userRole);
					psBean.setParent(userParent);
					psBean.setCreatedBy(createdBy);

					if (userRole.equalsIgnoreCase(ResourceConstants.SUPERMASTER)) {
						adminStake = totalStake - partnership;
						supermasterStake = partnership;
						psBean.setMastrerStake(masterStake);
						psBean.setSupermasterStake(supermasterStake);
						psBean.setAdminStake(adminStake);
						if (masterStake + supermasterStake + adminStake != 100) {
							isValid = false;
						}
					} else if (userRole.equalsIgnoreCase(ResourceConstants.MASTER)) {
						PartnershipBean psBeanObj = new PartnershipBean();
						psBeanObj = partnershipRepository.findByUserId(userParent);
						int admStake = psBeanObj.getAdminStake();
						int smStake = psBeanObj.getSupermasterStake();
						masterStake = partnership;
						adminStake = admStake;
						supermasterStake = smStake - masterStake;
						psBean.setMastrerStake(masterStake);
						psBean.setSupermasterStake(supermasterStake);
						psBean.setAdminStake(adminStake);
						if (masterStake + supermasterStake + adminStake != 100) {
							isValid = false;
						}
					} else if (userRole.equalsIgnoreCase(ResourceConstants.USER)) {
						PartnershipBean psBeanObj = new PartnershipBean();
						psBeanObj = partnershipRepository.findByUserId(userParent);

						masterStake = psBeanObj.getMastrerStake();
						adminStake = psBeanObj.getAdminStake();
						supermasterStake = psBeanObj.getSupermasterStake();

						psBean.setMastrerStake(masterStake);
						psBean.setSupermasterStake(supermasterStake);
						psBean.setAdminStake(adminStake);

						if (masterStake + supermasterStake + adminStake != 100) {
							isValid = false;
						}
					}

					log.info("[" + transactionId + "] adminStake: " + adminStake);
					log.info("[" + transactionId + "] supermasterStake: " + supermasterStake);
					log.info("[" + transactionId + "] masterStake: " + masterStake);

					if (isValid) {
						PartnershipBean psBeanRes = partnershipRepository.saveAndFlush(psBean);
						if (psBeanRes.getUserId() == userBean.getUserId()) {

							StakesBean stakesBean = new StakesBean();
							userBean.setPartnership(psBeanRes.getId());
							UserBean userRes = userRepository.save(userBean);

							stakesBean.setUserId(userBean.getUserId());
							stakesBean.setStake1(ResourceConstants.Stakes.STAKE1);
							stakesBean.setStake2(ResourceConstants.Stakes.STAKE2);
							stakesBean.setStake3(ResourceConstants.Stakes.STAKE3);
							stakesBean.setStake4(ResourceConstants.Stakes.STAKE4);
							stakesBean.setStake5(ResourceConstants.Stakes.STAKE5);
							stakesBean.setCreatedBy(createdBy);
							stakesRepository.save(stakesBean);

							if (userRes.getUserId() == userBean.getUserId()) {
								userResponseDto.setStatus(ResourceConstants.SUCCESS);
								userResponseDto.setErrorMsg(ResourceConstants.INSERTED);
							} else {
								userResponseDto.setStatus(ResourceConstants.FAILED);
								userResponseDto.setErrorCode(ResourceConstants.ERR_002);
								userResponseDto.setErrorMsg(ResourceConstants.INSERTION_FAILED);
							}
						} else {
							userResponseDto.setStatus(ResourceConstants.FAILED);
							userResponseDto.setErrorCode(ResourceConstants.ERR_002);
							userResponseDto.setErrorMsg(ResourceConstants.INSERTION_FAILED);
						}
					} else {
						userResponseDto.setStatus(ResourceConstants.FAILED);
						userResponseDto.setErrorCode(ResourceConstants.ERR_011);
						userResponseDto.setErrorMsg(ResourceConstants.PARTNERSHIP_INVALID);
					}

				} else {
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_001);
					userResponseDto.setErrorMsg(ResourceConstants.EXIST);
				}
			} else {
				userResponseDto.setStatus(ResourceConstants.FAILED);
				userResponseDto.setErrorCode(ResourceConstants.ERR_008);
				userResponseDto.setErrorMsg(ResourceConstants.USER_LIMIT_EXCEED);
			}

		} catch (Exception e) {
			userResponseDto.setStatus(ResourceConstants.EXCEPTION);
			userResponseDto.setErrorCode(ResourceConstants.ERR_EXCEPTION);
			userResponseDto.setErrorMsg(e.getMessage());
			e.printStackTrace();
		}
		return userResponseDto;
	}

	@Transactional
	public UserResponseDto userControls(UserControlsDto userControlsDto, String transactionId) {
		UserResponseDto userResponseDto = new UserResponseDto();
		log.info("[" + transactionId
				+ "]*************************INSIDE userControls CLASS UserDao*************************");
		log.info("[" + transactionId + "] userId: " + userControlsDto.getUserId());
		log.info("[" + transactionId + "] Action: " + userControlsDto.getAction());
		log.info("[" + transactionId + "] userName: " + userControlsDto.getUserName());
		String actString = userControlsDto.getAction().toUpperCase();
		String userString = userControlsDto.getUserId().toUpperCase();
		String updateByString = userControlsDto.getUserName().toUpperCase();
		try {
			Map<String, String> actionMap = new HashMap<String, String>();
			actionMap.put(ResourceConstants.LOCK_USER, "ISUSERLOCK='Y'");
			actionMap.put(ResourceConstants.UNLOCK_USER, "ISUSERLOCK='N'");
			actionMap.put(ResourceConstants.LOCK_BETTING, "ISBETTINGLOCK='Y'");
			actionMap.put(ResourceConstants.UNLOCK_BETTING, "ISBETTINGLOCK='N'");
			actionMap.put(ResourceConstants.OPEN_ACC, "ISACTIVE='Y'");
			actionMap.put(ResourceConstants.CLOSE_ACC, "ISACTIVE='N'");

			if (actionMap.containsKey(actString)) {
				long userCount = userRepository.countByUserId(userString);
				if (userCount > 0) {
					String sqlString = " WITH RECURSIVE TAB AS( SELECT USER_ID, PARENT FROM JETBET.JB_USER_DETAILS WHERE USER_ID =? "
							+ "UNION ALL SELECT J.USER_ID, J.PARENT FROM JETBET.JB_USER_DETAILS J, TAB WHERE J.PARENT = TAB.USER_ID ) "
							+ "UPDATE JETBET.JB_USER_DETAILS J1 SET " + actionMap.get(actString)
							+ " , LASTUPDATEDDATE=CURRENT_TIMESTAMP, LASTUPDATEBY=? "
							+ "WHERE J1.USER_ID IN ( SELECT USER_ID FROM TAB)";

					log.info("[" + transactionId + "] sqlString: " + sqlString);
					int count = jdbcTemplate.update(sqlString, new Object[] { userString, updateByString });
					if (count == 0) {
						userResponseDto.setStatus(ResourceConstants.FAILED);
						userResponseDto.setErrorCode(ResourceConstants.ERR_003);
						userResponseDto.setErrorMsg(ResourceConstants.UPDATION_FAILED);
					} else {
						userResponseDto.setStatus(ResourceConstants.SUCCESS);
						userResponseDto.setErrorMsg(ResourceConstants.UPDATED);
					}
				} else {
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_010);
					userResponseDto.setErrorMsg(ResourceConstants.USER_NOT_EXIST);
				}
			} else {
				userResponseDto.setStatus(ResourceConstants.FAILED);
				userResponseDto.setErrorCode(ResourceConstants.ERR_009);
				userResponseDto.setErrorMsg(ResourceConstants.ACTION_NOT_ALLOWED);
			}
		} catch (Exception e) {
			userResponseDto.setStatus(ResourceConstants.EXCEPTION);
			userResponseDto.setErrorCode(ResourceConstants.ERR_EXCEPTION);
			userResponseDto.setErrorMsg(e.getMessage());
			e.printStackTrace();
		}
		return userResponseDto;
	}

	/**
	 * This is method id used to maintain chips withdraw and deposit and all chips
	 * calculation fromUser -- LoggedInUser toUser -- AnotherUser chips -- chips to
	 * be deposited or withdrawn action -- withdraw/deposit
	 */
	@Transactional
	public UserResponseDto chipsAllocations(@Valid ChipsDto chipsDto, String transactionId) {
		UserResponseDto userResponseDto = new UserResponseDto();
		ChipsBean FromUserAccBean = new ChipsBean();
		ChipsBean toUserAccBean = new ChipsBean();
		ChipsBean FromUserAccBeanRes = new ChipsBean();
		ChipsBean toUserAccBeanRes = new ChipsBean();
		double currentChipsInFromUserAcc = 0;
		double currentChipsInToUserAcc = 0;
		double totalChipsInFromAcc = 0;
		double totalChipsInToAcc = 0;
		double depositWithdrawChips;
		String updateFromUserAccChipsSql = "";
		String updateToUserAccChipsSql = "";
		String returnMsgString = "";
		boolean errorRes = false;
		log.info("[" + transactionId
				+ "]*************************INSIDE chipsAllocations CLASS UserDao*************************");
		log.info("[" + transactionId + "] Chips: " + chipsDto.getChips());
		log.info("[" + transactionId + "] Action: " + chipsDto.getAction());
		log.info("[" + transactionId + "] fromUser: " + chipsDto.getFromUser());
		log.info("[" + transactionId + "] toUser: " + chipsDto.getToUser());
		log.info("[" + transactionId + "] userName: " + chipsDto.getUserName());
		String actString = chipsDto.getAction().toUpperCase();
		String createdByString = chipsDto.getUserName().toUpperCase();
		String fromUser = chipsDto.getFromUser().toUpperCase();
		String toUser = chipsDto.getToUser().toUpperCase();
		try {
			if (chipsDto.getChips() > 0) {
				depositWithdrawChips = chipsDto.getChips();
				log.info("[" + transactionId + "] depositWithdrawChipst: " + depositWithdrawChips);

				UserBean fromUserRes = userRepository.findFirst1ByUserId(fromUser);
				currentChipsInFromUserAcc = fromUserRes.getAvailLimit();
				log.info("[" + transactionId + "] Chips in From User Account: " + currentChipsInFromUserAcc);

				UserBean toUserRes = userRepository.findFirst1ByUserId(toUser);
				currentChipsInToUserAcc = toUserRes.getAvailLimit();
				log.info("[" + transactionId + "] Chips in To User Account: " + currentChipsInToUserAcc);

				if (actString.equalsIgnoreCase(ResourceConstants.DEPOSIT)) {
					log.info("[" + transactionId + "] INSIDE Deposit");
					returnMsgString = "Chips Deposited Successfully";
					if (depositWithdrawChips > currentChipsInFromUserAcc) {
						errorRes = true;
						userResponseDto.setStatus(ResourceConstants.FAILED);
						userResponseDto.setErrorCode(ResourceConstants.ERR_004);
						userResponseDto.setErrorMsg(ResourceConstants.INSUFFICIENT_AMOUNT);
					} else if (depositWithdrawChips <= currentChipsInFromUserAcc) {
						totalChipsInFromAcc = currentChipsInFromUserAcc - depositWithdrawChips;
						totalChipsInToAcc = currentChipsInToUserAcc + depositWithdrawChips;

						toUserAccBean.setCredit(depositWithdrawChips);
						toUserAccBean.setTotalChips(totalChipsInToAcc);

						FromUserAccBean.setDebit(depositWithdrawChips);
						FromUserAccBean.setTotalChips(totalChipsInFromAcc);
					}
				} else if (actString.equalsIgnoreCase(ResourceConstants.WITHDRAW)) {
					log.info("[" + transactionId + "] INSIDE Withdraw");
					returnMsgString = "Chips Withdrawn Successfully";
					if (depositWithdrawChips > currentChipsInToUserAcc) {
						errorRes = true;
						userResponseDto.setStatus(ResourceConstants.FAILED);
						userResponseDto.setErrorCode(ResourceConstants.ERR_004);
						userResponseDto.setErrorMsg(ResourceConstants.INSUFFICIENT_AMOUNT);
					} else if (depositWithdrawChips <= currentChipsInToUserAcc) {
						totalChipsInFromAcc = currentChipsInFromUserAcc + depositWithdrawChips;
						totalChipsInToAcc = currentChipsInToUserAcc - depositWithdrawChips;

						toUserAccBean.setDebit(depositWithdrawChips);
						toUserAccBean.setTotalChips(totalChipsInToAcc);

						FromUserAccBean.setCredit(depositWithdrawChips);
						FromUserAccBean.setTotalChips(totalChipsInFromAcc);
					}
				}

				log.info("[" + transactionId + "] errorRes:: " + errorRes);

				if (!errorRes) {
					log.info("[" + transactionId + "] totalChipsInFromAcc:: " + totalChipsInFromAcc);
					log.info("[" + transactionId + "] totalChipsInToAcc:: " + totalChipsInToAcc);

					updateFromUserAccChipsSql = QueryListConstant.UPDATE_FROM_USER_ACC_CHIPS_SQL;
					int updateFromUserAccChipsrowCount = jdbcTemplate.update(updateFromUserAccChipsSql,
							new Object[] { totalChipsInFromAcc,totalChipsInFromAcc, createdByString, fromUser });
					log.info("[" + transactionId + "] updateFromUserAccChipsrowCount:: "
							+ updateFromUserAccChipsrowCount);

					updateToUserAccChipsSql = QueryListConstant.UPDATE_TO_USER_ACC_CHIPS_SQL;
					int updateToUserAccChipsrowCount = jdbcTemplate.update(updateToUserAccChipsSql,
							new Object[] { totalChipsInToAcc,totalChipsInToAcc, createdByString, toUser });
					log.info("[" + transactionId + "] updateToUserAccChipsrowCount:: " + updateToUserAccChipsrowCount);

					toUserAccBean.setUserId(toUser);
					toUserAccBean.setFromUser(fromUser);
					toUserAccBean.setCreatedBy(createdByString);
					toUserAccBean.setRemarks(
							depositWithdrawChips + " chips " + actString + " from " + fromUser + " to " + toUser);
					toUserAccBeanRes = chipsRepository.saveAndFlush(toUserAccBean);

					FromUserAccBean.setUserId(fromUser);
					FromUserAccBean.setFromUser(toUser);
					FromUserAccBean.setCreatedBy(createdByString);
					FromUserAccBean.setRemarks(
							depositWithdrawChips + " chips " + actString + " from " + toUser + " to " + fromUser);
					FromUserAccBeanRes = chipsRepository.saveAndFlush(FromUserAccBean);

					if (updateFromUserAccChipsrowCount > 0 && updateToUserAccChipsrowCount > 0
							&& toUserAccBeanRes.getTotalChips() == toUserAccBean.getTotalChips()
							&& FromUserAccBeanRes.getTotalChips() == FromUserAccBean.getTotalChips()) {
						userResponseDto.setStatus(ResourceConstants.SUCCESS);
						userResponseDto.setErrorMsg(returnMsgString);
					} else {
						userResponseDto.setStatus(ResourceConstants.FAILED);
						userResponseDto.setErrorCode(ResourceConstants.ERR_005);
						userResponseDto.setErrorMsg(ResourceConstants.CHIPS_TRANSFER_FAILED);
					}
				}
			} else {
				userResponseDto.setStatus(ResourceConstants.FAILED);
				userResponseDto.setErrorCode(ResourceConstants.ERR_016);
				userResponseDto.setErrorMsg(ResourceConstants.CHIPS_GREATER_THAN_ZERO);
			}

		} catch (Exception e) {
			userResponseDto.setStatus(ResourceConstants.EXCEPTION);
			userResponseDto.setErrorCode(ResourceConstants.ERR_EXCEPTION);
			userResponseDto.setErrorMsg(e.getMessage());
			e.printStackTrace();
		}
		return userResponseDto;
	}

	@Transactional
	public List<ChipsDto> chipsBalance(String userId, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE chipsBalance CLASS UserDao*************************");
		log.info("[" + transactionId + "] Chips: " + userId);
		String getChipsBalance = QueryListConstant.GET_CHIPS_BALANCE_SQL;
		List<Map<String, Object>> chipsMap = jdbcTemplate.queryForList(getChipsBalance,
				new Object[] { userId, userId, userId });
		List<ChipsDto> chipsList = new ArrayList<ChipsDto>();
		for (Map<String, Object> row : chipsMap) {
			ChipsDto chipsDto = new ChipsDto();
			chipsDto.setUserRole(row.get("USER_ROLE").toString());
			chipsDto.setToUser(row.get("USER_ID").toString());
			chipsDto.setChips(Double.parseDouble(row.get("AVAIL_BALANCE").toString()));
			chipsList.add(chipsDto);
		}
		log.info("[" + transactionId + "] chipsMap:: " + chipsMap);
		return chipsList;
	}

	@Transactional
	public UserResponseDto changePassword(ChangePasswordDto changePasswordDto, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE changePassword CLASS UserDao*************************");
		UserResponseDto userResponseDto = new UserResponseDto();
		log.info("[" + transactionId + "] UserId: " + changePasswordDto.getUserId());
		log.info("[" + transactionId + "] Password: " + changePasswordDto.getPassword());
		log.info("[" + transactionId + "] ConfirmPassword: " + changePasswordDto.getConfirmPassword());
		log.info("[" + transactionId + "] LoggedInUser: " + changePasswordDto.getLoggedInUser());
		try {
			String loggedInUserString = changePasswordDto.getLoggedInUser().toUpperCase();
			String userId = changePasswordDto.getUserId().toUpperCase();
			String passwoord = changePasswordDto.getPassword();
			String confirmPass = changePasswordDto.getConfirmPassword();
			if (passwoord.equals(confirmPass)) {
				String updatePasswordSql = QueryListConstant.RESET_PASSWORD_SQL;
				int updatePasswordCount = jdbcTemplate.update(updatePasswordSql,
						new Object[] { confirmPass, loggedInUserString, userId });
				log.info("[" + transactionId + "] updatePasswordCount:: " + updatePasswordCount);
				if (updatePasswordCount > 0 && updatePasswordCount == 1) {
					userResponseDto.setStatus(ResourceConstants.SUCCESS);
					userResponseDto.setErrorMsg(ResourceConstants.PASSWORD_UPDATED);
				} else {
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_006);
					userResponseDto.setErrorMsg(ResourceConstants.PASSWORD_UPDATE_FAILED);
				}
			} else {
				userResponseDto.setStatus(ResourceConstants.FAILED);
				userResponseDto.setErrorCode(ResourceConstants.ERR_007);
				userResponseDto.setErrorMsg(ResourceConstants.PASSWORD_NOT_MATCH);
			}
		} catch (Exception e) {
			userResponseDto.setStatus(ResourceConstants.EXCEPTION);
			userResponseDto.setErrorCode(ResourceConstants.ERR_EXCEPTION);
			userResponseDto.setErrorMsg(e.getMessage());
			e.printStackTrace();
		}
		return userResponseDto;
	}

	@Transactional
	public List<SportsBean> activeSportsList(String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE sportsList CLASS UserDao*************************");
		List<SportsBean> responseBeanList = new ArrayList<SportsBean>();
		responseBeanList = sportsRepository.findByIsActiveOrderBySportsName("Y");
		log.info("[" + transactionId + "] responseBeanList:  " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	public List<SeriesBean> activeSeriesList(String sportsId, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE seriesList CLASS UserDao*************************");
		log.info("[" + transactionId + "]sportId:: " + sportsId);
		String isActive = "Y";
		List<SeriesBean> responseBeanList = new ArrayList<SeriesBean>();
		if (StringUtils.isBlank(sportsId)) {
			responseBeanList = seriesRepository.findByIsActiveOrderBySportId(isActive);
		} else {
			responseBeanList = seriesRepository.findBySportIdAndIsActiveOrderBySportId(sportsId, isActive);
		}
		log.info("[" + transactionId + "] responseBeanList:  " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	public List<MatchBean> activeMatchList(String sportId, String seriesId, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE matchList CLASS UserDao*************************");
		log.info("[" + transactionId + "]sportId:: " + sportId);
		log.info("[" + transactionId + "]seriesId:: " + seriesId);
		String isActive = "Y";
		List<MatchBean> responseBeanList = new ArrayList<MatchBean>();
		if (!StringUtils.isBlank(sportId) && !StringUtils.isBlank(seriesId)) {
			responseBeanList = matchRepository.findBySportIdAndSeriesIdAndIsActive(sportId, seriesId, isActive);
		} else if (!StringUtils.isBlank(sportId)) {
			responseBeanList = matchRepository.findBySportIdAndIsActive(sportId, isActive);
		} else if (!StringUtils.isBlank(seriesId)) {
			responseBeanList = matchRepository.findBySeriesIdAndIsActive(seriesId, isActive);
		} else if (StringUtils.isBlank(sportId) && StringUtils.isBlank(seriesId)) {
			responseBeanList = matchRepository.findByIsActive(isActive);
		}
		log.info("[" + transactionId + "] responseBeanList:  " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	public List<UserBean> getUserDetails(String master, String parent, String userId, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE getUserDetails CLASS UserDao*************************");
		log.info("[" + transactionId + "]parent:: " + master);
		log.info("[" + transactionId + "]parent:: " + parent);
		log.info("[" + transactionId + "]userId:: " + userId);
		List<UserBean> responseBeans = new ArrayList<UserBean>();
		if (!StringUtils.isBlank(master)) {

			responseBeans = jdbcTemplate.query(QueryListConstant.GET_USER_DETAILS_BY_MASTER,
					new Object[] { master.toUpperCase(), master.toUpperCase() },
					(rs, rowNum) -> new UserBean(rs.getLong("id"), rs.getString("user_id"), rs.getString("full_name"),
							rs.getString("user_Role"), rs.getString("parent"), rs.getDate("Reg_Date"),
							rs.getFloat("Odds_Commission"), rs.getFloat("Session_Commission"), rs.getInt("Bet_Delay"),
							rs.getInt("Session_Delay"), rs.getLong("User_Limit"), rs.getDouble("avail_limit"),
							rs.getDouble("avail_balance"), rs.getDouble("profit_loss"), rs.getString("isactive"),
							rs.getString("isuserlock"), rs.getString("isbettinglock"), rs.getString("remarks"),
							rs.getDate("lastupdateddate"), rs.getString("lastupdateby"), rs.getDate("createddate"),
							rs.getString("createdby")));
		} else if (!StringUtils.isBlank(parent)) {
			responseBeans = userRepository.findByParentOrderByUserId(parent.toUpperCase());
		} else if (!StringUtils.isBlank(userId)) {
			responseBeans = userRepository.findByUserIdOrderByUserId(userId.toUpperCase());
		} else {
			responseBeans = userRepository.findAllByOrderByUserId();
		}
		log.info("responseBeans:  " + responseBeans);
		return responseBeans;
	}

	@Transactional
	public PartnershipBean getPartnershipDetails(String userId, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE getPartnershipDetails CLASS UserDao*************************");
		log.info("[" + transactionId + "]userId:: " + userId);
		return partnershipRepository.findByUserId(userId.toUpperCase());
	}

	@Transactional
	public PartnershipBean updatePartnershipDetails(PartnershipBean psBean, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE updatePartnershipDetails CLASS UserDao*************************");
		String userId = psBean.getUserId().toUpperCase();
		String userRole = psBean.getUserRole();
		int admStake = psBean.getAdminStake();
		int smStake = psBean.getSupermasterStake();
		int masStake = psBean.getMastrerStake();
		PartnershipBean responseBean = null;
		log.info("[" + transactionId + "] userId:: " + userId);
		log.info("[" + transactionId + "] userRole:: " + userRole);
		log.info("[" + transactionId + "] admStake:: " + admStake);
		log.info("[" + transactionId + "] smStake:: " + smStake);
		log.info("[" + transactionId + "] masStake:: " + masStake);

		if (StringUtils.isBlank(psBean.getRemarks())) {
			psBean.setRemarks("Partnership updated By " + psBean.getCreatedBy().toUpperCase());
		}

		PartnershipBean psUpdBean = partnershipRepository.findByUserId(userId);
		int totUpdStake = psUpdBean.getAdminStake() + smStake + masStake;
		log.info("[" + transactionId + "]totUpdStake:: " + totUpdStake);
		if (totUpdStake == 100) {
			psUpdBean.setMastrerStake(masStake);
			psUpdBean.setSupermasterStake(smStake);
			psUpdBean.setLastUpdateBy(psBean.getCreatedBy().toUpperCase());
			psUpdBean.setRemarks(psBean.getRemarks());
			responseBean = partnershipRepository.save(psUpdBean);
		}
		log.info("[" + transactionId + "]responseBean:: " + responseBean);
		return responseBean;
	}

	@Transactional
	public Map<Integer, Boolean> psPercentage(PartnershipBean psBean, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE psPercentage CLASS UserDao*************************");
		Map<Integer, Boolean> psPercRes = new HashMap<Integer, Boolean>();

		String userId = psBean.getUserId().toUpperCase();
		String userRole = psBean.getUserRole();
		int admStake = psBean.getAdminStake();
		int smStake = psBean.getSupermasterStake();
		int masStake = psBean.getMastrerStake();
		log.info("[" + transactionId + "] userId:: " + userId);
		log.info("[" + transactionId + "] userRole:: " + userRole);
		log.info("[" + transactionId + "] admStake:: " + admStake);
		log.info("[" + transactionId + "] smStake:: " + smStake);
		log.info("[" + transactionId + "] masStake:: " + masStake);

		PartnershipBean psUpdBean = partnershipRepository.findByUserId(userId);
		int totUpdStake = psUpdBean.getAdminStake() + smStake + masStake;
		log.info("[" + transactionId + "]totUpdStake:: " + totUpdStake);

		if (totUpdStake == 100) {
			psPercRes.put(totUpdStake, true);
		} else {
			psPercRes.put(totUpdStake, false);
		}
		return psPercRes;
	}

	@Transactional
	public UserBean updateUserDetails(@Valid UserDetailsRequestDto userDetailsRequestDto, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE psPercentage CLASS UserDao*************************");
		String userId = userDetailsRequestDto.getUserId().toUpperCase();
		String fullName = userDetailsRequestDto.getFullName();
		double oddsCommission = userDetailsRequestDto.getOddsCommission();
		double sessionCommission = userDetailsRequestDto.getSessionCommission();
		int betDelay = userDetailsRequestDto.getBetDelay();
		int sessionDelay = userDetailsRequestDto.getSessionDelay();
		long userLimit = userDetailsRequestDto.getUserLimit();
//		double maxProfit = userDetailsRequestDto.getMaxProfit();
//		double maxLoss = userDetailsRequestDto.getMaxLoss();
//		double oddsMaxStake = userDetailsRequestDto.getOddsMaxStake();
//		double goingInPlayStake = userDetailsRequestDto.getGoingInPlayStake();
//		double sessionMaxStake = userDetailsRequestDto.getSessionMaxStake();
		String remarks = userDetailsRequestDto.getRemarks();

		log.info("[" + transactionId + "] userId:: " + userId);
		log.info("[" + transactionId + "] fullName:: " + fullName);
		log.info("[" + transactionId + "] oddCommision:: " + oddsCommission);
		log.info("[" + transactionId + "] sessionCommision:: " + sessionCommission);
		log.info("[" + transactionId + "] betDelay:: " + betDelay);
		log.info("[" + transactionId + "] sessionDelay:: " + sessionDelay);
		log.info("[" + transactionId + "] userLimit:: " + userLimit);
//		log.info("[" + transactionId + "] maxProfit:: " + maxProfit);
//		log.info("[" + transactionId + "] maxLoss:: " + maxLoss);
//		log.info("[" + transactionId + "] oddsMaxStake:: " + oddsMaxStake);
//		log.info("[" + transactionId + "] goingInPlayStake:: " + goingInPlayStake);
//		log.info("[" + transactionId + "] sessionMaxStake:: " + sessionMaxStake);
		log.info("[" + transactionId + "] remarks:: " + remarks);

		UserBean userUpdBean = userRepository.findFirst1ByUserId(userId);
		if (userUpdBean != null) {
			log.info("[" + transactionId + "] userUpdBean:: " + userUpdBean);
			userUpdBean.setFullName(fullName);
			userUpdBean.setOddsCommission(oddsCommission);
			userUpdBean.setSessionCommission(sessionCommission);
			userUpdBean.setBetDelay(betDelay);
			userUpdBean.setSessionDelay(sessionDelay);
			userUpdBean.setUserLimit(userLimit);
//			userUpdBean.setMaxProfit(maxProfit);
//			userUpdBean.setMaxLoss(maxLoss);
//			userUpdBean.setOddsMaxStake(oddsMaxStake);
//			userUpdBean.setGoingInPlayStake(goingInPlayStake);
//			userUpdBean.setSessionMaxStake(sessionMaxStake);
			userUpdBean.setRemarks(remarks);
			userUpdBean = userRepository.save(userUpdBean);
		}
		log.info("[" + transactionId + "] userUpdBean:: " + userUpdBean);

		return userUpdBean;
	}

	@Transactional
	public List<ChipsBean> getChipsHistory(String userId, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE getChipsHistory CLASS UserDao*************************");
		List<ChipsBean> res = chipsRepository.findByUserIdOrderById(userId.toUpperCase());
		log.info("[" + transactionId + "] Chips History : " + res);
		return res;
	}

	@Transactional
	public UserResponseDto changeUserPassword(@Valid ChangePasswordDto changePasswordDto, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE changeUserPassword CLASS UserDao*************************");
		UserResponseDto userResponseDto = new UserResponseDto();
		log.info("[" + transactionId + "] UserId: " + changePasswordDto.getUserId());
		log.info("[" + transactionId + "] OldPassword: " + changePasswordDto.getOldPassword());
		log.info("[" + transactionId + "] Password: " + changePasswordDto.getPassword());
		log.info("[" + transactionId + "] ConfirmPassword: " + changePasswordDto.getConfirmPassword());
		log.info("[" + transactionId + "] LoggedInUser: " + changePasswordDto.getLoggedInUser());
		try {
			String loggedInUserString = changePasswordDto.getLoggedInUser().toUpperCase();
			String userId = changePasswordDto.getUserId().toUpperCase();
			String oldPwd = changePasswordDto.getOldPassword();
			String passwoord = changePasswordDto.getPassword();
			String confirmPass = changePasswordDto.getConfirmPassword();

			UserBean userDet = userRepository.findFirst1ByUserId(userId);

			String oldPwdFromDB = userDet.getPassword();

			if (oldPwdFromDB.equals(oldPwd)) {
				if (passwoord.equals(confirmPass)) {
					String updatePasswordSql = QueryListConstant.UPDATE_PASSWORD_SQL;
					int updatePasswordCount = jdbcTemplate.update(updatePasswordSql,
							new Object[] { confirmPass, loggedInUserString, userId });
					log.info("[" + transactionId + "] updatePasswordCount:: " + updatePasswordCount);
					if (updatePasswordCount > 0 && updatePasswordCount == 1) {
						userResponseDto.setStatus(ResourceConstants.SUCCESS);
						userResponseDto.setErrorMsg(ResourceConstants.PASSWORD_UPDATED);
					} else {
						userResponseDto.setStatus(ResourceConstants.FAILED);
						userResponseDto.setErrorCode(ResourceConstants.ERR_006);
						userResponseDto.setErrorMsg(ResourceConstants.PASSWORD_UPDATE_FAILED);
					}
				} else {
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_007);
					userResponseDto.setErrorMsg(ResourceConstants.PASSWORD_NOT_MATCH);
				}
			} else {
				userResponseDto.setStatus(ResourceConstants.FAILED);
				userResponseDto.setErrorCode(ResourceConstants.ERR_012);
				userResponseDto.setErrorMsg(ResourceConstants.OLD_PASSWORD_WRONG);
			}

		} catch (Exception e) {
			userResponseDto.setStatus(ResourceConstants.EXCEPTION);
			userResponseDto.setErrorCode(ResourceConstants.ERR_EXCEPTION);
			userResponseDto.setErrorMsg(e.getMessage());
			e.printStackTrace();
		}
		return userResponseDto;

	}

	@Transactional
	public StakesBean geStakesDetails(String userId, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE geStakesDetails CLASS UserDao*************************");
		StakesBean res = stakesRepository.findByUserId(userId);
		log.info("[" + transactionId + "] Stakes Detail : " + res);
		return res;
	}

	@Transactional
	public StakesBean updateStakesDetails(@Valid StakesBean stakesBean, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE updateStakesDetails CLASS UserDao*************************");
		StakesBean resStakesBean = null;
		double stake1 = stakesBean.getStake1();
		double stake2 = stakesBean.getStake2();
		double stake3 = stakesBean.getStake3();
		double stake4 = stakesBean.getStake4();
		double stake5 = stakesBean.getStake5();
		String userId = stakesBean.getUserId().toUpperCase();

		log.info("[" + transactionId + "] stake1:  " + stake1);
		log.info("[" + transactionId + "] stake2:  " + stake2);
		log.info("[" + transactionId + "] stake3:  " + stake3);
		log.info("[" + transactionId + "] stake4:  " + stake4);
		log.info("[" + transactionId + "] stake5:  " + stake5);
		log.info("[" + transactionId + "] userId:  " + userId);

		StakesBean stakesdet = stakesRepository.findByUserId(userId);
		stakesdet.setStake1(stake1);
		stakesdet.setStake2(stake2);
		stakesdet.setStake3(stake3);
		stakesdet.setStake4(stake4);
		stakesdet.setStake5(stake5);
		stakesdet.setLastUpdateBy(userId);

		resStakesBean = stakesRepository.saveAndFlush(stakesdet);

		log.info("[" + transactionId + "] resStakesBean:  " + resStakesBean);

		return resStakesBean;
	}

	@Transactional
	public Double getLiability(String isBackLay, double odds, double stakes, String transactionId) {
		log.info("[" + transactionId + "]****************INSIDE getLiability CLASS UserDao****************");
		// DecimalFormat df = new DecimalFormat("0.00");
		log.info("[" + transactionId + "] odds:  " + odds);
		log.info("[" + transactionId + "] stakes:  " + stakes);
		double liability = 0.0;
		double total = 0.0;

		if (isBackLay.equalsIgnoreCase(ResourceConstants.LAY)) {
			total = odds * stakes;
			liability = Double.parseDouble(df.format(total - stakes));
		} else if (isBackLay.equalsIgnoreCase(ResourceConstants.BACK)) {
			liability = stakes;
		}

		log.info("[" + transactionId + "] total:  " + total);
		log.info("[" + transactionId + "] liability:  " + liability);

		return liability;
	}

	@Transactional
	public UserResponseDto placeBets(@Valid PlaceBetsBean placeBetsBean, String transactionId) {
		log.info("[" + transactionId + "]****************INSIDE placeBets CLASS UserDao****************");
		UserResponseDto userResponseDto = new UserResponseDto();
		BetfairDao bfDao = new BetfairDao();

		ChipsBean chipsBean = new ChipsBean();
		final String MINIMUM_STAKE = "MINIMUM_STAKE";
		final String IS_UNMATCHED_OPEN = "IS_UNMATCHED_OPEN";
		final String MATCH_ODDS = "Match Odds";
		final String BET_STATUS = "ACTIVE";
		final String BET_RESULT = "OPEN";
		final String BET_SETTLEMENT = "NOT_INITIATED";
		try {
			String updateChipsSql = null;
			String loginId = placeBetsBean.getLoginId();
			String userId = placeBetsBean.getUserId();
			String sportsId = placeBetsBean.getSportsId();
			String sportsName = placeBetsBean.getSportsName();
			String seriesId = placeBetsBean.getSeriesId();
			String seriesName = placeBetsBean.getSeriesName();
			String matchId = placeBetsBean.getMatchId();
			String matchName = placeBetsBean.getMatchName();
			String marketId = placeBetsBean.getMarketId();
			String marketName = placeBetsBean.getMarketName();
			Long selectionId = placeBetsBean.getSelectionId();
			String runnerName = placeBetsBean.getRunnerName();
			String marketType = placeBetsBean.getMarketType();
			double odds = placeBetsBean.getOdds();
			double stake = placeBetsBean.getStake();
			double liability = placeBetsBean.getLiability();
			String isback = placeBetsBean.getIsback();
			String isLay = placeBetsBean.getIsLay();
			String createdBy = placeBetsBean.getCreatedBy();
			String isBackLay = "";
			if (isback.equalsIgnoreCase("Y")) {
				isBackLay = "BACK";
			} else if (isLay.equalsIgnoreCase("Y")) {
				isBackLay = "LAY";
			}

			Double runnerPrize = bfDao.getRunnersPrizeAndSize(marketId, selectionId, isBackLay, transactionId);
			log.info("runnerPrize:: " + runnerPrize);
			UserBean userDetail = userRepository.findFirst1ByUserId(userId.toUpperCase());
			String parent = userDetail.getParent();
			int psId = userDetail.getPartnership();
			double chips = userDetail.getAvailBalance();
			double userLiab = userDetail.getLiability();
			String isbetLock = userDetail.getIsBettingLock();
			int sessionDelay = userDetail.getSessionDelay();
			int betDelay = userDetail.getBetDelay();

			log.info("[" + transactionId + "] parent:  " + parent);
			log.info("[" + transactionId + "] loginId:  " + loginId);
			log.info("[" + transactionId + "] userId:  " + userId);
			log.info("[" + transactionId + "] sportsId:  " + sportsId);
			log.info("[" + transactionId + "] sportsName:  " + sportsName);
			log.info("[" + transactionId + "] seriesId:  " + seriesId);
			log.info("[" + transactionId + "] seriesName:  " + seriesName);
			log.info("[" + transactionId + "] matchId:  " + matchId);
			log.info("[" + transactionId + "] matchName:  " + matchName);
			log.info("[" + transactionId + "] marketId:  " + marketId);
			log.info("[" + transactionId + "] marketName:  " + marketName);
			log.info("[" + transactionId + "] selectionId:  " + selectionId);
			log.info("[" + transactionId + "] runnerName:  " + runnerName);
			log.info("[" + transactionId + "] marketType:  " + marketType);
			log.info("[" + transactionId + "] odds:  " + odds);
			log.info("[" + transactionId + "] stake:  " + stake);
			log.info("[" + transactionId + "] porfitLoss:  " + liability);
			log.info("[" + transactionId + "] isback:  " + isback);
			log.info("[" + transactionId + "] isLay:  " + isLay);
			log.info("[" + transactionId + "] createdBy:  " + createdBy);

			placeBetsBean.setParent(parent);
			placeBetsBean.setPsId(psId);
			placeBetsBean.setRemarks("Bet Placed by User " + userId);
			placeBetsBean.setBetStatus(BET_STATUS);
			placeBetsBean.setBetResult(BET_RESULT);
			placeBetsBean.setBetSettlement(BET_SETTLEMENT);

			LookupTableBean lookupTableRes = lookupTableRepository.findByLookupType(MINIMUM_STAKE);
			LookupTableBean isUnMatched = lookupTableRepository.findByLookupType(IS_UNMATCHED_OPEN);
			Double minimumStake = Double.parseDouble(lookupTableRes.getLookupValue());
			String isUnMatchedOpen = isUnMatched.getLookupValue();
			if (isbetLock.equalsIgnoreCase("N")) {
				if (stake >= minimumStake) {
					if (odds == runnerPrize) {
						if (stake <= chips) {
							PlaceBetsBean placeBetsResBean = placeBetsRepository.saveAndFlush(placeBetsBean);
							if (placeBetsResBean.getUserId().equalsIgnoreCase(userId)) {

								log.info("[" + transactionId + "] placeBetsResBean:: " + placeBetsResBean);

								double balance = Double.parseDouble(df.format(chips - stake));
								double updUserLiab = Double.parseDouble(df.format(userLiab + liability));

								updateChipsSql = QueryListConstant.UPDATE_USER_CHIPS;
								int updateChipsRowCount = jdbcTemplate.update(updateChipsSql,
										new Object[] { balance, updUserLiab, userId, userId });
								log.info("[" + transactionId + "] updateChipsRowCount:: " + updateChipsRowCount);

								chipsBean.setUserId(userId);
								chipsBean.setFromUser("BET_PLACED");
								chipsBean.setBettingId(placeBetsResBean.getId());
								chipsBean.setDebit(stake);
								chipsBean.setTotalChips(balance);
								chipsBean.setCreatedBy(userId);
								chipsBean.setRemarks("Placed Bet on runner " + runnerName + " for " + stake);
								chipsBean = chipsRepository.saveAndFlush(chipsBean);

								log.info("[" + transactionId + "] chipsBean:: " + chipsBean);

								if (marketName.equalsIgnoreCase(MATCH_ODDS)) {
									Thread.sleep(betDelay * 1000);
								} else {
									Thread.sleep(sessionDelay * 1000);
								}
								userResponseDto.setStatus(ResourceConstants.SUCCESS);
								userResponseDto.setErrorMsg(ResourceConstants.BET_PLACED + placeBetsResBean.getId());
							} else {
								userResponseDto.setStatus(ResourceConstants.FAILED);
								userResponseDto.setErrorCode(ResourceConstants.ERR_002);
								userResponseDto.setErrorMsg(ResourceConstants.INSERTION_FAILED);
							}
						} else {
							userResponseDto.setStatus(ResourceConstants.FAILED);
							userResponseDto.setErrorCode(ResourceConstants.ERR_004);
							userResponseDto.setErrorMsg(ResourceConstants.INSUFFICIENT_AMOUNT);
						}
					} else {
						userResponseDto.setStatus(ResourceConstants.FAILED);
						userResponseDto.setErrorCode(ResourceConstants.ERR_015);
						userResponseDto.setErrorMsg(ResourceConstants.ODDS_MISMATCHED);
					}

				} else {
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_013);
					userResponseDto.setErrorMsg(ResourceConstants.MINIMUM_STAKE_ERROR + minimumStake);
				}
			} else {
				userResponseDto.setStatus(ResourceConstants.FAILED);
				userResponseDto.setErrorCode(ResourceConstants.ERR_013);
				userResponseDto.setErrorMsg(ResourceConstants.USER_BETTING_LOCK);
			}
		} catch (Exception e) {
			userResponseDto.setStatus(ResourceConstants.EXCEPTION);
			userResponseDto.setErrorCode(ResourceConstants.ERR_EXCEPTION);
			userResponseDto.setErrorMsg(e.getMessage());
			e.printStackTrace();
		}
		return userResponseDto;
	}

	public List<Object> userReport(String type, String userId, String fromDate, String toDate, String searchParam,
			String transactionId) {
		log.info("[" + transactionId + "]****************INSIDE userReport CLASS UserDao****************");
		List<PlaceBetsBean> betHistory = new ArrayList<PlaceBetsBean>();
		List<ChipsBean> accHistory = new ArrayList<ChipsBean>();
		List<UserLoginBean> loginHistory = new ArrayList<UserLoginBean>();
		List<Object> resObjects = new ArrayList<Object>();
		log.info("[" + transactionId + "] type:  " + type);
		log.info("[" + transactionId + "] userId:  " + userId);
		log.info("[" + transactionId + "] fromDate:  " + fromDate);
		log.info("[" + transactionId + "] toDate:  " + toDate);

		if (ResourceConstants.BetType.BET_HISTORY.equalsIgnoreCase(type)) {
			log.info("[" + transactionId + "] Inside :  " + type);
			if (!StringUtils.isEmpty(searchParam) && searchParam.equalsIgnoreCase("betSettlement")) {
				List<String> userList = jdbcTemplate.query(QueryListConstant.GET_USER_LIST,
						new Object[] { userId.toUpperCase() }, (rs, rowNum) -> new String(rs.getString("USER_ID")));
				log.info("userList:: " + userList);
				betHistory = placeBetsRepository.findByUserIdInAndBetSettlementOrderByUserIdDesc(userList, "PENDING");
			} else if (StringUtils.isEmpty(fromDate) && StringUtils.isEmpty(toDate)) {
				betHistory = placeBetsRepository.findByUserIdOrderByIdDesc(userId);
			} else {
				betHistory = jdbcTemplate.query(QueryListConstant.BET_HISTORY_BY_DATE_RANGE,
						new Object[] { fromDate, toDate, userId },
						(rs, rowNum) -> new PlaceBetsBean(rs.getLong("id"), rs.getString("login_id"),
								rs.getString("user_id"), rs.getString("parent"), rs.getString("sports_id"),
								rs.getString("sports_name"), rs.getString("series_id"), rs.getString("series_name"),
								rs.getString("match_id"), rs.getString("match_name"), rs.getString("market_id"),
								rs.getString("market_name"), rs.getLong("selection_id"), rs.getString("runner_name"),
								rs.getString("MARKET_TYPE"), rs.getDate("bet_place_date"), rs.getDouble("odds"),
								rs.getDouble("stake"), rs.getDouble("liability"), rs.getDouble("profit"),
								rs.getDouble("loss"), rs.getDouble("net_amount"), rs.getDouble("commision"),
								rs.getDouble("admin_stakes"), rs.getDouble("sm_stakes"), rs.getDouble("master_stakes"),
								rs.getString("admin_settle"), rs.getString("sm_settle"), rs.getString("master_settle"),
								rs.getString("isback"), rs.getString("islay"), rs.getInt("psid"),
								rs.getString("remarks"), rs.getString("bet_status"), rs.getString("bet_result"),
								rs.getString("bet_settlement"), rs.getString("created_by"), rs.getDate("created_date"),
								rs.getString("last_updated_by"), rs.getDate("last_updated_date")));
			}

			for (int i = 0; i < betHistory.size(); i++) {
				resObjects.add(betHistory.get(i));
			}

		} else if (ResourceConstants.BetType.ACCOUNT_HISTORY.equalsIgnoreCase(type)) {
			log.info("[" + transactionId + "] Inside :  " + type);
			if (StringUtils.isEmpty(fromDate) && StringUtils.isEmpty(toDate)) {
				accHistory = chipsRepository.findByUserIdOrderById(userId);
			} else {
				accHistory = jdbcTemplate.query(QueryListConstant.ACCOUNT_HISTORY_BY_DATE_RANGE,
						new Object[] { fromDate, toDate, userId },
						(rs, rowNum) -> new ChipsBean(rs.getLong("id"), rs.getString("user_id"),
								rs.getString("from_user"), rs.getDouble("credit"), rs.getDouble("debit"),
								rs.getDouble("total_chips"), rs.getString("remarks"), rs.getLong("betting_id"),
								rs.getString("created_by"), rs.getDate("created_on"), rs.getString("updated_by"),
								rs.getDate("updated_on")));
			}

			for (int i = 0; i < accHistory.size(); i++) {
				resObjects.add(accHistory.get(i));
			}
		} else if (ResourceConstants.BetType.LOGIN_HISTORY.equalsIgnoreCase(type)) {
			log.info("[" + transactionId + "] Inside :  " + type);
			if (StringUtils.isEmpty(fromDate) && StringUtils.isEmpty(toDate)) {
				loginHistory = userLoginRepository.findByUserIdOrderById(userId);
			} else {
				loginHistory = jdbcTemplate.query(QueryListConstant.LOGIN_HISTORY_BY_DATE_RANGE,
						new Object[] { fromDate, toDate, userId },
						(rs, rowNum) -> new UserLoginBean(rs.getLong("id"), rs.getString("user_id"),
								rs.getString("user_role"), rs.getString("user_parent"), rs.getDate("login_time"),
								rs.getString("ip_address"), rs.getString("browser_detail")));
			}

			for (int i = 0; i < loginHistory.size(); i++) {
				resObjects.add(loginHistory.get(i));
			}
		} else if (ResourceConstants.BetType.PROFIT_AND_LOSS_HISTORY.equalsIgnoreCase(type)) {
			log.info("[" + transactionId + "] Inside :  " + type);
			if (StringUtils.isEmpty(fromDate) && StringUtils.isEmpty(toDate)) {
				betHistory = placeBetsRepository.findByUserIdAndBetResultNotOrderByIdDesc(userId, "OPEN");
			} else {
				betHistory = jdbcTemplate.query(QueryListConstant.PROFIT_AND_LOSS_HISTORY_BY_DATE_RANGE,
						new Object[] { fromDate, toDate, userId },
						(rs, rowNum) -> new PlaceBetsBean(rs.getLong("id"), rs.getString("login_id"),
								rs.getString("user_id"), rs.getString("parent"), rs.getString("sports_id"),
								rs.getString("sports_name"), rs.getString("series_id"), rs.getString("series_name"),
								rs.getString("match_id"), rs.getString("match_name"), rs.getString("market_id"),
								rs.getString("market_name"), rs.getLong("selection_id"), rs.getString("runner_name"),
								rs.getString("MARKET_TYPE"), rs.getDate("bet_place_date"), rs.getDouble("odds"),
								rs.getDouble("stake"), rs.getDouble("liability"), rs.getDouble("profit"),
								rs.getDouble("loss"), rs.getDouble("net_amount"), rs.getDouble("commision"),
								rs.getDouble("admin_stakes"), rs.getDouble("sm_stakes"), rs.getDouble("master_stakes"),
								rs.getString("admin_settle"), rs.getString("sm_settle"), rs.getString("master_settle"),
								rs.getString("isback"), rs.getString("islay"), rs.getInt("psid"),
								rs.getString("remarks"), rs.getString("bet_status"), rs.getString("bet_result"),
								rs.getString("bet_settlement"), rs.getString("created_by"), rs.getDate("created_date"),
								rs.getString("last_updated_by"), rs.getDate("last_updated_date")));
			}

			for (int i = 0; i < betHistory.size(); i++) {
				resObjects.add(betHistory.get(i));
			}
		}

		log.info("betHistory:: " + betHistory);
		log.info("accHistory:: " + accHistory);
		log.info("resObjects:: " + resObjects);
		return resObjects;
	}

	public List<UserHomeDto> userHome(String sportsId, String transactionId) {
		log.info("[" + transactionId + "]****************INSIDE userHome CLASS UserDao****************");
		List<UserHomeDto> seriesMatchFancyResList = new ArrayList<UserHomeDto>();

		try {
			List<SeriesBean> seriesList = seriesRepository.findBySportIdAndIsActiveOrderBySportId(sportsId, "Y");
			for (int k = 0; k < seriesList.size(); k++) {
				UserHomeDto seriesMatchFancyRes = new UserHomeDto();
				String seriesId = seriesList.get(k).getSeriesId();
				String seriesName = seriesList.get(k).getSeriesName();

				seriesMatchFancyRes.setSeriesId(seriesId);
				seriesMatchFancyRes.setSeriesName(seriesName);
				List<MarketTypeDetailsDto> fancyDetailList = new ArrayList<MarketTypeDetailsDto>();
				List<MatchBean> matchList = matchRepository.findBySeriesIdAndIsActive(seriesId, "Y");
				for (int j = 0; j < matchList.size(); j++) {

					String matchId = matchList.get(j).getMatchId();
					String matchName = matchList.get(j).getMatchName();
					Date matchOpenDate = matchList.get(j).getMatchOpenDate();

					List<FancyBean> marketTypeList = fancyRepository.findByFancyIdMatchIdAndIsActive(matchId, "Y");
					for (int i = 0; i < marketTypeList.size(); i++) {

						MarketTypeDetailsDto marketTypeDetailsDto = new MarketTypeDetailsDto();
						marketTypeDetailsDto.setMatchId(matchId);
						marketTypeDetailsDto.setMatchName(matchName);
						marketTypeDetailsDto.setMatchOpenDate(matchOpenDate);
						marketTypeDetailsDto.setMarketType(marketTypeList.get(i).getFancyId().getMarketType());
						marketTypeDetailsDto.setMarketCount(marketTypeList.get(i).getMarketCount());
						fancyDetailList.add(marketTypeDetailsDto);

					}

				}
				seriesMatchFancyRes.setMatchAndFancyDetails(fancyDetailList);
				seriesMatchFancyResList.add(seriesMatchFancyRes);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return seriesMatchFancyResList;
	}

	public List<PlaceBetsBean> openPlacedBets(String userId, String transactionId) {
		log.info("[" + transactionId + "]****************INSIDE userHome CLASS UserDao****************");
		final String BET_RESULT = "OPEN";
		List<PlaceBetsBean> placeBetsList = new ArrayList<PlaceBetsBean>();

		placeBetsList = placeBetsRepository.findByUserIdAndBetResultOrderByIdDesc(userId.toUpperCase(), BET_RESULT);

		return placeBetsList;
	}

}
