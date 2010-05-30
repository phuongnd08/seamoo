package org.seamoo.utils;

import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

public class ListBuilderTest {
	public static class Wow {
		private String text;

		public void setText(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "Wow@" + text;
		}

		public Wow(String text) {
			this.text = text;
		}
	}

	@Test
	public void extractListFromMapShouldReturnItemsFollowIndexOrder() {
		Map<Long, Object> map = new HashMap<Long, Object>();
		Object o1 = new Wow("x1"), o2 = new Wow("x2"), o3 = new Wow("x3");
		map.put(1L, o1);
		map.put(2L, o2);
		map.put(3L, o3);
		assertEquals(ListBuilder.extractList(map, Arrays.asList(1L, 3L, 2L)).toArray(), new Object[] { o1, o3, o2 });
	}
}
