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
public class ExchangePrices {

	private List<PriceSize> availableToBack;
	private List<PriceSize> availableToLay;
	private List<PriceSize> tradedVolume;

}
