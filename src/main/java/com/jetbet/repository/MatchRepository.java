package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.MatchBean;

public interface MatchRepository extends JpaRepository<MatchBean, String>{
	List<MatchBean> findByIsActive(String isActive);
	
	long countByMatchId (String matchId);
	
}
