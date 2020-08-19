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
public class MarketCatalogue {

	private String marketId;
	private String marketName;
	private MarketDescription description;
	private Date marketStartTime;
	private String totalMatched;
	private List<RunnerCatalog> runners = null;
	private EventType eventType;
	private Competition competition;
	private Event event;
	

}
