package com.xiaotian.extend;

import java.io.File;

import org.junit.Test;
import org.xiaotian.extend.CMyFilenameFilter;

public class CMyFilenameFilterTest {

	@Test
	public void testUsable() {
		CMyFilenameFilter filter=new CMyFilenameFilter("png");
		File folder=new File("E:/temp/logo");
		File[] files=folder.listFiles(filter);
		for(File file:files){
			System.out.println(file.getName());
		}
	}

}
