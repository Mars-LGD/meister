package org.xiaotian.persistence.model;

import java.beans.PropertyDescriptor;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.dbutils.BeanProcessor;
import org.xiaotian.extend.CMyString;

/**
 * 自定义DBUtils的BeanProcessor，实现数据库带_(下划线)字段的骆峰转换
 * 
 * @author xiaotian15
 * 
 */
public class CustomBeanProcessor extends BeanProcessor {
	
	/**
	 * @see org.apache.commons.dbutils.BeanProcessor#mapColumnsToProperties(java.sql.ResultSetMetaData, java.beans.PropertyDescriptor[])
	 */
	@Override
	protected int[] mapColumnsToProperties(ResultSetMetaData rsmd, PropertyDescriptor[] props) throws SQLException {
		int cols = rsmd.getColumnCount();
		int columnToProperty[] = new int[cols + 1];
		Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);
		for (int col = 1; col <= cols; col++) {
			String columnName = rsmd.getColumnLabel(col);
			if (null == columnName || 0 == columnName.length()) {
				columnName = rsmd.getColumnName(col);
			}
			// 在这里进行数据库表columnName的特殊处理
			columnName = CMyString.underlineToCamel(columnName, '_');
			for (int i = 0; i < props.length; i++) {
				if (columnName.equalsIgnoreCase(props[i].getName())) {
					columnToProperty[col] = i;
					break;
				}
			}
		}
		return columnToProperty;
	}
}
