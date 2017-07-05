package com.bowlong.concurrent.async;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RecursiveTask;

import com.bowlong.bio2.B2Helper;
import com.bowlong.io.FileEx;
import com.bowlong.lang.ByteEx;
import com.bowlong.lang.StrEx;
import com.bowlong.lang.task.ThreadEx;
import com.bowlong.net.http.HttpEx;
import com.bowlong.text.Encoding;
import com.bowlong.third.FastJSON;

@SuppressWarnings("rawtypes")
public class Async {
	static ExecutorService THREAD_POOL;

	static int MIN = 1;
	static int MAX = 64;

	public static void init() {
		getThreadPool();
	}

	public static void init(final int min, final int max) {
		MIN = min;
		MAX = max;

		init();
	}

	static final ExecutorService getThreadPool() {
		if (THREAD_POOL == null)
			THREAD_POOL = ThreadEx.newThreadExecutor(MIN, MAX);
		return THREAD_POOL;
	}

	public static final ExecutorService newExecutor(final int min,
			final int max, final String name) {
		return ThreadEx.newThreadExecutor(min, max);
	}

	public static final ExecutorService newSingleThreadExecutor() {
		return ThreadEx.newSingleThreadExecutor();
	}

	static ExecutorService single;

	public static final void executeN1(Runnable r) {
		if (single == null)
			single = newSingleThreadExecutor();
		single.execute(r);
	}

	public static final Future submitN1(Callable<Object> task) {
		if (single == null)
			single = newSingleThreadExecutor();
		return single.submit(task);
	}

	public static class ForExcept {
		static final FutureTask<Exception> execute(final ExecutorService es,
				final FutureTask<Exception> task) {
			es.submit(task);
			return task;
		}

		static final FutureTask<Exception> execute(final ExecutorService es,
				final Callable<Exception> task) {
			FutureTask<Exception> ft = new FutureTask<Exception>(task);
			return execute(es, ft);
		}

		public static final FutureTask<Exception> exec(final CallableExcept ce) {
			ExecutorService es = getThreadPool();
			return execute(es, ce);
		}

