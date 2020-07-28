package com.jetbet.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "JB_USER_DETAILS")
public class UserBean {
	@Id
    @SequenceGenerator(name="jb_user_details_seq",sequenceName="jb_user_details_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="jb_user_details_seq")
	private Long id;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "user_Role")
	private String userRole;
	
	@Column(name = "parent")
	private String parent;
	
	@CreationTimestamp
	@Column(name = "Reg_Date")
	private Date regDate;
	
	@Column(name = "Partnership")
	private double partnership;
	
	@Column(name = "Odds_Commission")
	private double oddsCommission;
	
	@Column(name = "Session_Commission")
	private double sessionCommission;
	
	@Column(name = "Bet_Delay")
	private double betDelay;
	
	@Column(name = "Session_Delay")
	private double sessionDelay;
	
	@Column(name = "User_Limit")
	private double userLimit;
	
	@Column(name = "Max_Profit")
	private double maxProfit;
	
	@Column(name = "Max_Loss")
	private double maxLoss;
	
	@Column(name = "Odds_Max_Stake")
	private double oddsMaxStake;
	
	@Column(name = "Going_In_Play_Stake")
	private double goingInPlayStake;
	
	@Column(name = "Session_Max_Stake")
	private double sessionMaxStake;
	
	@Column(name = "chips")
	private double chips;
	
	@Column(name = "isactive")
	private String isActive="Y";
	
	@Column(name = "isuserlock")
	private String isUserLock="N";
	
	@Column(name = "isbettinglock")
	private String isBettingLock="N";
	
	@Column(name = "remarks")
	private String remarks;
	
	@UpdateTimestamp
	@Column(name = "lastupdateddate")
	private Date lastUpdatedDate; 
	
	@Column(name = "lastupdateby")
	private String lastUpdateBy;  
	
	@CreationTimestamp
	@Column(name = "createddate")
	private Date createdDate;   
	
	@Column(name = "createdby")
	private String createdBy;
}
