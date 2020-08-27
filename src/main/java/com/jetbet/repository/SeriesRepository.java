package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.SeriesBean;

public interface SeriesRepository extends JpaRepository<SeriesBean, String>{
	
	List<SeriesBean> findByIsActiveOrderBySportId(String isActive);
	
	long countBySeriesId (String seriesId);
	
	List<SeriesBean> findBySportIdAndIsActiveOrderBySportId(String sportId,String isActive);
	
	List<SeriesBean> findBySeriesIdAndIsActiveOrderBySportId(String seriesId,String isActive);
	
	List<SeriesBean> findBySportIdOrderBySportId(String sportId);
}
