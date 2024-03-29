package com.jetbet.betfair.entities;

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
public class CompetitionResult {
	private String eventTypeId;
	private Competition competition ; 
	private int marketCount;
	private String competitionRegion;
}
