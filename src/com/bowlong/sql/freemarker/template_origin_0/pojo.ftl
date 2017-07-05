package ${packageName};

<#list beanImports as imports>
import ${imports};
</#list>

/**
 * ${tableName}
 */
public class ${d_tableName} {

	public static final String TABLENAME = "${tableName}";
	public static final String CLASSNAME = "${d_tableName}"; 
	//Cache 中的索引
	<#list indexKeys as index>
	<#if index.unique>
	<#if index.indexName == "PRIMARY">
	<#else>
	private String  <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Index = null;
	
	public String get<#list index.d_columnNames as d_columnName>${d_columnName}</#list>Index(){
		return <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Index;
	}
	
	public void set<#list index.d_columnNames as d_columnName>${d_columnName}</#list>Index(String <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Index){
		this.<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Index = <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Index;
	}
	
	</#if>
	<#else>
	</#if>
	</#list>
	
	/**
	 * 需要更新字段集
	 */
	private Map<String, Object> updateColumns;

	<#list columns as column>
	/** ${column.remarks} */
	private ${column.basicType} ${column.x_columnName};
	
	</#list>
	
	public ${d_tableName}() {
		super();
	}
	
	public ${d_tableName}(${all_basicType_x_columnName}) {
		super();
		<#list columns as column>
		this.${column.x_columnName} = ${column.x_columnName};
		</#list>
	}
	
	<#-- 取得更新字段集 -->
	public Map<String, Object> getUpdateColumns() {
		if(updateColumns == null)
			updateColumns = new HashMap<String, Object>();
		return updateColumns;
	}
	
	<#-- Get And Set -->
	<#list columns as column>
	public ${column.basicType} get${column.d_columnName}() {
		return ${column.x_columnName};
	}
	
	public void set${column.d_columnName}(${column.basicType} ${column.x_columnName}) {
		this.${column.x_columnName} = ${column.x_columnName};
		addUpdateColumn("${column.columnName}",${column.x_columnName});
	}
	
	<#-- 数值类型字段Change操作 -->
	<#if column.basicType=="short" || column.basicType=="int" || column.basicType=="long" || column.basicType=="float" || column.basicType=="double">
	public void change${column.d_columnName}With(${column.basicType} ${column.x_columnName}){
		this.${column.x_columnName} += ${column.x_columnName};
		set${column.d_columnName}(this.${column.x_columnName});
	}
	
	public void change${column.d_columnName}WithMin(${column.basicType} ${column.x_columnName},${column.basicType} min){
		this.${column.x_columnName} += ${column.x_columnName};
		this.${column.x_columnName} = this.${column.x_columnName} < min ? min : this.${column.x_columnName};
		set${column.d_columnName}(this.${column.x_columnName});
	}
	
	public void change${column.d_columnName}WithMax(${column.basicType} ${column.x_columnName},${column.basicType} max){
		this.${column.x_columnName} += ${column.x_columnName};
		this.${column.x_columnName} = this.${column.x_columnName} > max ? max : this.${column.x_columnName};
		set${column.d_columnName}(this.${column.x_columnName});
	}
	
	public void change${column.d_columnName}WithMaxMin(${column.basicType} ${column.x_columnName},${column.basicType} max,${column.basicType} min){
		this.${column.x_columnName} += ${column.x_columnName};
		this.${column.x_columnName} = this.${column.x_columnName} < min ? min : this.${column.x_columnName};
		this.${column.x_columnName} = this.${column.x_columnName} > max ? max : this.${column.x_columnName};
		set${column.d_columnName}(this.${column.x_columnName});
	}	
	</#if>
	</#list>
	
	<#-- 主外键关联查询 -->
	<#list bindKeys as bindKey>
	<#if bindKey.pk>
	//${bindKey.fkColumnName}
	<#-- 主键 -->
	public ${bindKey.d_pkTableName} get${bindKey.d_pkTableName}Pk${bindKey.d_fkColumnName}(){
		return ${bindKey.d_pkTableName}Cache.getBy${bindKey.d_pkColumnName}(${bindKey.x_fkColumnName});
	}
	<#else>
	//${bindKey.pkColumnName}
	<#if bindKey.unique>
	<#-- 1对1 -->
	public ${bindKey.d_fkTableName} get${bindKey.d_fkTableName}sFk${bindKey.d_fkColumnName}(){
		return ${bindKey.d_fkTableName}Cache.getBy${bindKey.d_fkColumnName}(${bindKey.x_pkColumnName});
	}
	
	<#else>
	<#-- 1对多 -->
	public List<${bindKey.d_fkTableName}> get${bindKey.d_fkTableName}sFk${bindKey.d_fkColumnName}(){
		return ${bindKey.d_fkTableName}Cache.getBy${bindKey.d_fkColumnName}(${bindKey.x_pkColumnName});
	}
	
	</#if>
	</#if>
	</#list>
	<#-- 添加更新字段集 -->
	public void addUpdateColumn(String key, Object val) {
		Map<String, Object> map = getUpdateColumns();
		if (map == null)
			return;
		map.put(key, val);
	}
	
	<#-- 取得更新字段集 -->
	public void clearUpdateColumn() {
		Map<String, Object> map = getUpdateColumns();
		if (map == null)
			return;
		map.clear();
	}
	
	<#-- 对象转换 -->
	public Map<String, Object> toMap(){
        Map<String, Object> result = new HashMap<String, Object>();
        <#list columns as column>
        result.put("${column.x_columnName}", this.${column.x_columnName});
        </#list>
        return result;
    }
    
    public String toString(){
        return toMap().toString();
    }
    
    public String toJson(){
    	return JSON.toJSONString(toMap());
    }
    
    /**
     * 数据库源字段Map
     */
    public Map<String, Object> toColumnNameMap(){
        Map<String, Object> result = new HashMap<String, Object>();
        <#list columns as column>
        result.put("${column.columnName}", this.${column.x_columnName});
        </#list>
        return result;
    }
    
    public String toColumnNameString(){
        return toColumnNameMap().toString();
    }
    
    public byte[] toBytes() throws Exception {
    	try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
	    	<#list columns as column>
		    <#if column.basicType=="int">
		    SK_OutputStream.writeInt(out,this.${column.x_columnName});
		    <#elseif column.basicType=="long">
		    SK_OutputStream.writeLong(out,this.${column.x_columnName});
		    <#elseif column.basicType=="short">
		    SK_OutputStream.writeShort(out,this.${column.x_columnName});
		    <#elseif column.basicType=="float">
		    SK_OutputStream.writeFloat(out,this.${column.x_columnName});
		    <#elseif column.basicType=="double">
		    SK_OutputStream.writeDouble(out,this.${column.x_columnName});
		    <#elseif column.basicType=="Date" || column.basicType=="java.util.Date">
		    SK_OutputStream.writeDate(out,this.${column.x_columnName});
		    <#elseif column.basicType=="String">
		    SK_OutputStream.writeString(out,this.${column.x_columnName});
		    </#if>
		    </#list>
		    return out.toByteArray();
    	}catch (Exception e) {
            throw e;
        }
    }
    
    public byte[] toSKBytes() throws Exception {
    	try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
		    SK_OutputStream.writeMap(out,toMap());
		    return out.toByteArray();
    	}catch (Exception e) {
            throw e;
        }
    }
    
     public static ${d_tableName} createForBytes(byte[] _byte) throws Exception {
     	try (ByteArrayInputStream in = new ByteArrayInputStream(_byte);) {
	     	${d_tableName} ${x_tableName} = new ${d_tableName}();
	    	<#list columns as column>
		    <#if column.basicType=="int">
		    ${x_tableName}.${column.x_columnName} = SK_InputStream.readInt(in,null);
		    <#elseif column.basicType=="long">
		    ${x_tableName}.${column.x_columnName} = SK_InputStream.readLong(in,null);
		    <#elseif column.basicType=="short">
		    ${x_tableName}.${column.x_columnName} = SK_InputStream.readShort(in,null);
		    <#elseif column.basicType=="float">
		    ${x_tableName}.${column.x_columnName} = SK_InputStream.readFloat(in,null);
		    <#elseif column.basicType=="double">
		    ${x_tableName}.${column.x_columnName} = SK_InputStream.readDouble(in,null);
		    <#elseif column.basicType=="Date" || column.basicType=="java.util.Date">
		    ${x_tableName}.${column.x_columnName} = SK_InputStream.readDate(in,null);
		    <#elseif column.basicType=="String">
		    ${x_tableName}.${column.x_columnName} = SK_InputStream.readString(in,null);
		    </#if>
		    </#list>
		    return ${x_tableName};
    	}catch (Exception e) {
            throw e;
        }
     }
     
     public static ${d_tableName} createForSKBytes(byte[] _byte) throws Exception {
     	try (ByteArrayInputStream in = new ByteArrayInputStream(_byte);) {
     		@SuppressWarnings("unchecked")
     		Map<String,Object> map = SK_InputStream.readMap(in,null);
	     	${d_tableName} ${x_tableName} = ${d_tableName}.createForMap(map);
		    return ${x_tableName};
    	}catch (Exception e) {
            throw e;
        }
     }
    
    /**
     * 根据list创建对象
     */
    public static List<${d_tableName}> createForColumnNameList(List<Map<String, Object>> list){
    	List<${d_tableName}> ${x_tableName}s = new ArrayList<${d_tableName}>();
		for (Map<String, Object> map : list) {
			${x_tableName}s.add(createForColumnNameMap(map));
		}
		return ${x_tableName}s;
    }
    
    /**
     * 根据map创建对象
     */
    public static ${d_tableName} createForColumnNameMap(Map<String, Object> map){
    	${d_tableName} obj = new ${d_tableName}();
		<#list columns as column>
	    <#if column.basicType=="int">
	    obj.${column.x_columnName} = SK_Map.getInt("${column.columnName}", map);
	    <#elseif column.basicType=="long">
	    obj.${column.x_columnName} = SK_Map.getLong("${column.columnName}", map);
	    <#elseif column.basicType=="short">
	    obj.${column.x_columnName} = SK_Map.getShort("${column.columnName}", map);
	    <#elseif column.basicType=="float">
	    obj.${column.x_columnName} = SK_Map.getFloat("${column.columnName}", map);
	    <#elseif column.basicType=="double">
	    obj.${column.x_columnName} = SK_Map.getDouble("${column.columnName}", map);
	    <#elseif column.basicType=="Date" || column.basicType=="java.util.Date">
	    obj.${column.x_columnName} = SK_Map.getDate("${column.columnName}", map);
	    <#elseif column.basicType=="String">
	    obj.${column.x_columnName} = SK_Map.getString("${column.columnName}", map);
	    </#if>
	    </#list>
        return obj;
    }
    
    /**
     * 根据list创建对象
     */
    public static List<${d_tableName}> createForList(List<Map<String, Object>> list){
    	List<${d_tableName}> ${x_tableName}s = new ArrayList<${d_tableName}>();
		for (Map<String, Object> map : list) {
			${x_tableName}s.add(createForColumnNameMap(map));
		}
		return ${x_tableName}s;
    }
    
    /**
     * 根据map创建对象
     */
    public static ${d_tableName} createForMap(Map<String, Object> map){
    	${d_tableName} obj = new ${d_tableName}();
		<#list columns as column>
	    <#if column.basicType=="int">
	    obj.${column.x_columnName} = SK_Map.getInt("${column.x_columnName}", map);
	    <#elseif column.basicType=="long">
	    obj.${column.x_columnName} = SK_Map.getLong("${column.x_columnName}", map);
	    <#elseif column.basicType=="short">
	    obj.${column.x_columnName} = SK_Map.getShort("${column.x_columnName}", map);
	    <#elseif column.basicType=="float">
	    obj.${column.x_columnName} = SK_Map.getFloat("${column.x_columnName}", map);
	    <#elseif column.basicType=="double">
	    obj.${column.x_columnName} = SK_Map.getDouble("${column.x_columnName}", map);
	    <#elseif column.basicType=="Date" || column.basicType=="java.util.Date">
	    obj.${column.x_columnName} = SK_Map.getDate("${column.x_columnName}", map);
	    <#elseif column.basicType=="String">
	    obj.${column.x_columnName} = SK_Map.getString("${column.x_columnName}", map);
	    </#if>
	    </#list>
        return obj;
    }
    
    public static ${d_tableName} createForJson(String json){
    	Map<String, Object> map = JSON.parseObject(json);
    	return createForMap(map);
    }
    
    public static List<${d_tableName}> createForJson(List<String> jsons){
    	List<${d_tableName}> ${x_tableName}s = new ArrayList<${d_tableName}>();
    	for(String json : jsons){
    		${x_tableName}s.add(createForJson(json));
    	}
    	return ${x_tableName}s;
    }
    
    /** 级联删除(延迟入库) */
    public boolean deleteAndSon(){
    	return false;
    }
    
    /** 级联删除(及时入库) */
    public boolean deleteAndSonAndFlush(){
    	return false;
    }
    
    /** 延迟插入数据库 */
    public ${d_tableName} insert(){
    	return ${d_tableName}Cache.insert(this);
    }
    /** 延迟更新数据库 */
    public ${d_tableName} update(){
    	return ${d_tableName}Cache.update(this);
    }
    /** 延迟删除数据库 */
    public boolean delete(){
    	return ${d_tableName}Cache.delete(this);
    }
    /** 即时插入数据库 */
    public ${d_tableName} insertAndFlush(){
    	return ${d_tableName}Cache.insertAndFlush(this);
    }
    /** 即时更新数据库 */
    public ${d_tableName} updateAndFlush(){
    	return ${d_tableName}Cache.updateAndFlush(this);
    }
    /** 即时删除数据库 */
    public boolean deleteAndFlush(){
    	return ${d_tableName}Cache.deleteAndFlush(this);
    }
}