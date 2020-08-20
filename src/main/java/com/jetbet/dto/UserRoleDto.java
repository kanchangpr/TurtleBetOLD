package com.jetbet.dto;

import java.util.Date;

import com.jetbet.bean.SeriesBean;

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
public class UserRoleDto {

	private String userId;
	private String userRole;
}
