package com.bowlong.basic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.bowlong.lang.InputStreamEx;

/**
 * Runtime 封装，执行shell<br/>
 * sh -c 执行文本string中的命令
 * 
 * @author Canyon
 * @version 2019-05-08 20:20
 */
public class EORuntime extends EORegex {

	static final private String fmtSh = "sudo sh %s";
	static final public String strLinuxCmdNet = "sudo netstat -tunlp";
	static final private String fmtNet = "sudo netstat -anp | grep %s";

	static final public Runtime currRt() {
		return Runtime.getRuntime();
	}

	static final public Process exec(String cmd) {
		try {
			Runtime rt = currRt();
			return rt.exec(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static final public String execStr(String cmd) {
		Process _p = exec(cmd);
		if (_p == null)
			return null;
		String ret = "";
		try (InputStream in = _p.getInputStream()) {
			ret = InputStreamEx.inps2Str(in);
			_p.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	// cmd = "tar -cf" + tarName + " " + fileName;
	// envp = 设置全局环境变量，其格式为name=value;envp={"val=2", "call=Bash Shell"} 等价于
	// export
	// val=2
	static final public Process exec(String cmd, String[] envp, File dir) {
		try {
			Runtime rt = currRt();
			return rt.exec(cmd, envp, dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// cmd = {"tar","-cf",tarName,fileName};
	static final public Process exec(String[] cmd, String[] envp, File dir) {
		try {
			Runtime rt = currRt();
			return rt.exec(cmd, envp, dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 执行 command
	 * 
	 * @param cmd
	 *            sh x.sh 或者 cd 文件夹
	 * @param args
	 *            参数
	 * @param workspace
	 *            脚本所在的目录
	 * @return
	 */
	static final public Process callCMD(String cmd, String args, String workspace) {
		File dir = null;
		workspace = toDir(workspace);
		if (!isEmpty(workspace)) {
			dir = new File(workspace);
		}
		if (!isEmpty(args)) {
			cmd = cmd.concat(" ").concat(args);
		}
		return exec(cmd, null, dir);
	}

	/**
	 * 执行shell
	 * 
	 * @param script
	 *            脚本名
	 * @param args
	 *            参数
	 * @param workspace
	 *            脚本所在的目录
	 */
	static final public Process callShell(String script, String args, String workspace) {
		// cmd = "sh " + script + " " + args;
		String cmd = String.format(fmtSh, script);
		return callCMD(cmd, args, workspace);
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
			return String.format(am_kb, (free / KB), (total / KB), (used / KB), (max / KB));
		case mb_et:
			return String.format(am_mb, (free / MB), (total / MB), (used / MB), (max / MB));
		case gb_et:
			return String.format(am_gb, (free / GB), (total / GB), (used / GB), (max / GB));
		case tb_et:
			return String.format(am_tb, (free / TB), (total / TB), (used / TB), (max / TB));
		default:
			return String.format(am, free, total, used, max);
		}
	}

	static final public void addShutdownHook(Thread thread) {
		Runtime rt = currRt();
		rt.addShutdownHook(thread);
	}

	static final public String callNetstat() {
		return execStr(strLinuxCmdNet);
	}

	static final public String callNetstat(int port) {
		String _v = String.format(fmtNet, port);
		return execStr(_v);
	}
}
