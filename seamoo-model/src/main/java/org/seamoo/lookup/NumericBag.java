package org.seamoo.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import com.google.appengine.api.datastore.Blob;
import com.vercer.engine.persist.annotation.Key;
import com.vercer.engine.persist.annotation.Type;

public class NumericBag {
	@Id
	@Key
	private Long autoId;
	/**
	 * A string used to classify different type of bag, oftenly the class name of entity with an extra number
	 */

	private String classifier;

	@Type(Blob.class)
	private List<Long> heads = new ArrayList<Long>();

	@Type(Blob.class)
	private List<Long> tails = new ArrayList<Long>();

	/**
	 * Store the index associated with each heads
	 */
	@Type(Blob.class)
	private List<Long> indices = new ArrayList<Long>();

	private int size = 0;

	private long lastUpdatedTimestamp;

	private int numberOfRecentSameTimestamp;

	public NumericBag() {
	}

	public NumericBag(String classifier) {
		this();
		this.setClassifier(classifier);
	}

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public Long getAutoId() {
		return autoId;
	}

	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

	public String getClassifier() {
		return classifier;
	}

	public List<Long> getRandomSet(int size) {
		return null;
	}

	public int getSize() {
		return size;
	}

	private void insertSegment(int index, Long number) {
		heads.add(index, number);
		tails.add(index, number);
		if (index == indices.size()) {
			indices.add(index, new Long(size));
		} else {
			indices.add(index, indices.get(index));
			alter(index + 1, indices.size() - 1, +1L, indices);
		}
	}

	public void addNumber(Long number) {
		if (size == 0) {
			insertSegment(0, number);
		} else {
			int segmentIndex = find(number, heads);
			if (segmentIndex == heads.size()) {
				if (tails.get(tails.size() - 1) >= number)
					return;
				if (tails.get(tails.size() - 1) == number - 1)
					tails.set(tails.size() - 1, number);
				else
					insertSegment(segmentIndex, number);
			} else if (segmentIndex == -1) {
				if (number == heads.get(0) - 1) {
					heads.set(0, number);
					alter(1, heads.size() - 1, +1L, indices);
				} else {
					insertSegment(0, number);
				}
			} else {
				if (heads.get(segmentIndex) == number)
					return;
				if (tails.get(segmentIndex) >= number)
					return;

				if (tails.get(segmentIndex) == number - 1) {
					tails.set(segmentIndex, number);
					if (segmentIndex < heads.size() - 1) {// reduce the number of segments in case the number can help connect to
						// separated segments
						if (heads.get(segmentIndex + 1) == number + 1) {
							heads.remove(segmentIndex + 1);
							tails.remove(segmentIndex);
							indices.remove(segmentIndex + 1);
						}
					}

					alter(segmentIndex + 1, indices.size() - 1, +1L, indices);
				} else {
					insertSegment(segmentIndex + 1, number);
				}
			}
		}
		size++;
	}

	public void removeNumber(Long number) {
		if (size == 0)
			return;
		int segmentIndex = find(number, heads);
		if (segmentIndex == -1)
			return;// number is smaller than any element in the bag
		if (segmentIndex >= heads.size()) {
			if (tails.get(segmentIndex - 1) >= number)
				segmentIndex -= 1;
			else
				return;
		}
		if (tails.get(segmentIndex) < number)
			return;
		if (tails.get(segmentIndex) == number) {
			tails.set(segmentIndex, number - 1);
			if (number - 1 < heads.get(segmentIndex)) {
				heads.remove(segmentIndex);
				tails.remove(segmentIndex);
				indices.remove(segmentIndex);
				segmentIndex--;
			}
		} else if (heads.get(segmentIndex) == number) {
			heads.set(segmentIndex, number + 1);
			if (number + 1 > tails.get(segmentIndex)) {
				heads.remove(segmentIndex);
				tails.remove(segmentIndex);
				indices.remove(segmentIndex);
				segmentIndex--;
			}
		} else if (tails.get(segmentIndex) > number && number > heads.get(segmentIndex)) {
			heads.add(segmentIndex + 1, number + 1);
			tails.add(segmentIndex, number - 1);
			indices.add(segmentIndex + 1, indices.get(segmentIndex) + number - heads.get(segmentIndex));
			segmentIndex++;
		} else
			return;// number does not belong to any segment
		size--;
		alter(segmentIndex + 1, indices.size() - 1, -1L, indices);
	}

	public Long get(int index) {
		if (index >= size || index < 0)
			return null;
		int segmentIndex = find(new Long(index), indices);
		if (segmentIndex == indices.size())
			segmentIndex--;
		return heads.get(segmentIndex) + index - indices.get(segmentIndex);
	}

	/**
	 * Return the index of segment that the number is equal or bigger than the number at the post but smaller than the next
	 * 
	 * @param number
	 * @return
	 */
	private int find(long number, List<Long> list) {
		return findRecursive(number, 0, list.size() - 1, list);
	}

	private int findRecursive(long number, int from, int to, List<Long> list) {
		if (from >= to - 1) {
			if (list.get(to).longValue() < number)
				return to + 1;
			if (list.get(to).longValue() == number)
				return to;
			if (list.get(from).longValue() > number)
				return from - 1;
			return from;
		}
		int i = (from + to) / 2;
		long middle = list.get(i);
		if (middle > number)
			return findRecursive(number, from, i, list);
		if (middle == number)
			return i;
		return findRecursive(number, i, to, list);

	}

	private void alter(int from, int to, Long delta, List<Long> list) {
		for (int i = from; i <= to; i++)
			list.set(i, list.get(i) + delta);
	}

	/**
	 * Return an array contains all number in bag in asc order. Used for test purpose only
	 * 
	 * @param bag
	 * @return
	 */
	public Object[] toArray() {
		int size = this.getSize();
		Object[] result = new Object[size];
		for (int i = 0; i < size; i++)
			result[i] = this.get(i);
		return result;
	}

	public static String getUniformClassifier(Class<?> clazz, Object key) {
		return clazz.getCanonicalName() + "@" + key;
	}

	public void setLastUpdatedTimestamp(long lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}

	public long getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}

	public void setNumberOfRecentSameTimestamp(int numberOfRecentSameTimestamp) {
		this.numberOfRecentSameTimestamp = numberOfRecentSameTimestamp;
	}

	public int getNumberOfRecentSameTimestamp() {
		return numberOfRecentSameTimestamp;
	}

}
