package com.jetbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.UserLoginBean;

public interface UserLoginRepository extends JpaRepository<UserLoginBean, Long> {

	List<UserLoginBean> findByUserIdOrderById(String userId);
}
