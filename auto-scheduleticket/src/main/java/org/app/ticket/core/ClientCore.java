package org.app.ticket.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.app.ticket.bean.LoginDomain;
import org.app.ticket.bean.OrderRequest;
import org.app.ticket.bean.TrainQueryInfo;
import org.app.ticket.bean.UserInfo;
import org.app.ticket.constants.Constants;
import org.app.ticket.util.StringUtil;
import org.app.ticket.util.TicketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订票核心请求类
 * 
 * @Title: ClientCore.java
 * @Description: org.app.ticket.core
 * @Package org.app.ticket.core
 * @author hncdyj123@163.com
 * @date 2012-9-26
 * @version V1.0
 * 
 */
public class ClientCore {

	private static final Logger logger = LoggerFactory.getLogger(ClientCore.class);

	private static X509TrustManager tm = new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

	public static HttpClient getHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
		SSLContext sslcontext = SSLContext.getInstance("TLS");
		sslcontext.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory ssf = new SSLSocketFactory(sslcontext);
		ClientConnectionManager ccm = new DefaultHttpClient().getConnectionManager();
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", 443, ssf));
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 8000);
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 8000);
		HttpClient httpclient = new DefaultHttpClient(ccm, params);
		httpclient.getParams().setParameter(HTTP.USER_AGENT, "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)");
		return httpclient;
	}

	/**
	 * 创建post请求
	 * 
	 * @param url
	 * @return HttpPost
	 */
	public static HttpPost getHttpPost(String url) {
		// 创建post请求
		HttpPost post = new HttpPost(url);
		if (!StringUtil.isEmptyString(Constants.JSESSIONID_VALUE) && !StringUtil.isEmptyString(Constants.BIGIPSERVEROTSWEB_VALUE)) {
			// 放入session
			post.addHeader("Cookie", "JSESSIONID=" + Constants.JSESSIONID_VALUE + ";BIGipServerotsweb=" + Constants.BIGIPSERVEROTSWEB_VALUE + ";");
		}
		return post;
	}

	/**
	 * 创建get请求
	 * 
	 * @param url
	 * @return HttpGet
	 */
	public static HttpGet getHttpGet(String url) {
		// 创建get请求
		HttpGet get = new HttpGet(url);
		if (!StringUtil.isEmptyString(Constants.JSESSIONID_VALUE) && !StringUtil.isEmptyString(Constants.BIGIPSERVEROTSWEB_VALUE)) {
			// 放入session
			get.addHeader("Cookie", "JSESSIONID=" + Constants.JSESSIONID_VALUE + ";BIGipServerotsweb=" + Constants.BIGIPSERVEROTSWEB_VALUE + ";");
		}
		return get;
	}

	public static void getCookie() throws KeyManagementException, NoSuchAlgorithmException {
		logger.debug("-------------------getCookie start-------------------");
		HttpClient httpclient = getHttpClient();
		HttpGet get = getHttpGet("http://dynamic.12306.cn/otsweb/main.jsp");
		try {
			HttpResponse response = httpclient.execute(get);
			String responseBody = readInputStream(response.getEntity().getContent());
			// 获取消息头的信息
			Header[] headers = response.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				if (headers[i].getName().equals("Set-Cookie")) {
					String cookie = headers[i].getValue();
					String cookieName = cookie.split("=")[0];
					String cookieValue = cookie.split("=")[1].split(";")[0];
					if (cookieName.equals(Constants.JSESSIONID)) {
						Constants.JSESSIONID_VALUE = cookieValue;
					}
					if (cookieName.equals(Constants.BIGIPSERVEROTSWEB)) {
						Constants.BIGIPSERVEROTSWEB_VALUE = cookieValue;
					}
				}
			}
			logger.debug("jessionid = " + Constants.JSESSIONID_VALUE + ";bigipserverotsweb = " + Constants.BIGIPSERVEROTSWEB_VALUE);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		logger.debug("-------------------getCookie end-------------------");
	}

	public static String loginAysnSuggest() throws KeyManagementException, NoSuchAlgorithmException {
		logger.debug("-------------------loginAysnSuggest start-------------------");
		// 创建客户端
		HttpClient httpclient = getHttpClient();
		HttpPost post = getHttpPost(Constants.POST_UTL_LOGINACTION_LOGINAYSNSUGGEST);

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = null;
		try {
			responseBody = httpclient.execute(post, responseHandler);
			logger.debug(responseBody);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		logger.debug("-------------------loginAysnSuggest end-------------------");
		return responseBody;
	}

	public static String Login(LoginDomain loginDomain) throws KeyManagementException, NoSuchAlgorithmException {
		logger.debug("-------------------Login start-------------------");
		// 创建客户端
		HttpClient httpclient = getHttpClient();
		HttpPost post = getHttpPost(Constants.POST_UTL_LOGINACTION);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(Constants.LOGIN_LOGINRAND, loginDomain.getLoginRand()));
		parameters.add(new BasicNameValuePair(Constants.LOGIN_USERNAME, loginDomain.getUser_name()));
		parameters.add(new BasicNameValuePair(Constants.LOGIN_NAMEERRORFOCUS, loginDomain.getNameErrorFocus()));
		parameters.add(new BasicNameValuePair(Constants.LOGIN_PASSWORDERRORFOCUS, loginDomain.getPasswordErrorFocus()));
		parameters.add(new BasicNameValuePair(Constants.LOGIN_RANDCODE, loginDomain.getRandCode()));
		parameters.add(new BasicNameValuePair(Constants.LOGIN_RANDERRORFOCUS, loginDomain.getLoginRand()));
		parameters.add(new BasicNameValuePair(Constants.LOGIN_REFUNDFLAG, loginDomain.getRefundFlag()));
		parameters.add(new BasicNameValuePair(Constants.LOGIN_REFUNDLOGIN, loginDomain.getRefundLogin()));
		parameters.add(new BasicNameValuePair(Constants.LOGIN_PASSWORD, loginDomain.getPassword()));

		String responseBody = null;
		try {
			UrlEncodedFormEntity uef = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
			post.setEntity(uef);
			logger.debug(Constants.POST_UTL_LOGINACTION + Constants.SIGN + URLEncodedUtils.format(parameters, HTTP.UTF_8));
			HttpResponse response = httpclient.execute(post);
			HttpEntity entity = response.getEntity();
			responseBody = readInputStream(entity.getContent());
			logger.debug(responseBody);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		logger.debug("-------------------Login end-------------------");
		return responseBody;
	}

	// /**
	// * 获取TOKEN 提交订单需要
	// *
	// * @return String
	// * @throws KeyManagementException
	// * @throws NoSuchAlgorithmException
	// */
	// public static void getToken() throws KeyManagementException,
	// NoSuchAlgorithmException {
	// logger.debug("-------------------getToken start-------------------");
	// HttpClient httpclient = getHttpClient();
	// HttpGet get = getHttpGet(Constants.GET_URL_USERTOKEN);
	// ResponseHandler<String> responseHandler = new BasicResponseHandler();
	// try {
	// String responseBody = httpclient.execute(get, responseHandler);
	// logger.debug("Respone is " + responseBody);
	// TicketUtil.getToken(responseBody);
	// } catch (Exception e) {
	// logger.warn(e.getMessage());
	// e.printStackTrace();
	// } finally {
	// httpclient.getConnectionManager().shutdown();
	// }
	// logger.debug("-------------------getToken end-------------------");
	// }

	/**
	 * 查询列车信息
	 * 
	 * @param from
	 *            起始地点
	 * @param to
	 *            目标地点
	 * @param startDate
	 *            开车日期
	 * @param rangDate
	 *            查询时间区间段
	 * @return List<TrainQueryInfo>
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static List<TrainQueryInfo> queryTrain(OrderRequest orderRequest) throws KeyManagementException, NoSuchAlgorithmException {
		logger.debug("-------------------query train start-------------------");
		// 创建客户端
		HttpClient httpclient = getHttpClient();
		if (StringUtil.isEmptyString(orderRequest.getStart_time_str())) {
			orderRequest.setStart_time_str("00:00--24:00");
		}
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(Constants.QUERY_INCLUDESTUDENT, orderRequest.getIncludeStudent()));
		parameters.add(new BasicNameValuePair(Constants.QUERY_FROM_STATION_TELECODE, TicketUtil.getCityCode(orderRequest.getFrom())));
		parameters.add(new BasicNameValuePair(Constants.QUERY_START_TIME_STR, orderRequest.getStart_time_str()));
		parameters.add(new BasicNameValuePair(Constants.QUERY_TO_STATION_TELECODE, TicketUtil.getCityCode(orderRequest.getTo())));
		parameters.add(new BasicNameValuePair(Constants.QUERY_TRAIN_DATE, orderRequest.getTrain_date()));
		parameters.add(new BasicNameValuePair(Constants.QUERY_TRAIN_NO, orderRequest.getTrain_no()));
		parameters.add(new BasicNameValuePair(Constants.QUERY_SEATTYPEANDNUM, orderRequest.getSeatTypeAndNum()));
		parameters.add(new BasicNameValuePair(Constants.QUERY_TRAINCLASS, orderRequest.getTrainClass()));
		parameters.add(new BasicNameValuePair(Constants.QUERY_TRAINPASSTYPE, orderRequest.getTrainPassType()));
		logger.info(Constants.GET_URL_QUERYTICKET + Constants.SIGN + URLEncodedUtils.format(parameters, HTTP.UTF_8));
		HttpGet get = getHttpGet(Constants.GET_URL_QUERYTICKET + Constants.SIGN + URLEncodedUtils.format(parameters, HTTP.UTF_8));
		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		String responseBody = null;
		List<TrainQueryInfo> all = new ArrayList<TrainQueryInfo>();
		try {
			responseBody = httpclient.execute(get, responseHandler);
			all = TicketUtil.parserQueryInfo(responseBody);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		logger.debug("-------------------query train end-------------------");
		return all;
	}

	/**
	 * 提交预定的车次
	 * 
	 * @param trainQueryInfo
	 * @param orderRequest
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static void submitOrderRequest(TrainQueryInfo trainQueryInfo, OrderRequest orderRequest) throws KeyManagementException, NoSuchAlgorithmException {
		logger.debug("-------------------submitOrderRequest start-------------------");
		// 创建客户端
		HttpClient httpclient = getHttpClient();
		HttpPost post = getHttpPost(Constants.POST_URL_SUBMUTORDERREQUEST);
		// 提交预定的车次 一共24个参数
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(Constants.ORDER_ARRIVE_TIME, trainQueryInfo.getEndTime()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_FROM_STATION_NAME, trainQueryInfo.getFromStation()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_FROM_STATION_NO, trainQueryInfo.getFormStationNo()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_FROM_STATION_TELECODE, trainQueryInfo.getFromStationCode()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_FROM_STATION_TELECODE_NAME, trainQueryInfo.getFromStation()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_INCLUDE_STUDENT, orderRequest.getIncludeStudent()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_LISHI, trainQueryInfo.getTakeTime()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_MMSTR, trainQueryInfo.getMmStr()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_ROUND_START_TIME_STR, orderRequest.getStart_time_str()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_ROUND_TRAIN_DATE, orderRequest.getQuery_date()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_SEATTYPE_NUM, orderRequest.getSeatTypeAndNum()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_SINGLE_ROUND_TYPE, trainQueryInfo.getSingle_round_type()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_START_TIME_STR, orderRequest.getStart_time_str()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_STATION_TRAIN_CODE, trainQueryInfo.getTrainNo()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_TO_STATION_NAME, trainQueryInfo.getToStation()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_TO_STATION_NO, trainQueryInfo.getToStationNo()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_TO_STATION_TELECODE, trainQueryInfo.getToStationCode()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_TO_STATION_TELECODE_NAME, trainQueryInfo.getToStation()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_TRAIN_CLASS_ARR, orderRequest.getTrainClass()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_TRAIN_DATE, orderRequest.getTrain_date()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_TRAIN_PASS_TYPE, orderRequest.getTrainPassType()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_TRAIN_START_TIME, trainQueryInfo.getStartTime()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_TRAINNO4, trainQueryInfo.getTrainno4()));
		parameters.add(new BasicNameValuePair(Constants.ORDER_YPINFODETAIL, trainQueryInfo.getYpInfoDetail()));
		String responseBody;
		try {
			UrlEncodedFormEntity uef = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
			logger.debug(Constants.POST_URL_CONFIRMSINGLEFORQUEUEORDER + Constants.SIGN + URLEncodedUtils.format(parameters, HTTP.UTF_8));
			post.setEntity(uef);
			HttpResponse response = httpclient.execute(post);
			HttpEntity entity = response.getEntity();
			responseBody = readInputStream(entity.getContent());
			logger.debug(responseBody);
			int statusCode = response.getStatusLine().getStatusCode();
			logger.debug("statusCode = " + statusCode);
			// 返回码 301 或 302 转发到location的新地址
			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				Header locationHeader = response.getFirstHeader("location");
				String redirectUrl = locationHeader.getValue();
				post = getHttpPost(redirectUrl);
				response = httpclient.execute(post);
				entity = response.getEntity();
				responseBody = readInputStream(entity.getContent());
				TicketUtil.getCredential(responseBody);
				TicketUtil.getToken(responseBody);
				logger.debug(responseBody);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		logger.debug("-------------------submitOrderRequest end-------------------");
	}

	/**
	 * 获取提交令牌和我的联系人
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static void confirmPassenger() throws KeyManagementException, NoSuchAlgorithmException {
		logger.debug("-------------------confirmPassenger start-------------------");
		// 创建客户端
		HttpClient httpclient = getHttpClient();
		HttpGet get = getHttpGet(Constants.GET_URL_CONFIRMPASSENGER);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			String responseBody = httpclient.execute(get, responseHandler);
			logger.info("Response is " + responseBody);
			TicketUtil.getCredential(responseBody);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		logger.debug("-------------------confirmPassenger end-------------------");

	}

	/**
	 * 获取提交订单时验证码
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static void getPassCode(String url, String path) throws KeyManagementException, NoSuchAlgorithmException {
		logger.debug("-------------------getPassCode start-------------------");
		// 创建客户端
		HttpClient httpclient = getHttpClient();
		HttpGet get = getHttpGet(url);
		try {
			HttpResponse response = httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				OutputStream out = new FileOutputStream(new File(path));
				int byteread = 0;
				byte[] tmp = new byte[1];
				while ((byteread = instream.read(tmp)) != -1) {
					out.write(tmp);
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		logger.debug("-------------------getPassCode end-------------------");
	}

	public static void getQueueCount(String url, OrderRequest req, List<UserInfo> userInfos, TrainQueryInfo trainQueryInfo) throws KeyManagementException, NoSuchAlgorithmException {
		logger.debug("-------------------getQueueCount start-------------------");
		// 创建客户端
		HttpClient httpclient = getHttpClient();
		String responseBody = null;
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(Constants.GETQUEUECOUNT_FROM, TicketUtil.getCityCode(req.getFrom())));
		parameters.add(new BasicNameValuePair(Constants.GETQUEUECOUNT_SEAT, userInfos.get(0).getSeatType()));
		parameters.add(new BasicNameValuePair(Constants.GETQUEUECOUNT_STATION, trainQueryInfo.getTrainNo()));
		parameters.add(new BasicNameValuePair(Constants.GETQUEUECOUNT_TICKET, Constants.LEFTTICKETSTR));
		parameters.add(new BasicNameValuePair(Constants.GETQUEUECOUNT_TO, TicketUtil.getCityCode(req.getTo())));
		parameters.add(new BasicNameValuePair(Constants.GETQUEUECOUNT_TRAIN_DATE, req.getTrain_date()));
		logger.info(Constants.GET_URL_GETQUEUECOUNT + Constants.SIGN + URLEncodedUtils.format(parameters, HTTP.UTF_8));
		HttpGet get = getHttpGet(Constants.GET_URL_GETQUEUECOUNT + Constants.SIGN + URLEncodedUtils.format(parameters, HTTP.UTF_8));
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			responseBody = httpclient.execute(get, responseHandler);
			logger.debug(responseBody);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		logger.debug("-------------------getQueueCount end-------------------");
	}

	/**
	 * 提交订单信息
	 * 
	 * @param trainQueryInfo
	 * @param orderRequest
	 * @param userInfoList
	 * @param randCode
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static String confirmSingleForQueueOrder(TrainQueryInfo trainQueryInfo, OrderRequest orderRequest, List<UserInfo> userInfoList, String randCode) throws KeyManagementException, NoSuchAlgorithmException {
		
		
		
		checkOrderInfo(trainQueryInfo, orderRequest, userInfoList, randCode);
		getConfirmPassengerAction(trainQueryInfo, orderRequest, userInfoList, randCode);
		
		
		logger.debug("-------------------confirmSingleForQueueOrder start-------------------");
		// 创建客户端
		HttpClient httpclient = getHttpClient();
		
		HttpPost post = getHttpPost(Constants.POST_URL_CONFIRMSINGLEFORQUEUEORDER);
		
//		Accept	application/json, text/javascript, */*
//		Accept-Charset	GB2312,utf-8;q=0.7,*;q=0.7
//		Accept-Encoding	gzip, deflate
//		Accept-Language	zh-cn,zh;q=0.5
//		Cache-Control	no-cache
//		Connection	keep-alive
//		Content-Length	1319
//		Content-Type	application/x-www-form-urlencoded; charset=UTF-8
//		Cookie	JSESSIONID=B1C19E86F16DDA7C4213E6F5F4B8A381; BIGipServerotsweb=2664694026.62495.0000
//		Host	dynamic.12306.cn
//		Pragma	no-cache
//		Referer	https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=init
//		User-Agent	Mozilla/5.0 (Windows NT 5.1; rv:9.0.1) Gecko/20100101 Firefox/9.0.1
//		X-Requested-With	XMLHttpRequest
		
		post.setHeader("Accept", "application/json, text/javascript, */*");
		post.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
		post.setHeader("Accept-Encoding", "gzip, deflate");
		post.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
		post.setHeader("Cache-Control", "no-cache");
		post.setHeader("Connection", "keep-alive");
		//Content-Length	1319
		post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		post.setHeader("Host", "dynamic.12306.cn");
		post.setHeader("Pragma", "no-cache");
		post.setHeader("Referer", "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=init");
		post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:9.0.1) Gecko/20100101 Firefox/9.0.1");
		post.setHeader("X-Requested-With", "XMLHttpRequest");
		
		// 提交订单
		String orderUrl = "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=confirmSingleForQueueOrder";
