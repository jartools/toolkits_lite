package com.bowlong.basic;

/**
 * Runtime 封装，执行shell<br/>
 * 
 * @author Canyon
 * @version 2019-05-08 20:20
 */
public class EORuntime extends EORegex {

	static final private String fmtSh0 = "sh %s%s";
	static final private String fmtSh1 = "sh %s%s %s";

	static final public Runtime currRt() {
		return Runtime.getRuntime();
	}

	static final public Process exec(String shell) {
		try {
			return currRt().exec(shell);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static final public Process call(String shell) {
		return exec(shell); 
	}

	/**
	 * 执行shell
	 * @param script 脚本名
	 * @param args 参数
	 * @param workspace 脚本所在的目录
	 */
	static final public Process callShell(String script, String args, String workspace) {
		if (isEmpty(workspace)) {
			workspace = "";
		} else {
			workspace = toDir(workspace);
		}
		String cmd = "";
		// cmd = "sh " + script + " " + args;
		if (isEmpty(args)) {
			cmd = String.format(fmtSh0, workspace, script);
		} else {
			cmd = String.format(fmtSh1, workspace, script, args);
		}
		return exec(cmd);
	}
	
	// 获取当前 APP 内存分配
	static final public String appMemory(int ntype) {
		Runtime rt = currRt();
		long free = rt.freeMemory(); // 拿到的内存中，还没用上的
		long total = rt.totalMemory(); // 已经从系统拿到的总内存，它总是慢慢按需要从系统拿取。区间[-Xms,-Xmx]
		long used = total - free;
		long max = rt.maxMemory(); // 最大能够申请的内存，在 Java Heap 部分
		switch (ntype) {
		case kb_et:
			return String.format(am_kb, (free / KB),(total / KB),(used / KB),(max / KB));
		case mb_et:
			return String.format(am_mb, (free / MB),(total / MB),(used / MB),(max / MB));
		case gb_et:
			return String.format(am_gb, (free / GB),(total / GB),(used / GB),(max / GB));
		case tb_et:
			return String.format(am_tb, (free / TB),(total / TB),(used / TB),(max / TB));
		default:
			return String.format(am, free,total,used,max);
		}
	}
}
