package com.jetbet.betfair.entities;

import com.jetbet.betfair.enums.PersistenceType;

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
public class LimitOrder {

	private double size;
	private double price;
	private PersistenceType persistenceType;
}
