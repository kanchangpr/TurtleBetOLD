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
public class StartingPrices {
	private Double nearPrice;
	private Double farPrice;
	private List<PriceSize> backStakeTaken = null;
	private List<PriceSize> layLiabilityTaken = null;
	private Double actualSP;
}
