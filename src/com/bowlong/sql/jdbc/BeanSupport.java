package com.bowlong.sql.jdbc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bowlong.Toolkit;
import com.bowlong.bio2.B2OutputStream;
import com.bowlong.io.ByteOutStream;
import com.bowlong.json.MyJson;
import com.bowlong.lang.PStr;
import com.bowlong.objpool.ByteOutPool;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.pinyin.PinYin;
import com.bowlong.util.MapEx;
import com.bowlong.util.NewMap;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class BeanSupport implements ResultSetHandler, Cloneable,
		Serializable {

	private static final long serialVersionUID = 1L;

	public static final Log getLog(Class<?> clazz) {
		return LogFactory.getLog(clazz);
	}

	public <T> T handle(ResultSet rs) throws SQLException {
		return createFor(rs);
	}

	private NewMap ex;

	public Map ex() {
		return ex = (ex == null) ? new NewMap() : ex;
	}

	public Object putEx(Object key, Object val) {
		return ex().put(key, val);
	}

	public boolean getExBoolean(Object key) {
		return MapEx.getBoolean(ex(), key);
	}

	public int getExInt(Object key) {
		return MapEx.getInt(ex(), key);
	}

	public long getExLong(Object key) {
		return MapEx.getLong(ex(), key);
	}

	public double getExDouble(Object key) {
		return MapEx.getDouble(ex(), key);
	}

	public String getExString(Object key) {
		return MapEx.getString(ex(), key);
	}

	// key:字段 value:old value
	private Map<String, Object> _update = new Hashtable<>();

	public final List<String> changedFields() {
		List<String> r2 = new ArrayList();
		synchronized (_update) {
			r2.addAll(_update.keySet());
			return r2;
		}
	}

	public final boolean inChanged(String... fields) {
		synchronized (_update) {
			for (String f : fields) {
				if (_update.containsKey(f))
					return true;
			}
			return false;
		}
	}

	public final void reset() {
		synchronized (_update) {
			_update.clear();
		}
	}

	public final Object oldVal(String filedCn) {
		synchronized (_update) {
			return _update.get(filedCn);
		}
	}

	public final Object oldOrNew(String filedCn) {
		synchronized (_update) {
			Object obj = _update.get(filedCn);
			if (obj == null)
				return vZh(filedCn);

			return null;
		}
	}

	public final boolean isModify() {
		return (_update != null && !_update.isEmpty());
	}

	public final void changeIt(String str, Object oldValue) {
		if (str == null || str.isEmpty())
			return;
		synchronized (_update) {
			Object val = _update.get(str);
			if (val == null) { // 非空则添加
				_update.put(str, oldValue);
			} else {
				if (!val.equals(oldValue)) // 不空，如果是新值才更改
					_update.put(str, oldValue);
			}
		}
	}

	public final void changeIt(String str, Object oldValue, Object newValue) {
		changeIt(str, oldValue);
		// doEvent(str, oldValue, newValue);
	}

	public final String ustr() {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			List<String> vars = new Vector<String>();
			synchronized (_update) {
				vars.addAll(_update.keySet());
			}
			for (String str : vars) {
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(str);
				sb.append("=:");
				sb.append(str);
			}
			reset();
			return sb.toString();
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public <T> T createFor(ResultSet rs) throws SQLException {
		return null;
	}

	public Map toBasicMap() {
		return new HashMap();
	}

	public abstract <T> T vZh(String fieldZh);

	public abstract Map toMap();

	public abstract Map toOriginalMap();

	// 当有事件的时候处理,并不强制实现
	// public void doEvent(String key, Object oldValue, Object newValue) {
	// };

	// public String toString() {
	// return toMap().toString();
	// }

	// 指定某字段进行JSON解析
	private Map<String, Object> jsonCache = null;

	private Object jsonCache(String fieldEn) {
		if (jsonCache == null)
			jsonCache = new HashMap<String, Object>();
		String value = valueStr(fieldEn);
		if (value == null)
			return null;

		String s = value;
		Object result = jsonCache.get(s);
		if (result != null)
			return result;

		try {
			result = MyJson.parse(s);
			if (result == null)
				return null;
			if (result instanceof String)
				return null;

			jsonCache.put(fieldEn, result);
		} catch (IOException e1) {
			// e1.printStackTrace();
		}
		return result;
	}

	private Object jsonNoCache(String fieldEn) {
		String value = valueStr(fieldEn);
		if (value == null)
			return null;
		try {
			return MyJson.parse(value);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}

	public Map jsonMap(String fieldEn) {
		return jsonMap(fieldEn, true);
	}

	public Map jsonMap(String fieldEn, boolean bcache) {
		Object result = null;
		if (bcache)
			result = jsonCache(fieldEn);
		else
			result = jsonNoCache(fieldEn);

		if (result != null)
			return (Map) result;
		return null;
	}

	public List jsonList(String fieldEn) {
		return jsonList(fieldEn, true);
	}

	public List jsonList(String fieldEn, boolean bcache) {
		Object result = null;
		if (bcache)
			result = jsonCache(fieldEn);
		else
			result = jsonNoCache(fieldEn);

		if (result != null)
			return (List) result;
		return null;
	}

	public Map jsonZhMap(String fieldZh) {
		return jsonZhMap(fieldZh, true);
	}

	public Map jsonZhMap(String fieldZh, boolean bcache) {
		String fieldEn = PinYin.getShortPinYin(fieldZh);
		return jsonMap(fieldEn, bcache);
	}

	public List jsonZhList(String fieldZh) {
		return jsonZhList(fieldZh, true);
	}

	public List jsonZhList(String fieldZh, boolean bcache) {
		String fieldEn = PinYin.getShortPinYin(fieldZh);
		return jsonList(fieldEn, bcache);
	}

	// public abstract String valueStr(String fieldEn);
	public String valueStr(String fieldEn) {
		return null;
	};

	public String toString() {
		return toOriginalMap().toString();
	}

	// //////////////////////////////////////////////////////////
	public Object _primaryKey() {
		return 0;
	}

	public String _tableId() {
		return "";
	}

	public String _key() {
		return PStr.b(_tableId()).a("-").e(_primaryKey());
	}

	public abstract <T> T insert();

	public abstract <T> T insert2();

	public abstract <T> T update();

	public abstract <T> T insertOrUpdate();

	public abstract int delete();

	public abstract <T> T clone2();

	public abstract boolean asyncUpdate();

	// //////////////////////////////////////////////////////////

	public static final int compareTo(Object v1, Object v2) {
		return Toolkit.compareTo(v1, v2);
	}

	public void setTimeout(ScheduledExecutorService ses, Runner r, long delay) {
		if (ses == null || r == null)
			return;
		r.runNow();
		ses.schedule(r, delay, TimeUnit.MILLISECONDS);
	}

	public void setTimeout(ScheduledExecutorService ses, Runner r, Date dat) {
		if (ses == null || r == null || dat == null)
			return;
		long now = System.currentTimeMillis();
		long t1 = dat.getTime();
		long delay = t1 - now;
		delay = delay < 0 ? 1 : delay;
		setTimeout(ses, r, delay);
	}

	// //////////////
	public static final ByteOutStream getStream() {
		ByteOutStream baos = ByteOutPool.borrowObject();
		return baos;
	}

	public static final void freeStream(final ByteOutStream os) {
		ByteOutPool.returnObject(os);
	}

	// //////////////
	public void writeMapTag(OutputStream out, int len) throws Exception {
		if (out == null)
			return;
		B2OutputStream.writeMapTag(out, len);
	}

	public static final void writeMapEntry(OutputStream out, Object key,
			Object var) throws Exception {
		if (out == null || key == null)
			return;
		B2OutputStream.writeMapEntry(out, key, var);
	}

	// //////////////
	public boolean isThis(String str) {
		return false;
	}

	public boolean isThis(Map originalMap) {
		return false;
	}
}
