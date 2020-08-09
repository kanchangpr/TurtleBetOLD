package com.jetbet.dto;

import java.util.Date;
import java.util.List;

import com.jetbet.bean.MarketCatalogueBean;
import com.jetbet.betfair.entities.MarketCatalogue;

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
public class MatchAndFancyDetailDto {
	private String matchId;
	private String matchName;
	private String marketType;
	private String marketCount;
	private Date matchDate;
	private List<MarketCatalogue> marketCatalogueRes;
}
