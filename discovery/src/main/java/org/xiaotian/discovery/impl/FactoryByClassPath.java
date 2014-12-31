/*
 * History          Who            What
 * 2011-10-08       wenyh           created.
 */
package org.xiaotian.discovery.impl;

import java.io.File;
import java.util.ArrayList;

import org.xiaotian.discovery.DiscoveryFactory;
import org.xiaotian.discovery.IDiscovery;

/**
 * 一个从classpath分析获取的工厂类.从获取的系统属性<code>java.class.path</code>中获取类路径中的类.<br>
 * 然后分析其中的内容,然后获取其中的目录及trs*.jar(zip).这个只是为了使用上的便利,实际上该工厂调用的是FactoryByFiles的实现.
 * 
 * @author leafgray
 * 
 */
public class FactoryByClassPath extends DiscoveryFactory {

    public IDiscovery build() {
        String clazzPath = System.getProperty("java.class.path", "");
        String pathSep = System.getProperty("path.separator", ";");
        String[] names = clazzPath.split(pathSep);
        ArrayList libs = new ArrayList();
        ArrayList dirs = new ArrayList();
        for (int i = 0, len = names.length; i < len; i++) {
            File file = new File(names[i]);
            if (file.isDirectory()) {
                dirs.add(file);
            } else {
                String jarFile = file.getName();
                if (jarFile.startsWith("trs")) {// 只找trs*.jar(zip)
                    libs.add(file);
                }
            }
        }

        File[] libFiles = new File[libs.size()];
        libs.toArray(libFiles);
        File[] clazzDirs = new File[libs.size()];
        dirs.toArray(clazzDirs);

        return new FactoryByFiles(libFiles, clazzDirs).build();
    }

}
