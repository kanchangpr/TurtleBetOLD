package com.jetbet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.UserLoginBean;

public interface UserLoginRepository extends JpaRepository<UserLoginBean, Long> {

}
