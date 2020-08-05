package com.jetbet.dto;

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
public class ChipsDto {
	private long chips;
	private String userRole;
	private String toUser;
	private String fromUser;
	private String action;
	private String userName;
}
