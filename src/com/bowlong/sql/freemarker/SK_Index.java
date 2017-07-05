package com.bowlong.sql.freemarker;

import java.util.List;

import com.bowlong.lang.StrEx;

/**
 * @UserName : SandKing
 * @DataTime : 2013年11月28日 下午5:54:36
 * @Description ：索引对象
 */
public class SK_Index {
	private String indexName;
	private List<String> columnNames;
	private List<String> d_columnNames;
	private List<String> x_columnNames;
	private List<String> basicTypes;
	private List<String> classTypes;
	private String all_basicType_x_columnName;
	private String all_x_columnName;
	/** columnName = ? AND columnName = ?... */
	private String all_x_columnName_help;
	/** columnName.getColumnName() + columnName.getColumnName()... */
	private String all_d_columnName_get;
	private String all_d_columnName_cfg_get;
	/** columnName + columnName... */
	private String all_d_columnName_plus;
	/** columnName.getColumnName() + columnName.getColumnName()... */
	private String all_x_columnName_get;
	/** 是否是唯一索引 */
	private boolean unique;
	private SK_ITable table;

	public SK_Index(String indexName, List<String> columnNames,
			List<String> d_columnNames, List<String> x_columnNames,
			List<String> basicTypes, List<String> classTypes,
			String all_basicType_x_columnName, String all_x_columnName,
			String all_x_columnName_help, String all_d_columnName_get,
			String all_d_columnName_cfg_get, String all_d_columnName_plus,
			String all_x_columnName_get, boolean unique, SK_ITable table) {
		super();
		this.indexName = indexName;
		this.columnNames = columnNames;
		this.d_columnNames = d_columnNames;
		this.x_columnNames = x_columnNames;
		this.basicTypes = basicTypes;
		this.classTypes = classTypes;
		this.all_basicType_x_columnName = all_basicType_x_columnName;
		this.all_x_columnName = all_x_columnName;
		this.all_x_columnName_help = all_x_columnName_help;
		this.all_d_columnName_get = all_d_columnName_get;
		this.all_d_columnName_cfg_get = all_d_columnName_cfg_get;
		this.all_d_columnName_plus = all_d_columnName_plus;
		this.all_x_columnName_get = all_x_columnName_get;
		this.unique = unique;
		this.table = table;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public List<String> getD_columnNames() {
		return d_columnNames;
	}

	public void setD_columnNames(List<String> d_columnNames) {
		this.d_columnNames = d_columnNames;
	}

	public List<String> getX_columnNames() {
		return x_columnNames;
	}

	public void setX_columnNames(List<String> x_columnNames) {
		this.x_columnNames = x_columnNames;
	}

	public List<String> getBasicTypes() {
		return basicTypes;
	}

	public void setBasicTypes(List<String> basicTypes) {
		this.basicTypes = basicTypes;
	}

	public List<String> getClassTypes() {
		return classTypes;
	}

	public void setClassTypes(List<String> classTypes) {
		this.classTypes = classTypes;
	}

	public void setAll_x_columnName_help(String all_x_columnName_help) {
		this.all_x_columnName_help = all_x_columnName_help;
	}

	public void setAll_d_columnName_get(String all_d_columnName_get) {
		this.all_d_columnName_get = all_d_columnName_get;
	}

	public void setAll_d_columnName_cfg_get(String all_d_columnName_cfg_get) {
		this.all_d_columnName_cfg_get = all_d_columnName_cfg_get;
	}

	public void setAll_x_columnName_get(String all_x_columnName_get) {
		this.all_x_columnName_get = all_x_columnName_get;
	}

	public void setAll_d_columnName_plus(String all_d_columnName_plus) {
		this.all_d_columnName_plus = all_d_columnName_plus;
	}

	public SK_ITable getTable() {
		return table;
	}

	public void setTable(SK_ITable table) {
		this.table = table;
	}

	public String getAll_basicType_x_columnName() {
		if (StrEx.isEmpty(all_basicType_x_columnName)) {
			StringBuffer sb = new StringBuffer();
			int length = columnNames.size();
			for (int i = 0; i < length; i++) {
				sb.append(basicTypes.get(i));
				sb.append(" ");
				sb.append(x_columnNames.get(i));
				if ((i + 1) < length) {
					sb.append(", ");
				}
			}
			all_basicType_x_columnName = sb.toString();
		}
		return all_basicType_x_columnName;
	}

	public void setAll_basicType_x_columnName(String all_basicType_x_columnName) {
		this.all_basicType_x_columnName = all_basicType_x_columnName;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public String getAll_x_columnName() {
		if (StrEx.isEmpty(all_x_columnName)) {
			StringBuffer sb = new StringBuffer();
			int length = x_columnNames.size();
			for (int i = 0; i < length; i++) {
				sb.append(x_columnNames.get(i));
				if ((i + 1) < length) {
					sb.append(", ");
				}
			}
			all_x_columnName = sb.toString();
		}
		return all_x_columnName;
	}

	public void setAll_x_columnName(String all_x_columnName) {
		this.all_x_columnName = all_x_columnName;
	}

	public String getAll_x_columnName_help() {
		if (StrEx.isEmpty(all_x_columnName_help)) {
			StringBuffer sb = new StringBuffer();
			int length = x_columnNames.size();
			for (int i = 0; i < length; i++) {
				sb.append(x_columnNames.get(i));
				sb.append(" = ");
				sb.append("?");
				if ((i + 1) < length) {
					sb.append(" AND ");
				}
			}
			all_x_columnName_help = sb.toString();
		}
		return all_x_columnName_help;
	}

	public String getAll_x_columnName_get() {
		if (StrEx.isEmpty(all_x_columnName_get)) {
			StringBuffer sb = new StringBuffer();
			int length = d_columnNames.size();
			for (int i = 0; i < length; i++) {
				if (i > 0) {
					sb.append(",").append("\"_\"").append(",");
				}
				sb.append(table.getX_tableName());
				sb.append(".get");
				sb.append(d_columnNames.get(i));
				sb.append("()");
			}
			all_x_columnName_get = sb.toString();
		}
		return all_x_columnName_get;
	}

	public String getAll_d_columnName_get() {
		if (StrEx.isEmpty(all_d_columnName_get)) {
			StringBuffer sb = new StringBuffer();
			int length = d_columnNames.size();
			for (int i = 0; i < length; i++) {
				if (i < 1) {
					sb.append("PStr.b(");
				}
				sb.append(table.getX_tableName());
				sb.append(".get");
				sb.append(d_columnNames.get(i));
				sb.append("()");
				if ((i + 1) >= length) {
					sb.append(").e()");
				} else {
					sb.append(",");
				}
			}
			all_d_columnName_get = sb.toString();
		}
		return all_d_columnName_get;
	}

	public String getAll_d_columnName_cfg_get() {
		if (StrEx.isEmpty(all_d_columnName_cfg_get)) {
			StringBuffer sb = new StringBuffer();
			int length = d_columnNames.size();
			for (int i = 0; i < length; i++) {
				if (i < 1) {
					sb.append("PStr.b(");
				}
				sb.append(table.getX_tableName());
				sb.append("Cfg.get");
				sb.append(d_columnNames.get(i));
				sb.append("()");
				if ((i + 1) >= length) {
					sb.append(").e()");
				} else {
					sb.append(",");
				}
			}
			all_d_columnName_cfg_get = sb.toString();
		}
		return all_d_columnName_cfg_get;
	}

	public String getAll_d_columnName_plus() {
		if (StrEx.isEmpty(all_d_columnName_plus)) {
			StringBuffer sb = new StringBuffer();
			int length = x_columnNames.size();
			for (int i = 0; i < length; i++) {
				if (i < 1) {
					sb.append("PStr.b(");
				}
				sb.append(x_columnNames.get(i));
				if ((i + 1) >= length) {
					sb.append(").e()");
				} else {
					sb.append(",").append("\"_\"").append(",");
				}
			}
			all_d_columnName_plus = sb.toString();
		}
		return all_d_columnName_plus;
	}
}
