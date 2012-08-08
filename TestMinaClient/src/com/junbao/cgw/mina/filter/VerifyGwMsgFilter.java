package com.junbao.cgw.mina.filter;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;

import com.hisunsray.commons.res.Config;
import com.junbao.hf.utils.common.HttpSignServiceV2;
import com.junbao.hf.utils.common.StringUtils;

/**
 * @desc 描述：核心与渠道
 * 
 *       该filter必须后于ProtocolCodecFilter 应该ProtocolCodecFilter包含\n
 * 
 *       用户client 先经过 filterWrite
 * 
 *       不能关闭连接
 * 
 * @author weisd E-mail:weisd@junbao.net
 */
public class VerifyGwMsgFilter extends IoFilterAdapter {

	// 输出到一个而外文件,方便开始时候检查是否有问题
	private static Logger logger = Logger.getLogger("verifycorelog");

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		// 是否 验证加密摘要
		if ("true".equals(Config.getProperty("CORE_TO_QD_VERIFY_IS"))) {
			if (message instanceof String) {
				String msgStr = (String) message;
				if (!StringUtils.isEmptyOrNullByTrim(msgStr)) {
					boolean verifyFlag = HttpSignServiceV2.validResponseString(msgStr.trim(), "&", Config.getProperty("CORE_TO_QD_APPKEYNAME"), Config.getProperty("CORE_TO_QD_APPKEY"));
					if (verifyFlag) {
						logger.error("渠道返回加密验证通过,-----请删除该日志-------,原信息:" + msgStr);
						// 加密验证通过
						nextFilter.messageReceived(session, message);
					} else {
						if ("true".equals(Config.getProperty("CORE_TO_QD_VERIFY_LIMIT"))) {
							logger.error("渠道返回加密验证失败,停止继续,丢弃消息(请联系技术),原信息:" + msgStr);
						} else {
							logger.warn("渠道返回加密验证失败,继续执行,原信息:" + msgStr);
							nextFilter.messageReceived(session, message);
						}
					}
				} else {
					// 让handler处理
					nextFilter.messageReceived(session, message);
				}
			} else {
				nextFilter.messageReceived(session, message);
			}
		} else {
			nextFilter.messageReceived(session, message);
		}
	}

	@Override
	public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		Object msgObj = writeRequest.getMessage();
		if (null != msgObj && msgObj instanceof String) {
			// 如果有\r\n 则去除
			String msgStr = (String) msgObj;
			if (StringUtils.isEmptyOrNullByTrim(msgStr)) {
				nextFilter.filterWrite(session, writeRequest);
			} else {
				// 必须保证该字符串没有存在加密摘要
				String messageDigest = HttpSignServiceV2.creatHttpParamLineString(msgStr.trim(), "&", Config.getProperty("CORE_TO_QD_APPKEYNAME"), Config.getProperty("CORE_TO_QD_APPKEY"));
				logger.info("核心发送渠道加密数据:" + messageDigest);
				DefaultWriteRequest digestDWR = new DefaultWriteRequest(messageDigest, writeRequest.getFuture(), writeRequest.getDestination());
				nextFilter.filterWrite(session, digestDWR);
			}
		} else {
			nextFilter.filterWrite(session, writeRequest);
		}
	}

}
