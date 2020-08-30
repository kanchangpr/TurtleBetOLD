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
	
	
	private String fullName;
	
	
	private String password;
	
	
	private String userRole;
	
	
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
	
	
	private String remarks;
	
	
	private String userName;
}
