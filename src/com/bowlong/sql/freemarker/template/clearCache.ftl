package ${packageName};

<#list cacheImports as imports>
import ${imports};
</#list>
/**
 * ${tableName}
 */
public class ${d_tableName}Cache {

	static private AtomicInt unitCell = new AtomicInt(0);
	static private boolean isLoadAll = false;
	
	static public boolean isLoadAll() {
		return isLoadAll;
	}

	static public void setLoadAll(boolean isLoadAll) {
		${d_tableName}Cache.isLoadAll = isLoadAll;
	}
	
	static private int cacheCur = CfgDbCache.CP;

	static public void setChacheType(int cType) {
		cacheCur = cType;
	}
	
	//缓存
	<#list indexKeys as index>
	<#if index.unique>
	<#if index.indexName == "PRIMARY">
	static final Map<String, ${d_tableName}> <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache = MapEx.newSortedMap();
	<#else>
	static final Map<String, String> <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache = new ConcurrentHashMap<String, String>();
	</#if>
	<#else>
	static final Map<String, Set<String>> <#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache = new ConcurrentHashMap<String, Set<String>>();
	</#if>
	</#list>
	
	static final NewListLock<${d_tableName}> saveDelete = new NewListLock<${d_tableName}>();
	
	static final NewListLock<${d_tableName}> saveInsert = new NewListLock<${d_tableName}>();
	
	static final NewListLock<${d_tableName}> saveUpdate = new NewListLock<${d_tableName}>();
	
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
		if(key != null){
			${x_tableName} = ${primaryX_columnName}Cache.get(key);	
		} 
		</#if>
		
		boolean isNull = ${x_tableName} == null;
		if(!isNull)
			return ${x_tableName};
		
		switch (cacheCur) {
		case CfgDbCache.CPJ:
			${x_tableName} = ${d_tableName}Jedis.getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
			break;
		case CfgDbCache.CPD:
			${x_tableName} = ${d_tableName}Dao.getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
			saveUqVal(${x_tableName});
			break;
		case CfgDbCache.CPJD:
			${x_tableName} = ${d_tableName}Jedis.getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
			if(${x_tableName} == null){
				${x_tableName} = ${d_tableName}Dao.getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
				if(${x_tableName} != null){
					${d_tableName}Jedis.loadCache(${x_tableName});
				}
			}
			break;
		default:
			break;
		}

