package org.xiaotian.extend;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.xiaotian.constants.ExceptionNumber;

/**
 * 日期时间处理对象的定义和实现
 * 
 * @author xiaotian15
 * 
 */
public class CMyDateTime implements Cloneable, Serializable {

	private static final long serialVersionUID = -7387290207647363072L;

	private java.util.Date m_dtDate = null; // 日期时间值

	private SimpleDateFormat m_dtFormater = null; // 格式化对象

	// 常量定义：日时字符串格式
	/** 日时字符串格式：默认格式 */
	public final static int FORMAT_DEFAULT = 0; // 默认格式

	/** 日时字符串格式：长格式（如：年份用4位表示） */
	public final static int FORMAT_LONG = 1; // 长格式（如：年份用4位表示）

	/** 日时字符串格式：短格式（如：年份用2位表示） */
	public final static int FORMAT_SHORT = 2; // 短格式（如：年份用2位表示）

	/** 默认日期字符串格式 "yyyy-MM-dd" （用于java程序） */
	public final static String DEF_DATE_FORMAT_PRG = "yyyy-MM-dd"; // 默认日期字符串格式（

	// 用于java程序）

	/** 默认时间字符串格式 "HH:mm:ss" （用于java程序） */
	public final static String DEF_TIME_FORMAT_PRG = "HH:mm:ss"; // 默认时间字符串格式（

	// 用于java程序）

	/** 默认日时字符串格式 "yyyy-MM-dd HH:mm:ss" （用于java程序） */
	public final static String DEF_DATETIME_FORMAT_PRG = "yyyy-MM-dd HH:mm:ss"; // 默认日时字符串格式

	// （
	// 用于java程序
	// ）

	/** 默认日时字符串格式 "YYYY-MM-DD HH24:MI:SS" （用于Oracle DB） */
	public final static String DEF_DATETIME_FORMAT_DB = "YYYY-MM-DD HH24:MI:SS";// 默认日时字符串格式

	// （
	// 用于Oracle

	// DB）

	// 常量定义：日期/时间 Field
	/** 日期/时间的各个部分标识：年（1） */
	public final static int YEAR = 1;

	/** 日期/时间的各个部分标识：月（2） */
	public final static int MONTH = 2;

	/** 日期/时间的各个部分标识：日（3） */
	public final static int DAY = 3;

	/** 日期/时间的各个部分标识：时（4） */
	public final static int HOUR = 4;

	/** 日期/时间的各个部分标识：分（5） */
	public final static int MINUTE = 5;

	/** 日期/时间的各个部分标识：秒（6） */
	public final static int SECOND = 6;

	/** 日期/时间的各个部分标识：一刻钟（11） */
	public final static int QUATER = 11;

	/** 日期/时间的各个部分标识：一周（12） */
	public final static int WEEK = 12;

	/** 当月天数（13） */
	public final static int DAY_OF_MONTH = 13;

	/** 当月周数（14） */
	public final static int WEEK_OF_MONTH = 14;

	/** 当年天数（15） */
	public final static int DAY_OF_YEAR = 15;

	/** 当月周数（16） */
	public final static int WEEK_OF_YEAR = 16;

	/** 1 天的时间数（毫秒数） */
	public final static long ADAY_MILLIS = 86400000; // 1 天的时间数（毫秒数）

	public final static String[] MONTHS = { "January", "February", " March", " April", "May", "June", "July", "August", "September", "October",
			"November", "December" };

	public final static String[] WEEKS = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	// =1000*60*60*24

	// =======================================================
	// 构造函数

	/**
	 * 默认构造函数
	 */
	public CMyDateTime() {
		super();
	}

	/**
	 * 使用长整型的数构造日期对象
	 * 
	 * @param p_lDate
	 */
	public CMyDateTime(long p_lDate) {
		m_dtDate = new java.util.Date(p_lDate);
	}

	// =========================================================
	//

	/**
	 * 获取值为系统当前时间的CMyDateTime对象
	 * 
	 * @return
	 */
	public static CMyDateTime now() {
		CMyDateTime mydtNow = new CMyDateTime();
		mydtNow.setDateTimeWithCurrentTime();
		return mydtNow;
	}

	/**
	 * 判断时间对象是否为空
	 * 
	 * @return
	 */
	public boolean isNull() {
		return (m_dtDate == null);
	}

	/**
	 * 获取时间值（毫秒数）
	 * 
	 * @return
	 */
	public long getTimeInMillis() {
		return (m_dtDate == null ? 0 : m_dtDate.getTime());
	}

	/**
	 * 取当前时区差值(单位：毫秒) <br>
	 * 如：东8区，getTimeZoneRawOffset()=1000*60*60*8=28800000
	 * 
	 * @return
	 */
	public static int getTimeZoneRawOffset() {
		TimeZone timeZone = TimeZone.getDefault();
		int nOffset = timeZone.getRawOffset();
		return nOffset;
	}

	// ================================================================
	// 时间比较。
	// 返回值：两者之间的时间差（毫秒数）。
	/**
	 * 时间比较
	 * 
	 * @param p_dtAnother
	 *            待比较的时间对象：java.util.Date对象
	 * @return 两者之间的时间差（毫秒数）。
	 */
	public long compareTo(java.util.Date p_dtAnother) {
		long lMyTime = (m_dtDate == null ? 0 : m_dtDate.getTime());
		long lAnotherTime = (p_dtAnother == null ? 0 : p_dtAnother.getTime());
		return (lMyTime - lAnotherTime);
	}

	/**
	 * 时间比较
	 * 
	 * @param p_mydtAnother
	 *            待比较的时间对象：CMyDateTime对象
	 * @return 两者之间的时间差（毫秒数）。
	 */
	public long compareTo(CMyDateTime p_mydtAnother) {
		return this.compareTo(p_mydtAnother.getDateTime());
	}

