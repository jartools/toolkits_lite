package com.bowlong.sql.freemarker;

import com.bowlong.lang.StrEx;

/**
 * @UserName : SandKing
 * @DataTime : 2013年11月24日 下午6:23:56
 * @Description ：数据库字段对象
 */
public class SK_Column {
	private boolean autoincrement;
	private String columnName;
	private String d_columnName;
	private String x_columnName;
	private String columnType;
	private int columnSize;
	private int dataType;
	private String classType;
	private String basicType;
	private String remarks;
	private boolean isNumber;

	public SK_Column(boolean autoincrement, String columnName,
			String d_columnName, String x_columnName, String columnType,
			int columnSize, int dataType, String classType, String basicType,
			String remarks) {
		super();
		this.autoincrement = autoincrement;
		this.columnName = columnName;
		this.d_columnName = d_columnName;
		this.x_columnName = x_columnName;
		this.columnType = columnType;
		this.columnSize = columnSize;
		this.dataType = dataType;
		this.classType = classType;
		this.basicType = basicType;
		this.remarks = remarks;
	}

	public boolean isAutoincrement() {
		return autoincrement;
	}

	public void setAutoincrement(boolean autoincrement) {
		this.autoincrement = autoincrement;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getD_columnName() {
		return d_columnName;
	}

	public void setD_columnName(String d_columnName) {
		this.d_columnName = d_columnName;
	}

	public String getX_columnName() {
		return x_columnName;
	}

	public void setX_columnName(String x_columnName) {
		this.x_columnName = x_columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getBasicType() {
		return basicType;
	}

	public void setBasicType(String basicType) {
		this.basicType = basicType;
	}

	public String getRemarks() {
		if (remarks == null || "".equals(remarks)) {
			return columnName;
		} else {
			return remarks;
		}
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean isNumber() {
		isNumber = StrEx.isEmpty(basicType);
		if (!isNumber) {
			isNumber = "short".equals(basicType) || "int".equals(basicType)
					|| "long".equals(basicType) || "float".equals(basicType)
					|| "double".equals(basicType);
		}
		return isNumber;
	}

	@Override
	public String toString() {
		return "SK_Column [autoincrement=" + autoincrement + ", columnName="
				+ columnName + ", d_columnName=" + d_columnName
				+ ", x_columnName=" + x_columnName + ", columnType="
				+ columnType + ", columnSize=" + columnSize + ", dataType="
				+ dataType + ", classType=" + classType + ", basicType="
				+ basicType + ", remarks=" + remarks + "]";
	}

}
