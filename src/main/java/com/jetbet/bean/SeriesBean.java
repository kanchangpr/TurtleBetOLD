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
@Table(name = "JB_SERIES_DETAILS")
public class SeriesBean {
	@Id
	@OrderBy
	@Column(name = "series_id")
	private String seriesId;
	
	@Column(name = "series_name")
	private String seriesName;
	
	@Column(name = "series_market_count")
	private int seriesMarketCount;
	
	@Column(name = "series_competition_region")
	private String seriesCompRegion;
	
	@Column(name = "sports_id")
	private String sportId;
	
	@Column(name = "is_active" , updatable=false)
	private String isActive="N";
	
	@Column(name = "series_created_by" , updatable=false)
	private String seriesCreatedBy;
	
	@CreationTimestamp
	@Column(name = "series_created_date" , updatable=false)
	private Date seriesCreatedDate;
	
	@Column(name = "series_updated_by")
	private String seriesUpdatedBy;
	
	@UpdateTimestamp
	@Column(name = "series_updated_date")
	private Date seriesUpdatedDate;
}
