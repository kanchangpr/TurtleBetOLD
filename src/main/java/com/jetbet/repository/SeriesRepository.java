package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.SeriesBean;

public interface SeriesRepository extends JpaRepository<SeriesBean, String>{
	
	List<SeriesBean> findByIsActive(String isActive);
	
	long countBySeriesId (String seriesId);
	
	List<SeriesBean> findBySportIdAndIsActive(String sportId,String isActive);
}
