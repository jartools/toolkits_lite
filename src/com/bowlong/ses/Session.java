package com.bowlong.ses;

/**
 * Session 会话对象
 * 
 * @author Canyon
 * @time 2014-06-15 09:30
 */
public class Session extends MgrSession {
	private static final long serialVersionUID = 1L;
	static private final int defMs = 600 * 1000;// 10分钟
	public long sesid; // 唯一表示
	protected int ms_interval = 0; // 间隔 ms
	private long overdue; // 过期时间 ms
	private long creattime;

	public long getCreattime() {
		return creattime;
	}

	protected void SetOverdue(long overdue) {
		this.overdue = overdue;
	}

	public Session() {
	}

	public Session(long id) {
		Init(id, defMs);
	}

	public Session(long id, int msDelay) {
		Init(id, msDelay);
	}

	public void Init(long id, int msDelay) {
		this.creattime = now();
		this.sesid = id;
		ReInterval(msDelay);
	}

	public void Init(long id) {
		Init(id, defMs);
	}

	public void ReInterval(int msDelay) {
		ms_interval = msDelay;
		ResetTimeOverdue();
	}

	public void ResetTimeOverdue() {
		if (ms_interval > 1000) {
			SetOverdue(now() + ms_interval);
		} else {
			SetOverdue(0);
		}
	}

	public boolean IsValid() {
		if (overdue > defMs) {
			return now() < overdue;
		}
		return true;
	}
}