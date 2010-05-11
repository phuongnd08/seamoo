package org.seamoo.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public final class ObjectSerializer {
	public static Object cloneObject(Object object) throws IOException, ClassNotFoundException {
		ByteOutputStream bos = new ByteOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(object);
		oos.close();
		ByteInputStream bis = new ByteInputStream(bos.getBytes(), bos.getCount());
		ObjectInputStream ois = new ObjectInputStream(bis);
		return ois.readObject();
	}
}
