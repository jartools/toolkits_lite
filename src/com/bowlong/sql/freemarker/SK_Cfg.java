package com.bowlong.sql.freemarker;

import java.util.List;

/**
 * @UserName : SandKing
 * @DataTime : 2014年4月24日 上午12:27:47
 * @Description ：Please describe this document
 */
public class SK_Cfg {
	private String className;

	private String packageName;

	private List<SK_ITable> tables;

	public SK_Cfg(String className, String packageName, List<SK_ITable> tables) {
		super();
		this.className = className;
		this.packageName = packageName;
		this.tables = tables;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<SK_ITable> getTables() {
		return tables;
	}

	public void setTables(List<SK_ITable> tables) {
		this.tables = tables;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
