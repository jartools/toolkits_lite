package ${packageName};

import ${packageName}.imp.*;

/**
 * 数据存储
 */
public class ${className} {

	static private int cacheCur = CfgDbCache.CP;

	static public int getChacheType() {
		return cacheCur;
	}
	
	static public void setChacheType(int cType) {
		cacheCur = cType;
	<#list tables as table>
		// ${table.tableName}
		${table.d_tableName}Imp.setChacheType(cacheCur);
	</#list>
	}
	
	static public boolean saveAll() throws Exception{
	<#list tables as table>
		// ${table.tableName}
		${table.d_tableName}Imp.saveAll();
	</#list>
		return true;
	}
		
	static public boolean saveRedisAll() throws Exception{
	<#list tables as table>
		// ${table.tableName}
		${table.d_tableName}Imp.saveRedisClear();
	</#list>
		return true;
	}
	
	static public boolean saveDatabaseAll() throws Exception{
	<#list tables as table>
		// ${table.tableName}
		${table.d_tableName}Imp.saveDatabaseClear();
	</#list>
		return true;
	}
}