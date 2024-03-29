package com.jetbet.betfair.entities;

import java.util.List;

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
public class RunnerCatalog {
	private Long selectionId;
	private String runnerName;
	private Double handicap;
	private int sortPriority;
	//private ExchangePrices ex;
	private Runner runner;
	private String status;
	
}
