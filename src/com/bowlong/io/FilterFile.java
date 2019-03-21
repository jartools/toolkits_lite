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
	boolean isNoEmtExd = false;
	boolean isNoEmtInd = false;

	public FilterFile() {
		super();
	}

	public FilterFile(String include, String exclude) {
		super();
		_init(include, exclude);
	}

	public FilterFile(String include) {
		this(include, null);
	}
	
	void _init(String include, String exclude) {
		this.include = include;
		this.exclude = exclude;
		this.isNoEmtExd = !isEmpty(exclude);
		this.isNoEmtInd = !isEmpty(include);
	}

	@Override
	public boolean accept(File file) {
		String fn = file.getName();
		if (this.isNoEmtExd) {
			if (fn.contains(exclude))
				return false;
		}

		if (this.isNoEmtInd) {
			if (fn.contains(include))
				return true;
			
			return false;
		}
		return true;
	}

}
