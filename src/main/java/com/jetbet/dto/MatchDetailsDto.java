package com.jetbet.dto;

import java.util.Date;
import java.util.List;

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
public class MatchDetailsDto {
	private String matchId;
	private String matchName;
	private Date matchO;
	private List<MarketTypeDetailsDto> marketTypeDetail;
}
