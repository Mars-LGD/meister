package org.xiaotian.db.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.xiaotian.config.ConfigException;
import org.xiaotian.config.bean.IConfigElement;
import org.xiaotian.extend.CMyString;

/**
 * 数据库连接特性的配置对象
 * 
 * @author xiaotian15
 * 
 */
public class DBConnectionConfig implements IConfigElement {

	public DBConnectionConfig() {
		super();
	}

	// ===================================通用属性===================================
	/** 数据库标签 */
	private String id;

	/** 数据库连接url **/
	private String url;

	/** 数据库driver */
	private String driver;

	/** 数据库连接用户名 **/
	private String username;

	/** 数据库连接密码 **/
	private String password;

	/** 同一时间分配的最大数据库连接，默认值是100 **/
	private int maxActive = 100;

	/** 最大的空闲数据库连接，默认值是等于maxActive **/
	private int maxIdle = 100;

	/** 最小的空闲数据库连接，默认值等于initialSize **/
	private int minIdle = 10;

	/** 初始化时的数据库连接，默认值为10 **/
	private int initialSize = 10;

	/** 当无连接分配时，请求连接的最大等待时间 */
	private int maxWait = 30 * 1000;

	/** 验证查询的SQL */
	private String validationQuery;

	// ===================================特殊属性===================================
	/** 是否允许JMX */
	private boolean jmxEnabled = true;

	/** 数据库连接的最长存活时间，0表示不作时间检查 */
	private Long maxAge = 0L;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public boolean isJmxEnabled() {
		return jmxEnabled;
	}

	public void setJmxEnabled(boolean jmxEnabled) {
		this.jmxEnabled = jmxEnabled;
	}

	public Long getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Long maxAge) {
		this.maxAge = maxAge;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * xml标签对应实体有效性验证
	 * 
	 * @return 验证是否成功，成功返回true，不成功返回false
	 */
	public boolean valid() throws ConfigException {
		if (CMyString.isEmpty(url) || CMyString.isEmpty(username) || CMyString.isEmpty(password) || CMyString.isEmpty(id)
				|| CMyString.isEmpty(validationQuery) || CMyString.isEmpty(driver)) {
			return false;
		}
		return true;
	}

	/**
	 * 根据数据源连接属性构造datasource
	 * 
	 * @return
	 */
	public DataSource convertToDataSource() {
		DataSource datasource = new DataSource();
		PoolProperties p = new PoolProperties();
		p.setUrl(url);
		p.setDriverClassName(driver);
		p.setUsername(username);
		p.setPassword(password);
		p.setJmxEnabled(jmxEnabled);
		p.setValidationQuery(validationQuery);

		p.setMaxActive(maxActive);
		p.setInitialSize(initialSize);
		p.setMaxWait(maxWait);
		p.setMaxIdle(maxIdle);
		p.setMinIdle(minIdle);
		p.setMaxAge(maxAge);
		datasource = new DataSource();
		datasource.setPoolProperties(p);
		return datasource;
	}

}