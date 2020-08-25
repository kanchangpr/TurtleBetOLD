package com.jetbet.dto;

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
public class BetSettlementDto {
	private String userId;
	private double stake;
	private double liability;
	public BetSettlementDto(String userId,  double amount,
			 double adminStakes, double smStakes, double masterStakes) {
		super();
		this.userId = userId;
		this.amount = amount;
		this.adminStakes = adminStakes;
		this.smStakes = smStakes;
		this.masterStakes = masterStakes;
	}
	
	public BetSettlementDto(String userId,  double amount,
			 double adminStakes, double smStakes) {
		super();
		this.userId = userId;
		this.amount = amount;
		this.adminStakes = adminStakes;
		this.smStakes = smStakes;
	}
	
	public BetSettlementDto(String userId,  double amount,
			 double adminStakes) {
		super();
		this.userId = userId;
		this.amount = amount;
		this.adminStakes = adminStakes;
	}
	private double profit;
	private double loss;
	private double amount;
	private double commision;
	private String admin;
	private double adminStakes;
	private String sm;
	private double smStakes;
	private String master;
	private double masterStakes;
}
