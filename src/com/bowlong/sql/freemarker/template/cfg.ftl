package ${packageName};

<#list cfgImports as imports>
import ${imports};
</#list>

/**
 * ${tableName}
 */
public class ${d_tableName}Cfg {

	public static final String TABLENAME = "${tableName}";
	private static boolean isLoadAll = false;
	
	public static boolean isLoadAll() {
		return isLoadAll;
	}

	public static void setLoadAll(boolean isLoadAll) {
		${d_tableName}Cfg.isLoadAll = isLoadAll;
	}
	
	//缓存
	<#list indexKeys as index>
	<#if index.unique>
	<#if index.indexName == "PRIMARY">
	static final Map<String, ${d_tableName}Cfg> <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache = MapEx.newSortedMap();
	<#else>
	static final Map<String, String> <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache = new ConcurrentHashMap<String, String>();
	</#if>
	<#else>
	static final Map<String, Set<String>> <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache = MapEx.newMap();
	</#if>
	</#list>

	<#list columns as column>
	/** ${column.remarks} */
	private ${column.basicType} ${column.x_columnName};
	
	</#list>
	
	public ${d_tableName}Cfg() {
		super();
	}
	
	public ${d_tableName}Cfg(${all_basicType_x_columnName}) {
		super();
		<#list columns as column>
		this.${column.x_columnName} = ${column.x_columnName};
		</#list>
	}
	
	<#-- Get And Set -->
	<#list columns as column>
	public ${column.basicType} get${column.d_columnName}() {
		return ${column.x_columnName};
	}
	
	public void set${column.d_columnName}(${column.basicType} ${column.x_columnName}) {
		this.${column.x_columnName} = ${column.x_columnName};
	}
	</#list>
	
	 /**
     * 根据list创建对象
     */
    public static List<${d_tableName}Cfg> createForColumnNameList(List<Map<String, Object>> list){
    	List<${d_tableName}Cfg> ${x_tableName}Cfgs = new ArrayList<${d_tableName}Cfg>();
		for (Map<String, Object> map : list) {
			${x_tableName}Cfgs.add(createForColumnNameMap(map));
		}
		return ${x_tableName}Cfgs;
    }
    
    /**
     * 根据map创建对象
     */
    public static ${d_tableName}Cfg createForColumnNameMap(Map<String, Object> map){
    	${d_tableName}Cfg obj = new ${d_tableName}Cfg();
		<#list columns as column>
	    <#if column.basicType=="int">
	    obj.${column.x_columnName} = MapEx.getInt(map,"${column.columnName}");
	    <#elseif column.basicType=="long">
	    obj.${column.x_columnName} = MapEx.getLong(map,"${column.columnName}");
	    <#elseif column.basicType=="short">
	    obj.${column.x_columnName} = MaMapExtShort(map,"${column.columnName}");
	    <#elseif column.basicType=="float">
	    obj.${column.x_columnName} = MapEx.getFloat(map,"${column.columnName}");
	    <#elseif column.basicType=="double">
	    obj.${column.x_columnName} = MapEx.getDouble(map,"${column.columnName}");
	    <#elseif column.basicType=="Date" || column.basicType=="java.util.Date">
	    obj.${column.x_columnName} = MapEx.getDate(map,"${column.columnName}");
	    <#elseif column.basicType=="String">
	    obj.${column.x_columnName} = MapEx.getString(map,"${column.columnName}");
	    </#if>
	    </#list>
        return obj;
    }
    
    public void toStream(ByteArrayOutputStream out) throws Exception {
    	<#list columns as column>
	    <#if column.basicType=="int">
	    B2OutputStream.writeInt(out,this.${column.x_columnName});
	    <#elseif column.basicType=="long">
	    B2OutputStream.writeLong(out,this.${column.x_columnName});
	    <#elseif column.basicType=="short">
	    B2OutputStream.writeShort(out,this.${column.x_columnName});
	    <#elseif column.basicType=="float">
	    B2OutputStream.writeFloat(out,this.${column.x_columnName});
	    <#elseif column.basicType=="double">
	    B2OutputStream.writeDouble(out,this.${column.x_columnName});
	    <#elseif column.basicType=="Date" || column.basicType=="java.util.Date">
	    B2OutputStream.writeDate(out,this.${column.x_columnName});
	    <#elseif column.basicType=="String">
	    B2OutputStream.writeString(out,this.${column.x_columnName});
	    </#if>
	    </#list>
    }
    
