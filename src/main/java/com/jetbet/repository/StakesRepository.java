package com.jetbet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.StakesBean;

public interface StakesRepository extends JpaRepository<StakesBean, Long> {

	StakesBean findByUserId(String userId);
}
