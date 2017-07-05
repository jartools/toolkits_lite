package ${packageName};

<#list cacheImports as imports>
import ${imports};
</#list>
/**
 * ${tableName}
 */
public class ${d_tableName}Cache {
	private static AtomicInteger LASTID = new AtomicInteger();
	private static boolean isLoadAll = false;
	
	public static boolean isLoadAll() {
		return isLoadAll;
	}

	public static void setLoadAll(boolean isLoadAll) {
		${d_tableName}Cache.isLoadAll = isLoadAll;
	}
	//缓存
	<#list indexKeys as index>
	<#if index.unique>
	<#if index.indexName == "PRIMARY">
	static final Map<String, ${d_tableName}> <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache = SK_Collections.newSortedMap();
	<#else>
	// static final SK_IndexMap<String, String> <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache = new SK_IndexMap<String, String>();
	static final Map<String, String> <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache = new ConcurrentHashMap<String, String>();
	</#if>
	<#else>
	static final Map<String, Set<String>> <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache = new ConcurrentHashMap<String, Set<String>>();
	</#if>
	</#list>
	
	static final List<${d_tableName}> saveDelete = new CopyOnWriteArrayList<${d_tableName}>();
	
	static final List<${d_tableName}> saveInsert = new CopyOnWriteArrayList<${d_tableName}>();
	
