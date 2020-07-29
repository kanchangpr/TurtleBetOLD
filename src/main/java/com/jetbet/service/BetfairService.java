package com.jetbet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jetbet.bean.FancyBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.dto.SessionDetails;

@Service
public interface BetfairService {

	List<SportsBean> getListOfSports(String applicationKey, String sessionToken, String userName, String transactionId);
	
	List<SeriesBean> getListOfSeries(String applicationKey, String sessionToken, String userName, String transactionId);

	List<MatchBean> getListOfMatches(String applicationKey, String sessionToken, String userName, String transactionId);

	List<FancyBean> getListOfOdds(String applicationKey, String sessionToken, String userName, String transactionId);

	SessionDetails getSessionToken(String userName, String password, String transactionId);

}
