package ${packageName};

<#list daoImports as imports>
import ${imports};
</#list>
/**
 * ${tableName}
 */
public class ${d_tableName}Dao {
	<#---------------------- INSERT ---------------------->
	public static ${d_tableName} insert(${d_tableName} ${x_tableName}){
		Connection conn = ${config}.getConnection();
		return insert(${x_tableName},conn);
	}
	
	public static ${d_tableName} insert(${d_tableName} ${x_tableName},Connection conn){
		return insert(${x_tableName},conn,${d_tableName}.TABLENAME);
	}
	
	public static ${d_tableName} insert(${d_tableName} ${x_tableName},DataSource ds){
		try {
			Connection conn = ds.getConnection();
			return insert(${x_tableName},conn);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ${d_tableName} insert(${d_tableName} ${x_tableName},String tableName){
		Connection conn = ${config}.getConnection();
		return insert(${x_tableName},conn,tableName);
	}
	
	public static ${d_tableName} insert(${d_tableName} ${x_tableName},Connection conn,String tableName){
		<#assign primaryType="int" />
		<#assign isAutoincrement=false />
		<#list columns as column>
		<#if column.x_columnName == primaryX_columnName && column.autoincrement>
		<#assign isAutoincrement=true />
		<#assign primaryType=column.basicType />
		</#if>
		</#list>
		
		<#if isAutoincrement>
		SK_Query sq = new SK_Query();
		<#else>
		QueryRunner run = new QueryRunner();
		</#if>
		String sql = "INSERT INTO " +tableName+ " (${all_columnName}) VALUES (${all_columnNameSign})";
		try {
			<#if isAutoincrement>
			<#if primaryType=="int">
			int i = (int)sq.insert(conn,sql,${all_objAndGetD_columnName});
			<#else>
			long i = sq.insert(conn,sql,${all_objAndGetD_columnName});
			</#if>
			if(${x_tableName}.get${primaryD_columnName}()==0){
				${x_tableName}.set${primaryD_columnName}(i);
			}
			<#else>
			int i = run.update(conn,sql,${all_objAndGetD_columnName});
			</#if>
			return i > 0 ? ${x_tableName} : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try{
				DbUtils.close(conn);
			}catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}
	
	public static ${d_tableName} insert(${d_tableName} ${x_tableName},DataSource ds,String tableName){
		try {
			Connection conn = ds.getConnection();
			return insert(${x_tableName},conn,tableName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	<#---------------------- INSERT BATCH---------------------->
	public static int[] insertBatch(List<${d_tableName}> ${x_tableName}s){
		Connection conn = ${config}.getConnection();
		return insertBatch(${x_tableName}s,conn);
	}
	
	public static int[] insertBatch(List<${d_tableName}> ${x_tableName}s,Connection conn){
		return insertBatch(${x_tableName}s,conn,${d_tableName}.TABLENAME);
	}
	
	public static int[] insertBatch(List<${d_tableName}> ${x_tableName}s,DataSource ds){
		try {
			Connection conn = ds.getConnection();
			return insertBatch(${x_tableName}s,conn);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int[] insertBatch(List<${d_tableName}> ${x_tableName}s,String tableName){
		Connection conn = ${config}.getConnection();
		return insertBatch(${x_tableName}s,conn,tableName);
	}
	
	public static int[] insertBatch(List<${d_tableName}> ${x_tableName}s,Connection conn,String tableName){
		QueryRunner run = new QueryRunner();
		String sql = "INSERT INTO " +tableName+ " (${all_columnName}) VALUES (${all_columnNameSign})";
		try {
			int columnSize = ${columnSize};
			int size = ${x_tableName}s.size();
			Object[][] params = new Object[size][columnSize];
			for (int i = 0; i < size; i++) {
				<#assign i=0 />
				<#list all_objAndGetD_columnNames as all_objAndGetD_columnName>
				params[i][${i}] =${all_objAndGetD_columnName};
				<#assign i=i+1 />
				</#list>
			}
			int[] is = run.batch(conn,sql,params);
			return is.length > 1 ? is : new int[]{};
		} catch (Exception e) {
			e.printStackTrace();
			return new int[]{};
		} finally {
			try{
				DbUtils.close(conn);
			}catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}
	
	public static int[] insertBatch(List<${d_tableName}> ${x_tableName}s,DataSource ds,String tableName){
		try {
			Connection conn = ds.getConnection();
			return insertBatch(${x_tableName}s,conn,tableName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	<#---------------------- UPDATE ---------------------->
	public static ${d_tableName} update(${d_tableName} ${x_tableName}){
		Connection conn = ${config}.getConnection();
		return update(${x_tableName},conn);
	}
	
	public static ${d_tableName} update(${d_tableName} ${x_tableName},Connection conn){
		return update(${x_tableName},conn,${d_tableName}.TABLENAME);
	}
	
	public static ${d_tableName} update(${d_tableName} ${x_tableName},DataSource ds){
		try {
			Connection conn = ds.getConnection();
			return update(${x_tableName},conn);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ${d_tableName} update(${d_tableName} ${x_tableName},String tableName){
		Connection conn = ${config}.getConnection();
		return update(${x_tableName},conn,tableName);
	}
	
	public static ${d_tableName} update(${d_tableName} ${x_tableName},Connection conn,String tableName){
		QueryRunner run = new QueryRunner();
		StringBuffer sb = new StringBuffer();
		Map<String, Object> updateColumns = ${x_tableName}.getUpdateColumns();
		int columnSize = updateColumns.size();
		if (updateColumns.isEmpty()) {
			return ${x_tableName};
		}
		sb.append("UPDATE ");
		sb.append(tableName);
		sb.append(" SET ");
		Object[] values = new Object[(columnSize + 1)];
		int i = 0;
		for (Map.Entry<String, Object> updateColumn : updateColumns.entrySet()) {
			String key = updateColumn.getKey();
			values[i] = updateColumn.getValue();
			i++;
			sb.append(key);
			sb.append("=");
			sb.append("?");
			if (i < columnSize) {
				sb.append(",");
			}
		}
		sb.append(" WHERE ");
		sb.append("${primary_columnName}");
		sb.append(" = ?");
		values[columnSize] = ${x_tableName}.get${primaryD_columnName}();
		String sql = sb.toString();
		try {
			i = run.update(conn, sql, values);			
			return i == 1 ? ${x_tableName} : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			try{
				${x_tableName}.clearUpdateColumn();
				DbUtils.close(conn);
			}catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}
	
	public static ${d_tableName} update(${d_tableName} ${x_tableName},DataSource ds,String tableName){
		try {
			Connection conn = ds.getConnection();
			return update(${x_tableName},conn,tableName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	<#---------------------- UPDATE BATCH 只能更新全部字段时候使用---------------------->
	public static int[] updateBatch(List<${d_tableName}> ${x_tableName}s){
		Connection conn = ${config}.getConnection();
		return updateBatch(${x_tableName}s,conn);
	}
	
	public static int[] updateBatch(List<${d_tableName}> ${x_tableName}s,Connection conn){
		return updateBatch(${x_tableName}s,conn,${d_tableName}.TABLENAME);
	}
	
	public static int[] updateBatch(List<${d_tableName}> ${x_tableName}s,DataSource ds){
		try {
			Connection conn = ds.getConnection();
			return updateBatch(${x_tableName}s,conn);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int[] updateBatch(List<${d_tableName}> ${x_tableName}s,String tableName){
		Connection conn = ${config}.getConnection();
		return updateBatch(${x_tableName}s,conn,tableName);
	}
	
	public static int[] updateBatch(List<${d_tableName}> ${x_tableName}s,Connection conn,String tableName){
		QueryRunner run = new QueryRunner();
		String sql = "UPDATE " +tableName+ " SET ${all_columnName_Sign} WHERE ${primary_columnName} = ?";
		try {
			int columnSize = ${columnSize};
			int size = ${x_tableName}s.size();
			Object[][] params = new Object[size][columnSize + 1];
			for (int i = 0; i < size; i++) {
				<#assign i=0 />
				<#list all_objAndGetD_columnNames as all_objAndGetD_columnName>
				params[i][${i}] =${all_objAndGetD_columnName};
				<#assign i=i+1 />
				</#list>
				params[i][columnSize] =${x_tableName}s.get(i).get${primaryD_columnName}();
			}
			int[] is = run.batch(conn,sql,params);
			return is.length > 1 ? is : new int[]{};
		} catch (Exception e) {
			e.printStackTrace();
			return new int[]{};
		} finally {
			try{
				DbUtils.close(conn);
			}catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}
	
	public static int[] updateBatch(List<${d_tableName}> ${x_tableName}s,DataSource ds,String tableName){
		try {
			Connection conn = ds.getConnection();
			return updateBatch(${x_tableName}s,conn,tableName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	<#---------------------- DELETE ---------------------->
	public static boolean delete(${d_tableName} ${x_tableName}){
		Connection conn = ${config}.getConnection();
		return delete(${x_tableName},conn);
	}
	
	public static boolean delete(${d_tableName} ${x_tableName},Connection conn){
		return delete(${x_tableName},conn,${d_tableName}.TABLENAME);
	}
	
	public static boolean delete(${d_tableName} ${x_tableName},DataSource ds){
		try {
			Connection conn = ds.getConnection();
			return delete(${x_tableName},conn);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean delete(${d_tableName} ${x_tableName},String tableName){
		Connection conn = ${config}.getConnection();
		return delete(${x_tableName},conn,tableName);
	}
	
	public static boolean delete(${d_tableName} ${x_tableName},Connection conn,String tableName){
		QueryRunner run = new QueryRunner();
		String sql = "DELETE FROM " + tableName + " WHERE ${primary_columnName} = ?";
		try {
			int i = run.update(conn,sql, ${x_tableName}.get${primaryD_columnName}());
			return i > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			try{
				DbUtils.close(conn);
			}catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}
		}
	}
	
	public static boolean delete(${d_tableName} ${x_tableName},DataSource ds,String tableName){
		try {
			Connection conn = ds.getConnection();
			return delete(${x_tableName},conn,tableName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	<#---------------------- DELETE BATCH---------------------->
	public static boolean deleteBatch(List<${d_tableName}> ${x_tableName}s){
		Connection conn = ${config}.getConnection();
		return deleteBatch(${x_tableName}s,conn);
	}
	
	public static boolean deleteBatch(List<${d_tableName}> ${x_tableName}s,Connection conn){
		return deleteBatch(${x_tableName}s,conn,${d_tableName}.TABLENAME);
	}
	
	public static boolean deleteBatch(List<${d_tableName}> ${x_tableName}s,DataSource ds){
		try {
			Connection conn = ds.getConnection();
			return deleteBatch(${x_tableName}s,conn);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean deleteBatch(List<${d_tableName}> ${x_tableName}s,String tableName){
		Connection conn = ${config}.getConnection();
		return deleteBatch(${x_tableName}s,conn,tableName);
	}
	
	public static boolean deleteBatch(List<${d_tableName}> ${x_tableName}s,Connection conn,String tableName){
		QueryRunner run = new QueryRunner();
		String sql = "DELETE FROM " + tableName + " WHERE ${primary_columnName} = ?";
		try {
			int size = ${x_tableName}s.size();
			Object[][] params = new Object[size][1];
			for (int i = 0; i < size; i++) {
				params[i][0] = ${x_tableName}s.get(i).get${primaryD_columnName}();
			}
			int[] is = run.batch(conn,sql,params);
			return is.length > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			try{
				DbUtils.close(conn);
			}catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}
		}
	}
	
	public static boolean deleteBatch(List<${d_tableName}> ${x_tableName}s,DataSource ds,String tableName){
		try {
			Connection conn = ds.getConnection();
			return deleteBatch(${x_tableName}s,conn,tableName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	<#list indexKeys as index>
	/**
	 * 根据(<#list index.columnNames as columnName> ${columnName} </#list>) 查询
	 */
	<#if index.unique>
	<#-- 唯一索引查询 -->
	public static ${d_tableName} getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName}){
		Connection conn = ${config}.getConnection();
		return getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName}, conn);
	}
	
	public static ${d_tableName} getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},String tableName){
		Connection conn = ${config}.getConnection();
		return getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName}, conn,tableName);
	}
	
	<#else>
	<#-- 聚集索引查询 -->
	public static List<${d_tableName}> getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName}){
		Connection conn = ${config}.getConnection();
		return getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName}, conn);
	}
	
	public static List<${d_tableName}> getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},String tableName){
		Connection conn = ${config}.getConnection();
		return getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName}, conn,tableName);
	}
	
	<#-- 聚集索引分页查询 -->
	public static List<${d_tableName}> getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},int page,int pageSize){
		Connection conn = ${config}.getConnection();
		return getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName}, conn,page,pageSize);
	}
	
	public static List<${d_tableName}> getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},String tableName,int page,int pageSize){
		Connection conn = ${config}.getConnection();
		return getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName}, conn,tableName,page,pageSize);
	}
	</#if>
	</#list>
	
	//Connection
	<#list indexKeys as index>
	/**
	 * 根据(<#list index.columnNames as columnName> ${columnName} </#list>) 查询
	 */
	<#if index.unique>
	public static ${d_tableName} getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},Connection conn){
		return getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName},conn,${d_tableName}.TABLENAME);
	}
	
	public static ${d_tableName} getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},Connection conn,String tableName){
		QueryRunner run = new QueryRunner();
		String sql = "SELECT ${all_columnName} FROM " + tableName + " WHERE " + "${index.all_x_columnName_help} ORDER BY ${primary_columnName} ASC";
		${d_tableName} ${x_tableName} = null; 
		try {
			Map<String, Object> map = run.query(conn,sql, new MapHandler(), ${index.all_x_columnName});
			${x_tableName} = ${d_tableName}.createForColumnNameMap(map);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try{
				DbUtils.close(conn);
			}catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
		return ${x_tableName};
	}
	
	<#else>
	public static List<${d_tableName}> getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},Connection conn){
		return getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName},conn,${d_tableName}.TABLENAME);
	}
	
	public static List<${d_tableName}> getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},Connection conn,String tableName){
		QueryRunner run = new QueryRunner();
		String sql = "SELECT ${all_columnName} FROM " + tableName + " WHERE " + "${index.all_x_columnName_help} ORDER BY ${primary_columnName} ASC";
		List<${d_tableName}> ${x_tableName}s = null; 
		try {
			List<Map<String,Object>> list = run.query(conn, sql, new MapListHandler(), ${index.all_x_columnName});
			${x_tableName}s = ${d_tableName}.createForColumnNameList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try{
				DbUtils.close(conn);
			}catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
		return ${x_tableName}s;
	}
	
	//-----------------------------------page-----------------------------------
	public static List<${d_tableName}> getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},Connection conn,int page,int pageSize){
		return getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName},conn,${d_tableName}.TABLENAME,page,pageSize);
	}
	
	public static List<${d_tableName}> getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},Connection conn,String tableName,int page,int pageSize){
		QueryRunner run = new QueryRunner();
		<#if db.databaseType=="MySQL">
		page = ((page-1) * pageSize);
		String sql = "SELECT ${all_columnName} FROM " + tableName + " WHERE " + "${index.all_x_columnName_help} ORDER BY ${primary_columnName} ASC LIMIT " + page + " , " +pageSize;
		<#else>
		String sql = "SELECT ${all_columnName} FROM " + tableName + " WHERE " + "${index.all_x_columnName_help}";
		</#if>
		List<${d_tableName}> ${x_tableName}s = null; 
		try {
			List<Map<String,Object>> list = run.query(conn, sql, new MapListHandler(), ${index.all_x_columnName});
			${x_tableName}s = ${d_tableName}.createForColumnNameList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try{
				DbUtils.close(conn);
			}catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
		return ${x_tableName}s;
	}
	</#if>
	</#list>
	
	//DataSource
	<#list indexKeys as index>
	/**
	 * 根据(<#list index.columnNames as columnName> ${columnName} </#list>) 查询
	 */
	<#if index.unique>
	public static ${d_tableName} getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},DataSource ds){
		try {
			Connection conn = ds.getConnection();
			return getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName}, conn);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ${d_tableName} getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},DataSource ds,String tableName){
		try {
			Connection conn = ds.getConnection();
			return getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName}, conn);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	<#else>
	public static List<${d_tableName}> getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},DataSource ds){
		try {
			Connection conn = ds.getConnection();
			return getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName}, conn);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<${d_tableName}> getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},DataSource ds,String tableName){
		try {
			Connection conn = ds.getConnection();
			return getBy<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName}, conn);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//-----------------------------------page-----------------------------------
	public static List<${d_tableName}> getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},DataSource ds,int page,int pageSize){
		try {
			Connection conn = ds.getConnection();
			return getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName}, conn,page,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<${d_tableName}> getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_basicType_x_columnName},DataSource ds,String tableName,int page,int pageSize){
		try {
			Connection conn = ds.getConnection();
			return getByPage<#list index.d_columnNames as d_columnName>${d_columnName}</#list>(${index.all_x_columnName}, conn,page,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	</#if>
	</#list>
	
	
	<#---------------------- GetAll ---------------------->
	public static List<${d_tableName}> getAll(){
		Connection conn = ${config}.getConnection();
		return getAll(conn);
	}
	
	public static List<${d_tableName}> getAll(Connection conn){
		return getAll(conn,${d_tableName}.TABLENAME);
	}
	
	public static List<${d_tableName}> getAll(DataSource ds){
		try {
			Connection conn = ds.getConnection();
			return getAll(conn);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<${d_tableName}> getAll(String tableName){
		Connection conn = ${config}.getConnection();
		return getAll(conn,tableName);
	}
	
	public static List<${d_tableName}> getAll(Connection conn,String tableName){
		QueryRunner run = new QueryRunner();
		String sql = "SELECT ${all_columnName} FROM " + tableName + " ORDER BY ${primary_columnName} ASC";
		List<${d_tableName}> ${x_tableName}s = null; 
		try {
			List<Map<String,Object>> list = run.query(conn, sql, new MapListHandler());
			${x_tableName}s = ${d_tableName}.createForColumnNameList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try{
				DbUtils.close(conn);
			}catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
		return ${x_tableName}s;
	}
	
	public static List<${d_tableName}> getAll(DataSource ds,String tableName){
		try {
			Connection conn = ds.getConnection();
			return getAll(conn,tableName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	<#-- ALL PAGE -->
	<#---------------------- GetAll ---------------------->
	public static List<${d_tableName}> getAllPage(int page,int pageSize){
		Connection conn = ${config}.getConnection();
		return getAllPage(conn,page,pageSize);
	}
	
	public static List<${d_tableName}> getAllPage(Connection conn,int page,int pageSize){
		return getAllPage(conn,${d_tableName}.TABLENAME,page,pageSize);
	}
	
	public static List<${d_tableName}> getAllPage(DataSource ds,int page,int pageSize){
		try {
			Connection conn = ds.getConnection();
			return getAllPage(conn,page,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<${d_tableName}> getAllPage(String tableName,int page,int pageSize){
		Connection conn = ${config}.getConnection();
		return getAllPage(conn,tableName,page,pageSize);
	}
	
	public static List<${d_tableName}> getAllPage(Connection conn,String tableName,int page,int pageSize){
		QueryRunner run = new QueryRunner();
		<#if db.databaseType=="MySQL">
		page = ((page-1) * pageSize);
		String sql = "SELECT ${all_columnName} FROM " + tableName + " ORDER BY ${primary_columnName} ASC LIMIT " + page + " , " +pageSize;
		<#else>
		String sql = "SELECT ${all_columnName} FROM " + tableName;
		</#if>
		List<${d_tableName}> ${x_tableName}s = null; 
		try {
			List<Map<String,Object>> list = run.query(conn, sql, new MapListHandler());
			${x_tableName}s = ${d_tableName}.createForColumnNameList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try{
				DbUtils.close(conn);
			}catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
		return ${x_tableName}s;
	}
	
	public static List<${d_tableName}> getAllPage(DataSource ds,String tableName,int page,int pageSize){
		try {
			Connection conn = ds.getConnection();
			return getAllPage(conn,tableName,page,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public static boolean truncate(){
		Connection conn = SK_Config.getConnection();
		return truncate(conn);
	}
	
	public static boolean truncate(Connection conn){
		return truncate(conn,${d_tableName}.TABLENAME);
	}
	
	public static boolean truncate(DataSource ds){
		try {
			Connection conn = ds.getConnection();
			return truncate(conn);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean truncate(String tableName){
		Connection conn = SK_Config.getConnection();
		return truncate(conn,tableName);
	}
	
	public static boolean truncate(Connection conn,String tableName){
		QueryRunner run = new QueryRunner();
		String sql = "TRUNCATE " + tableName;
		try {
			run.update(conn, sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				DbUtils.close(conn);
			} catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}
		}
	}
	
	public static boolean truncate(DataSource ds,String tableName){
		try {
			Connection conn = ds.getConnection();
			return truncate(conn,tableName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	//Drop Table
	public static boolean drop(){
		Connection conn = SK_Config.getConnection();
		return drop(conn);
	}
	
	public static boolean drop(Connection conn){
		return drop(conn,${d_tableName}.TABLENAME);
	}
	
	public static boolean drop(DataSource ds){
		try {
			Connection conn = ds.getConnection();
			return drop(conn);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean drop(String tableName){
		Connection conn = SK_Config.getConnection();
		return drop(conn,tableName);
	}
	
	public static boolean drop(Connection conn,String tableName){
		QueryRunner run = new QueryRunner();
		String sql = "DROP TABLE " + tableName;
		try {
			run.update(conn, sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				DbUtils.close(conn);
			} catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}
		}
	}
	
	public static boolean drop(DataSource ds,String tableName){
		try {
			Connection conn = ds.getConnection();
			return drop(conn,tableName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	//create
	public static boolean createTable(){
		Connection conn = SK_Config.getConnection();
		return createTable(conn);
	}
	
	public static boolean createTable(Connection conn){
		return createTable(conn,${d_tableName}.TABLENAME);
	}
	
	public static boolean createTable(DataSource ds){
		try {
			Connection conn = ds.getConnection();
			return createTable(conn);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean createTable(String tableName){
		Connection conn = SK_Config.getConnection();
		return createTable(conn,tableName);
	}
	
	public static boolean createTable(Connection conn,String tableName){
		QueryRunner run = new QueryRunner();
		SK_Plus plus = SK_Plus.b("CREATE TABLE IF NOT EXISTS `", tableName,"` (");
		<#list columns as column>
		<#if column.autoincrement>
		plus.a("  `${column.columnName}` ${column.columnType}(${column.columnSize}) NOT NULL AUTO_INCREMENT,");	
		<#else>
		plus.a("  `${column.columnName}` ${column.columnType}(${column.columnSize}) NOT NULL,");	
		</#if>
		</#list>
		plus.a("  PRIMARY KEY (`${primary_columnName}`)");
		plus.a(") ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;");
		String sql = plus.e();
		try {
			run.update(conn, sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				DbUtils.close(conn);
			} catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}
		}
	}
	
	public static boolean createTable(DataSource ds,String tableName){
		try {
			Connection conn = ds.getConnection();
			return createTable(conn,tableName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}