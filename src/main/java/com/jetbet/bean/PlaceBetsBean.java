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
	@SequenceGenerator(name="JB_BETS_DETAILS_SEQ",sequenceName="JB_BETS_DETAILS_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="JB_BETS_DETAILS_SEQ")
	@Column(name = "id")
	private Long id; 
	
	@Column(name = "login_id")
	private String loginId;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "parent")
	private String parent;
	
	@Column(name = "sports_id")
	private String sportsId;
	@Column(name = "sports_name")
	private String sportsName;
	@Column(name = "series_id")
	private String seriesId;
	@Column(name = "series_name")
	private String seriesName;
	
	
	@Column(name = "match_id")
	private String matchId;
	@Column(name = "match_name")
	private String matchName;
	
	@Column(name = "market_id")
	private String marketId;
	@Column(name = "market_name")
	private String marketName;
	
	@Column(name = "selection_id")
	private String selectionId;
	
	@Column(name = "runner_name")
	private String runnerName;
	
	@CreationTimestamp
	@Column(name = "bet_place_date")
	private Date betPlaceDate;
	
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
	private int psId;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@CreationTimestamp
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "last_updated_by")
	private String lastUpdateBy;
	
	@UpdateTimestamp
	@Column(name = "last_updated_date")
	private Date lastUpdatedDate;
}
