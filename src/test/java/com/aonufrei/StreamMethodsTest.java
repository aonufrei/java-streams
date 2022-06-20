package com.aonufrei;

import com.aonufrei.dto.FamilyTicketDto;
import com.aonufrei.dto.TicketDto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


public class StreamMethodsTest {

	/**
	 * forEach method is used to iterate through the elements of the stream and perform some operation. It is a terminal
	 * method, so you cannot use stream methods after it.
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
	 * filter method is used to select the elements of some specific characteristic. You need to pass a Predicate
	 * that returns true for elements that are required
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
	 * collect is used to convert the processing stream into the end List, Map e.t.c
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
	 * map method is used to convert elements of the stream into other objects.
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
	 * mapToInt, mapToFloat, mapToDouble methods work the same way as map does, but is used for int, float and
	 * double values only. The performance of using these methods is much higher that using regular map method.
	 */
	@Test
	public void testOtherMaps() {

		List<FamilyTicketDto> tickets = Arrays.asList(
				FamilyTicketDto.builder().name("Ticket 1").memberNames(Arrays.asList("John", "Jennifer")).sold(true).build(),
				FamilyTicketDto.builder().name("Ticket 2").memberNames(Arrays.asList("Robert", "Mary")).sold(true).build(),
				FamilyTicketDto.builder().name("Ticket 3").memberNames(Arrays.asList("William", "Barbara", "Lisa")).sold(true).build(),
				FamilyTicketDto.builder().name("Ticket 4").memberNames(Arrays.asList("Donald", "Ashley")).sold(true).build(),
				FamilyTicketDto.builder().name("Ticket 5").memberNames(Collections.singletonList("Kevin")).sold(true).build()
		);

		int sumOfMembers = tickets.stream().mapToInt(it -> it.getMemberNames().size()).sum();
		int expectedSum = 0;
		for (FamilyTicketDto t : tickets) {
			expectedSum += t.getMemberNames().size();
		}

		assertEquals(expectedSum, sumOfMembers);

		List<Float> floats = Arrays.asList(1.2f, 1.4f, 1.5f, 1.7f);
		double sumOfFloats = floats.stream().mapToDouble(Float::doubleValue).sum();
		double expectedSumOfFloats = 0;
		for (Float f : floats) {
			expectedSumOfFloats += f.doubleValue();
		}

		assertEquals(expectedSumOfFloats, sumOfFloats);

		List<Integer> integers = Arrays.asList(1, 2, 3, 4);
		double sumOfLongs = integers.stream().mapToLong(Integer::longValue).sum();
		double expectedSumOfLongs = 0;
		for (Integer i : integers) {
			expectedSumOfLongs += i.longValue();
		}

		assertEquals(expectedSumOfLongs, sumOfLongs);
	}

	/**
	 * flatMap method is used to union streams in the objects into general stream. The Function that returns Stream
	 * is required.
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

	/**
	 * peek method is similar to forEach, but is not a terminal method. That means you can use other stream methods
	 * after it
	 */
	@Test
	public void testPeek() {

		List<TicketDto> tickets = Arrays.asList(
				new TicketDto("Ticket 1", true),
				new TicketDto("Ticket 2"),
				new TicketDto("Ticket 3"),
				new TicketDto("Ticket 4", true),
				new TicketDto("Ticket 5", true)
		);

		long actualCount = tickets.stream()
				.map(TicketDto::copy)
				.peek(it -> it.setSold(!it.getSold()))
				.filter(TicketDto::getSold).count();

		List<TicketDto> result = new ArrayList<>();
		for (TicketDto t : tickets) {
			TicketDto tc = t.copy();
			tc.setSold(!tc.getSold());
			if (tc.getSold()) {
				result.add(tc);
			}
		}
		long expectedCount = result.size();

		assertEquals(expectedCount, actualCount);
	}

	/**
	 * toArray method is used to collect the elements of the stream into the primitive array
	 */
	@Test
	public void testToArray() {

		List<TicketDto> tickets = Arrays.asList(
				new TicketDto("Ticket 1", true),
				new TicketDto("Ticket 2"),
				new TicketDto("Ticket 3"),
				new TicketDto("Ticket 4", true),
				new TicketDto("Ticket 5", true)
		);

		TicketDto[] ticketsPrimitiveArray = tickets.stream().filter(TicketDto::getSold).toArray(TicketDto[]::new);
		int soldTicketsNumber = 0;
		for (TicketDto t : tickets) {
			if (t.getSold()) {
				soldTicketsNumber++;
			}
		}
		TicketDto[] expectedArray = new TicketDto[soldTicketsNumber];
		int p = 0;
		for (TicketDto t : tickets) {
			if (t.getSold()) {
				expectedArray[p] = t;
				p++;
			}
		}

		assertEquals(expectedArray.length, ticketsPrimitiveArray.length);
		assertEquals(new HashSet<>(Arrays.asList(expectedArray)), new HashSet<>(Arrays.asList(ticketsPrimitiveArray)));
	}

	/**
	 * reduce method is used to combine elements into one summarizing result
	 */
	@Test
	public void testReduce() {
		List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

		// first element is accumulator - the result at the beginning of calculation
		Integer summarizingResult1 = nums.stream().reduce(0, Integer::sum);
		// the same operation as before, but instead without accumulator. In this case Option<> is returned
		Integer summarizingResult2 = nums.stream().reduce(Integer::sum).orElse(0);

		Integer expectedResult = 0;
		for (Integer n : nums) {
			expectedResult += n;
		}
		assertEquals(expectedResult, summarizingResult1);
		assertEquals(expectedResult, summarizingResult2);
	}