//		Header referHeader = Utils.makeReferHeader("https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=init");
//		Header[] headers = Utils.buildHeader(referHeader, httpsCookieHeaders);
		Map<String, String> orderData = new HashMap<String, String>();
		
		String leftTicketToken = Constants.LEFTTICKETSTR;
		
		orderData.put("checkbox0", "0");
		//orderData.put("checkbox0", "0");
		orderData.put("checkbox9", "Y");
		//orderData.put("checkbox9=Y", "");
		orderData.put("checkbox9", "Y");
		//orderData.put("checkbox9=Y", "");
		orderData.put("checkbox9", "Y");
		//orderData.put("checkbox9=Y", "");
		orderData.put("checkbox9", "Y");
		//orderData.put("checkbox9=Y", "");
		orderData.put("checkbox9", "Y");
		//orderData.put("checkbox9=Y", "");
		orderData.put("leftTicketStr", leftTicketToken);
		//1019203104405370002610192000233033900043
		orderData.put("oldPassengers", userInfoList.get(0).getSimpleText());
//		orderData.put("oldPassengers", Utils.getPassengerTickets4oldPassengers(orderData));
		//orderData.put("oldPassengers=韦胜迪,1,452723198604122813", Utils.getPassengerTickets4oldPassengers(orderData));
		orderData.put("oldPassengers", "");
		//orderData.put("oldPassengers=", "");
		orderData.put("oldPassengers", "");
		//orderData.put("oldPassengers=", "");
		orderData.put("oldPassengers", "");
		//orderData.put("oldPassengers=", "");
		orderData.put("oldPassengers", "");
		//orderData.put("oldPassengers=", "");
		orderData.put("orderRequest.bed_level_order_num", "000000000000000000000000000000");
		//orderData.put("orderRequest.bed_level_order_num", "000000000000000000000000000000");
		orderData.put("orderRequest.cancel_flag", "1");
		//orderData.put("orderRequest.cancel_flag=1", "1");
		orderData.put("orderRequest.end_time",trainQueryInfo.getEndTime());
		//orderData.put("orderRequest.end_time=17:37", ticketinfoArr[1]);
		orderData.put("orderRequest.from_station_name", trainQueryInfo.getFromStation());
		//orderData.put("orderRequest.from_station_name=杭州南", pb.getFromStationName());
		orderData.put("orderRequest.from_station_telecode", trainQueryInfo.getFromStationCode());
		//orderData.put("orderRequest.from_station_telecode=XHH", StationNameUtil.getStationTelecode(pb.getFromStationName()));
		orderData.put("orderRequest.id_mode", "Y");
		//orderData.put("orderRequest.id_mode=Y", "Y");
		orderData.put("orderRequest.reserve_flag", "A");
		//orderData.put("orderRequest.reserve_flag=A", "A");
		orderData.put("orderRequest.seat_detail_type_code", "");
		//orderData.put("orderRequest.seat_detail_type_code=", "");
		orderData.put("orderRequest.seat_type_code", "");
		//orderData.put("orderRequest.seat_type_code=", "");
		orderData.put("orderRequest.start_time", trainQueryInfo.getStartTime());
		//orderData.put("orderRequest.start_time=19:01", ticketinfoArr[0]);
		orderData.put("orderRequest.station_train_code", trainQueryInfo.getTrainNo());
		//orderData.put("orderRequest.station_train_code=K537", pb.getTrainCode());
		orderData.put("orderRequest.ticket_type_order_num", "");
		//orderData.put("orderRequest.ticket_type_order_num=", "");
		orderData.put("orderRequest.to_station_name", trainQueryInfo.getToStation());
		//orderData.put("orderRequest.to_station_name=柳州", pb.getToStationName());
		orderData.put("orderRequest.to_station_telecode", trainQueryInfo.getToStationCode());
		//orderData.put("orderRequest.to_station_telecode=LZZ", StationNameUtil.getStationTelecode(pb.getToStationName()));
		orderData.put("orderRequest.train_date", orderRequest.getTrain_date());
		//orderData.put("orderRequest.train_date=2013-01-09", pb.getTicketDate());
		orderData.put("orderRequest.train_no", trainQueryInfo.getTrainno4());
		//orderData.put("orderRequest.train_no=550000K53752", pb.getTrainNo());
		orderData.put("org.apache.struts.taglib.html.TOKEN", Constants.TOKEN);
		orderData.put("passengerTickets", userInfoList.get(0).getText());
		//orderData.put("passengerTickets=1,0,1,韦胜迪,1,452723198604122813,15201386005,Y", Utils.getPassengerTickets(orderData));
		//orderData.put("passengerTickets=3,0,1,韦胜迪,1,452723198604122813,15201386005,Y", Utils.getPassengerTickets(orderData));
		orderData.put("passenger_1_cardno", userInfoList.get(0).getCardID());
		//orderData.put("passenger_1_cardno=452723198604122813", pb.getIdcard());
		orderData.put("passenger_1_cardtype", "1");// 身份证
		//orderData.put("passenger_1_cardtype=1", "1");// 身份证
		orderData.put("passenger_1_mobileno", userInfoList.get(0).getPhone());// 手机号
		//orderData.put("passenger_1_mobileno=15201386005", pb.getTel());// 手机号
		orderData.put("passenger_1_name", userInfoList.get(0).getName());// 姓名
		//orderData.put("passenger_1_name=韦胜迪", pb.getName());// 姓名
		
		orderData.put("passenger_1_seat", userInfoList.get(0).getSeatType());// 座位类别
		//orderData.put("passenger_1_seat=1", buySeatNo);// 座位类别
		orderData.put("passenger_1_seat_detail", "0");// 上下铺，0表示随机
		//orderData.put("passenger_1_seat_detail=0", "0");// 上下铺，0表示随机
		orderData.put("passenger_1_seat_detail_select", "0");
		//orderData.put("passenger_1_seat_detail_select=0", "0");
		orderData.put("passenger_1_ticket", "1");// 票数目？
		//orderData.put("passenger_1_ticket=1", "1");// 票数目？
		orderData.put("randCode", randCode);
		orderData.put("textfield", "中文或拼音首字母");
		//textfield=中文或拼音首字母
		//orderData.put("randCode=SHCA", orderRandCode);
		//-----------------
		
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		for(Map.Entry<String,String> entry : orderData.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			parameters.add(new BasicNameValuePair(key,value ));
		}
		
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
	
		
		for (int i = 0; i < parameters.size(); i++) {
			NameValuePair  v = parameters.get(i);
			logger.info(v.getName() + ":" + v.getValue());
		}
		
		
		logger.info("提交参数结束------------------------");
		String responseBody = "";
		try {
			UrlEncodedFormEntity uef = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
			logger.debug(Constants.POST_URL_CONFIRMSINGLEFORQUEUEORDER + Constants.SIGN + URLEncodedUtils.format(parameters, HTTP.UTF_8));
			post.setEntity(uef);
			responseBody = httpclient.execute(post, responseHandler);
			logger.info("Response is " + responseBody);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		
		logger.debug("-------------------confirmSingleForQueueOrder end-------------------");
		return responseBody;
	}
	
	/**
	 * 提交订单信息
	 * 
	 * @param trainQueryInfo
	 * @param orderRequest
	 * @param userInfoList
	 * @param randCode
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static String confirmSingleForQueueOrder_bak(TrainQueryInfo trainQueryInfo, OrderRequest orderRequest, List<UserInfo> userInfoList, String randCode) throws KeyManagementException, NoSuchAlgorithmException {
		
		
		checkOrderInfo(trainQueryInfo, orderRequest, userInfoList, randCode);
		getConfirmPassengerAction(trainQueryInfo, orderRequest, userInfoList, randCode);
		
		
		logger.debug("-------------------confirmSingleForQueueOrder start-------------------");
		// 创建客户端
		HttpClient httpclient = getHttpClient();

		HttpPost post = getHttpPost(Constants.POST_URL_CONFIRMSINGLEFORQUEUEORDER);
		
//		Accept	application/json, text/javascript, */*
//		Accept-Charset	GB2312,utf-8;q=0.7,*;q=0.7
//		Accept-Encoding	gzip, deflate
//		Accept-Language	zh-cn,zh;q=0.5
//		Cache-Control	no-cache
//		Connection	keep-alive
//		Content-Length	1319
//		Content-Type	application/x-www-form-urlencoded; charset=UTF-8
//		Cookie	JSESSIONID=B1C19E86F16DDA7C4213E6F5F4B8A381; BIGipServerotsweb=2664694026.62495.0000
//		Host	dynamic.12306.cn
//		Pragma	no-cache
//		Referer	https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=init
//		User-Agent	Mozilla/5.0 (Windows NT 5.1; rv:9.0.1) Gecko/20100101 Firefox/9.0.1
//		X-Requested-With	XMLHttpRequest
		
		post.setHeader("Accept", "application/json, text/javascript, */*");
		post.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
		post.setHeader("Accept-Encoding", "gzip, deflate");
		post.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
		post.setHeader("Cache-Control", "no-cache");
		post.setHeader("Connection", "keep-alive");
		//Content-Length	1319
		post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		post.setHeader("Host", "dynamic.12306.cn");
		post.setHeader("Pragma", "no-cache");
		post.setHeader("Referer", "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=init");
		post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:9.0.1) Gecko/20100101 Firefox/9.0.1");
		post.setHeader("X-Requested-With", "XMLHttpRequest");
		

		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		
		logger.info("提交参数开始------------------------");

		parameters.add(new BasicNameValuePair("checkbox9", "Y"));
		parameters.add(new BasicNameValuePair("checkbox9", "Y"));
		parameters.add(new BasicNameValuePair("checkbox9", "Y"));
		parameters.add(new BasicNameValuePair("checkbox9", "Y"));
		parameters.add(new BasicNameValuePair("checkbox9", "Y"));
		for (int i = 0; i < (5 - userInfoList.size()); i++) {
			parameters.add(new BasicNameValuePair(Constants.SUBMIT_OLDPASSENGERS, ""));
		}
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_LEFTTICKETSTR, Constants.LEFTTICKETSTR));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_BED_LEVEL_ORDER_NUM, "000000000000000000000000000000"));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_CANCEL_FLAG, "1"));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_END_TIME, trainQueryInfo.getEndTime()));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_FROM_STATION_NAME, trainQueryInfo.getFromStation()));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_FROM_STATION_TELECODE, trainQueryInfo.getFromStationCode()));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_ID_MODE, "Y"));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_RESERVE_FLAG, "A"));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_SEAT_DETAIL_TYPE_CODE, ""));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_START_TIME, trainQueryInfo.getStartTime()));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_STATION_TRAIN_CODE, trainQueryInfo.getTrainNo()));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_TICKET_TYPE_ORDER_NUM, ""));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_TO_STATION_NAME, trainQueryInfo.getToStation()));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_TO_STATION_TELECODE, trainQueryInfo.getToStationCode()));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_TO_SEAT_TYPE_CODE, ""));
		parameters.add(new BasicNameValuePair(Constants.QUERY_TRAIN_DATE, orderRequest.getTrain_date()));
		parameters.add(new BasicNameValuePair(Constants.QUERY_TRAIN_NO, trainQueryInfo.getTrainno4()));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_TOKEN, Constants.TOKEN));
		String responseBody = "";
		for (int i = 0; i < userInfoList.size(); i++) {
			// TODO 循环添加联系人
			parameters.add(new BasicNameValuePair("checkbox" + i, Integer.toString(i)));
			// 几个联系人就添加几个订票信息
			parameters.add(new BasicNameValuePair(Constants.SUBMIT_OLDPASSENGERS, userInfoList.get(i).getSimpleText()));
			// TODO 循环添加乘车人信息
			parameters.add(new BasicNameValuePair("passenger_" + (i + 1) + "_cardno", userInfoList.get(i).getCardID()));
			parameters.add(new BasicNameValuePair("passenger_" + (i + 1) + "_cardtype", userInfoList.get(i).getCardType()));
			parameters.add(new BasicNameValuePair("passenger_" + (i + 1) + "_mobileno", userInfoList.get(i).getPhone()));
			parameters.add(new BasicNameValuePair("passenger_" + (i + 1) + "_name", userInfoList.get(i).getName()));
			parameters.add(new BasicNameValuePair("passenger_" + (i + 1) + "_seat", userInfoList.get(i).getSeatType()));
			parameters.add(new BasicNameValuePair("passenger_" + (i + 1) + "_seat_detail", "0"));
			parameters.add(new BasicNameValuePair("passenger_" + (i + 1) + "_ticket", userInfoList.get(i).getTickType()));
			parameters.add(new BasicNameValuePair(Constants.SUBMIT_PASSENGERTICKETS, userInfoList.get(i).getText()));
		}
		
		parameters.add(new BasicNameValuePair("passenger_1_seat_detail_select", "0"));
		
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_RANDCODE, randCode));
		parameters.add(new BasicNameValuePair(Constants.SUBMIT_TEXTFIELD, "中文或拼音首字母"));
		
		
		for (int i = 0; i < parameters.size(); i++) {
			NameValuePair  v = parameters.get(i);
			logger.info(v.getName() + ":" + v.getValue());
		}
		
		
		logger.info("提交参数结束------------------------");
		
		try {
			UrlEncodedFormEntity uef = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
			logger.debug(Constants.POST_URL_CONFIRMSINGLEFORQUEUEORDER + Constants.SIGN + URLEncodedUtils.format(parameters, HTTP.UTF_8));
			post.setEntity(uef);
			responseBody = httpclient.execute(post, responseHandler);
			logger.info("Response is " + responseBody);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		logger.debug("-------------------confirmSingleForQueueOrder end-------------------");
		return responseBody;
	}
	
	public static String checkOrderInfo(TrainQueryInfo trainQueryInfo, OrderRequest orderRequest, List<UserInfo> userInfoList, String randCode) throws KeyManagementException, NoSuchAlgorithmException{
		
		
		// 创建客户端
		HttpClient httpclient = getHttpClient();
		
		String url = "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=checkOrderInfo&rand=" + randCode;

		HttpPost post = getHttpPost(url);
		
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		
		
		logger.debug("-------------------confirmSingleForQueueOrder start-------------------");
		// 创建客户端
		
		
//		Accept	application/json, text/javascript, */*
//		Accept-Charset	GB2312,utf-8;q=0.7,*;q=0.7
//		Accept-Encoding	gzip, deflate
//		Accept-Language	zh-cn,zh;q=0.5
//		Cache-Control	no-cache
//		Connection	keep-alive
//		Content-Length	1319
//		Content-Type	application/x-www-form-urlencoded; charset=UTF-8
//		Cookie	JSESSIONID=B1C19E86F16DDA7C4213E6F5F4B8A381; BIGipServerotsweb=2664694026.62495.0000
//		Host	dynamic.12306.cn
//		Pragma	no-cache
//		Referer	https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=init
//		User-Agent	Mozilla/5.0 (Windows NT 5.1; rv:9.0.1) Gecko/20100101 Firefox/9.0.1
//		X-Requested-With	XMLHttpRequest
		
		post.setHeader("Accept", "application/json, text/javascript, */*");
		post.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
		post.setHeader("Accept-Encoding", "gzip, deflate");
		post.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
		post.setHeader("Cache-Control", "no-cache");
		post.setHeader("Connection", "keep-alive");
		//Content-Length	1319
		post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		post.setHeader("Host", "dynamic.12306.cn");
		post.setHeader("Pragma", "no-cache");
		post.setHeader("Referer", "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=init");
		post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:9.0.1) Gecko/20100101 Firefox/9.0.1");
		post.setHeader("X-Requested-With", "XMLHttpRequest");
		
		// 提交订单
		String orderUrl = "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=confirmSingleForQueueOrder";
