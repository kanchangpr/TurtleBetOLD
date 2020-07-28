package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.SportsBean;

public interface SportsRepository extends JpaRepository<SportsBean, String>{
	
	List<SportsBean> findByIsActive(String isActive);
	
	long countBySportsTypeId (String sportsTypeId);
	
}
