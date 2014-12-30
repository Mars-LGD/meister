/*
 * History			Who			What
 * 2006-03-15		wenyh		修正splitToInt(String,String)函数:
 * 									如果第一个参数为空,返回长度为0的数组,如果第二个为空,则默认使用','分隔
 * 2007.20.28		wenyh		修正setStrEndWith的逻辑,避免StringIndexOutOfBoundsException错误.
 * 
 */

package com.xiaotian.extend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xiaotian.constant.ExceptionNumber;
import com.xiaotian.exception.WCMException;
import com.xiaotian.language.I18NMessage;
import com.xiaotian.utils.URLCoder;

/**
 * 字符串处理对象<BR>
 * 
 * @author xiaotian15
 * 
 */
public class CMyString {

	/** 默认字符编码集 */
	public static String ENCODING_DEFAULT = "UTF-8";

	public static String GET_ENCODING_DEFAULT = "UTF-8";

	public static String FILE_WRITING_ENCODING = "GBK";

	private static final int PGVARNAME_MAX_LENGTH = 20;

	public static final int DEFAULT_ENTER_NUM = 1;

	public static String HTML_TAGS = "(?im)</?(?:STYLE|A|META|ACRONYM|ADDRESS|APPLET|AREA|B|BASE|BASEFONT|BDO|BGSOUND|BIG|BLOCKQUOTE|BODY|BR|BUTTON|CAPTION|CENTER|CITE|CODE|COL|COLGROUP|COMMENT|CUSTOM|DD|DEL|DFN|DIR|DIV|DL|DT|EM|EMBED|FIELDSET|FONT|FORM|FRAME|FRAMESET|HEAD|hn|HR|HTML|I|IFRAME|IMG|INPUT|INS|ISINDEX|KBD|LABEL|LEGEND|LI|LINK|LISTING|MAP|MARQUEE|MENU|nextID|NOBR|NOFRAMES|NOSCRIPT|OBJECT|OL|OPTION|P|PLAINTEXT|PRE|Q|RT|RUBY|S|SAMP|SCRIPT|SELECT|SMALL|SPAN|STRIKE|STRONG|styleSheet|SUB|SUP|TABLE|TBODY|TD|TEXTAREA|TFOOT|TH|THEAD|TITLE|TR|TT|U|UL|VAR|WBR|XML|XMP|\\?*[a-z1-9]+:?)[^>]*>";

	/** 定义正常sql中不应该在单引号外边出现的字符，如果在单引号外出现，认为是非法字符 */
	private static char[] charSQLEnds = new char[] { ';', '\n', '\r' };

	public CMyString() {
		super();
	}

	// ==========================================================================
	// ====
	// 常用字符串函数

	/**
	 * 判断指定字符串是否为空
	 * 
	 * @param _string
	 *            指定的字符串
	 * @return 若字符串为空对象（_string==null）或空串（长度为0），则返回true；否则，返回false.
	 */
	public static boolean isEmpty(String _string) {
		return ((_string == null) || (_string.trim().length() == 0));
	}

	/**
	 * 判断指定字符串是否为空
	 * 
	 * @param _string
	 *            指定的字符串
	 * @return 若字符串为空对象（_string==null）或空串（长度为0），则返回true；否则，返回false.
	 * @deprecated 由函数 isEmpty 替换
	 * @see isEmpty( String _string )
	 */
	public static boolean isEmptyStr(String _string) {
		return ((_string == null) || (_string.trim().length() == 0));
	}

	/**
	 * 字符串显示处理函数：若为空对象，则返回指定的字符串
	 * 
	 * @see showNull( String _sValue, String _sReplaceIfNull )
	 */
	public static String showObjNull(Object p_sValue) {
		return showObjNull(p_sValue, "");
	}

	/**
	 * 字符串显示处理函数：若为空对象，则返回指定的字符串
	 * 
	 * @param _sValue
	 *            指定的字符串
	 * @param _sReplaceIfNull
	 *            当_sValue==null时的替换显示字符串；可选参数，缺省值为空字符串（""）
	 * @return 处理后的字符串
	 */
	public static String showObjNull(Object _sValue, String _sReplaceIfNull) {
		if (_sValue == null)
			return _sReplaceIfNull;
		return _sValue.toString();
	}

	/**
	 * 字符串显示处理函数：若为空对象，则返回指定的字符串
	 * 
	 * @see showNull( String _sValue, String _sReplaceIfNull )
	 */
	public static String showNull(String p_sValue) {
		return showNull(p_sValue, "");
	}

	/**
	 * 字符串显示处理函数：若为空对象，则返回指定的字符串
	 * 
	 * @param _sValue
	 *            指定的字符串
	 * @param _sReplaceIfNull
	 *            当_sValue==null时的替换显示字符串；可选参数，缺省值为空字符串（""）
	 * @return 处理后的字符串
	 */
	public static String showNull(String _sValue, String _sReplaceIfNull) {
		return (_sValue == null ? _sReplaceIfNull : _sValue);
	}

	/**
	 * 字符串显示处理函数：若为空对象，则返回指定的字符串
	 * 
	 * @see showNull( String _sValue, String _sReplaceIfNull )
	 */
	public static String showEmpty(String p_sValue) {
		return showEmpty(p_sValue, "");
	}

	/**
	 * 字符串显示处理函数：若为空对象或者空串，则返回指定的字符串
	 * 
	 * @param _sValue
	 *            指定的字符串
	 * @param _sReplaceIfEmpty
	 *            当CMyStirng.isEmpty(_sValue)时的替换显示字符串；可选参数，缺省值为空字符串（""）
	 * @return 处理后的字符串
	 */
	public static String showEmpty(String _sValue, String _sReplaceIfEmpty) {
		return CMyString.isEmpty(_sValue) ? _sReplaceIfEmpty : _sValue;
	}

	/**
	 * 扩展字符串长度；若长度不足，则是用指定的字符串填充
	 * 
	 * @param _string
	 *            要扩展的字符串
	 * @param _length
	 *            扩展后的字符串长度。
	 * @param _chrFill
	 *            扩展时，用于填充的字符。
	 * @param _bFillOnLeft
	 *            扩展时，是否为左填充（扩展）；否则，为右填充
	 * @return 长度扩展后的字符串
	 */
	public static String expandStr(String _string, int _length, char _chrFill, boolean _bFillOnLeft) {
		int nLen = _string.length();
		if (_length <= nLen)
			return _string; // 长度已够

		// else,扩展字符串长度
		String sRet = _string;
		for (int i = 0; i < _length - nLen; i++) {
			sRet = (_bFillOnLeft ? _chrFill + sRet : sRet + _chrFill); // 填充
		}
		return sRet;
	}

	/**
	 * 设置字符串最后一位为指定的字符
	 * 
	 * @param _string
	 *            指定的字符串
	 * @param _chrEnd
	 *            指定字符，若字符串最后一位不是该字符，则在字符串尾部追加该字符
	 * @return 处理后的字符串 如果<code>isEmpty(_string)</code>返回true,则原样返回
	 * @see #isEmpty(String)
	 */
	public static String setStrEndWith(String _str, char _charEnd) {
		if (isEmpty(_str) || _str.endsWith(String.valueOf(_charEnd))) {
			return _str;
		}

		return _str + _charEnd;
	}

	/**
	 * 构造指定长度的空格字符串
	 * 
	 * @param _length
	 *            指定长度
	 * @return 指定长度的空格字符串
	 */
	public static String makeBlanks(int _length) {
		if (_length < 1)
			return "";
		StringBuffer buffer = new StringBuffer(_length);
		for (int i = 0; i < _length; i++) {
			buffer.append(' ');
		}
		return buffer.toString();
	}

	// ==========================================================================
	// ===
	// 字符串替换

	/**
	 * 字符串替换函数：用于将指定字符串中指定的字符串替换为新的字符串。
	 * 
	 * @param _strSrc
	 *            源字符串。
	 * @param _strOld
	 *            被替换的旧字符串
	 * @param _strNew
	 *            用来替换旧字符串的新字符串
	 * @return 替换处理后的字符串
	 */
	public static String replaceStr(String _strSrc, String _strOld, String _strNew) {
		if (_strSrc == null || _strNew == null || _strOld == null)
			return _strSrc;

		// 提取源字符串对应的字符数组
		char[] srcBuff = _strSrc.toCharArray();
		int nSrcLen = srcBuff.length;
		if (nSrcLen == 0)
			return "";

		// 提取旧字符串对应的字符数组
		char[] oldStrBuff = _strOld.toCharArray();
		int nOldStrLen = oldStrBuff.length;
		if (nOldStrLen == 0 || nOldStrLen > nSrcLen)
			return _strSrc;

		StringBuffer retBuff = new StringBuffer((nSrcLen * (1 + _strNew.length() / nOldStrLen)));

		int i, j, nSkipTo;
		boolean bIsFound = false;

		i = 0;
		while (i < nSrcLen) {
			bIsFound = false;

			// 判断是否遇到要找的字符串
			if (srcBuff[i] == oldStrBuff[0]) {
				for (j = 1; j < nOldStrLen; j++) {
					if (i + j >= nSrcLen)
						break;
					if (srcBuff[i + j] != oldStrBuff[j])
						break;
				}
				bIsFound = (j == nOldStrLen);
			}

			// 若找到则替换，否则跳过
			if (bIsFound) { // 找到
				retBuff.append(_strNew);
				i += nOldStrLen;
			} else { // 没有找到
				if (i + nOldStrLen >= nSrcLen) {
					nSkipTo = nSrcLen - 1;
				} else {
					nSkipTo = i;
				}
				for (; i <= nSkipTo; i++) {
					retBuff.append(srcBuff[i]);
				}
			}
		}// end while
		srcBuff = null;
		oldStrBuff = null;
		return retBuff.toString();
	}

	/**
	 * 字符串替换函数：用于将指定字符串中指定的字符串替换为新的字符串。
	 * 
	 * @param _strSrc
	 *            源字符串。
	 * @param _strOld
	 *            被替换的旧字符串
	 * @param _strNew
	 *            用来替换旧字符串的新字符串
	 * @return 替换处理后的字符串
	 */
	public static String replaceStr(StringBuffer _strSrc, String _strOld, String _strNew) {
		if (_strSrc == null)
			return null;

		// 提取源字符串对应的字符数组
		int nSrcLen = _strSrc.length();
		if (nSrcLen == 0)
			return "";

		// 提取旧字符串对应的字符数组
		char[] oldStrBuff = _strOld.toCharArray();
		int nOldStrLen = oldStrBuff.length;
		if (nOldStrLen == 0 || nOldStrLen > nSrcLen)
			return _strSrc.toString();

		StringBuffer retBuff = new StringBuffer((nSrcLen * (1 + _strNew.length() / nOldStrLen)));

		int i, j, nSkipTo;
		boolean bIsFound = false;

		i = 0;
		while (i < nSrcLen) {
			bIsFound = false;

			// 判断是否遇到要找的字符串
			if (_strSrc.charAt(i) == oldStrBuff[0]) {
				for (j = 1; j < nOldStrLen; j++) {
					if (i + j >= nSrcLen)
						break;
					if (_strSrc.charAt(i + j) != oldStrBuff[j])
						break;
				}
				bIsFound = (j == nOldStrLen);
			}

			// 若找到则替换，否则跳过
			if (bIsFound) { // 找到
				retBuff.append(_strNew);
				i += nOldStrLen;
			} else { // 没有找到
				if (i + nOldStrLen >= nSrcLen) {
					nSkipTo = nSrcLen - 1;
				} else {
					nSkipTo = i;
				}
				for (; i <= nSkipTo; i++) {
					retBuff.append(_strSrc.charAt(i));
				}
			}
		}// end while
		oldStrBuff = null;
		return retBuff.toString();
	}

