package com.jetbet.dto;

import java.util.Date;

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
public class MarketTypeDetailsDto {
	private String matchId;
	private String matchName;
	private Date matchOpenDate;
	private String marketType;
	private int marketCount;
}
