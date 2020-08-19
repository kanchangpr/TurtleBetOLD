package com.jetbet.dto;

import javax.validation.constraints.NotNull;

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
public class UserDetailsRequestDto {
	
	@NotNull
	private String userId;
	
	@NotNull
	private String fullName;
	
	@NotNull
	private String password;
	
	@NotNull
	private String userRole;
	
	@NotNull
	private String parent;
	
	private int partnership;
	private double oddsCommission;
	private double sessionCommission;
	private int betDelay;
	private int sessionDelay;
	private int userLimit;
//	private int maxProfit;
//	private int maxLoss;
//	private int oddsMaxStake;
//	private int goingInPlayStake;
//	private int sessionMaxStake;
	
	@NotNull
	private String remarks;
	
	@NotNull
	private String userName;
}
