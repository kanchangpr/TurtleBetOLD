package com.jetbet.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
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
@Table(name = "JB_FANCY_DETAILS")
public class FancyBean {
	
	@Id
	@OrderBy
	@Column(name = "market_type")
	private String marketType;
	
	@Column(name = "market_count")
	private int marketCount;
	
	@Column(name = "series_id")
	private String seriesId;
	
	@Column(name = "sports_id")
	private String sportId;
	
	@Column(name = "is_active")
	private String isActive="N";
	
	@Column(name = "fancy_created_by")
	private String fancyCreatedBy;
	
	@CreationTimestamp
	@Column(name = "fancy_created_date")
	private Date fancyCreatedDate;
	
	@Column(name = "fancy_updated_by")
	private String fancyUpdatedBy;
	
	@LastModifiedDate
	@Column(name = "fancy_updated_date")
	private Date fancyUpdatedDate;

}
