import static org.junit.Assert.*;

import org.junit.Test;

public class MatchingTest {

	@Test
	public void test01() {
		String a = "abc";
		String b = "abc";
		Matcher matcher = new Matcher();
		Boolean result = matcher.matching(a, b);

		assertEquals(true, result);

		System.out.println("同じ文字列");
		System.out.println("test1," + a + "," + b + "," + result);
	}

	@Test
	public void test02() {
		String a = "abc";
		String b = "efg";
		Matcher matcher = new Matcher();
		Boolean result = matcher.matching(a, b);

		assertEquals(false, result);

		System.out.println("異なる文字列");
		System.out.println("test2," + a + "," + b + "," + result);

	}

	@Test
	public void test03() {
		String a = "?X";
		String b = "abc";
		Matcher matcher = new Matcher();
		Boolean result = matcher.matching(a, b);

		assertEquals(true, result);

		System.out.println("最初が変数");
		System.out.println("test3," + a + "," + b + "," + result);

	}

	@Test
	public void test04() {
		String a = "abc";
		String b = "?Y";
		Matcher matcher = new Matcher();
		Boolean result = matcher.matching(a, b);

		assertEquals(true, result);

		System.out.println("二つ目が変数");
		System.out.println("test4," + a + "," + b + "," + result);

	}

	@Test
	public void test05() {
		String a = "?X";
		String b = "?Y";
		Matcher matcher = new Matcher();
		Boolean result = matcher.matching(a, b);

		assertEquals(false, result);

		System.out.println("両方とも変数");
		System.out.println("test5," + a + "," + b + "," + result);

	}

	@Test
	public void test06() {
		String a = "abc efg 123";
		String b = "abc efg 123";
		Matcher matcher = new Matcher();
		Boolean result = matcher.matching(a, b);

		assertEquals(true, result);

		System.out.println("複数_同じ文字列");
		System.out.println("test6," + a + "," + b + "," + result);

	}

	@Test
	public void test07() {
		String a = "abc efg 123";
		String b = "abc efg 456";
		Matcher matcher = new Matcher();
		Boolean result = matcher.matching(a, b);

		assertEquals(false, result);

		System.out.println("複数_最後のトークン異なる文字列");
		System.out.println("test7," + a + "," + b + "," + result);

	}

	@Test
	public void test08() {
		String a = "abc efg ?X";
		String b = "?X efg abc";
		Matcher matcher = new Matcher();
		Boolean result = matcher.matching(a, b);

		assertEquals(true, result);

		System.out.println("複数_変数と定数が混在１");
		System.out.println("test8," + a + "," + b + "," + result);

	}

	@Test
	public void test09() {
		String a = "abc efg ?X";
		String b = "?X efg 123";
		Matcher matcher = new Matcher();
		Boolean result = matcher.matching(a, b);

		// ?Xもうabcと束縛したので、?Xと123マッチングすると、falseをリターンする
		assertEquals(false, result);

		System.out.println("複数_変数と定数が混在２");
		System.out.println("test9," + a + "," + b + "," + result);

	}

	@Test
	public void test10() {
		String a = "abc efg ?Y";
		String b = "?X efg abc";
		Matcher matcher = new Matcher();
		Boolean result = matcher.matching(a, b);

		assertEquals(true, result);

		System.out.println("複数_変数と定数が混在３");
		System.out.println("test10," + a + "," + b + "," + result);

	}

	/*
	 * Unifyのケース
	 * 想定の戻り値がtrueですが
	 * Matchingのロジックで
	 * falseを返す
	 */
	@Test
	public void test11() {
		String a = "?X ?Y";
		String b = "?Y bye";
		Matcher matcher = new Matcher();
		Boolean result = matcher.matching(a, b);

		assertEquals(false, result);

		System.out.println("Unifyのケース");
		System.out.println("test11," + a + "," + b + "," + result);

	}

}
