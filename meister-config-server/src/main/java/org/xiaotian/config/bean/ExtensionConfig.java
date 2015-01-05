/**
 * Created:         2005-5-11 8:56:15
 * Last Modified:   2005-5-11/2005-5-11
 * Description:
 *      class CacheConfig
 */
package org.xiaotian.config.bean;

import java.util.ArrayList;

import org.xiaotian.config.ConfigException;

/**
 * castor xml <extension>标签对应Config实体
 * 
 * @author xiaotian15
 * 
 */
public class ExtensionConfig implements IConfigElement {

	
    /**
     * 扩展标签的Classname
     */
    private String className;

    /**
     * 扩展标签的list集合
     */
    private ArrayList<IConfigElement> configElements;

    public ExtensionConfig() {
        this.configElements = new ArrayList<IConfigElement>();
    }

    /**
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return IConfigElement List
     */
    public ArrayList<IConfigElement> getConfigElements() {
        return configElements;
    }

    /**
     * @param configElements
     */
    public void setConfigElements(ArrayList<IConfigElement> configElements) {
        this.configElements = configElements;
    }

    public void addConfigElements(IConfigElement element) {
        this.configElements.add(element);
    }


	/**
	 * @throws ConfigException 
	 * @see org.xiaotian.config.bean.IConfigElement#valid()
	 */
	public boolean valid() throws ConfigException {
		try{
			Class.forName(className);
			return true;
		}catch(Exception e){
			throw new ConfigException(ExtensionConfig.class.getName()+"类对应标签下配置错误", e);
		}
	}
}