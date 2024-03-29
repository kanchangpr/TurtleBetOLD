package com.jetbet.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
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
@Table(name = "JB_SPORTS_DETAILS")
public class SportsBean {
	@Id
	@OrderBy
	@Column(name = "sports_id")
	private String sportsTypeId;
	
	@Column(name = "sports_name")
	private String sportsName;
	
	@Column(name = "sports_market_count")
	private int sportsMarketCount;
	
	@Column(name = "isActive")
	private String isActive="N";
	
	@Column(name = "sports_created_by")
	private String sportsCreatedBy;
	
	@CreationTimestamp
	@Column(name = "sports_created_date")
	private Date sportsCreatedDate;
	
	@Column(name = "sports_updated_by")
	private String sportsUpdatedBy;
	
	@UpdateTimestamp
	@Column(name = "sports_updated_date")
	private Date sportsUpdatedDate;

}
