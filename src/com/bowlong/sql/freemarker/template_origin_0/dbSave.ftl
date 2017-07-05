package ${packageName};

import ${packageName}.imp.*;

/**
 * 数据存储
 */
public class ${className} {

	public static boolean saveAll() throws Exception{
	<#list tables as table>
		// ${table.tableName}
		${table.d_tableName}Imp.saveAll();
		
	</#list>
		return true;
	}
	
	public static boolean saveRedisAll() throws Exception{
	<#list tables as table>
		// ${table.tableName}
		${table.d_tableName}Imp.saveRedis();
		
	</#list>
		return true;
	}
	
	public static boolean saveDatabaseAll() throws Exception{
	<#list tables as table>
		// ${table.tableName}
		${table.d_tableName}Imp.saveDatabase();
		
	</#list>
		return true;
	}
}