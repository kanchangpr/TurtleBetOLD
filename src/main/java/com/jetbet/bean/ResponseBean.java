package com.jetbet.bean;
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
public class ResponseBean {
	private String status;
	private String errorCode;
	private String errorMsg;
	private String token;
	private String userName;
	private String userRole;
	private String userParent;
}
