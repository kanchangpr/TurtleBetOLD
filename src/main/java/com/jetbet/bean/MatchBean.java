package com.jetbet.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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
@Table(name = "JB_MATCH_DETAILS")
public class MatchBean {
	
	@Id
	@Column(name = "match_id")
	private String matchId;
	
	@Column(name = "match_name")
	private String matchName;
	
	@Column(name = "match_country_code")
	private String matchCountryCode;
	
	@Column(name = "match_time_zone")
	private String matchTimezone;
	
	@Column(name = "match_venue")
	private String matchVenue;
	
	@Column(name = "match_open_date")
	private Date matchOpenDate;
	
	@Column(name = "match_market_count")
	private int matchMarketCount;
	
	@Column(name = "series_id")
	private String seriesId;
	
	@Column(name = "sports_id")
	private String sportId;
	
	@Column(name = "in_play" , updatable=true)
	private String inPlay;
	
	@Column(name = "is_active" , updatable=true)
	private String isActive="N";
	
	@Column(name = "match_created_by" , updatable=false)
	private String matchCreatedBy;
	
	@CreationTimestamp
	@Column(name = "match_created_date" , updatable=false)
	private Date matchCreatedDate;
	
	@Column(name = "match_updated_by")
	private String matchUpdatedBy;
	
	@UpdateTimestamp
	@Column(name = "match_updated_date")
	private Date matchUpdatedDate;
}
