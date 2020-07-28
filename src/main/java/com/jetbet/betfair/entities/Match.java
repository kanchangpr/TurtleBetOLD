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
public class Match {

	private String betId;
	private String matchId;
	private String side;
	private Double price;
	private Double Size;
	private Date matchDate;

}
