package com.jetbet.bean;

import java.util.Date;

import javax.persistence.Column;
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
@Table(name = "JB_PARTNERSHIP_DETAILS")
public class PartnershipBean {

	@Id
    @SequenceGenerator(name="jb_partnership_details_seq",sequenceName="jb_partnership_details_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="jb_partnership_details_seq")
	private int id;
	
	@Column(name = "user_id", updatable=false)
	private String userId;
	
	@Column(name = "user_role", updatable=false)
	private String userRole;
	
	@Column(name = "parent", updatable=false)
	private String parent;
	
	@Column(name = "admin_stake", updatable=false)
	private int adminStake;
	
	@Column(name = "supermaster_stake")
	private int supermasterStake;
	
	@Column(name = "mastrer_stake")
	private int mastrerStake;
	
	@Column(name = "remarks")
	private String remarks="remarks";
	
	@Column(name = "auto_remarks")
	private String autoRemarks="AutoRemarks";
	
	@UpdateTimestamp
	@Column(name = "lastupdateddate")
	private Date lastUpdatedDate; 
	
	@Column(name = "lastupdateby")
	private String lastUpdateBy;  
	
	@CreationTimestamp
	@Column(name = "createddate" , updatable=false)
	private Date createdDate;   
	
	@Column(name = "createdby" , updatable=false)
	private String createdBy;

}
