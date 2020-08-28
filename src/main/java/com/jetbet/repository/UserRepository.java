package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.UserBean;

public interface UserRepository extends JpaRepository<UserBean, Long>{

	UserBean findFirst1ByUserId(String userName);
	
	List<UserBean> findByParentOrderByUserId(String parent);
	
	List<UserBean> findByUserIdOrderByUserId(String userName);
	
	Long countByUserId(String userName);
	
	long countByParent(String parent);
	
	List<UserBean> findAllByOrderByUserId();
	
	UserBean findByUserIdAndPassword(String username, String password);
	
	UserBean findByUserId(String username);

}
