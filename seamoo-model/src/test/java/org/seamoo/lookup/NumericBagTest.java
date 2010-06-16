package org.seamoo.lookup;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NumericBagTest {

	NumericBag numericBag;

	@BeforeMethod
	public void setUp() {
		numericBag = new NumericBag();
	}

	@Test
	public void addShouldIncreaseTheSizeOfBag() {
		numericBag.addNumber(1L);
		assertEquals(numericBag.getSize(), 1);
		numericBag.addNumber(2L);
		assertEquals(numericBag.getSize(), 2);
		numericBag.addNumber(4L);
		assertEquals(numericBag.getSize(), 3);
		numericBag.addNumber(3L);
		assertEquals(numericBag.getSize(), 4);
	}

	void assertBagCollections(NumericBag bag, String name, Object[] values) {
		ArrayList<Long> list = (ArrayList<Long>) ReflectionTestUtils.getField(bag, name);
		assertEquals(list.toArray(), values);
	}

	void assertAllBagCollections(NumericBag bag, Object[] heads, Object[] tails, Object[] indices) {
		assertBagCollections(bag, "heads", heads);
		assertBagCollections(bag, "tails", tails);
		assertBagCollections(bag, "indices", indices);
	}

	@Test
	public void addConnectiveElementMergeSegments() {
		numericBag.addNumber(1L);
		assertAllBagCollections(numericBag, new Object[] { 1L }, new Object[] { 1L }, new Object[] { 0L });
		numericBag.addNumber(2L);
		assertAllBagCollections(numericBag, new Object[] { 1L }, new Object[] { 2L }, new Object[] { 0L });
		numericBag.addNumber(4L);
		assertAllBagCollections(numericBag, new Object[] { 1L, 4L }, new Object[] { 2L, 4L }, new Object[] { 0L, 2L });
		numericBag.addNumber(3L);
		assertAllBagCollections(numericBag, new Object[] { 1L }, new Object[] { 4L }, new Object[] { 0L });
	}

	@Test
	public void removeConnectiveElementSplitSegments() {
		numericBag.addNumber(1L);
		numericBag.addNumber(2L);
		numericBag.addNumber(3L);
		numericBag.addNumber(4L);
		numericBag.removeNumber(3L);
		assertAllBagCollections(numericBag, new Object[] { 1L, 4L }, new Object[] { 2L, 4L }, new Object[] { 0L, 2L });
		numericBag.removeNumber(2L);
		assertAllBagCollections(numericBag, new Object[] { 1L, 4L }, new Object[] { 1L, 4L }, new Object[] { 0L, 1L });
		numericBag.removeNumber(1L);
		assertAllBagCollections(numericBag, new Object[] { 4L }, new Object[] { 4L }, new Object[] { 0L });
		numericBag.removeNumber(4L);
		assertAllBagCollections(numericBag, new Object[] {}, new Object[] {}, new Object[] {});
	}

	@Test
	public void addDuplicateShouldNotIncreaseTheSizeOfBag() {
		numericBag.addNumber(1L);
		numericBag.addNumber(2L);
		numericBag.addNumber(2L);
		assertEquals(numericBag.getSize(), 2);
		numericBag.addNumber(3L);
		numericBag.addNumber(2L);
		assertEquals(numericBag.getSize(), 3);
	}

	@Test
	public void removeShouldDecreaseTheSizeOfBag() {
		numericBag.addNumber(1L);
		numericBag.addNumber(2L);
		numericBag.addNumber(4L);
		numericBag.addNumber(3L);
		numericBag.removeNumber(3L);
		assertEquals(numericBag.getSize(), 3);
		numericBag.removeNumber(2L);
		assertEquals(numericBag.getSize(), 2);
		numericBag.removeNumber(1L);
		assertEquals(numericBag.getSize(), 1);
		numericBag.removeNumber(4L);
		assertEquals(numericBag.getSize(), 0);
	}

	@Test
	public void removeNonExistentShouldNotDecreaseTheSizeOfBag() {
		numericBag.addNumber(1L);
		numericBag.addNumber(2L);
		numericBag.addNumber(4L);
		numericBag.addNumber(3L);
		numericBag.removeNumber(5L);
		assertEquals(numericBag.getSize(), 4);
	}

	@Test
	public void removeElementFromEmptyBagShouldBeOk() {
		numericBag.removeNumber(3L);
	}

	
	@Test
	public void removeElementThatSmallerThanAnyElementInBagShouldBeOk() {
		numericBag.addNumber(11L);
		numericBag.removeNumber(9L);
	}

	@Test
	public void addRemoveAlternativelyShouldMaintainTheBag() {
		numericBag.addNumber(1L);
		assertEquals(numericBag.toArray(), new Object[] { 1L });
		numericBag.addNumber(2L);
		assertEquals(numericBag.toArray(), new Object[] { 1L, 2L });
		numericBag.addNumber(4L);
		assertEquals(numericBag.toArray(), new Object[] { 1L, 2L, 4L });
		numericBag.addNumber(3L);
		assertEquals(numericBag.toArray(), new Object[] { 1L, 2L, 3L, 4L });
		numericBag.removeNumber(3L);
		assertEquals(numericBag.toArray(), new Object[] { 1L, 2L, 4L });
		numericBag.removeNumber(2L);
		assertEquals(numericBag.toArray(), new Object[] { 1L, 4L });
		numericBag.removeNumber(1L);
		assertEquals(numericBag.toArray(), new Object[] { 4L });
		numericBag.removeNumber(4L);
		assertEquals(numericBag.toArray(), new Object[] {});
	}

	
}
