package com.jetbet.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.jetbet.bean.FancyBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.PlaceBetsBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.dto.BetSettlementDto;
import com.jetbet.dto.FancyControl;
import com.jetbet.dto.FancyReponseDto;
import com.jetbet.dto.MatchDashboardDto;
import com.jetbet.dto.SportsControl;
import com.jetbet.dto.UserResponseDto;

@Service
public interface AdminService {

	UserResponseDto sportsControl(SportsControl sportsControlReq, String transactionId);

	List<SportsBean> sportsList(String transactionId);

	List<SeriesBean> seriesList(String sportsId,String transactionId);

	List<MatchBean> matchList(String sportsId,String seriesId,String transactionId);

	List<FancyBean> fancyList(String sportsId,String matchId,String fancyName,String transactionId);

	UserResponseDto updateFancy(@Valid FancyControl fancyControl, String transactionId);

	List<PlaceBetsBean> openPlacedBetsBySports(String matchId,String sportsId,String userId, String transactionId);

	 List<BetSettlementDto> betSettlement(String accountType,String userId, String transactionId);

	 UserResponseDto settlement(double chips, String remarks, String userId, String loggedInUser, String transactionId);

	 List<MatchDashboardDto> matchDashboard(String userId, String transactionId);

	List<MatchDashboardDto> getCurrentOddsPosition(String userId, String matchId, String transactionId);

	List<String> getFancyList(String userId, String matchId, String transactionId);

	List<PlaceBetsBean> getFancyPosition(String userId, String matchId, String marketType, String transactionId);

}
