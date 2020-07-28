package com.jetbet.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jetbet.bean.FancyBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.service.BetfairService;
import com.jetbet.util.ResourceConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/Betfair")
public class BetfairController {
	
	@Autowired
	BetfairService betfairService;
	
	private static final DateTimeFormatter datetimeformatter=DateTimeFormatter.ofPattern("HH:mm:ss");
	//@RequestMapping(value=ResourceConstants.LIST_OF_SPORTSS, method=RequestMethod.GET)
	//public ResponseEntity<List<SportsBean>> getListOfSports(@PathVariable String applicationKey, String sessionToken, String userName) {
	//@Scheduled(fixedDelay = 40000)
	public void getListOfSports() {
		
		log.info("Time of Execution: "+datetimeformatter.format(LocalDateTime.now()));
		
		String applicationKey="5tsF8QHfEw3n4Kp8";
		String sessionToken="XyS/BclYHo/fEccWpCL8Hd7Teciwn1jJ80ccdROY4nc=";
		String userName="KANCHAN";
		
		String transactionId = "JB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<SportsBean> response=betfairService.getListOfSports(applicationKey, sessionToken, userName, transactionId);
        //return new ResponseEntity<List<SportsBean>> (response,HttpStatus.OK);
	}
	
	//@RequestMapping(value=ResourceConstants.LIST_OF_SERIES, method=RequestMethod.GET)
	
	//public ResponseEntity<List<SeriesBean>> getListOfSeries(@PathVariable String applicationKey, String sessionToken, String userName) {
	
	//@Scheduled(fixedDelay = 40000)
	public void getListOfSeries() {
		String applicationKey="5tsF8QHfEw3n4Kp8";
		String sessionToken="XyS/BclYHo/fEccWpCL8Hd7Teciwn1jJ80ccdROY4nc=";
		String userName="KANCHAN";
		String transactionId = "JB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<SeriesBean> response=betfairService.getListOfSeries(applicationKey, sessionToken, userName, transactionId);
       // return new ResponseEntity<List<SeriesBean>> (response,HttpStatus.OK);
	}
	
	@RequestMapping(value=ResourceConstants.LIST_OF_MATHCES, method=RequestMethod.GET)
	public ResponseEntity<List<MatchBean>> getListOfMatches() {
		String applicationKey="5tsF8QHfEw3n4Kp8";
		String sessionToken="XyS/BclYHo/fEccWpCL8Hd7Teciwn1jJ80ccdROY4nc=";
		String userName="KANCHAN";
		String transactionId = "JB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<MatchBean> response=betfairService.getListOfMatches(applicationKey, sessionToken, userName, transactionId);
        return new ResponseEntity<List<MatchBean>> (response,HttpStatus.OK);
	}
	
	@RequestMapping(value=ResourceConstants.LIST_OF_ODDS, method=RequestMethod.GET)
	public ResponseEntity<List<FancyBean>> getListOfOdds() {
		String applicationKey="5tsF8QHfEw3n4Kp8";
		String sessionToken="XyS/BclYHo/fEccWpCL8Hd7Teciwn1jJ80ccdROY4nc=";
		String userName="KANCHAN";
		String transactionId = "JB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<FancyBean> response=betfairService.getListOfOdds(applicationKey, sessionToken, userName, transactionId);
        return new ResponseEntity<List<FancyBean>> (response,HttpStatus.OK);
	}

}
