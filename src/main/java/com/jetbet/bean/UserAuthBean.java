package com.jetbet.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Component
@Table(name = "JB_USER_DETAILS")
public class UserAuthBean {
	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "user_role")
	private String role;
	
	@Column(name = "isactive")
	private String isActive;
	
	@Column(name = "isuserlock")
	private String isUserLock;
	
	@Column(name = "isbettinglock")
	private String isBettingLock;
}
