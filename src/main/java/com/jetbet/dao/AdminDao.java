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
import com.jetbet.dto.SportsControl;
import com.jetbet.dto.UserResponseDto;
import com.jetbet.repository.FancyRepository;
import com.jetbet.repository.MatchRepository;
import com.jetbet.repository.SeriesRepository;
import com.jetbet.repository.SportsRepository;
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
		log.info("["+transactionId+"]*************************INSIDE addUserRole CLASS UserDao*************************");
		UserResponseDto userResponseDto= new UserResponseDto();
		Boolean errorCode=false;
		String sqlString = null;
		String tableName = null;
		String columnName = null;
		String lastUpdatedByColumn = null;
		String lastUpdateDateColumn = null;
		try {
			if(sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.SPORTS_PAGE)) {
				tableName=ResourceConstants.SPORTS_TABLE;
				columnName=ResourceConstants.SPORTS_ID;
				lastUpdatedByColumn=ResourceConstants.SPORTS_UPDATED_BY;
				lastUpdateDateColumn=ResourceConstants.SPORTS_UPDATED_DATE;
			}else if(sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.SERIES_PAGE)) {
				tableName=ResourceConstants.SERIES_TABLE;
				columnName=ResourceConstants.SERIES_ID;
				lastUpdatedByColumn=ResourceConstants.SERIES_UPDATED_BY;
				lastUpdateDateColumn=ResourceConstants.SERIES_UPDATED_DATE ;
			}else if(sportsControlReq.getOperation().equalsIgnoreCase(ResourceConstants.MATCH_PAGE)) {
				tableName=ResourceConstants.MATCH_TABLE;
				columnName=ResourceConstants.MATCH_ID;
				lastUpdatedByColumn=ResourceConstants.MATCH_UPDATED_BY;
				lastUpdateDateColumn=ResourceConstants.MATCH_UPDATED_DATE;
			}else {
				errorCode=true;
				userResponseDto.setStatus(ResourceConstants.FAILED);
				userResponseDto.setErrorCode(ResourceConstants.ERR_009);
				userResponseDto.setErrorMsg(ResourceConstants.ACTION_NOT_ALLOWED);
			}
			
			log.info("["+transactionId+"] errorCode:: "+errorCode);
			
			if(!errorCode) {
				sqlString="UPDATE "+tableName
						+" SET IS_ACTIVE= ? , "
						+lastUpdatedByColumn +" = ? , "
						+lastUpdateDateColumn +" = CURRENT_TIMESTAMP "
						+"WHERE "+columnName +" = ?" ;
				
				log.info("["+transactionId+"] sqlString:: "+sqlString); 
				
				int count=jdbcTemplate.update(sqlString, new Object[]{
						sportsControlReq.getIsActive(),
						sportsControlReq.getUserName(),
						sportsControlReq.getOperationId()});
				if(count==0) {
					userResponseDto.setStatus(ResourceConstants.FAILED);
					userResponseDto.setErrorCode(ResourceConstants.ERR_003);
					userResponseDto.setErrorMsg(ResourceConstants.UPDATION_FAILED);
				}else {
					userResponseDto.setStatus(ResourceConstants.SUCCESS);
					userResponseDto.setErrorMsg(ResourceConstants.UPDATED);
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
	public List<SportsBean> sportsList(String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE sportsList CLASS UserDao*************************");
		List<SportsBean> responseBeanList = new ArrayList<SportsBean>();
		responseBeanList=sportsRepository.findAll();
		log.info("["+transactionId+"] responseBeanList:  "+responseBeanList);
		return responseBeanList;
	}
	
	public List<SeriesBean> seriesList(String sportsId,String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE seriesList CLASS UserDao*************************");
		log.info("["+transactionId+"]sportId:: "+sportsId);
		List<SeriesBean> responseBeanList = new ArrayList<SeriesBean>();
		if(StringUtils.isBlank(sportsId)) {
			responseBeanList=seriesRepository.findAll();
		}else {
			responseBeanList=seriesRepository.findBySportId(sportsId);
		}
		log.info("["+transactionId+"] responseBeanList:  "+responseBeanList);
		return responseBeanList;
	}
	
	@Transactional
	public List<MatchBean> matchList(String sportId,String seriesId,String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE matchList CLASS UserDao*************************");
		log.info("["+transactionId+"]sportId:: "+sportId);
		log.info("["+transactionId+"]seriesId:: "+seriesId);
		List<MatchBean> responseBeanList = new ArrayList<MatchBean>();
		if(!StringUtils.isBlank(sportId) && !StringUtils.isBlank(seriesId)) {
			responseBeanList=matchRepository.findBySportIdAndSeriesId(sportId, seriesId);
		}else if(!StringUtils.isBlank(sportId)) {
			responseBeanList=matchRepository.findBySportId(sportId);
		}else if(!StringUtils.isBlank(seriesId)) {
			responseBeanList=matchRepository.findBySeriesId(seriesId);
		} else if(StringUtils.isBlank(sportId) && StringUtils.isBlank(seriesId)) {
			responseBeanList=matchRepository.findAll();
		}
		log.info("["+transactionId+"] responseBeanList:  "+responseBeanList);
		return responseBeanList;
	}

	@Transactional
	public List<FancyBean> fancyList(String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE fancyList CLASS UserDao*************************");
		List<FancyBean> responseBeanList = new ArrayList<FancyBean>();
		responseBeanList=fancyRepository.findAll();
		log.info("["+transactionId+"] responseBeanList:  "+responseBeanList);
		return responseBeanList;
	}
	

}
