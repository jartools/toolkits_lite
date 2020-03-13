package ${packageName};

<#list impImports as imports>
import ${imports};
</#list>
/**
 * ${tableName}
 */
public class ${d_tableName}Imp extends ${d_tableName}Cache{
}