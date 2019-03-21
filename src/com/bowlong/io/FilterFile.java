package com.bowlong.io;

import java.io.File;
import java.io.FileFilter;

import com.bowlong.basic.ExToolkit;

/**
 * 文件过滤<br/>
 * 针对File.listFiles
 * @author Canyon
 * @time 2019-03-21 09:32
 */
public class FilterFile extends ExToolkit implements FileFilter {

	String include = null; // 包括
	String exclude = null; // 排除，不包括

	public FilterFile() {
		super();
	}

	public FilterFile(String include, String exclude) {
		super();
		this.include = include;
		this.exclude = exclude;
	}

	public FilterFile(String include) {
		this(include, null);
	}

	@Override
	public boolean accept(File file) {
		String fn = file.getName();
		if (!isEmpty(exclude)) {
			if (fn.contains(exclude))
				return false;
		}

		if (!isEmpty(include)) {
			if (fn.contains(include))
				return true;
		}
		return true;
	}

}
