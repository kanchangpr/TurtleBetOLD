package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.FancyBean;
import com.jetbet.dto.FancyIdDto;

public interface FancyRepository extends JpaRepository<FancyBean, FancyIdDto>{
	List<FancyBean> findByIsActive(String isActive);
	
	List<FancyBean> findByFancyIdMatchIdAndIsActive(String matchId,String isActive);
	
	long countByFancyIdMarketTypeAndFancyIdMatchId (String marketType, String matchId);
	
	List<FancyBean> findByFancyIdMarketTypeAndFancyIdMatchIdAndIsActive (String marketType, String matchId,String isActive);
	
	
	List<FancyBean> findBySportIdAndFancyIdMatchIdAndIsActive (String sportId, String matchId,String isActive);
}
