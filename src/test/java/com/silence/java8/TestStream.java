package com.silence.java8;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.silence.java8.stream.Person;

import org.junit.Test;

public class TestStream {
	public List<String> strList(String... strs) {
		List<String> res = new ArrayList<>(strs.length);
		for (String str : strs)
			res.add(str);
		return res;
	}

	public List<Integer> intList(int... ints) {
		List<Integer> res = new ArrayList<>(ints.length);
		for (int i : ints)
			res.add(i);
		return res;
	}

	public List<String> defaultStr() {
		return strList("tom", "tony", "wan", "eva", "color");
	}

	public List<Integer> defaultInt() {
		return intList(1, 3, 10, 4, 0, 3, -1, 40, 20, 1000, -4, -20);
	}

	@Test
	public void testAnyMatch() {
		List<String> strs = defaultStr();
		boolean match = strs.stream().anyMatch((each) -> Objects.equals(each, "tom"));
		assertTrue(match);
		match = strs.stream().anyMatch((each) -> Objects.equals(each, "wan"));
		assertTrue(match);
		match = strs.stream().anyMatch((each) -> Objects.equals(each, "not"));
		assertFalse(match);
	}

	@Test
	public void testAllMatch() {
		List<String> strs = defaultStr();
		boolean match = strs.stream().allMatch((each) -> Objects.equals(each, "tom"));
		assertFalse(match);
		strs = strList("ww", "ww", "ww");
		match = strs.stream().allMatch((each) -> Objects.equals(each, "ww"));
		assertTrue(match);
	}

	@Test
	public void testConcat() {
		List<String> strs = defaultStr();
		Stream<String> stream = strs.stream();
		strs = strList("ww", "ww", "ww");
		Stream<String> stream1 = strs.stream();
		List<String> ls = Stream.concat(stream, stream1).collect(Collectors.toList());
		assertArrayEquals(ls.toArray(), List.of("tom", "tony", "wan", "eva", "color", "ww", "ww", "ww").toArray());
	}

	@Test
	public void testDistinct() {
		List<String> strs = defaultStr();
		strs.add("tom");
		strs.add("tony");
		Object[] ss = strs.stream().distinct().collect(Collectors.toList()).toArray();
		assertArrayEquals(ss, defaultStr().toArray());
	}

	@Test
	public void testCount() {
		Stream<Integer> stream = defaultInt().stream();
		long count = stream.count();
		assertEquals(12, count);
		long sum = defaultInt().stream().reduce(0, (a, b) -> a + b);
		assertEquals(sum, 1 + 3 + 10 + 4 + 3 - 1 + 40 + 20 + 1000 - 4 - 20);
	}

	@Test
	public void testReuse() {
		List<Integer> source = defaultInt();
		Supplier<Stream<Integer>> streamSupplier = () -> source.stream().filter(i -> i > 0);
		boolean moreThan3 = streamSupplier.get().anyMatch(any -> any > 3);
		assertTrue("> 3", moreThan3);
		boolean moreThan50 = streamSupplier.get().anyMatch(any -> any > 50);
		assertTrue("> 50", moreThan50);
	}

	@Test
	public void testGroupBy() {
		List<Person> people = List.of(new Person("tom", 10), new Person("tony", 10),
				new Person("cat", 11), new Person("lono", 11),
				new Person("fly", 1), new Person("rich", 1));
		Supplier<Stream<Person>> supplier = () -> people.stream();
		Map<Integer, List<Person>> ageGroup = supplier.get().collect(Collectors.groupingBy(each -> each.age));
		ageGroup.entrySet().stream().map(entry -> {
			return String.format("age %d : %s", entry.getKey(), entry.getValue());
		}).forEach(each -> System.out.println(each));
	}

	@Test
	public void testAveraingInt() {
		List<Person> people = List.of(
				new Person("tom", 10), new Person("tony", 10),
				new Person("cat", 11), new Person("lono", 11),
				new Person("fly", 1), new Person("rich", 1));
		double averageAge = people.stream()
				.collect(Collectors.averagingInt(each -> each.age));
		assertEquals(22 / 3.0, averageAge, 0.01);
	}

	@Test
	public void testJoining() {
		List<Person> people = List.of(
				new Person("tom", 10), new Person("tony", 10),
				new Person("cat", 11), new Person("lono", 11),
				new Person("fly", 1), new Person("rich", 1));
		String str = people.stream()
				.map(person -> person.name)
				.collect(Collectors.joining(",", "prefix -> ", " <- tail"));
		System.out.println(str);
	}

}
