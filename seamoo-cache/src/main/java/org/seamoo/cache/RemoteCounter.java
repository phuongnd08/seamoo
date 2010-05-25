package org.seamoo.cache;

public interface RemoteCounter {
	/**
	 * Increase the counter by one, and return the result
	 * The operation is synchronized and atomic
	 * @return
	 */
	public long inc();
	/**
	 * Decrease the counter by one, and return the result
	 * The operation is synchronized and atomic
	 * @return
	 */
	public long dec();
	/**
	 * Change the counter by a given number, and return the result
	 * The operation is synchronized and atomic 
	 * @param step
	 * @return
	 */
	public long alter(long step);
}
