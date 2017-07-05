package com.bowlong.sql.freemarker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import opensource.jpinyin.PinyinHelper;

import com.bowlong.lang.StrEx;
import com.bowlong.sql.freemarker.decode.SK_JdbcType;
import com.bowlong.sql.freemarker.decode.SK_SqlTypeDecode;
import com.bowlong.util.ListEx;
import com.bowlong.util.MapEx;

/**
 * @UserName : SandKing
 * @DataTime : 2013年11月24日 下午6:28:21
 * @Description ：数据库表对象
 */
public abstract class SK_ITable {
	// 原名称
	private String tableName;
	// 首字母大写拼音
	private String d_tableName;
	// 首字母小写拼音
	private String x_tableName;
	// 所属包名称
	private String packageName;
	// 所有字段 类型 + 变量 名字符串
	private String all_basicType_x_columnName;
	// 所有字段  变量 名字符串
	private String all_x_columnName;
	// 元字段
	private String all_columnName;
	// 占位符
	private String all_columnNameSign;
	// 占位符值
	private String all_objAndGetD_columnName;
	// 占位符 + 值
	private String all_columnName_Sign;
	// 主键字段
	private String primary_columnName;
	// 主键大写字段
	private String primaryD_columnName;
	// 主键小写字段
	private String primaryX_columnName;

	private String primaryClassType;
	private String primaryBasicType;

	// 占位符值批处理列表
	private List<String> all_objAndGetD_columnNames;
	// 字段集
	private List<SK_Column> columns;
	// 主外键集
	private List<SK_BindKey> bindKeys;
	private List<SK_Index> indexKeys;
	private SK_Database db;
	private String config;
	private int columnSize;
	private boolean isCfg;

	protected Set<String> beanImports;
	protected Set<String> cacheImports;
	protected Set<String> jedisImports;
	protected Set<String> daoImports;
	protected Set<String> impImports;
	protected Set<String> cfgImports;

	public SK_ITable(String tableName, String d_tableName, String x_tableName,
			String packageName, String all_basicType_x_columnName,
			String all_columnName, String all_columnNameSign,
			String all_objAndGetD_columnName, String all_columnName_Sign,
			String primary_columnName, String primaryD_columnName,
			String primaryX_columnName, String primaryClassType,
			String primaryBasicType, List<String> all_objAndGetD_columnNames,
			List<SK_Column> columns, List<SK_BindKey> bindKeys,
			List<SK_Index> indexKeys, SK_Database db, String config,
			int columnSize, boolean isCfg) {
		super();
		this.tableName = tableName;
		this.d_tableName = d_tableName;
		this.x_tableName = x_tableName;
		this.packageName = packageName;
		this.all_basicType_x_columnName = all_basicType_x_columnName;
		this.all_columnName = all_columnName;
		this.all_columnNameSign = all_columnNameSign;
		this.all_objAndGetD_columnName = all_objAndGetD_columnName;
		this.all_columnName_Sign = all_columnName_Sign;
		this.primary_columnName = primary_columnName;
		this.primaryD_columnName = primaryD_columnName;
		this.primaryX_columnName = primaryX_columnName;
		this.primaryClassType = primaryClassType;
		this.primaryBasicType = primaryBasicType;
		this.all_objAndGetD_columnNames = all_objAndGetD_columnNames;
		this.columns = columns;
		this.bindKeys = bindKeys;
		this.indexKeys = indexKeys;
		this.db = db;
		this.config = config;
		this.columnSize = columnSize;
		this.isCfg = isCfg;
	}

	public String getPrimaryClassType() {
		if (StrEx.isEmpty(primaryClassType)) {
			List<SK_Index> index_ = getIndexKeys(tableName);
			for (SK_Index sk_Index : index_) {
				if ("PRIMARY".equals(sk_Index.getIndexName())) {
					primaryClassType = sk_Index.getClassTypes().get(0);
					break;
				}
			}
		}
		return primaryClassType;
	}

	public void setPrimaryClassType(String primaryClassType) {
		this.primaryClassType = primaryClassType;
	}

	public String getPrimaryBasicType() {
		if (StrEx.isEmpty(primaryBasicType)) {
			List<SK_Index> index_ = getIndexKeys(tableName);
			for (SK_Index sk_Index : index_) {
				if ("PRIMARY".equals(sk_Index.getIndexName())) {
					primaryBasicType = sk_Index.getBasicTypes().get(0);
					break;
				}
			}
		}
		return primaryBasicType;
	}

