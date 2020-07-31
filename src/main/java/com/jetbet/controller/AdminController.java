package com.jetbet.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.jetbet.bean.FancyBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.dto.SportsControl;
import com.jetbet.dto.UserResponseDto;
import com.jetbet.service.AdminService;
import com.jetbet.util.JetbetMapper;
import com.jetbet.util.ResourceConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/Admin")
public class AdminController {

	@Autowired
	AdminService adminService;
	
	@RequestMapping(value=ResourceConstants.SPORTS_CONTROL, method=RequestMethod.PUT)
	public ResponseEntity<UserResponseDto> sportsControl(@Valid @RequestBody SportsControl sportsControl) {
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfEventType METHOD GET*************************");
		SportsControl sportsControlReq = JetbetMapper.convertSportsControlToUpper(sportsControl);
		UserResponseDto response=adminService.sportsControl(sportsControlReq, transactionId);
        return new ResponseEntity<UserResponseDto> (response,HttpStatus.OK);
	}
	
	
	@RequestMapping(value=ResourceConstants.LIST_OF_SPORTS, method=RequestMethod.GET)
	public ResponseEntity<List<SportsBean>> sportsList() {
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<SportsBean> response=adminService.sportsList(transactionId);
        return new ResponseEntity<List<SportsBean>> (response,HttpStatus.OK);
	}
	
	@RequestMapping(value=ResourceConstants.LIST_OF_SERIES, method=RequestMethod.GET)
	public ResponseEntity<List<SeriesBean>> seriesList(@RequestParam (value="sportsId",required=false) String sportsId ) {
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<SeriesBean> response=adminService.seriesList(sportsId,transactionId);
        return new ResponseEntity<List<SeriesBean>> (response,HttpStatus.OK);
	}
	
	@RequestMapping(value=ResourceConstants.LIST_OF_MATHCES, method=RequestMethod.GET)
	public ResponseEntity<List<MatchBean>> matchList(
			@RequestParam (value="sportsId",required=false) String sportsId,
			@RequestParam (value="seriesId",required=false) String seriesId) {
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<MatchBean> response=adminService.matchList(sportsId,seriesId,transactionId);
        return new ResponseEntity<List<MatchBean>> (response,HttpStatus.OK);
	}
	
	@RequestMapping(value=ResourceConstants.LIST_OF_ODDS, method=RequestMethod.GET)
	public ResponseEntity<List<FancyBean>> fancyList() {
		String transactionId = "TB"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		log.info("["+transactionId+"]*************************INSIDE getListOfEventType METHOD GET*************************");
		List<FancyBean> response=adminService.fancyList(transactionId);
        return new ResponseEntity<List<FancyBean>> (response,HttpStatus.OK);
	}
	
	
}
