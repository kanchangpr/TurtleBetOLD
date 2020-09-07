package com.jetbet.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import com.jetbet.dto.FancyIdDto;

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
public class FancyBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FancyIdDto fancyId;
	
//	@Column(name = "market_type")
//	private String marketType;
//
	@Column(name = "match_name")
	private String matchName;
	
	@Column(name = "market_count")
	private int marketCount;
	
	@Column(name = "series_id")
	private String seriesId;
	
	@Column(name = "sports_id")
	private String sportId;
	
	@Column(name = "is_active" , updatable=true)
	private String isActive="N";
	
	@Column(name = "fancy_created_by" , updatable=false)
	private String fancyCreatedBy;
	
	@CreationTimestamp
	@Column(name = "fancy_created_date" , updatable=false)
	private Date fancyCreatedDate;
	
	@Column(name = "fancy_updated_by")
	private String fancyUpdatedBy;
	
	@UpdateTimestamp
	@Column(name = "fancy_updated_date")
	private Date fancyUpdatedDate;

//	public FancyBean(String marketType, int marketCount, String matchId, String seriesId, String sportId,
//			String isActive, String fancyCreatedBy, Date fancyCreatedDate) {
//		super();
//		this.marketType = marketType;
//		this.marketCount = marketCount;
//		this.matchId = matchId;
//		this.seriesId = seriesId;
//		this.sportId = sportId;
//		this.isActive = isActive;
//		this.fancyCreatedBy = fancyCreatedBy;
//		this.fancyCreatedDate = fancyCreatedDate;
//	}

	public FancyBean(FancyIdDto fancyId, int marketCount,String matchName, String seriesId, String sportId, String isActive,
			String fancyCreatedBy, Date fancyCreatedDate) {
		super();
		this.fancyId = fancyId;
		this.marketCount = marketCount;
		this.matchName = matchName;
		this.seriesId = seriesId;
		this.sportId = sportId;
		this.isActive = isActive;
		this.fancyCreatedBy = fancyCreatedBy;
		this.fancyCreatedDate = fancyCreatedDate;
	}

	
	
	
}
