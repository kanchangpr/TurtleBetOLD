package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.UserBean;

public interface UserRepository extends JpaRepository<UserBean, Long>{

	UserBean findFirst1ByUserId(String userName);
	
	List<UserBean> findByParent(String parent);
	
	List<UserBean> findByUserId(String userName);
	
	long countByUserId(String userName);
	
	long countByParent(String parent);

}
