package com.jetbet.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jetbet.bean.FancyBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.dao.BetfairDao;
import com.jetbet.dto.SessionDetails;
import com.jetbet.repository.SportsRepository;
import com.jetbet.service.BetfairService;

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

}
