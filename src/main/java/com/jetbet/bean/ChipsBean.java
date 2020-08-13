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
@Table(name = "JB_CHIPS_DETAILS")
public class ChipsBean {
	@Id
    @SequenceGenerator(name="jb_chips_details_seq",sequenceName="jb_chips_details_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="jb_chips_details_seq")
	private Long id;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "from_user")
	private String fromUser;
	
	@Column(name = "credit")
	private double credit;
	
	@Column(name = "debit")
	private double debit;
	
	@Column(name = "total_chips")
	private double totalChips;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "betting_id")
	private long bettingId;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@CreationTimestamp
	@Column(name = "created_on")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@UpdateTimestamp
	@Column(name = "updated_on")
	private Date updatedDate;

}