		public static final FutureTask<Exception> exec(final CallableExcept ce,
				Queue<FutureTask<Exception>> outQueue) {
			final FutureTask<Exception> f = exec(ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<Exception> exec(
				final ExecutorService es, final CallableExcept ce) {
			return execute(es, ce);
		}

		public static final FutureTask<Exception> exec(
				final ExecutorService es, final CallableExcept ce,
				Queue<FutureTask<Exception>> outQueue) {
			final FutureTask<Exception> f = exec(es, ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final Queue<FutureTask<Exception>> newQueue() {
			return new LinkedList<FutureTask<Exception>>();
		}

		public static final Exception findExcept(
				final Queue<FutureTask<Exception>> outQueue)
				throws InterruptedException, ExecutionException {
			if (outQueue == null || outQueue.isEmpty())
				return null;
			while (!outQueue.isEmpty()) {
				FutureTask<Exception> f = outQueue.poll();
				Exception e = f.get();
				if (e != null)
					return e;
			}
			return null;
		}
	}

	public static class ForObject {
		static final FutureTask<Object> execute(final ExecutorService es,
				final FutureTask<Object> task) {
			es.submit(task);
			return task;
		}

		static final FutureTask<Object> execute(final ExecutorService es,
				final Callable<Object> task) {
			FutureTask<Object> ft = new FutureTask<Object>(task);
			return execute(es, ft);
		}

		public static final FutureTask<Object> exec(final CallableForObject ce) {
			ExecutorService es = getThreadPool();
			return execute(es, ce);
		}

		public static final FutureTask<Object> exec(final ExecutorService es,
				final CallableForObject ce) {
			return execute(es, ce);
		}

		public static final FutureTask<Object> exec(final CallableForObject ce,
				Queue<Object> outQueue) {
			FutureTask<Object> f = exec(ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final Queue<FutureTask<Object>> newQueue() {
			return new LinkedList<FutureTask<Object>>();
		}
	}

	public static class ForBool {
		static final FutureTask<Boolean> execute(final ExecutorService es,
				final FutureTask<Boolean> task) {
			es.submit(task);
			return task;
		}

		static final FutureTask<Boolean> execute(final ExecutorService es,
				final Callable<Boolean> task) {
			FutureTask<Boolean> ft = new FutureTask<Boolean>(task);
			return execute(es, ft);
		}

		public static final FutureTask<Boolean> exec(final CallableForBoolean ce) {
			ExecutorService es = getThreadPool();
			return execute(es, ce);
		}

		public static final FutureTask<Boolean> exec(
				final CallableForBoolean ce, Queue<FutureTask<Boolean>> outQueue) {
			FutureTask<Boolean> f = exec(ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<Boolean> exec(final ExecutorService es,
				final CallableForBoolean ce) {
			return execute(es, ce);
		}

		public static final FutureTask<Boolean> exec(final ExecutorService es,
				final CallableForBoolean ce, Queue<FutureTask<Boolean>> outQueue) {
			FutureTask<Boolean> f = exec(es, ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

	}

	public static class ForInt {
		static final FutureTask<Integer> execute(final ExecutorService es,
				final FutureTask<Integer> task) {
			es.submit(task);
			return task;
		}

		static final FutureTask<Integer> execute(final ExecutorService es,
				final Callable<Integer> task) {
			FutureTask<Integer> ft = new FutureTask<Integer>(task);
			return execute(es, ft);
		}

		public static final FutureTask<Integer> exec(final CallableForInt ce) {
			ExecutorService es = getThreadPool();
			return execute(es, ce);
		}

		public static final FutureTask<Integer> exec(final CallableForInt ce,
				final Queue<FutureTask<Integer>> outQueue) {
			FutureTask<Integer> f = exec(ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<Integer> exec(final ExecutorService es,
				final CallableForInt ce) {
			return execute(es, ce);
		}

		public static final FutureTask<Integer> exec(final ExecutorService es,
				final CallableForInt ce,
				final Queue<FutureTask<Integer>> outQueue) {
			FutureTask<Integer> f = exec(es, ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

	}

	public static class ForDouble {
		static final FutureTask<Double> execute(final ExecutorService es,
				final FutureTask<Double> task) {
			es.submit(task);
			return task;
		}

		static final FutureTask<Double> execute(final ExecutorService es,
				final Callable<Double> task) {
			FutureTask<Double> ft = new FutureTask<Double>(task);
			return execute(es, ft);
		}

		public static final FutureTask<Double> exec(final CallableForDouble ce) {
			ExecutorService es = getThreadPool();
			return execute(es, ce);
		}

		public static final FutureTask<Double> exec(final CallableForDouble ce,
				final Queue<FutureTask<Double>> outQueue) {
			FutureTask<Double> f = exec(ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<Double> exec(final ExecutorService es,
				final CallableForDouble ce) {
			return execute(es, ce);
		}

		public static final FutureTask<Double> exec(final ExecutorService es,
				final CallableForDouble ce,
				final Queue<FutureTask<Double>> outQueue) {
			FutureTask<Double> f = exec(es, ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

	}

	public static class ForString {
		static final FutureTask<String> execute(final ExecutorService es,
				final FutureTask<String> task) {
			es.submit(task);
			return task;
		}

		static final FutureTask<String> execute(final ExecutorService es,
				final Callable<String> task) {
			FutureTask<String> ft = new FutureTask<String>(task);
			return execute(es, ft);
		}

		public static final FutureTask<String> exec(final CallableForString ce) {
			ExecutorService es = getThreadPool();
			return execute(es, ce);
		}

		public static final FutureTask<String> exec(final CallableForString ce,
				final Queue<FutureTask<String>> outQueue) {
			FutureTask<String> f = exec(ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<String> exec(final ExecutorService es,
				final CallableForString ce) {
			return execute(es, ce);
		}

		public static final FutureTask<String> exec(final ExecutorService es,
				final CallableForString ce,
				final Queue<FutureTask<String>> outQueue) {
			FutureTask<String> f = exec(es, ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

	}

	public static class ForBytes {
		static final FutureTask<byte[]> execute(final ExecutorService es,
				final FutureTask<byte[]> task) {
			es.submit(task);
			return task;
		}

		static final FutureTask<byte[]> execute(final ExecutorService es,
				final Callable<byte[]> task) {
			FutureTask<byte[]> ft = new FutureTask<byte[]>(task);
			return execute(es, ft);
		}

		public static final FutureTask<byte[]> exec(final CallableForBytes ce) {
			ExecutorService es = getThreadPool();
			return execute(es, ce);
		}

		public static final FutureTask<byte[]> exec(final CallableForBytes ce,
				final Queue<FutureTask<byte[]>> outQueue) {
			FutureTask<byte[]> f = exec(ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<byte[]> exec(final ExecutorService es,
				final CallableForBytes ce) {
			return execute(es, ce);
		}

		public static final FutureTask<byte[]> exec(final ExecutorService es,
				final CallableForBytes ce,
				final Queue<FutureTask<byte[]>> outQueue) {
			FutureTask<byte[]> f = exec(es, ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

	}

	public static class ForMap {
		static final FutureTask<Map> execute(final ExecutorService es,
				final FutureTask<Map> task) {
			es.submit(task);
			return task;
		}

		static final FutureTask<Map> execute(final ExecutorService es,
				final Callable<Map> task) {
			FutureTask<Map> ft = new FutureTask<Map>(task);
			return execute(es, ft);
		}

		public static final FutureTask<Map> exec(final CallableForMap ce) {
			ExecutorService es = getThreadPool();
			return execute(es, ce);
		}

		public static final FutureTask<Map> exec(final CallableForMap ce,
				final Queue<FutureTask<Map>> outQueue) {
			FutureTask<Map> f = exec(ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<Map> exec(final ExecutorService es,
				final CallableForMap ce) {
			return execute(es, ce);
		}

		public static final FutureTask<Map> exec(final ExecutorService es,
				final CallableForMap ce, final Queue<FutureTask<Map>> outQueue) {
			FutureTask<Map> f = exec(es, ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

	}

	public static class ForList {
		static final FutureTask<List> execute(final ExecutorService es,
				final FutureTask<List> task) {
			es.submit(task);
			return task;
		}

		static final FutureTask<List> execute(final ExecutorService es,
				final Callable<List> task) {
			FutureTask<List> ft = new FutureTask<List>(task);
			return execute(es, ft);
		}

		public static final FutureTask<List> exec(final CallableForList ce) {
			ExecutorService es = getThreadPool();
			return execute(es, ce);
		}

		public static final FutureTask<List> exec(final CallableForList ce,
				final Queue<FutureTask<List>> outQueue) {
			FutureTask<List> f = exec(ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<List> exec(final ExecutorService es,
				final CallableForList ce) {
			return execute(es, ce);
		}

		public static final FutureTask<List> exec(final ExecutorService es,
				final CallableForList ce, final Queue<FutureTask<List>> outQueue) {
			FutureTask<List> f = exec(es, ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

	}

	public static class ForResultSet {
		static final FutureTask<ResultSet> execute(final ExecutorService es,
				final FutureTask<ResultSet> task) {
			es.submit(task);
			return task;
		}

		static final FutureTask<ResultSet> execute(final ExecutorService es,
				final Callable<ResultSet> task) {
			FutureTask<ResultSet> ft = new FutureTask<ResultSet>(task);
			return execute(es, ft);
		}

		public static final FutureTask<ResultSet> exec(
				final CallableForResultSet ce) {
			ExecutorService es = getThreadPool();
			return execute(es, ce);
		}

		public static final FutureTask<ResultSet> exec(
				final CallableForResultSet ce,
				final Queue<FutureTask<ResultSet>> outQueue) {
			FutureTask<ResultSet> f = exec(ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<ResultSet> exec(
				final ExecutorService es, final CallableForResultSet ce) {
			return execute(es, ce);
		}

		public static final FutureTask<ResultSet> exec(
				final ExecutorService es, final CallableForResultSet ce,
				final Queue<FutureTask<ResultSet>> outQueue) {
			FutureTask<ResultSet> f = exec(es, ce);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

	}

	public static class ToFile {
		public static final FutureTask<byte[]> readFully(final File fn) {
			FutureTask<byte[]> f = ForBytes.exec(new CallableForBytes() {
				public byte[] exec() throws Exception {
					return FileEx.readFully(fn);
				}
			});
			return f;
		}

		public static final FutureTask<byte[]> readFully(
				final ExecutorService es, final File fn) {
			FutureTask<byte[]> f = ForBytes.exec(es, new CallableForBytes() {
				public byte[] exec() throws Exception {
					return FileEx.readFully(fn);
				}
			});
			return f;
		}

		public static final FutureTask<byte[]> readFully(final File fn,
				final Queue<FutureTask<byte[]>> outQueue) {
			FutureTask<byte[]> f = readFully(fn);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<byte[]> readFully(
				final ExecutorService es, final File fn,
				final Queue<FutureTask<byte[]>> outQueue) {
			FutureTask<byte[]> f = readFully(es, fn);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<Exception> writeFully(final File fn,
				final byte[] buf) {
			FutureTask<Exception> f = ForExcept.exec(new CallableExcept() {
				public void exec() throws Exception {
					FileEx.write(fn, buf);
				}
			});
			return f;
		}

		public static final FutureTask<Exception> writeFully(
				final ExecutorService es, final File fn, final byte[] buf) {
			FutureTask<Exception> f = ForExcept.exec(es, new CallableExcept() {
				public void exec() throws Exception {
					FileEx.write(fn, buf);
				}
			});
			return f;
		}

		public static final FutureTask<Exception> writeFully(final File fn,
				final byte[] buf, final Queue<FutureTask<Exception>> outQueue) {
			FutureTask<Exception> f = writeFully(fn, buf);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<Exception> writeFully(
				final ExecutorService es, final File fn, final byte[] buf,
				final Queue<FutureTask<Exception>> outQueue) {
			FutureTask<Exception> f = writeFully(es, fn, buf);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<String> readText(final File fn) {
			FutureTask<String> f = ForString.exec(new CallableForString() {
				public String exec() throws Exception {
					return FileEx.readText(fn);
				}
			});
			return f;
		}

		public static final FutureTask<String> readText(
				final ExecutorService es, final File fn) {
			FutureTask<String> f = ForString.exec(es, new CallableForString() {
				public String exec() throws Exception {
					return FileEx.readText(fn);
				}
			});
			return f;
		}

		public static final FutureTask<String> readText(final File fn,
				final Queue<FutureTask<String>> outQueue) {
			FutureTask<String> f = readText(fn);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<String> readText(
				final ExecutorService es, final File fn,
				final Queue<FutureTask<String>> outQueue) {
			FutureTask<String> f = readText(es, fn);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<Exception> writeText(final File fn,
				final String str) {
			Charset charset = Encoding.UTF8;
			return writeText(fn, str, charset);
		}

		public static final FutureTask<Exception> writeText(
				final ExecutorService es, final File fn, final String str) {
			Charset charset = Encoding.UTF8;
			return writeText(es, fn, str, charset);
		}

		public static final FutureTask<Exception> writeText(final File fn,
				final String str, Queue<FutureTask<Exception>> outQueue) {
			Charset charset = Encoding.UTF8;
			return writeText(fn, str, charset, outQueue);
		}

		public static final FutureTask<Exception> writeText(
				final ExecutorService es, final File fn, final String str,
				Queue<FutureTask<Exception>> outQueue) {
			Charset charset = Encoding.UTF8;
			return writeText(es, fn, str, charset, outQueue);
		}

		public static final FutureTask<Exception> writeText(final File fn,
				final String str, final Charset charset) {
			FutureTask<Exception> f = ForExcept.exec(new CallableExcept() {
				public void exec() throws Exception {
					FileEx.writeText(fn, str, charset);
				}
			});
			return f;
		}

		public static final FutureTask<Exception> writeText(
				final ExecutorService es, final File fn, final String str,
				final Charset charset) {
			FutureTask<Exception> f = ForExcept.exec(es, new CallableExcept() {
				public void exec() throws Exception {
					FileEx.writeText(fn, str, charset);
				}
			});
			return f;
		}

		public static final FutureTask<Exception> writeText(final File fn,
				final String str, final Charset charset,
				Queue<FutureTask<Exception>> outQueue) {
			FutureTask<Exception> f = writeText(fn, str, charset);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

		public static final FutureTask<Exception> writeText(
				final ExecutorService es, final File fn, final String str,
				final Charset charset, Queue<FutureTask<Exception>> outQueue) {
			FutureTask<Exception> f = writeText(es, fn, str, charset);
			if (outQueue != null)
				outQueue.add(f);
			return f;
		}

	}

	public static final class ToJson {
		public static final FutureTask<String> toJSONString(final Map map) {
			FutureTask<String> f = ForString.exec(new CallableForString() {
				public String exec() throws Exception {
					return FastJSON.format(map);
				}
			});
			return f;
		}

		public static final FutureTask<String> toJSONString(
				final ExecutorService es, final Map map) {
			FutureTask<String> f = ForString.exec(es, new CallableForString() {
				public String exec() throws Exception {
					return FastJSON.format(map);
				}
			});
			return f;
		}

		public static final FutureTask<Exception> writeTo(final File fn,
				final Map map) {
			final Charset charset = Encoding.UTF8;
			return writeTo(fn, map, charset);
		}

		public static final FutureTask<Exception> writeTo(
				final ExecutorService es, final File fn, final Map map) {
			final Charset charset = Encoding.UTF8;
			return writeTo(es, fn, map, charset);
		}

		public static final FutureTask<Exception> writeTo(final File fn,
				final Map map, final Charset charset) {
			FutureTask<Exception> f = ForExcept.exec(new CallableExcept() {
				public void exec() throws Exception {
					String str = FastJSON.format(map);
					FileEx.writeText(fn, str, charset);
				}
			});
			return f;
		}

		public static final FutureTask<Exception> writeTo(
				final ExecutorService es, final File fn, final Map map,
				final Charset charset) {
			FutureTask<Exception> f = ForExcept.exec(es, new CallableExcept() {
				public void exec() throws Exception {
					String str = FastJSON.format(map);
					FileEx.writeText(fn, str, charset);
				}
			});
			return f;
		}

		public static final FutureTask<String> toJSONString(final List list) {
			FutureTask<String> f = ForString.exec(new CallableForString() {
				public String exec() throws Exception {
					return FastJSON.format(list);
				}
			});
			return f;
		}

		public static final FutureTask<String> toJSONString(
				final ExecutorService es, final List list) {
			FutureTask<String> f = ForString.exec(es, new CallableForString() {
				public String exec() throws Exception {
					return FastJSON.format(list);
				}
			});
			return f;
		}

		public static final FutureTask<Exception> writeTo(final File fn,
				final List list, final Charset charset) {
			FutureTask<Exception> f = ForExcept.exec(new CallableExcept() {
				public void exec() throws Exception {
					String str = FastJSON.format(list);
					FileEx.writeText(fn, str, charset);
				}
			});
			return f;
		}

		public static final FutureTask<Exception> writeTo(
				final ExecutorService es, final File fn, final List list,
				final Charset charset) {
			FutureTask<Exception> f = ForExcept.exec(es, new CallableExcept() {
				public void exec() throws Exception {
					String str = FastJSON.format(list);
					FileEx.writeText(fn, str, charset);
				}
			});
			return f;
		}

		public static final FutureTask<Exception> writeTo(final File fn,
				final List list) {
			final Charset charset = Encoding.UTF8;
			return writeTo(fn, list, charset);
		}

		public static final FutureTask<Exception> writeTo(
				final ExecutorService es, final File fn, final List list) {
			final Charset charset = Encoding.UTF8;
			return writeTo(es, fn, list, charset);
		}

		public static final FutureTask<Map> parseMap(final String json) {
			FutureTask<Map> f = ForMap.exec(new CallableForMap() {
				public Map exec() throws Exception {
					return FastJSON.parseMap(json);
				}
			});
			return f;
		}

		public static final FutureTask<Map> parseMap(final ExecutorService es,
				final String json) {
			FutureTask<Map> f = ForMap.exec(es, new CallableForMap() {
				public Map exec() throws Exception {
					return FastJSON.parseMap(json);
				}
			});
			return f;
		}

		public static final FutureTask<Map> parseMap(final File fn) {
			FutureTask<Map> f = ForMap.exec(new CallableForMap() {
				public Map exec() throws Exception {
					if (FileEx.isEmpty(fn))
						return new HashMap();
					String json = FileEx.readText(fn);
					if (StrEx.isEmpty(json))
						return new HashMap();
					return FastJSON.parseMap(json);
				}
			});
			return f;
		}

		public static final FutureTask<Map> parseMap(final ExecutorService es,
				final File fn) {
			FutureTask<Map> f = ForMap.exec(es, new CallableForMap() {
				public Map exec() throws Exception {
					if (FileEx.isEmpty(fn))
						return new HashMap();
					String json = FileEx.readText(fn);
					if (StrEx.isEmpty(json))
						return new HashMap();
					return FastJSON.parseMap(json);
				}
			});
			return f;
		}

		public static final FutureTask<List> parseList(final String json) {
			FutureTask<List> f = ForList.exec(new CallableForList() {
				public List exec() throws Exception {
					return FastJSON.parseList(json);
				}
			});
			return f;
		}

		public static final FutureTask<List> parseList(
				final ExecutorService es, final String json) {
			FutureTask<List> f = ForList.exec(es, new CallableForList() {
				public List exec() throws Exception {
					return FastJSON.parseList(json);
				}
			});
			return f;
		}

		public static final FutureTask<List> parseList(final File fn) {
			FutureTask<List> f = ForList.exec(new CallableForList() {
				public List exec() throws Exception {
					if (FileEx.isEmpty(fn))
						return new ArrayList();
					String json = FileEx.readText(fn);
					if (StrEx.isEmpty(json))
						return new ArrayList();
					return FastJSON.parseList(json);
				}
			});
			return f;
		}

		public static final FutureTask<List> parseList(
				final ExecutorService es, final File fn) {
			FutureTask<List> f = ForList.exec(es, new CallableForList() {
				public List exec() throws Exception {
					if (FileEx.isEmpty(fn))
						return new ArrayList();
					String json = FileEx.readText(fn);
					if (StrEx.isEmpty(json))
						return new ArrayList();
					return FastJSON.parseList(json);
				}
			});
			return f;
		}

	}

	public static final class ToBio2 {
		public static final FutureTask<byte[]> toBytes(final Map map) {
			FutureTask<byte[]> f = ForBytes.exec(new CallableForBytes() {
				public byte[] exec() throws Exception {
					return B2Helper.toBytes(map);
				}
			});
			return f;
		}

		public static final FutureTask<byte[]> toBytes(
				final ExecutorService es, final Map map) {
			FutureTask<byte[]> f = ForBytes.exec(es, new CallableForBytes() {
				public byte[] exec() throws Exception {
					return B2Helper.toBytes(map);
				}
			});
			return f;
		}

		public static final FutureTask<Exception> writeTo(final File fn,
				final Map map) {
			FutureTask<Exception> f = ForExcept.exec(new CallableExcept() {
				public void exec() throws Exception {
					byte[] buf = B2Helper.toBytes(map);
					FileEx.write(fn, buf);
				}
			});
			return f;
		}

		public static final FutureTask<Exception> writeTo(
				final ExecutorService es, final File fn, final Map map) {
			FutureTask<Exception> f = ForExcept.exec(es, new CallableExcept() {
				public void exec() throws Exception {
					byte[] buf = B2Helper.toBytes(map);
					FileEx.write(fn, buf);
				}
			});
			return f;
		}

		public static final FutureTask<byte[]> toBytes(final List list) {
			FutureTask<byte[]> f = ForBytes.exec(new CallableForBytes() {
				public byte[] exec() throws Exception {
					return B2Helper.toBytes(list);
				}
			});
			return f;
		}

		public static final FutureTask<byte[]> toBytes(
				final ExecutorService es, final List list) {
			FutureTask<byte[]> f = ForBytes.exec(es, new CallableForBytes() {
				public byte[] exec() throws Exception {
					return B2Helper.toBytes(list);
				}
			});
			return f;
		}

		public static final FutureTask<Exception> writeTo(final File fn,
				final List list) {
			FutureTask<Exception> f = ForExcept.exec(new CallableExcept() {
				public void exec() throws Exception {
					byte[] buf = B2Helper.toBytes(list);
					FileEx.write(fn, buf);
				}
			});
			return f;
		}

		public static final FutureTask<Exception> writeTo(
				final ExecutorService es, final File fn, final List list) {
			FutureTask<Exception> f = ForExcept.exec(es, new CallableExcept() {
				public void exec() throws Exception {
					byte[] buf = B2Helper.toBytes(list);
					FileEx.write(fn, buf);
				}
			});
			return f;
		}

		public static final FutureTask<Map> parseMap(final byte[] buf) {
			FutureTask<Map> f = ForMap.exec(new CallableForMap() {
				public Map exec() throws Exception {
					return B2Helper.toMap(buf);
				}
			});
			return f;
		}

		public static final FutureTask<Map> parseMap(final ExecutorService es,
				final byte[] buf) {
			FutureTask<Map> f = ForMap.exec(es, new CallableForMap() {
				public Map exec() throws Exception {
					return B2Helper.toMap(buf);
				}
			});
			return f;
		}

		public static final FutureTask<Map> parseMap(final File fn) {
			FutureTask<Map> f = ForMap.exec(new CallableForMap() {
				public Map exec() throws Exception {
					final byte[] buf = FileEx.readFully(fn);
					return B2Helper.toMap(buf);
				}
			});
			return f;
		}

		public static final FutureTask<Map> parseMap(final ExecutorService es,
				final File fn) {
			FutureTask<Map> f = ForMap.exec(es, new CallableForMap() {
				public Map exec() throws Exception {
					final byte[] buf = FileEx.readFully(fn);
					return B2Helper.toMap(buf);
				}
			});
			return f;
		}

		public static final FutureTask<Map> parseMap(final URL url) {
			FutureTask<Map> f = ForMap.exec(new CallableForMap() {
				public Map exec() throws Exception {
					try (InputStream in = url.openStream();) {
						final byte[] buf = FileEx.readFully(in);
						return B2Helper.toMap(buf);
					}
				}
			});
			return f;
		}

		public static final FutureTask<Map> parseMap(final ExecutorService es,
				final URL url) {
			FutureTask<Map> f = ForMap.exec(es, new CallableForMap() {
				public Map exec() throws Exception {
					try (InputStream in = url.openStream();) {
						final byte[] buf = FileEx.readFully(in);
						return B2Helper.toMap(buf);
					}
				}
			});
			return f;
		}

		public static final FutureTask<List> parseList(final byte[] buf) {
			FutureTask<List> f = ForList.exec(new CallableForList() {
				public List exec() throws Exception {
					return B2Helper.toList(buf);
				}
			});
			return f;
		}

		public static final FutureTask<List> parseList(
				final ExecutorService es, final byte[] buf) {
			FutureTask<List> f = ForList.exec(es, new CallableForList() {
				public List exec() throws Exception {
					return B2Helper.toList(buf);
				}
			});
			return f;
		}

		public static final FutureTask<List> parseList(final File fn) {
			FutureTask<List> f = ForList.exec(new CallableForList() {
				public List exec() throws Exception {
					final byte[] buf = FileEx.readFully(fn);
					return B2Helper.toList(buf);
				}
			});
			return f;
		}

		public static final FutureTask<List> parseList(
				final ExecutorService es, final File fn) {
			FutureTask<List> f = ForList.exec(es, new CallableForList() {
				public List exec() throws Exception {
					final byte[] buf = FileEx.readFully(fn);
					return B2Helper.toList(buf);
				}
			});
			return f;
		}

		public static final FutureTask<List> parseList(final URL url) {
			FutureTask<List> f = ForList.exec(new CallableForList() {
				public List exec() throws Exception {
					try (InputStream in = url.openStream();) {
						final byte[] buf = FileEx.readFully(in);
						return B2Helper.toList(buf);
					}
				}
			});
			return f;
		}

		public static final FutureTask<List> parseList(
				final ExecutorService es, final URL url) {
			FutureTask<List> f = ForList.exec(es, new CallableForList() {
				public List exec() throws Exception {
					try (InputStream in = url.openStream();) {
						final byte[] buf = FileEx.readFully(in);
						return B2Helper.toList(buf);
					}
				}
			});
			return f;
		}
	}

	public static final class ToHttp {
		public static final FutureTask<byte[]> readUrl(final URL url) {
			FutureTask<byte[]> f = ForBytes.exec(new CallableForBytes() {
				public byte[] exec() throws Exception {
					return HttpEx.readUrl(url);
				}
			});
			return f;
		}

		public static final FutureTask<byte[]> readUrl(
				final ExecutorService es, final URL url) {
			FutureTask<byte[]> f = ForBytes.exec(es, new CallableForBytes() {
				public byte[] exec() throws Exception {
					return HttpEx.readUrl(url);
				}
			});
			return f;
		}

		public static final FutureTask<String> readUrl(final URL url,
				final Charset charset) {
			FutureTask<String> f = ForString.exec(new CallableForString() {
				public String exec() throws Exception {
					byte[] buf = HttpEx.readUrl(url);
					if (ByteEx.isEmpty(buf))
						return "";
					return new String(buf, charset);
				}
			});
			return f;
		}

		public static final FutureTask<String> readUrl(
				final ExecutorService es, final URL url, final Charset charset) {
			FutureTask<String> f = ForString.exec(es, new CallableForString() {
				public String exec() throws Exception {
					byte[] buf = HttpEx.readUrl(url);
					if (ByteEx.isEmpty(buf))
						return "";
					return new String(buf, charset);
				}
			});
			return f;
		}

	}

	public static final void execute(final Runnable r) {
		ExecutorService es = getThreadPool();
		execute(es, r);
	}

	public static final void execute(final ExecutorService es, final Runnable r) {
		es.execute(r);
	}

	public static final Future exec(Runnable r) {
		ExecutorService es = getThreadPool();
		Future f = exec(es, r);
		return f;
		// es.execute(r);
	}

	public static final Future exec(final ExecutorService es, final Runnable r) {
		Future f = es.submit(r);
		return f;
	}

	public static final Future exec(final Runnable r,
			final Queue<Future> outQueue) {
		Future f = exec(r);
		if (outQueue != null)
			outQueue.add(f);
		// es.execute(r);
		return f;
	}

	public static final <T> Future<T> exec(final Callable<T> task) {
		ExecutorService es = getThreadPool();
		Future<T> f = exec(es, task);
		return f;
	}

	public static final <T> Future<T> exec(final ExecutorService es,
			final Callable<T> task) {
		Future<T> f = es.submit(task);
		return f;
	}

	public static final <T> Future<T> exec(final Callable<T> task,
			final Queue<Future> outQueue) {
		Future<T> f = exec(task);
		if (outQueue != null)
			outQueue.add(f);
		return f;
	}

	public static final Queue<Future> newQueue() {
		return new LinkedList<Future>();
	}

	public static final void waitDone(final Queue<Future> outQueue)
			throws InterruptedException, ExecutionException {
		if (outQueue == null || outQueue.isEmpty())
			return;
		while (!outQueue.isEmpty()) {
			Future f = outQueue.poll();
			f.get();
		}
	}

	// ///////////////////////////////////////
	// fork/join
	static ForkJoinPool FORK_JOIN_POOL;

	static final ForkJoinPool getForkJoinPool() {
		if (FORK_JOIN_POOL == null)
			FORK_JOIN_POOL = new ForkJoinPool(32);

		return FORK_JOIN_POOL;
	}

	public static final ForkJoinPool getForkJoinPool(int num) {
		return new ForkJoinPool(num);
	}

	public static final <T> ForkJoinTask<T> fork(final ForkJoinTask<T> task) {
		ForkJoinPool fjp = getForkJoinPool();
		ForkJoinTask<T> f = fork(fjp, task);
		return f;
	}

	public static final <T> ForkJoinTask<T> fork(final ForkJoinPool fjp,
			final ForkJoinTask<T> task) {
		if (fjp == null || task == null)
			return null;
		return fjp.submit(task);
	}

	public static final <T> ForkJoinTask<T> fork(final ForkJoinTask<T> task,
			final Queue<ForkJoinTask> outQueue) {
		ForkJoinTask<T> f = fork(task);
		if (outQueue != null)
			outQueue.add(f);
		return f;
	}

	public static final <T> ForkJoinTask<T> fork(final ForkJoinPool fjp,
			final ForkJoinTask<T> task, final Queue<ForkJoinTask> outQueue) {
		ForkJoinTask<T> f = fork(fjp, task);
		if (outQueue != null)
			outQueue.add(f);
		return f;
	}

	// ////////////
	public static final <T> ForkJoinTask<T> fork(final RecursiveTask<T> task) {
		ForkJoinPool fjp = getForkJoinPool();
		ForkJoinTask<T> f = fork(fjp, task);
		return f;
	}

	public static final <T> ForkJoinTask<T> fork(final ForkJoinPool fjp,
			final RecursiveTask<T> task) {
		ForkJoinTask<T> f = fjp.submit(task);
		return f;
	}

	public static final <T> ForkJoinTask<T> fork(final RecursiveTask<T> task,
			final Queue<ForkJoinTask> outQueue) {
		ForkJoinTask<T> f = fork(task);
		if (outQueue != null)
			outQueue.add(f);
		return f;
	}

	public static final <T> ForkJoinTask<T> fork(final ForkJoinPool fjp,
			final RecursiveTask<T> task, final Queue<ForkJoinTask> outQueue) {
		ForkJoinTask<T> f = fork(fjp, task);
		if (outQueue != null)
			outQueue.add(f);
		return f;
	}

	public static final Queue<ForkJoinTask> newTaskQueue() {
		return new LinkedList<ForkJoinTask>();
	}

	public static final void waitForks(final Queue<ForkJoinTask> outQueue)
			throws InterruptedException, ExecutionException {
		if (outQueue == null || outQueue.isEmpty())
			return;
		while (!outQueue.isEmpty()) {
			ForkJoinTask f = outQueue.poll();
			f.get();
		}
	}

	public static final Exception findForksExcept(
			final Queue<ForkJoinTask<Exception>> outQueue)
			throws InterruptedException, ExecutionException {
		if (outQueue == null || outQueue.isEmpty())
			return null;
		while (!outQueue.isEmpty()) {
			ForkJoinTask<Exception> f = outQueue.poll();
			Exception e = f.join();
			if (e != null)
				return e;
		}
		return null;
	}

}
