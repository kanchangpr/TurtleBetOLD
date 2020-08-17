package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.PlaceBetsBean;

public interface PlaceBetsRepository extends JpaRepository<PlaceBetsBean, Long>{

	List<PlaceBetsBean> findByUserIdOrderByIdDesc(String userId);
	
	List<PlaceBetsBean> findByBetStatusOrderById(String betStatus);
	
	List<PlaceBetsBean> findByUserIdAndBetResultOrderByIdDesc(String userId,String betResult);
}
