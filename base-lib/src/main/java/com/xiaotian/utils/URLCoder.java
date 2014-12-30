/*
 * URLCoder.java created on 2005-8-5 16:47:38 by Martin (Fu Chengrui)
 */

package com.xiaotian.utils;

import java.io.UnsupportedEncodingException;

/**
 * URL的编码和解码
 * 
 * 是从“com.trs.util.net”中移植过来的一个类，因为目前还不太方便直接使用“com.trs.util.net”
 * 中的工具类。所以把其中的类型“URLCoder”移植到“com.trs.infra.util”中。
 * 
 * @author Martin (Fu Chengrui)
 */
public final class URLCoder {

	/**
	 * 不需要转换的字符映射表
	 */
	private static boolean[] dontNeed;

	/**
	 * 缺省的编码字符集，其确定的顺序为 1.System.getProperty("trs.url.encoding")
	 * 2.System.getProperty("file.encoding") 3.GBK 4.ASCII
	 */
	private static String encoding;

	/**
	 * 16进制转换表
	 */
	private static char[] hexDigit;

	/**
	 * 初始化
	 */
	static {
		init();
	}

	/**
	 * 使用缺省字符集解码URL字符串
	 * 
	 * @param s
	 *            待解码的URL字符串
	 */
	public final static String decode(String s) {
		if (s != null) {
			byte[] buff = new byte[s.length()];

			int iLen = decode(s, buff);

			try {
				return (new String(buff, 0, iLen, encoding));
			} catch (UnsupportedEncodingException ex) {
				// ignore
			}
		}
		return null;
	}

	/**
	 * 解码URL字符串到字节数组，并返回解码后的长度。
	 * 
	 * @param str
	 *            待解码的URL字符串
	 * @param out
	 *            输出数组
	 * @return 解码后的长度
	 */
	private final static int decode(String str, byte[] out) {
		int iPos = 0;
		int iLen = out.length;
		int iDst = 0;

		mainloop: for (iPos = 0; iPos < iLen; iPos++) {
			char c = str.charAt(iPos);
			switch (c) {
			case '+':
				out[iDst++] = (byte) ' ';
				break;
			case '%':
				try {
					out[iDst++] = hex2byte(str.charAt(iPos + 1), str.charAt(iPos + 2));
					iPos += 2;
				} catch (NumberFormatException e) {
					break mainloop;
				}
				break;
			default:
				out[iDst++] = (byte) c;
				break;
			}
		}

		return iDst;
	}

	/**
	 * 使用指定的字符集解码URL
	 * 
	 * @param str
	 *            待解码的URL字符串
	 * @param enc
	 *            指定的字符集
	 * @return 解码后的字串
	 * @throws UnsupportedEncodingException
	 */
	public final static String decode(String str, String enc) throws UnsupportedEncodingException {
		byte[] buff = new byte[str.length()];

		int iLen = decode(str, buff);

		return (new String(buff, 0, iLen, enc));
	}

	/**
	 * 对字节数组进行URL编码
	 * 
	 * @param buff
	 *            待编码的字节数组
	 * @return 编码后的URL
	 * @return
	 */
	private final static String encode(byte[] buff) {
		int iResultLen = 0;
		char[] out = new char[3 * buff.length + 1];
		for (int i = 0; i < buff.length; i++) {
			int c = buff[i];
			c = c & 0xFF;
			if (dontNeed[c] == true) {
				if (c == ' ') {
					c = '+';
				}
				out[iResultLen++] = (char) c;
			} else {
				out[iResultLen++] = '%';
				out[iResultLen++] = hexDigit[(c >> 4) & 0xF];
				out[iResultLen++] = hexDigit[c & 0xF];
			}
		}
		return new String(out, 0, iResultLen);
	}

	/**
	 * 使用缺省字符集编码URL
	 * 
	 * @param s
	 *            待编码的字符串
	 * @return 编码后的URL
	 */
	public final static String encode(String s) {
		if (s != null) {
			try {
				return encode(s.getBytes(encoding));
			} catch (UnsupportedEncodingException ex) {
				// ignore
			}
		}
		return null;
	}

	/**
	 * 使用指定的字符集编码URL
	 * 
	 * @param str
	 *            待编码的字符串
	 * @param enc
	 *            指定的字符集
	 * @return 编码后的URL
	 * @throws UnsupportedEncodingException
	 */
	public final static String encode(String str, String enc) throws UnsupportedEncodingException {
		return encode(str.getBytes(enc));
	}

	/**
	 * 转换连个连续的16进制字符为一个字节
	 * 
	 * @param h
	 *            高位字符
	 * @param l
	 *            低位字符
	 * @return 转换结果
	 */
	private final static byte hex2byte(char h, char l) {
		int a = h - '0';

		if (a >= 10) {
			a = a - ('A' - '0' - 10);
		}

		int b = l - '0';

		if (b >= 10) {
			b = b - ('A' - '0' - 10);
		}

		return (byte) ((a << 4) | b);
	}

	/**
	 * 初始化所有的常量
	 */
	private final static void init() {
		hexDigit = new char[16];
		hexDigit[0x0] = '0';
		hexDigit[0x1] = '1';
		hexDigit[0x2] = '2';
		hexDigit[0x3] = '3';
		hexDigit[0x4] = '4';
		hexDigit[0x5] = '5';
		hexDigit[0x6] = '6';
		hexDigit[0x7] = '7';
		hexDigit[0x8] = '8';
		hexDigit[0x9] = '9';
		hexDigit[0xA] = 'A';
		hexDigit[0xB] = 'B';
		hexDigit[0xC] = 'C';
		hexDigit[0xD] = 'D';
		hexDigit[0xE] = 'E';
		hexDigit[0xF] = 'F';
		//
		encoding = null;
		String s = null;

		s = System.getProperty("trs.url.encoding");
		if (testEncoding(s)) {
			encoding = s;
		}
		s = System.getProperty("file.encoding");
		if (testEncoding(s)) {
			encoding = s;
		}
		s = "GBK";
		if (testEncoding(s)) {
			encoding = s;
		}
		if (encoding == null) {
			encoding = "ASCII";
		}
		//
		dontNeed = new boolean[256];
		int i;
		for (i = 0; i < 256; i++)
			dontNeed[i] = false;
		for (i = 'a'; i <= 'z'; i++)
			dontNeed[i] = true;
		for (i = 'A'; i <= 'Z'; i++)
			dontNeed[i] = true;
		for (i = '0'; i <= '9'; i++)
			dontNeed[i] = true;
		dontNeed[(int) ' '] = true;
		dontNeed[(int) '-'] = true;
		dontNeed[(int) '_'] = true;
		dontNeed[(int) '.'] = true;
		dontNeed[(int) '*'] = true;
	}

	/**
	 * 仅供测试使用
	 * 
	 * @param args
	 *            命令行参数
	 */
	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String s = args[i];

			if (s.equals("-e")) {
				encoding = args[++i];
				continue;
			}
			if (s.equals("-d")) {
				System.out.println(decode(args[++i]));
				continue;
			}
			System.out.println(encode(s));
		}
	}

	/**
	 * 测试在当前JVM中，指定的编码字符集是否可用
	 * 
	 * @param enc
	 *            待测试的编码字符集
	 * @return 如果可用返回<code>true</code>，否则返回<code>false</code>。
	 */
	private final static boolean testEncoding(String enc) {
		if (enc != null) {
			try {
				"test".getBytes(enc);
				return true;
			} catch (Exception e) {
				// ignore
			}
		}
		return false;
	}

	/**
	 * 永远不能实例化
	 */
	private URLCoder() {
		super();
	}

}