//		Header referHeader = Utils.makeReferHeader("https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=init");
//		Header[] headers = Utils.buildHeader(referHeader, httpsCookieHeaders);
		Map<String, String> orderData = new HashMap<String, String>();
		
		String leftTicketToken = Constants.LEFTTICKETSTR;
		
		orderData.put("checkbox0", "0");
		//orderData.put("checkbox0", "0");
		orderData.put("checkbox9", "Y");
		//orderData.put("checkbox9=Y", "");
		orderData.put("checkbox9", "Y");
		//orderData.put("checkbox9=Y", "");
		orderData.put("checkbox9", "Y");
		//orderData.put("checkbox9=Y", "");
		orderData.put("checkbox9", "Y");
		//orderData.put("checkbox9=Y", "");
		orderData.put("checkbox9", "Y");
		//orderData.put("checkbox9=Y", "");
		orderData.put("leftTicketStr", leftTicketToken);
		//1019203104405370002610192000233033900043
		orderData.put("oldPassengers", userInfoList.get(0).getSimpleText());
//		orderData.put("oldPassengers", Utils.getPassengerTickets4oldPassengers(orderData));
		//orderData.put("oldPassengers=韦胜迪,1,452723198604122813", Utils.getPassengerTickets4oldPassengers(orderData));
		orderData.put("oldPassengers", "");
		//orderData.put("oldPassengers=", "");
		orderData.put("oldPassengers", "");
		//orderData.put("oldPassengers=", "");
		orderData.put("oldPassengers", "");
		//orderData.put("oldPassengers=", "");
		orderData.put("oldPassengers", "");
		//orderData.put("oldPassengers=", "");
		orderData.put("orderRequest.bed_level_order_num", "000000000000000000000000000000");
		//orderData.put("orderRequest.bed_level_order_num", "000000000000000000000000000000");
		orderData.put("orderRequest.cancel_flag", "1");
		//orderData.put("orderRequest.cancel_flag=1", "1");
		orderData.put("orderRequest.end_time",trainQueryInfo.getEndTime());
		//orderData.put("orderRequest.end_time=17:37", ticketinfoArr[1]);
		orderData.put("orderRequest.from_station_name", trainQueryInfo.getFromStation());
		//orderData.put("orderRequest.from_station_name=杭州南", pb.getFromStationName());
		orderData.put("orderRequest.from_station_telecode", trainQueryInfo.getFromStationCode());
		//orderData.put("orderRequest.from_station_telecode=XHH", StationNameUtil.getStationTelecode(pb.getFromStationName()));
		orderData.put("orderRequest.id_mode", "Y");
		//orderData.put("orderRequest.id_mode=Y", "Y");
		orderData.put("orderRequest.reserve_flag", "A");
		//orderData.put("orderRequest.reserve_flag=A", "A");
		orderData.put("orderRequest.seat_detail_type_code", "");
		//orderData.put("orderRequest.seat_detail_type_code=", "");
		orderData.put("orderRequest.seat_type_code", "");
		//orderData.put("orderRequest.seat_type_code=", "");
		orderData.put("orderRequest.start_time", trainQueryInfo.getStartTime());
		//orderData.put("orderRequest.start_time=19:01", ticketinfoArr[0]);
		orderData.put("orderRequest.station_train_code", trainQueryInfo.getTrainNo());
		//orderData.put("orderRequest.station_train_code=K537", pb.getTrainCode());
		orderData.put("orderRequest.ticket_type_order_num", "");
		//orderData.put("orderRequest.ticket_type_order_num=", "");
		orderData.put("orderRequest.to_station_name", trainQueryInfo.getToStation());
		//orderData.put("orderRequest.to_station_name=柳州", pb.getToStationName());
		orderData.put("orderRequest.to_station_telecode", trainQueryInfo.getToStationCode());
		//orderData.put("orderRequest.to_station_telecode=LZZ", StationNameUtil.getStationTelecode(pb.getToStationName()));
		orderData.put("orderRequest.train_date", orderRequest.getTrain_date());
		//orderData.put("orderRequest.train_date=2013-01-09", pb.getTicketDate());
		orderData.put("orderRequest.train_no", trainQueryInfo.getTrainno4());
		//orderData.put("orderRequest.train_no=550000K53752", pb.getTrainNo());
		orderData.put("org.apache.struts.taglib.html.TOKEN", Constants.TOKEN);
		orderData.put("passengerTickets", userInfoList.get(0).getText());
		//orderData.put("passengerTickets=1,0,1,韦胜迪,1,452723198604122813,15201386005,Y", Utils.getPassengerTickets(orderData));
		//orderData.put("passengerTickets=3,0,1,韦胜迪,1,452723198604122813,15201386005,Y", Utils.getPassengerTickets(orderData));
		orderData.put("passenger_1_cardno", userInfoList.get(0).getCardID());
		//orderData.put("passenger_1_cardno=452723198604122813", pb.getIdcard());
		orderData.put("passenger_1_cardtype", "1");// 身份证
		//orderData.put("passenger_1_cardtype=1", "1");// 身份证
		orderData.put("passenger_1_mobileno", userInfoList.get(0).getPhone());// 手机号
		//orderData.put("passenger_1_mobileno=15201386005", pb.getTel());// 手机号
		orderData.put("passenger_1_name", userInfoList.get(0).getName());// 姓名
		//orderData.put("passenger_1_name=韦胜迪", pb.getName());// 姓名
		
		orderData.put("passenger_1_seat", userInfoList.get(0).getSeatType());// 座位类别
		//orderData.put("passenger_1_seat=1", buySeatNo);// 座位类别
		orderData.put("passenger_1_seat_detail", "0");// 上下铺，0表示随机
		//orderData.put("passenger_1_seat_detail=0", "0");// 上下铺，0表示随机
		orderData.put("passenger_1_seat_detail_select", "0");
		//orderData.put("passenger_1_seat_detail_select=0", "0");
		orderData.put("passenger_1_ticket", "1");// 票数目？
		//orderData.put("passenger_1_ticket=1", "1");// 票数目？
		orderData.put("randCode", randCode);
		orderData.put("textfield", "中文或拼音首字母");
		orderData.put("tFlag", "dc");
		//textfield=中文或拼音首字母
		//orderData.put("randCode=SHCA", orderRandCode);
		//-----------------
		
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		for(Map.Entry<String,String> entry : orderData.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			parameters.add(new BasicNameValuePair(key,value ));
		}
		
	
		
		for (int i = 0; i < parameters.size(); i++) {
			NameValuePair  v = parameters.get(i);
			logger.info(v.getName() + ":" + v.getValue());
		}
		
		
		logger.info("提交参数结束------------------------");
		String responseBody = "";
		try {
			UrlEncodedFormEntity uef = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
			logger.debug(Constants.POST_URL_CONFIRMSINGLEFORQUEUEORDER + Constants.SIGN + URLEncodedUtils.format(parameters, HTTP.UTF_8));
			post.setEntity(uef);
			responseBody = httpclient.execute(post, responseHandler);
			logger.info("Response is " + responseBody);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		
		logger.debug("-------------------confirmSingleForQueueOrder end-------------------");
		return responseBody;
	}
	
	public static String getConfirmPassengerAction(TrainQueryInfo trainQueryInfo, OrderRequest orderRequest, List<UserInfo> userInfoList, String randCode) throws KeyManagementException, NoSuchAlgorithmException{
		
		
		// 创建客户端
		HttpClient httpclient = getHttpClient();

		String url = "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=getQueueCount";
//				"train_date=2013-01-23&" +
//				"train_no=5500000T8110&" +
//				"station=T81&" +
//				"seat=3&" +
//				"from=XHH&" +
//				"to=LZZ&" +
//				"ticket=1019203104405370002610192000233033900043";
		HttpPost post = getHttpPost(url);

		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		

		parameters.add(new BasicNameValuePair("train_date", orderRequest.getTrain_date()));
		parameters.add(new BasicNameValuePair("train_no", trainQueryInfo.getTrainno4()));
		parameters.add(new BasicNameValuePair("station", trainQueryInfo.getTrainNo()));
		parameters.add(new BasicNameValuePair("seat", userInfoList.get(0).getSeatType()));
		parameters.add(new BasicNameValuePair("from", trainQueryInfo.getFromStationCode()));
		parameters.add(new BasicNameValuePair("to", trainQueryInfo.getToStationCode()));
		parameters.add(new BasicNameValuePair("ticket", Constants.LEFTTICKETSTR));

		logger.info("提交参数结束------------------------");
		for (int i = 0; i < parameters.size(); i++) {
			NameValuePair  v = parameters.get(i);
			logger.info(v.getName() + ":" + v.getValue());
		}
		
		
		
		logger.info("提交参数结束------------------------");
		String responseBody = "";
		try {
			UrlEncodedFormEntity uef = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
			logger.debug(Constants.POST_URL_CONFIRMSINGLEFORQUEUEORDER + Constants.SIGN + URLEncodedUtils.format(parameters, HTTP.UTF_8));
			post.setEntity(uef);
			responseBody = httpclient.execute(post, responseHandler);
			logger.info("Response is " + responseBody);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		
		return "";
	}

	/**
	 * 处理返回文件流
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private static String readInputStream(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		StringBuffer buffer = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null)
			buffer.append(line + "\n");
		is.close();
		return buffer.toString();
	}
}