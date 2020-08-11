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
@Table(name = "jb_stakes_details")
public class StakesBean {
	@Id
    @SequenceGenerator(name="jb_stakes_details_seq",sequenceName="jb_stakes_details_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="jb_stakes_details_seq")
	private Long id;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "stake1")
	private double stake1;
	
	@Column(name = "stake2")
	private double stake2;
	
	@Column(name = "stake3")
	private double stake3;
	
	@Column(name = "stake4")
	private double stake4;
	
	@Column(name = "stake5")
	private double stake5;
	
	@UpdateTimestamp
	@Column(name = "updated_on")
	private Date lastUpdatedDate; 
	
	@Column(name = "updated_by")
	private String lastUpdateBy;  
	
	@CreationTimestamp
	@Column(name = "created_on" , updatable=false)
	private Date createdDate;   
	
	@Column(name = "created_by" , updatable=false)
	private String createdBy;
}
