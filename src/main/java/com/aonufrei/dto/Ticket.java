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
public class Ticket {

	private String id = UUID.randomUUID().toString();

	private String name;

	private String buyer;

	private Integer price;

	private Boolean sold = false;

	public Ticket(String name) {
		this.name = name;
	}

	public Ticket(String name, boolean sold) {
		this.name = name;
		this.sold = sold;
	}

	public Ticket(String id, String name, boolean sold) {
		this.id = id;
		this.name = name;
		this.sold = sold;
	}

	public Ticket copy() {
		return Ticket.builder()
				.id(id)
				.name(name)
				.sold(sold)
				.buyer(buyer)
				.build();
	}
}
