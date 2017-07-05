package com.bowlong.sql.freemarker;

/**
 * @UserName : SandKing
 * @DataTime : 2013年11月28日 下午5:46:47
 * @Description ：主外键对象
 */
public class SK_BindKey {
	/** 原主键表名称 */
	private String pkTableName;
	/** 原主键表字段 */
	private String pkColumnName;
	/** 原外键表名称 */
	private String fkTableName;
	/** 原外键表字段 */
	private String fkColumnName;

	/** 大写主键表名称 */
	private String d_pkTableName;
	/** 大写主键表字段 */
	private String d_pkColumnName;
	/** 大写外键表名称 */
	private String d_fkTableName;
	/** 大写外键表字段 */
	private String d_fkColumnName;

	/** 小写主键表名称 */
	private String x_pkTableName;
	/** 小写主键表字段 */
	private String x_pkColumnName;
	/** 小写外键表名称 */
	private String x_fkTableName;
	/** 小写外键表字段 */
	private String x_fkColumnName;
	/** 是否是主键 */
	private boolean pk;
	/** 是否是唯一 */
	private boolean unique;

	public SK_BindKey(String pkTableName, String pkColumnName,
			String fkTableName, String fkColumnName, String d_pkTableName,
			String d_pkColumnName, String d_fkTableName, String d_fkColumnName,
			String x_pkTableName, String x_pkColumnName, String x_fkTableName,
			String x_fkColumnName, boolean pk, boolean unique) {
		super();
		this.pkTableName = pkTableName;
		this.pkColumnName = pkColumnName;
		this.fkTableName = fkTableName;
		this.fkColumnName = fkColumnName;
		this.d_pkTableName = d_pkTableName;
		this.d_pkColumnName = d_pkColumnName;
		this.d_fkTableName = d_fkTableName;
		this.d_fkColumnName = d_fkColumnName;
		this.x_pkTableName = x_pkTableName;
		this.x_pkColumnName = x_pkColumnName;
		this.x_fkTableName = x_fkTableName;
		this.x_fkColumnName = x_fkColumnName;
		this.pk = pk;
		this.unique = unique;
	}

	public String getPkTableName() {
		return pkTableName;
	}

	public void setPkTableName(String pkTableName) {
		this.pkTableName = pkTableName;
	}

	public String getPkColumnName() {
		return pkColumnName;
	}

	public void setPkColumnName(String pkColumnName) {
		this.pkColumnName = pkColumnName;
	}

	public String getFkTableName() {
		return fkTableName;
	}

	public void setFkTableName(String fkTableName) {
		this.fkTableName = fkTableName;
	}

	public String getFkColumnName() {
		return fkColumnName;
	}

	public void setFkColumnName(String fkColumnName) {
		this.fkColumnName = fkColumnName;
	}

	public String getD_pkTableName() {
		return d_pkTableName;
	}

	public void setD_pkTableName(String d_pkTableName) {
		this.d_pkTableName = d_pkTableName;
	}

	public String getD_pkColumnName() {
		return d_pkColumnName;
	}

	public void setD_pkColumnName(String d_pkColumnName) {
		this.d_pkColumnName = d_pkColumnName;
	}

	public String getD_fkTableName() {
		return d_fkTableName;
	}

	public void setD_fkTableName(String d_fkTableName) {
		this.d_fkTableName = d_fkTableName;
	}

	public String getD_fkColumnName() {
		return d_fkColumnName;
	}

	public void setD_fkColumnName(String d_fkColumnName) {
		this.d_fkColumnName = d_fkColumnName;
	}

	public String getX_pkTableName() {
		return x_pkTableName;
	}

	public void setX_pkTableName(String x_pkTableName) {
		this.x_pkTableName = x_pkTableName;
	}

	public String getX_pkColumnName() {
		return x_pkColumnName;
	}

	public void setX_pkColumnName(String x_pkColumnName) {
		this.x_pkColumnName = x_pkColumnName;
	}

	public String getX_fkTableName() {
		return x_fkTableName;
	}

	public void setX_fkTableName(String x_fkTableName) {
		this.x_fkTableName = x_fkTableName;
	}

	public String getX_fkColumnName() {
		return x_fkColumnName;
	}

	public void setX_fkColumnName(String x_fkColumnName) {
		this.x_fkColumnName = x_fkColumnName;
	}

	public boolean isPk() {
		return pk;
	}

	public void setPk(boolean pk) {
		this.pk = pk;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

}
