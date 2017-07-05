package com.bowlong.text.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import com.bowlong.text.Encoding;

/**
 * 屏蔽字过滤
 * 
 * @author Huayco
 */
public class FilterMackWord {

	/** 是否严格模式，即“习 近 平”包含空格的屏蔽字是否也被处理 */
	public static boolean IS_STAICT_MODE = false;

	/** 替换字星星数组 */
	static String[] replaces = null;

	/** 严格模式分隔符 */
	static NoConflictHashMapForChar splitter = new NoConflictHashMapForChar();

	static TrieFilter empty = new TrieFilter();
	private static TrieFilter root = new TrieFilter();

	/**
	 * 
	 * @param path
	 *            "fonts/dritywordspliter.txt"
	 * @param mackPath
	 *            "fonts/dirtyword.txt"
	 */
	static public void init(String path, String mackPath) {
		File f = new File(path);
		if (f.exists()) {
			// 加载严格模式分隔符
			try (LineNumberReader lr = new LineNumberReader(
					new InputStreamReader(new FileInputStream(f), Encoding.UTF8));) {
				String line = null;
				while ((line = lr.readLine()) != null) {
					line = line.toUpperCase();
					if (line.length() == 1) {
						if (!splitter.containsKey(line.charAt(0)))
							splitter.put(line.charAt(0), empty);
					} else {
						System.out.println("异常分隔符:" + line + ", 行数："
								+ lr.getLineNumber());
					}
				}
				lr.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		File ff = new File(mackPath);
		if (!ff.exists())
			return;
		// 加载屏蔽字
		try (LineNumberReader lr = new LineNumberReader(new InputStreamReader(
				new FileInputStream(ff), Encoding.UTF8));) {
			List<String> keyWords = new ArrayList<String>();
			String line = null;
			int maxLength = 0;
			while ((line = lr.readLine()) != null) {
				line = line.toUpperCase();
				if (line.length() > 0) {
					keyWords.add(line);
					if (maxLength < line.length()) {
						maxLength = line.length();
					}
				} else {
					System.out.println("异常屏蔽字：" + line + ", 行数："
							+ lr.getLineNumber());
				}
			}
			System.out.println("屏蔽字数量=" + lr.getLineNumber());
			for (int i = 0; i < keyWords.size(); i++) {
				root.AddKey(keyWords.get(i));
			}
			replaces = new String[maxLength + 1];
			StringBuffer sb = new StringBuffer(40);
			for (int i = 0; i < replaces.length; i++) {
				for (int j = 0; j <= i; j++) {
					sb.append("*");
				}
				replaces[i] = sb.toString();
				sb.delete(0, sb.length());
			}
			lr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检查指定字符串是否包含屏蔽字
	 * 
	 * @param text
	 *            要检查的字符串
	 * @return 返回第一个找到的屏蔽字，没有则返回null
	 */
	public static String findOne(String text) {
		text = text.toUpperCase();
		TrieFilter node;
		for (int i = 0; i < text.length(); i++) {
			if ((node = root.m_values.get(text.charAt(i))) != null) {
				for (int j = i + 1; j < text.length(); j++) {
					if ((node = node.m_values.get(text.charAt(j))) != null) {
						if (node.m_values.containsKey('\0')) {
							return text.substring(i, j + 1);
						}
					} else {
						break;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 检查指定字符串是否包含屏蔽字，是则替换成相应长度的*号
	 * 
	 * @param text
	 *            要检查的字符串
	 * @return 返回经过过滤，不包含屏蔽字的字符串
	 */
	public static String findAndReplace(String text) {
		text = text.toUpperCase();
		TrieFilter node;
		TrieFilter node2;
		for (int i = 0; i < text.length(); i++) {
			if ((node = root.m_values.get(text.charAt(i))) != null) {
				int end = 0;
				if (node.m_values.containsKey('\0')) { // 碰到敏感词
					end = i + 1;
				}
				for (int j = i + 1; j < text.length(); j++) {
					if ((node2 = node.m_values.get(text.charAt(j))) != null) {
						node = node2;
						if (node.m_values.containsKey('\0')) {
							end = j + 1;
						}
					} else if (IS_STAICT_MODE
							&& FilterMackWord.splitter.containsKey(text
									.charAt(j))) {
						if (node.m_values.containsKey('\0')) {
							end = j + 1;
						}
						continue;
					} else {
						break;
					}
				}
				if (end > 0) {
					String key = text.substring(i, end); // 截取出屏蔽字
					int l = key.length() + 1 > replaces.length ? replaces.length - 1
							: key.length() - 1; // 计算替换字*号长度
					text = replace(text, key, replaces[l]); // 将屏蔽字替换成相应长度的*号
					i = end
							- 1
							- (key.length() > replaces.length ? (key.length()
									- replaces.length + 1) : 0);
				}
			}
		}
		return text;
	}

	/**
	 * 高效的字符串替换
	 */
	private static String replace(String strSource, String strFrom, String strTo) {
		if (strSource == null) {
			return null;
		}
		int i = 0;
		if ((i = strSource.indexOf(strFrom, i)) >= 0) {
			char[] cSrc = strSource.toCharArray();
			char[] cTo = strTo.toCharArray();
			int len = strFrom.length();
			StringBuffer buf = new StringBuffer(cSrc.length);
			buf.append(cSrc, 0, i).append(cTo);
			i += len;
			int j = i;
			while ((i = strSource.indexOf(strFrom, i)) > 0) {
				buf.append(cSrc, j, i - j).append(cTo);
				i += len;
				j = i;
			}
			buf.append(cSrc, j, cSrc.length - j);
			return buf.toString();
		}
		return strSource;
	}

	public static void main(String[] args) {
		String path = "fonts/dritywordspliter.txt";
		String mackPath = "fonts/dirtyword.txt";
		init(path, mackPath);
		IS_STAICT_MODE = true;
		String text = "免 费 淫 色 情 电 影 成 人@午 夜 场@影院";
		String v = findOne(text);
		System.out.println(v);
		v = findAndReplace(text);
		System.out.println(v);
	}
}

/**
 * @author Huayco
 *         byte注：m_values使用NoConflictHashMapForChar是空间换时间的做法，使用HashMap也可以，
 *         但是查找速度比NoConflictHashMapForChar降一半。
 *         数据：13388个屏蔽字，NoConflictHashMapForChar占4061K，HashMap占2406K
 */
class TrieFilter {

	public NoConflictHashMapForChar m_values;

	// public HashMap<Character, TrieFilter> m_values;

	public TrieFilter() {
		m_values = new NoConflictHashMapForChar();
		// m_values = new HashMap<Character, TrieFilter>();
	}

	/**
	 * 添加屏蔽字
	 * 
	 * @param key
	 */
	public void AddKey(String key) {
		if (key == null || key.length() == 0) {
			return;
		}
		TrieFilter node = this; // 根节点
		for (int i = 0; i < key.length(); i++) {
			if (FilterMackWord.splitter.containsKey(key.charAt(i))) {
				continue;
			}
			TrieFilter subnode;
			if ((subnode = node.m_values.get(key.charAt(i))) == null) {
				subnode = new TrieFilter();
				node.m_values.put(key.charAt(i), subnode);
			}
			node = subnode;
		}
		// 最后的结点，表示单词结束，用‘\0’表示，指向一个空对象
		node.m_values.put('\0', null);
	}
}