		if (isNull && ${x_tableName} != null) {
			// 加入缓存
			loadCache(${x_tableName});
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
				if (${x_tableName} == null)
					continue;
				${x_tableName}s.add(${x_tableName});
			}
		}else{
			switch (cacheCur) {
			case CfgDbCache.CPJ:
				${x_tableName}s = ${d_tableName}Jedis.getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
				break;
			case CfgDbCache.CPD:
				${x_tableName}s = ${d_tableName}Dao.getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
				saveUqValList(${x_tableName}s);
				break;
			case CfgDbCache.CPJD:
				${x_tableName}s = ${d_tableName}Jedis.getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
				if(ListEx.isEmpty(${x_tableName}s)){
					${x_tableName}s = ${d_tableName}Dao.getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
					${d_tableName}Jedis.loadCaches(${x_tableName}s);
				}
				break;
			default:
				break;
			}
			
			if(${x_tableName}s != null && !${x_tableName}s.isEmpty()){
				loadCaches(${x_tableName}s);
			}
		}
		return ${x_tableName}s;
	}
	
	<#-- 聚集索引分页查询 -->
	public static List<${d_tableName}> getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},int page,int size){
		List<${d_tableName}> ${x_tableName}s = getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName});
		${x_tableName}s = ListEx.getPageT(${x_tableName}s, page, size);
		return ${x_tableName}s;
	}
	</#if>
	</#list>
	
	public static List<${d_tableName}> getCacheAll(){
		<#list indexKeys as index>
		<#if index.indexName == "PRIMARY">
		return ListEx.valueToList(<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache);
		</#if>
		</#list>
	}
	
	/**
	 * 全部加载进内存(慎用)
	 */
	public static List<${d_tableName}> getAll(){
		List<${d_tableName}> ${x_tableName}s = new ArrayList<${d_tableName}>();
		if(!isLoadAll){
			switch (cacheCur) {
			case CfgDbCache.CPJ:
				${x_tableName}s = ${d_tableName}Jedis.getAll();
				break;
			case CfgDbCache.CPD:
				${x_tableName}s = ${d_tableName}Dao.getAll();
				break;
			case CfgDbCache.CPJD:
				${x_tableName}s = ${d_tableName}Jedis.getAll();
				if(${x_tableName}s == null || ${x_tableName}s.isEmpty()){
					${x_tableName}s = ${d_tableName}Dao.getAll();
					${d_tableName}Jedis.loadCaches(${x_tableName}s);
				}
				break;
			default:
				break;
			}
			
			loadCaches(${x_tableName}s);
			isLoadAll = true;
		}else{
			<#list indexKeys as index>
			<#if index.indexName == "PRIMARY">
			${x_tableName}s = ListEx.valueToList(<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache);
			</#if>
			</#list>
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
	
	/**
	 * 保存unique唯一属性值
	 */
	static public void saveUqValList(List<${d_tableName}> ${x_tableName}s){
		if(ListEx.isEmpty(${x_tableName}s))
			return;
		int len = ${x_tableName}s.size();
		for(int i = 0; i < len;i++){
			${d_tableName} en = ${x_tableName}s.get(i);
			 saveUqVal(en);
		}
	}
	
	/**
	 * 保存unique唯一属性值
	 */
	static public void saveUqVal(${d_tableName} ${x_tableName}){
		if(${x_tableName} == null)
			return;
		${d_tableName}Jedis.saveUqVal(${x_tableName});
	}
	
	/**
	 * 加载缓存
	 */
	public static void loadCaches(List<${d_tableName}> ${x_tableName}s){
		if(${x_tableName}s == null || ${x_tableName}s.isEmpty())
			return;
		for(${d_tableName} ${x_tableName} : ${x_tableName}s){
			loadCache(${x_tableName});
		}
	}
	
	/**
	 * 加载缓存
	 */
	public static void loadCache(${d_tableName} ${x_tableName}){
		<#assign i=0 />
		<#list indexKeys as index>
		<#if index.unique>
		<#if index.indexName == "PRIMARY">
		<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.put(${index.all_d_columnName_get},${x_tableName});
		<#else>
		<#if i==0>
		String tmpVal = "";
		<#assign i=1 />
		</#if>
		
		tmpVal = PStr.b(${index.all_x_columnName_get}).e();//${index.all_d_columnName_get}
		<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.put(tmpVal,String.valueOf(${x_tableName}.get${primaryD_columnName}()));
		// ${x_tableName}.set<#list index.d_columnNames as d_columnName>${d_columnName}</#list>Index(tmpVal);
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
	
	/*** 清除缓存 **/
	public static void clearCache(${d_tableName} ${x_tableName}){
		<#list indexKeys as index>
		<#if index.unique>
		<#if index.indexName == "PRIMARY">
		<#list index.x_columnNames as x_columnName>${x_columnName}</#list>Cache.remove(${index.all_d_columnName_get});
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
	}
	
	/**
	 * 清空缓存 
	 * saveType  0:不保存 1：保存database 2:保存redis 3:保存全部(redis+database)
	 */
	public static void clearCache(${d_tableName} ${x_tableName},int saveType){
		clearCache(${x_tableName});
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
	 * saveType  0:不保存 1：保存database 2:保存redis 3:保存全部(redis+database)
	 */
	public static void clearCaches(List<${d_tableName}> ${x_tableName}s,int saveType){
		for(${d_tableName} ${x_tableName} : ${x_tableName}s){
			clearCache(${x_tableName},0);
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
    	if(${x_tableName} == null)
    		return ${x_tableName};
    		
		int id = unitCell.incrementAndGet();
		boolean isSaveUq = false;
    	if(id <= 1){
    		isFlush = true;
    		switch (cacheCur) {
			case CfgDbCache.CPJ:
				${x_tableName} = ${d_tableName}Jedis.insert(${x_tableName});
				break;
			case CfgDbCache.CPD:
				${x_tableName} = ${d_tableName}Dao.insert(${x_tableName});
				isSaveUq = true;
				break;
			case CfgDbCache.CPJD:
				${x_tableName} = ${d_tableName}Dao.insert(${x_tableName});
				${x_tableName} = ${d_tableName}Jedis.insert(${x_tableName});
				break;
			default:
				${x_tableName}.set${primaryD_columnName}(id);
				isSaveUq = true;
				break;
			}
    		unitCell.set(${x_tableName}.get${primaryD_columnName}());
    	}else{
    		isSaveUq = true;
    		int _id = ${x_tableName}.get${primaryD_columnName}();
			if (_id < id) {
				_id = id;
				${x_tableName}.set${primaryD_columnName}(id);
			} else if (_id > id) {
				unitCell.set(_id);
			}
				
			switch (cacheCur) {
			case CfgDbCache.CPJ:
			case CfgDbCache.CPJD:
				${d_tableName}Jedis.setAtomicId(_id);
				break;
			default:
				break;
			}
    		//加入定时器
    	}
    	
    	if(isSaveUq)
    		saveUqVal(${x_tableName});
    	
    	loadCache(${x_tableName});
    	if(!isFlush){
	    	if (!saveInsert.contains(${x_tableName})) {
				saveInsert.addE(${x_tableName});
			}
		}
    	return ${x_tableName};
    }
    
    private static ${d_tableName} update(${d_tableName} ${x_tableName},boolean isFlush){
    	clearCache(${x_tableName},0);
    	loadCache(${x_tableName});
    	//加入定时器
    	if(!isFlush){
    		if (!saveUpdate.contains(${x_tableName})) {
				saveUpdate.addE(${x_tableName});
			}
		}else{
			saveUpdate.remove(${x_tableName});
		}
    	return ${x_tableName};
    }
    
    private static boolean delete(${d_tableName} ${x_tableName},boolean isFlush){
    	clearCache(${x_tableName},0);
    	//加入定时器
    	if(!isFlush){
    		if (!saveDelete.contains(${x_tableName})) {
				saveUpdate.remove(${x_tableName});
				saveInsert.remove(${x_tableName});
				saveDelete.addE(${x_tableName});
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
    	switch (cacheCur) {
		case CfgDbCache.CPJ:
			${x_tableName} = ${d_tableName}Jedis.update(${x_tableName});
			break;
		case CfgDbCache.CPD:
			${x_tableName} = ${d_tableName}Dao.update(${x_tableName});
			break;
		case CfgDbCache.CPJD:
			${x_tableName} = ${d_tableName}Dao.update(${x_tableName});
			${x_tableName} = ${d_tableName}Jedis.update(${x_tableName});
			break;
		default:
			break;
		}
    	return ${x_tableName};
    }
    
    public static ${d_tableName} insertAndFlush(${d_tableName} ${x_tableName}){
    	int id = unitCell.get();
    	insert(${x_tableName},true);
    	if(id > 0){
    		switch (cacheCur) {
			case CfgDbCache.CPJ:
				${x_tableName} = ${d_tableName}Jedis.insert(${x_tableName});
				break;
			case CfgDbCache.CPD:
				${x_tableName} = ${d_tableName}Dao.insert(${x_tableName});
				break;
			case CfgDbCache.CPJD:
				${x_tableName} = ${d_tableName}Dao.insert(${x_tableName});
				${x_tableName} = ${d_tableName}Jedis.insert(${x_tableName});
				break;
			default:
				break;
			}
    	}
    	return ${x_tableName};
    }
    
    public static boolean deleteAndFlush(${d_tableName} ${x_tableName}){
    	delete(${x_tableName},true);
    	boolean isDel = false;
    	switch (cacheCur) {
		case CfgDbCache.CPJ:
			isDel = ${d_tableName}Jedis.delete(${x_tableName});
			break;
		case CfgDbCache.CPD:
			isDel = ${d_tableName}Dao.delete(${x_tableName});
			break;
		case CfgDbCache.CPJD:
			isDel = ${d_tableName}Dao.delete(${x_tableName});
			isDel = ${d_tableName}Jedis.delete(${x_tableName});
			break;
		default:
			break;
		}
    	return isDel;
    }
    
    // ******************************** 持久化操作 ********************************
     public static void saveDatabase(){
    	if(cacheCur != CfgDbCache.CPD && cacheCur != CfgDbCache.CPJD)
    		return;
    	if(!saveDelete.isEmpty())
    		${d_tableName}Dao.deleteBatch(saveDelete);
    	if(!saveInsert.isEmpty())
    		${d_tableName}Dao.insertBatch(saveInsert);
    	if(!saveUpdate.isEmpty())
    		${d_tableName}Dao.updateBatch(saveUpdate);
    }
    
    public static void saveDatabase(${d_tableName} ${x_tableName}){
    	if(cacheCur != CfgDbCache.CPD && cacheCur != CfgDbCache.CPJD)
    		return;
    	if (saveDelete.remove(${x_tableName}))
			${d_tableName}Dao.delete(${x_tableName});
		if (saveInsert.remove(${x_tableName}))
			${d_tableName}Dao.insert(${x_tableName});
		if (saveUpdate.remove(${x_tableName}))
			${d_tableName}Dao.update(${x_tableName});
    }
    
    public static void saveDatabase(List<${d_tableName}> dels,List<${d_tableName}> ines,
    	List<${d_tableName}> upes){
    	if(cacheCur != CfgDbCache.CPD && cacheCur != CfgDbCache.CPJD)
    		return;
    	boolean isEmpty = ListEx.isEmpty(dels);
    	if(!isEmpty){
    		saveDelete.removeAll(dels);
    		${d_tableName}Dao.deleteBatch(dels);
    	}
    	
    	isEmpty = ListEx.isEmpty(ines);
    	if(!isEmpty){
    		saveInsert.removeAll(ines);
    		${d_tableName}Dao.insertBatch(ines);
    	}
		
		isEmpty = ListEx.isEmpty(upes);
    	if(!isEmpty){
    		saveUpdate.removeAll(upes);
    		${d_tableName}Dao.updateBatch(upes);
    	}
    }
    
    public static void saveDatabase(List<${d_tableName}> ${x_tableName}s){
    	saveDatabase(${x_tableName}s,${x_tableName}s,${x_tableName}s);
    }
    
    public static void saveRedis(){
    	if(cacheCur != CfgDbCache.CPJ && cacheCur != CfgDbCache.CPJD)
    		return;
    	if(!saveDelete.isEmpty())
    		${d_tableName}Jedis.clearCaches(saveDelete);
    	if(!saveInsert.isEmpty())
    		${d_tableName}Jedis.loadCaches(saveInsert);
    	if(!saveUpdate.isEmpty())
    		${d_tableName}Jedis.loadCaches(saveUpdate);
    }
    
    public static void saveRedis(${d_tableName} ${x_tableName}){
    	if(cacheCur != CfgDbCache.CPJ && cacheCur != CfgDbCache.CPJD)
    		return;
    	if (saveDelete.remove(${x_tableName}))
			${d_tableName}Jedis.clearCache(${x_tableName});
		if (saveInsert.remove(${x_tableName}))
			${d_tableName}Jedis.loadCache(${x_tableName});
		if (saveUpdate.remove(${x_tableName}))
			${d_tableName}Jedis.loadCache(${x_tableName});
    }
    
    public static void saveRedis(List<${d_tableName}> dels,List<${d_tableName}> ines,
    	List<${d_tableName}> upes){
    	if(cacheCur != CfgDbCache.CPJ && cacheCur != CfgDbCache.CPJD)
    		return;
    	boolean isEmpty = ListEx.isEmpty(dels);
    	if(!isEmpty){
    		saveDelete.removeAll(dels);
    		${d_tableName}Jedis.clearCaches(dels);
    	}
    	
    	isEmpty = ListEx.isEmpty(ines);
    	if(!isEmpty){
    		saveInsert.removeAll(ines);
    		${d_tableName}Jedis.loadCaches(ines);
    	}
		
		isEmpty = ListEx.isEmpty(upes);
    	if(!isEmpty){
    		saveUpdate.removeAll(upes);
    		${d_tableName}Jedis.loadCaches(upes);
    	}
    }
    
    public static void saveRedis(List<${d_tableName}> ${x_tableName}s){
    	saveRedis(${x_tableName}s,${x_tableName}s,${x_tableName}s);
    }
    
    public static void saveAll(${d_tableName} ${x_tableName}){
   		saveDatabase(${x_tableName});
   		saveRedis(${x_tableName});
    }
    
     public static void saveAll(List<${d_tableName}> dels,List<${d_tableName}> ines,
    	List<${d_tableName}> upes){
   		saveDatabase(dels,ines,upes);
   		saveRedis(dels,ines,upes);
    }
    
    public static void saveAll(List<${d_tableName}> ${x_tableName}s){
   		saveAll(${x_tableName}s,${x_tableName}s,${x_tableName}s);
    }
    
    /*** type[1.DB,2.Jedis,other:Both] **/
	private static void save(int type) {
		List<${d_tableName}> dels = new ArrayList<${d_tableName}>();
		List<${d_tableName}> ines = new ArrayList<${d_tableName}>();
		List<${d_tableName}> upes = new ArrayList<${d_tableName}>();
		try {
			saveDelete.wl.lockInterruptibly();
			saveInsert.wl.lockInterruptibly();
			saveUpdate.wl.lockInterruptibly();
			dels.addAll(saveDelete);
			ines.addAll(saveInsert);
			upes.addAll(saveUpdate);
			clearSave();
		} catch (Exception e) {
		} finally {
			try {
				saveDelete.wl.unlock();
				saveInsert.wl.unlock();
				saveUpdate.wl.unlock();
			} catch (Exception e) {
			}
		}
		if (type != 2)
			saveDatabase(dels,ines,upes);
		if (type != 1)
			saveRedis(dels,ines,upes);
	}
	
	public static void saveAll() {
		save(0);
	}
	
	public static void saveRedisClear() {
		save(2);
	}
	
	public static void saveDatabaseClear() {
		save(1);
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
	
		clearCache(${x_tableName},saveType);
		
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
	
	// =============== 清除缓存(进程级别，redis数据层)
	public static void clearAllCache(${d_tableName} ${x_tableName}){
		if(${x_tableName} == null)
			return;
		clearCacheCascade(${x_tableName},1);
		if(cacheCur != CfgDbCache.CPJ && cacheCur != CfgDbCache.CPJD)
    		return;
		${d_tableName}Jedis.clearCache(${x_tableName});
	}
}