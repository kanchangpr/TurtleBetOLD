package com.jetbet.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatchDashboardDto {

	String sportsId;
	//String sportsName;
	String matchId;
	String matchName;
//	String teamAId;
//	String teamBId;
//	String teamCId;
	String teamAName;
	String teamBName;
	String teamCName;
	double teamAStake;
	double teamBStake;
	double teamCStake;
	Date matchDate;
}
