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
	private String selectionId;
	private String runnerName;
	private String teamA;
	private String teamB;
	private String draw;
	private Date matchOpenDate;
}
