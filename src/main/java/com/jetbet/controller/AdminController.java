package com.jetbet.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.jetbet.service.AdminService;
import com.jetbet.util.JetbetMapper;
import com.jetbet.util.ResourceConstants;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping(value = "/Admin")
public class AdminController {

	@Autowired
	AdminService adminService;

	@RequestMapping(value = ResourceConstants.SPORTS_CONTROL, method = RequestMethod.PUT)
	public ResponseEntity<UserResponseDto> sportsControl(@Valid @RequestBody SportsControl sportsControl) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE getListOfEventType METHOD GET*************************");
		SportsControl sportsControlReq = JetbetMapper.convertSportsControlToUpper(sportsControl);
		UserResponseDto response = adminService.sportsControl(sportsControlReq, transactionId);
		return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.LIST_OF_SPORTS, method = RequestMethod.GET)
	public ResponseEntity<List<SportsBean>> sportsList() {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<SportsBean> response = adminService.sportsList(transactionId);
		return new ResponseEntity<List<SportsBean>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.LIST_OF_SERIES, method = RequestMethod.GET)
	public ResponseEntity<List<SeriesBean>> seriesList(
			@RequestParam(value = "sportsId", required = false) String sportsId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<SeriesBean> response = adminService.seriesList(sportsId, transactionId);
		return new ResponseEntity<List<SeriesBean>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.LIST_OF_MATHCES, method = RequestMethod.GET)
	public ResponseEntity<List<MatchBean>> matchList(
			@RequestParam(value = "sportsId", required = false) String sportsId,
			@RequestParam(value = "seriesId", required = false) String seriesId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<MatchBean> response = adminService.matchList(sportsId, seriesId, transactionId);
		return new ResponseEntity<List<MatchBean>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.LIST_OF_FANCY, method = RequestMethod.GET)
	public ResponseEntity<List<FancyBean>> fancyList(
			@RequestParam(value = "sportsId", required = false) String sportsId,
			@RequestParam(value = "matchId", required = false) String matchId,
			@RequestParam(value = "fancyName", required = false) String fancyName) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<FancyBean> response = adminService.fancyList(sportsId,matchId, fancyName, transactionId);
		return new ResponseEntity<List<FancyBean>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.LIST_OF_FANCY, method = RequestMethod.PUT)
	public ResponseEntity<UserResponseDto> updateFancy(@Valid @RequestBody FancyControl fancyControl) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE getListOfEventType METHOD GET*************************");
		UserResponseDto response = adminService.updateFancy(fancyControl, transactionId);
		return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.OPEN_PLACE_BETS_BY_SPORTS, method = RequestMethod.GET)
	public ResponseEntity<List<PlaceBetsBean>> openPlacedBetsBySports(
			@RequestParam(value = "matchId", required = true) String matchId,
			@RequestParam(value = "sportsId", required = true) String sportsId,
			@RequestParam(value = "userId", required = true) String userId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId + "]*************INSIDE userHome METHOD POST**************");
		List<PlaceBetsBean> response = adminService.openPlacedBetsBySports(matchId, sportsId, userId, transactionId);
		return new ResponseEntity<List<PlaceBetsBean>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = ResourceConstants.OPEN_PLACE_BETS_COUNT, method = RequestMethod.GET)
	public ResponseEntity<Long> openPlacedBetsBySportsCount(
			@RequestParam(value = "matchId", required = true) String matchId,
			@RequestParam(value = "sportsId", required = true) String sportsId,
			@RequestParam(value = "userId", required = true) String userId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId + "]*************INSIDE userHome METHOD POST**************");
		Long response = adminService.openPlacedBetsBySportsCount(matchId, sportsId, userId, transactionId);
		return new ResponseEntity<Long>(response, HttpStatus.OK);
	}

	@RequestMapping(value = ResourceConstants.BET_SETTLEMENT, method = RequestMethod.GET)
	public ResponseEntity<List<BetSettlementDto>> betSettlement(
			@RequestParam(value = "accountType", required = false) String accountType,
			@RequestParam(value = "userId", required = false) String userId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<BetSettlementDto> response = adminService.betSettlement(accountType, userId, transactionId);
		return new ResponseEntity<List<BetSettlementDto>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = ResourceConstants.SETTLEMENT, method = RequestMethod.GET)
	public ResponseEntity<UserResponseDto> settlement(
			@RequestParam(value = "chips", required = true) double chips,
			@RequestParam(value = "remarks", required = true) String remarks,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "loggedInUser", required = true) String loggedInUser) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE settlement METHOD GET*************************");
		UserResponseDto response = adminService.settlement(chips,remarks, userId.toUpperCase(),loggedInUser, transactionId);
		return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = ResourceConstants.MATCH_DASHBOARD, method = RequestMethod.GET)
	public ResponseEntity<List<MatchDashboardDto>> matchDashboard(
			@RequestParam(value = "userId", required = true) String userId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE settlement METHOD GET*************************");
		List<MatchDashboardDto> response = adminService.matchDashboard(userId.toUpperCase(),transactionId);
		return new ResponseEntity<List<MatchDashboardDto>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = ResourceConstants.CURRENT_ODDS_POSITION, method = RequestMethod.GET)
	public ResponseEntity<List<MatchDashboardDto>> getCurrentOddsPosition(
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "matchId", required = true) String matchId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE settlement METHOD GET*************************");
		List<MatchDashboardDto> response = adminService.getCurrentOddsPosition(userId.toUpperCase(),matchId,transactionId);
		return new ResponseEntity<List<MatchDashboardDto>>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = ResourceConstants.FANCY_LIST, method = RequestMethod.GET)
	public ResponseEntity<List<String>> getFancyList(
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "matchId", required = true) String matchId) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE settlement METHOD GET*************************");
		List<String> response = adminService.getFancyList(userId.toUpperCase(),matchId,transactionId);
		return new ResponseEntity<List<String>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = ResourceConstants.FANCY_POTISION, method = RequestMethod.GET)
	public ResponseEntity<List<PlaceBetsBean>> getFancyPosition(
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "matchId", required = true) String matchId,
			@RequestParam(value = "marketType", required = true) String marketType) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE settlement METHOD GET*************************");
		List<PlaceBetsBean> response = adminService.getFancyPosition(userId.toUpperCase(),matchId,marketType,transactionId);
		return new ResponseEntity<List<PlaceBetsBean>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = ResourceConstants.USER_PROFIT_LOSS, method = RequestMethod.GET)
	public ResponseEntity<Double> getUserPl(
			@RequestParam(value = "marketId", required = true) String marketId,
			@RequestParam(value = "selectionId", required = true) String selectionId,
			@RequestParam(value = "userName", required = true) String userName) {
		String transactionId = "TB" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("[" + transactionId
				+ "]*************************INSIDE settlement METHOD GET*************************");
		String userId=userName.toUpperCase();
		Double response = adminService.getUserPl(marketId,selectionId,userId,transactionId);
		return new ResponseEntity<Double>(response, HttpStatus.OK);
	}

}
