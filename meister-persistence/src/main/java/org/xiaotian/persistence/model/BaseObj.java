package org.xiaotian.persistence.model;

/**
 * CMS对象基类
 * 
 * @author xiaotian15
 * 
 */
public abstract class BaseObj implements Cloneable {

	/** 对象ID标识，用以提高访问效率 */
	protected int id = 0;

	/**
	 * 构造函数
	 */
	public BaseObj() {
		super();
	}

	/**
	 * 取该对象的类别名称
	 * 
	 * @param _bIncludePackage
	 *            是否包含package名称
	 * @return
	 */
	public String getClassName(boolean _bIncludePackage) {
		String sClassName = this.getClass().getName();
		// 如果不包含package名称，则去掉package名称
		if (!_bIncludePackage) {
			int nPos = sClassName.lastIndexOf('.');
			if (nPos >= 0)
				sClassName = sClassName.substring(nPos + 1);
		}
		return sClassName;
	}

	/**
	 * 获取对象的ID
	 * 
	 * @return
	 */
	public int getId() {
		return this.id;
	}
}