	static final List<${d_tableName}> saveUpdate = new CopyOnWriteArrayList<${d_tableName}>();
	
	
	<#list indexKeys as index>
	/**
	 * 根据(<#list index.columnNames as columnName> ${columnName} </#list>) 查询
	 */
	<#if index.unique>
	<#-- 唯一索引查询 -->
	public static ${d_tableName} getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName}){
		${d_tableName} ${x_tableName} = null;
		String key = ${index.all_d_columnName_plus};
		<#if index.indexName == "PRIMARY">
		${x_tableName} = <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.get(key);
		
		<#else>
		key = <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.get(key);
		if(key!=null){
			${x_tableName} = ${primaryX_columnName}Cache.get(key);	
		} 
		</#if>
		if(${x_tableName}==null){
			//查询数据库
			${x_tableName} = ${d_tableName}Jedis.getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
			if(${x_tableName}!=null){
				//加入缓存
				loadCache(${x_tableName});
			}
		}
		return ${x_tableName};
	}
	
	<#else>
	<#-- 聚集索引查询 -->
	public static List<${d_tableName}> getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName}){
		List<${d_tableName}> ${x_tableName}s = new ArrayList<${d_tableName}>();
		String key = ${index.all_d_columnName_plus};
		Set<String> keys = <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.get(key);
		if(keys != null){
			${d_tableName} ${x_tableName} = null;
			for (String k : keys) {
				${x_tableName} = getBy${primaryD_columnName}(Integer.valueOf(k));
				if (${x_tableName} == null) continue;
					${x_tableName}s.add(${x_tableName});
			}
		}else{
			${x_tableName}s = ${d_tableName}Jedis.getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
			if(!${x_tableName}s.isEmpty()){
				loadCaches(${x_tableName}s);
			}
		}
		return ${x_tableName}s;
	}
	
	<#-- 聚集索引分页查询 -->
	public static List<${d_tableName}> getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},int page,int size,Integer pageCount){
		List<${d_tableName}> ${x_tableName}s = getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
		${x_tableName}s = SK_List.getPage(${x_tableName}s, page, size, pageCount);
		return ${x_tableName}s;
	}
	</#if>
	</#list>
	
	public static List<${d_tableName}> getCacheAll(){
		<#list indexKeys as index>
		<#if index.indexName == "PRIMARY">
		return new ArrayList<${d_tableName}>(<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.values());
		</#if>
		</#list>
	}
	
	/**
	 * 全部加载进内存(慎用)
	 */
	public static List<${d_tableName}> getAll(){
		List<${d_tableName}> ${x_tableName}s = new ArrayList<${d_tableName}>();
		if(!isLoadAll){
			${x_tableName}s = ${d_tableName}Jedis.getAll();
			loadCaches(${x_tableName}s);
			isLoadAll = true;
		}else{
			<#list indexKeys as index>
			<#if index.indexName == "PRIMARY">
			${x_tableName}s = new ArrayList<${d_tableName}>(<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.values());
			</#if>
			</#list>
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
	
	/**
	 * 加载缓存
	 */
	public static void loadCaches(List<${d_tableName}> ${x_tableName}s){
		for(${d_tableName} ${x_tableName} : ${x_tableName}s){
			loadCache(${x_tableName});
		}
	}
	
	/**
	 * 加载缓存
	 */
	public static void loadCache(${d_tableName} ${x_tableName}){
		<#list indexKeys as index>
		<#if index.unique>
		<#if index.indexName == "PRIMARY">
		<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.put(${index.all_d_columnName_get},${x_tableName});
		<#else>
		<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.put(${index.all_d_columnName_get},String.valueOf(${x_tableName}.get${primaryD_columnName}()));
		</#if>
		<#else>
		Set<String> <#list index.x_columnNames as x_columnName>${x_columnName}</#list>set = <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.get(String.valueOf(${index.all_d_columnName_get}));
		if(<#list index.x_columnNames as x_columnName>${x_columnName}</#list>set == null){
			<#list index.x_columnNames as x_columnName>${x_columnName}</#list>set = new HashSet<String>();
		}
		<#list index.x_columnNames as x_columnName>${x_columnName}</#list>set.add(String.valueOf(${x_tableName}.get${primaryD_columnName}()));
		<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.put(${index.all_d_columnName_get},<#list index.x_columnNames as x_columnName>${x_columnName}</#list>set);
		</#if>
		</#list>
	}
	
	
	
	/**
	 * 清空缓存 
	 * clearType 1:只清空缓存关系  2:只清空对象   3.全部清空
	 * saveType  0:不保存 1：保存database 2:保存redis 3:保存全部(redis+database)
	 */
	public static void clearCache(${d_tableName} ${x_tableName},int clearType,int saveType){
		<#list indexKeys as index>
		<#if index.unique>
		<#if index.indexName == "PRIMARY">
		if(clearType == 2){
			<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.remove(${index.all_d_columnName_get});
			return;
		}
		if(clearType == 3){
			<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.remove(${index.all_d_columnName_get});
		}
		<#else>
		<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.remove(${x_tableName}.get<#list index.d_columnNames as d_columnName>${d_columnName}</#list>Index());
		</#if>
		<#else>
		Set<String> <#list index.x_columnNames as x_columnName>${x_columnName}</#list>set = <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.get(String.valueOf(${index.all_d_columnName_get}));
		if(<#list index.x_columnNames as x_columnName>${x_columnName}</#list>set == null){
			<#list index.x_columnNames as x_columnName>${x_columnName}</#list>set = new HashSet<String>();
		}
		if (<#list index.x_columnNames as x_columnName>${x_columnName}</#list>set.contains(String.valueOf(${x_tableName}.get${primaryD_columnName}()))) {
			<#list index.x_columnNames as x_columnName>${x_columnName}</#list>set.remove(String.valueOf(${x_tableName}.get${primaryD_columnName}()));
		}
		<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.put(${index.all_d_columnName_get},<#list index.x_columnNames as x_columnName>${x_columnName}</#list>set);
		</#if>
		</#list>
		switch (saveType) {
		case 1:
			saveDatabase(${x_tableName});
			break;
		case 2:
			saveRedis(${x_tableName});
			break;
		case 3:
			saveAll(${x_tableName});
			break;
		default:
			break;
		}
	}
	
	/**
	 * 清空缓存
	 * clearType 1:只清空缓存关系  2:只清空对象   3.全部清空
	 * saveType  0:不保存 1：保存database 2:保存redis 3:保存全部(redis+database)
	 */
	public static void clearCaches(List<${d_tableName}> ${x_tableName}s,int clearType,int saveType){
		for(${d_tableName} ${x_tableName} : ${x_tableName}s){
			clearCache(${x_tableName},clearType,0);
		}
		switch (saveType) {
		case 1:
			saveDatabase(${x_tableName}s);
			break;
		case 2:
			saveRedis(${x_tableName}s);
			break;
		case 3:
			saveAll(${x_tableName}s);
			break;
		default:
			break;
		}
	}
	
	public static ${d_tableName} insert(${d_tableName} ${x_tableName}){
		return insert(${x_tableName},false);
    }
    
    public static ${d_tableName} update(${d_tableName} ${x_tableName}){
    	return update(${x_tableName},false);
    }
    
    public static boolean delete(${d_tableName} ${x_tableName}){
    	return delete(${x_tableName},false);
    }
    
    private static ${d_tableName} insert(${d_tableName} ${x_tableName},boolean isFlush){
		int id = LASTID.get();
    	if(id < 1){
    		${x_tableName} = ${d_tableName}Jedis.insert(${x_tableName});
    		LASTID.set(${x_tableName}.get${primaryD_columnName}());
    	}else{
    		int _id = ${x_tableName}.get${primaryD_columnName}();
			if (_id < 1) {
				LASTID.set(LASTID.incrementAndGet());
			} else if (${x_tableName}.get${primaryD_columnName}() > id) {
				LASTID.set(${x_tableName}.get${primaryD_columnName}());
			}
    		//加入定时器
    	}
    	loadCache(${x_tableName});
    	if(!isFlush){
	    	if (!saveInsert.contains(${x_tableName})) {
				saveInsert.add(${x_tableName});
			}
		}
    	return ${x_tableName};
    }
    
    private static ${d_tableName} update(${d_tableName} ${x_tableName},boolean isFlush){
    	clearCache(${x_tableName},1,0);
    	loadCache(${x_tableName});
    	//加入定时器
    	if(!isFlush){
    		if (!saveUpdate.contains(${x_tableName})) {
				saveUpdate.add(${x_tableName});
			}
		}else{
			saveUpdate.remove(${x_tableName});
		}
    	return ${x_tableName};
    }
    
    private static boolean delete(${d_tableName} ${x_tableName},boolean isFlush){
    	clearCache(${x_tableName},3,0);
    	//加入定时器
    	if(!isFlush){
    		if (!saveDelete.contains(${x_tableName})) {
				saveUpdate.remove(${x_tableName});
				saveInsert.remove(${x_tableName});
				saveDelete.add(${x_tableName});
			}
    	}else{
    		saveUpdate.remove(${x_tableName});
			saveInsert.remove(${x_tableName});
			saveDelete.remove(${x_tableName});
    	}
    	return false;
    }
    
    public static ${d_tableName} updateAndFlush(${d_tableName} ${x_tableName}){
    	update(${x_tableName},true);
    	return ${d_tableName}Jedis.update(${x_tableName});
    }
    
    public static ${d_tableName} insertAndFlush(${d_tableName} ${x_tableName}){
    	int id = LASTID.get();
    	insert(${x_tableName},true);
    	if(id > 0){
    		${x_tableName} = ${d_tableName}Jedis.insert(${x_tableName});
    	}
    	return ${x_tableName};
    }
    
    public static boolean deleteAndFlush(${d_tableName} ${x_tableName}){
    	delete(${x_tableName},true);
    	return ${d_tableName}Jedis.delete(${x_tableName});
    }
    
    
    
    // ******************************** 持久化操作 ********************************
    public static void saveDatabase(){
    	${d_tableName}Dao.deleteBatch(saveDelete);
    	${d_tableName}Dao.insertBatch(saveInsert);
    	${d_tableName}Dao.updateBatch(saveUpdate);
    	clearSave();
    }
    
    public static void saveDatabase(${d_tableName} ${x_tableName}){
    	if (saveDelete.remove(${x_tableName}))
			${d_tableName}Dao.delete(${x_tableName});
		if (saveInsert.remove(${x_tableName}))
			${d_tableName}Dao.insert(${x_tableName});
		if (saveUpdate.remove(${x_tableName}))
			${d_tableName}Dao.update(${x_tableName});
    }
    
    public static void saveDatabase(List<${d_tableName}> ${x_tableName}s){
    	if (saveDelete.removeAll(${x_tableName}s))
			${d_tableName}Dao.deleteBatch(${x_tableName}s);
		if (saveInsert.removeAll(${x_tableName}s))
			${d_tableName}Dao.insertBatch(${x_tableName}s);
		if (saveUpdate.removeAll(${x_tableName}s))
			${d_tableName}Dao.updateBatch(${x_tableName}s);
    }
    
    public static void saveRedis(){
    	${d_tableName}Jedis.clearCaches(saveDelete);
    	${d_tableName}Jedis.loadCaches(saveInsert);
    	${d_tableName}Jedis.loadCaches(saveUpdate);
    	clearSave();
    }
    
    public static void saveRedis(${d_tableName} ${x_tableName}){
    	if (saveDelete.remove(${x_tableName}))
			${d_tableName}Jedis.clearCache(${x_tableName});
		if (saveInsert.remove(${x_tableName}))
			${d_tableName}Jedis.loadCache(${x_tableName});
		if (saveUpdate.remove(${x_tableName}))
			${d_tableName}Jedis.loadCache(${x_tableName});
    }
    
    public static void saveRedis(List<${d_tableName}> ${x_tableName}s){
    	if (saveDelete.removeAll(${x_tableName}s))
			${d_tableName}Jedis.clearCaches(${x_tableName}s);
		if (saveInsert.removeAll(${x_tableName}s))
			${d_tableName}Jedis.loadCaches(${x_tableName}s);
		if (saveUpdate.removeAll(${x_tableName}s))
			${d_tableName}Jedis.loadCaches(${x_tableName}s);
    }
    
    public static void saveAll(){
   		saveDatabase();
   		saveRedis();
   		clearSave();
    }
    
    public static void saveAll(${d_tableName} ${x_tableName}){
   		saveDatabase(${x_tableName});
   		saveRedis(${x_tableName});
    }
    
    public static void saveAll(List<${d_tableName}> ${x_tableName}s){
   		saveDatabase(${x_tableName}s);
   		saveRedis(${x_tableName}s);
    }
    
    private static void clearSave(){
    	saveDelete.clear();
    	saveInsert.clear();
    	saveUpdate.clear();
    }
    
    // ******************************** 级联操作 ********************************
    
    /**
	 * 级联加载缓存
	 */
	public static void loadCacheCascade(${d_tableName} ${x_tableName}){
	
		loadCache(${x_tableName});
		
		<#list bindKeys as bindKey>
		<#if !bindKey.pk>
		<#if bindKey.unique>
		<#-- 1对1 -->
		${bindKey.d_fkTableName}Cache.loadCacheCascade(${bindKey.d_fkTableName}Cache.getBy${bindKey.d_fkColumnName}(${x_tableName}.get${bindKey.d_pkColumnName}()));
		<#else>
		<#-- 1对多 -->
		${bindKey.d_fkTableName}Cache.loadCachesCascade(${bindKey.d_fkTableName}Cache.getBy${bindKey.d_fkColumnName}(${x_tableName}.get${bindKey.d_pkColumnName}()));
		</#if>
		</#if>
		</#list>
	}
	
	/**
	 * 级联加载缓存
	 */
	public static void loadCachesCascade(List<${d_tableName}> ${x_tableName}s){
		for(${d_tableName} ${x_tableName} : ${x_tableName}s){
			loadCacheCascade(${x_tableName});
		}
	}
	
	
	/**
	 * 级联清除缓存
	 */
	public static void clearCacheCascade(${d_tableName} ${x_tableName},int saveType){
	
		clearCache(${x_tableName},3,saveType);
		
		<#list bindKeys as bindKey>
		<#if !bindKey.pk>
		<#if bindKey.unique>
		<#-- 1对1 -->
		${bindKey.d_fkTableName}Cache.clearCacheCascade(${bindKey.d_fkTableName}Cache.getBy${bindKey.d_fkColumnName}(${x_tableName}.get${bindKey.d_pkColumnName}()),saveType);
		<#else>
		<#-- 1对多 -->
		${bindKey.d_fkTableName}Cache.clearCachesCascade(${bindKey.d_fkTableName}Cache.getBy${bindKey.d_fkColumnName}(${x_tableName}.get${bindKey.d_pkColumnName}()),saveType);
		</#if>
		</#if>
		</#list>
	}
	
	/**
	 * 级联清除缓存
	 */
	public static void clearCachesCascade(List<${d_tableName}> ${x_tableName}s,int saveType){
		for(${d_tableName} ${x_tableName} : ${x_tableName}s){
			clearCacheCascade(${x_tableName},0);
		}
		switch (saveType) {
		case 1:
			saveDatabase(${x_tableName}s);
			break;
		case 2:
			saveRedis(${x_tableName}s);
			break;
		case 3:
			saveAll(${x_tableName}s);
			break;
		default:
			break;
		}
	}
}