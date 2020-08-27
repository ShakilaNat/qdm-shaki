package com.qdm.cg.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientActivityDto {
	private int activity_id;
	private String activity_name;
	private String client_name;
	private String date_time;
	private Boolean is_attended;

}