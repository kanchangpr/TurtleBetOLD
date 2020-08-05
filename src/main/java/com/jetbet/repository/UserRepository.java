package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.UserBean;

public interface UserRepository extends JpaRepository<UserBean, Long>{

	UserBean findFirst1ByUserIdOrderByFullName(String userName);
	
	List<UserBean> findByParentOrderByFullName(String parent);
	
	List<UserBean> findByUserIdOrderByFullName(String userName);
	
	Long countByUserId(String userName);
	
	long countByParent(String parent);
	
	List<UserBean> findAllByOrderByFullName();

}
