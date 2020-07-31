package com.jetbet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jetbet.bean.MatchBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.dto.SportsControl;
import com.jetbet.dto.UserResponseDto;

@Service
public interface AdminService {

	UserResponseDto sportsControl(SportsControl sportsControlReq, String transactionId);

	List<SportsBean> sportsList(String transactionId);

	List<SeriesBean> seriesList(String transactionId);

	List<MatchBean> matchList(String transactionId);

}
