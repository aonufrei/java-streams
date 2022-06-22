package com.aonufrei;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class AdvancedCollectMethodsTest {

	@Test
	public void testCollect() {
		List<String> words = Arrays.asList("I", "am", "Java", "Developer");

		String sentence = words.stream().map(String::toUpperCase).collect(Collectors.joining(" "));

		StringBuilder expectedSentenceBuilder = new StringBuilder();
		for (String word: words) {
			expectedSentenceBuilder.append(" ").append(word.toUpperCase());
		}
		String expectedSentence = expectedSentenceBuilder.substring(1);

		assertEquals(expectedSentence, sentence);
	}

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

	@Test
	public void testToCollection() {
		List<Integer> nums = Arrays.asList(1, 1, 2, 2, 3, 4, 5, 5);

		LinkedList<Integer> linkedListOfNums = nums.stream().filter(n -> n > 1).collect(Collectors.toCollection(LinkedList::new));
		assertInstanceOf(LinkedList.class, linkedListOfNums);
	}

}
