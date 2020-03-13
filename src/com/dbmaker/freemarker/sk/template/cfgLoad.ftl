package ${packageName};

import ${packageName}.cfg.*;
import com.bowlong.lang.PStr;

/**
 * 加载所有的配置
 */
public class ${className} {

	static public final String cfgPath = "cfg/";
	
	public static boolean loadAllConfig(String path) throws Exception{
	<#list tables as table>
		// ${table.tableName}
		${table.d_tableName}Cfg.loadFile(PStr.b(path, "${table.d_tableName}.sk").e());
		
	</#list>
		return true;
	}
	
	public static boolean writeAllConfig(String path) throws Exception{
	<#list tables as table>
		// ${table.tableName}
		${table.d_tableName}Cfg.writeFile(PStr.b(path, "${table.d_tableName}.sk").e());
		
	</#list>
		return true;
	}
	
	public static void main(String[] args) throws Exception{
		writeAllConfig(cfgPath);
	}
}