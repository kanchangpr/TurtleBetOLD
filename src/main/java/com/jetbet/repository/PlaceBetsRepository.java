package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.PlaceBetsBean;
import com.jetbet.bean.UserBean;

public interface PlaceBetsRepository extends JpaRepository<PlaceBetsBean, Long>{

	List<PlaceBetsBean> findByUserIdOrderByIdDesc(String userId);
	
	List<PlaceBetsBean> findByUserIdAndBetResultNotOrderByIdDesc(String userId, String betResult);
	
	List<PlaceBetsBean> findByBetStatusOrderById(String betStatus);
	
	List<PlaceBetsBean> findByBetResultInAndBetSettlementOrderByUserId(List<String> betResult, String betSettlement);
	
	Long countByUserIdAndBetSettlementOrderById(String userId, String betSettlement);
	
	List<PlaceBetsBean> findByUserIdAndBetResultOrderByIdDesc(String userId,String betResult);
	
	List<PlaceBetsBean> findByUserIdInAndBetSettlementOrderByUserIdDesc(List<String> userId,String betSettlement);
}
