package org.seamoo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

public class ResourceIteratorProvider {
	public ResourceIterator<String> getIterator(String resourceName) throws IOException {
		final ResourceLoader loader = new DefaultResourceLoader();
		final InputStream stream = loader.getResource(resourceName).getInputStream();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));

		ResourceIterator<String> r = new AbstractResourceIterator<String>() {

			@Override
			public void remove() {
			}

			@Override
			public String next() {
				try {
					return reader.readLine();
				} catch (IOException e) {
					return null;
				}
			}

			@Override
			public boolean hasNext() {
				try {
					return reader.ready();
				} catch (IOException e) {
					return false;
				}
			}

			@Override
			public void close() {
				try {
					reader.close();
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		return r;

	}
}
