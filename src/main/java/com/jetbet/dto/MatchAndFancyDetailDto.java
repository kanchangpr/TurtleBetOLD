package com.jetbet.dto;

import java.util.Date;
import java.util.List;

import com.jetbet.bean.MarketCatalogueBean;
import com.jetbet.betfair.entities.MarketBook;
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
	private String sportId;
	private String sportName;
	private String seriesId;
	private String seriesName;
	private String marketType;
	private int marketCount;
	private Date matchDate;
	private List<MarketCatalogue> marketCatalogueRes;
	private List<MarketBook> marketBook;
	public MatchAndFancyDetailDto(String sportName, String seriesId, String seriesName,
			String marketType, int marketCount) {
		super();
		
		this.sportName = sportName;
		this.seriesId = seriesId;
		this.seriesName = seriesName;
		this.marketType = marketType;
		this.marketCount = marketCount;
	}
}
