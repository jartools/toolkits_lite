package com.bowlong.security;

import com.bowlong.lang.NumEx;
import com.bowlong.lang.StrEx;

/*** 自定义加密，解密工具 **/
public class EncDecoder {

	static final String Flag_Add = "+";
	static final String Flag_Redues = "-";

	static private String trimIntZero(String nStr) {
		if (nStr == null || "".equals(nStr))
			return "";
		String flag = StrEx.left(nStr, 1);
		String tmpStr = nStr;
		if (flag == Flag_Add || flag == Flag_Redues) {
			tmpStr = nStr.substring(1);
		} else {
			flag = "";
		}
		int len = tmpStr.length();
		int index = 0;
		for (int i = 0; i < len; i++) {
			if (!"0".equals(tmpStr.substring(i, i + 1))) {
				break;
			}
			index++;
		}

		return flag + tmpStr.substring(index);
	}

	static String nStrForLen(int n, int len) {
		String r2 = "" + n;
		StringBuilder sb_ = new StringBuilder("");
		int size_ = r2.length();
		for (int i = 0; i < len - size_; i++) {
			sb_.append("0");
		}
		sb_.append(r2);
		r2 = sb_.toString();
		return r2;
	}

	/*** nu1 and nu2.大数相加 **/
	static String bgnuadd(String nu1, String nu2) {
		if ((nu2 == null || "".equals(nu2)))
			return nu1;
		if ((nu1 == null || "".equals(nu1)))
			return nu2;
		String result = "";
		int fn1 = nu1.indexOf(Flag_Redues);
		int fn2 = nu2.indexOf(Flag_Redues);
		// 'nu1为负、nu2为正
		if (fn1 == 0 && fn2 != 0) {
			result = bignumbersubduct(nu2, nu1.substring(1));
			// 'nu1为负、nu2为负
		} else if (fn1 == 0 && fn2 == 0) {
			result = Flag_Redues
					+ bignumberadditive(nu1.substring(1), nu2.substring(1));
			// 'nu1为正、nu2为正
		} else if (fn1 != 0 && fn2 != 0) {
			result = bignumberadditive(nu1, nu2);
			// 'nu1为正、nu2为负
		} else if (fn1 != 0 && fn2 == 0) {
			result = bignumbersubduct(nu1, nu2.substring(1));
		}
		return result;
	}

	/*** nu1 and nu2.大数相减 **/
	static public String bgnusub(String nu1, String nu2) {
		if ((nu2 == null || "".equals(nu2)))
			return nu1;
		if ((nu1 == null || "".equals(nu1)))
			return nu2;
		String result = "";
		int fn1 = nu1.indexOf(Flag_Redues);
		int fn2 = nu2.indexOf(Flag_Redues);
		// 'nu1为负、nu2为正
		if (fn1 == 0 && fn2 != 0) {
			result = Flag_Redues + bignumberadditive(nu1.substring(1), nu2);
			// 'nu1为负、nu2为负
		} else if (fn1 == 0 && fn2 == 0) {
			result = bignumbersubduct(nu2.substring(1), nu1.substring(1));
			// 'nu1为正、nu2为正
		} else if (fn1 != 0 && fn2 != 0) {
			result = bignumbersubduct(nu1, nu2);
			// 'nu1为正、nu2为负
		} else if (fn1 != 0 && fn2 == 0) {
			result = bignumberadditive(nu1, nu2.substring(1));
		}
		return result;
	}

	/*** nu1 and nu2.大数相加，以4位长的数字分段计算，计算两个参数是不代符号的 **/
	static String bignumberadditive(String nu1, String nu2) {
		String result = "";
		String a = "";
		String b = "";
		int sizea = 0;
		int sizeb = 0;
		String tmpstr;
		int i = 0;
		a = nu1;
		b = nu2;

		if (a.length() < b.length()) {
			tmpstr = a;
			a = b;
			b = tmpstr;
		}
		if (a.length() % 4 == 0) {
			sizea = a.length() / 4;
		} else {
			sizea = a.length() / 4 + 1;
		}

		if (b.length() % 4 == 0) {
			sizeb = b.length() / 4;
		} else {
			sizeb = b.length() / 4 + 1;
		}
		String[] lista = new String[sizea];
		String[] tmpresult = new String[sizea];
		String[] listb = new String[sizeb];

		for (i = 0; i < sizea; i++) {
			if (a.length() > 4) {
				lista[i] = StrEx.right(a, 4);
				a = StrEx.left(a, a.length() - 4);
			} else {
				lista[i] = StrEx.right(a, a.length());
				a = StrEx.left(a, a.length());
			}
		}
		for (i = 0; i < sizeb; i++) {
			if (b.length() > 4) {
				listb[i] = StrEx.right(b, 4);
				b = StrEx.left(b, b.length() - 4);
			} else {
				listb[i] = StrEx.right(b, b.length());
				b = StrEx.left(b, b.length());
			}
		}

		for (i = 0; i < sizea; i++) {
			if (i < sizeb) {
				tmpresult[i] = ""
						+ (NumEx.stringToInt(lista[i]) + NumEx
								.stringToInt(listb[i]));
			} else {
				tmpresult[i] = lista[i];
			}
			if (i != 0) {
				if ((tmpresult[i - 1]).length() == 5) {
					tmpresult[i] = (NumEx.stringToInt(tmpresult[i]) + 1) + "";
				}
			}
			if (i != sizea - 1) {
				int tmpN = 0;
				if (tmpresult[i].length() >= 4) {
					tmpN = NumEx.stringToInt(StrEx.right(tmpresult[i], 4));
				} else {
					tmpN = NumEx.stringToInt(tmpresult[i]);
				}
				result = nStrForLen(tmpN, 4) + result;
			} else {
				result = tmpresult[i] + result;
			}
		}
		return result;
	}