     public static ${d_tableName}Cfg forStream(ByteArrayInputStream in) throws Exception {
     	${d_tableName}Cfg ${x_tableName}Cfg = new ${d_tableName}Cfg();
    	<#list columns as column>
	    <#if column.basicType=="int">
	    ${x_tableName}Cfg.${column.x_columnName} = B2InputStream.readInt(in);
	    <#elseif column.basicType=="long">
	    ${x_tableName}Cfg.${column.x_columnName} = (long) B2InputStream.readObject(in);
	    <#elseif column.basicType=="short">
	    ${x_tableName}Cfg.${column.x_columnName} = (short) B2InputStream.readObject(in);
	    <#elseif column.basicType=="float">
	    ${x_tableName}Cfg.${column.x_columnName} = (float) B2InputStream.readObject(in);
	    <#elseif column.basicType=="double">
	    ${x_tableName}Cfg.${column.x_columnName} = (double) B2InputStream.readObject(in);
	    <#elseif column.basicType=="Date" || column.basicType=="java.util.Date">
	    ${x_tableName}Cfg.${column.x_columnName} = (Date) B2InputStream.readObject(in);
	    <#elseif column.basicType=="String">
	    ${x_tableName}Cfg.${column.x_columnName} = (String) B2InputStream.readObject(in);
	    </#if>
	    </#list>
	    return ${x_tableName}Cfg;
     }
    
    public static List<${d_tableName}Cfg> loadDatabase(){
		Connection conn = ${config}.getConnectionCfg();
		return loadDatabase(conn);
	}
	
	public static List<${d_tableName}Cfg> loadDatabase(Connection conn){
		return loadDatabase(conn,${d_tableName}Cfg.TABLENAME);
	}
	
