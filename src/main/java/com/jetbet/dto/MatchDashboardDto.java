package com.jetbet.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
//@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatchDashboardDto {
	String userId;
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
	public MatchDashboardDto(String sportsId, String matchId, String matchName, String teamAName, String teamBName,
			String teamCName, double teamAStake, double teamBStake, double teamCStake, Date matchDate) {
		super();
		this.sportsId = sportsId;
		this.matchId = matchId;
		this.matchName = matchName;
		this.teamAName = teamAName;
		this.teamBName = teamBName;
		this.teamCName = teamCName;
		this.teamAStake = teamAStake;
		this.teamBStake = teamBStake;
		this.teamCStake = teamCStake;
		this.matchDate = matchDate;
	}
	public MatchDashboardDto(String userId, String teamAName, String teamBName, String teamCName, double teamAStake,
			double teamBStake, double teamCStake) {
		super();
		this.userId = userId;
		this.teamAName = teamAName;
		this.teamBName = teamBName;
		this.teamCName = teamCName;
		this.teamAStake = teamAStake;
		this.teamBStake = teamBStake;
		this.teamCStake = teamCStake;
	}
	
}
