package com.jetbet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.LookupTableBean;

public interface LookupTableRepository extends JpaRepository<LookupTableBean, Long>{
	
	LookupTableBean findByLookupType(String lookupType);

}
