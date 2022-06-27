package com.aonufrei;

import com.aonufrei.dto.Ticket;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AdvancedCollectMethodsTest {

	/**
	 * collect method is used to combine elements of the stream in a single object
	 */
	@Test
	public void testBasicCollectInterface() {
		List<String> words = Arrays.asList("I", "am", "Java", "Developer");

		StringBuilder actual1 = words.stream().collect(StringBuilder::new,              // object to collect in
				StringBuilder::append,                                                  // how to add item to the object
				(subresult1, subresult2) -> subresult1.append(" ").append(subresult2)); // how to union two subresults

		// stream is not parallel, so there is no subresults and third lambda is not used at all
		assertEquals("IamJavaDeveloper", actual1.toString());

		StringBuilder actual2 = words.parallelStream().collect(
				StringBuilder::new,
				StringBuilder::append,
				(subresult1, subresult2) -> subresult1.append(",").append(subresult2));

		// the stream is parallel, so there are subresults and third lambda is used.
		// As the amount of elements in the stream is not big, all words where placed in separate subresults
		assertEquals("I,am,Java,Developer", actual2.toString());

	}

	/**
	 * joining method is used to sum multiple string
	 */
	@Test
	public void testJoins() {
		List<String> words = Arrays.asList("I", "am", "Java", "Developer");

		// Example 1 : Join strings
		String actual = words.stream().map(String::toUpperCase).collect(Collectors.joining());
		assertEquals("IAMJAVADEVELOPER", actual);

		// Example 2 : Join strings with delimiter
		actual = words.stream().map(String::toUpperCase).collect(Collectors.joining(" "));
		assertEquals("I AM JAVA DEVELOPER", actual);

		// Example 3 : Join strings with delimiter, prefix and suffix
		actual = words.stream().map(String::toUpperCase).collect(Collectors.joining(" ", "[", "]"));
		assertEquals("[I AM JAVA DEVELOPER]", actual);
	}

	/**
	 * toSet and toList is used to collect all elements of the stream in a Set or in a List accordingly
	 */
	@Test
	public void testToSetAndToList() {
		List<Integer> nums = Arrays.asList(1, 1, 2, 2, 3, 4, 5, 5);

		// toSet converts stream to HashSet
		Set<Integer> setOfNums = nums.stream().filter(n -> n > 1).collect(Collectors.toSet());
		// toList converts stream to ArrayList
		List<Integer> listOfNums = nums.stream().filter(n -> n > 1).collect(Collectors.toList());

		Set<Integer> expectedSet = new HashSet<>();
		List<Integer> expectedList = new ArrayList<>();
		for (Integer n : nums) {
			if (n <= 1) continue;
			expectedSet.add(n);
			expectedList.add(n);
		}

		assertEquals(expectedSet, setOfNums);
		assertEquals(expectedList, listOfNums);
	}

	/**
	 * toCollection is used to collect elements of the stream in a specified Set or List implementations
	 */
	@Test
	public void testToCollection() {
		List<Integer> nums = Arrays.asList(1, 1, 2, 2, 3, 4, 5, 5);

		LinkedList<Integer> linkedListOfNums = nums.stream().filter(n -> n > 1).collect(Collectors.toCollection(LinkedList::new));
		assertInstanceOf(LinkedList.class, linkedListOfNums);
	}

	/**
	 * toMap is used to collect elements of the stream in a Map
	 */
	@Test
	public void testToMap() {
		List<Ticket> tickets = Arrays.asList(
				Ticket.builder().name("Ticket 1").price(1000).build(),
				Ticket.builder().name("Ticket 2").price(2000).build(),
				Ticket.builder().name("Ticket 3").price(1000).build(),
				Ticket.builder().name("Ticket 4").price(3000).build(),
				Ticket.builder().name("Ticket 5").price(2000).build()
		);

		Map<Integer, List<Ticket>> actual = tickets.stream().collect(
				Collectors.toMap(Ticket::getPrice,                                                // what to pick as a key for map
				Collections::singletonList,                                                          // what to pick as a value for a map
				(f, s) -> Stream.of(f, s).flatMap(Collection::stream).collect(Collectors.toList())));// what to do with the value when there are two same keys

		Map<Integer, List<Ticket>> expected = new HashMap<Integer, List<Ticket>>() {{
			put(1000, tickets.stream().filter(it -> it.getPrice() == 1000).collect(Collectors.toList()));
			put(2000, tickets.stream().filter(it -> it.getPrice() == 2000).collect(Collectors.toList()));
			put(3000, tickets.stream().filter(it -> it.getPrice() == 3000).collect(Collectors.toList()));
		}};

		assertEquals(expected, actual);
	}

	/**
	 * summingInt, summingLong, and summingDouble can be used to sum int, long, or double elements of the stream.
	 */
	@Test
	public void testSumming() {
		List<Integer> ints = Arrays.asList(1, 2, 3, 4);
		List<Long> longs = Arrays.asList(1L, 2L, 3L, 4L);
		List<Double> doubles = Arrays.asList(1.0, 2.0, 3.0, 4.0);

		Integer intSum = ints.stream().collect(Collectors.summingInt(it -> it));
		Long longSum = longs.stream().collect(Collectors.summingLong(it -> it));
		Double doubleSum = doubles.stream().collect(Collectors.summingDouble(it -> it));

		assertEquals(10, intSum);
		assertEquals(10, longSum);
		assertEquals(10, doubleSum);
	}

	/**
	 * summarizingInt, summarizingLong, and summarizingDouble can be used to get statistics by data from the stream.
	 * As a result, you get the IntSummaryStatistics, LongSummaryStatistics, or DoubleSummaryStatistics that contains
	 * max, min, sum, and count values of the provided data
	 */
	@Test
	public void testSummarizing() {
		List<Integer> ints = Arrays.asList(1, 2, 3, 4);
		List<Long> longs = Arrays.asList(1L, 2L, 3L, 4L);
		List<Double> doubles = Arrays.asList(1.0, 2.0, 3.0, 4.0);

		IntSummaryStatistics intSummaryStatistics = ints.stream().collect(Collectors.summarizingInt(it -> it));
		LongSummaryStatistics longSummaryStatistics = longs.stream().collect(Collectors.summarizingLong(it -> it));
		DoubleSummaryStatistics doubleSummaryStatistics = doubles.stream().collect(Collectors.summarizingDouble(it -> it));

		assertEquals(4, intSummaryStatistics.getMax());
		assertEquals(4L, longSummaryStatistics.getMax());
		assertEquals(4.0, doubleSummaryStatistics.getMax());

		assertEquals(1, intSummaryStatistics.getMin());
		assertEquals(1L, longSummaryStatistics.getMin());
		assertEquals(1.0, doubleSummaryStatistics.getMin());

		assertEquals(4, intSummaryStatistics.getCount());
		assertEquals(4, longSummaryStatistics.getCount());
		assertEquals(4, doubleSummaryStatistics.getCount());

		assertEquals(10, intSummaryStatistics.getSum());
		assertEquals(10, longSummaryStatistics.getSum());
		assertEquals(10, doubleSummaryStatistics.getSum());
	}

}