	// ==========================================================================
	// ====
	// 字符编码处理函数

	/**
	 * 字符串编码转换函数，用于将指定编码的字符串转换为标准（Unicode）字符串
	 * 
	 * @see getStr( String _strSrc, String _encoding )
	 */
	public static String getStr(String _strSrc) {
		return getStr(_strSrc, ENCODING_DEFAULT);
	}

	/**
	 * 字符转换函数，处理中文问题
	 * 
	 * @param _strSrc
	 *            源字符串
	 * @param _bPostMethod
	 *            提交数据的方式（Get方式采用GET_ENCODING_DEFAULT字符集，
	 *            Post方式采用ENCODING_DEFAULT字符集 ）
	 * @return
	 */
	public static String getStr(String _strSrc, boolean _bPostMethod) {
		return getStr(_strSrc, (_bPostMethod ? ENCODING_DEFAULT : GET_ENCODING_DEFAULT));
	}

	/**
	 * 字符串编码转换函数，用于将指定编码的字符串转换为标准（Unicode）字符串
	 * <p>
	 * Purpose: 转换字符串内码，用于解决中文显示问题
	 * </p>
	 * <p>
	 * Usage： 在页面切换时，获取并显示中文字符串参数时可用。
	 * </p>
	 * 
	 * @param _strSrc
	 *            需要转换的字符串
	 * @param _encoding
	 *            指定字符串（_strSrc）的编码方式；可选参数，缺省值为ENCODING_DEFAULT
	 * @return
	 */
	public static String getStr(String _strSrc, String _encoding) {
		if (_encoding == null || _encoding.length() == 0)
			return _strSrc;

		try {
			byte[] byteStr = new byte[_strSrc.length()];
			char[] charStr = _strSrc.toCharArray();
			for (int i = byteStr.length - 1; i >= 0; i--) {
				byteStr[i] = (byte) charStr[i];
			}
			/*
			 * 如上的实现和下面的方法调用的实现是等价的，同样地丢弃了16位字符的高8位。 _strSrc.getBytes(0,
			 * _strSrc.length(), byteStr, 0);
			 * 之所以这样写，而不是String类型的方法调用（如上），是要明确这种丢弃高8位行为。
			 */
			return new String(byteStr, _encoding);
			// return new String(_strSrc.getBytes(), _encoding);
			// commented by frank:2002-09-13
			// byte[] bytes = _strSrc.getBytes( _encoding ); //why@2002-04-22
			// 使用指定字符编码
			// return new String( bytes );
		} catch (Exception ex) {
			return _strSrc; // 出错时，返回源字符串 //why@2002-04-27：不返回"null"
		}
	}// END: getStr()

	/**
	 * 将指定的字符串转化为ISO-8859-1编码的字符串
	 * 
	 * @param _strSrc
	 *            指定的源字符串
	 * @return 转化后的字符串
	 */
	public static String toISO_8859(String _strSrc) {
		if (_strSrc == null)
			return null;

		try {
			return new String(_strSrc.getBytes(), "ISO-8859-1");
		} catch (Exception ex) {
			return _strSrc;
		}
	}

	/**
	 * 将指定的字符串转化为ISO-8859-1编码的字符串
	 * 
	 * @param _strSrc
	 *            指定的源字符串
	 * @return 转化后的字符串
	 * @deprecated 含义模糊，已经使用toISO_8859替换
	 */
	public static String toUnicode(String _strSrc) {
		return toISO_8859(_strSrc);
	}

	// why@2002-04-27 come from java.util.ZipOutputSteam
	/**
	 * 提取字符串UTF8编码的字节流
	 * <p>
	 * 说明：等价于 <code>_string.getBytes("UTF8")</code>
	 * </p>
	 * 
	 * @param _string
	 *            源字符串
	 * @return UTF8编码的字节数组
	 */
	public static byte[] getUTF8Bytes(String _string) {
		char[] c = _string.toCharArray();
		int len = c.length;

		// Count the number of encoded bytes...
		int count = 0;
		for (int i = 0; i < len; i++) {
			int ch = c[i];
			if (ch <= 0x7f) {
				count++;
			} else if (ch <= 0x7ff) {
				count += 2;
			} else {
				count += 3;
			}
		}

		// Now return the encoded bytes...
		byte[] b = new byte[count];
		int off = 0;
		for (int i = 0; i < len; i++) {
			int ch = c[i];
			if (ch <= 0x7f) {
				b[off++] = (byte) ch;
			} else if (ch <= 0x7ff) {
				b[off++] = (byte) ((ch >> 6) | 0xc0);
				b[off++] = (byte) ((ch & 0x3f) | 0x80);
			} else {
				b[off++] = (byte) ((ch >> 12) | 0xe0);
				b[off++] = (byte) (((ch >> 6) & 0x3f) | 0x80);
				b[off++] = (byte) ((ch & 0x3f) | 0x80);
			}
		}
		return b;
	}

	// why@2002-04-27 come from java.util.ZipInputStream
	public static String getUTF8String(byte[] b) {
		return getUTF8String(b, 0, b.length);
	}

