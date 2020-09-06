package com.jetbet.betfair.entities;

import java.util.Date;
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
public class Runner {
	private Long selectionId;
	private String runnerName;
	private Double userPl;
	private Double handicap;
	private String status;
	private Double adjustmentFactor;
	private Double lastPriceTraded;
	private Double totalMatched;
	private Date removalDate;
	private StartingPrices sp;
	private ExchangePrices ex;
	private List<Order> orders;
	private List<Match> matches;

}
