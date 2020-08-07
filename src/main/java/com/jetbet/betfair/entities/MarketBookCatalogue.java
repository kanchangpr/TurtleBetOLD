package com.jetbet.betfair.entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MarketBookCatalogue {
	private List<MarketBook> marketBook;
	private List<MarketCatalogue> marketCatalogue;
}
