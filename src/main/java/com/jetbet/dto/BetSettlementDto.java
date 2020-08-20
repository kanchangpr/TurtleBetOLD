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
	public BetSettlementDto(String userId, double stake, double liability, double profit, double loss, double amount,
			double commision, double adminStakes, double smStakes, double masterStakes) {
		super();
		this.userId = userId;
		this.stake = stake;
		this.liability = liability;
		this.profit = profit;
		this.loss = loss;
		this.amount = amount;
		this.commision = commision;
		this.adminStakes = adminStakes;
		this.smStakes = smStakes;
		this.masterStakes = masterStakes;
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
