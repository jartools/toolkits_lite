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
	<#-- 唯一索引查询 -->
	<#if index.unique>
	public static ${d_tableName} getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName}){
		Jedis jedis = null;
		${d_tableName} ${x_tableName} = null;
		try{
			jedis = JedisTookits.getJedis();
			${x_tableName} = getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(jedis,${index.all_x_columnName});
		}catch (Exception e) {
			
		}finally{
			 JedisTookits.returnJedis(jedis);
		}
		saveUqVal(${x_tableName});
		return ${x_tableName};
	}
	
	<#if index.indexName == "PRIMARY">
	// 数据库唯一标识字段名称(主键)
	static final String dbID = "${index.all_x_columnName}";
	static final String dbIDHead = "${index.all_x_columnName}:";
	
	public static ${d_tableName} getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(Jedis jedis,${index.all_basicType_x_columnName}) throws Exception{
		if(jedis == null)
			return null;
		${d_tableName} ${x_tableName} = null;
		String key = "Object:${d_tableName}";
		String field = PStr.b(dbIDHead,${index.all_x_columnName}).e();
		${x_tableName} = ${d_tableName}.createForJson(jedis.hget(key,field));
		return ${x_tableName};
	}
	<#else>
	public static ${d_tableName} getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(Jedis jedis,${index.all_basicType_x_columnName}) throws Exception{
		if(jedis == null)
			return null;
		
		${d_tableName} ${x_tableName} = null;
		
		String key_unique = "Indexes:${d_tableName}:${index.all_x_columnName}";
		String field_unique = PStr.b(${index.all_x_columnName}).e();
		String primaryKey = jedis.hget(key_unique,field_unique);
		if(primaryKey != null){
			String key = "Object:${d_tableName}";
			String field = PStr.b(dbIDHead,primaryKey).e();
			${x_tableName} = ${d_tableName}.createForJson(jedis.hget(key,field));
		}
		return ${x_tableName};
	}
	</#if>
	
	<#else>
	<#-- 聚集索引查询 -->
	public static List<${d_tableName}> getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName}){
		Jedis jedis = JedisTookits.getJedis();
		List<${d_tableName}> ${x_tableName}s = new ArrayList<${d_tableName}>();
		try{
			String key = PStr.b("Indexes:${d_tableName}:${index.all_x_columnName}:",${index.all_x_columnName}).e();
			Set<String> setStr = jedis.smembers(key);
			if(setStr != null){
				key = "Object:${d_tableName}";
				String[] fieldArray = {};
				fieldArray = setStr.toArray(fieldArray);
				List<String> jsons = jedis.hmget(key,fieldArray);
				${x_tableName}s = ${d_tableName}.createForJson(jsons);
				loadCaches(${x_tableName}s);
			}
		}catch (Exception e) {
			
		}finally{
			 JedisTookits.returnJedis(jedis);
		}
		return ${x_tableName}s;
	}
	
	
	public static List<${d_tableName}> getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},int page,int size){
		List<${d_tableName}> ${x_tableName}s = getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
		${x_tableName}s = ListEx.getPageT(${x_tableName}s, page, size);
		return ${x_tableName}s;
	}
	
	</#if>
	</#list>
	
	public static void loadCache(${d_tableName} ${x_tableName}){
		Jedis jedis = JedisTookits.getJedis();
		try{
			Pipeline p = jedis.pipelined();
			loadCache(${x_tableName},p);
			p.sync();
		}catch (Exception e) {
			
		}finally{
			 JedisTookits.returnJedis(jedis);
		}
	}
	
	/**
	 * 保存unique唯一属性值
	 */
	static public void saveUqVal(${d_tableName} ${x_tableName}){
		if(${x_tableName} == null)
			return;
		<#assign i=0 />
		<#list indexKeys as index>
		<#if index.unique>
		<#if index.indexName == "PRIMARY">
		<#else>
		<#if i==0>
		String tmpVal = "";
		<#assign i=1 />
		</#if>
		
		tmpVal = PStr.b(${index.all_x_columnName_get}).e();//${index.all_d_columnName_get}
		${x_tableName}.set<#list index.d_columnNames as d_columnName>${d_columnName}</#list>Index(tmpVal);
		</#if>
		</#if>
		</#list>
	}
	 
	/**
	 * 加载一个缓存 通道
	 */
	public static void loadCache(${d_tableName} ${x_tableName},Pipeline p){
<#assign i=0 />
<#assign u=0 />
<#list indexKeys as index>
	<#if index.unique>
		<#if index.indexName == "PRIMARY">
		int primaryKeyInt = ${x_tableName}.get${primaryD_columnName}();//${index.all_x_columnName_get}
		String primaryKey = String.valueOf(primaryKeyInt);
		// ---------------------- 主键索引  Indexes----------------------
		String key = "Object:${d_tableName}";
		String field = PStr.b(dbIDHead,primaryKey).e();
		String data = ${x_tableName}.toJson();
		p.hset(key,field,data);
		<#else>
			<#if i==0>
		// ---------------------- 唯一索引  Indexes----------------------
		key = "Indexes:${d_tableName}:";
		String tmpKey = "";
		saveUqVal(${x_tableName});
			<#assign i=1 />
			</#if>
		// ---------------------- 唯一索引  key,field[${index.all_x_columnName_get}],val[主键primaryKey]
		tmpKey = PStr.b(key,"${index.all_x_columnName}").e();
		field = PStr.b(${index.all_x_columnName_get}).e();
		p.hset(tmpKey,field,primaryKey);
		
		// ---------------------- 唯一索引  key,field[主键primaryKey],val[${index.all_x_columnName_get}]
		// tmpKey = PStr.b(key,"${index.all_x_columnName}_Index").e();
		// p.hset(tmpKey,primaryKey,field);
		</#if>
	<#else>
		<#if i==0>
		key = "Indexes:${d_tableName}:";
		String tmpKey = "";
		<#assign i=1 />
		</#if>
		
		<#if u==0>
		// ---------------------- 聚集索引  Indexes----------------------
		<#assign u=1 />
		</#if>
		tmpKey = PStr.b(key,"${index.all_x_columnName}:",${index.all_x_columnName_get}).e();
		field = PStr.b(dbIDHead,primaryKey).e();
		p.sadd(tmpKey, field);
	</#if>
</#list>
	}
	
	/**
	 * 加载一组缓存
	 */
	public static void loadCaches(List<${d_tableName}> ${x_tableName}s){
		if(${x_tableName}s == null || ${x_tableName}s.isEmpty())
			return;
		Jedis jedis = JedisTookits.getJedis();
		try{
			Pipeline p = jedis.pipelined();
			for(${d_tableName} ${x_tableName} : ${x_tableName}s){
				clearAgoCache(${x_tableName},p);
				loadCache(${x_tableName},p);
			}
			p.sync();
		}catch (Exception e) {
			
		}finally{
			 JedisTookits.returnJedis(jedis);
		}
	}
	
	public static void clearCache(${d_tableName} ${x_tableName}){
		Jedis jedis = JedisTookits.getJedis();
		try{
			Pipeline p = jedis.pipelined();
			clearCache(${x_tableName},p);
			p.sync();
		}catch (Exception e) {
			
		}finally{
			 JedisTookits.returnJedis(jedis);
		}
	}
	
	/**
	 * 清空一个缓存
	 */
	public static void clearCache(${d_tableName} ${x_tableName},Pipeline p){
	
		clearAgoCache(${x_tableName},p);
		
<#assign i=0 />
<#assign u=0 />
<#list indexKeys as index>
	<#if index.unique>
		<#if index.indexName == "PRIMARY">
		int primaryKeyInt = ${x_tableName}.get${primaryD_columnName}();//${index.all_x_columnName_get}
		String primaryKey = String.valueOf(primaryKeyInt);
		// ---------------------- 主键索引  Indexes----------------------
		String key = "Object:${d_tableName}";
		String field = PStr.b(dbIDHead,primaryKey).e();
		p.hdel(key,field);
		<#else>
			<#if i==0>
		// ---------------------- 唯一索引  Indexes----------------------
		key = "Indexes:${d_tableName}:";
		String tmpKey = "";
			<#assign i=1 />
			</#if>
		
		// ---------------------- 唯一索引  key,field[${index.all_x_columnName_get}],val[主键primaryKey]
		tmpKey = PStr.b(key,"${index.all_x_columnName}").e();
		field = PStr.b(${index.all_x_columnName_get}).e();
		p.hdel(tmpKey,field);
		</#if>
	<#else>
		<#if i==0>
		key = "Indexes:${d_tableName}:";
		String tmpKey = "";
		<#assign i=1 />
		</#if>
		
		<#if u==0>
		// ---------------------- 聚集索引  Indexes----------------------
		<#assign u=1 />
		</#if>
		tmpKey = PStr.b(key,"${index.all_x_columnName}:",${index.all_x_columnName_get}).e();
		p.srem(tmpKey, primaryKey);
	</#if>
</#list>
	}
	
	/**
	 * 清空一个缓存对象上面的唯一属性值，该值第一加载进来保存在对象实体上面
	 */
	public static void clearAgoCache(${d_tableName} ${x_tableName},Pipeline p){
<#assign i=0 />
<#list indexKeys as index>
	<#if index.unique>
		<#if index.indexName == "PRIMARY">
		<#else>
			<#if i==0>
		// ---------------------- 唯一索引  Indexes----------------------
		String key = "Indexes:${d_tableName}:";
		String field = "";
		String tmpKey = "";
			<#assign i=1 />
			</#if>
		tmpKey = PStr.b(key,"${index.all_x_columnName}").e();
		field = ${x_tableName}.get<#list index.d_columnNames as d_columnName>${d_columnName}</#list>Index();
		if(field != null){
			p.hdel(tmpKey,field);
		}
		</#if>
	</#if>
</#list>
	}
	
	/**
	 * 清空一组缓存
	 */
	public static void clearCaches(List<${d_tableName}> ${x_tableName}s){
		Jedis jedis = JedisTookits.getJedis();
		try{
			Pipeline p = jedis.pipelined();
			for(${d_tableName} ${x_tableName} : ${x_tableName}s){
				clearCache(${x_tableName},p);
			}
			p.sync();
		}catch (Exception e) {
			
		}finally{
			 JedisTookits.returnJedis(jedis);
		}
	}
	
	//添加jedis原子性质
	static private final String keyJedisAtomic = "atomicKey";
	
	static public void setAtomicId(long nv){
		Jedis jedis = null;
		try{
			jedis = JedisTookits.getJedis();
			jedis.hset(keyJedisAtomic,dbIDHead+"${x_tableName}",String.valueOf(nv));
		}catch (Exception e) {
			
		}finally{
			 JedisTookits.returnJedis(jedis);
		}
	}
	
	static public int getAtomicId(){
		Jedis jedis = null;
		int result = 0;
		try{
			jedis = JedisTookits.getJedis();
			long incr = jedis.hincrBy(keyJedisAtomic,dbIDHead+"${x_tableName}",1);
			result = (int)incr;
		}catch (Exception e) {
			
		}finally{
			 JedisTookits.returnJedis(jedis);
		}
		return result;
	}
	
	public static ${d_tableName} insert(${d_tableName} ${x_tableName}){
		// ${x_tableName} = ${d_tableName}Dao.insert(${x_tableName}); 
		if(${x_tableName} != null){
			int id = ${x_tableName}.get${primaryD_columnName}();
			int atomicId = getAtomicId();
			if(id > atomicId){
				setAtomicId(id);
			}else{
				${x_tableName}.set${primaryD_columnName}(atomicId);
			}
			loadCache(${x_tableName});
		}
    	return ${x_tableName};
    }
    
    public static ${d_tableName} update(${d_tableName} ${x_tableName}){
    	// ${x_tableName} = ${d_tableName}Dao.update(${x_tableName});
    	if(${x_tableName} != null){
    		clearCache(${x_tableName});
			loadCache(${x_tableName});
		}
    	return ${x_tableName};
    }
    
    public static boolean delete(${d_tableName} ${x_tableName}){
    	// boolean bool = ${d_tableName}Dao.delete(${x_tableName});
    	boolean bool = ${x_tableName} != null;
    	if(bool){
    		clearCache(${x_tableName});
    	}
    	return bool;
    }
    
    /**
	 * 全部加载进内存(慎用)
	 */
    public static List<${d_tableName}> getCacheAll(){
		Jedis jedis = JedisTookits.getJedis();
		List<${d_tableName}> ${x_tableName}s = new ArrayList<${d_tableName}>();
		try{
			String key = "Object:${d_tableName}";
			List<String> jsons = jedis.hvals(key);
			${x_tableName}s = ${d_tableName}.createForJson(jsons);
		}catch (Exception e) {
			
		}finally{
			 JedisTookits.returnJedis(jedis);
		}
		return ${x_tableName}s;
	}
	
	/**
	 * 全部加载进内存(慎用)
	 */
	public static List<${d_tableName}> getAll(){
		Jedis jedis = JedisTookits.getJedis();
		List<${d_tableName}> ${x_tableName}s = new ArrayList<${d_tableName}>();
		try{
			// if(!isLoadAll){
				// ${x_tableName}s = ${d_tableName}Dao.getAll();
				// loadCaches(${x_tableName}s);
				// isLoadAll = true;
			// }else{
			String key = "Object:${d_tableName}";
			List<String> jsons = jedis.hvals(key);
			${x_tableName}s = ${d_tableName}.createForJson(jsons);			
			// }
		}catch (Exception e) {
			
		}finally{
			JedisTookits.returnJedis(jedis);
		}
		return ${x_tableName}s;
	}
	
	/**
	 * 全部加载进内存(慎用)
	 */
	public static List<${d_tableName}> getAllByPage(int page,int size){
		List<${d_tableName}> ${x_tableName}s = getAll();
		${x_tableName}s = ListEx.getPageT(${x_tableName}s, page, size);
		return ${x_tableName}s;
	}
}