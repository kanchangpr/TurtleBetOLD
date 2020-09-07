package com.jetbet.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jetbet.bean.PlaceBetsBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.betfair.entities.MarketCatalogue;
import com.jetbet.dao.BetfairDao;
import com.jetbet.dto.DashboardMatchListDto;
import com.jetbet.dto.MatchAndFancyDetailDto;
import com.jetbet.dto.RunnerPriceAndSize;
import com.jetbet.dto.SeriesMatchFancyResponseDto;
import com.jetbet.dto.SessionDetails;
import com.jetbet.dto.UserRoleDto;
import com.jetbet.service.BetfairService;
import com.jetbet.util.QueryListConstant;
import com.jetbet.util.ResourceConstants;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping(value="/TurtleBets")
public class BetfairController {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public static String applicationKey;
	public static String sessionToken;
	public static String status;
	
	@Autowired
	BetfairService betfairService;
	
	private static final DateTimeFormatter datetimeformatter=DateTimeFormatter.ofPattern("HH:mm:ss");
	@RequestMapping(value=ResourceConstants.LIST_OF_SPORTS, method=RequestMethod.GET)
	public ResponseEntity<List<SportsBean>> getListOfSports() {
	//@Scheduled(fixedDelay = 40000)
	//public void getListOfSports() {
		
		log.info("Time of Execution: "+datetimeformatter.format(LocalDateTime.now()));
	//	ResponseEntity<SessionDetails> sessionDetails=getSessionToken();
//		String applicationKey="5tsF8QHfEw3n4Kp8";
//		String sessionToken="PsszL+gNaXw+s7+MiHF7vk8HfFrz+oNeZaxO8l+GZGU=";
//		String userName="KANCHAN";
		
//		String applicationKey=sessionDetails.getBody().getProduct();
//		String sessionToken=sessionDetails.getBody().getToken();
		String userName=ResourceConstants.USER_NAME;
		
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<SportsBean> response=betfairService.getListOfSports(applicationKey, sessionToken, userName, transactionId);
        return new ResponseEntity<List<SportsBean>> (response,HttpStatus.OK);
	}
	
	@RequestMapping(value=ResourceConstants.LIST_OF_SERIES, method=RequestMethod.GET)
	//public ResponseEntity<List<SeriesBean>> getListOfSeries() {
	//@Scheduled(fixedDelay = 360000, initialDelay = 15000)
	public void getListOfSeries() {
//		String applicationKey="5tsF8QHfEw3n4Kp8";
//		String sessionToken="PsszL+gNaXw+s7+MiHF7vk8HfFrz+oNeZaxO8l+GZGU=";
//		String userName="KANCHAN";
	//	ResponseEntity<SessionDetails> sessionDetails=getSessionToken();
//		String applicationKey=sessionDetails.getBody().getProduct();
//		String sessionToken=sessionDetails.getBody().getToken();
		String userName=ResourceConstants.USER_NAME;
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfSeries METHOD GET*************************");
		betfairService.getListOfSeries(applicationKey, sessionToken, userName, transactionId);
		getListOfMatches();
		//List<SeriesBean> response=betfairService.getListOfSeries(applicationKey, sessionToken, userName, transactionId);
        //return new ResponseEntity<List<SeriesBean>> (response,HttpStatus.OK);
	}
	
	//@RequestMapping(value=ResourceConstants.LIST_OF_MATHCES, method=RequestMethod.GET)
	//public ResponseEntity<List<MatchBean>> getListOfMatches() {
	//@Scheduled(fixedDelay = 40000)
	public void getListOfMatches() {
//		String applicationKey="5tsF8QHfEw3n4Kp8";
//		String sessionToken="PsszL+gNaXw+s7+MiHF7vk8HfFrz+oNeZaxO8l+GZGU=";
//		String userName="KANCHAN";
	//	ResponseEntity<SessionDetails> sessionDetails=getSessionToken();
//		String applicationKey=sessionDetails.getBody().getProduct();
//		String sessionToken=sessionDetails.getBody().getToken();
		String userName=ResourceConstants.USER_NAME;
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfMatches METHOD GET*************************");
		betfairService.getListOfMatches(applicationKey, sessionToken, userName, transactionId);
		getListOfOdds();
		//List<MatchBean> response=betfairService.getListOfMatches(applicationKey, sessionToken, userName, transactionId);
       // return new ResponseEntity<List<MatchBean>> (response,HttpStatus.OK);
	}
	
