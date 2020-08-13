package com.jetbet.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "JB_LOGIN_DETAILS")
public class UserLoginBean  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @SequenceGenerator(name="JB_LOGIN_DETAILS_SEQ",sequenceName="JB_LOGIN_DETAILS_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="JB_LOGIN_DETAILS_SEQ")
	private Long id;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "user_role")
	private String userRole;
	
	@Column(name = "user_parent")
	private String userParent;
	
	@CreationTimestamp
	@Column(name = "login_time")
	private Date loginTime;
	
	@Column(name = "ip_address")
	private String ipAddress;
	
	@Column(name = "browser_detail")
	private String browserDetail;
}
