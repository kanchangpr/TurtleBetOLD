package com.jetbet.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jetbet.bean.FancyBean;
import com.jetbet.bean.MarketCatalogueBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.betfair.entities.MarketCatalogue;
import com.jetbet.dao.BetfairDao;
import com.jetbet.dto.DashboardMatchListDto;
import com.jetbet.dto.RunnerPriceAndSize;
import com.jetbet.dto.SeriesMatchFancyResponseDto;
import com.jetbet.dto.SessionDetails;
import com.jetbet.repository.SportsRepository;
import com.jetbet.service.BetfairService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BetfairServiceImpl implements BetfairService{
	
	@Autowired
	BetfairDao betfairDao;
	
	@Autowired
	SportsRepository sportsRepository;
	
	@Override
	public List<SportsBean> getListOfSports(String applicationKey, String sessionToken, String userName, String transactionId) {
		return betfairDao.getlistOfEventType(applicationKey, sessionToken,userName,transactionId);
	}

	@Override
	public List<SeriesBean> getListOfSeries(String applicationKey, String sessionToken, String userName, String transactionId) {
		return betfairDao.getlistOfComp(applicationKey, sessionToken,userName,transactionId);
	}

	@Override
	public List<MatchBean> getListOfMatches(String applicationKey, String sessionToken, String userName, String transactionId) {
		return betfairDao.getlistOfMatches(applicationKey, sessionToken,userName,transactionId);
	}

	@Override
	public List<FancyBean> getListOfOdds(String applicationKey, String sessionToken, String userName,String transactionId) {
		return betfairDao.getListOfOdds(applicationKey, sessionToken,userName,transactionId);
	}

	@Override
	public SessionDetails getSessionToken(String userName, String password, String transactionId) {
		return betfairDao.getSessionToken(userName,password,transactionId);
	}
	
	@Override
	public List<SeriesMatchFancyResponseDto> getMarketCatalogue(String sportsId,String applicationKey,String sessionToken,String userName, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getMatchOdds CLASS AdminServiceImpl*************************");
		return betfairDao.getMarketCatalogue(sportsId,applicationKey, sessionToken, userName,transactionId);
	}

	@Override
	public List<DashboardMatchListDto> dashboardMatchList(String applicationKey, String sessionToken,
			String userName, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getMatchOdds CLASS AdminServiceImpl*************************");
		return betfairDao.dashboardMatchList(applicationKey, sessionToken, userName,transactionId);
	}

	@Override
	public RunnerPriceAndSize getRunnersPrizeAndSize(String marketId, String selectionId, String applicationKey,
			String sessionToken, String userName, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getRunnersPrizeAndSize CLASS AdminServiceImpl*************************");
		return null;
	}

	@Override
	public void declareResult(String applicationKey, String sessionToken, String userName, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE declareResult CLASS AdminServiceImpl*************************");
		betfairDao.declareResult(applicationKey, sessionToken, userName,transactionId);
	}

	@Override
	public void calculateProfitLoss() {
		betfairDao.calculateProfitLoss();
	}

	@Override
	public List<MarketCatalogue> dashboardDetails(String applicationKey, String sessionToken, String matchId,
			String marketType, String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE getMatchOdds CLASS AdminServiceImpl*************************");
		return betfairDao.dashboardDetails(applicationKey, sessionToken, matchId,marketType,transactionId);
	}
}