	//@RequestMapping(value=ResourceConstants.LIST_OF_FANCY, method=RequestMethod.GET)
	//public ResponseEntity<List<FancyBean>> getListOfOdds() {
	//@Scheduled(fixedDelay = 40000)
	public void getListOfOdds() {
//		String applicationKey="5tsF8QHfEw3n4Kp8";
//		String sessionToken="PsszL+gNaXw+s7+MiHF7vk8HfFrz+oNeZaxO8l+GZGU=";
//		String userName="KANCHAN";
		//ResponseEntity<SessionDetails> sessionDetails=getSessionToken();
//		String applicationKey=sessionDetails.getBody().getProduct();
//		String sessionToken=sessionDetails.getBody().getToken();
		String userName=ResourceConstants.USER_NAME;
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfOdds METHOD GET*************************");
		betfairService.getListOfOdds(applicationKey, sessionToken, userName, transactionId);
		//List<FancyBean> response=betfairService.getListOfOdds(applicationKey, sessionToken, userName, transactionId);
       // return new ResponseEntity<List<FancyBean>> (response,HttpStatus.OK);
	}
	@Scheduled(fixedDelay = 3600000)
	@RequestMapping(value=ResourceConstants.GET_SESSION_TOKEN, method=RequestMethod.POST)
	public ResponseEntity<SessionDetails> getSessionToken() {
		String userName="shiltonpereira@live.com";
		String password="Wsxedc@123";
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getSessionToken METHOD GET*************************");
		
		SessionDetails response=betfairService.getSessionToken(userName, password, transactionId);
		applicationKey=response.getProduct();
		sessionToken=response.getToken();
		log.info("applicationKey:: "+applicationKey);
		log.info("sessionToken:: "+sessionToken);
        return new ResponseEntity<SessionDetails> (response,HttpStatus.OK);
	}
	

	@RequestMapping(value=ResourceConstants.MARKET_CATALOGUE, method=RequestMethod.GET)
	public List<SeriesMatchFancyResponseDto> getMarketCatalogue(@RequestParam(value="sportsId" ,required=false) String sportsId) {
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE USER ROLE METHOD GET*************************");
		log.info("applicationKey:: "+applicationKey);
		log.info("sessionToken:: "+sessionToken);
		//ResponseEntity<SessionDetails> sessionDetails=getSessionToken();
//		String applicationKey=sessionDetails.getBody().getProduct();
//		String sessionToken=sessionDetails.getBody().getToken();
		String userName=ResourceConstants.USER_NAME;
		
		return betfairService.getMarketCatalogue(sportsId,applicationKey,sessionToken,userName,transactionId);
	}
	
	@RequestMapping(value=ResourceConstants.MATCH_ODDS, method=RequestMethod.GET)
	public List<SeriesMatchFancyResponseDto> getMatchOdds(@RequestParam(value="sportsId" ,required=false) String sportsId) {
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE USER ROLE METHOD GET*************************");
		log.info("applicationKey:: "+applicationKey);
		log.info("sessionToken:: "+sessionToken);
		//ResponseEntity<SessionDetails> sessionDetails=getSessionToken();
//		String applicationKey=sessionDetails.getBody().getProduct();
//		String sessionToken=sessionDetails.getBody().getToken();
		String userName=ResourceConstants.USER_NAME;
		
		return betfairService.getMatchOdds(sportsId,applicationKey,sessionToken,userName,transactionId);
	}
	
	@RequestMapping(value=ResourceConstants.MATCH_ODDS_AND_FANCY, method=RequestMethod.GET)
	public List<MatchAndFancyDetailDto> getMatchOddsAndFancy(@RequestParam(value="sportsId" ,required=false) String sportsId,
			@RequestParam(value="matchId" ,required=false) String matchId,
			@RequestParam(value="userName" ,required=false) String userName) {
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE USER ROLE METHOD GET*************************");
		log.info("applicationKey:: "+applicationKey);
		log.info("sessionToken:: "+sessionToken);
		//String userName=ResourceConstants.USER_NAME;
		
		return betfairService.getMatchOddsAndFancy(sportsId,matchId,applicationKey,sessionToken,userName,transactionId);
	}
	
	@RequestMapping(value=ResourceConstants.USER_DASHBOARD_MATCH_LIST, method=RequestMethod.GET)
	public List<DashboardMatchListDto> dashboardMatchList() {
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE dashboardMatchList METHOD GET*************************");
		log.info("applicationKey:: "+applicationKey);
		log.info("sessionToken:: "+sessionToken);
		//ResponseEntity<SessionDetails> sessionDetails=getSessionToken();
//		String applicationKey=sessionDetails.getBody().getProduct();
//		String sessionToken=sessionDetails.getBody().getToken();
		String userName=ResourceConstants.USER_NAME;
		
		return betfairService.dashboardMatchList(applicationKey,sessionToken,userName,transactionId);
	}
	
