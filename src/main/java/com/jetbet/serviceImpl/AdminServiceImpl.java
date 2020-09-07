package com.jetbet.serviceImpl;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jetbet.bean.FancyBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.PlaceBetsBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.dao.AdminDao;
import com.jetbet.dto.BetSettlementDto;
import com.jetbet.dto.FancyControl;
import com.jetbet.dto.FancyReponseDto;
import com.jetbet.dto.MatchDashboardDto;
import com.jetbet.dto.SportsControl;
import com.jetbet.dto.UserResponseDto;
import com.jetbet.service.AdminService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
	private AdminDao adminDao;
	
	@Override
	public UserResponseDto sportsControl(SportsControl sportsControlReq, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE sportsControl CLASS AdminServiceImpl*************************");
		return adminDao.sportsControl(sportsControlReq,transactionId);
	}

	@Override
	public List<SportsBean> sportsList(String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE sportsList CLASS AdminServiceImpl*************************");
		return adminDao.sportsList(transactionId);
	}

	@Override
	public List<SeriesBean> seriesList(String sportsId,String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE seriesList CLASS AdminServiceImpl*************************");
		return adminDao.seriesList(sportsId,transactionId);
	}

	@Override
	public List<MatchBean> matchList(String sportsId,String seriesId,String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE matchList CLASS AdminServiceImpl*************************");
		return adminDao.matchList(sportsId,seriesId,transactionId);
	}

	@Override
	public List<FancyBean> fancyList(String sportsId,String matchId,String fancyName,String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE fancyList CLASS AdminServiceImpl*************************");
		return adminDao.fancyList(sportsId,matchId,fancyName,transactionId);
	}

	@Override
	public UserResponseDto updateFancy(@Valid FancyControl fancyControl, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE updateFancy CLASS AdminServiceImpl*************************");
		return adminDao.updateFancy(fancyControl,transactionId);
	}

	@Override
	public List<PlaceBetsBean> openPlacedBetsBySports(String matchId,String sportsId,String userId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE openPlacedBetsBySports CLASS AdminServiceImpl*************************");
		return adminDao.openPlacedBetsBySports(matchId,sportsId,userId,transactionId);
	}

	@Override
	public  List<BetSettlementDto> betSettlement(String accountType, String userId,String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE betSettlement CLASS AdminServiceImpl*************************");
		return adminDao.betSettlement(accountType,userId,transactionId);
	}

	@Override
	public UserResponseDto settlement(double chips, String remarks, String userId, String loggedInUser, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE betSettlement CLASS AdminServiceImpl*************************");
		return adminDao.settlement(chips,remarks,userId,loggedInUser,transactionId);
	}

	@Override
	public List<MatchDashboardDto> matchDashboard(String userId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE betSettlement CLASS AdminServiceImpl*************************");
		return adminDao.matchDashboard(userId,transactionId);
	}

	@Override
	public List<MatchDashboardDto> getCurrentOddsPosition(String userId, String matchId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getCurrentOddsPosition CLASS AdminServiceImpl*************************");
		return adminDao.getCurrentOddsPosition(userId,matchId,transactionId);
	}

	@Override
	public List<String> getFancyList(String userId, String matchId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getFancyList CLASS AdminServiceImpl*************************");
		return adminDao.getFancyList(userId,matchId,transactionId);
	}

	@Override
	public List<PlaceBetsBean> getFancyPosition(String userId, String matchId, String marketType,
			String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getFancyPosition CLASS AdminServiceImpl*************************");
		return adminDao.getFancyPosition(userId,matchId,marketType,transactionId);
	}

	@Override
	public List<PlaceBetsBean> getUserPl(String marketId, String selectionId, String userId, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getUserPl CLASS AdminServiceImpl*************************");
		return adminDao.getUserPl(marketId,selectionId,userId,transactionId);
	}

}