	/**
	 * 从指定的字节数组中提取UTF8编码的字符串
	 * <p>
	 * 说明：函数等价于 <code> new String(b,"UTF8") </code>
	 * </p>
	 * 
	 * @param b
	 *            指定的字节数组（UTF8编码）
	 * @param off
	 *            开始提取的字节起始位置；可选参数，缺省值为0；
	 * @param len
	 *            提取的字节数；可选择书，缺省值为全部。
	 * @return 提取后得到的字符串。
	 */
	public static String getUTF8String(byte[] b, int off, int len) {
		// First, count the number of characters in the sequence
		int count = 0;
		int max = off + len;
		int i = off;
		while (i < max) {
			int c = b[i++] & 0xff;
			switch (c >> 4) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				// 0xxxxxxx
				count++;
				break;
			case 12:
			case 13:
				// 110xxxxx 10xxxxxx
				if ((b[i++] & 0xc0) != 0x80) {
					throw new IllegalArgumentException();
				}
				count++;
				break;
			case 14:
				// 1110xxxx 10xxxxxx 10xxxxxx
				if (((b[i++] & 0xc0) != 0x80) || ((b[i++] & 0xc0) != 0x80)) {
					throw new IllegalArgumentException();
				}
				count++;
				break;
			default:
				// 10xxxxxx, 1111xxxx
				throw new IllegalArgumentException();
			}
		}
		if (i != max) {
			throw new IllegalArgumentException();
		}
		// Now decode the characters...
		char[] cs = new char[count];
		i = 0;
		while (off < max) {
			int c = b[off++] & 0xff;
			switch (c >> 4) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				// 0xxxxxxx
				cs[i++] = (char) c;
				break;
			case 12:
			case 13:
				// 110xxxxx 10xxxxxx
				cs[i++] = (char) (((c & 0x1f) << 6) | (b[off++] & 0x3f));
				break;
			case 14:
				// 1110xxxx 10xxxxxx 10xxxxxx
				int t = (b[off++] & 0x3f) << 6;
				cs[i++] = (char) (((c & 0x0f) << 12) | t | (b[off++] & 0x3f));
				break;
			default:
				// 10xxxxxx, 1111xxxx
				throw new IllegalArgumentException();
			}
		}
		return new String(cs, 0, count);
	}

	// ==========================================================================
	// ====
	// 字符串显示处理函数

	/**
	 * 将字节数据输出为16进制数表示的字符串
	 * 
	 * @see byteToHexString( byte[] _bytes, char _delim )
	 */
	public static String byteToHexString(byte[] _bytes) {
		return byteToHexString(_bytes, ',');
	}

	/**
	 * 将字节数据输出为16进制无符号数表示的字符串
	 * 
	 * @param _bytes
	 *            字节数组
	 * @param _delim
	 *            字节数据显示时，字节之间的分隔符；可选参数，缺省值为','
	 * @return 16进制无符号数表示的字节数据
	 */
	public static String byteToHexString(byte[] _bytes, char _delim) {
		String sRet = "";
		for (int i = 0; i < _bytes.length; i++) {
			if (i > 0) {
				sRet += _delim;
			}
			sRet += Integer.toHexString(_bytes[i]);
		}
		return sRet;
	}

	/**
	 * 将字节数据输出为指定进制数表示的字符串（注意：负数带有负号）
	 * 
	 * @param _bytes
	 *            字节数组
	 * @param _delim
	 *            字节数据显示时，字节之间的分隔符；可选参数，缺省值为','
	 * @param _radix
	 *            进制数（如16进制）
	 * @return 指定进制数表示的字节数据（负数带由负号）
	 */
	public static String byteToString(byte[] _bytes, char _delim, int _radix) {
		String sRet = "";
		for (int i = 0; i < _bytes.length; i++) {
			if (i > 0) {
				sRet += _delim;
			}
			sRet += Integer.toString(_bytes[i], _radix);
		}
		return sRet;
	}

	/**
	 * 用于在Html中显示文本内容
	 * 
	 * @see <code>transDisplay( String _sContent, boolean _bChangeBlank )</code>
	 */
	public static String transDisplay(String _sContent) {
		return transDisplay(_sContent, true);
	}

	/**
	 * 用于在Html中显示文本内容。将空格等转化为html标记。
	 * <p>
	 * 说明：处理折行时，若使用 <code>style="WORD_WRAP:keepall"</code> ，则不能将空格转换为
	 * <code>&amp;nbsp;</code>
	 * </p>
	 * 
	 * @param _sContent
	 *            要显示的内容
	 * @param _bChangeBlank
	 *            是否转换空格符；可选参数，默认值为true.
	 * @return 转化后的Html文本
	 */
	public static String transDisplay(String _sContent, boolean _bChangeBlank) {
		if (_sContent == null)
			return "";

		char[] srcBuff = _sContent.toCharArray();
		int nSrcLen = srcBuff.length;

		StringBuffer retBuff = new StringBuffer(nSrcLen * 2);

		int i;
		char cTemp;
		for (i = 0; i < nSrcLen; i++) {
			cTemp = srcBuff[i];
			switch (cTemp) {
			case ' ':
				retBuff.append(_bChangeBlank ? "&nbsp;" : " ");
				break;
			case '<':
				retBuff.append("&lt;");
				break;
			case '>':
				retBuff.append("&gt;");
				break;
			case '\n':
				// 再处理段首和段尾的时候处理
				if (_bChangeBlank)
					retBuff.append("<br/>");
				else
					retBuff.append(cTemp);
				break;
			case '"':
				retBuff.append("&quot;");
				break;
			case '&': // why: 2002-3-19
				// caohui@0515
				// 处理unicode代码
				/*
				 * boolean bUnicode = false; for (int j = (i + 1); j < nSrcLen
				 * && !bUnicode; j++) { cTemp = srcBuff[j]; if (cTemp == '#' ||
				 * cTemp == ';') { retBuff.append("&"); bUnicode = true; } } if
				 * (!bUnicode) retBuff.append("&amp;");
				 */
				retBuff.append("&amp;");
				break;
			case 9: // Tab
				retBuff.append(_bChangeBlank ? "&nbsp;&nbsp;&nbsp;&nbsp;" : "    ");
				break;

			default:
				retBuff.append(cTemp);
			}// case
		}

		// 如果替换了空格，直接返回，否则还需要
		if (_bChangeBlank)
			return retBuff.toString();

		// 需要特殊处理段首和段尾
		return replaceParasStartEndSpaces(retBuff.toString());

	}// END:transDisplay

	// 将指定文本内容，格式化为bbs风格的Html文本字符串。
	// 参数：p_strContent：p_sQuoteColor：
	// _bChangeBlank
	//
	public static String transDisplay_bbs(String _sContent, String p_sQuoteColor) {
		return transDisplay_bbs(_sContent, p_sQuoteColor, true);
	}

	/**
	 * 将指定文本内容，格式化为bbs风格的Html文本字符串。
	 * <p>
	 * 说明：[1]处理折行时，若使用 style="WORD_WRAP:keepall"，则不能将空格转换为
	 * <code>&amp;nbsp;</code>
	 * </p>
	 * <p>
	 * [2]该函数格式化时，如果遇到某一行以":"开始，则认为是引用语(quote)，
	 * </p>
	 * <p
	 * 并用参数p_sQuoteColor指定的颜色显示。
	 * </p>
	 * 
	 * @param _sContent
	 *            文本内容；
	 * @param p_sQuoteColor
	 *            引用语的显示颜色。
	 * @param _bChangeBlank
	 *            是否转换空格符，可省略，默认值为true
	 * @return 转化后的Html文本
	 */
	public static String transDisplay_bbs(String _sContent, String p_sQuoteColor, boolean _bChangeBlank) {
		if (_sContent == null)
			return "";

		int i;
		char cTemp;
		boolean bIsQuote = false; // 是否是引用语
		boolean bIsNewLine = true; // 是否是新的一行

		char[] srcBuff = _sContent.toCharArray();
		int nSrcLen = srcBuff.length;

		StringBuffer retBuff = new StringBuffer((int) (nSrcLen * 1.8));

		for (i = 0; i < nSrcLen; i++) {
			cTemp = srcBuff[i];
			switch (cTemp) {
			case ':': {
				if (bIsNewLine) {
					bIsQuote = true;
					retBuff.append("<font color=" + p_sQuoteColor + ">:");
				} else {
					retBuff.append(":");
				}
				bIsNewLine = false;
				break;
			}

			case ' ': {
				retBuff.append(_bChangeBlank ? "&nbsp;" : " ");
				bIsNewLine = false;
				break;
			}
			case '<': {
				retBuff.append("&lt;");
				bIsNewLine = false;
				break;
			}
			case '>': {
				retBuff.append("&gt;");
				bIsNewLine = false;
				break;
			}
			case '"': {
				retBuff.append("&quot;");
				bIsNewLine = false;
				break;
			}
			case '&': { // why: 2002-3-19
				retBuff.append("&amp;");
				bIsNewLine = false;
				break;
			}

			case 9: {// Tab
				retBuff.append(_bChangeBlank ? "&nbsp;&nbsp;&nbsp;&nbsp;" : "    ");
				bIsNewLine = false;
				break;
			}

			case '\n': {
				if (bIsQuote) {
					bIsQuote = false;
					retBuff.append("</font>");
				}
				retBuff.append("<br/>");
				bIsNewLine = true;
				break;
			}
			default: {
				retBuff.append(cTemp);
				bIsNewLine = false;
			}
			}// end case
		}// end for
		if (bIsQuote) {
			retBuff.append("</font>");
		}
		return retBuff.toString();
	}// END: transDisplay_bbs

	/**
	 * javascript显示处理，用于处理javascript中的文本字符串显示
	 * 
	 * @param _sContent
	 *            javascript文本
	 * @return 处理后的javascript文本
	 */
	public static String transJsDisplay(String _sContent) {
		if (_sContent == null)
			return "";

		char[] srcBuff = _sContent.toCharArray();
		int nSrcLen = srcBuff.length;

		StringBuffer retBuff = new StringBuffer((int) (nSrcLen * 1.5));

		int i;
		char cTemp;
		for (i = 0; i < nSrcLen; i++) {
			cTemp = srcBuff[i];
			switch (cTemp) {
			case '<':
				retBuff.append("&lt;");
				break;
			case '>':
				retBuff.append("&gt;");
				break;
			case 34: // "
				retBuff.append("&quot;");
				break;
			default:
				retBuff.append(cTemp);
			}// case
		}
		return retBuff.toString();
	}// END:transJsDisplay

	/**
	 * 字符串的掩码显示：用指定的掩码构造与指定字符串相同长度的字符串
	 * <p>
	 * 用于：密码显示等需要掩码处理的场合
	 * </p>
	 * 
	 * @param _strSrc
	 *            源字符串
	 * @param p_chrMark
	 *            指定的掩码
	 * @return 用掩码处理后的字符串
	 */
	public static String transDisplayMark(String _strSrc, char p_chrMark) {
		if (_strSrc == null)
			return "";

		// else
		char[] buff = new char[_strSrc.length()];
		for (int i = 0; i < buff.length; i++) {
			buff[i] = p_chrMark;
		}
		return new String(buff);
	}

	// ==========================================================================
	// ====
	// 字符串过滤函数

	/**
	 * SQL语句特殊字符过滤处理函数
	 * <p>
	 * 用于：构造SQL语句时，填充字符串参数时使用
	 * </p>
	 * <p>
	 * 如：
	 * <code>String strSQL = "select * from tbName where Name='"+CMyString.filterForSQL("a'bc")+"'" </code>
	 * </p>
	 * <p>
	 * 说明：需要处理的特殊字符及对应转化规则：如： <code> ' ---&gt;''</code>
	 * </p>
	 * <p>
	 * 不允许使用的特殊字符： <code> !@#$%^&*()+|-=\\;:\",./&lt;&gt;? </code>
	 * </p>
	 * 
	 * @param _sContent
	 *            需要处理的字符串
	 * @return 过滤处理后的字符串
	 */
	public static String filterForSQL(String _sContent) {
		if (_sContent == null)
			return "";

		int nLen = _sContent.length();
		if (nLen == 0)
			return "";

		char[] srcBuff = _sContent.toCharArray();
		StringBuffer retBuff = new StringBuffer((int) (nLen * 1.5));

		// caohui@0508 各个应用都需要不去除特殊字符，特修改
		for (int i = 0; i < nLen; i++) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case '\'': {
				retBuff.append("''");
				break;
			}
			case ';':// caohui@0516为了查询Unicode字符
				boolean bSkip = false;
				for (int j = (i + 1); j < nLen && !bSkip; j++) {
					char cTemp2 = srcBuff[j];
					if (cTemp2 == ' ')
						continue;
					if (cTemp2 == '&')
						retBuff.append(';');
					bSkip = true;
				}
				if (!bSkip)
					retBuff.append(';');
				break;
			// case '[': //niuzhao@2005-08-11 处理SQL Server中的通配符 []
			// retBuff.append("[[]");
			// break;
			// case '_': //niuzhao@2005-08-11 处理SQL Server中的通配符 _
			// retBuff.append("[_]");
			// break;
			default:
				retBuff.append(cTemp);
			}// case
		}// end for
		/*
		 * for( int i=0; i <nLen; i++ ){ char cTemp = srcBuff[i]; switch( cTemp
		 * ){ case '\'':{ retBuff.append( "''" ); break; } case '!': case '@':
		 * case '#': case '$': case '%': case '^': case '&': case '*': case '(':
		 * case ')': case '+': case '|': case '-': case '=': case '\\': case
		 * ';': case ':': case '\"': case ',': case '.': case '/': case ' <':
		 * case '>': case '?': break; //skip default : retBuff.append( cTemp );
		 * }//case }//end for
		 */

		return retBuff.toString();
	}

	public static String filterForSQL2(String _sContent) {
		if (isEmpty(_sContent))
			return _sContent;
		return _sContent.replaceAll("(?i)([;\n\r])", "");
	}

	public static String filterForXsltValue(String _sAttrValue) {
		if (_sAttrValue == null)
			return "";
		_sAttrValue = _sAttrValue.replaceAll("\\{", "{{");
		_sAttrValue = _sAttrValue.replaceAll("\\}", "}}");
		return _sAttrValue;
	}

	/**
	 * XML文本过滤处理函数：将 <code> & &lt; &gt;\ </code> 等特殊字符做转化处理
	 * 
	 * @param _sContent
	 *            指定的XML文本内容
	 * @return 处理后的文本内容
	 */
	public static String filterForXML(String _sContent) {
		if (_sContent == null)
			return "";

		char[] srcBuff = _sContent.toCharArray();
		int nLen = srcBuff.length;
		if (nLen == 0)
			return "";

		StringBuffer retBuff = new StringBuffer((int) (nLen * 1.8));

		for (int i = 0; i < nLen; i++) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case '&': // 转化：& -->&amp;
				retBuff.append("&amp;");
				break;
			case '<': // 转化：< --> &lt;
				retBuff.append("&lt;");
				break;
			case '>': // 转化：> --> &gt;
				retBuff.append("&gt;");
				break;
			case '\"': // 转化：" --> &quot;
				retBuff.append("&quot;");
				break;
			case '\'': // 转化：' --> &apos;
				retBuff.append("&apos;");
				break;
			default:
				retBuff.append(cTemp);
			}// case
		}// end for

		return retBuff.toString();
	}

	public static String filterBlankLine(String _sContent, boolean _bTrimLine) {
		StringBuffer sbResult = new StringBuffer(_sContent.length());
		int nStartIndex = 0, nLen = _sContent.length();
		// int nTemp = 0; // 记录每一行开始的位置;
		// 去除前面的空格，以及空行
		for (; nStartIndex < nLen; nStartIndex++) {
			char c = _sContent.charAt(nStartIndex);
			if (c == ' ' || c == '　' || c == 9)
				continue;
			// 空格或者TAB后面是回车，表示是空行
			if (c == '\r' || c == '\n') {
				// nTemp = nStartIndex + 1;
				continue;
			}

			// 首行的空格都忽略
			break;

			// // 空格或者TAB后面不是回车，表示不是空行
			// // 如果做Trim，直接跳出，从这个字符开始处理
			// if (_bTrimLine) {
			// break;
			// }
			// // 如果不做Trim需要回退
			// else {
			// nStartIndex = nTemp;
			// break;
			// }
		}

		// 去除中间的空行
		int nCharLineCount = 0;// 一行里面的字数
		int nBlankCountInLine = 0;// 一个TAB是四个空格
		for (; nStartIndex < nLen; nStartIndex++) {
			char c = _sContent.charAt(nStartIndex);
			// 忽略每一行开始的空行
			if (nCharLineCount == 0 && (c == ' ' || c == '　' || c == 9)) {
				nBlankCountInLine++;
				if (c == 9)
					nBlankCountInLine += 3;
				if (c == '　')
					nBlankCountInLine += 1;
				continue;
			}
			// 如果是空回车，忽略
			if (c == '\n' || c == '\r') {
				// 空行
				if (nCharLineCount == 0) {
					nBlankCountInLine = 0;
				}
				// 一行结束，清零
				else {
					nCharLineCount = 0;
					sbResult.append(c);
				}
				continue;
			}
			// 如果不做Trim，需要将之前忽略的空格追加
			if (!_bTrimLine && nCharLineCount == 0) {
				for (int i = 0; i < nBlankCountInLine; i++)
					sbResult.append(' ');
			}

			// 其他字符，直接追加，并且修改计数器
			nCharLineCount++;

			sbResult.append(c);
		}

		return sbResult.toString();
	}

	/**
	 * HTML元素value值过滤处理函数：将 <code> & &lt; &gt;\ </code> 等特殊字符作转化处理
	 * 
	 * @sample <code>
	 *    &lt;input type="text" name="Name" value="<%=CMyString.filterForHTMLValue(sContent)%>"&gt;
	 * </code>
	 * @param _sContent
	 *            指定的文本内容
	 * @return 处理后的文本内容
	 */
	public static String filterForHTMLValue(String _sContent) {
		if (_sContent == null)
			return "";

		// _sContent = replaceStr(_sContent,"</script>","<//script>");
		// _sContent = replaceStr(_sContent,"</SCRIPT>","<//SCRIPT>");

		char[] srcBuff = _sContent.toCharArray();
		int nLen = srcBuff.length;
		if (nLen == 0)
			return "";

		StringBuffer retBuff = new StringBuffer((int) (nLen * 1.8));

		for (int i = 0; i < nLen; i++) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case '&': // 转化：& -->&amp;why: 2002-3-19
				// caohui@0515
				// 处理unicode代码
				if ((i + 1) < nLen) {
					cTemp = srcBuff[i + 1];
					if (cTemp == '#')
						retBuff.append("&");
					else
						retBuff.append("&amp;");
				} else
					retBuff.append("&amp;");
				break;
			case '<': // 转化：< --> &lt;
				retBuff.append("&lt;");
				break;
			case '>': // 转化：> --> &gt;
				retBuff.append("&gt;");
				break;
			case '\"': // 转化：" --> &quot;
				retBuff.append("&quot;");
				break;
			default:
				retBuff.append(cTemp);
			}// case
		}// end for

		return retBuff.toString();
	}

	/**
	 * 将_sContent做filterForHTMLValue的逆处理
	 * 
	 * @param _sContent
	 * @return
	 */
	public static String unfilterForHTMLValue(String _sContent) {
		if (CMyString.isEmpty(_sContent)) {
			return "";
		}
		String[][] mapping = { { "&amp;", "&" }, { "&lt;", "<" }, { "&gt;", ">" }, { "&quot;", "\"" } };
		StringBuffer sbResult = new StringBuffer(_sContent.length());
		Pattern pattern = Pattern.compile("(?im)(&[^;]+;)");
		Matcher matcher = pattern.matcher(_sContent);
		int nStartIndex = 0;
		int nEndIndex = 0;
		String sTarget;
		while (matcher.find()) {
			nStartIndex = matcher.start();
			sbResult.append(_sContent.substring(nEndIndex, nStartIndex));
			nEndIndex = matcher.end();
			sTarget = matcher.group(1).toLowerCase();

			for (int i = 0; i < mapping.length; i++) {
				if (mapping[i][0].equals(sTarget)) {
					sbResult.append(mapping[i][1]);
					break;
				}
			}
		}
		if (nEndIndex < _sContent.length()) {
			sbResult.append(_sContent.substring(nEndIndex));
		}
		return sbResult.toString();
	}

	/**
	 * URL过滤处理函数：将 <code> # & </code> 等特殊字符作转化处理
	 * 
	 * @param _sContent
	 *            指定的URL内容
	 * @return 处理后的字符串
	 */
	public static String filterForUrl(String _sContent) {
		if (_sContent == null)
			return "";

		char[] srcBuff = _sContent.toCharArray();
		int nLen = srcBuff.length;
		if (nLen == 0)
			return "";

		StringBuffer retBuff = new StringBuffer((int) (nLen * 1.8));

		for (int i = 0; i < nLen; i++) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case '%':
				retBuff.append("%25");
				break;
			case '?':
				retBuff.append("%3F");
				break;
			case '#': // 转化：# --> %23
				retBuff.append("%23");
				break;
			case '&': // 转化：& --> %26
				retBuff.append("%26");
				break;
			case ' ': // 转化：空格 --> %20
				retBuff.append("%20");
				break;
			default:
				retBuff.append(cTemp);
			}// case
		}// end for

		return retBuff.toString();
	}

	// why:2002-04-02 修正转换错误
	/**
	 * JavaScript过滤处理函数：将指定文本中的 <code> " \ \r \n</code> 等特殊字符做转化处理
	 * 
	 * @sample <code>
	 *      <br>&lt;script language="javascript"&gt;
	 *      <br>     document.getElementById("id_txtName").value = "<%=CMyString.filterForJs(sValue)%>";
	 *      <br>&lt;/script&gt;
	 * </code>
	 * @param _sContent
	 *            指定的javascript文本
	 * @return 转化处理后的字符串
	 */
	public static String filterForJs(String _sContent) {
		if (_sContent == null)
			return "";

		char[] srcBuff = _sContent.toCharArray();
		int nLen = srcBuff.length;
		if (nLen == 0)
			return "";

		StringBuffer retBuff = new StringBuffer((int) (nLen * 1.8));

		for (int i = 0; i < nLen; i++) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case '"': // 转化：" --> \"
				retBuff.append("\\\"");
				break;
			case '\'': // 转化：' --> \'
				retBuff.append("\\\'");
				break;
			case '\\': // 转化：\ --> \\
				retBuff.append("\\\\");
				break;
			case '\n':
				retBuff.append("\\n");
				break;
			case '\r':
				retBuff.append("\\r");
				break;
			case '\f':
				retBuff.append("\\f");
				break;
			case '\t':
				retBuff.append("\\t");
				break;
			case '/':
				retBuff.append("\\/");
				break;
			default:
				retBuff.append(cTemp);
			}// case
		}// end for

		return retBuff.toString();
	}

	/**
	 * 可能有置标内容输出到java代码中作为参数。在输出之前需要排除特殊字符。
	 * 
	 * @param _sContent置标输出之前的内容
	 * @return 转化处理后的字符串
	 */
	public static String filterForJava(String _sContent) {
		if (_sContent == null)
			return "";

		char[] srcBuff = _sContent.toCharArray();
		int nLen = srcBuff.length;
		if (nLen == 0)
			return "";

		StringBuffer retBuff = new StringBuffer((int) (nLen * 1.8));

		for (int i = 0; i < nLen; i++) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case '\"':
				retBuff.append("\\\"");
				break;
			case '\\':
				retBuff.append("\\\\");
				break;
			default:
				retBuff.append(cTemp);
			}
		}
		return retBuff.toString();
	}

	// ==========================================================================
	// ====
	// 数字转化为字符串

	/**
	 * 将指定整型值转化为字符串
	 * 
	 * @see numberToStr( int _nValue, int _length, char _chrFill )
	 */
	public static String numberToStr(int _nValue) {
		return numberToStr(_nValue, 0);
	}

	/**
	 * 将指定整型值转化为字符串
	 * 
	 * @see numberToStr( int _nValue, int _length, char _chrFill )
	 */
	public static String numberToStr(int _nValue, int _length) {
		return numberToStr(_nValue, _length, '0');
	}

	/**
	 * 将指定整型值转化为字符串
	 * 
	 * @param _nValue
	 *            指定整数
	 * @param _length
	 *            转化后字符串长度；若实际长度小于该长度，则使用_chrFill左填充; 可选参数，缺省值0，表示按照实际长度，不扩展。
	 * @param _chrFill
	 *            当整数的实际位数小于指定长度时的填充字符；可选参数，缺省值'0'
	 * @return 转化后的字符串
	 */
	public static String numberToStr(int _nValue, int _length, char _chrFill) {
		String sValue = String.valueOf(_nValue);
		return expandStr(sValue, _length, _chrFill, true);
	}

	// 重载：使用long型数值
	/**
	 * 将指定长整数转化为字符串
	 * 
	 * @see <code> numberToStr( long _lValue, int _length, char _chrFill ) </code>
	 */
	public static String numberToStr(long _lValue) {
		return numberToStr(_lValue, 0);
	}

	/**
	 * 将指定长整数转化为字符串
	 * 
	 * @see <code> numberToStr( long _lValue, int _length, char _chrFill ) </code>
	 */
	public static String numberToStr(long _lValue, int _length) {
		return numberToStr(_lValue, _length, '0');
	}

	/**
	 * 将指定长整数转化为字符串
	 * 
	 * @param _lValue
	 *            指定长整数
	 * @param _length
	 *            转化后字符串长度；若实际长度小于该长度，则使用_chrFill左填充; 可选参数，缺省值0，表示按照实际长度，不扩展。
	 * @param _chrFill
	 *            当整数的实际位数小于指定长度时的填充字符；可选参数，缺省值'0'
	 * @return 转化后的字符串
	 */
	public static String numberToStr(long _lValue, int _length, char _chrFill) {
		String sValue = String.valueOf(_lValue);
		return expandStr(sValue, _length, _chrFill, true);
	}

	// ==========================================================================
	// ====
	// 其他字符串处理函数

	/**
	 * 字符串翻转：对于给定的字符串，按相反的顺序输出
	 * 
	 * @param _strSrc
	 *            指定的字符串
	 * @return 翻转后的字符串
	 */
	public static String circleStr(String _strSrc) {
		if (_strSrc == null)
			return null; // 错误保护

		String sResult = "";
		int nLength = _strSrc.length();
		for (int i = nLength - 1; i >= 0; i--) {
			sResult = sResult + _strSrc.charAt(i);
		}// end for
		return sResult;
	}

	/**
	 * 判断指定的字符是不是汉字，目前是通过判断其值是否大于7FH实现的。
	 * 
	 * @param c
	 *            指定的字符
	 * @return 是否汉字
	 */
	public final static boolean isChineseChar(int c) {
		return c > 0x7F;
	}

	/**
	 * 返回指定字符的显示宽度，在目前的实现中，认为一个英文字符的显示宽度是1，一个汉字的显示宽度是2。
	 * 
	 * @param c
	 *            指定的字符
	 * @return 指定字符的显示宽度
	 */
	public final static int getCharViewWidth(int c) {
		return isChineseChar(c) ? 2 : 1;
	}

	/**
	 * 返回指定字符串的显示宽度
	 * 
	 * @param s
	 *            指定的字符串
	 * @return 指定字符串的显示宽度
	 */
	public final static int getStringViewWidth(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}

		int iWidth = 0;
		int iLength = s.length();

		for (int i = 0; i < iLength; i++) {
			iWidth += getCharViewWidth(s.charAt(i));
		}

		return iWidth;
	}

	/**
	 * 字符串截断函数：取指定字符串前部指定长度的字符串； 说明：英文和数字字符长度记1；中文字符长度记2。
	 * 
	 * @param _string
	 *            要截断的字符串。
	 * @param _maxLength
	 *            截断长度。
	 * @return 截断后的字符串。若指定长度小于字符串实际长度，则在返回的字符串后补“...”
	 */
	public static String truncateStr(String _string, int _maxLength) {
		return truncateStr(_string, _maxLength, "..");
	}

	/**
	 * 字符串截断函数：取指定字符串前部指定长度的字符串； 说明：英文和数字字符长度记1；中文字符长度记2。
	 * 
	 * @param _string
	 *            要截断的字符串。
	 * @param _maxLength
	 *            截断长度。
	 * @param _sExt
	 *            在截断后的字符串上的附加的字符串
	 * @return 截断后的字符串
	 */
	public static String truncateStr(String _string, int _maxLength, String _sExt) {
		if (_string == null) {
			return null;
		}

		if (_sExt == null) {
			_sExt = "..";
		}

		int nSrcLen = getStringViewWidth(_string);
		if (nSrcLen <= _maxLength) {
			// 源字符串太短，不需要截断
			return _string;
		}

		int nExtLen = getStringViewWidth(_sExt);
		if (nExtLen >= _maxLength) {
			// 目标长度太短（小于了附加字符串的长度），无法截断。
			return _string;
		}

		int iLength = _string.length();
		int iRemain = _maxLength - nExtLen;
		StringBuffer sb = new StringBuffer(_maxLength + 2); // 附加的“2”是没有意义的，只是为了容错

		for (int i = 0; i < iLength; i++) {
			char aChar = _string.charAt(i);
			int iNeed = getCharViewWidth(aChar);
			if (iNeed > iRemain) {
				sb.append(_sExt);
				break;
			}
			sb.append(aChar);
			iRemain = iRemain - iNeed;
		}

		return sb.toString();
	}

	/**
	 * 过滤掉XML不接受的字符
	 * 
	 * @param _string
	 *            源字符串
	 * @return
	 */
	public static String filterForJDOM(String _string) {
		if (_string == null)
			return null;

		char[] srcBuff = _string.toCharArray();
		int nLen = srcBuff.length;

		StringBuffer dstBuff = new StringBuffer(nLen);

		for (int i = 0; i < nLen; i++) {
			char aChar = srcBuff[i];
			if (!isValidCharOfXML(aChar))
				continue;

			dstBuff.append(aChar); // 检查是否还有字符
		}// end for
		return dstBuff.toString();

	}

	/**
	 * 校验当前字符是否是合法的XML字符
	 * 
	 * @param _char
	 *            需要校验的字符
	 * @return
	 */
	public static boolean isValidCharOfXML(char _char) {
		if (_char == 0x9 || _char == 0xA || _char == 0xD || (0x20 <= _char && _char <= 0xD7FF) || (0xE000 <= _char && _char <= 0xFFFD)
				|| (0x10000 <= _char && _char <= 0x10FFFF)) {
			return true;
		}
		return false;
	}

	/**
	 * 计算字符串所占的字节数；
	 * <p>
	 * 说明：英文和数字字符长度记1；中文字符长度记2。
	 * </p>
	 * 
	 * @param _string
	 *            要截断的字符串。
	 * @return 截断后的字符串。若指定长度小于字符串实际长度，则在返回的字符串后补“...”
	 */
	public static int getBytesLength(String _string) {
		if (_string == null)
			return 0;

		char[] srcBuff = _string.toCharArray();

		int nGet = 0; // 已经取得的字符串长度（长度：英文字符记1，中文字符记2）
		for (int i = 0; i < srcBuff.length; i++) {
			char aChar = srcBuff[i];
			nGet += (aChar <= 0x7f ? 1 : 2); // （长度：英文字符记1，中文字符记2）
		}// end for
		return nGet;
	}

	// 新增接口：截取规定长度的字符串
	// 程序说明（2002-04-20 by yql）：
	// 给定一个字符串，不管是英文还是中文，还是中英文混合的，只取前面的n个英文字母占位的宽度。
	// 如果字符串本身的长度小于需要截取的长度，则直接取该字符串，否则：
	// 当最后一个字为中文，并且前面已经取得 n-1 位时，就不再取这个字，在最后位置补"..."。
	/**
	 * 字符串截断函数：取指定字符串前部指定长度的字符串；
	 * <p>
	 * 说明：英文和数字字符长度记1；中文字符长度记2。
	 * </p>
	 * 
	 * @param _string
	 *            要截断的字符串。
	 * @param _length
	 *            截断长度。
	 * @return 截断后的字符串。若指定长度小于字符串实际长度，则在返回的字符串后补“...”
	 * @deprecated 已经由函数truncateStr替代
	 */
	public static String cutStr(String _string, int _length) {
		return truncateStr(_string, _length);

		/*
		 * int nTmp = 0; int nLen = 0; int nMaxLen = 0; int nTotalLen = 0;
		 * 
		 * //先计算字符串的长度 for( int j=0;j <_string.length();j++ ) { if(
		 * _string.charAt(j)>=0&&_string.charAt(j) <=128 ) nTotalLen += 1; else
		 * nTotalLen += 2; }
		 * 
		 * if( nTotalLen <=_length ) { //字符串本身的长度小于需要截取的长度，直接取该字符串 return
		 * _string; }
		 * 
		 * else { //否则进行判断 for( int i=0;i <_length;i++ ) { if(
		 * _string.charAt(i)>255 ) nTmp += 2; //中文字符长度加2 else nLen += 1;
		 * //英文字符长度加1
		 * 
		 * nMaxLen += 1; //记数
		 * 
		 * if( nTmp+nLen==_length ) { return ( _string.substring(0,nMaxLen)+".."
		 * ); } if( nTmp+nLen>_length ) { return (
		 * _string.substring(0,nMaxLen-1)+".." ); } } //end for } //end else
		 * 
		 * return _string; //
		 */
	}

	// ==========================================================================
	// ====
	// 处理Get方式的字符串

	/**
	 * 处理Get方式的字符串
	 * 
	 * @param s
	 *            待处理的字符串
	 * @return 返回经过处理的字符串
	 */
	public static String URLEncode(String s) {
		try {
			return URLCoder.encode(s, GET_ENCODING_DEFAULT);
		} catch (Exception ex) {
			return s;
		}
	}

	public static String[] split(String _str, String _sDelim) {
		// String[] str
		if (_str == null || _sDelim == null) {
			return new String[0];
		}

		java.util.StringTokenizer stTemp = new java.util.StringTokenizer(_str, _sDelim);
		int nSize = stTemp.countTokens();
		if (nSize == 0) {
			return new String[0];
		}

		String[] str = new String[nSize];
		int i = 0;
		while (stTemp.hasMoreElements()) {
			str[i] = stTemp.nextToken().trim();
			i++;
		}// endwhile
		return str;
	}

	/**
	 * 获取按照指定的分隔符截取到的字符个数
	 * 
	 * @param _str
	 *            指定的字符数
	 * @param _sDelim
	 *            指定的分隔符
	 * @return 分隔的字符个数（int）
	 */
	public static int countTokens(String _str, String _sDelim) {
		java.util.StringTokenizer stTemp = new java.util.StringTokenizer(_str, _sDelim);
		return stTemp.countTokens();
	}

	/**
	 * @param _str
	 *            if <code>null</code> or empty string return an array with zero
	 *            length.
	 * @param _sDelim
	 *            if <code>null</code> or empty string then this will set to
	 *            <code>,</code>
	 * @return
	 */
	public static int[] splitToInt(String _str, String _sDelim) {
		// wenyh@2006-3-15 16:28:35 add comment:如果是空串,返回长度为0的数组
		if (isEmpty(_str)) {
			return new int[0];
		}

		// to avoid null pointer exception throw
		if (isEmpty(_sDelim)) {
			_sDelim = ",";
		}

		java.util.StringTokenizer stTemp = new java.util.StringTokenizer(_str, _sDelim);
		int[] arInt = new int[stTemp.countTokens()];
		int nIndex = 0;
		String sValue;
		while (stTemp.hasMoreElements()) {
			sValue = (String) stTemp.nextElement();
			arInt[nIndex] = Integer.parseInt(sValue.trim());
			nIndex++;
		}
		return arInt;
	}

	/**
	 * @param vIdList
	 *            list数组
	 * @param _chrDelim
	 *            分隔符
	 * @return 带分割符字符串
	 */
	public static String getIdListAsString(List vIdList, char _separate) {
		String sIdList = null; // ID列表
		Object oKey = null;
		int nId;

		for (int i = 0; i < vIdList.size(); i++) {
			oKey = vIdList.get(i);
			if (oKey == null)
				continue;
			nId = ((Integer) oKey).intValue();
			if (sIdList == null) {
				sIdList = String.valueOf(nId);
			} else {
				sIdList += _separate + String.valueOf(nId);
			}// end if
		}// end for
		return sIdList;
	}

	public static int[] getIdListAsIntArr(List vIdList) {
		return splitToInt(getIdListAsString(vIdList, ','), ",");
	}

	// =====================================================
	// 中文名的缓冲
	private static String PY_RESOURCE_FILE = "winpy2000.txt";

	private static Hashtable m_hCharName = null;

	private static void loadFirstLetter(String _sFileName) throws Exception {
		if (m_hCharName == null) {
			m_hCharName = new Hashtable(300);
		} else {
			m_hCharName.clear();
		}
		FileReader fileReader = null;
		FileInputStream fis = null;
		BufferedReader buffReader = null;
		try {
			String sLine;
			fis = new FileInputStream(_sFileName);
			buffReader = new BufferedReader(new InputStreamReader(fis, CMyString.FILE_WRITING_ENCODING));
			while ((sLine = buffReader.readLine()) != null) {
				sLine = sLine.trim();
				// if(sLine.substring(0,
				// 1).equals("铁"))System.out.println(sLine);
				if (sLine.length() < 2)
					continue;
				char chrName = sLine.charAt(1);
				if (chrName > 0x7f)
					continue;
				m_hCharName.put(sLine.substring(0, 1).toUpperCase(), sLine.substring(1, 2));
			}// end while
		} catch (FileNotFoundException ex) {
			throw new CMyException(ExceptionNumber.ERR_FILE_NOTFOUND, I18NMessage.get(CMyString.class, "CMyString.label2",
					"要读取的拼音配置文件没有找到(CMyString.getFirstLetter)"), ex);
		} catch (IOException ex) {
			throw new CMyException(ExceptionNumber.ERR_FILEOP_READ, I18NMessage.get(CMyString.class, "CMyString.label3",
					"读拼音配置文件件时错误(CMyString.getFirstLetter)"), ex);
		} finally {
			if (buffReader != null)
				buffReader.close();
			if (fis != null)
				fis.close();
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (Exception ex) {
				}
			}
		}// end try
	}// END: readFile()

	private static Hashtable getPYResource() throws Exception {
		if (m_hCharName != null) {
			return m_hCharName;
		}

		String sResourcePath = CMyFile.mapResouceFullPath(PY_RESOURCE_FILE);
		loadFirstLetter(sResourcePath);
		return m_hCharName;
	}

	public static String getFirstLetter(String _str) throws Exception {
		if (_str == null || _str.length() < 0) {
			return "";
		}
		char[] arChars = _str.toCharArray();
		String sFirstChar = _str.substring(0, 1);
		if (arChars[0] > 0x7f) {// 如果首字为中文
			return ((String) getPYResource().get(sFirstChar.toUpperCase())).toUpperCase();
		}
		return sFirstChar.toUpperCase();
	}

	/**
	 * 处理XML内容时 <BR>
	 * 如果有CDATA嵌套则替换
	 * 
	 * @param _str
	 * @return
	 */
	public static final String encodeForCDATA(String _str) {
		if (_str == null || _str.length() < 1) {
			return _str;
		}

		return replaceStr(_str, CDATA_END, CDATA_END_REPLACER);
	}

	/**
	 * 处理XML内容 <BR>
	 * 如果有经过@see #encodeForCDATA(String)替换的CDATA嵌套则还原
	 * 
	 * @param _str
	 * @return
	 */
	public static final String decodeForCDATA(String _str) {
		if (_str == null || _str.length() < 1) {
			return _str;
		}

		return replaceStr(_str, CDATA_END_REPLACER, CDATA_END);
	}

	private static final String CDATA_END = "]]>";

	private static final String CDATA_END_REPLACER = "(TRSWCM_CDATA_END_HOLDER_TRSWCM)";

	// wenyh@2005-5-20 16:17:13 add comment:添加判断字符串中是否有中文字符的方法

	/**
	 * 判断字符串中是否包含中文字符 <BR>
	 * 如果包含,则返回 <code>true<code>
	 * 
	 * @param _str
	 *            指定的字符串
	 * @return
	 */
	public static final boolean isContainChineseChar(String _str) {
		if (_str == null) {
			return false;
		}

		return (_str.getBytes().length != _str.length());
	}

	// ge add by gfc @2005-8-23 15:44:00
	/**
	 * 将一个数组按照给定的连接符联结起来
	 * 
	 * @param _arColl
	 *            进行操作的数组
	 * @param _sSeparator
	 *            连接符
	 * @return 连接后的字符串
	 */
	public static String join(ArrayList _arColl, String _sSeparator) {
		// check parameters
		if (_arColl == null)
			return null;

		// invoke reload-method and return
		return join(_arColl.toArray(), _sSeparator);
	}

	// ge add by gfc @2005-8-23 15:44:22
	/**
	 * 将一个数组按照给定的连接符联结起来
	 * 
	 * @param _arColl
	 *            进行操作的数组
	 * @param _sSeparator
	 *            连接符
	 * @return 连接后的字符串
	 */
	public static String join(Object[] _arColl, String _sSeparator) {
		// check parameters
		if (_arColl == null || _arColl.length == 0 || _sSeparator == null)
			return null;

		if (_arColl.length == 1)
			return _arColl[0].toString();

		// resolve the demiter into the string
		StringBuffer result = new StringBuffer(_arColl[0].toString());
		for (int i = 1; i < _arColl.length; i++) {
			if (_arColl[i] == null) {
				continue;
			}
			result.append(_sSeparator);
			result.append(_arColl[i].toString());
		}

		// return the result
		return result.toString();
	}

	public static String join(int[] _intColl, String _sSeparator) {
		if (_intColl == null || _intColl.length == 0) {
			return "";
		}
		// 可以为""
		if (_sSeparator == null) {
			_sSeparator = ",";
		}
		StringBuffer buf = new StringBuffer();
		buf.append(_intColl[0]);
		for (int i = 1; i < _intColl.length; i++) {
			buf.append(_sSeparator).append(_intColl[i]);
		}
		return buf.toString();
	}

	public static boolean containsCDATAStr(String _sValue) {
		if (_sValue == null)
			return false;

		return _sValue.matches("(?ism).*<!\\[CDATA\\[.*|.*\\]\\]>.*");
	}

	/**
	 * 替换内容中的变量
	 * 
	 * @param _sContent
	 *            正文内容
	 * @param _variables
	 *            变量表
	 * @return
	 * @throws WCMException
	 */
	public static String parsePageVariables(String _sContent, Map _variables) throws WCMException {
		return parsePageVariables(_sContent, _variables, new char[] { '{', '}' }, PGVARNAME_MAX_LENGTH);
	}

	/**
	 * 替换内容中的变量
	 * 
	 * @param _sContent
	 *            正文内容
	 * @param _variables
	 *            变量表<KEY需要为大写>
	 * @param _aSep
	 *            变量前缀和后缀分隔符
	 * @return
	 * @throws WCMException
	 */
	public static String parsePageVariables(String _sContent, Map _variables, char[] _aSep) throws WCMException {
		return parsePageVariables(_sContent, _variables, _aSep, PGVARNAME_MAX_LENGTH);
	}

	/**
	 * 替换内容中的变量
	 * 
	 * @param _sContent
	 *            正文内容
	 * @param _variables
	 *            变量表<KEY需要为大写>
	 * @param _nPgvarnameMaxLength
	 *            变量最大匹配长度
	 * @return
	 * @throws WCMException
	 */
	public static String parsePageVariables(String _sContent, Map _variables, int _nPgvarnameMaxLength) throws WCMException {
		return parsePageVariables(_sContent, _variables, new char[] { '{', '}' }, _nPgvarnameMaxLength);
	}

	/**
	 * 替换内容中的变量
	 * 
	 * @param _sContent
	 *            正文内容
	 * @param _variables
	 *            变量表<KEY需要为大写>
	 * @param _aSep
	 *            变量前缀和后缀分隔符
	 * @param _nPgvarnameMaxLength
	 *            变量最大匹配长度
	 * @return
	 * @throws WCMException
	 */
	public static String parsePageVariables(String _sContent, Map _variables, char[] _aSep, int _nPgvarnameMaxLength) throws WCMException {
		if (_sContent == null) {
			return null;
		}

		StringBuffer buffResult = null;

		try {
			char[] chrArray = _sContent.toCharArray();
			int nLength = chrArray.length;
			int nPos = 0;
			buffResult = new StringBuffer();

			while (nPos < chrArray.length) {
				char aChar = chrArray[nPos++];
				// to find "${"
				if (aChar == '$' && nPos < nLength && chrArray[nPos] == _aSep[0]) {
					StringBuffer buffVarName = new StringBuffer(16);
					nPos++; // skip '{'

					// 下面的写法,如果正文恰好含有"${"组合会导致错误 by FCR
					// while ((aChar = chrArray[nPos++]) != '}')
					// {
					// buffVarName.append(aChar);
					// }

					// 下面的修改仍然存在隐患 by FCR
					// 对“${...${...}”的极端情况仍然没有处理 by FCR

					int iCount = 0;
					boolean zFound = false;

					while (iCount++ < _nPgvarnameMaxLength && nPos < nLength) {
						if ((aChar = chrArray[nPos++]) == _aSep[1]) {
							zFound = true;
							break;
						}
						buffVarName.append(aChar);
					}

					if (zFound) {
						String sKey = buffVarName.toString().toUpperCase();
						String sVal = "";
						Object oValue = _variables.get(sKey);
						if (oValue != null) {
							sVal = oValue.toString();
						}

						if (sVal != null) {
							buffResult.append(sVal);
						} else {
							buffResult.append("$").append(_aSep[0]).append(buffVarName).append(_aSep[1]);
						}
					} else {
						buffResult.append("$").append(_aSep[0]).append(buffVarName);
					}
				} else {
					buffResult.append(aChar);
				}
			}// endwhile
			return buffResult.toString();
		} catch (Exception ex) {
			throw new WCMException(ExceptionNumber.ERR_WCMEXCEPTION, I18NMessage.get(CMyString.class, "CMyString.label4", "解析内容中的变量失败!"), ex);
		}
	}

	public static String transPrettyUrl(String _sUrl, int _nMaxLen) {
		return transPrettyUrl(_sUrl, _nMaxLen, null);
	}

	public static String transPrettyUrl(String _sUrl, int _nMaxLen, String _sSkimWord) {
		int nDemPos = 0;
		if (_sUrl == null || _nMaxLen <= 0 || _sUrl.length() <= _nMaxLen || (nDemPos = _sUrl.lastIndexOf('/')) == -1) {
			return _sUrl;
		}
		// else
		int nFirstPartDemPos = _sUrl.lastIndexOf("://") + 3;
		String sFirstPart = _sUrl.substring(0, nFirstPartDemPos);
		String sMidPart = _sUrl.substring(nFirstPartDemPos, nDemPos);
		if (sMidPart.length() < 3) {
			return _sUrl;
		}
		int nMidLen = (_nMaxLen + sMidPart.length() - _sUrl.length());
		if (nMidLen <= 3) {
			nMidLen = 3;
		}
		sMidPart = sMidPart.substring(0, nMidLen);
		sMidPart += (_sSkimWord != null ? _sSkimWord : "....");

		String sLastPart = _sUrl.substring(nDemPos);
		return sFirstPart + sMidPart + sLastPart;
	}

	/**
	 * 替换段首和段尾的空格
	 * 
	 * @param _strValue
	 * @return
	 */
	public static String replaceStartEndSpaces(String _strValue) {
		Pattern pattern = Pattern.compile("(?m)^(\\s*)(.*?)(\\s*)$");
		Matcher matcher = pattern.matcher(_strValue);
		int nLineCount = 30;
		StringBuffer sbResult = new StringBuffer(nLineCount * 100 + _strValue.length());
		while (matcher.find()) {
			// 替换段首的空格
			String sStartSpaces = matcher.group(1);
			for (int i = 0; i < sStartSpaces.length(); i++) {
				char c = sStartSpaces.charAt(i);
				if (c == ' ')
					sbResult.append("&nbsp;");
				else {
					sbResult.append(c);
				}
			}
			// 追加正文
			sbResult.append(matcher.group(2));
			// 替换结尾的空格
			String sEndSpaces = matcher.group(3);
			char c = 0;
			for (int i = 0; i < sEndSpaces.length(); i++) {
				c = sEndSpaces.charAt(i);
				if (c == ' ')
					sbResult.append("&nbsp;");
				else {
					sbResult.append(c);
				}
			}

		}
		return sbResult.toString();
	}

	/**
	 * 先拆分成段，再分别替换各段段首和段尾的空格
	 * 
	 * @param _strValue
	 * @return
	 */
	public static String replaceParasStartEndSpaces(String _strValue) {
		Pattern pattern = Pattern.compile("\n\r|\n|\r");
		Matcher matcher = pattern.matcher(_strValue);
		int nLineCount = 30;
		StringBuffer sbResult = new StringBuffer(nLineCount * 100 + _strValue.length());
		int nLastIndex = 0;
		while (matcher.find()) {
			int nCurrIndex = matcher.start();
			String sTmpBefore = _strValue.substring(nLastIndex, nCurrIndex);
			sTmpBefore = replaceStartEndSpaces(sTmpBefore);
			sbResult.append(sTmpBefore);
			sbResult.append("<br/>");
			nLastIndex = matcher.end();
		}
		String sTmpAfter = _strValue.substring(nLastIndex);
		sTmpAfter = replaceStartEndSpaces(sTmpAfter);
		sbResult.append(sTmpAfter);
		return sbResult.toString();
	}

	public static Map split2AttrMap(String _sAttrStr) {
		Pattern pattern = Pattern.compile("([^\\s=]*)\\s*=(([^\\s'\"]+\\s)|(\\s*(['\"]?)(.*?)\\5))");
		Matcher matcher = pattern.matcher(_sAttrStr);
		Map mpResult = new HashMap();
		while (matcher.find()) {
			String sValue = CMyString.showNull(matcher.group(6), matcher.group(3));
			mpResult.put(matcher.group(1), sValue);
		}
		return mpResult;
	}

	/**
	 * 用数组_args里面的第i个变量替换掉_sFormat中的{i}
	 * 
	 * @param _sFromat
	 * @param _args
	 * @return
	 */
	public static String format(String _sFormat, String[] _args) {
		StringBuffer sbResult = new StringBuffer(_sFormat.length());

		Pattern pattern = Pattern.compile("\\{(\\d+)\\}");
		Matcher matcher = pattern.matcher(_sFormat);

		int nIndex;
		int nStart = 0;
		int nEnd = 0;
		while (matcher.find()) {
			nStart = matcher.start();
			sbResult.append(_sFormat.substring(nEnd, nStart));

			nIndex = Integer.parseInt(matcher.group(1));
			sbResult.append(_args[nIndex]);
			nEnd = matcher.end();
		}

		if (nEnd < _sFormat.length()) {
			sbResult.append(_sFormat.substring(nEnd));
		}
		return sbResult.toString();
	}

	/**
	 * 用数组_args里面的第i个变量替换掉_sFormat中的{i}
	 * 
	 * @param _sFromat
	 * @param _args
	 * @return
	 */
	public static String format(String _sFormat, Object[] _args) {
		if (CMyString.isEmpty(_sFormat))
			return "";

		StringBuffer sbResult = new StringBuffer(_sFormat.length());

		Pattern pattern = Pattern.compile("\\{(\\d+)\\}");
		Matcher matcher = pattern.matcher(_sFormat);

		int nIndex;
		int nStart = 0;
		int nEnd = 0;
		while (matcher.find()) {
			nStart = matcher.start();
			sbResult.append(_sFormat.substring(nEnd, nStart));

			nIndex = Integer.parseInt(matcher.group(1));
			sbResult.append(_args[nIndex]);
			nEnd = matcher.end();
		}

		if (nEnd < _sFormat.length()) {
			sbResult.append(_sFormat.substring(nEnd));
		}
		return sbResult.toString();
	}

	/**
	 * 用数组_args里面的第i个变量替换掉_sFormat中的{i}
	 * 
	 * @param _sFromat
	 * @param _args
	 * @return
	 */
	public static String format(String _sFormat, int[] _args) {
		if (CMyString.isEmpty(_sFormat))
			return "";

		StringBuffer sbResult = new StringBuffer(_sFormat.length());

		Pattern pattern = Pattern.compile("\\{(\\d+)\\}");
		Matcher matcher = pattern.matcher(_sFormat);

		int nIndex;
		int nStart = 0;
		int nEnd = 0;
		while (matcher.find()) {
			nStart = matcher.start();
			sbResult.append(_sFormat.substring(nEnd, nStart));

			nIndex = Integer.parseInt(matcher.group(1));
			sbResult.append(_args[nIndex]);
			nEnd = matcher.end();
		}

		if (nEnd < _sFormat.length()) {
			sbResult.append(_sFormat.substring(nEnd));
		}
		return sbResult.toString();
	}

	public static String innerText(String _strValue) {
		// 段与段之间的显示1个换行
		return transInnerText(_strValue, DEFAULT_ENTER_NUM);
	}

	public static String innerText(String _strValue, int _enterNum) {
		return transInnerText(_strValue, _enterNum);
	}

	/**
	 * 
	 * @param _srcContent
	 * @param _nEnterNum
	 *            段落换行替换为几个\n
	 * @return
	 */
	private static String transInnerText(String _srcContent, int _nEnterNum) {
		if (CMyString.isEmpty(_srcContent)) {
			return "";
		}
		String sEnterCon = "";
		if (_nEnterNum <= 0)
			_nEnterNum = DEFAULT_ENTER_NUM;
		for (int i = 0; i < _nEnterNum; i++) {
			sEnterCon += "\n";
		}
		sEnterCon += "　　";
		String sValue = _srcContent.replaceAll("(?is)<style[^>]*>.*</style>", "");
		sValue = sValue.replaceAll("(?is)<br[^>]*>([\\n\\s]*(&nbsp;| |　)*)*", "\n　　");
		sValue = sValue.replaceAll("(?is)<p[^>]*>([\\n\\s]*(&nbsp;| |　)*)*", "\n　　");
		sValue = sValue.replaceAll("(?is)</p[^>]*>", "");
		sValue = sValue.replaceAll(HTML_TAGS, "");
		sValue = sValue.replaceAll("(?is)([ 　]*\n+[ 　]*)+", sEnterCon);
		return sValue.replaceAll("&nbsp;", " ").replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"").replaceAll("&apos;", "\'")
				.replaceAll("&amp;", "&").replaceAll("&ldquo;", "“").replaceAll("&rdquo;", "”").replaceAll("&hellip;", "…")
				.replaceAll("&mdash;", "—").replaceAll("&lsquo;", "‘").replaceAll("&rsquo;", "’");
	}

	/**
	 * 对输入字符串中的正则表达式特殊字符进行转义，转成普通字符
	 * 
	 * @param sSource
	 * @return
	 */
	public static String encodeForRegExp(String sSource) {
		String specialRegExpCharset = ".^${[(|}])*+?\";\\";
		StringBuffer sb = new StringBuffer(2 * sSource.length());
		for (int i = 0, length = sSource.length(); i < length; i++) {
			int nCurrChar = sSource.charAt(i);
			if (specialRegExpCharset.indexOf(nCurrChar) >= 0) {
				sb.append("\\");
			}
			sb.append((char) nCurrChar);
		}
		return sb.toString();
	}

	public static String capitalize(String _strValue) {
		if (isEmpty(_strValue))
			return _strValue;
		char first = _strValue.charAt(0);
		char capitalized = Character.toUpperCase(first);
		return (first == capitalized) ? _strValue : capitalized + _strValue.substring(1);
	}

	/**
	 * 判断指定的字符串是否在指定的字符串数组中.
	 * 
	 * @param string
	 *            需要判断定字符串
	 * @param targetArray
	 *            指定的字符串数组
	 * @param ignoreCase
	 *            是否忽略大小写比较,当该参数值为<code>true</code>时,忽略大小写
	 * @return 当string为<code>null</code>或targetArray为空数组(值为<code>null</code>
	 *         或长度为0),返回<code>false</code>.当且仅当targetArray数组中包含string时返回
	 *         <code>true</code>
	 */
	public static boolean isStringInArray(String string, String[] targetArray, boolean ignoreCase) {
		if (string == null || targetArray == null || targetArray.length == 0) {
			return false;
		}
		if (ignoreCase) {
			for (int i = targetArray.length - 1; i >= 0; i--) {
				if (string.equalsIgnoreCase(targetArray[i])) {
					return true;
				}
			}
		} else {
			for (int i = targetArray.length - 1; i >= 0; i--) {
				if (string.equals(targetArray[i])) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 过滤掉sHtml中，指定tag的属性
	 */
	public static String cleanAttributes4Tags(String sHtml, String sTags) {
		String[] aTags = CMyString.split(sTags, ",");
		return cleanAttributes4Tags(sHtml, aTags);
	}

	public static String cleanAttributes4Tags(String sHtml, String[] aTags) {
		String sResult = sHtml;

		for (int i = 0; i < aTags.length; i++) {
			sResult = cleanAttributes4Tag(sResult, aTags[i]);
		}

		return sResult;
	}

	public static String cleanAttributes4Tag(String sHtml, String tag) {
		String sPattern = "(?is)<(" + tag + ")[^>]*>";
		return sHtml.replaceAll(sPattern, "<$1>");
	}

	/**
	 * 需求的提出：<br/>
	 * 外交部iPhone版本细览由于要显示网页，但网页中仅保留table,img元素，所以需要过来掉其他所有元素。<br/>
	 * 接口的说明：<br/>
	 * 将sHtml内容中没有出现在tags中的标签都过滤掉，如果该标签内部有内容，那么内容将保留，有点类似innerText。
	 * 
	 * @param tags
	 * @param sHtml
	 * @return
	 */
	public static String includeTags(String sHtml, String sTags) {
		String[] aTags = CMyString.split(sTags, ",");
		return includeTags(sHtml, aTags);
	}

	public static String includeTags(String sHtml, String[] tags) {

		// 匹配html标记
		Pattern m_oTagPattern = Pattern.compile("(?is)</?([^> ]*)[^>]*/?>");

		// 进行正则匹配
		Matcher matcher = m_oTagPattern.matcher(sHtml);

		// 存放片段被替换之后的结果
		StringBuffer sbHtml = new StringBuffer(sHtml.length());

		String sMatchTag;
		int nStartIndex = 0;
		int nEndIndex = 0;

		// 循环分析匹配结果
		while (matcher.find()) {
			// 获取本次匹配的开始位置
			nStartIndex = matcher.start();

			// 追加前一次匹配结束到本次匹配开始之间的内容
			sbHtml.append(sHtml.substring(nEndIndex, nStartIndex));

			// 获取本次匹配的结束位置
			nEndIndex = matcher.end();

			// 获取本次匹配的tag
			sMatchTag = matcher.group(1);

			// 需要包含的标记
			if (isStringInArray(sMatchTag, tags, true)) {
				sbHtml.append(sHtml.substring(nStartIndex, nEndIndex));
				continue;
			}
		}

		// 如果匹配过程中，没有到达需要处理串的尾部，则继续添加上一次匹配的结束位置到字符串末尾之间的内容
		if (nEndIndex < sHtml.length()) {
			sbHtml.append(sHtml.substring(nEndIndex));
		}
		return sbHtml.toString();
	}

	/**
	 * 需求的提出：<br/>
	 * 外交部iPad版本细览由于要显示网页，但网页中的embed元素不能正常显示，所以需要过来掉embed元素，另外一些script元素也需要过滤。
	 * 接口的说明：<br/>
	 * 将sHtml内容中出现的tags标签都过滤掉，如果该标签内部还有内容，那么它的内容也将被过滤，如：script节点。
	 * 如果该tag嵌套，那么过滤将可能有问题。
	 * 
	 * @param sHtml
	 * @param tags
	 * @return
	 */
	public static String excludeTags(String sHtml, String sTags) {
		String[] aTags = CMyString.split(sTags, ",");
		return excludeTags(sHtml, aTags);
	}

	public static String excludeTags(String sHtml, String[] tags) {
		String sResult = sHtml;

		for (int i = 0; i < tags.length; i++) {
			sResult = excludeTag(tags[i], sResult);
		}
		return sResult;
	}

	public static String excludeTag(String tag, String sHtml) {

		// 过滤保护内容的节点
		String sPattern = "(?is)<" + tag + "[^>]*>.*?</" + tag + ">";
		sHtml = sHtml.replaceAll(sPattern, "");

		// 过滤没有包含内容的节点
		sPattern = "(?is)<" + tag + "[^>]*/>";
		return sHtml.replaceAll(sPattern, "");
	}

	/**
	 * 进行简单的四则运算，不考虑优先级，只按照顺序进行运算. <br />
	 * 如：3+5*2-1=15，如果是jdk1.6的话，可以使用JSEngineManager,但由于需要兼容地版本的jdk
	 * 
	 * @param sCalcuString
	 * @return
	 */
	public static float expressionEval(String sCalcuString) {

		float result = 0;
		char cLastOperator = '+';
		StringBuffer sbCurrent = new StringBuffer(sCalcuString.length());

		for (int i = 0, length = sCalcuString.length(); i <= length; i++) {
			char cTemp;

			if (i != length) {
				cTemp = sCalcuString.charAt(i);
			} else {
				cTemp = '+';// 迫使遇到字符串结尾时也进行运算
			}

			// 已经是下一个运算符的开始，则进行已获取数字的运算
			if (cTemp == '+' || cTemp == '-' || cTemp == '*' || cTemp == '/') {
				//
				String sLastNumber = sbCurrent.toString();
				sbCurrent.setLength(0);// 清空结果
				float fLastNumber = Float.parseFloat(sLastNumber);

				switch (cLastOperator) {
				case '+':
					result += fLastNumber;
					break;
				case '-':
					result -= fLastNumber;
					break;
				case '*':
					result *= fLastNumber;
					break;
				case '/':
					result /= fLastNumber;
					break;
				}

				cLastOperator = cTemp;
				continue;
			}
			sbCurrent.append(cTemp);
		}

		return result;
	}

	/**
	 * 过滤html标记
	 * 
	 * @param sHtml
	 * @return
	 */
	public static String stripHTMLTags(String sHtml) {
		if (CMyString.isEmpty(sHtml)) {
			return "";
		}
		return sHtml.replaceAll(HTML_TAGS, "");
	}

	/**
	 * 进行html合法性的校验，忽略TRS置标产生的影响
	 * 
	 * @param sHtml
	 * @return
	 */
	public static String verifyHTMLIgnoreTRSTag(String sHtml) {
		// 先替换TRS置标，将<TRS > 替换成《TRS 》;
		sHtml = sHtml.replaceAll("(?is)<(/?TRS[^>]*)>", "《$1》");

		String sResult = verifyHTML(sHtml);

		if (!CMyString.isEmpty(sResult)) {
			sResult = sResult.replaceAll("(?is)《(/?TRS[^》]*)》", "<$1>");
		}
		return sResult;
	}

	/**
	 * 校验html是否正常闭合，html的写法必须符合xhtml规范，否则认为是不合法的。 <br />
	 * 如：<br />
	 * 1....&lt;div&gt;这是一条测试&lt;/div&lgt;...是正常的闭合； 2....<div>sdkkd
	 * <p>
	 * sdfj</div>...是没有正常闭合； 3....<div>sdjf
	 * <p>
	 * sdjkfs
	 * </p>
	 * dskdf</div>...是正常闭合
	 * 
	 * @param sHtml
	 * @return 如果闭合，返回null；否则返回没有闭合的提示信息。
	 */
	public static String verifyHTML(String sHtml) {

		// 该正则匹配诸如：<div>, <div class="xxx"...>, </div>, <br />等各种形式
		Pattern pattern = Pattern.compile("(?s)<(/?)([^> !/=]+)[^>]*?(/?)>");

		// 用堆栈的形式记录存储结果，元素形式为：<标记名称小写，行索引，列索引，完整的标记>，如：<div, 5,
		// 3, <div class="xx"> >表示该元素为div元素，出现在第5行，第3列
		Stack result = new Stack();

		// 记录匹配发生时的开始索引位及结束索引位
		int nStartIndex = 0;

		// 记录当前匹配过程中出现的行号及列号
		int nRowIndex = 1, nColIndex = 1;

		Matcher matcher = pattern.matcher(sHtml);

		while (matcher.find()) {
			int nLastStartIndex = nStartIndex;
			nStartIndex = matcher.start();

			// 更新行号及列号
			int nTmpIndex = nLastStartIndex;
			while (true) {
				int nEnterIndex = sHtml.indexOf('\n', nTmpIndex);

				// 没有找到换行符号或者找到的换行符号大于本次匹配
				if (nEnterIndex < 0 || nEnterIndex >= nStartIndex) {
					nColIndex += (nStartIndex - nTmpIndex);
					break;
				}

				// 找到了正确位置的换行符
				nRowIndex++;
				nColIndex = 1;// 重置列号

				// 继续寻找下一个换行
				nTmpIndex = nEnterIndex + 1;
			}

			// 调试信息
			// System.out.println("rowIndex:" + nRowIndex + ";colIndex:"
			// + nColIndex + "\n" + matcher.group());

			// 获取当前匹配的标记，如：div
			String sTag = matcher.group(2).toLowerCase();

			String sEndSlash = matcher.group(3);
			if (sEndSlash.length() > 0) {// 本标记为自闭合标记，如：<br />
				continue;
			}

			String sStartSlash = matcher.group(1);
			/*
			 * 本标记为闭合标记，如：</div> 和栈顶标记进行比较，如果盏顶为匹配的开始标记，则认为是符号规则，否则认为不符合规则
			 */
			if (sStartSlash.length() > 0) {
				Object[] item = (Object[]) result.pop();

				if (!item[0].equals(sTag)) {
					return "开始标记[tag:" + item[0] + ",row:" + item[1] + ",col:" + item[2] + "]与结束标记[tag:" + sTag + ",row:" + nRowIndex + ",col:"
							+ nColIndex + "]不匹配。\n开始标记的代码片段为:\n" + item[3] + "\n结束的代码片段为:\n" + matcher.group();
				}

				// 匹配，进入下一次查找
				continue;
			}

			/*
			 * 为开始标记，如：<div class="xxx"...>
			 */
			result.push(new Object[] { sTag, Integer.valueOf(nRowIndex), Integer.valueOf(nColIndex), matcher.group() });
		}

		// 如果结束后，堆栈不空，则认为整段html不符合规则
		if (!result.isEmpty()) {
			Object[] item = (Object[]) result.pop();
			return "开始标记[tag:" + item[0] + ",row:" + item[1] + ",col:" + item[2] + "]没有对应的结束标记。\n开始标记的代码片段为:\n" + item[3];
		}

		return null;
	}

	/**
	 * IP是否在允许范围之内
	 * 
	 * @param _pIPs
	 * @param _sRemoteAddr
	 * @return
	 */
	public static boolean isAllowIP(String[] _pIPs, String _sRemoteAddr) {
		if (_pIPs == null)
			return false;

		String[] pRemoteIPList = _sRemoteAddr.split("\\.");
		for (int i = 0; i < _pIPs.length; i++) {
			String[] pAllowIPList = _pIPs[i].split("\\.");
			if (pAllowIPList.length != pRemoteIPList.length)
				continue;

			boolean bNotEquals = false;
			for (int j = 0; j < pRemoteIPList.length; j++) {
				if (pAllowIPList[j].equals("*"))
					continue;

				if (!pRemoteIPList[j].equals(pAllowIPList[j])) {
					bNotEquals = true;
					break;
				}
			}
			if (!bNotEquals) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 简单测试用例
	 * 
	 * @param args
	 * @throws CMyException
	 */
	public static void main(String[] args) throws CMyException {
		String myTAGS = "(?im)</?(?:STYLE|A|META|ACRONYM|ADDRESS|APPLET|AREA|B|BASE|BASEFONT|BDO|BGSOUND|BIG|BLOCKQUOTE|BODY|BR|BUTTON|CAPTION|CENTER|CITE|CODE|COL|COLGROUP|COMMENT|CUSTOM|DD|DEL|DFN|DIR|DIV|DL|DT|EM|EMBED|FIELDSET|FONT|FORM|FRAME|FRAMESET|HEAD|hn|HR|HTML|I|IFRAME|IMG|INPUT|INS|ISINDEX|KBD|LABEL|LEGEND|LI|LINK|LISTING|MAP|MARQUEE|MENU|nextID|NOBR|NOFRAMES|NOSCRIPT|OBJECT|OL|OPTION|P|PLAINTEXT|PRE|Q|RT|RUBY|S|SAMP|SCRIPT|SELECT|SMALL|SPAN|STRIKE|STRONG|styleSheet|SUB|SUP|TABLE|TBODY|TD|TEXTAREA|TFOOT|TH|THEAD|TITLE|TR|TT|U|UL|VAR|WBR|XML|XMP|\\?*[a-z1-9]+:?)[^>]*>";
		String sTest = "<?xml:namespace prefix='xxx' />33333<中国>33333333<o:p>55555555555</o:p>999999999999";
		System.out.println(sTest.replaceAll(myTAGS, ""));
		if (true)
			return;

		String sContent = CMyFile.readFile("C:\\Users\\huxiejin\\Desktop\\wjb-1120\\tags.html", "utf-8");
		String sCleanTagsAttrs = "p,span";
		sContent = cleanAttributes4Tags(sContent, sCleanTagsAttrs);

		// String sExcludeTags = "script";
		// sContent = excludeTags(sContent, sExcludeTags);
		//
		// String sIncludeTags = "table,tbody,tr,td,img,p";
		// sContent = includeTags(sContent, sIncludeTags);
		CMyFile.writeFile("C:\\Users\\huxiejin\\Desktop\\wjb-1120\\tags2.html", sContent, "utf-8");
		if (true)
			return;

		// String sValue = I18NMessage.get(CMyString.class, "CMyString.label5",
		// " \na aa \n bbb\n中 国\n\r大工业 ac bd \n \r全部bcc ");
		// // String sValue = "。\n<br /> \n ";
		// sValue = replaceParasStartEndSpaces(sValue);
		// System.out.println(sValue);
		// String sExtraAttrs =
		// "action=post.do target = \"_bla nk\" onclick='alert(\"abc\"); return false;'";
		// System.out.println(split2AttrMap(sExtraAttrs));
		// String source = "~!@#$%^&*()_+|}{:?><,./;'[]\\=-`";
		// System.out.println("~!@#$%^&*()_+|}{:?><,./;'[]\\=-`".matches(CMyString
		// .encodeForRegExp(source)));
		//
		// String sHtmlValue =
		// "&lt;div align='center'&gt;&nbsp;2010 北京拓尔思信息技术股份有限公司&lt;/div&gt;&lt;div align='center'&gt;版权所有.保留所有权&lt;/div&gt;";
		// System.out.println(CMyString.unfilterForHTMLValue(sHtmlValue));
		String _dDBAntBuildFile = "D:" + File.separator + "TRS" + File.separator + "TRSWCMV6514304" + File.separator + "Tomcat" + File.separator
				+ "webapps" + File.separator + "wcm" + File.separator + "update" + File.separator + "db" + File.separator + "build.xml";
		_dDBAntBuildFile = CMyString.replaceStr(_dDBAntBuildFile, "\\", "\\\\");
		String sFileExt = CMyFile.extractFileExt(_dDBAntBuildFile);
		String sBuildFile = _dDBAntBuildFile.substring(0, _dDBAntBuildFile.lastIndexOf("." + sFileExt));
		String sBuildNewFile = sBuildFile + "_true." + sFileExt;
		System.out.println("----------------" + sBuildNewFile);
	}
	/* add by wgw 2008-10-30 */

}