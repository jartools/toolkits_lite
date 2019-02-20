package com.bowlong.third;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.logging.Log;

import com.bowlong.basic.ExToolkit;

//Log4j日志清理器
public class ExpiredLog4j extends ExToolkit implements Runnable {
	static Log log = getLog(ExpiredLog4j.class);

	static ScheduledExecutorService SES = newScheduledPool("ExpiredLog4j", 2);

	private static ExpiredLog4j expiredlog4j = null;

	public String path = "logs";
	public int day = 5;

	public static ExpiredLog4j beginIt() {
		if (expiredlog4j == null) {
			expiredlog4j = new ExpiredLog4j();
			scheduledEveryDay(SES, expiredlog4j, 0, 1, 1);
		}
		return expiredlog4j;
	}

	private void removeExpiredLog4j() throws Exception {
		File f = new File(path + "/");
		Set<String> backupFiles = new CopyOnWriteArraySet<String>();
		backupFiles.add(path + "/log");
		{
			File[] fs = f.listFiles(new DayFilenameFilter("log", day));
			if (fs != null) {
				for (File f1 : fs) {
					backupFiles.add(f1.getPath());
				}
			}
		}
		File[] fs = f.listFiles();
		if (fs == null)
			return;
		for (File f1 : fs) {
			String s1 = f1.getPath();
			boolean delIt = true;
			for (String s2 : backupFiles) {
				if (s2.equals(s1)) {
					delIt = false;
					break;
				}
			}
			if (!delIt)
				continue;
			f1.delete();
		}
	}

	@Override
	public void run() {
		rmLog4j();
	}

	public void rmLog4j() {
		try {
			removeExpiredLog4j();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class DayFilenameFilter implements FilenameFilter {
		private static final long TIME_DAY = 24 * 60 * 60 * 1000;
		private static final String fmt = "yyyy-MM-dd";

		private final Calendar cal = Calendar.getInstance();
		private final List<String> backupDays = new ArrayList<String>();
		private final SimpleDateFormat sdf;
		private final String preix;
		private int backupDay = 7; // 多少天以内的数据

		private int lastDay = 0;

		public DayFilenameFilter(String preix, int day, String fmt) {
			this.preix = preix;
			this.backupDay = day;
			this.sdf = new SimpleDateFormat(fmt);
		}

		public DayFilenameFilter(String preix, int day) {
			this.preix = preix;
			this.backupDay = day;
			sdf = new SimpleDateFormat(fmt);
		}

		public DayFilenameFilter(String preix) {
			this.preix = preix;
			this.sdf = new SimpleDateFormat(fmt);
		}

		@Override
		public boolean accept(File dir, String name) {
			int d = cal.get(Calendar.DAY_OF_MONTH);
			if (d != lastDay) {
				backupDays.clear();
				long now = System.currentTimeMillis();
				for (int i = 0; i < backupDay; i++) {
					String dstr = sdf.format(new Date(now - i * TIME_DAY));
					backupDays.add(dstr);
				}
			}

			boolean bAccept = name.indexOf(preix) > -1;
			if (!bAccept)
				return false;
			for (String dstr : backupDays) {
				if (name.indexOf(dstr) > -1)
					return true;
			}
			return false;
		}

	}

	public void rmLog4j(String path) {
		this.path = path;
		rmLog4j();
	}
}