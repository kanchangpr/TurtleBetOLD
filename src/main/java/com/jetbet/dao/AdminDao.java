package com.jetbet.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.jetbet.bean.FancyBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.controller.BetfairController;
import com.jetbet.dto.FancyIdDto;
import com.jetbet.dto.SportsControl;
import com.jetbet.dto.UserResponseDto;
import com.jetbet.dto.UserRolesResponseDto;
import com.jetbet.repository.FancyRepository;
import com.jetbet.repository.MatchRepository;
import com.jetbet.repository.SeriesRepository;
import com.jetbet.repository.SportsRepository;
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

	@Transactional
	public UserResponseDto sportsControl(SportsControl sportsControlReq, String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE addUserRole CLASS UserDao*************************");
		UserResponseDto userResponseDto = new UserResponseDto();
		BetfairController bfController=new BetfairController();
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
					if(sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.SPORTS_PAGE)) {
						new Thread(() -> {
							bfController.updateListOfSeries();
							return;
						}).start();
					}else if(sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.SERIES_PAGE)) {
						new Thread(() -> {
							bfController.updateListOfMatches();
							return;
						}).start();
					}else if(sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.MATCH_PAGE)) {
						new Thread(() -> {
							bfController.updateListOfOdds();
							return;
						}).start();
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
	public List<FancyBean> fancyList(String transactionId) {
		log.info("[" + transactionId
				+ "]*************************INSIDE fancyList CLASS UserDao*************************");
		List<FancyBean> responseBeanList = new ArrayList<FancyBean>();
		String getUserRolesSql = QueryListConstant.GET_FANCY_LIST;
		responseBeanList = jdbcTemplate.query(getUserRolesSql,
				(rs, rowNum) -> new FancyBean( new FancyIdDto(rs.getString("market_type"),rs.getString("MATCH_NAME")) , rs.getInt("market_count"),
						 rs.getString("series_id"),
						rs.getString("sports_id"), rs.getString("is_active"), rs.getString("fancy_created_by"),
						rs.getDate("fancy_created_date")));
		log.info("[" + transactionId + "] responseBeanList:  " + responseBeanList);
		return responseBeanList;
	}

}
