package com.jetbet.betfair.entities;


import java.util.Date;

import com.jetbet.betfair.enums.InstructionReportErrorCode;
import com.jetbet.betfair.enums.InstructionReportStatus;

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
public class PlaceInstructionReport {
	private InstructionReportStatus status;
	private InstructionReportErrorCode errorCode;
	private PlaceInstruction instructionl;
	private String betId;
	private Date placedDate;
	private double averagePriceMatched;
	private double sizeMatched;
}
