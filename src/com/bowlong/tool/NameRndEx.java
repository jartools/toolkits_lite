package com.bowlong.tool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bowlong.io.FileRw;
import com.bowlong.lang.RndEx;

public class NameRndEx implements Serializable {
	private static final long serialVersionUID = 1L;
	static Log log = LogFactory.getLog(NameRndEx.class);

	private NameRndEx() {
	}

	private static NameRndEx _self = null;

	public static NameRndEx getInstance() {
		if (_self == null) {
			_self = new NameRndEx();
			_self.initDate();
		}
		return _self;
	}

	static public final int Laguage_Type_CNFT = 1;
	static public final int Laguage_Type_EN = 2;
	static public final int Laguage_Type_CN = 3;

	// 中文名字
	// 简体
	static final String Path_Zh_FirName = "names/zh_fir.txt";
	static final String Path_Zh_EndName = "names/zh_end.txt";

	List<String> zh_firname = new ArrayList<String>();
	List<String> zh_secname = new ArrayList<String>();
	List<String> zh_endname = new ArrayList<String>();

	// 繁体
	static final String Path_ZhFt_FirName = "names/zhft_fir.txt";
	static final String Path_ZhFt_EndName = "names/zhft_end.txt";
	List<String> zhft_fir = new ArrayList<String>();
	List<String> zhft_sec = new ArrayList<String>();
	List<String> zhft_end = new ArrayList<String>();

	// 英文名字
	static final String Path_En_FirName = "names/en_fir.txt";
	static final String Path_En_EndName = "names/en_end.txt";

	List<String> en_firname = new ArrayList<String>();
	// List<String> en_secname = new ArrayList<String>();
	List<String> en_endname = new ArrayList<String>();

	void initDate() {
		try {
			System.out.println("======名字加载 begin=======");
			Names.init(false, Path_Zh_FirName, Path_Zh_EndName, zh_firname,
					zh_secname, zh_endname);
			Names.init(false, Path_ZhFt_FirName, Path_ZhFt_EndName, zhft_fir,
					zhft_sec, zhft_end);
			Names.init(true, Path_En_FirName, Path_En_EndName, en_firname,
					null, en_endname);
			System.out.println("======名字加载 end=======");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void initNames() {
		getInstance();
	}

	private String newNameByEn() {
		StringBuilder build = new StringBuilder();
		int lenFir = 0;
		// int lenSec = 0;
		int lenEnd = 0;
		int index = 0;
		String strFir = "";
		String strSec = "";
		String strEnd = "";
		strSec = ".";
		lenFir = en_firname.size();
		lenEnd = en_endname.size();
		index = RndEx.nextInt(lenFir);
		strFir = en_firname.get(index);
		index = RndEx.nextInt(lenEnd);
		strEnd = en_endname.get(index);
		build.append(strFir).append(strSec).append(strEnd);
		String r = build.toString();
		return r;
	}

	private String newNameByZh() {
		StringBuilder build = new StringBuilder();
		int lenFir = 0;
		int lenSec = 0;
		int lenEnd = 0;
		int index = 0;
		String strFir = "";
		String strSec = "";
		String strEnd = "";

		int randK = RndEx.nextInt(1000);

		lenFir = zh_firname.size();
		index = RndEx.nextInt(lenFir);
		strFir = zh_firname.get(index);

		if (randK < 750) {
			lenSec = zh_secname.size();
			index = RndEx.nextInt(lenSec);
			strSec = zh_secname.get(index);
		}

		if (randK > 400) {
			lenEnd = zh_endname.size();
			index = RndEx.nextInt(lenEnd);
			strEnd = zh_endname.get(index);
		}

		build.append(strFir).append(strSec).append(strEnd);
		String r = build.toString();
		return r;
	}

	private String newNameByZhFt() {
		StringBuilder build = new StringBuilder();
		int lenFir = 0;
		int lenSec = 0;
		int lenEnd = 0;
		int index = 0;
		String strFir = "";
		String strSec = "";
		String strEnd = "";

		int randK = RndEx.nextInt(1000);

		lenFir = zhft_fir.size();
		index = RndEx.nextInt(lenFir);
		strFir = zhft_fir.get(index);

		if (randK < 750) {
			lenSec = zhft_sec.size();
			index = RndEx.nextInt(lenSec);
			strSec = zhft_sec.get(index);
		}

		if (randK > 400) {
			lenEnd = zhft_end.size();
			index = RndEx.nextInt(lenEnd);
			strEnd = zhft_end.get(index);
		}

		build.append(strFir).append(strSec).append(strEnd);
		String r = build.toString();
		return r;
	}

	static public String newName(int type) {
		return getInstance().newRndName(type);
	}

	public String newNameCn() {
		return newName(Laguage_Type_CN);
	}

	public String newRndName(int type) {
		switch (type) {
		case Laguage_Type_CNFT:
			return newNameByZhFt();
		case Laguage_Type_EN:
			return newNameByEn();
		default:
			return newNameByZh();
		}
	}

	private static class Names implements Serializable {

		private static final long serialVersionUID = 1L;

		static private List<String> sortStrByMap(Map<String, String> map) {
			List<String> r = new ArrayList<String>();
			if (map == null || map.isEmpty())
				return r;
			Set<String> keys = map.keySet();
			r.addAll(keys);
			r = sortStrByList(r);
			return r;
		}

		static private List<String> sortStrByList(List<String> r) {
			if (r == null || r.isEmpty())
				return new ArrayList<String>();
			Collections.sort(r, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					int len1 = o1.length();
					int len2 = o2.length();
					if (len1 == len2) {
						return o1.compareTo(o2);
					} else if (len1 > len2) {
						return 1;
					} else if (len1 < len2) {
						return -1;
					}
					return 0;
				}
			});
			return r;
		}

		static public void init(boolean isEn, String pathBeg, String pathEnd,
				List<String> first, List<String> second, List<String> three) {
			// ============ 第一个名字
			String fisrtName = FileRw.readStr(pathBeg);
			String[] fnes = fisrtName.split("\r");
			Map<String, String> map = new HashMap<String, String>();
			for (String str : fnes) {
				if (str == null)
					continue;
				str = str.trim();
				if ("".equals(str) || "\r".equals(str))
					continue;
				// System.out.println("[" + str + "]");
				map.put(str, str);
			}
			List<String> sortListFirst = sortStrByMap(map);
			if (first != null)
				first.addAll(sortListFirst);

			// ============ 第二,三个名字
			// System.out.println("============ 第二,三中文个名字================");
			String secthame = FileRw.readStr(pathEnd);
			String[] sntes = secthame.split("\r");

			Map<String, String> secmap = new HashMap<String, String>();
			Map<String, String> thrmap = new HashMap<String, String>();
			String strSec = "";
			String strThr = "";
			for (String str : sntes) {
				if (str == null)
					continue;
				str = str.trim();
				if ("".equals(str) || "\r".equals(str))
					continue;
				// System.out.println("[" + str + "]");
				if (isEn) {
					thrmap.put(str, str);
				} else {
					int len = str.length();
					if (len >= 2) {
						strSec = str.substring(0, 1);
						strThr = str.substring(1);
						secmap.put(strSec, strSec);
						thrmap.put(strThr, strThr);
					} else {
						secmap.put(str, str);
					}
				}
			}

			List<String> sortListSec = sortStrByMap(secmap);
			if (second != null)
				second.addAll(sortListSec);

			List<String> sortListEnd = sortStrByMap(thrmap);
			if (three != null)
				three.addAll(sortListEnd);
		}
	}
}