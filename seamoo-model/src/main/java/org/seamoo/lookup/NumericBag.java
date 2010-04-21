package org.seamoo.lookup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Blob;
import com.vercer.engine.persist.annotation.Key;
import com.vercer.engine.persist.annotation.Store;

public class NumericBag {
	@Id
	@Key
	private Long autoId;
	/**
	 * A string used to classify different type of bag, oftenly the class name
	 * of entity
	 */

	private String classifier;

	private Blob keyBlob;

	@Transient
	@Store(false)
	private List<Long> keyList;

	@Store(false)
	@Transient
	private ByteArrayOutputStream keyBAOS;
	@Store(false)
	@Transient
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
			for (Long l : keyList)
				keyDOS.writeLong(l);
			keyBlob = new Blob(keyBAOS.toByteArray());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void addKey(Long key) {
		getKeyList().add(key);
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
		getKeyList().remove(key);
		rewriteKeyBlob();
	}

	public List<Long> getKeyList() {
		if (keyList == null) {
			keyList = new ArrayList<Long>();
			try {
				ByteArrayInputStream byteInputStream = new ByteArrayInputStream(keyBlob.getBytes());
				DataInputStream inputStream = new DataInputStream(byteInputStream);
				while (inputStream.available() > 0) {
					keyList.add(inputStream.readLong());
				}
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		return keyList;
	}
}