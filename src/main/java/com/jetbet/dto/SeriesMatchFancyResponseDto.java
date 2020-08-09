package com.jetbet.dto;

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
public class SeriesMatchFancyResponseDto {
	private String seriesId;
	private String seriesName;
	private List<MatchAndFancyDetailDto> matchAndFancyDetail;
}
