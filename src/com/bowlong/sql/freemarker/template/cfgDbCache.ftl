package ${packageName};

/**
 * 数据存储
 */
public class CfgDbCache{
	/*** 进程级缓存(cacheProcess) **/
	static public final int CP = 1;
	/*** jedis+进程级缓存(cacheProcessJedis) **/
	static public final int CPJ = 2;
	/*** 数据库+进程级缓存(cacheProcessDatabase) **/
	static public final int CPD = 3; 
	/*** 数据库+jedis+进程级缓存(cacheProcessDBJedis) **/
	static public final int CPJD = 4;
}