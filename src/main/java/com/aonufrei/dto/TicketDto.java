package com.aonufrei.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

	private String id = UUID.randomUUID().toString();

	private String name;

	private String buyer;

	private Integer price;

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

	public TicketDto copy() {
		return TicketDto.builder()
				.id(id)
				.name(name)
				.sold(sold)
				.buyer(buyer)
				.build();
	}
}
