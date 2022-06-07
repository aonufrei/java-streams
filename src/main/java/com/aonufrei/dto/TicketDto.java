package com.aonufrei.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

	private String id = UUID.randomUUID().toString();

	private String name;

	private String buyer;

	private Boolean sold = false;

	public TicketDto(String name) {
		this.name = name;
	}

	public TicketDto(String name, boolean sold) {
		this.name = name;
		this.sold = sold;
	}

	public TicketDto(String id, String name, boolean sold) {
		this.id = id;
		this.name = name;
		this.sold = sold;
	}
}
