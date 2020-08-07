package com.jetbet.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "JB_BET_DETAILS")
public class PlaceBetsBean {
	
	@Id
	@SequenceGenerator(name="JB_BET_DETAILS_SEQ",sequenceName="JB_BET_DETAILS_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="JB_BET_DETAILS_SEQ")
	@Column(name = "id")
	private Long id; 
	
	@Column(name = "loginid")
	private String loginId;
	
	@Column(name = "userid")
	private String userId;
	
	@Column(name = "parent")
	private String parent;
	
	@Column(name = "matchid")
	private String matchId;
	
	@Column(name = "marketid")
	private String marketId;
	
	@Column(name = "selectionid")
	private String selectionId;
	
	@Column(name = "selectionname")
	private String selectionName;
	
	@Column(name = "betplacedate")
	private String betPlaceDate;
	
	@Column(name = "odds")
	private double odds;
	
	@Column(name = "stake")
	private double stake;
	
	@Column(name = "porfitloss")
	private double porfitLoss;
	
	@Column(name = "isback")
	private String isback;
	
	@Column(name = "islay")
	private String isLay;
	
	@Column(name = "psid")
	private String psId;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "createdby")
	private String createdBy;
	
	@Column(name = "createddate")
	private Date createdDate;
	
	@Column(name = "lastupdateby")
	private String lastUpdateBy;
	
	@Column(name = "lastupdateddate")
	private Date lastUpdatedDate;
}
