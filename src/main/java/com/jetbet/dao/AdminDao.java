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

import com.jetbet.bean.FancyBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.PlaceBetsBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.bean.UserBean;
import com.jetbet.controller.BetfairController;
import com.jetbet.dto.BetSettlementDto;
import com.jetbet.dto.FancyControl;
import com.jetbet.dto.FancyIdDto;
import com.jetbet.dto.SportsControl;
import com.jetbet.dto.UserResponseDto;
import com.jetbet.dto.UserRoleDto;
import com.jetbet.repository.FancyRepository;
import com.jetbet.repository.MatchRepository;
import com.jetbet.repository.SeriesRepository;
import com.jetbet.repository.SportsRepository;
import com.jetbet.repository.UserRepository;
import com.jetbet.util.QueryListConstant;
import com.jetbet.util.ResourceConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	SportsRepository sportsRepository;

	@Autowired
	SeriesRepository seriesRepository;

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	FancyRepository fancyRepository;
	
	@Autowired
	UserRepository userRepository;

	@Transactional
	public UserResponseDto sportsControl(SportsControl sportsControlReq, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE addUserRole CLASS UserDao*************************");
		UserResponseDto userResponseDto = new UserResponseDto();
		BetfairDao bfDao = new BetfairDao();
		Boolean errorCode = false;
		String sqlString = null;
		String tableName = null;
		String columnName = null;
		String lastUpdatedByColumn = null;
		String lastUpdateDateColumn = null;
		try {
			if (sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.SPORTS_PAGE)) {
				tableName = ResourceConstants.SPORTS_TABLE;
				columnName = ResourceConstants.SPORTS_ID;
				lastUpdatedByColumn = ResourceConstants.SPORTS_UPDATED_BY;
				lastUpdateDateColumn = ResourceConstants.SPORTS_UPDATED_DATE;
			} else if (sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.SERIES_PAGE)) {
				tableName = ResourceConstants.SERIES_TABLE;
				columnName = ResourceConstants.SERIES_ID;
				lastUpdatedByColumn = ResourceConstants.SERIES_UPDATED_BY;
				lastUpdateDateColumn = ResourceConstants.SERIES_UPDATED_DATE;
			} else if (sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.MATCH_PAGE)) {
				tableName = ResourceConstants.MATCH_TABLE;
				columnName = ResourceConstants.MATCH_ID;
				lastUpdatedByColumn = ResourceConstants.MATCH_UPDATED_BY;
				lastUpdateDateColumn = ResourceConstants.MATCH_UPDATED_DATE;
			} else if (sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.FANCY_PAGE)) {
				tableName = ResourceConstants.MATCH_TABLE;
				columnName = ResourceConstants.MATCH_ID;
				lastUpdatedByColumn = ResourceConstants.MATCH_UPDATED_BY;
				lastUpdateDateColumn = ResourceConstants.MATCH_UPDATED_DATE;
			} else {
				errorCode = true;
				userResponseDto.setStatus(ResourceConstants.FAILED);
				userResponseDto.setErrorCode(ResourceConstants.ERR_009);
				userResponseDto.setErrorMsg(ResourceConstants.ACTION_NOT_ALLOWED);
			}

			log.info("[" + transactionId + "] errorCode:: " + errorCode + "sportsControlReq.getIsActive(): "
					+ sportsControlReq.getIsActive());

			if (!errorCode && sportsControlReq.getIsActive().equalsIgnoreCase("Y")) {

				sqlString = "UPDATE " + tableName + " SET IS_ACTIVE= ? , " + lastUpdatedByColumn + " = ? , "
						+ lastUpdateDateColumn + " = CURRENT_TIMESTAMP " + "WHERE " + columnName + " = ?";

				log.info("[" + transactionId + "] sqlString:: " + sqlString);

				int count = jdbcTemplate.update(sqlString, new Object[] { sportsControlReq.getIsActive(),
						sportsControlReq.getUserName(), sportsControlReq.getOperationId() });
				if (count == 0) {
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_003);
					userResponseDto.setErrorMsg(ResourceConstants.UPDATION_FAILED);
				} else {
					userResponseDto.setStatus(ResourceConstants.SUCCESS);
					userResponseDto.setErrorMsg(ResourceConstants.UPDATED);
					if (sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.SPORTS_PAGE)) {
						bfDao.updateListOfSeries(ResourceConstants.USER_NAME,transactionId);
					} else if (sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.SERIES_PAGE)) {
						bfDao.updateListOfMatches(ResourceConstants.USER_NAME,transactionId);
					} else if (sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.MATCH_PAGE)) {
						bfDao.updateListOfOdds(ResourceConstants.USER_NAME,transactionId);
					}
				}
			} else if (!errorCode && sportsControlReq.getIsActive().equalsIgnoreCase("N")) {
				if (sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.SPORTS_PAGE)) {
					log.info("[" + transactionId + "] sportsControlReq.getOperation(): "
							+ sportsControlReq.getOperation());
					jdbcTemplate.update(QueryListConstant.SPORTS_CONTROL_FOR_SPORTS_PAGE,
							new Object[] { sportsControlReq.getIsActive(), sportsControlReq.getUserName(),
									sportsControlReq.getOperationId() });
					jdbcTemplate.update(QueryListConstant.SPORTS_CONTROL_FOR_SERIES_PAGE,
							new Object[] { sportsControlReq.getIsActive(), sportsControlReq.getUserName(),
									sportsControlReq.getOperationId() });
					jdbcTemplate.update(QueryListConstant.SPORTS_CONTROL_FOR_MATCH_PAGE,
							new Object[] { sportsControlReq.getIsActive(), sportsControlReq.getUserName(),
									sportsControlReq.getOperationId() });

					userResponseDto.setStatus(ResourceConstants.SUCCESS);
					userResponseDto.setErrorMsg(ResourceConstants.UPDATED);

				} else if (sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.SERIES_PAGE)) {
					log.info("[" + transactionId + "] sportsControlReq.getOperation(): "
							+ sportsControlReq.getOperation());
					jdbcTemplate.update(QueryListConstant.SERIES_CONTROL_FOR_SERIES_PAGE,
							new Object[] { sportsControlReq.getIsActive(), sportsControlReq.getUserName(),
									sportsControlReq.getOperationId() });
					jdbcTemplate.update(QueryListConstant.SERIES_CONTROL_FOR_MATCH_PAGE,
							new Object[] { sportsControlReq.getIsActive(), sportsControlReq.getUserName(),
									sportsControlReq.getOperationId() });

					userResponseDto.setStatus(ResourceConstants.SUCCESS);
					userResponseDto.setErrorMsg(ResourceConstants.UPDATED);

				} else if (sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.MATCH_PAGE)) {
					log.info("[" + transactionId + "] sportsControlReq.getOperation(): "
							+ sportsControlReq.getOperation());
					jdbcTemplate.update(QueryListConstant.MATCH_CONTROL_FOR_MATCH_PAGE,
							new Object[] { sportsControlReq.getIsActive(), sportsControlReq.getUserName(),
									sportsControlReq.getOperationId() });

					jdbcTemplate.update(QueryListConstant.MATCH_CONTROL_FOR_FANCY_PAGE,
							new Object[] { sportsControlReq.getIsActive(), sportsControlReq.getUserName(),
									sportsControlReq.getOperationId() });

					userResponseDto.setStatus(ResourceConstants.SUCCESS);
					userResponseDto.setErrorMsg(ResourceConstants.UPDATED);

				} else {
					errorCode = true;
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_009);
					userResponseDto.setErrorMsg(ResourceConstants.ACTION_NOT_ALLOWED);
				}
			}

		} catch (Exception e) {
			userResponseDto.setStatus(ResourceConstants.EXCEPTION);
			userResponseDto.setErrorCode(ResourceConstants.ERR_EXCEPTION);
			userResponseDto.setErrorMsg(e.getMessage());
			e.printStackTrace();
		}
		log.info("[" + transactionId + "] userResponseDto: " + userResponseDto);
		return userResponseDto;
	}

	@Transactional
	public List<SportsBean> sportsList(String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE sportsList CLASS UserDao*************************");
		List<SportsBean> responseBeanList = new ArrayList<SportsBean>();
		responseBeanList = sportsRepository.findAll();
		log.info("[" + transactionId + "] responseBeanList:  " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	public List<SeriesBean> seriesList(String sportsId, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE seriesList CLASS UserDao*************************");
		log.info("[" + transactionId + "]sportId:: " + sportsId);
		List<SeriesBean> responseBeanList = new ArrayList<SeriesBean>();
		if (StringUtils.isBlank(sportsId)) {
			String getUserRolesSql = QueryListConstant.GET_SERIES_LIST;
			responseBeanList = jdbcTemplate.query(getUserRolesSql,
					(rs, rowNum) -> new SeriesBean(rs.getString("series_id"), rs.getString("series_name"),
							rs.getInt("series_market_count"), rs.getString("series_competition_region"),
							rs.getString("SPORTS_NAME"), rs.getString("is_active"), rs.getString("series_created_by"),
							rs.getDate("series_created_date"), rs.getString("series_updated_by"),
							rs.getDate("series_updated_date")));
		} else {
			String getUserRolesSql = QueryListConstant.GET_SERIES_LIST_BY_SPORTS_ID;
			responseBeanList = jdbcTemplate.query(getUserRolesSql, new Object[] { sportsId },
					(rs, rowNum) -> new SeriesBean(rs.getString("series_id"), rs.getString("series_name"),
							rs.getInt("series_market_count"), rs.getString("series_competition_region"),
							rs.getString("SPORTS_NAME"), rs.getString("is_active"), rs.getString("series_created_by"),
							rs.getDate("series_created_date"), rs.getString("series_updated_by"),
							rs.getDate("series_updated_date")));
		}
		log.info("[" + transactionId + "] responseBeanList:  " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	public List<MatchBean> matchList(String sportId, String seriesId, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE matchList CLASS UserDao*************************");
		log.info("[" + transactionId + "]sportId:: " + sportId);
		log.info("[" + transactionId + "]seriesId:: " + seriesId);
		List<MatchBean> responseBeanList = new ArrayList<MatchBean>();
		if (!StringUtils.isBlank(sportId) && !StringUtils.isBlank(seriesId)) {
			String getUserRolesSql = QueryListConstant.GET_MATCHES_LIST_BY_SPORTS_AND_SERIES_ID;
			responseBeanList = jdbcTemplate.query(getUserRolesSql, new Object[] { sportId, seriesId },
					(rs, rowNum) -> new MatchBean(rs.getString("match_id"), rs.getString("match_name"),
							rs.getString("match_country_code"), rs.getString("match_time_zone"),
							rs.getString("match_venue"), rs.getDate("match_open_date"), rs.getInt("match_market_count"),
							rs.getString("series_name"), rs.getString("sports_name"), rs.getString("is_active"),
							rs.getString("match_created_by"), rs.getDate("match_created_date"),
							rs.getString("match_updated_by"), rs.getDate("match_updated_date")));
		} else if (!StringUtils.isBlank(sportId)) {
			String getUserRolesSql = QueryListConstant.GET_MATCHES_LIST_BY_SPORTS_ID;
			responseBeanList = jdbcTemplate.query(getUserRolesSql, new Object[] { sportId },
					(rs, rowNum) -> new MatchBean(rs.getString("match_id"), rs.getString("match_name"),
							rs.getString("match_country_code"), rs.getString("match_time_zone"),
							rs.getString("match_venue"), rs.getDate("match_open_date"), rs.getInt("match_market_count"),
							rs.getString("series_name"), rs.getString("sports_name"), rs.getString("is_active"),
							rs.getString("match_created_by"), rs.getDate("match_created_date"),
							rs.getString("match_updated_by"), rs.getDate("match_updated_date")));
		} else if (!StringUtils.isBlank(seriesId)) {
			String getUserRolesSql = QueryListConstant.GET_MATCHES_LIST_BY_SERIES_ID;
			responseBeanList = jdbcTemplate.query(getUserRolesSql, new Object[] { seriesId },
					(rs, rowNum) -> new MatchBean(rs.getString("match_id"), rs.getString("match_name"),
							rs.getString("match_country_code"), rs.getString("match_time_zone"),
							rs.getString("match_venue"), rs.getDate("match_open_date"), rs.getInt("match_market_count"),
							rs.getString("series_name"), rs.getString("sports_name"), rs.getString("is_active"),
							rs.getString("match_created_by"), rs.getDate("match_created_date"),
							rs.getString("match_updated_by"), rs.getDate("match_updated_date")));
		} else if (StringUtils.isBlank(sportId) && StringUtils.isBlank(seriesId)) {
			String getUserRolesSql = QueryListConstant.GET_MATCHES_LIST;
			responseBeanList = jdbcTemplate.query(getUserRolesSql,
					(rs, rowNum) -> new MatchBean(rs.getString("match_id"), rs.getString("match_name"),
							rs.getString("match_country_code"), rs.getString("match_time_zone"),
							rs.getString("match_venue"), rs.getDate("match_open_date"), rs.getInt("match_market_count"),
							rs.getString("series_name"), rs.getString("sports_name"), rs.getString("is_active"),
							rs.getString("match_created_by"), rs.getDate("match_created_date"),
							rs.getString("match_updated_by"), rs.getDate("match_updated_date")));
		}
		log.info("[" + transactionId + "] responseBeanList:  " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	public List<FancyBean> fancyList(String matchId, String fancyName, String transactionId) {
		log.info("[" + transactionId + "]**************INSIDE fancyList CLASS UserDao******************");
		List<FancyBean> responseBeanList = new ArrayList<FancyBean>();
		String getUserRolesSql = null;

		if (StringUtils.isBlank(matchId) && StringUtils.isBlank(fancyName)) {
			getUserRolesSql = QueryListConstant.GET_FANCY_LIST + " ORDER BY MATCH_NAME";
		} else if (!StringUtils.isBlank(matchId) && StringUtils.isBlank(fancyName)) {
			getUserRolesSql = QueryListConstant.GET_FANCY_LIST + " AND MATCH.MATCH_ID='" + matchId
					+ "' ORDER BY MATCH_NAME";
		} else if (!StringUtils.isBlank(matchId) && !StringUtils.isBlank(fancyName)) {
			getUserRolesSql = QueryListConstant.GET_FANCY_LIST + " AND MATCH.MATCH_ID='" + matchId
					+ "' AND FANCY.MARKET_TYPE='" + fancyName + "' ORDER BY MATCH_NAME";
		}

		responseBeanList = jdbcTemplate.query(getUserRolesSql,
				(rs, rowNum) -> new FancyBean(new FancyIdDto(rs.getString("market_type"), rs.getString("MATCH_NAME")),
						rs.getInt("market_count"), rs.getString("series_id"), rs.getString("sports_id"),
						rs.getString("is_active"), rs.getString("fancy_created_by"), rs.getDate("fancy_created_date")));
		log.info("[" + transactionId + "] responseBeanList:  " + responseBeanList);
		return responseBeanList;
	}

	public UserResponseDto updateFancy(@Valid FancyControl fancyControl, String transactionId) {
		log.info("[" + transactionId + "]**************INSIDE fancyList CLASS UserDao******************");
		UserResponseDto userResponseDto = new UserResponseDto();

		String matchId = fancyControl.getMatchId();
		String marketName = fancyControl.getMarketName();
		String isActive = fancyControl.getIsActive();
		String updatedBy = fancyControl.getUserName();
		log.info("[" + transactionId + "] ");

		int count = jdbcTemplate.update(QueryListConstant.UPDATE_FANCY_DETAIL,
				new Object[] { isActive, updatedBy, marketName, matchId });
		if (count == 0) {
			userResponseDto.setStatus(ResourceConstants.FAILED);
			userResponseDto.setErrorCode(ResourceConstants.ERR_003);
			userResponseDto.setErrorMsg(ResourceConstants.UPDATION_FAILED);
		} else {
			userResponseDto.setStatus(ResourceConstants.SUCCESS);
			userResponseDto.setErrorMsg(ResourceConstants.UPDATED);
		}

		return userResponseDto;
	}

	public List<PlaceBetsBean> openPlacedBetsBySports(String matchId, String marketId, String userId,
			String transactionId) {
		List<PlaceBetsBean> placeBetsList = new ArrayList<PlaceBetsBean>();
		placeBetsList = jdbcTemplate.query(QueryListConstant.OPEN_BET_FOR_MASTERS,
				new Object[] { userId, matchId, marketId },
				(rs, rowNum) -> new PlaceBetsBean(rs.getString("user_id"), rs.getString("match_id"),
						rs.getString("match_name"), rs.getString("market_id"), rs.getString("market_name"),
						rs.getLong("selection_id"), rs.getString("runner_name"), rs.getDate("bet_place_date"),
						rs.getDouble("odds"), rs.getDouble("stake"), rs.getDouble("liability"), rs.getString("isback"),
						rs.getString("islay")));
		log.info("[" + transactionId + "] responseBeanList:  " + placeBetsList);
		return placeBetsList;
	}

	public List<BetSettlementDto> betSettlement(String accountType, String userId, String transactionId) {
		String sqlString = null;
		UserBean userDetail = userRepository.findByUserId(userId.toUpperCase());
		String role= userDetail.getUserRole();
		List<BetSettlementDto> betSettlementList= new ArrayList<BetSettlementDto>();
		
		if(role.equalsIgnoreCase(ResourceConstants.MASTER)) {
			if(accountType.equalsIgnoreCase("MINUS")) {
				sqlString=QueryListConstant.GET_SETTLEMENT_FOR_MASTER_MINUS;
			}else if(accountType.equalsIgnoreCase("PLUS")) {
				sqlString=QueryListConstant.GET_SETTLEMENT_FOR_MASTER_PLUS;
			} 
			
			betSettlementList = jdbcTemplate.query(sqlString,
					new Object[] { userId.toUpperCase() },
					(rs, rowNum) -> new BetSettlementDto(
							rs.getString("USER_ID"), 
							rs.getDouble("AMOUNT"),
							rs.getDouble("ADMIN_STAKES"),
							rs.getDouble("SM_STAKES"),
							rs.getDouble("MASTER_STAKES")
							));
			
		}else if(role.equalsIgnoreCase(ResourceConstants.SUPERMASTER)) {
			if(accountType.equalsIgnoreCase("MINUS")) {
				sqlString=QueryListConstant.GET_SETTLEMENT_FOR_SM_MINUS;
			}else if(accountType.equalsIgnoreCase("PLUS")) {
				sqlString=QueryListConstant.GET_SETTLEMENT_FOR_SM_PLUS;
			}
			
			betSettlementList = jdbcTemplate.query(sqlString,
					new Object[] { userId.toUpperCase() },
					(rs, rowNum) -> new BetSettlementDto(
							rs.getString("USER_ID"), 
							rs.getDouble("MASTER_STAKES"),
							rs.getDouble("ADMIN_STAKES"),
							rs.getDouble("SM_STAKES")
							));
			
		}else if(role.equalsIgnoreCase(ResourceConstants.ADMIN)) {
			if(accountType.equalsIgnoreCase("MINUS")) {
				sqlString=QueryListConstant.GET_SETTLEMENT_FOR_ADMIN_MINUS;
			}else if(accountType.equalsIgnoreCase("PLUS")) {
				sqlString=QueryListConstant.GET_SETTLEMENT_FOR_ADMIN_PLUS;
			}
			
			betSettlementList = jdbcTemplate.query(sqlString,
					new Object[] { userId.toUpperCase() },
					(rs, rowNum) -> new BetSettlementDto(
							rs.getString("USER_ID"), 
							rs.getDouble("SM_STAKES"),
							rs.getDouble("ADMIN_STAKES")
							));
		}
		
		return betSettlementList;
		
		/* String admin;
		 String sm;
		 String master;
		 String sqlString = null;
		List<UserRoleDto> userDetails= new ArrayList<UserRoleDto>();
		java.util.Map<String,String> userParentMap=new HashMap<String,String>();
		
		List<BetSettlementDto> betSettlementResList= new ArrayList<BetSettlementDto>();
		List<BetSettlementDto> betSettlementList= new ArrayList<BetSettlementDto>();
		if(accountType.equalsIgnoreCase("MINUS")) {
			sqlString=QueryListConstant.GET_BET_SETTLEMENT_DATA_MINUS;
		}else if(accountType.equalsIgnoreCase("PLUS")) {
			sqlString=QueryListConstant.GET_BET_SETTLEMENT_DATA_PLUS;
		}
		
		betSettlementList = jdbcTemplate.query(sqlString,
				new Object[] { userId },
				(rs, rowNum) -> new BetSettlementDto(
						rs.getString("USER_ID"), rs.getDouble("STAKES"), 
						rs.getDouble("LIABILITY"),	rs.getDouble("PROFIT"),
						rs.getDouble("LOSS"),
						rs.getDouble("AMOUNT"),
						rs.getDouble("COMMISION"),
						rs.getDouble("ADMIN_STAKES"),
						rs.getDouble("SM_STAKES"),
						rs.getDouble("MASTER_STAKES")
						));
		for (int i = 0; i < betSettlementList.size(); i++) {
			BetSettlementDto betSettlementRes= new BetSettlementDto();
			userDetails = jdbcTemplate.query(QueryListConstant.GET_PARENT_LIST,
					new Object[] { betSettlementList.get(i).getUserId() },
					(rs, rowNum) -> new UserRoleDto(
							rs.getString("USER_ID"), rs.getString("USER_ROLE")
							));
			for (int j = 0; j < userDetails.size(); j++) {
				userParentMap.put(userDetails.get(j).getUserRole(),userDetails.get(j).getUserId());
			}
			admin=userParentMap.get("ADMIN");
			sm=userParentMap.get("SUPERMASTER");
			master=userParentMap.get("MASTER");
			betSettlementRes.setAdmin(admin);
			betSettlementRes.setSm(sm);
			betSettlementRes.setMaster(master);
			betSettlementRes.setUserId(betSettlementList.get(i).getUserId());
			betSettlementRes.setStake(betSettlementList.get(i).getStake());
			betSettlementRes.setLiability(betSettlementList.get(i).getLiability());
			betSettlementRes.setProfit(betSettlementList.get(i).getProfit());
			betSettlementRes.setLoss(betSettlementList.get(i).getLoss());
			betSettlementRes.setAmount(betSettlementList.get(i).getAmount());
			betSettlementRes.setCommision(betSettlementList.get(i).getCommision());
			betSettlementRes.setAdminStakes(betSettlementList.get(i).getAdminStakes());
			betSettlementRes.setSmStakes(betSettlementList.get(i).getSmStakes());
			betSettlementRes.setMasterStakes(betSettlementList.get(i).getMasterStakes());
			betSettlementResList.add(betSettlementRes);
		}
		
		return betSettlementResList;*/
	}

	@Transactional
	public UserResponseDto settlement(double chips,String remarks, String userId, String loggedInUser, String transactionId) {
		log.info("[" + transactionId + "]**************INSIDE settlement CLASS UserDao******************");

		log.info("[" + transactionId + "] chips:: "+chips);
		log.info("[" + transactionId + "] remarks:: "+remarks);
		log.info("[" + transactionId + "] userId:: "+userId);
		log.info("[" + transactionId + "] loggedInUser:: "+loggedInUser);
		UserResponseDto userResponseDto= new UserResponseDto();
		try {
			
//			List<UserRoleDto> userDetails= new ArrayList<UserRoleDto>();
//			Map<String,String> userParentMap=new HashMap<String,String>();
//			userDetails = jdbcTemplate.query(QueryListConstant.GET_PARENT_LIST,
//					new Object[] { userId },
//					(rs, rowNum) -> new UserRoleDto(
//							rs.getString("USER_ID"), rs.getString("USER_ROLE")
//							));
//			for (int j = 0; j < userDetails.size(); j++) {
//				userParentMap.put(userDetails.get(j).getUserRole().toUpperCase(),userDetails.get(j).getUserId().toUpperCase());
//			}
//			
//			UserBean userDet= userRepository.findByUserId(userId);
//			UserBean masterDet= userRepository.findByUserId(userParentMap.get("MASTER"));
//			UserBean smDet= userRepository.findByUserId(userParentMap.get("SUPERMASTER"));
//			
//			double userPL=userDet.getPrifitLoss();
//			double masterPL=masterDet.getPrifitLoss();
//			double smPL=smDet.getPrifitLoss();
//			
//			if((userPL==0 && masterPL==0)||(masterPL==0 && smPL))
			
			int userUpdateCount=jdbcTemplate.update(QueryListConstant.RESET_USER_TABLE_ON_SETTLEMENT,
					new Object[] { loggedInUser, userId});
			
			int betUpdateCount=jdbcTemplate.update(QueryListConstant.RESET_BET_TABLE_ON_SETTLEMENT,
					new Object[] { loggedInUser, remarks, userId});
			
			if(userUpdateCount>0 ) {
				userResponseDto.setStatus(ResourceConstants.SUCCESS);
				userResponseDto.setErrorMsg(ResourceConstants.SETTLEMENT_SUCCESS);
			}else {
				userResponseDto.setStatus(ResourceConstants.FAILED);
				userResponseDto.setErrorMsg(ResourceConstants.SETTLEMENT_FAILED);
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
