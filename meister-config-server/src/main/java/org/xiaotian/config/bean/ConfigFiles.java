package org.xiaotian.config.bean;

import java.util.ArrayList;

/**
 * ConfigFile文件对集合
 * 
 * @author xiaotian15
 * 
 */
public class ConfigFiles {
    private ArrayList<ConfigFile> files = null;

    public ConfigFiles() {
        this.files = new ArrayList<ConfigFile>();
    }

    public void add(ConfigFile _file) {
        this.files.add(_file);
    }

    public ConfigFile get(int i) {
        return (ConfigFile) this.files.get(i);
    }

    public int size() {
        return this.files.size();
    }

}