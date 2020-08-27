package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.MatchBean;

public interface MatchRepository extends JpaRepository<MatchBean, String>{
	List<MatchBean> findByMatchIdAndIsActive(String matchId,String isActive);
	
	List<MatchBean> findByIsActive(String isActive);
	
	List<MatchBean> findBySportId(String sportId);
	
	List<MatchBean> findBySeriesId(String seriesId);
	
	List<MatchBean> findBySportIdAndSeriesId(String sportId,String seriesId);
	
	List<MatchBean> findBySportIdAndIsActive(String sportId,String isActive);
	
	List<MatchBean> findBySeriesIdAndIsActive(String seriesId,String isActive);
	
	List<MatchBean> findBySportIdAndSeriesIdAndIsActive(String sportId,String seriesId,String isActive);
	
	long countByMatchId (String matchId);
	
}
