package com.jetbet.betfair.entities;

import java.util.Date;

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
public class Order {

	private String betId;
	private String orderType;
	private String status;
	private String persistenceType;
	private String side;
	private Double price;
	private Double size;
	private Double bspLiability;
	private Date placedDate;
	private Double avgPriceMatched;
	private Double sizeMatched;
	private Double sizeRemaining;
	private Double sizeLapsed;
	private Double sizeCancelled;
	private Double sizeVoided;

}
