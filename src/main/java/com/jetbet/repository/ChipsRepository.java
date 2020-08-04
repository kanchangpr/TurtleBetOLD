package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.ChipsBean;

public interface ChipsRepository extends JpaRepository<ChipsBean, Long>{
	
	List<ChipsBean> findByUserIdOrderById(String userId);
}
