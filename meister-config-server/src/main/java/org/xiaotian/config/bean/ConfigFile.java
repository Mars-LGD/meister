/**
 * Created:         2005-5-24 15:42:05
 * Last Modified:   2005-5-24/2005-5-24
 * Description:
 *      class ConfigFile
 */
package org.xiaotian.config.bean;

import java.io.File;

/**
 * ConfigFile文件对（config.xml-mapping.xml）
 * 
 * @author xiaotian15
 * 
 */
public class ConfigFile {
	
	
    /**
     * config.xml
     */
    private File plugin;

    /**
     * mapping.xml
     */
    private File mapping;

    /**
     * @param _file
     * @param mapping2
     */
    public ConfigFile(File _plugin, File _mapping) {
        this.plugin = _plugin;
        this.mapping = _mapping;
    }

    /**
     * @return
     */
    public File getMapping() {
        return mapping;
    }

    /**
     * @param mapping
     */
    public void setMapping(File mapping) {
        this.mapping = mapping;
    }

    /**
     * @return
     */
    public File getPlugin() {
        return plugin;
    }

    /**
     * @param plugin
     */
    public void setPlugin(File plugin) {
        this.plugin = plugin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "plugin=" + this.plugin.getPath() + ", mapping="
                + mapping.getPath();
    }
}