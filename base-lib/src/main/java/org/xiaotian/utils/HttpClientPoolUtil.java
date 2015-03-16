package org.xiaotian.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class HttpClientPoolUtil {

	private final static Logger logger = Logger.getLogger(HttpClientPoolUtil.class);
	private static PoolingHttpClientConnectionManager connManager = null;
	private static CloseableHttpClient httpclient = null;

	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 50000;

	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 8000;
	/**
	 * 连接超时时间
	 */
	public final static int CONNECT_TIMEOUT = 5000;
	/**
	 * 数据传输超时时间
	 */
	public final static int SOCKET_TIMEOUT = 5000;
	/**
	 * 获取连接的最大等待时间
	 */
	public final static int CONNECT_REQUEST_TIMEOUT = 5000;

	/**
	 * 默认编码
	 */
	public final static String DEFAULT_CHARSET = "utf-8";

	static {
		try {
			SSLContext sslContext = SSLContexts.custom().useTLS().build();
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} }, null);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sslContext)).build();

			connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

			httpclient = HttpClients.custom().setConnectionManager(connManager).build();
			// Create socket configuration
			SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
			connManager.setDefaultSocketConfig(socketConfig);
			// Create message constraints
			MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000).build();
			// Create connection configuration
			ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
					.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).setMessageConstraints(messageConstraints).build();
			connManager.setDefaultConnectionConfig(connectionConfig);
			// 设置最大连接数
			connManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
			// 设置每个路由最大基础连接数
			connManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
		} catch (KeyManagementException e) {
			logger.error("KeyManagementException", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException", e);
		}
	}

	// 自定义响应处理
	private static ResponseHandler<String> responseStringHandler = new ResponseHandler<String>() {
		public synchronized String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			String responseBody = "";
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode >= 300) {
				throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
			}
			if (HttpStatus.SC_OK == statusCode) {
				HttpEntity entity = response.getEntity();
				if (null == entity) {
					throw new ClientProtocolException("Response contains no content");
				} else {
					responseBody = getResponseBody(entity);
				}
			}
			return responseBody;
		}
	};

	// 自定义响应处理
	private static ResponseHandler<HttpResponse> responseHandler = new ResponseHandler<HttpResponse>() {
		public synchronized HttpResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			HttpEntity entity = response.getEntity();
			if (null == entity) {
				throw new ClientProtocolException("Response contains no content");
			} else {
				StringEntity stringEntity = new StringEntity(getResponseBody(entity), DEFAULT_CHARSET);
				response.setEntity(stringEntity);
			}
			return response;
		}
	};

	// 默认请求requestConfig配置
	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT)
			.setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).build();

	/**
	 * 获取CloseableHttpClient实例
	 * 
	 * @return
	 */
	public static HttpClient getHttpClient() {
		return httpclient;
	}
	
	/**
	 * 获取HttpCient对应HttpClientConnectionManager实例
	 * 
	 * @return
	 */
	public static PoolingHttpClientConnectionManager getConnectionManager(){
		return connManager;
	}

	/**
	 * post json请求
	 * 
	 * @param url
	 * @param timeout
	 * @param map
	 * @param encoding
	 * @return 若请求失败，返回空字符串
	 */
	public static String postJsonBody(String url, int timeout, Map<String, Object> map, String encoding) {
		HttpPost post = new HttpPost(url);
		try {
			post.setHeader("Content-type", "application/json");
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
					.setConnectionRequestTimeout(timeout).setExpectContinueEnabled(false).build();
			post.setConfig(requestConfig);

			String params = new JSONObject(map).toString();
			post.setEntity(new StringEntity(params));
			logger.debug("[HttpUtils Post] begin invoke url:" + url + " , params:" + params);
			CloseableHttpResponse response = httpclient.execute(post);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						String result = EntityUtils.toString(entity, encoding);
						logger.debug("[HttpUtils Post]Debug response, url :" + url + " , response string :" + result);
						return result;
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException", e);
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
			post.releaseConnection();
		}
		return "";
	}

	@SuppressWarnings("deprecation")
	public static String invokeGet(String url, Map<String, String> params, String encode, int connectTimeout, int soTimeout) {
		String responseString = null;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeout).setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout).build();

		StringBuilder sb = new StringBuilder();
		sb.append(url);
		int i = 0;
		for (Entry<String, String> entry : params.entrySet()) {
			if (i == 0 && !url.contains("?")) {
				sb.append("?");
			} else {
				sb.append("&");
			}
			sb.append(entry.getKey());
			sb.append("=");
			String value = entry.getValue();
			try {
				sb.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				logger.warn("encode http get params error, value is " + value, e);
				sb.append(URLEncoder.encode(value));
			}
			i++;
		}
		logger.info("[HttpUtils Get] begin invoke:" + sb.toString());
		HttpGet get = new HttpGet(sb.toString());
		get.setConfig(requestConfig);

		try {
			CloseableHttpResponse response = httpclient.execute(get);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						responseString = EntityUtils.toString(entity, encode);
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} catch (Exception e) {
				logger.error(String.format("[HttpUtils Get]get response error, url:%s", sb.toString()), e);
				return responseString;
			} finally {
				if (response != null) {
					response.close();
				}
			}
			logger.info(String.format("[HttpUtils Get]Debug url:%s , response string %s:", sb.toString(), responseString));
		} catch (SocketTimeoutException e) {
			logger.error(String.format("[HttpUtils Get]invoke get timout error, url:%s", sb.toString()), e);
			return responseString;
		} catch (Exception e) {
			logger.error(String.format("[HttpUtils Get]invoke get error, url:%s", sb.toString()), e);
		} finally {
			get.releaseConnection();
		}
		return responseString;
	}

	/**
	 * HTTPS请求，默认超时为5S
	 * 
	 * @param reqURL
	 * @param params
	 * @return
	 */
	public static String connectPostHttps(String reqURL, Map<String, String> params) {

		String responseContent = null;

		HttpPost httpPost = new HttpPost(reqURL);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT)
					.setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).build();

			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));
			httpPost.setConfig(requestConfig);
			// 绑定到请求 Entry
			for (Map.Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				// 执行POST请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, Consts.UTF_8);
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
			logger.info("requestURI : " + httpPost.getURI() + ", responseContent: " + responseContent);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		} finally {
			httpPost.releaseConnection();
		}
		return responseContent;

	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：Http GET 方法 返回类型为String
	 * </p>
	 * 
	 * @param url
	 *            请求URL
	 * @return String
	 * @throws HttpException
	 * @throws IOException
	 */
	public static String get(String url) throws HttpException, IOException {
		HttpClient httpClient = getHttpClient();
		HttpGet getMethod = getMethod(url);
		getMethod.setConfig(requestConfig);
		String responseBody = "";
		try {
			responseBody = httpClient.execute(getMethod, responseStringHandler);
		} finally {
			getMethod.releaseConnection();
		}
		return responseBody;
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：Http GET 方法 返回类型为HttpResponse
	 * </p>
	 * 
	 * @param url
	 *            请求URL
	 * @return HttpResponse
	 * @throws HttpException
	 * @throws IOException
	 */
	public static HttpResponse sendGetRequest(String url) throws HttpException, IOException {
		HttpClient httpClient = getHttpClient();
		HttpGet getMethod = getMethod(url);
		getMethod.setConfig(requestConfig);
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(getMethod, responseHandler);
		} finally {
			getMethod.releaseConnection();
		}
		return httpResponse;
	}

	/**
	 * <p class="detail">
	 * 功能：POST请求 返回响应体 String 内容
	 * </p>
	 * 
	 * @param url
	 *            请求地址
	 * @param postData
	 *            请求参数
	 * @return String
	 * @throws HttpException
	 */
	public static String post(String url, Map<String, String> postData) throws Exception {
		if (null == postData || postData.isEmpty()) {
			return get(url);
		}
		HttpPost postMethod = null;
		String responseBody = "";
		try {
			HttpClient httpClient = getHttpClient();
			postMethod = postMethod(url);
			postMethod.setConfig(requestConfig);
			Set<Entry<String, String>> entrySet = postData.entrySet();
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			Iterator<Entry<String, String>> it = entrySet.iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				formparams.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, DEFAULT_CHARSET);
			postMethod.setEntity(entity);
			responseBody = httpClient.execute(postMethod, responseStringHandler);
		} catch (Exception e) {
			throw e;
		} finally {
			postMethod.releaseConnection();
		}
		return responseBody;
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：POST请求 返回响应信息
	 * </p>
	 * 
	 * @param url
	 * @param postData
	 *            请求地址
	 * @return 请求参数
	 * @throws Exception
	 */
	public static HttpResponse sendPostRequest(String url, Map<String, String> postData) throws Exception {
		if (null == postData || postData.isEmpty()) {
			return sendGetRequest(url);
		}
		HttpPost postMethod = null;
		HttpResponse httpResponse = null;
		try {
			HttpClient httpClient = getHttpClient();
			postMethod = postMethod(url);
			postMethod.setConfig(requestConfig);
			Set<Entry<String, String>> entrySet = postData.entrySet();
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			Iterator<Entry<String, String>> it = entrySet.iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				formparams.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
			}
			if (null != formparams && formparams.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, DEFAULT_CHARSET);
				postMethod.setEntity(entity);
			}
			httpResponse = httpClient.execute(postMethod, responseHandler);
		} catch (Exception e) {
			throw e;
		} finally {
			postMethod.releaseConnection();
		}
		return httpResponse;
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：得到GET METHOD
	 * </p>
	 * 
	 * @param url
	 *            请求URL
	 * @return
	 */
	public static HttpGet getMethod(String url) {
		HttpGet getMethod = new HttpGet(url);
		getMethod.setConfig(requestConfig);
		getMethod.setHeaders(getHeaders());
		return getMethod;
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：得到POST METHOD
	 * </p>
	 * 
	 * @param url
	 *            请求URL
	 * @return
	 */
	public static HttpPost postMethod(String url) {
		HttpPost postMethod = new HttpPost(url);
		postMethod.setConfig(requestConfig);
		postMethod.setHeaders(getHeaders());
		return postMethod;
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：请求头信息
	 * </p>
	 * @date Jan 27, 2014
	 */
	private static Header[] getHeaders() {
		ArrayList<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Accept", "text/html, text/json, text/xml, html/text, */*"));
		headers.add(new BasicHeader("Accept-Language", "zh-cn,en-us,zh-tw,en-gb,en;"));
		headers.add(new BasicHeader("Accept-Charset", "gbk,gb2312,utf-8,BIG5,ISO-8859-1;"));
		headers.add(new BasicHeader("Connection", "keep-alive"));
		headers.add(new BasicHeader("Cache-Control", "no-cache"));
		headers.add(new BasicHeader("Accept-Encoding", "gzip"));
		headers.add(new BasicHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; CIBA)"));
		return headers.toArray(new Header[0]);
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：从响应体内容
	 * </p>
	 * @param httpResponse
	 *            httpClient 响应
	 * @return 响应体内容
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static String getResponseBody(HttpEntity entity) throws IllegalStateException, IOException {
		StringBuffer respnseBody = new StringBuffer();
		BufferedReader in = null;
		try {
			InputStream is = entity.getContent();
			in = new BufferedReader(new InputStreamReader(is, DEFAULT_CHARSET));
			String line = null;
			while (null != (line = in.readLine())) {
				respnseBody.append(line);
			}
		} catch (IllegalStateException e1) {
			throw e1;
		} catch (IOException e2) {
			throw e2;
		} finally {
			in.close();
		}
		return respnseBody.toString();
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：从响应中得到响应体内容
	 * </p>
	 * @param httpResponse
	 *            httpClient 响应
	 * @return 响应体内容
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static String getResponseBody(HttpResponse httpResponse) throws IllegalStateException, IOException {
		String respnseBody = "";
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = httpResponse.getEntity();
			if (null != entity) {
				respnseBody = getResponseBody(entity);
			}
		}
		return respnseBody;
	}

}