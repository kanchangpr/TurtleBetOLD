package com.jetbet.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jetbet.bean.MatchBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.dao.AdminDao;
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
	public List<SeriesBean> seriesList(String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE seriesList CLASS AdminServiceImpl*************************");
		return adminDao.seriesList(transactionId);
	}

	@Override
	public List<MatchBean> matchList(String transactionId) {
		log.info("["+transactionId+"]*************************INSIDE matchList CLASS AdminServiceImpl*************************");
		return adminDao.matchList(transactionId);
	}

}
