package com.aonufrei;

import com.aonufrei.dto.FamilyTicketDto;
import com.aonufrei.dto.TicketDto;
import org.junit.jupiter.api.Test;

import java.util.*;
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

		assertEquals(new HashSet<>(expectedTickets), new HashSet<>(soldTickets));
	}

	/**
	 * map
	 */
	@Test
	public void testMap() {

		List<TicketDto> tickets = Arrays.asList(
				new TicketDto("Ticket 1", true),
				new TicketDto("Ticket 2"),
				new TicketDto("Ticket 3"),
				new TicketDto("Ticket 4", true),
				new TicketDto("Ticket 5", true)
		);

		Set<String> ticketNameList = tickets.stream().map(TicketDto::getName).collect(Collectors.toSet());

		Set<String> expectedTicketNameList = new HashSet<>();
		for (TicketDto t : tickets) {
			expectedTicketNameList.add(t.getName());
		}

		assertEquals(expectedTicketNameList, ticketNameList);
	}

	/**
	 * flatMap
	 */
	@Test
	public void testFlatMap() {

		List<FamilyTicketDto> tickets = Arrays.asList(
				FamilyTicketDto.builder().name("Ticket 1").memberNames(Arrays.asList("John", "Jennifer")).sold(true).build(),
				FamilyTicketDto.builder().name("Ticket 2").memberNames(Arrays.asList("Robert", "Mary")).sold(true).build(),
				FamilyTicketDto.builder().name("Ticket 3").memberNames(Arrays.asList("William", "Barbara", "Lisa")).sold(true).build(),
				FamilyTicketDto.builder().name("Ticket 4").memberNames(Arrays.asList("Donald", "Ashley")).sold(true).build(),
				FamilyTicketDto.builder().name("Ticket 5").memberNames(Collections.singletonList("Kevin")).sold(true).build()
		);

		List<String> members = tickets.stream()
				.map(FamilyTicketDto::getMemberNames) // map to Stream<List<String>> (list of members)
				.flatMap(Collection::stream)          // map to Stream<String>
				.collect(Collectors.toList());        // collect to List<String>

		Set<String> expectedMembers = new HashSet<>();
		for (FamilyTicketDto t : tickets) {
			expectedMembers.addAll(t.getMemberNames());
		}

		assertEquals(expectedMembers, new HashSet<>(members));
	}
}
