package ${packageName};

import ${packageName}.cfg.*;
import com.sandking.tools.SK_Plus;

/**
 * 加载所有的配置
 */
public class ${className} {
	public static boolean loadAllConfig(String path) throws Exception{
	<#list tables as table>
		// ${table.tableName}
		${table.d_tableName}Cfg.loadFile(SK_Plus.b(path, "${table.d_tableName}.sk").e());
		
	</#list>
		return true;
	}
	
	public static boolean writeAllConfig(String path) throws Exception{
	<#list tables as table>
		// ${table.tableName}
		${table.d_tableName}Cfg.writeFile(SK_Plus.b(path, "${table.d_tableName}.sk").e());
		
	</#list>
		return true;
	}
}