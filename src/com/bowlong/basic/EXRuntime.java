package com.bowlong.basic;

/**
 * Runtime 封装，执行shell<br/>
 * 
 * @author Canyon
 * @version 2019-05-08 20:20
 */
public class EXRuntime extends EOURL {

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

	/**
	 * 执行shell
	 * @param script 脚本名
	 * @param args 参数
	 * @param workspace 脚本所在的目录
	 */
	static final public void callShell(String script, String args, String workspace) {
		try {
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
			exec(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
