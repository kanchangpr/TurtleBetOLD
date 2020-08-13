package com.jetbet.bean;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

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
public class JwtTokenRequest implements Serializable {

	private static final long serialVersionUID = -5616176897013108345L;

	@NotBlank
	private String username;
	@NotBlank
	private String password;

}
