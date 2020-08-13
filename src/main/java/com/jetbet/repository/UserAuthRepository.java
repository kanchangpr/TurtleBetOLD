package com.jetbet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.UserAuthBean;


public interface UserAuthRepository extends JpaRepository<UserAuthBean, Long>{
	
	UserAuthBean findByUserIdAndPassword(String username, String password);
	
	UserAuthBean findByUserId(String username);
	
}
