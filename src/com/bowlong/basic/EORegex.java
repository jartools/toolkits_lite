package com.bowlong.basic;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 * 
 * @author Canyon
 *
 */
public class EORegex extends EOJson {
	// 是否有匹配
	static final public boolean isMatch(String str, String regx) {
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	// 查找以指定字符开始，并以指定字符结尾的数据集
	static final public List<String> matchBeginAndEnd(String str, String begin, String end) {
		String regx = begin + "[\\s\\S]*?" + end;
		List<String> ret = newListT();
		int start = 0;
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(str);
		while (m.find(start)) {
			String item = m.group();
			start = m.end();
			ret.add(item);
		}
		return ret;
	}

	// 查找以指定字符开始，并以指定字符结尾的数据集，并且包含指定字符串
	static final public List<String> matchBeginAndEndAndInc(String str, String begin, String end, String inc) {
		String regx = begin + "[\\s\\S]*" + inc + "[\\s\\S]*?" + end;
		List<String> ret = newListT();
		int start = 0;
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(str);
		while (m.find(start)) {
			String item = m.group();
			start = m.end();
			ret.add(item);
		}
		return ret;
	}

	// 只能输入n位的数字
	static final public boolean matchNum(String str, int nbegn, int nend) {
		String regx = null;
		if (nbegn >= 0) {
			if (nend > nbegn)
				regx = "^\\d{" + nbegn + "," + nend + "}$";
			else if (nend == 0)
				regx = "^\\d{" + nbegn + ",}$";
			else
				regx = "^\\d{" + nbegn + "}$";
		} else {
			regx = "^[0-9]*$";
		}
		return isMatch(str, regx);
	}

	// ????是否只有数字
	static final public boolean matchNum(String str) {
		return matchNum(str, -1, 0);
	}

	// 只能输入至少n位的数字
	static final public boolean matchNumThan(String str, int n) {
		return matchNum(str, n, 0);
	}

	// 只能输入m~n位的数字
	static final public boolean matchNumBetween(String str, int n1, int n2) {
		return matchNum(str, n1, n2);
	}

	// 只能输入零和非零????的数??
	static final public boolean matchNumBeginStr(String str) {
		return isMatch(str, "^(0|[1-9][\\s\\S]*)$");
	}

	static final public List<String> href(String str) {
		String regx = "href\\s*=\\s*(?:\"([^\"]*)\"|'([^']*)'|([^\"'>\\s]+))";
		List<String> ret = newListT();
		int start = 0;
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(str);
		while (m.find(start)) {
			String item = m.group();
			start = m.end();
			ret.add(item);
		}
		return ret;
	}

	static final public String htmlToText(String html) {
		String regx = "<[^>]+>";
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(html);
		return m.replaceAll("");
	}

	static final public boolean regexAnsi(String GID) {
		return isMatch(GID, "^\\w+$");
	}

	static final public boolean isContains(String src, String contains, boolean isBeg, boolean isEnd) {
		String mid = String.format("(%s)+", contains);
		String str2 = "(.)+"; // 必须有
		String str3 = "(.*)?"; // 可有可无
		return isMatch(src, (isBeg ? str2 : str3) + mid + (isEnd ? str2 : str3));
	}

	static final public boolean isContains(String src, String contains) {
		return isContains(src, contains, false, false);
	}

	static final public boolean isContains(String src, String contains, boolean isEnd) {
		return isContains(src, contains, false, isEnd);
	}
}
