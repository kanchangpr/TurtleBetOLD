package com.jetbet.betfair.entities;

import com.jetbet.betfair.enums.MarketBettingType;
import com.jetbet.betfair.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MarketFilter {

	private String textQuery;
	private Set<String> exchangeIds;
	private Set<String> eventTypeIds;
	private Set<String> marketIds;
	private Boolean inPlayOnly;
	private Set<String> eventIds;
	private Set<String> competitionIds;
	private Set<String> venues;
	private Boolean bspOnly;
	private Boolean turnInPlayEnabled;
	private Set<MarketBettingType> marketBettingTypes;
	private Set<String> marketCountries;
	private Set<String> marketTypeCodes;
	private TimeRange marketStartTime;
	private Set<OrderStatus> withOrders;


}
