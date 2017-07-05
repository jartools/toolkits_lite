package com.bowlong.tool;

import java.util.regex.Pattern;

import com.bowlong.idcard.IdcardValidator;
import com.bowlong.lang.StrEx;

public class TkitValidateCheck {

	// 验证手机号
	static final Pattern p4Mobile = Pattern.compile("^[1][3,4,5,8][0-9]{9}$");
	// 验证带区号的电话
	static final Pattern p4Phone1 = Pattern
			.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");
	// 验证没有区号的电话
	static final Pattern p4Phone2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");
	// 验证邮件
	static final Pattern p4Email = Pattern
			.compile("^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$");

	// 判断小数点后一位的数字的正则表达式
	static final Pattern p4Number = Pattern
			.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");

	/*** 手机号验证 **/
	static public final boolean isMobile(String str) {
		if (StrEx.isEmptyTrim(str))
			return false;
		return p4Mobile.matcher(str).matches();
	}

	/*** 电话号码验证 **/
	static public final boolean isPhone(String str) {
		if (StrEx.isEmptyTrim(str))
			return false;
		if (str.length() > 9) {
			return p4Phone1.matcher(str).matches();
		} else {
			return p4Phone2.matcher(str).matches();
		}
	}

	/*** 邮箱验证 **/
	static public final boolean isEmail2(String email) {
		boolean isEmail = StrEx.isEmailAddr(email);
		if (!isEmail) {
			return false;
		}
		return p4Email.matcher(email).matches();
	}

	/*** 邮箱验证 **/
	static public final boolean isEmail(String email) {
		boolean isEmail = StrEx.isEmailAddr(email);
		if (!isEmail) {
			return false;
		}
		return StrEx.isValidEmailAddress(email);
	}

	/*** 日期格式:yyyy-mm-dd验证 **/
	static public final boolean isValidDate(String sDate) {
		if (StrEx.isEmptyTrim(sDate))
			return false;
		String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
		String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
				+ "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
				+ "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
				+ "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		if ((sDate != null)) {
			Pattern pattern = Pattern.compile(datePattern1);
			if (pattern.matcher(sDate).matches()) {
				pattern = Pattern.compile(datePattern2);
				return pattern.matcher(sDate).matches();
			} else {
				return false;
			}
		}
		return false;
	}

	/*** 数量Number验证 **/
	static public final boolean isNumber(String str) {
		if (StrEx.isEmptyTrim(str))
			return false;
		return p4Number.matcher(str).matches();
	}

	/*** 身份证验证 **/
	static public final boolean isIDCard(String str) {
		if (StrEx.isEmptyTrim(str))
			return false;
		return IdcardValidator.idCard.isValidatedAllIdcard(str);
	}

}
