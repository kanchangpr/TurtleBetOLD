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
@Table(name = "jb_runners_Details")
public class RunnersBean {

    
    @Id
	@Column(name = "match_id")
	private String matchId;
	
	@Column(name = "market_id")
	private String marketId;
	
	@Column(name = "teama_id")
	private long teama_id;
	
	@Column(name = "teama_name")
	private String teama_name;
	
	@Column(name = "teamb_id")
	private long teamb_id;
	
	@Column(name = "teamb_name")
	private String teamb_name;
	
	@Column(name = "teamc_id")
	private long teamc_id;
	
	@Column(name = "teamc_name")
	private String teamc_name;
}
