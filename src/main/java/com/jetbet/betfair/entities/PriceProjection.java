package com.jetbet.betfair.entities;


import java.util.Set;

import com.jetbet.betfair.enums.PriceData;

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
public class PriceProjection {
	private Set<PriceData> priceData;
	private ExBestOfferOverRides exBestOfferOverRides;
	private boolean virtualise;
	private boolean rolloverStakes;

}
