package com.silence.regex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class TestMatchDot {

	@Test
	public void matchDotb(){
		String str = "abcdefghijk";
		Pattern pattern = Pattern.compile(".b");
		Matcher matcher = pattern.matcher(str);
		boolean matched = false;
		while(matcher.find()){
			matched = true;
			assertEquals(String.format("pattern: %s match group end is index %d ", ".b", matcher.end() ), "ab", matcher.group(0));
		}
		assertTrue(String.format( "pattern(%s) should match string (%s) ", ".b", str), matched);
	}

	@Test
	public void slashMatch(){
		String regex = "\\(";
		String str = "0123(";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		int index;
		boolean matched = false;
		while(matcher.find()){
			index = matcher.start();
			matched = true;
			String mstr = str.substring(index, index + 1);
			assertEquals(String.format("match string should be %s",  mstr), "(", mstr);
		}
		assertTrue(String.format( "pattern(%s) should match string (%s) ", regex, str), matched);
	}

	@Test
	public void matchTheBegin(){
		String regex = "^abc";
		String str = "abcd";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		int index;
		boolean matched = false;
		while(matcher.find()){
			index = matcher.start();
			matched = true;
			String mstr = str.substring(index, index + 1);
			assertEquals(String.format("match string should be %s",  mstr), "a", mstr);
			assertEquals(String.format("match string should be %s",  matcher.group(0)), "abc", matcher.group(0));
		}
		assertTrue(String.format( "pattern(%s) should match string (%s) ", regex, str), matched);
	}

	@Test
	public void matchTheTail(){
		String regex = "abc$";
		String str = "1abc";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		int index;
		boolean matched = false;
		while(matcher.find()){
			index = matcher.start();
			matched = true;
			String mstr = str.substring(index, index + 1);
			assertEquals(String.format("match string should be %s",  mstr), "a", mstr);
			assertEquals(String.format("match string should be %s",  matcher.group(0)), "abc", matcher.group(0));
		}
		assertTrue(String.format( "pattern(%s) should match string (%s) ", regex, str), matched);
	}

	@Test
	public void matchZeroToMultiTimes(){
		/**
		 * *号匹配 在*之前的字符出现大于等于0次
		 */
		String regex = ".*abc.*";
		String str = "01234abc89";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		int index;
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		boolean matched = false;
		while(matcher.find()){
			index = matcher.start();
			matched = true;
			String mstr = str.substring(index, index + 1);
			assertEquals(String.format("match string should be %s",  mstr), "0", mstr);
		}
		assertTrue(String.format( "pattern(%s) should match string (%s) ", regex, str), matched);
		str = "abc";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "abcoo";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "abcooa";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
	}

	@Test
	public void matchWithSubPattern(){
		/**
		 * 字符集也叫做字符类。 方括号用来指定一个字符集。 在方括号中使用连字符来指定字符集的范围。
		 */
		String regex = "[ab]*dd.*";
		String str = "aaaddbcv";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "aaddxyz";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "caddxyz";
		assertFalse(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "baddxyz";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
	}

	@Test
	public void matchSquareBrackets(){
		String regex = "[Tt]he";
		String str = "the";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "The";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
	}

	@Test
	public void matchTimesBetween(){
		String regex = "^a{3,5}d{2}bcv";
		String str = "aaaddbcv";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "addbcv";
		assertFalse(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "aaaaaaddbcv";
		assertFalse(String.format("pattern(%s) should not match string (%s) ", regex, str), Pattern.matches(regex, str));
	}
	

	@Test
	public void matchSquareBracketsDot(){
		// [.] 表示 .
		String regex = "[.]he";
		String str = ".he";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "ahe";
		assertFalse(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
	}

	@Test
	public void matchNot(){
		/**
		 * 一般来说 ^ 表示一个字符串的开头，但它用在一个方括号的开头的时候，它表示这个字符集是否定的。 
		 * 例如，表达式[^c]ar 匹配一个后面跟着ar的除了c的任意字符。
		 */
		String regex = "[^a]he";
		String str = ".he";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "ahe";
		assertFalse(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		regex = "[^abc]he";
		str = "bhe";
		assertFalse(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "che";
		assertFalse(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
	}

	@Test
	public void matchPlug(){
		/**
		 * +号匹配+号之前的字符出现 >=1 次
		 */
		String regex = "c+he";
		String str = "ccche";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "he";
		assertFalse(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
	}

	@Test
	public void matchQuestionMark(){
		/**
		 * 在正则表达式中元字符 ? 标记在符号前面的字符为可选，即出现 0 或 1 次
		 */
		String regex = "[T]?he";
		String str = "The";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "he";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "TThe";
		assertFalse(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
	}
	
	@Test
	public void matchBrace(){
		/**
		 * 在正则表达式中 {} 是一个量词，常用来限定一个或一组字符可以重复出现的次数。
		 * 例如， 表达式 [0-9]{2,3} 匹配最少 2 位最多 3 位 0~9 的数字。
		 */
		String regex = "[0-9]{1,4}he";
		String str = "9he";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "080he";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "0812he";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "he";
		assertFalse(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "12345he";
		assertFalse(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
	}
	
	@Test
	public void matchLowerBoundOfTimes(){
		/**
		 * 我们可以省略第二个参数。 例如，[0-9]{2,} 匹配至少两位 0~9 的数字。
		 */
		String regex = ".*a{2,}d{2}bcv";
		String str = "aaaddbcv";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "addbcv";
		assertFalse(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
	}

	@Test
	public void matchADefinedTime(){
		/**
		 * 如果逗号也省略掉则表示重复固定的次数。 例如，[0-9]{3} 匹配3位数字
		 */
		String regex = ".*a{2}d{2}bcv";
		String str = "aaaddbcv";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		regex = ".*ba{2}d{2}bcv";
		str = "abaaddbcv";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
	}

	@Test
	public void matchGroup(){
		/**
		 * 特征标群是一组写在 (...) 中的子模式。
		 * (...) 中包含的内容将会被看成一个整体
		 * |或运算符就表示或，用作判断条件。
		 */
		String regex = "(c|g|p)ar";
		String str = "car";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "par";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
	}

	@Test
	public void matchOr(){
		/**
		 * 或运算符就表示或，用作判断条件
		 */
		String regex = "(T|t)he|car";
		String str = "car";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "the";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
		str = "The";
		assertTrue(String.format("pattern(%s) should match string (%s) ", regex, str), Pattern.matches(regex, str));
	}

}
