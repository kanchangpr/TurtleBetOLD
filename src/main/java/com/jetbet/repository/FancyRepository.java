package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.FancyBean;

public interface FancyRepository extends JpaRepository<FancyBean, String>{
	List<FancyBean> findByIsActive(String isActive);
	
	long countByMarketType (String marketType);
}