	/**
	 * sorted method is used to sort the stream elements by the provided comparator
	 */
	@Test
	public void testSorted() {
		List<Integer> nums = Arrays.asList(9, 2, 4, 1, 6, 7, 3);

		List<Integer> sortedNums = nums.stream().sorted(Integer::compareTo).collect(Collectors.toList());
		for (int i = 0; i < sortedNums.size() - 1; i++) {
			assertTrue(sortedNums.get(i) <= sortedNums.get(i + 1));
		}
	}

	/**
	 * distinct method is used to get unique elements only
	 */
	@Test
	public void testDistinct() {
		List<Integer> nums = Arrays.asList(1, 1, 1, 2, 2, 2, 3, 3, 3);

		List<Integer> uniqueNums = nums.stream().distinct().collect(Collectors.toList());
		List<Integer> expectedNums = new ArrayList<>(new HashSet<>(nums));
		assertEquals(expectedNums, uniqueNums);
	}

	/**
	 * skip and limit methods are used to ignore some number of elements from the beginning or at the end respectfully
	 */
	@Test
	public void testSkipAndLimit() {
		List<Integer> nums = Arrays.asList(1, 1, 1, 2, 2, 2, 3, 3, 3);

		List<Integer> nNums = nums.stream()
				.skip(4) // skip first 4 elements
				.limit(2) // select 2 elements only
				.collect(Collectors.toList());

		List<Integer> expected = new ArrayList<>();
		for (int i = 0; i < nums.size(); i++) {
			if (i < 4) {
				continue;
			}
			if (i > 5) {
				break;
			}
			expected.add(nums.get(i));
		}

		assertEquals(expected, nNums);
		assertEquals(nNums.size(), 2);
	}

	/**
	 * count method is terminal and is used to get the number of elements in the list.
	 */
	@Test
	public void testCount() {
		List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6);

		long calculatedNumberOfElements = nums.stream().filter(it -> it > 2).count();

		long expected = 0;
		for (Integer n : nums) {
			if (n > 2) {
				expected++;
			}
		}

		assertEquals(expected, calculatedNumberOfElements);
	}

	/**
	 * min and max methods are used to get minimum value and maximum value respectfully. Returns the Optional<>
	 */
	@Test
	public void testMinAndMax() {
		List<TicketDto> tickets = Arrays.asList(
				TicketDto.builder().name("Ticket 1").price(2).build(),
				TicketDto.builder().name("Ticket 2").price(5).build(),
				TicketDto.builder().name("Ticket 3").price(9).build(),
				TicketDto.builder().name("Ticket 4").price(4).build()
		);

		TicketDto maxPriceTicket = tickets.stream().max(Comparator.comparing(TicketDto::getPrice)).orElse(null);
		TicketDto minPriceTicket = tickets.stream().min(Comparator.comparing(TicketDto::getPrice)).orElse(null);

		TicketDto expectedMaxPriceTicket = null;
		for (TicketDto t : tickets) {
			if (expectedMaxPriceTicket == null) {
				expectedMaxPriceTicket = t;
				continue;
			}
			if (t.getPrice() > expectedMaxPriceTicket.getPrice()) {
				expectedMaxPriceTicket = t;
			}
		}
		TicketDto expectedMinPriceTicket = null;
		for (TicketDto t : tickets) {
			if (expectedMinPriceTicket == null) {
				expectedMinPriceTicket = t;
				continue;
			}
			if (t.getPrice() < expectedMinPriceTicket.getPrice()) {
				expectedMinPriceTicket = t;
			}
		}

		assertEquals(expectedMaxPriceTicket, maxPriceTicket);
		assertEquals(expectedMinPriceTicket, minPriceTicket);
	}

	/**
	 * sum, average, and range methods are used on numbers calculations. These methods are applied only on IntStream,
	 * LongStream, or DoubleStream objects. You need to convert regular stream into appropriate stream in order to use them.
	 * It can be performed by using mapToInt, mapToLong, or mapToDouble
	 */
	@Test
	public void testSumAverageAndRange() {
		// range is used with IntStream to generate numbers ranging from 0 to 9
		// boxed is used to convert IntStream to Stream<Integer>
		List<Integer> nums = IntStream.range(0, 10).boxed().collect(Collectors.toList());
		for (int i = 0; i < nums.size(); i++) {
			assertEquals(i, nums.get(i));
		}

		// sum is used to find the sum of elements
		int sum = nums.stream().mapToInt(it -> it).sum();
		int expectedSum = 0;
		for (Integer n : nums) {
			expectedSum += n;
		}
		assertEquals(expectedSum, sum);

		// average is used to find double average value
		double average = nums.stream().mapToInt(it -> it).average().orElse(0);
		double expectedAverage = 0;
		for (Integer n : nums) {
			expectedAverage += n;
		}
		expectedAverage /= nums.size();
		assertEquals(expectedAverage, average);
	}

	/**
	 * allMatch, anyMatch, and noneMatch methods are used to check if there is an elements with required criteria
	 * in the stream. Returns respectful boolean
	 */
	@Test
	public void testMatch() {
		List<Integer> nums = Arrays.asList(1, 2, 3);

		assertFalse(nums.stream().allMatch(it -> it == 1)); // all elements in the stream are not 1
		assertTrue(nums.stream().anyMatch(it -> it == 1)); // there is at least one element in the stream that equals to 1
		assertTrue(nums.stream().noneMatch(it -> it == 10)); // 10 is not presented in the stream
	}
}
