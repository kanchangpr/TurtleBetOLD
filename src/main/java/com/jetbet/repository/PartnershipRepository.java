package com.jetbet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.PartnershipBean;

public interface PartnershipRepository extends JpaRepository<PartnershipBean, Integer>{
	PartnershipBean findByUserId(String userId);
}
