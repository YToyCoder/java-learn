package com.silence.stream;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.collections.api.bag.primitive.CharBag;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.tuple.primitive.CharIntPair;
import org.eclipse.collections.impl.factory.Strings;
import org.junit.Test;

public class StringStreamTest {
  private final String str = "123456abccdid";

  @Test
  public void charsTest(){
    String distinctLetters = Strings.asChars(str)
      .select(Character::isAlphabetic)
      .distinct()
      .toString();
    // distinctLetters
    assertEquals("abcdi", distinctLetters);
  }

  @Test
  public void charsTest2() {
    String distinctLetters = str.chars()
    .filter(Character::isAlphabetic)
    .map(Character::toLowerCase)
    .distinct()
    .mapToObj(Character::toString)
    .collect(Collectors.joining());
    assertEquals("abcdi", distinctLetters);
  }

  @Test
  public void top_letters_EC() {
    CharBag chars= Strings.asChars(str)
    .select(Character::isAlphabetic)
    .collectChar(Character::toLowerCase)
    .toBag();
    ListIterable<CharIntPair> top3 = chars.topOccurrences(3);
    top3.forEach(System.out::println);
  }

  @Test
  public void top_letters_JS_V1() {
    Map<String, Long> map = str.chars()
    .filter(Character::isAlphabetic)
    .map(Character::toLowerCase)
    .mapToObj(Character::toString)
    .collect(Collectors.groupingBy(
      Function.identity(),
      Collectors.counting()
    ));

    final List<Map.Entry<String,Long>> mostFrequentLetters = 
    map.entrySet().stream()
    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
    .limit(3)
    .toList();

    final Map<Long, List<String>> mfls = 
    map.entrySet().stream()
      .collect(Collectors.groupingBy(
        Map.Entry::getValue,
        Collectors.mapping(
          Map.Entry::getKey, 
          Collectors.toList()
          )
        )
      );
  }
}
