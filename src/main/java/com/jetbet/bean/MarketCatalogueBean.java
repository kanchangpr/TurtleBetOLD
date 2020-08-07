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
import org.springframework.data.annotation.LastModifiedBy;

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
@Table(name="jb_market_catalogue_details")
public class MarketCatalogueBean {
	@Id
	@SequenceGenerator(name="jb_market_catalogue_details_seq",sequenceName="jb_market_catalogue_details_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="jb_market_catalogue_details_seq")
	@Column(name = "id")
	private long id;
	
	@Column(name = "marketid")
	private String marketId;
	
	@Column(name = "markettype")
	private String marketType;
	
	@Column(name = "marketname")
	private String marketName;
	
	@Column(name = "marketstarttime")
	private Date marketStartTime;
	
	@Column(name = "totalmatched")
	private String totalMatched;
	
	@Column(name = "runnerselectionid")
	private Long runnerSelectionId;
	
	@Column(name = "runnername")
	private String runnerName;
	
	@Column(name = "handicap")
	private Double handicap;
	
	@Column(name = "sortpriority")
	private int sortPriority;
	
	@Column(name = "sportsid")
	private String sportsId;
	
	@Column(name = "sprotsname")
	private String sprotsName;
	
	@Column(name = "seriesid")
	private String seriesId;
	
	@Column(name = "seriesname")
	private String seriesName;
	
	@Column(name = "matchid")
	private String matchId;
	
	@Column(name = "matchname")
	private String matchName;
	
	@Column(name = "matchcountrycode")
	private String matchCountryCode;
	
	@Column(name = "matchtimezone")
	private String matchTimeZone;
	
	@Column(name = "matchvenue")
	private String matchVenue;
	
	@Column(name = "matchopendate")
	private Date matchOpenDate;
	
	@Column(name = "createdby")
	private String createdBy;
	
	@CreationTimestamp
	@Column(name = "createddate")
	private Date createdDate;
	
	@Column(name = "lastupdateby")
	private String lastUpdateBy;
	
	@LastModifiedBy
	@Column(name = "lastupdateddate")
	private Date lastUpdatedDate;
}