	// 取与指定时间之间的（年/月/日/小时/分/秒/季度/周）差
	// 参数：p_nPart：指定获取时间差的部分（年/月/日/小时/分/秒/季度/周）

	/**
	 * 取与指定时间之间的（年/月/日/小时/分/秒/季度/周）差
	 * 
	 * @param p_nPart
	 *            定义返回值的类型（CMyDateTime.YEAR等）
	 * @param p_mydtAnother
	 *            指定的时间对象：CMyDateTime
	 * @return @throws CMyException
	 */
	public long dateDiff(int p_nPart, CMyDateTime p_mydtAnother) throws CMyException {
		if (p_mydtAnother == null) {
			throw new CMyException(ExceptionNumber.ERR_PARAM_INVALID, "无效的日期时间对象参数(CMyDateTime.dateDiff(CMyDateTime)");
		}
		return dateDiff(p_nPart, p_mydtAnother.getDateTime());
	}

	/**
	 * 取与指定时间之间的（年/月/日/小时/分/秒/季度/周）差
	 * 
	 * @param p_nPart
	 *            定义返回值的类型（CMyDateTime.YEAR等）
	 * @param p_dtAnother
	 *            指定的时间对象：java.util.Date
	 * @return @throws CMyException
	 */
	public long dateDiff(int p_nPart, java.util.Date p_dtAnother) throws CMyException {
		// 检验参数的有效性
		if (p_dtAnother == null)
			throw new CMyException(ExceptionNumber.ERR_PARAM_INVALID, "无效的日期时间参数（CMyDateTime.dateDiff(int,java.util.Date)");
		if (this.isNull())
			throw new CMyException(ExceptionNumber.ERR_PARAM_INVALID, "日期时间为空（CMyDateTime.dateDiff(int,java.util.Date)");

		// 对于年和月的处理，由于一年/月的准确天数不确定，
		// 故需要根据具体的年月进行计算和判断；
		if (p_nPart == YEAR)
			return dateDiff_year(p_dtAnother);
		if (p_nPart == MONTH)
			return dateDiff_month(p_dtAnother);

		// else, 对于有准确时间量的单位（天/小时/分/秒）可以通过直接计算时间差得到。
		long lMyTime = (m_dtDate == null ? 0 : m_dtDate.getTime());
		long lAnotherTime = (p_dtAnother == null ? 0 : p_dtAnother.getTime());
		long lDiffTime = (lMyTime - lAnotherTime) / 1000; // 秒数

		switch (p_nPart) {
		// case YEAR: { return lDiffTime/(3600*24)/365; } //year
		case DAY: {
			return lDiffTime / (3600 * 24);
		} // day
		case HOUR: {
			return lDiffTime / 3600;
		} // hour
		case MINUTE: {
			return lDiffTime / 60;
		} // minute
		case SECOND: {
			return lDiffTime;
		} // second

		case QUATER: {
			return lDiffTime / (3600 * 24) / 91;
		} // quater
		case WEEK: {
			return lDiffTime / (3600 * 24) / 7;
		} // week
		default: {
			throw new CMyException(ExceptionNumber.ERR_PARAM_INVALID, "参数无效(CMyDateTime.dateDiff(int,java.util.Date))");
		}
		}// end case
	}// end dateDiff()

	// 计算当前时间与指定时间的 年 差
	// 内部函数，dateDiff中使用
	// 备注：不检测对象的有效性（即是否为空）。检测在调用前做。
	private long dateDiff_year(java.util.Date p_dtAnother) {
		int nYear1, nYear2;
		int nMonth1, nMonth2;

		Calendar cal = new java.util.GregorianCalendar();
		cal.setTimeZone(TimeZone.getDefault());

		cal.setTime(m_dtDate);
		nYear1 = cal.get(Calendar.YEAR);
		nMonth1 = cal.get(Calendar.MONTH);

		cal.setTime(p_dtAnother);
		nYear2 = cal.get(Calendar.YEAR);
		nMonth2 = cal.get(Calendar.MONTH);

		if (nYear1 == nYear2)
			return 0;
		else if (nYear1 > nYear2)
			return (nYear1 - nYear2) + (nMonth1 >= nMonth2 ? 0 : -1);
		else
			return (nYear1 - nYear2) + (nMonth1 > nMonth2 ? 1 : 0);
	}// END:dateDiff_year()

