package org.seamoo.lookup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;

@PersistenceCapable
public class NumericBag {
	private SortedSet<Long> keySet;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long autoId;
	/**
	 * A string used to classify different type of bag, oftenly the class name
	 * of entity
	 */
	@Persistent
	private String classifier;

	@Persistent
	private Blob keyBlob;

	private ByteArrayOutputStream keyBAOS;
	private DataOutputStream keyDOS;

	public NumericBag() {
		keyBlob = new Blob(new byte[0]);
	}

	public NumericBag(String classifier) {
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

	protected void prepareBlobOutputStream() {
		if (keyBAOS == null) {
			try {
				keyBAOS = new ByteArrayOutputStream();
				keyDOS = new DataOutputStream(keyBAOS);
				keyDOS.write(keyBlob.getBytes());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	private void rewriteKeyBlob() {
		try {
			keyBAOS = new ByteArrayOutputStream(0);
			keyDOS = new DataOutputStream(keyBAOS);
			for (Long l : keySet)
				keyDOS.writeLong(l);
			keyBlob = new Blob(keyBAOS.toByteArray());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void addKey(Long key) {
		keySet.add(key);
		prepareBlobOutputStream();
		try {
			keyDOS.writeLong(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		keyBlob = new Blob(keyBAOS.toByteArray());
	}

	public void removeKey(Long key) {
		keySet.remove(key);
		rewriteKeyBlob();
	}

	public SortedSet<Long> getKeySet() throws IOException {
		if (keySet == null) {
			keySet = new TreeSet<Long>();
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(keyBlob.getBytes());
			DataInputStream inputStream = new DataInputStream(byteInputStream);
			keySet.add(inputStream.readLong());
		}
		return keySet;
	}
}
