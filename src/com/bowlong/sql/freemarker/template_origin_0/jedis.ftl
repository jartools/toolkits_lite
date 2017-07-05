package ${packageName};

<#list jedisImports as imports>
import ${imports};
</#list>
/**
 * ${tableName}
 */
public class ${d_tableName}Jedis{

	private static boolean isLoadAll = false;
	
	public static boolean isLoadAll() {
		return isLoadAll;
	}

	public static void setLoadAll(boolean isLoadAll) {
		${d_tableName}Jedis.isLoadAll = isLoadAll;
	}
	
	<#list indexKeys as index>
	/**
	 * 根据(<#list index.columnNames as columnName> ${columnName} </#list>) 查询
	 */
	<#if index.unique>
	<#-- 唯一索引查询 -->
	public static ${d_tableName} getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName}){
		Jedis jedis = SK_Jedis.getInstance().getJedis();
		${d_tableName} ${x_tableName} = null;
		try{
		<#if index.indexName == "PRIMARY">
			String key = "${d_tableName}_Object";
			String field = SK_Plus.b("${index.all_x_columnName}:",${index.all_x_columnName}).e();
			${x_tableName} = ${d_tableName}.createForJson(jedis.hget(key,field));
		<#else>
			String key = "${d_tableName}_Index";
			String field = SK_Plus.b("${index.all_x_columnName}:",${index.all_x_columnName}).e();
			String primaryKey = jedis.hget(key,field);
			if(primaryKey!=null){
				${x_tableName} = getBy${primaryD_columnName}(Integer.valueOf(primaryKey));
			}
		</#if>
			if(${x_tableName} == null){
				${x_tableName} = ${d_tableName}Dao.getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
				if(${x_tableName}!=null){
					loadCache(${x_tableName});
				}
			}
		}catch (Exception e) {
			
		}finally{
			 SK_Jedis.getInstance().returnJedis(jedis);
		}
		
		return ${x_tableName};
	}
	
	<#else>
	<#-- 聚集索引查询 -->
	public static List<${d_tableName}> getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName}){
		Jedis jedis = SK_Jedis.getInstance().getJedis();
		List<${d_tableName}> ${x_tableName}s = new ArrayList<${d_tableName}>();
		try{
			String key = "${d_tableName}_Index";
			String field = SK_Plus.b(key,"${index.all_x_columnName}:",${index.all_x_columnName}).e();
			Set<String> setStr = jedis.smembers(field);
			if(setStr!=null){
				key = "${d_tableName}_Object";
				String[] fieldArray = (String[]) setStr.toArray();
				List<String> jsons = jedis.hmget(key,fieldArray);
				${x_tableName}s = ${d_tableName}.createForJson(jsons);
			}else{
				${x_tableName}s = ${d_tableName}Dao.getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
				if(${x_tableName}s!=null && !${x_tableName}s.isEmpty()){
					loadCaches(${x_tableName}s);
				}
			}
			
		}catch (Exception e) {
			
		}finally{
			 SK_Jedis.getInstance().returnJedis(jedis);
		}
		return ${x_tableName}s;
	}
	
	
	public static List<${d_tableName}> getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},int page,int size,Integer pageCount){
		List<${d_tableName}> ${x_tableName}s = getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
		${x_tableName}s = SK_List.getPage(${x_tableName}s, page, size, pageCount);
		return ${x_tableName}s;
	}
	
	</#if>
	</#list>

	public static void loadCache(${d_tableName} ${x_tableName}){
		Jedis jedis = SK_Jedis.getInstance().getJedis();
		try{
			Pipeline p = jedis.pipelined();
			loadCache(${x_tableName},p);
			p.sync();
		}catch (Exception e) {
			
		}finally{
			 SK_Jedis.getInstance().returnJedis(jedis);
		}
	}
	
	/**
	 * 加载一个缓存
	 */
	public static void loadCache(${d_tableName} ${x_tableName},Pipeline p){
		<#assign i=0 />
		<#assign u=0 />
		<#list indexKeys as index>
		<#if index.unique>
		<#if index.indexName == "PRIMARY">
		// ---------------------- 主键索引 ----------------------
		String key = "${d_tableName}_Object";
		String field = SK_Plus.b("${index.all_x_columnName}:",${index.all_x_columnName_get}).e();
		String data = ${x_tableName}.toJson();
		p.hset(key,field,data);
		
		<#else>
		<#if i==0>
		// ---------------------- 唯一索引 ----------------------
		String primaryKey = String.valueOf(${x_tableName}.get${primaryD_columnName}());
		key = "${d_tableName}_Index";
		
		<#assign i=1 />
		</#if>
		field = SK_Plus.b("${index.all_x_columnName}:",${index.all_x_columnName_get}).e();
		p.hset(key,field,primaryKey);
		${x_tableName}.set<#list index.d_columnNames as d_columnName>${d_columnName}</#list>Index(field);
		
		</#if>
		<#else>
		<#if i==0>
		String primaryKey = String.valueOf(${x_tableName}.get${primaryD_columnName}());
		key = "${d_tableName}_Index";
		<#assign i=1 />
		</#if>
		<#if u==0>
		// ---------------------- 聚集索引 ----------------------
		<#assign u=1 />
		</#if>
		field = SK_Plus.b(key,"${index.all_x_columnName}:",${index.all_x_columnName_get}).e();
		p.sadd(field, primaryKey);
		
		</#if>
		</#list>
	}
	
	/**
	 * 加载一组缓存
	 */
	public static void loadCaches(List<${d_tableName}> ${x_tableName}s){
		Jedis jedis = SK_Jedis.getInstance().getJedis();
		try{
			Pipeline p = jedis.pipelined();
			for(${d_tableName} ${x_tableName} : ${x_tableName}s){
				loadCache(${x_tableName},p);
			}
			p.sync();
		}catch (Exception e) {
			
		}finally{
			 SK_Jedis.getInstance().returnJedis(jedis);
		}
	}
	
	public static void clearCache(${d_tableName} ${x_tableName}){
		Jedis jedis = SK_Jedis.getInstance().getJedis();
		try{
			Pipeline p = jedis.pipelined();
			clearCache(${x_tableName},p);
			p.sync();
		}catch (Exception e) {
			
		}finally{
			 SK_Jedis.getInstance().returnJedis(jedis);
		}
	}
	
	/**
	 * 清空一个缓存
	 */
	public static void clearCache(${d_tableName} ${x_tableName},Pipeline p){
		<#assign i=0 />
		<#assign u=0 />
		<#list indexKeys as index>
		<#if index.unique>
		<#if index.indexName == "PRIMARY">
		// ---------------------- 主键索引 ----------------------
		String key = "${d_tableName}_Object";
		String field = SK_Plus.b("${index.all_x_columnName}:",${index.all_x_columnName_get}).e();
		p.hdel(key,field);
		
		<#else>
		<#if i==0>
		// ---------------------- 唯一索引 ----------------------
		key = "${d_tableName}_Index";
		
		<#assign i=1 />
		</#if>
		// field = SK_Plus.b("${index.all_x_columnName}:",${index.all_x_columnName_get}).e();
		field = ${x_tableName}.get<#list index.d_columnNames as d_columnName>${d_columnName}</#list>Index();
		if(field!=null){
			p.hdel(key,field);
		}
		</#if>
		<#else>
		<#if i==0>
		key = "${d_tableName}_Index";
		<#assign i=1 />
		</#if>
		<#if u==0>
		// ---------------------- 聚集索引 ----------------------
		String primaryKey = String.valueOf(${x_tableName}.get${primaryD_columnName}());
		<#assign u=1 />
		</#if>
		field = SK_Plus.b(key,"${index.all_x_columnName}:",${index.all_x_columnName_get}).e();
		p.srem(field, primaryKey);
		
		</#if>
		</#list>
	}
	/**
	 * 清空一组缓存
	 */
	public static void clearCaches(List<${d_tableName}> ${x_tableName}s){
		Jedis jedis = SK_Jedis.getInstance().getJedis();
		try{
			Pipeline p = jedis.pipelined();
			for(${d_tableName} ${x_tableName} : ${x_tableName}s){
				clearCache(${x_tableName},p);
			}
			p.sync();
		}catch (Exception e) {
			
		}finally{
			 SK_Jedis.getInstance().returnJedis(jedis);
		}
	}
	
	public static ${d_tableName} insert(${d_tableName} ${x_tableName}){
		${x_tableName} = ${d_tableName}Dao.insert(${x_tableName});
		if(${x_tableName}!=null){
			loadCache(${x_tableName});
		}
    	return ${x_tableName};
    }
    
    public static ${d_tableName} update(${d_tableName} ${x_tableName}){
    	${x_tableName} = ${d_tableName}Dao.update(${x_tableName});
    	if(${x_tableName}!=null){
    		clearCache(${x_tableName});
			loadCache(${x_tableName});
		}
    	return ${x_tableName};
    }
    
    public static boolean delete(${d_tableName} ${x_tableName}){
    	boolean bool = ${d_tableName}Dao.delete(${x_tableName});
    	if(bool){
    		clearCache(${x_tableName});
    	}
    	return bool;
    }
    
    /**
	 * 全部加载进内存(慎用)
	 */
    public static List<${d_tableName}> getCacheAll(){
		Jedis jedis = SK_Jedis.getInstance().getJedis();
		List<${d_tableName}> ${x_tableName}s = new ArrayList<${d_tableName}>();
		try{
			String key = "${d_tableName}_Object";
			List<String> jsons = jedis.hvals(key);
			${x_tableName}s = ${d_tableName}.createForJson(jsons);
		}catch (Exception e) {
			
		}finally{
			 SK_Jedis.getInstance().returnJedis(jedis);
		}
		return ${x_tableName}s;
	}
	
	/**
	 * 全部加载进内存(慎用)
	 */
	public static List<${d_tableName}> getAll(){
		Jedis jedis = SK_Jedis.getInstance().getJedis();
		List<${d_tableName}> ${x_tableName}s = new ArrayList<${d_tableName}>();
		try{
			if(!isLoadAll){
				${x_tableName}s = ${d_tableName}Dao.getAll();
				loadCaches(${x_tableName}s);
				isLoadAll = true;
			}else{
				String key = "${d_tableName}_Object";
				List<String> jsons = jedis.hvals(key);
				${x_tableName}s = ${d_tableName}.createForJson(jsons);			
			}
		}catch (Exception e) {
			
		}finally{
			 SK_Jedis.getInstance().returnJedis(jedis);
		}
		return ${x_tableName}s;
	}
	
	/**
	 * 全部加载进内存(慎用)
	 */
	public static List<${d_tableName}> getAllByPage(int page,int size,Integer pageCount){
		List<${d_tableName}> ${x_tableName}s = getAll();
		${x_tableName}s = SK_List.getPage(${x_tableName}s, page, size, pageCount);
		return ${x_tableName}s;
	}
}