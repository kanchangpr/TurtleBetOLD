package com.jetbet.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DashboardMatchListDto {
	private String matchId;
	private String matchName;
	private String marketId;
	private String marketType;
	private Double teamABackPrize;
	private Double teamBBackPrize;
	private Double drawBackPrize;
	private Double teamABackSize;
	private Double teamBBackSize;
	private Double drawBackSize;
	
	private Double teamALayPrize;
	private Double teamBLayPrize;
	private Double drawLayPrize;
	private Double teamALaySize;
	private Double teamBLaySize;
	private Double drawLaySize;
	
	private Long teamAId;
	private Long teamBId;
	private Long drawId;
	
	private String teamA;
	private String teamB;
	private String draw;
	
	private String teamAResult;
	private String teamBResult;
	private String drawResult;
	private Date matchOpenDate;
}