	/*** nu1 and nu2.大数相减，以4位长的数字分段计算，计算两个参数是不代符号的 **/
	static String bignumbersubduct(String nu1, String nu2) {
		String result = "";
		String a;
		String b;
		String tmpstr;
		int sizea = 0;
		int sizeb = 0;

		int i = 0;
		String flag = "";
		a = nu1;
		b = nu2;
		if (a.length() < b.length()) {
			tmpstr = a;
			a = b;
			b = tmpstr;
			flag = Flag_Redues;
		} else if (a.length() == b.length()) {
			if (a.compareTo(b) == -1) {
				tmpstr = a;
				a = b;
				b = tmpstr;
				flag = Flag_Redues;
			}
		}
		if (a.length() % 4 == 0) {
			sizea = a.length() / 4;
		} else {
			sizea = a.length() / 4 + 1;
		}

		if (b.length() % 4 == 0) {
			sizeb = b.length() / 4;
		} else {
			sizeb = b.length() / 4 + 1;
		}
		String[] lista = new String[sizea];
		String[] tmpresult = new String[sizea];
		String[] listb = new String[sizeb];
		for (i = 0; i < sizea; i++) {
			if (a.length() > 4) {
				lista[i] = StrEx.right(a, 4);
				a = StrEx.left(a, a.length() - 4);
			} else {
				lista[i] = StrEx.right(a, a.length());
				a = StrEx.left(a, a.length());
			}
		}

		for (i = 0; i < sizeb; i++) {
			if (b.length() > 4) {
				listb[i] = StrEx.right(b, 4);
				b = StrEx.left(b, b.length() - 4);
			} else {
				listb[i] = StrEx.right(b, b.length());
				b = StrEx.left(b, b.length());
			}
		}
		for (i = 0; i < sizea; i++) {
			if (i < sizeb) {
				if (i != sizea - 1) {
					tmpresult[i] = (NumEx.stringToInt("1" + lista[i]) - NumEx
							.stringToInt(listb[i])) + "";
				} else {
					tmpresult[i] = (NumEx.stringToInt(lista[i]) - NumEx
							.stringToInt(listb[i])) + "";
				}
			} else {
				if (i != sizea - 1) {
					tmpresult[i] = "1" + lista[i];
				} else {
					tmpresult[i] = lista[i];
				}
			}
			if (i != 0) {
				if (tmpresult[i - 1].length() < 5) {
					tmpresult[i] = (NumEx.stringToInt(tmpresult[i]) - 1) + "";
				}
			}
			if (i != sizea - 1) {
				int tempN = 0;
				if (tmpresult[i].length() >= 4) {
					tempN = NumEx.stringToInt(StrEx.right(tmpresult[i], 4));
				} else {
					tempN = NumEx.stringToInt(tmpresult[i]);
				}

				result = nStrForLen(tempN, 4) + result;
			} else {
				result = tmpresult[i] + result;
			}
		}
		result = flag + result;
		return result;
	}

