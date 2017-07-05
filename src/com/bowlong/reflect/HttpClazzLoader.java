package com.bowlong.reflect;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class HttpClazzLoader {
	public class CacheClaccLoader {
		public final int hashCode;
		public final String url;
		public final URLClassLoader ucl;

		private final Map<String, Class<?>> cached = new ConcurrentHashMap<>();

		public CacheClaccLoader(final String szUrl)
				throws MalformedURLException {
			this.hashCode = szUrl.hashCode();
			this.url = szUrl;
			// final URL url = new URL(szUrl);
			// final URL[] urls = new URL[1];
			// urls[0] = url;
			final URL[] urls = new URL[] { new URL(szUrl) };
			this.ucl = new URLClassLoader(urls);
		}

		public Class<?> loadClass(String name) throws ClassNotFoundException {
			Class<?> c = cached.get(name);
			if (c == null) {
				if (ucl == null)
					return null;
				c = ucl.loadClass(name);
				if (c == null)
					return null;

				cached.put(name, c);
			}
			return c;
		}

		public void close() {
			if (this.ucl == null)
				return;
			try {
				this.ucl.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// /////////////////////////////////////////////

	public CacheClaccLoader newCacheClaccLoader(final String szUrl)
			throws MalformedURLException {
		return new CacheClaccLoader(szUrl);
	}

	// /////////////////////////////////////////////
	private static final HttpClazzLoader instance = new HttpClazzLoader();

	// /////////////////////////////////////////////

	private static final Map<String, CacheClaccLoader> cached = new ConcurrentHashMap<>();

	public static final synchronized CacheClaccLoader getCacheClassLoader(
			final String szUrl) throws Exception {
		CacheClaccLoader ucl = cached.get(szUrl);
		if (ucl == null) {
			ucl = instance.newCacheClaccLoader(szUrl);
			cached.put(szUrl, ucl);
		}
		return ucl;
	}

	public static final Class<?> loadClass(final String szUrl,
			final String className) throws Exception {
		final CacheClaccLoader ucl = getCacheClassLoader(szUrl);
		if (ucl == null)
			return null;
		return ucl.loadClass(className);
	}

	public static final <T> T loadObject(final String szUrl,
			final String className) throws Exception {
		final Class<?> c = loadClass(szUrl, className);
		if (c == null)
			return null;
		return (T) c.newInstance();
	}
}
