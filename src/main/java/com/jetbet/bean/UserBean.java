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
import org.springframework.data.annotation.LastModifiedDate;

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
	
	@Column(name = "user_id" , updatable=false)
	private String userId;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "password" , updatable=false)
	private String password;
	
	@Column(name = "user_Role" , updatable=false)
	private String userRole;
	
	@Column(name = "parent" , updatable=false)
	private String parent;
	
	@CreationTimestamp
	@Column(name = "Reg_Date" , updatable=false)
	private Date regDate;
	
	@Column(name = "Partnership_id" , updatable=false)
	private int partnership;
	
	@Column(name = "Odds_Commission")
	private float oddsCommission;
	
	@Column(name = "Session_Commission")
	private float sessionCommission;
	
	@Column(name = "Bet_Delay")
	private int betDelay;
	
	@Column(name = "Session_Delay")
	private int sessionDelay;
	
	@Column(name = "User_Limit")
	private long userLimit;
	
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
	
	@Column(name = "chips" , updatable=false)
	private double chips;
	
	@Column(name = "isactive" , updatable=false)
	private String isActive="Y";
	
	@Column(name = "isuserlock" , updatable=false)
	private String isUserLock="N";
	
	@Column(name = "isbettinglock" , updatable=false)
	private String isBettingLock="N";
	
	@Column(name = "remarks")
	private String remarks;
	
	@LastModifiedDate
	@Column(name = "lastupdateddate")
	private Date lastUpdatedDate; 
	
	@Column(name = "lastupdateby")
	private String lastUpdateBy;  
	
	@CreationTimestamp
	@Column(name = "createddate" , updatable=false)
	private Date createdDate;   
	
	@Column(name = "createdby" , updatable=false)
	private String createdBy;

	public UserBean(Long id, String userId, String fullName, String userRole, String parent, Date regDate,
			float oddsCommission, float sessionCommission, int betDelay, int sessionDelay, long userLimit,
			double maxProfit, double maxLoss, double oddsMaxStake, double goingInPlayStake, double sessionMaxStake,
			double chips, String isActive, String isUserLock, String isBettingLock, String remarks,
			Date lastUpdatedDate, String lastUpdateBy, Date createdDate, String createdBy) {
		super();
		this.id = id;
		this.userId = userId;
		this.fullName = fullName;
		this.userRole = userRole;
		this.parent = parent;
		this.regDate = regDate;
		this.oddsCommission = oddsCommission;
		this.sessionCommission = sessionCommission;
		this.betDelay = betDelay;
		this.sessionDelay = sessionDelay;
		this.userLimit = userLimit;
		this.maxProfit = maxProfit;
		this.maxLoss = maxLoss;
		this.oddsMaxStake = oddsMaxStake;
		this.goingInPlayStake = goingInPlayStake;
		this.sessionMaxStake = sessionMaxStake;
		this.chips = chips;
		this.isActive = isActive;
		this.isUserLock = isUserLock;
		this.isBettingLock = isBettingLock;
		this.remarks = remarks;
		this.lastUpdatedDate = lastUpdatedDate;
		this.lastUpdateBy = lastUpdateBy;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
	}
	
	
	
}