	/*** 加密 **/
	static public String encoderStr(String str, String scrtkey) {
		if ((str == null || "".equals(str)))
			return "";
		String unicodestr = "";
		String posstr = "";
		String tmpstr = "";
		String uniscrtkey = "";
		String ret = "";
		int i;
		int len_ = str.length();
		int[] poslist = new int[len_];
		for (i = 0; i < len_; i++) {
			unicodestr = unicodestr + (int) (str.charAt(i));
			poslist[i] = unicodestr.length();
		}
		for (i = 0; i < len_; i++) {
			int index_ = poslist[i];
			tmpstr = unicodestr.substring(index_ - 1, index_);
			unicodestr = tmpstr + StrEx.left(unicodestr, index_ - 1)
					+ unicodestr.substring(index_);
			posstr = posstr + nStrForLen(poslist[i], 4); // 每4位表示一个位置
		}
		for (i = 0; i < scrtkey.length(); i++) {
			uniscrtkey = uniscrtkey + (int) (scrtkey.charAt(i));
		}
		String flag = Flag_Add;
		posstr = trimIntZero(posstr);
		String sub = bgnusub(uniscrtkey, posstr);
		if ((sub != null) && sub.length() > 0 && sub.indexOf(Flag_Redues) == 0) {
			sub = sub.substring(1);
			flag = Flag_Redues;
		}
		// 每四位中把前面为0的用+号代表
		String enSub = "";
		int tmpN = 0;
		for (i = sub.length() - 4; i >= 0; i = i - 4) {
			tmpN = NumEx.stringToInt(sub.substring(i, i + 4));
			enSub = ((tmpN + "").length() < 4 ? Flag_Add : "") + tmpN + enSub;
		}
		if (i != -4) {
			tmpN = NumEx.stringToInt(StrEx.left(sub, i + 4));
			enSub = ((tmpN + "").length() < 4 ? Flag_Add : "") + tmpN + enSub;
		}

		ret = unicodestr + flag + enSub;
		return ret;
	}

	/*** 加密 **/
	static public String encoder(Object obj, String scrtkey) {
		if (obj == null)
			return null;
		return encoderStr(String.valueOf(obj), scrtkey);
	}

	/*** 解密 **/
	static public String decoder(String encodestr, String scrtkey) {
		if (encodestr == null || "".equals(encodestr) || scrtkey == null
				|| "".equals(scrtkey))
			return "";
		String result = "";
		String unicodestr = "";
		String posstr = "";
		String tmpstr = "";
		String uniscrtkey = "";
		int sizepos = 0;
		int i = 0;
		char splitChar = '-';
		int splitPos = encodestr.indexOf('-');
		if (splitPos < 0) {
			splitChar = '+';
			splitPos = encodestr.indexOf('+');
		}
		if (splitPos < 0)
			return "";
		unicodestr = StrEx.left(encodestr, splitPos);
		posstr = StrEx.right(encodestr, encodestr.length() - splitPos - 1);
		String[] ss = posstr.split("\\+");
		posstr = "";
		for (i = 0; i < ss.length; i++) {
			int j = 0;
			tmpstr = "";
			String strCur = ss[i];
			for (j = strCur.length() - 4; j >= 0; j = j - 4) {
				tmpstr = strCur.substring(j, j + 4) + tmpstr;
			}
			if (j != -4) {
				int tmpN = NumEx.stringToInt(strCur.substring(0, j + 4));
				tmpstr = nStrForLen(tmpN, 4) + tmpstr;
			}
			posstr += tmpstr;
		}
		// 去掉面前的0
		posstr = trimIntZero(posstr);
		if (splitChar == '-') {
			posstr = Flag_Redues + posstr;
		}
		for (i = 0; i < scrtkey.length(); i++) {
			uniscrtkey = uniscrtkey + (int) (scrtkey.charAt(i));
		}
		posstr = bgnusub(uniscrtkey, posstr);
		if (posstr.length() % 4 == 0) {
			sizepos = posstr.length() / 4;
		} else {
			sizepos = posstr.length() / 4 + 1;
		}
		int[] poslist = new int[sizepos];
		for (i = 0; i < sizepos; i++) {
			int tmpN = 0;
			if (posstr.length() >= 4) {
				tmpN = NumEx.stringToInt(StrEx.right(posstr, 4));
			} else {
				tmpN = NumEx.stringToInt(posstr);
			}
			if (tmpN == 0)
				break;
			poslist[i] = tmpN;
			if (posstr.length() > 4) {
				posstr = StrEx.left(posstr, posstr.length() - 4);
			}
		}
		sizepos = i;
		for (i = 0; i < sizepos; i++) {
			int tmpInt = poslist[i];
			String strLt = StrEx.left(unicodestr, tmpInt);
			unicodestr = strLt + unicodestr.substring(0, 1)
					+ unicodestr.substring(tmpInt);
			unicodestr = unicodestr.substring(1);
		}
		for (i = 0; i < sizepos; i++) {
			if (i != sizepos - 1) {
				result = (char) (NumEx.stringToInt(unicodestr.substring(
						poslist[i + 1], poslist[i]))) + result;
			} else {
				result = (char) (NumEx.stringToInt(unicodestr.substring(0,
						poslist[i]))) + result;
			}
		}
		return result;
	}

	static public void main(String[] args) {
		long now_time = System.currentTimeMillis();
		System.out.println(now_time);
		String appKey = "coolape";
		String sign = EncDecoder.encoder(now_time, appKey);
		System.out.println(sign);
		System.out.println(sign.length());
		String v = EncDecoder.decoder(sign, appKey);
		System.out.println(v);
		sign = sign.replaceAll("\\+", "a");
		sign = sign.replaceAll("\\-", "r");
		System.out.println(sign);
		System.out.println(sign.length());
	}
}
