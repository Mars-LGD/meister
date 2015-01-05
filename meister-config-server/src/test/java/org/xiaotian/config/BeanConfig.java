/**
 * Created:         2005-5-11 10:12:20
 * Last Modified:   2005-5-11/2005-5-11
 * Description:
 *      class BeanCfg
 */
package org.xiaotian.config;

import org.xiaotian.config.bean.IConfigElement;


/**
 * 测试用<bean>标签对应实体
 * 
 * @author xiaotian15
 * 
 */
public class BeanConfig implements IConfigElement {
    private String id;

    private String className;

    private boolean isSingleton;

    private String name;

    public BeanConfig() {
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
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return
     */
    public boolean isSingleton() {
        return isSingleton;
    }

    /**
     * @param isSingleton
     */
    public void setSingleton(boolean isSingleton) {
        this.isSingleton = isSingleton;
    }

    /**
     * @param _sName
     */
    public void setName(String _sName) {
        name = _sName;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

	/**
	 * @see org.xiaotian.config.bean.IConfigElement#valid()
	 */
	public boolean valid() {
		return true;
	}

}