	@RequestMapping(value=ResourceConstants.RUNNERS_PRICE_SIZE, method=RequestMethod.GET)
	public RunnerPriceAndSize getRunnersPrizeAndSize(
			@RequestParam(value="marketId" ,required=true) String marketId,
			@RequestParam(value="selectionId" ,required=true) String selectionId) {
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE USER ROLE METHOD GET*************************");
		log.info("applicationKey:: "+applicationKey);
		log.info("sessionToken:: "+sessionToken);
		//ResponseEntity<SessionDetails> sessionDetails=getSessionToken();
//		String applicationKey=sessionDetails.getBody().getProduct();
//		String sessionToken=sessionDetails.getBody().getToken();
		String userName=ResourceConstants.USER_NAME;
		
		return betfairService.getRunnersPrizeAndSize(marketId,selectionId,applicationKey,sessionToken,userName,transactionId);
	}
	
	
	public void updateListOfSeries() {
		log.info("applicationKey:: "+applicationKey);
		log.info("sessionToken:: "+sessionToken);
	//	ResponseEntity<SessionDetails> sessionDetails=getSessionToken();
//		String applicationKey=sessionDetails.getBody().getProduct();
//		String sessionToken=sessionDetails.getBody().getToken();
		String userName=ResourceConstants.USER_NAME;
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfSeries METHOD GET*************************");
		betfairService.getListOfSeries(applicationKey, sessionToken, userName, transactionId);
	}
	
	

	@RequestMapping(value=ResourceConstants.USER_DASHBOARD, method=RequestMethod.GET)
	public List<MarketCatalogue> dashboardDetails(
			@RequestParam(value="matchId" ,required=true) String matchId,
			@RequestParam(value="marketType" ,required=true) String marketType) {
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE USER ROLE METHOD GET*************************");
		log.info("applicationKey:: "+applicationKey);
		log.info("sessionToken:: "+sessionToken);
		return betfairService.dashboardDetails(applicationKey,sessionToken,matchId,marketType,transactionId);
	}
	
	public void updateListOfMatches() {
		log.info("applicationKey:: "+applicationKey);
		log.info("sessionToken:: "+sessionToken);
	//	ResponseEntity<SessionDetails> sessionDetails=getSessionToken();
//		String applicationKey=sessionDetails.getBody().getProduct();
//		String sessionToken=sessionDetails.getBody().getToken();
		String userName=ResourceConstants.USER_NAME;
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfMatches METHOD GET*************************");
		betfairService.getListOfMatches(applicationKey, sessionToken, userName, transactionId);
	}
	
	
	public void updateListOfOdds() {
		log.info("applicationKey:: "+applicationKey);
		log.info("sessionToken:: "+sessionToken);
		//ResponseEntity<SessionDetails> sessionDetails=getSessionToken();
//		String applicationKey=sessionDetails.getBody().getProduct();
//		String sessionToken=sessionDetails.getBody().getToken();
		String userName=ResourceConstants.USER_NAME;
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfOdds METHOD GET*************************");
		betfairService.getListOfOdds(applicationKey, sessionToken, userName, transactionId);
	}
	
	
	//@RequestMapping(value=ResourceConstants.DECLARE_RESULT, method=RequestMethod.GET)
	//@Scheduled(fixedDelay = 300000, initialDelay = 10000)
	public void declareResult() {
		log.info("applicationKey:: "+applicationKey);
		log.info("sessionToken:: "+sessionToken);
	//	ResponseEntity<SessionDetails> sessionDetails=getSessionToken();
//		String applicationKey=sessionDetails.getBody().getProduct();
//		String sessionToken=sessionDetails.getBody().getToken();
		String userName=ResourceConstants.USER_NAME;
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfOdds METHOD GET*************************");
		betfairService.declareResult(applicationKey, sessionToken, userName, transactionId);
	}
	
	//@RequestMapping(value = ResourceConstants.CALCULATE_SETTLEMENT, method = RequestMethod.GET)
	//@Scheduled(fixedDelay = 300000, initialDelay = 180000)
	public void calculateProfitLoss() {
		log.info("applicationKey:: "+applicationKey);
		log.info("sessionToken:: "+sessionToken);
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId + "]*************INSIDE userHome METHOD POST**************");
		betfairService.calculateProfitLoss();
	}
	
}