	public static List<${d_tableName}Cfg> loadDatabase(DataSource ds){
		try {
			Connection conn = ds.getConnection();
			return loadDatabase(conn);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<${d_tableName}Cfg> loadDatabase(String tableName){
		Connection conn = ${config}.getConnectionCfg();
		return loadDatabase(conn,tableName);
	}
	
	public static List<${d_tableName}Cfg> loadDatabase(Connection conn,String tableName){
		QueryRunner run = new QueryRunner();
		String sql = "SELECT ${all_columnName} FROM " + tableName;
		List<${d_tableName}Cfg> ${x_tableName}Cfgs = null; 
		try {
			List<Map<String,Object>> list = run.query(conn, sql, new MapListHandler());
			${x_tableName}Cfgs = ${d_tableName}Cfg.createForColumnNameList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try{
				DbUtils.close(conn);
			}catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
		return ${x_tableName}Cfgs;
	}
	
	public static List<${d_tableName}Cfg> loadDatabase(DataSource ds,String tableName){
		try {
			Connection conn = ds.getConnection();
			return loadDatabase(conn,tableName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public static void writeFile(String path) throws Exception{
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			List<${d_tableName}Cfg> ${x_tableName}Cfgs = loadDatabase();
			B2OutputStream.writeInt(out, ${x_tableName}Cfgs.size());
			for(${d_tableName}Cfg ${x_tableName}Cfg : ${x_tableName}Cfgs){
				${x_tableName}Cfg.toStream(out);
			}
			FileUtils.writeByteArrayToFile(new File(path), out.toByteArray());
		}catch (Exception e) {
            throw e;
        }
	}
	
	public static void loadFile(String path) throws Exception{
		byte[] data = FileUtils.readFileToByteArray(new File(path));
		try (ByteArrayInputStream in = new ByteArrayInputStream(data);) {
			int size = B2InputStream.readInt(in);
			for (int i = 0; i < size; i++) {
				loadCache(forStream(in));
			}
			isLoadAll = true;
		}catch (Exception e) {
            throw e;
        }
	}
	
	/**
	 * 加载缓存
	 */
	public static void loadCache(${d_tableName}Cfg ${x_tableName}Cfg){
		<#list indexKeys as index>
		<#if index.unique>
		<#if index.indexName == "PRIMARY">
		<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.put(${index.all_d_columnName_cfg_get},${x_tableName}Cfg);
		<#else>
		<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.put(${index.all_d_columnName_cfg_get},String.valueOf(${x_tableName}Cfg.get${primaryD_columnName}()));
		</#if>
		<#else>
		Set<String> <#list index.x_columnNames as x_columnName>${x_columnName}</#list>set = <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.get(String.valueOf(${index.all_d_columnName_cfg_get}));
		if(<#list index.x_columnNames as x_columnName>${x_columnName}</#list>set == null){
			<#list index.x_columnNames as x_columnName>${x_columnName}</#list>set = new HashSet<String>();
		}
		<#list index.x_columnNames as x_columnName>${x_columnName}</#list>set.add(String.valueOf(${x_tableName}Cfg.get${primaryD_columnName}()));
		<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.put(${index.all_d_columnName_cfg_get},<#list index.x_columnNames as x_columnName>${x_columnName}</#list>set);
		</#if>
		</#list>
	}
	
	<#list indexKeys as index>
	/**
	 * 根据(<#list index.columnNames as columnName> ${columnName} </#list>) 查询
	 */
	<#if index.unique>
	<#-- 唯一索引查询 -->
	public static ${d_tableName}Cfg getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName}){
		${d_tableName}Cfg ${x_tableName}Cfg = null;
		String key = ${index.all_d_columnName_plus};
		<#if index.indexName == "PRIMARY">
		${x_tableName}Cfg = <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.get(key);
		
		<#else>
		key = <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.get(key);
		if(key!=null){
			${x_tableName}Cfg = ${primaryX_columnName}Cache.get(key);	
		} 
		</#if>
		return ${x_tableName}Cfg;
	}
	
	<#else>
	<#-- 聚集索引查询 -->
	public static List<${d_tableName}Cfg> getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName}){
		List<${d_tableName}Cfg> ${x_tableName}Cfgs = new ArrayList<${d_tableName}Cfg>();
		String key = ${index.all_d_columnName_plus};
		Set<String> keys = <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.get(key);
		if(keys != null){
			${d_tableName}Cfg ${x_tableName}Cfg = null;
			for (String k : keys) {
				${x_tableName}Cfg = getBy${primaryD_columnName}(Integer.valueOf(k));
				if (${x_tableName}Cfg == null) continue;
					${x_tableName}Cfgs.add(${x_tableName}Cfg);
			}
		}
		return ${x_tableName}Cfgs;
	}
	
	<#-- 聚集索引分页查询 -->
	public static List<${d_tableName}Cfg> getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},int page,int size,Integer pageCount){
		List<${d_tableName}Cfg> ${x_tableName}Cfgs = getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
		${x_tableName}Cfgs = ListEx.getPage(${x_tableName}Cfgs, page, size, pageCount);
		return ${x_tableName}Cfgs;
	}
	</#if>
	</#list>
	
	public static List<${d_tableName}Cfg> getAll(){
		<#list indexKeys as index>
		<#if index.indexName == "PRIMARY">
		return new ArrayList<${d_tableName}Cfg>(<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.values());
		</#if>
		</#list>
	}
	
	public static List<${d_tableName}Cfg> getAll(int page,int size){
		List<${d_tableName}Cfg> ${x_tableName}Cfgs = getAll();
		${x_tableName}Cfgs = ListEx.getPageT(${x_tableName}Cfgs, page, size);
		return ${x_tableName}Cfgs;
	}
}