	public void setPrimaryBasicType(String primaryBasicType) {
		this.primaryBasicType = primaryBasicType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getD_tableName() {
		return d_tableName;
	}

	public boolean isCfg() {
		return isCfg;
	}

	public void setCfg(boolean isCfg) {
		this.isCfg = isCfg;
	}

	public void setD_tableName(String d_tableName) {
		this.d_tableName = d_tableName;
	}

	public String getX_tableName() {
		return x_tableName;
	}

	public void setX_tableName(String x_tableName) {
		this.x_tableName = x_tableName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setIndexKeys(List<SK_Index> indexKeys) {
		this.indexKeys = indexKeys;
	}

	public SK_Database getDb() {
		return db;
	}

	public void setDb(SK_Database db) {
		this.db = db;
	}

	public void setBeanImports(Set<String> beanImports) {
		this.beanImports = beanImports;
	}

	public void setCacheImports(Set<String> cacheImports) {
		this.cacheImports = cacheImports;
	}

	public void setDaoImports(Set<String> daoImports) {
		this.daoImports = daoImports;
	}

	public void setAll_basicType_x_columnName(String all_basicType_x_columnName) {
		this.all_basicType_x_columnName = all_basicType_x_columnName;
	}

	public void setColumns(List<SK_Column> columns) {
		this.columns = columns;
	}

	public void setBindKeys(List<SK_BindKey> bindKeys) {
		this.bindKeys = bindKeys;
	}

	public void setImpImports(Set<String> impImports) {
		this.impImports = impImports;
	}

	public void setCfgImports(Set<String> cfgImports) {
		this.cfgImports = cfgImports;
	}

	public void setJedisImports(Set<String> jedisImports) {
		this.jedisImports = jedisImports;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public void setAll_columnName(String all_columnName) {
		this.all_columnName = all_columnName;
	}

	public void setAll_columnNameSign(String all_columnNameSign) {
		this.all_columnNameSign = all_columnNameSign;
	}

	public void setAll_objAndGetD_columnName(String all_objAndGetD_columnName) {
		this.all_objAndGetD_columnName = all_objAndGetD_columnName;
	}

	public void setAll_columnName_Sign(String all_columnName_Sign) {
		this.all_columnName_Sign = all_columnName_Sign;
	}

	public void setPrimaryD_columnName(String primaryD_columnName) {
		this.primaryD_columnName = primaryD_columnName;
	}

	public void setPrimary_columnName(String primary_columnName) {
		this.primary_columnName = primary_columnName;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public void setAll_objAndGetD_columnNames(
			List<String> all_objAndGetD_columnNames) {
		this.all_objAndGetD_columnNames = all_objAndGetD_columnNames;
	}

	public void setPrimaryX_columnName(String primaryX_columnName) {
		this.primaryX_columnName = primaryX_columnName;
	}
	
	public void setAll_x_columnName(String all_x_columnName) {
		this.all_x_columnName = all_x_columnName;
	}

	// -------------------------------------------------------------------------------------------------------
	/**
	 * 取得字段集
	 * 
	 * @return
	 */
	public List<SK_Column> getColumns() {
		if (columns.isEmpty()) {
			List<Map<String, Object>> names = SK_MetaData.getColumns(
					db.getConn(), tableName);
			SK_Column column = null;
			for (Map<String, Object> map : names) {
				String columnName = map.get("COLUMN_NAME").toString();
				String remarks = map.get("REMARKS").toString();
				boolean autoincrement = "YES".equals(MapEx.getString(map,
						"IS_AUTOINCREMENT")) ? true : false;
				String columnName_ = PinyinHelper.getShortPinyin(columnName);
				String d_columnName = StrEx.upperN1(columnName_);
				String x_columnName = StrEx.lowerFirst(columnName_);
				String typeName = map.get("TYPE_NAME").toString();
				int dataType = Integer
						.parseInt(map.get("DATA_TYPE").toString());
				int columnSize = 0;
				Object column_size = map.get("COLUMN_SIZE");
				if (column_size != null) {
					columnSize = Integer.parseInt(column_size.toString());
				}
				String classType = "No Class Name";
				if (db.getSqlDecode() instanceof SK_JdbcType) {
					classType = db.getSqlDecode().getTypeByDataType(dataType);
				} else {
					classType = db.getSqlDecode().getTypeByTypeName(typeName,
							columnSize);
				}
				String basicType = SK_SqlTypeDecode.getBasicType(classType);

				column = new SK_Column(autoincrement, columnName, d_columnName,
						x_columnName, typeName, columnSize, dataType,
						classType, basicType, remarks);
				columns.add(column);
			}
		}
		return columns;
	}

	/**
	 * 取得键集
	 * 
	 * @return
	 */
	public List<SK_BindKey> getBindKeys() {
		if (bindKeys.isEmpty()) {
			List<Map<String, Object>> keyList = SK_MetaData.getExportedKeys(
					db.getConn(), tableName);
			SK_BindKey sk_key = null;
			boolean pk = false;
			for (Map<String, Object> map : keyList) {
				String pkTableName = map.get("PKTABLE_NAME").toString();
				String pkColumnName = map.get("PKCOLUMN_NAME").toString();
				String fkTableName = map.get("FKTABLE_NAME").toString();
				String fkColumnName = map.get("FKCOLUMN_NAME").toString();
				boolean unique = false;
				// 取得这个外键的相关索引
				List<SK_Index> index_ = getIndexKeys(fkTableName);
				for (SK_Index sk_Index : index_) {
					int size = sk_Index.getColumnNames().size();
					if (size == 1) {
						// 索引字段=外键字段 & 是唯一索引
						if (fkColumnName.equals(sk_Index.getColumnNames()
								.get(0)) && sk_Index.isUnique()) {
							unique = true;
						}
					}
				}
				String pkTableName_ = PinyinHelper.getShortPinyin(pkTableName);
				String pkColumnName_ = PinyinHelper
						.getShortPinyin(pkColumnName);
				String fkTableName_ = PinyinHelper.getShortPinyin(fkTableName);
				String fkColumnName_ = PinyinHelper
						.getShortPinyin(fkColumnName);

				String d_pkTableName = StrEx.upperN1(pkTableName_);
				String d_pkColumnName = StrEx.upperN1(pkColumnName_);
				String d_fkTableName = StrEx.upperN1(fkTableName_);
				String d_fkColumnName = StrEx.upperN1(fkColumnName_);

				String x_pkTableName = StrEx.lowerFirst(pkTableName_);
				String x_pkColumnName = StrEx.lowerFirst(pkColumnName_);
				String x_fkTableName = StrEx.lowerFirst(fkTableName_);
				String x_fkColumnName = StrEx.lowerFirst(fkColumnName_);
				sk_key = new SK_BindKey(pkTableName, pkColumnName, fkTableName,
						fkColumnName, d_pkTableName, d_pkColumnName,
						d_fkTableName, d_fkColumnName, x_pkTableName,
						x_pkColumnName, x_fkTableName, x_fkColumnName, pk,
						unique);
				bindKeys.add(sk_key);
			}
			pk = true;
			keyList = SK_MetaData.getImportedKeys(db.getConn(), tableName);
			for (Map<String, Object> map : keyList) {
				String pkTableName = map.get("PKTABLE_NAME").toString();
				String pkColumnName = map.get("PKCOLUMN_NAME").toString();
				String fkTableName = map.get("FKTABLE_NAME").toString();
				String fkColumnName = map.get("FKCOLUMN_NAME").toString();

				String pkTableName_ = PinyinHelper.getShortPinyin(pkTableName);
				String pkColumnName_ = PinyinHelper
						.getShortPinyin(pkColumnName);
				String fkTableName_ = PinyinHelper.getShortPinyin(fkTableName);
				String fkColumnName_ = PinyinHelper
						.getShortPinyin(fkColumnName);

				String d_pkTableName = StrEx.upperN1(pkTableName_);
				String d_pkColumnName = StrEx.upperN1(pkColumnName_);
				String d_fkTableName = StrEx.upperN1(fkTableName_);
				String d_fkColumnName = StrEx.upperN1(fkColumnName_);

				String x_pkTableName = StrEx.lowerFirst(pkTableName_);
				String x_pkColumnName = StrEx.lowerFirst(pkColumnName_);
				String x_fkTableName = StrEx.lowerFirst(fkTableName_);
				String x_fkColumnName = StrEx.lowerFirst(fkColumnName_);
				sk_key = new SK_BindKey(pkTableName, pkColumnName, fkTableName,
						fkColumnName, d_pkTableName, d_pkColumnName,
						d_fkTableName, d_fkColumnName, x_pkTableName,
						x_pkColumnName, x_fkTableName, x_fkColumnName, pk, true);
				bindKeys.add(sk_key);
			}
		}
		return bindKeys;
	}

	/**
	 * 取得All_basicType_x_columnName
	 * 
	 * @return
	 */
	public String getAll_basicType_x_columnName() {
		if (StrEx.isEmpty(all_basicType_x_columnName)) {
			StringBuffer sb = new StringBuffer();
			int length = columns.size();
			for (int i = 0; i < length; i++) {
				sb.append(columns.get(i).getBasicType());
				sb.append(" ");
				sb.append(columns.get(i).getX_columnName());
				if ((i + 1) < length) {
					sb.append(", ");
				}
			}
			all_basicType_x_columnName = sb.toString();
		}
		return all_basicType_x_columnName;
	}
	
	/**
	 * 取得All_basicType_x_columnName
	 * 
	 * @return
	 */
	public String getAll_x_columnName() {
		if (StrEx.isEmpty(all_x_columnName)) {
			StringBuffer sb = new StringBuffer();
			int length = columns.size();
			for (int i = 0; i < length; i++) {
				sb.append(columns.get(i).getX_columnName());
				if ((i + 1) < length) {
					sb.append(", ");
				}
			}
			all_x_columnName = sb.toString();
		}
		return all_x_columnName;
	}

	/**
	 * 取得索引集
	 * 
	 * @return
	 */
	public List<SK_Index> getIndexKeys() {
		if (indexKeys.isEmpty()) {
			indexKeys = getIndexKeys(tableName);
		}
		return indexKeys;
	}

	public List<SK_Index> getIndexKeys(String tableName) {
		List<SK_Index> indexs = new ArrayList<SK_Index>();
		List<Map<String, Object>> indexList = SK_MetaData.getIndexs(
				db.getConn(), tableName);
		SK_Index index = null;
		// 判断是否已经添加
		Map<String, SK_Index> sk_indexs = new HashMap<String, SK_Index>();
		for (Map<String, Object> map : indexList) {
			// 原名称
			String columnName = map.get("COLUMN_NAME").toString();
			String indexName = MapEx.getString(map, "INDEX_NAME");
			String basicType = "";
			String classType = "";
			List<SK_Column> columnArray = getColumns();
			for (SK_Column sk_Column : columnArray) {
				if (sk_Column.getColumnName().equals(columnName)) {
					basicType = sk_Column.getBasicType();
					classType = sk_Column.getClassType();
					break;
				}
			}
			String columnName_ = PinyinHelper.getShortPinyin(columnName);
			String d_columnName = StrEx.upperN1(columnName_);
			String x_columnName = StrEx.lowerFirst(columnName_);
			index = sk_indexs.remove(indexName);
			List<String> columnNames = null;
			List<String> d_columnNames = null;
			List<String> x_columnNames = null;
			List<String> basicTypes = null;
			List<String> classTypes = null;
			if (index == null) {
				boolean unique = MapEx.getBoolean(map, "NON_UNIQUE") ? false
						: true;
				columnNames = new ArrayList<String>();
				d_columnNames = new ArrayList<String>();
				x_columnNames = new ArrayList<String>();
				basicTypes = new ArrayList<String>();
				classTypes = new ArrayList<String>();
				columnNames.add(columnName);
				d_columnNames.add(d_columnName);
				x_columnNames.add(x_columnName);
				basicTypes.add(basicType);
				classTypes.add(classType);
				index = new SK_Index(indexName, columnNames, d_columnNames,
						x_columnNames, basicTypes, classTypes, "", "", "", "",
						"", "", "", unique, this);
				indexs.add(index);
			} else {
				columnNames = index.getColumnNames();
				d_columnNames = index.getD_columnNames();
				x_columnNames = index.getX_columnNames();
				basicTypes = index.getBasicTypes();
				columnNames.add(columnName);
				d_columnNames.add(d_columnName);
				x_columnNames.add(x_columnName);
				basicTypes.add(basicType);
			}
			sk_indexs.put(indexName, index);
		}
		return indexs;
	}

	/**
	 * 取得引用包集
	 * 
	 * @return
	 */
	public abstract Set<String> getBeanImports();

	public abstract Set<String> getCacheImports();

	public abstract Set<String> getJedisImports();

	public abstract Set<String> getDaoImports();

	public abstract Set<String> getImpImports();

	public abstract Set<String> getCfgImports();

	public String getConfig() {
		return config;
	}

	public String getAll_columnName() {
		if (StrEx.isEmpty(all_columnName)) {
			StringBuffer sb = new StringBuffer();
			int length = columns.size();
			for (int i = 0; i < length; i++) {
				sb.append(columns.get(i).getColumnName());
				if ((i + 1) < length) {
					sb.append(",");
				}
			}
			all_columnName = sb.toString();
		}
		return all_columnName;
	}

	public String getAll_columnNameSign() {
		if (StrEx.isEmpty(all_columnNameSign)) {
			StringBuffer sb = new StringBuffer();
			int length = columns.size();
			for (int i = 0; i < length; i++) {
				sb.append("?");
				if ((i + 1) < length) {
					sb.append(",");
				}
			}
			all_columnNameSign = sb.toString();
		}
		return all_columnNameSign;
	}

	public String getAll_columnName_Sign() {
		if (StrEx.isEmpty(all_columnName_Sign)) {
			StringBuffer sb = new StringBuffer();
			int length = columns.size();
			for (int i = 0; i < length; i++) {
				sb.append(columns.get(i).getColumnName());
				sb.append(" = ");
				sb.append("?");
				if ((i + 1) < length) {
					sb.append(",");
				}
			}
			all_columnName_Sign = sb.toString();
		}
		return all_columnName_Sign;
	}

	public String getAll_objAndGetD_columnName() {
		if (StrEx.isEmpty(all_objAndGetD_columnName)) {
			StringBuffer sb = new StringBuffer();
			int length = columns.size();
			for (int i = 0; i < length; i++) {
				sb.append(x_tableName);
				sb.append(".");
				sb.append("get");
				sb.append(columns.get(i).getD_columnName());
				sb.append("()");
				if ((i + 1) < length) {
					sb.append(",");
				}
			}
			all_objAndGetD_columnName = sb.toString();
		}
		return all_objAndGetD_columnName;
	}

	public String getPrimaryD_columnName() {
		if (StrEx.isEmpty(primaryD_columnName)) {
			List<SK_Index> index_ = getIndexKeys(tableName);
			for (SK_Index sk_Index : index_) {
				if ("PRIMARY".equals(sk_Index.getIndexName())) {
					primaryD_columnName = sk_Index.getD_columnNames().get(0);
					break;
				}
			}
		}
		return primaryD_columnName;
	}

	public String getPrimary_columnName() {
		if (StrEx.isEmpty(primary_columnName)) {
			List<SK_Index> index_ = getIndexKeys(tableName);
			for (SK_Index sk_Index : index_) {
				if ("PRIMARY".equals(sk_Index.getIndexName())) {
					primary_columnName = sk_Index.getColumnNames().get(0);
					break;
				}
			}
		}
		return primary_columnName;
	}

	public int getColumnSize() {
		columnSize = getColumns().size();
		return columnSize;
	}

	public List<String> getAll_objAndGetD_columnNames() {
		if (all_objAndGetD_columnNames.isEmpty()) {
			int length = columns.size();
			for (int i = 0; i < length; i++) {
				StringBuffer sb = new StringBuffer();
				sb.append(x_tableName);
				sb.append("s.get(i)");
				sb.append(".");
				sb.append("get");
				sb.append(columns.get(i).getD_columnName());
				sb.append("()");
				all_objAndGetD_columnNames.add(sb.toString());
			}
		}
		return all_objAndGetD_columnNames;
	}

	public String getPrimaryX_columnName() {
		if (StrEx.isEmpty(primaryX_columnName)) {
			List<SK_Index> index_ = getIndexKeys(tableName);
			for (SK_Index sk_Index : index_) {
				if ("PRIMARY".equals(sk_Index.getIndexName())) {
					primaryX_columnName = sk_Index.getX_columnNames().get(0);
					break;
				}
			}
		}
		return primaryX_columnName;
	}

	/** 取得实体中唯一标识字段的值方法的方法 ***/
	private String enUqFun;

	public String getEnUqFun() {
		if (ListEx.isEmpty(indexKeys))
			return enUqFun;
		if (StrEx.isEmpty(enUqFun)) {
			StringBuffer buff = new StringBuffer();
			buff.append("String tmp = \"\"");
			int len = indexKeys.size();
			for (int i = 0; i < len; i++) {
				SK_Index index = indexKeys.get(i);
				if (!index.isUnique())
					continue;
				if ("PRIMARY".equals(index.getIndexName()))
					continue;
				for (String item : index.getColumnNames()) {
					buff.append("tmp = ").append("");
					buff.append(item).append("Cache.remove(");
				}
				index.getColumnNames();
			}
		}
		return enUqFun;
	}

	public void setEnUqFun(String enUqFun) {
		this.enUqFun = enUqFun;
	}

	public String getUniqueColumnFun(String colname) {
		if (StrEx.isByte(colname))
			return "";
		StringBuffer buff = new StringBuffer(x_tableName);
		buff.append(",get");
		String v = StrEx.upperN1(colname);
		buff.append(v).append("Index()");
		return buff.toString();
	}
}
