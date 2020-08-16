package com.jetbet.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@Table(name = "jb_lookup_table")
public class LookupTableBean {

	@Id
    @SequenceGenerator(name="jb_lookup_table_seq",sequenceName="jb_lookup_table_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="jb_lookup_table_seq")
	private Long id;
	
	@Column(name = "lookup_type")
	private String lookupType;
	
	@Column(name = "lookup_value")
	private String lookupValue;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "last_updated_date")
	private Date lastUpdatedDate;
	
	@Column(name = "last_updated_by")
	private String lastUpdatedBy;
}
