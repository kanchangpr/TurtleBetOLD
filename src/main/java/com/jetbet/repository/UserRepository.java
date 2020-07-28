package com.jetbet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.UserBean;

public interface UserRepository extends JpaRepository<UserBean, Long>{

	UserBean findByUserId(String userName);
	
	long countByUserId(String userName);

}
