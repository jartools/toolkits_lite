package com.bowlong.ses;

import java.io.Serializable;
import java.util.Map;

import com.bowlong.basic.ExToolkit;

/**
 * 管理 Session 会话对象
 * 
 * @author Canyon
 * @time 2014-06-15 09:30
 */
public class MgrSession extends ExToolkit implements Serializable {
	private static final long serialVersionUID = 1L;

	static final protected Map<Long, Session> mapSession = newMap();
	static final protected Map<String, Long> mapLg2Id = newMap();
	static final protected Map<Long, String> mapId2Lg = newMap();

	static final public Session getSession(long sesid) {
		if (mapSession.containsKey(sesid)) {
			return mapSession.get(sesid);
		}
		return null;
	}

	static final public Session getSession(String lgid, String lgpwd) {
		String vKey = String.format("%s_%s", lgid, lgpwd);
		long sKey = 0;
		if (mapLg2Id.containsKey(vKey)) {
			sKey = mapLg2Id.get(vKey);
		}
		return getSession(sKey);
	}

	static final public Session getSession(String oneKey) {
		return getSession(oneKey, "");
	}

	static final public boolean rmSession(long sesid) {
		if (mapId2Lg.containsKey(sesid)) {
			String vKey = mapId2Lg.get(sesid);
			mapLg2Id.remove(vKey);
			mapId2Lg.remove(sesid);
			mapSession.remove(sesid);
			return true;
		}
		return false;
	}

	static final public boolean rmSession(Session objSes) {
		if (objSes == null)
			return false;
		return rmSession(objSes.sesid);
	}

	static final public void addSession(String lgid, String lgpwd, Session objSes) {
		String vKey = String.format("%s_%s", lgid, lgpwd);
		long sesid = objSes.sesid;
		mapLg2Id.put(vKey, sesid);
		mapId2Lg.put(sesid, vKey);
		mapSession.put(sesid, objSes);
	}

	static final public void addSession(String oneKey, Session objSes) {
		addSession(oneKey, "", objSes);
	}
}
