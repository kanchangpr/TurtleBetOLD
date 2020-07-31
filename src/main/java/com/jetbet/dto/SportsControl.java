package com.jetbet.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import lombok.Getter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SportsControl {
	private String operation;
	private String operationId;
	private String isActive;
	private String userName;
}
