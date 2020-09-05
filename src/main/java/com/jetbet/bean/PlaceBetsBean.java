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
	
	
	
	public PlaceBetsBean(String userId, String matchId, String runnerName, double masterStakes) {
		super();
		this.userId = userId;
		this.matchId = matchId;
		this.runnerName = runnerName;
		this.masterStakes = masterStakes;
	}

	public PlaceBetsBean(Long id,String userId, String matchId, String matchName, String marketId, String marketName,
			Long selectionId, String runnerName, Date betPlaceDate, double odds, double stake, double liability,
			String isback, String isLay) {
		super();
		this.id=id;
		this.userId = userId;
		this.matchId = matchId;
		this.matchName = matchName;
		this.marketId = marketId;
		this.marketName = marketName;
		this.selectionId = selectionId;
		this.runnerName = runnerName;
		this.betPlaceDate = betPlaceDate;
		this.odds = odds;
		this.stake = stake;
		this.liability = liability;
		this.isback = isback;
		this.isLay = isLay;
	}

	@Id
	@SequenceGenerator(name="JB_BETS_DETAILS_SEQ",sequenceName="JB_BETS_DETAILS_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="JB_BETS_DETAILS_SEQ")
	@Column(name = "id")
	private Long id; 
	
	@Column(name = "login_id" , updatable = false)
	private String loginId;
	
	@Column(name = "user_id" , updatable = false)
	private String userId;
	
	@Column(name = "parent" , updatable = false)
	private String parent;
	
	@Column(name = "sports_id" , updatable = false)
	private String sportsId;
	@Column(name = "sports_name" , updatable = false)
	private String sportsName;
	@Column(name = "series_id" , updatable = false)
	private String seriesId;
	@Column(name = "series_name" , updatable = false)
	private String seriesName;
	
	
	@Column(name = "match_id" , updatable = false)
	private String matchId;
	@Column(name = "match_name" , updatable = false)
	private String matchName;
	
	@Column(name = "market_id", updatable = false)
	private String marketId;
	@Column(name = "market_name", updatable = false)
	private String marketName;
	
	@Column(name = "selection_id", updatable = false)
	private Long selectionId;
	
	@Column(name = "runner_name", updatable = false)
	private String runnerName;
	
	@Column(name = "MARKET_TYPE" , updatable = false)
	private String marketType;
	
	@CreationTimestamp
	@Column(name = "bet_place_date", updatable = false)
	private Date betPlaceDate;
	
	@Column(name = "odds", updatable = false)
	private double odds;
	
	@Column(name = "stake", updatable = false)
	private double stake;
	
	@Column(name = "liability", updatable = false)
	private double liability;
	
	@Column(name = "profit")
	private double profit;
	
	@Column(name = "loss")
	private double loss;
	
	@Column(name = "net_amount")
	private double netAmount;
	
	@Column(name = "commision")
	private double commision;
	
	@Column(name = "admin_stakes")
	private double adminStakes;
	
	@Column(name = "sm_stakes")
	private double smStakes;
	
	@Column(name = "master_stakes")
	private double masterStakes;
	//
	@Column(name = "user_pl")
	private double userPl;
	
	@Column(name = "master_pl")
	private double masterPl;
	
	@Column(name = "sm_pl")
	private double smPl;
	
	@Column(name = "admin_pl")
	private double adminPl;
	
	
	@Column(name = "admin_settle")
	private String adminSettle;
	
	@Column(name = "sm_settle")
	private String smSettle;
	
	@Column(name = "master_settle")
	private String masterSettle;
	
	@Column(name = "isback" , updatable = false)
	private String isback;
	
	@Column(name = "islay" , updatable = false)
	private String isLay;
	
	@Column(name = "psid" , updatable = false)
	private long psId;
	
	@Column(name = "remarks" , updatable = false)
	private String remarks;
	
	@Column(name = "bet_status" , updatable = false)
	private String betStatus;
	
	@Column(name = "bet_result" , updatable = false)
	private String betResult;
	
	@Column(name = "bet_settlement")
	private String betSettlement;
	
	@Column(name = "created_by" , updatable = false)
	private String createdBy;
	
	@CreationTimestamp
	@Column(name = "created_date" , updatable = false)
	private Date createdDate;
	
	@Column(name = "last_updated_by")
	private String lastUpdateBy;
	
	@UpdateTimestamp
	@Column(name = "last_updated_date")
	private Date lastUpdatedDate;
}