	// 计算当前时间与指定时间的 月 差
	// 内部函数，dateDiff中使用
	// 备注：不检测对象的有效性（即是否为空）。检测在调用前做。
	/**
	 * 计算当前时间对象与指定时间的【月】差
	 * 
	 * @param p_dtAnother
	 *            指定的时间对象：
	 * @return
	 */
	public long dateDiff_month(java.util.Date p_dtAnother) {
		int nMonths1, nMonths2; // 总的月数=年*12+月
		int nDay1, nDay2; // 日期中当月的天数

		Calendar cal = new java.util.GregorianCalendar();
		cal.setTimeZone(TimeZone.getDefault());

		cal.setTime(m_dtDate);
		nMonths1 = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH);
		nDay1 = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(p_dtAnother);
		nMonths2 = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH);
		nDay2 = cal.get(Calendar.DAY_OF_MONTH);

		if (nMonths1 == nMonths2)
			return 0;
		else if (nMonths1 > nMonths2)
			return nMonths1 - nMonths2 + (nDay1 < nDay2 ? -1 : 0);
		else
			return nMonths1 - nMonths2 + (nDay1 > nDay2 ? 1 : 0);
	}// END:dateDiff_month

	// 日期/时间的分解
	// 参数：p_nField指定域（年/月/日/小时/分/秒/星期）编号
	/**
	 * 日期/时间的分解
	 * 
	 * @param p_nField
	 *            指定域（年/月/日/小时/分/秒/星期）编号（定义在CMyDateTime.YEAR）
	 * @return @throws CMyException
	 */
	public int get(int p_nField) throws CMyException {
		if (m_dtDate == null) {
			throw new CMyException(ExceptionNumber.ERR_OBJ_NULL, "日期时间为空（CMyDateTime.get）");
		}

		Calendar cal = new java.util.GregorianCalendar();
		cal.setTimeZone(TimeZone.getDefault());
		cal.setTime(m_dtDate);
		switch (p_nField) {
		case YEAR: {
			return cal.get(Calendar.YEAR);
		}
		case MONTH: {
			return cal.get(Calendar.MONTH) + 1;
		}
		case DAY: {
			return cal.get(Calendar.DAY_OF_MONTH);
		}
		case HOUR: {
			return cal.get(Calendar.HOUR_OF_DAY);
		}
		case MINUTE: {
			return cal.get(Calendar.MINUTE);
		}
		case SECOND: {
			return cal.get(Calendar.SECOND);
		}
		case WEEK: {
			// REM: cal.setFirstDayOfWeek( Calendar.MONDAY )
			// is not used, because it doesn't work!
			// return (cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7;
			return cal.get(Calendar.DAY_OF_WEEK);
		}
		case DAY_OF_MONTH: {
			return ((GregorianCalendar) cal).getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		case WEEK_OF_MONTH: {
			return getWeekCountsOfMonth(true);
		}
		case DAY_OF_YEAR: {
			return ((GregorianCalendar) cal).getActualMaximum(Calendar.DAY_OF_YEAR);
		}
		case WEEK_OF_YEAR: {
			return ((GregorianCalendar) cal).getActualMaximum(Calendar.WEEK_OF_YEAR);
		}
		default: {
			throw new CMyException(ExceptionNumber.ERR_PARAM_INVALID, "无效的日期时间域参数（CMyDateTime.get）");
		}
		}// end case
	}// end: get()

	/**
	 * 获取当前对象中的【年】的部分
	 * 
	 * @return @throws CMyException
	 */
	public int getYear() throws CMyException {
		return this.get(YEAR);
	}

	/**
	 * 获取当前对象中的【月】的部分
	 * 
	 * @return @throws CMyException
	 */
	public int getMonth() throws CMyException {
		return this.get(MONTH);
	}

	/**
	 * 获取当前对象中的【日】的部分
	 * 
	 * @return @throws CMyException
	 */
	public int getDay() throws CMyException {
		return this.get(DAY);
	}

	/**
	 * 获取当前对象中的【小时】的部分
	 * 
	 * @return @throws CMyException
	 */
	public int getHour() throws CMyException {
		return this.get(HOUR);
	}

	/**
	 * 获取当前对象中的【分】的部分
	 * 
	 * @return @throws CMyException
	 */
	public int getMinute() throws CMyException {
		return this.get(MINUTE);
	}

	/**
	 * 获取当前对象中的【秒】的部分
	 * 
	 * @return @throws CMyException
	 */
	public int getSecond() throws CMyException {
		return this.get(SECOND);
	}

	/**
	 * 取当前日期是所在week的第几天 <br>
	 * 说明：一个礼拜的第一天为Monday(0)，最后一天为Sunday(6)
	 * 
	 * @return @throws CMyException
	 */
	public int getDayOfWeek() throws CMyException {
		return this.get(WEEK);
	}

	// 日期时间增/减函数
	// 参数：p_nField：指定域；p_nAdd：增加数目（负值为减）
	/**
	 * 日期时间增/减函数
	 * 
	 * @param p_nField
	 *            指定域（例如CMyDateTime.YEAR等）
	 * @param p_nAdd
	 *            增加数目（负值为减）
	 * @return 返回当前对象本身
	 * @throws CMyException
	 */
	public CMyDateTime dateAdd(int p_nField, int p_nAdd) throws CMyException {
		if (m_dtDate == null) {
			throw new CMyException(ExceptionNumber.ERR_OBJ_NULL, "日期时间为空（CMyDateTime.dateAdd）");
		}
		// caohui@2004-1-3 修改计算的不准确
		int nCalField = 0;
		switch (p_nField) {
		case YEAR:
			nCalField = Calendar.YEAR;
			break;
		case MONTH:
			nCalField = Calendar.MONTH;
			break;
		case WEEK:
			nCalField = Calendar.DATE;
			p_nAdd = p_nAdd * 7;
			break;
		case DAY:
			nCalField = Calendar.DATE;
			break;
		case HOUR:
			nCalField = Calendar.HOUR;
			break;
		case MINUTE:
			nCalField = Calendar.MINUTE;
			break;
		case SECOND:
			nCalField = Calendar.SECOND;
			break;
		default: {
			throw new CMyException(ExceptionNumber.ERR_PARAM_INVALID, "无效的日期时间域参数（CMyDateTime.dateAdd）");
		}
		}// end case

		Calendar cal = new GregorianCalendar();
		cal.setTimeZone(TimeZone.getDefault());
		cal.setTime(m_dtDate);
		cal.set(nCalField, cal.get(nCalField) + p_nAdd);
		m_dtDate = cal.getTime();
		return this;
	}// end: dateAdd

	// =========================================================
	// 对象克隆
	/**
	 * 对象克隆
	 * 
	 * @return
	 */
	public synchronized Object clone() {
		CMyDateTime newMyDateTime = new CMyDateTime();
		newMyDateTime.m_dtDate = (m_dtDate == null ? null : (java.util.Date) m_dtDate.clone());
		newMyDateTime.m_dtFormater = (m_dtFormater == null ? null : (SimpleDateFormat) m_dtFormater.clone());
		return newMyDateTime;
	}

	// 取日期时间对象java.util.Date
	/**
	 * 将当前对象转换为java.util.Date对象
	 * 
	 * @return
	 */
	public java.util.Date getDateTime() {
		return m_dtDate;
	}

	/** @see public String toString( String p_sFormat ) */
	public String toString() {
		return toString(DEF_DATETIME_FORMAT_PRG);
	}

	/**
	 * 将日期数据转化为指定格式的字符串
	 * 
	 * @param p_sFormat
	 *            指定的格式（例如"yyyy-MM-dd HH:mm:ss"）可省略，默认为"yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public String toString(String p_sFormat) {
		if (m_dtDate == null)
			return null;
		try {
			return getDateTimeAsString(p_sFormat);
		} catch (CMyException ex) {
			return null;
		}// end try
	}

	// wenyh@2009-7-10 comment:添加方法,以支持带有本地化信息的时间格式化.
	public String toString(String sFormat, String sLocaleId, String sTimezoneId) {
		if (m_dtDate == null) {
			return null;
		}
		boolean bWithLocale = !CMyString.isEmpty(sLocaleId);
		boolean bWithTimezone = !CMyString.isEmpty(sTimezoneId);
		if (!(bWithLocale || bWithTimezone)) {
			return toString(sFormat);
		}
		try {
			Locale locale = bWithLocale ? new Locale(sLocaleId) : Locale.getDefault();
			TimeZone timeZone = bWithTimezone ? TimeZone.getTimeZone(sTimezoneId) : TimeZone.getDefault();
			DateFormat df = new SimpleDateFormat(sFormat, locale);
			df.setTimeZone(timeZone);
			return df.format(m_dtDate);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 转化为java.sq.Date型数据
	 * 
	 * @return
	 */
	public java.sql.Date toDate() {
		if (m_dtDate == null)
			return null;
		return new java.sql.Date(m_dtDate.getTime());
	}

	/**
	 * 转化为java.sql.Time型数据
	 * 
	 * @return
	 */
	public java.sql.Time toTime() {
		if (m_dtDate == null)
			return null;
		return new java.sql.Time(m_dtDate.getTime());
	}

	/**
	 * 转化为java.sql.Timestamp型数据
	 * 
	 * @return
	 */
	public java.sql.Timestamp toTimestamp() {
		if (m_dtDate == null)
			return null;
		return new java.sql.Timestamp(m_dtDate.getTime());
	}

	// //////////////////////////////////////////////////////
	// do with datetime
	// 设置日期时间型数据

	/**
	 * 使用 java.util.Date 对象设置日期和时间
	 * 
	 * @param p_dtDate
	 *            java.util.Date 对象
	 */
	public void setDateTime(java.util.Date p_dtDate) {
		m_dtDate = p_dtDate;
	}

	/**
	 * 使用字符串设置日期和时间
	 * 
	 * @param p_sValue
	 *            日期和时间字符串
	 * @param p_sFormat
	 *            日期和时间字符串格式
	 * @return @throws CMyException
	 */
	public boolean setDateTimeWithString(String p_sValue, String p_sFormat) throws CMyException {
		try {
			SimpleDateFormat dtFormat = new SimpleDateFormat(p_sFormat);
			m_dtDate = dtFormat.parse(p_sValue);
			return true;
		} catch (Exception ex) {
			throw new CMyException(ExceptionNumber.ERR_PARAM_INVALID, "日期时间字符串值和格式无效（CMyDateTime.setDateTimeWithString）", ex);
		}// end try
	}

	/**
	 * 设置日期和时间为系统当前日期时间
	 */
	public void setDateTimeWithCurrentTime() {
		if (m_dtDate == null) {
			m_dtDate = new java.util.Date(System.currentTimeMillis());
		} else {
			m_dtDate.setTime(System.currentTimeMillis());
		}// end if
	}

	/**
	 * 使用Timestamp对象设置日期和时间
	 * 
	 * @param p_tsDateTime
	 *            java.sql.Timestamp对象
	 * @throws CMyException
	 */
	public void setDateTimeWithTimestamp(java.sql.Timestamp p_tsDateTime) throws CMyException {
		try {
			if (p_tsDateTime == null) {
				m_dtDate = null;
			} else {
				if (m_dtDate == null) {
					m_dtDate = new java.util.Date();
				}
				m_dtDate.setTime(p_tsDateTime.getTime());
			}// end if
		} catch (Exception ex) {
			throw new CMyException(ExceptionNumber.ERR_UNKNOWN, "使用Timestamp对象设置日期和时间出错：CMyDateTime.setDateTimeWithTimestamp()", ex);
		}// end try
	}

	/**
	 * 从记录集当前行读取指定日期/时间字段的值
	 * 
	 * @param p_rsData
	 *            java.sql.ResultSet对象
	 * @param p_nFieldIndex
	 *            指定Date字段的位置
	 * @throws CMyException
	 */
	public void setDateTimeWithRs(java.sql.ResultSet p_rsData, int p_nFieldIndex) throws CMyException {
		try {
			java.sql.Timestamp tsDateTime = p_rsData.getTimestamp(p_nFieldIndex);
			setDateTimeWithTimestamp(tsDateTime);
		} catch (java.sql.SQLException ex) {
			throw new CMyException(ExceptionNumber.ERR_DBOP_FAIL, "从记录集中读取时间字段时出错：CMyDateTime.setDateTimeWithRs()", ex);
		}// end try
	}

	/**
	 * 从记录集当前行读取指定日期/时间字段的值
	 * 
	 * @param p_rsData
	 *            java.sql.ResultSet对象
	 * @param p_sFieldName
	 *            指定Date字段名称
	 * @throws CMyException
	 */
	public void setDateTimeWithRs(java.sql.ResultSet p_rsData, String p_sFieldName) throws CMyException {
		try {
			java.sql.Timestamp tsDateTime = p_rsData.getTimestamp(p_sFieldName);
			setDateTimeWithTimestamp(tsDateTime);
		} catch (java.sql.SQLException ex) {
			throw new CMyException(ExceptionNumber.ERR_DBOP_FAIL, "从记录集中读取时间字段时出错：CMyDateTime.setDateTimeWithRs()", ex);
		}// end try
	}

	// ///////////////////////////////////////////////////////
	// 设置日期或时间数据

	/**
	 * 设置日期值 <br>
	 * 说明：若当前日期时间非空，则仅改变日期值，时间值保持不变
	 * 
	 * @param p_dDate
	 * @return @throws CMyException
	 */
	public boolean setDate(java.sql.Date p_dDate) throws CMyException {
		if (p_dDate == null)
			return false;
		return this.setDateWithString(p_dDate.toString(), FORMAT_DEFAULT);
	}

	/**
	 * 设置时间值 <br>
	 * 说明：若当前日期时间非空，则仅改变时间值，日期值保持不变
	 * 
	 * @param p_tTime
	 * @return @throws CMyException
	 */
	public boolean setTime(java.sql.Time p_tTime) throws CMyException {
		if (p_tTime == null)
			return false;
		return this.setTimeWithString(p_tTime.toString(), FORMAT_DEFAULT);
	}

	/**
	 * 使用字符串设置日期值
	 * 
	 * @param p_sDateValue
	 *            日期值字符串
	 * @param p_nFormatType
	 *            日期格式类型
	 * @return @throws CMyException 若格式不正确，则抛出异常
	 */
	public boolean setDateWithString(String p_sDateValue, int p_nFormatType) throws CMyException {
		String sDateValue, sTimeValue;
		boolean blHasSepChar = false; // if Date string value has seperator char
		int nLen = p_sDateValue.length();

		if (nLen < 6)
			throw new CMyException(ExceptionNumber.ERR_PARAM_INVALID, "日期字符串无效（CMyDateTime.setDateWithString）");
		try {
			switch (p_nFormatType) {
			case FORMAT_LONG: {
				// form: (1)has seperator: yyyy-mm-dd / yyyy.mm.dd
				// (2)has no seperator: yyyymmdd
				blHasSepChar = (nLen >= 10);
				sDateValue = p_sDateValue.substring(0, 4) + "-" + p_sDateValue.substring((blHasSepChar ? 5 : 4), (blHasSepChar ? 7 : 6)) + "-"
						+ p_sDateValue.substring((blHasSepChar ? 8 : 6), (blHasSepChar ? 10 : 8));
				break;
			}
			case FORMAT_SHORT: {
				// form: (1)has seperator: yy.mm.dd / yy-mm-dd
				// (2)has no seperator:
				sDateValue = (p_sDateValue.charAt(0) < '5' ? "20" : "19");
				blHasSepChar = (nLen >= 8);
				sDateValue += p_sDateValue.substring(0, 2) + "-" + p_sDateValue.substring((blHasSepChar ? 3 : 2), (blHasSepChar ? 5 : 4)) + "-"
						+ p_sDateValue.substring((blHasSepChar ? 6 : 4), (blHasSepChar ? 8 : 6));
				break;
			}
			default: { // format: yyyy-mm-dd
				sDateValue = p_sDateValue;
				break;
			}
			}// end case

			if (this.m_dtDate == null) {
				return this.setDateTimeWithString(sDateValue, DEF_DATE_FORMAT_PRG);
			}
			// else
			sTimeValue = getDateTimeAsString(DEF_TIME_FORMAT_PRG);
			return this.setDateTimeWithString(sDateValue + " " + sTimeValue, DEF_DATETIME_FORMAT_PRG);
		} catch (Exception ex) {
			throw new CMyException(ExceptionNumber.ERR_PARAM_INVALID, "无效的日期字符串（CMyException.setDateWithString）", ex);
		}// end try
	}// end: setDateWithString()

	/**
	 * 使用字符串设置时间值
	 * 
	 * @param p_sTimeValue
	 *            时间值字符串
	 * @param p_nFormatType
	 *            时间格式类型（例如CMyDateTime.FORMAT_LONG等）
	 * @return @throws CMyException 若格式不正确，则抛出异常
	 */
	public boolean setTimeWithString(String p_sTimeValue, int p_nFormatType) throws CMyException {
		String sDateValue, sTimeValue;
		boolean blHasSepChar = false; // if Date string value has seperator char
		int nLen = p_sTimeValue.length();

		if (nLen < 4)
			throw new CMyException(ExceptionNumber.ERR_PARAM_INVALID, "时间字符串格式无效（）");
		try {
			switch (p_nFormatType) {
			case FORMAT_LONG: {
				// form: (1)has seperator: HH:mm:ss
				// (2)has no seperator: HHmmss
				blHasSepChar = (nLen >= 8);
				sTimeValue = p_sTimeValue.substring(0, 2) + ":" + p_sTimeValue.substring((blHasSepChar ? 3 : 2), (blHasSepChar ? 5 : 4)) + ":"
						+ p_sTimeValue.substring((blHasSepChar ? 6 : 4), (blHasSepChar ? 8 : 6));
				break;
			}
			case FORMAT_SHORT: {
				// form: (1)has seperator: HH:mm
				// (2)has no seperator: HHmm
				blHasSepChar = (nLen >= 5);
				sTimeValue = p_sTimeValue.substring(0, 2) + ":" + p_sTimeValue.substring((blHasSepChar ? 3 : 2), (blHasSepChar ? 5 : 4)) + ":00";
				break;
			}
			default: { // format: HH:mm:ss
				sTimeValue = p_sTimeValue;
				break;
			}
			}// end case

			if (m_dtDate == null) {
				return this.setDateTimeWithString(sTimeValue, DEF_TIME_FORMAT_PRG);
			}
			// else
			sDateValue = getDateTimeAsString(DEF_DATE_FORMAT_PRG);
			return this.setDateTimeWithString(sDateValue + " " + sTimeValue, DEF_DATETIME_FORMAT_PRG);
		} catch (Exception ex) {
			throw new CMyException(ExceptionNumber.ERR_PARAM_INVALID, "无效的时间字符串（CMyException.setTimeWithString）", ex);
		}// end try
	}// end: setTimeWithString()

	// //////////////////////////////////////////////////
	// 格式化输出字符串

	/**
	 * 设置日期时间字符串格式
	 * 
	 * @param p_sFormat
	 *            指定格式（例如："yyyy-MM-dd HH:mm:ss"）
	 */
	public void setDateTimeFormat(String p_sFormat) {
		if (m_dtFormater == null) {
			m_dtFormater = new SimpleDateFormat(p_sFormat);
		} else {
			m_dtFormater.applyPattern(p_sFormat);
		}// end if
	}

	/**
	 * 获取格式化的日期时间字符串
	 * 
	 * @param p_sFormat
	 *            指定日期时间字符串格式（例如："yyyy-MM-dd HH:mm:ss"）
	 * @return @throws CMyException
	 */
	public String getDateTimeAsString(String p_sFormat) throws CMyException {
		if (m_dtDate == null)
			return null;
		try {
			this.setDateTimeFormat(p_sFormat);
			return m_dtFormater.format(m_dtDate);
		} catch (Exception ex) {
			throw new CMyException(ExceptionNumber.ERR_PARAM_INVALID, "指定的日期时间格式有错（CMyDateTime.getDateTimeAsString）", ex);
		}// end try
	}

	/**
	 * 输出格式化的日期时间字符串 <br>
	 * 格式：由setDateTimeFormat指定
	 * 
	 * @return 若日期时间为空，或者格式化对象m_dtFormater为空，返回null.
	 * @throws CMyException
	 */
	public String getDateTimeAsString() throws CMyException {
		if ((m_dtDate == null) || (m_dtFormater == null))
			return null;
		try {
			return m_dtFormater.format(m_dtDate);
		} catch (Exception ex) {
			throw new CMyException(ExceptionNumber.ERR_UNKNOWN, "格式化日期时间字符串出错（CMyDateTime.getDateTimeAsString()）", ex);
		}
	}

	// =========================================================================
	// 扩展支持

	// private final static String formats[] = { "yyyy.MM.dd HH:mm:ss",
	// "yyyy.M.d H:m:s" , "yyyy-MM-dd HH:mm:ss", "yyyy-M-d H:m:s", "yyyy/MM/dd
	// HH:mm:ss", "yyyy/M/d H:m:s" };
	// private final static String shortFormats[] = { "yyyy.MM.dd", "yyyy.M.d" ,
	// "yyyy-MM-dd", "yyyy-M-d", "yyyy/MM/dd", "yyyy/M/d", "yy.MM.dd", "yy.M.d"
	// , "yy-MM-dd", "yy-M-d", "yy/MM/dd", "yy/M/d" };

	// why@2002-06-14：解析未知各式的日期时间字符串
	/**
	 * 提取 日期时间 字符串数据的格式
	 * 
	 * @param _sValue
	 *            指定的日期时间字符串
	 * @return 若正确解析，则返回日时字符串的格式字符串；否则返回null。
	 */
	public static String extractDateTimeFormat(String _sValue) {
		final char[] FORMAT_CHAR = { 'y', 'M', 'd', 'H', 'm', 's' }; // 域字符

		return extractFormat(_sValue, FORMAT_CHAR);
	}

	/**
	 * 提取 日期 字符串数据的格式
	 * 
	 * @param _sValue
	 *            指定的日期字符串
	 * @return 若正确解析，则返回日期字符串的格式字符串；否则返回null。
	 */
	public static String extractDateFormat(String _sValue) {
		final char[] FORMAT_CHAR = { 'y', 'M', 'd' }; // 域字符

		return extractFormat(_sValue, FORMAT_CHAR);
	}

	/**
	 * 提取 时间 字符串数据的格式
	 * 
	 * @param _sValue
	 *            指定的时间字符串
	 * @return 若正确解析，则返回时间字符串的格式字符串；否则返回null。
	 */
	public static String extractTimeFormat(String _sValue) {
		final char[] FORMAT_CHAR = { 'H', 'm', 's' }; // 域字符

		return extractFormat(_sValue, FORMAT_CHAR);
	}

	/**
	 * 内部函数，解析指定日期时间字符串的格式。
	 * 
	 * @param _sValue
	 *            日期或时间或日时字符串
	 * @param _formatChar
	 *            域字符集。如：日期的域字符集为{'y','M','d'}
	 * @return 若解析成功，则返回字符串对应的格式；否则返回null.
	 */
	private static String extractFormat(String _sValue, char[] _formatChar) {
		// 检查参数有效性
		if (_sValue == null)
			return null;

		char[] buffValue = _sValue.trim().toCharArray();
		if (buffValue.length == 0)
			return null;

		// 解析_sValue的数据格式 （如：yyyy-MM-dd HH:mm:ss ）
		StringBuffer buffFormat = new StringBuffer(19); // 用于保存_sValue的格式化

		int nAt = 0, nAtField = 0;
		char aChar;

		while (nAt < buffValue.length) {
			aChar = buffValue[nAt++];
			if (Character.isDigit(aChar)) { // 是数字
				buffFormat.append(_formatChar[nAtField]); // 填充格式
			} else {
				buffFormat.append(aChar); // 分割符
				nAtField++; // 当前域结束
				if (nAtField >= _formatChar.length)
					break;
			}
		}// end while

		return buffFormat.toString();
	}

	// why@2002-06-14：解析未知格式的日期时间字符串
	/**
	 * 使用未知格式的字符串值设置日期时间
	 * 
	 * @param _sValue
	 *            指定的日期时间值（字符串）
	 * @return 若设置成功，则返回true；否则返回false.
	 * @throws CMyException
	 *             解析日期时间值（字符串）可能抛出异常
	 */
	public boolean setDateTimeWithString(String _sValue) throws CMyException {
		String sFormat = extractDateTimeFormat(_sValue);
		if (_sValue == null)
			return false;

		// else
		return this.setDateTimeWithString(_sValue, sFormat);
	}

	/**
	 * 把以毫秒计算的使用时间格式化为“xxx分xx秒”的格式
	 * 
	 * @param iTimeMillis
	 *            毫秒数
	 * @return 格式化的时间
	 */
	public final static String formatTimeUsed(long iMillis) {
		if (iMillis <= 0) {
			return "";
		}

		int iSecond = 0;
		int iMinute = 0;
		StringBuffer sb = new StringBuffer(16);
		iSecond = (int) (iMillis / 1000);
		iMillis = iMillis % 1000;
		if (iSecond > 0) {
			iMinute = iSecond / 60;
			iSecond = iSecond % 60;
		}
		if (iMinute > 0) {
			if (iMinute > 1) {
				sb.append(iMinute).append("分");
			} else {
				sb.append(iMinute).append("分");
			}
			if (iSecond < 10) {
				sb.append('0');
			}
			sb.append(iSecond);
		} else {
			sb.append(iSecond).append('.');
			if (iMillis < 10) {
				sb.append('0').append('0');
			} else if (iMillis < 100) {
				sb.append('0');
			}
			sb.append(iMillis);
		}
		if (iMillis > 1) {
			sb.append("秒");
		} else {
			sb.append("秒");
		}
		// 的国际化

		return sb.toString();
	}

	public static String getStr(Object _dtTime, String _sFormat) {
		if (_dtTime instanceof CMyDateTime) {
			return ((CMyDateTime) _dtTime).toString(_sFormat);
		}

		return CMyString.showObjNull(_dtTime);
	}

	// /////////////////////////////////////////////////////////
	// the following is set to test for class

	public static void main(String[] args) {
		CMyDateTime myDateTime = new CMyDateTime();

		try {
			CMyDateTime now = CMyDateTime.now();
			System.out.println("now:" + now.toString(CMyDateTime.DEF_DATETIME_FORMAT_PRG));
			CMyDateTime nowClone = (CMyDateTime) now.clone();

			CMyDateTime execStartTime = nowClone.dateAdd(CMyDateTime.DAY, -30);
			System.out.println("now-30:" + execStartTime.toString(CMyDateTime.DEF_DATETIME_FORMAT_PRG));
			System.out.println("now:" + now.toString(CMyDateTime.DEF_DATETIME_FORMAT_PRG));
			System.out.println("nowClone:" + nowClone.toString(CMyDateTime.DEF_DATETIME_FORMAT_PRG));

			now = CMyDateTime.now();
			execStartTime = now.dateAdd(CMyDateTime.DAY, -3);
			System.out.println("now-3:" + execStartTime.toString(CMyDateTime.DEF_DATETIME_FORMAT_PRG));

			now = CMyDateTime.now();
			execStartTime = now.dateAdd(CMyDateTime.HOUR, -3);
			System.out.println("now-3:" + execStartTime.toString(CMyDateTime.DEF_DATETIME_FORMAT_PRG));

			execStartTime.setDateTimeWithString("2002.1.1 00:00:00", "yyyy.MM.dd HH:mm:ss");
			System.out.println("time:" + execStartTime.toString(CMyDateTime.DEF_DATETIME_FORMAT_PRG));

			execStartTime.setDateTimeWithString(execStartTime.toString(CMyDateTime.DEF_DATE_FORMAT_PRG) + " 23:00:00",
					CMyDateTime.DEF_DATETIME_FORMAT_PRG);

			CMyDateTime execEndTime = CMyDateTime.now();
			execEndTime.setDateTimeWithString(execEndTime.toString(CMyDateTime.DEF_DATE_FORMAT_PRG) + " 24:00:00",
					CMyDateTime.DEF_DATETIME_FORMAT_PRG);

			// String wcmPath =
			// app.getInitProperty("WCM_PATH");
			System.out.println("now:" + now.toString());
			System.out.println("execStartTime:" + execStartTime.toString());
			System.out.println("now.compareTo(execStartTime):" + now.compareTo(execStartTime));

			System.out.println("TimeZone = " + CMyDateTime.getTimeZoneRawOffset());

			myDateTime.setDateTimeWithCurrentTime();
			System.out.println("Start:" + myDateTime.getDateTimeAsString("yyyy/MM/dd HH:mm:ss"));

			long nTime = myDateTime.getTimeInMillis() % (1000 * 3600);
			System.out.print("\nTime=" + nTime);
			java.sql.Time tempTime = new java.sql.Time(nTime);
			System.out.print("  " + tempTime.toString());
			System.out.print("\n");

			// test for Date
			myDateTime.setDateWithString("2001-04-15", CMyDateTime.FORMAT_DEFAULT);
			System.out.println(myDateTime.getDateTimeAsString("yyyy.MM.dd"));

			myDateTime.setDateWithString("000505", CMyDateTime.FORMAT_SHORT);
			System.out.println(myDateTime.getDateTimeAsString("yyyy.MM.dd"));

			// test for Time
			myDateTime.setTimeWithString("12:01:02", CMyDateTime.FORMAT_DEFAULT);
			System.out.println(myDateTime.getDateTimeAsString("HH:mm:ss"));

			myDateTime.setTimeWithString("00:25", CMyDateTime.FORMAT_SHORT);
			System.out.println(myDateTime.getDateTimeAsString("yyyy-MM-dd HH:mm:ss"));

			// test for DateTime
			java.sql.Date dDate = new java.sql.Date(0);
			java.sql.Time tTime = new java.sql.Time(0);
			dDate = Date.valueOf("1978-02-04");
			tTime = Time.valueOf("12:00:20");
			System.out.println(myDateTime.getDateTimeAsString("yyyy/MM/dd HH:mm:ss"));
			myDateTime.setDate(dDate);
			System.out.println(myDateTime.getDateTimeAsString("yyyy/MM/dd HH:mm:ss"));
			myDateTime.setTime(tTime);
			System.out.println(myDateTime.getDateTimeAsString("yyyy/MM/dd HH:mm:ss"));

			myDateTime.setDateTimeWithCurrentTime();
			System.out.println("End:" + myDateTime.getDateTimeAsString("yyyy/MM/dd HH:mm:ss"));

			// test for dateDiff
			CMyDateTime myDateTime2 = new CMyDateTime();
			int nFields[] = { CMyDateTime.YEAR, CMyDateTime.MONTH, CMyDateTime.DAY, CMyDateTime.HOUR, CMyDateTime.MINUTE, CMyDateTime.SECOND,
					CMyDateTime.QUATER, CMyDateTime.WEEK };
			myDateTime2.setDateTimeWithString("2001-02-07 14:34:00", CMyDateTime.DEF_DATETIME_FORMAT_PRG);
			myDateTime.setDateTimeWithString("2001-03-07 15:35:01", CMyDateTime.DEF_DATETIME_FORMAT_PRG);

			for (int i = 0; i < 8; i++) {
				long lDateDiff = myDateTime.dateDiff(nFields[i], myDateTime2.getDateTime());
				System.out.println("DateDiff(" + nFields[i] + ")=" + lDateDiff);
			}// end for

			for (int i = 0; i < 6; i++) {
				System.out.println("get(" + nFields[i] + ")=" + myDateTime.get(nFields[i]));
			}// end for
			System.out.println("getWeek=" + myDateTime.get(CMyDateTime.WEEK));

			System.out.println("Test for dateAdd()");
			System.out.println("oldDateTime = " + myDateTime.toString());
			myDateTime.dateAdd(CMyDateTime.YEAR, 12);
			System.out.println("dateAdd(YEAR,12) = " + myDateTime.toString());
			myDateTime.dateAdd(CMyDateTime.YEAR, -12);
			System.out.println("dateAdd(YEAR,-12) = " + myDateTime.toString());
			myDateTime.dateAdd(CMyDateTime.MONTH, -3);
			System.out.println("dateAdd(MONTH,-3) = " + myDateTime.toString());
			myDateTime.dateAdd(CMyDateTime.DAY, 10);
			System.out.println("dateAdd(DAY,10) = " + myDateTime.toString());

			myDateTime.setDateTimeWithCurrentTime();
			int nWeek = myDateTime.getDayOfWeek();
			myDateTime.dateAdd(CMyDateTime.DAY, -nWeek);
			System.out.println("Monday of this week is:" + myDateTime.toString(CMyDateTime.DEF_DATE_FORMAT_PRG));
			for (int j = 1; j < 7; j++) {
				myDateTime.dateAdd(CMyDateTime.DAY, 1);
				System.out.println((j + 1) + " of this week is:" + myDateTime.toString(CMyDateTime.DEF_DATE_FORMAT_PRG));
			}

			// test for CMyDateTime.set()
			System.out.println("\n\n===== test for CMyDateTime.set() ====== ");
			String[] sValues = { "2002.06.13 12:00:12", "1900.2.4 3:4:5", "1901-03-15 23:05:10", "1978-2-4 5:6:7", "2001/12/31 21:08:22",
					"1988/2/5 9:1:2", "1986.12.24", "0019.2.8", "2002-12-20", "1999-8-1", "2001/12/21", "2000/1/5", "78.02.04", "89.2.6", "99-12-31",
					"22-3-6", "01/02/04", "02/5/8" };
			for (int j = 0; j < sValues.length; j++) {
				myDateTime.setDateTimeWithString(sValues[j]);
				System.out.println("[" + j + "]" + CMyDateTime.extractDateTimeFormat(sValues[j]) + "  " + myDateTime.toString());
			}
		} catch (CMyException ex) {
			ex.printStackTrace(System.out);
		}// end try
	}

	public boolean isLeapYear() throws CMyException {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(m_dtDate);
		return calendar.isLeapYear(this.getYear());
	}

	public boolean isToday() {
		CMyDateTime today = CMyDateTime.now();
		return this.toString("yyyy-MM-dd").equals(today.toString("yyyy-MM-dd"));
	}

	public int getWeekCountsOfMonth(boolean _bSundayStart) throws CMyException {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(m_dtDate);
		int nWeekCounts = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

		if (_bSundayStart) {
			return nWeekCounts;
		}

		CMyDateTime firstDay = new CMyDateTime();
		firstDay.setDateTime(m_dtDate);
		firstDay.setDateTimeWithString(firstDay.getYear() + "-" + firstDay.getMonth() + "-1");

		if (firstDay.getDayOfWeek() == 6) {
			nWeekCounts += 1;
		}

		return nWeekCounts;
	}

	/**
	 * Returns <code>true</code> if this datetime equals with the specified one.
	 * 
	 * @param _another
	 *            another object to compare
	 * @return <code>true</code> if this datetime equals with the specified one.
	 */
	public boolean equals(Object _another) {
		return (_another != null) && (_another instanceof CMyDateTime) && (((CMyDateTime) _another).getTimeInMillis() == this.getTimeInMillis());
	}

	/**
	 * 测试日期格式是否合法
	 * 
	 * @param _sFieldValue
	 * @return
	 */
	public boolean testDateFormat(String _sFieldValue) {
		try {
			boolean isValid = setDateTimeWithString(_sFieldValue);
			return isValid;
		} catch (Exception e) {
			return false;
		}
	}
}
