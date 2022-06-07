package com.aonufrei;

import com.aonufrei.dto.TicketDto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class StreamMethodsTest {

	/**
	 * forEach method is used to iterate through the elements of the stream and perform some operation. It is a terminal
	 * method, so you cannot use stream after it.
	 */
	@Test
	public void testForEach() {
		List<TicketDto> tickets = Arrays.asList(
				new TicketDto("Ticket 1"),
				new TicketDto("Ticket 2"),
				new TicketDto("Ticket 3"),
				new TicketDto("Ticket 4"),
				new TicketDto("Ticket 5")
		);
		tickets.forEach(it -> it.setSold(true));

		for (TicketDto t : tickets) {
			assertTrue(t.getSold());
		}
	}

	/**
	 * filter
	 */
	@Test
	public void testFilter() {
		List<TicketDto> tickets = Arrays.asList(
				new TicketDto("Ticket 1", true),
				new TicketDto("Ticket 2"),
				new TicketDto("Ticket 3"),
				new TicketDto("Ticket 4", true),
				new TicketDto("Ticket 5", true)
		);

		tickets.stream().filter(TicketDto::getSold).forEach(t -> t.setBuyer("Buyer 1"));

		for (TicketDto t : tickets) {
			if (t.getSold()) {
				assertEquals("Buyer 1", t.getBuyer());
				continue;
			}
			assertNull(t.getBuyer());
		}
	}

	/**
	 * collect
	 */
	@Test
	public void testSimpleCollect() {
		List<TicketDto> tickets = Arrays.asList(
				new TicketDto("Ticket 1", true),
				new TicketDto("Ticket 2"),
				new TicketDto("Ticket 3"),
				new TicketDto("Ticket 4", true),
				new TicketDto("Ticket 5", true)
		);

		List<TicketDto> soldTickets = tickets.stream().filter(TicketDto::getSold).collect(Collectors.toList());

		List<TicketDto> expectedTickets = new ArrayList<>();
		for (TicketDto t : tickets) {
			if (t.getSold()) {
				expectedTickets.add(t);
			}
		}

		assertEquals(new HashSet<>(soldTickets), new HashSet<>(expectedTickets));
	}

	/**
	 * map
	 */
	@Test
	public void testMap() {

	}

}
