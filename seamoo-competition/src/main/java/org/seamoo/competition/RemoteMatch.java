package org.seamoo.competition;

import java.util.ArrayList;
import java.util.List;

import org.seamoo.cache.RemoteCompositeObject;
import org.seamoo.cache.RemoteObject;
import org.seamoo.cache.memcacheImpl.MemcacheRemoteCompositeObjectImpl;
import org.seamoo.cache.memcacheImpl.MemcacheRemoteObjectImpl;
import org.seamoo.entities.matching.MatchCompetitor;

import com.google.appengine.api.memcache.MemcacheService;

public class RemoteMatch {
	/**
	 * The moment from which match have 2 players
	 */
	public static final String READY_MOMENT_SUBKEY = "readyMoment";
	public static final String FINISHED_MOMENT_SUBKEY = "finishedMoment";
	public static final String COMPETITORS_SUBKEY = "competitors";
	public static final String COMPETITORS_COUNT_SUBKEY = COMPETITORS_SUBKEY + "@count";
	public static final String DB_AUTOID_SUBKEY = "dbAutoId";
	public static final String QUESTION_IDS_SUBKEY = "questionIds";

	private Long key;

	RemoteCompositeObject mixin;

	public RemoteMatch(RemoteCompositeObject mixin) {
		this.mixin = mixin;
	}

	public long getReadyMoment() {
		return mixin.get(Long.class, READY_MOMENT_SUBKEY, new Long(0));
	}

	public void setReadyMoment(long moment) {
		mixin.put(READY_MOMENT_SUBKEY, new Long(moment));
	}

	public int acquireCompetitorSlot() {
		long slot = mixin.inc(COMPETITORS_COUNT_SUBKEY, 1L, 0L);
		return (int) slot;
	}

	public long getCompetitorCount() {
		return mixin.get(Long.class, COMPETITORS_COUNT_SUBKEY, new Long(0));
	}

	public MatchCompetitor[] getCompetitors() {
		List<MatchCompetitor> results = new ArrayList<MatchCompetitor>();
		long count = getCompetitorCount();
		for (int i = 1; i <= count; i++) {
			RemoteObject<MatchCompetitor> ro = getRemoteCompetitor(i);
			MatchCompetitor o = ro.getObject();
			if (o == null)
				break;
			results.add(o);
		}
		return results.toArray(new MatchCompetitor[results.size()]);
	}

	private String getCompetitorSlotKey(int slot) {
		return COMPETITORS_SUBKEY + "@" + slot;
	}

	public RemoteObject<MatchCompetitor> getRemoteCompetitor(int slot) {
		return mixin.getSubRemoteObject(getCompetitorSlotKey(slot));
	}

	public Long getDbKey() {
		return mixin.get(Long.class, DB_AUTOID_SUBKEY);
	}

	public void setDbKey(Long dbKey) {
		mixin.put(DB_AUTOID_SUBKEY, dbKey);
	}

	public long getFinishedMoment() {
		return mixin.get(Long.class, FINISHED_MOMENT_SUBKEY, new Long(0));
	}

	public void setFinishedMoment(long moment) {
		mixin.put(FINISHED_MOMENT_SUBKEY, new Long(moment));
	}

	public Long[] getQuestionIds() {
		return mixin.get(Long[].class, QUESTION_IDS_SUBKEY);
	}

	public void setQuestionIds(Long[] questionIds) {
		mixin.put(QUESTION_IDS_SUBKEY, questionIds);
	}

	public boolean isMatchLocked() {
		return mixin.isLocked();
	}

	public boolean tryLockMatch() {
		return mixin.tryLock(0);
	}

	public void unlockMatch() {
		mixin.unlock();
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getKey() {
		return key;
	}
}
