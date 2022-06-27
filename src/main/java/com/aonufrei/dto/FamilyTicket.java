package com.aonufrei.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyTicket {

	private String id = UUID.randomUUID().toString();

	private String name;

	private String buyer;

	private Boolean sold = false;

	private List<String> memberNames = new ArrayList<>();

}
