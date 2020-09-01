package com.jetbet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.RunnersBean;

public interface RunnersRepository extends JpaRepository<RunnersBean, Long>{

	long countByMatchId(String matchId);
	
}
