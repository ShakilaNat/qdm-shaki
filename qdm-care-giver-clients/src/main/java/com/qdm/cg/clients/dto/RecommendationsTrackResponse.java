package com.qdm.cg.clients.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationsTrackResponse {
	private String product_name;
	private int product_id;
	private String product_price;
	private String current_status;
	private List<TimeLine> timeline;
}