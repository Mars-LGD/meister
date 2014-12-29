/*
 *	I18NMessage.java
 *	History				Who				What
 *	2009-4-25 		 	wenyh			Created.
 *  2009-05-13          wenyh           换行的处理，添加load时间输出,以确定是否需要延时加载
 */
package com.xiaotian.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.xiaotian.extend.CMyString;

/**
 * Title: TRS 内容协作平台（TRS WCM）<BR>
 * Description: <BR>
 * 国际化消息处理 <BR>
 * Copyright: Copyright (c) 2004-2009 北京拓尔思信息技术股份有限公司<BR>
 * Company: 北京拓尔思信息技术股份有限公司(www.trs.com.cn)<BR>
 * 
 * @author wenyh
 * @version 1.0
 * 
 */

public final class I18NMessage {
    private final static Logger LOG = Logger.getLogger(I18NMessage.class);

    private final static Map MESSAGES = new HashMap(500, 0.2f);

    // private static String[] m_dirs;

    // private final static Map LOADED = new

    public static String makeObjectNotFound(String[] args) {
        return CMyString.format(I18NMessage.get(I18NMessage.class,
                "I18NMessage.label1", "没有找到指定的{1}[ID={0}]"),args);
    }

    public static String get(Class objCls, String key, String defaultValue) {
        String pkg = objCls.getPackage().getName();
        String v = (String) MESSAGES.get(pkg + "." + key);
        if (v != null) {
            return v;
        }

        return defaultValue;
    }

    static void load(String rootDir, String lang) {
        LOG.info("load i18n message begin...");
        long start = System.currentTimeMillis();
        File fDir = new File(rootDir);
        if (!fDir.exists() || !fDir.isDirectory()) {
            throw new IllegalArgumentException("No such directory:" + rootDir);
        }
        String fn = "i18nmessage";
        if (lang != null) {
            // 暂时不支持同时支持多语种
            // fn += "_" + lang;
        }
        fn += ".properties";
        // m_dirs = fDir.list();
        final String fileName = fn;
        final Set files = new HashSet(500, 0.2f);
        fDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    pathname.listFiles(this);
                } else if (fileName.equals(pathname.getName())) {
                    files.add(pathname.getAbsolutePath());
                }
                return false;
            }

        });

        Iterator fItr = files.iterator();
        while (fItr.hasNext()) {
            fn = (String) fItr.next();
            read(fn);
        }
        long end = System.currentTimeMillis();
        LOG.info("i18n message loaded.time used(ms):" + (end - start));
    }

    /**
     * @param fn
     */
    private static void read(String fn) {
        InputStream ins = null;
        InputStreamReader insr = null;
        BufferedReader reader = null;
        try {
            ins = new FileInputStream(fn);
            insr = new InputStreamReader(ins, "GBK");
            reader = new BufferedReader(insr);
            String line = reader.readLine();
            String pkg = line.substring(1);
            line = reader.readLine();
            while (line != null) {
                int nIndex = line.indexOf('=');
                // 需要使用replaceAll对消息中的\n转义回来,readLine会将之当作普通字符对待
                MESSAGES.put(pkg + "." + line.substring(0, nIndex), line
                        .substring(nIndex + 1).replaceAll("\\\\n", "\n"));
                line = reader.readLine();
            }
        } catch (Exception ex) {
            // Ignore.
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (Exception ex) {
                }
            }
            if (insr != null) {
                try {
                    insr.close();
                } catch (Exception ex) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ex) {
                }
            }
        }
    }
}