package com.xiaotian.extend;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 
 * FilenameFilter实现类
 *
 * @author xiaotian15
 * 
 */
public class CMyFilenameFilter extends Object implements FilenameFilter {

	private String sExt;

	public CMyFilenameFilter(String _extendName) {
		sExt = _extendName;
	}

	public boolean accept(File _dir, String _name) {
		return _name.endsWith(sExt);
	}

}