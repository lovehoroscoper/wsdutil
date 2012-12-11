package com.test.face;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2012-12-3 下午6:46:51
 */
public interface TestSoInit extends Library {

	TestSoInit instance = (TestSoInit) Native.loadLibrary("tstts", TestSoInit.class);

//	int tsttsInit(String path);
	
	
	/**
	* @brief 语音合成初始化
	* @param [in]pWorkPath,语音合成工作路径,初始化的时候将从这个路径找配置，
	*			词典，合成库等数据。
	* @return 错误码
	* @retval 0成功，非0失败
	*/
	//TSERR_TYPE tsttsInit(const char* pWorkPath);
	int tsttsInit(String pWorkPath);

	/**
	* @brief 语音合成逆初始化，释放资源，退出合成
	* @return 返回错误码
	* @retval 0成功，非0失败
	*/
	//TSERR_TYPE tsttsUninit();
	Object tsttsUninit();

	/**
	* @brief 新建一个会话
	* @param [out] pSessionId,执行成功后用该参数返回会话id
	* @return 返回错误码
	* @retval 0成功，非0失败
	*/
	//TSERR_TYPE tsttsNewSession(uint32_t* pSessionId);
	int tsttsNewSession(IntByReference pSessionId);

	/**
	* @brief 删除一个会话
	* @param [in] sessionId, 要销毁的会话id
	* @return 返回错误码
	* @retval 0成功，非0失败
	*/
	//TSERR_TYPE tsttsDelSession(uint32_t sessionId);
	Object tsttsDelSession(int pSessionId);


	/**
	* @brief 得到一个会话的一个参数值,用字符串表示'\0'结尾
	* @param [in] sessionId, 会话id。如该值为0，则是对公共参数进行操作
	* @param [in] strName, 参数名
	* @param [out] pBuf,用该参数返回所要的参数值，用'\0'结尾的字符串表示。
	*           pBuf可以为NULL，如为NULL则本函数仅返回所需缓冲区的大小，不填充值
	* @param [in] bufSize, 传入的缓冲区大小
	* @return  该参数需要多少缓冲区存储，返回0表示不存在这样的参数，
	*        如果传入的缓冲区大小小于该值，会被截断
	*/
	//uint32_t tsttsParamGet(uint32_t sessionId, const char* strName, char* pBuf, uint32_t bufSize);
//	Object tsttsParamGet(int sessionId, String strName);

	/**
	* @brief 设置一个参数值,用字符串表示，'\0'结尾
	* @param [in] sessionId, 会话id。如该值为0，则是对公共参数进行操作
	* @param [in] strName, 参数名
	* @param [in] pBuf, 用'\0'结尾的字符串表示的参数值，
	* @return  错误码
	* @retval 0成功，非0失败
	*/
	//TSERR_TYPE tsttsParamSet(uint32_t sessionId, const char* strName, const char* pBuf);

	/**
	* @brief 设置要合成的文本，该函数要和函数tsttsGetAudio配合使用，
	*       在合成数据提取完成前，不要修改该文本内容，文本缓冲区内存也不能释放
	* @param [in]sessionId,会话id
	* @param [in]pTextBuf, 要合成的文本缓冲区
	* @param [in]textLenByByte,要合成文本的长度，以字节为单位
	//这行取消* @param [in]textCoding,要合成文本的字符编码，0:自动检测;1:utf-8;2:utf-16;4:utf-32;17:GBK;33:BIG5
	* @return  错误码
	* @retval 0成功，非0失败
	*/
	//TSERR_TYPE tsttsPrepareText(uint32_t sessionId, const void* pTextBuf, uint32_t textLenByByte);

	/**
	* @brief 提取合成的音频，该函数要和函数tsttsSetText配合使用
	* @param [in]sessionId,会话id
	* @param [out]pAudioBuf,音频缓冲区
	* @param [in/out]pLen,传入时为音频缓冲区大小，输出为实际提取的数据长度，均以字节为单位
	* @return  错误码
	* @retval 0成功，非0失败，其中TSERR_NO_DATA表示合成音频已经取完
	*/
	//TSERR_TYPE tsttsGetAudio(uint32_t sessionId, void* pAudioBuf, uint32_t* pLen);

	/**
	* @brief 直接合成文本到音频文件，不要和tsttsSetText，tsttsGetAudio这两个函数交叉使用
	* @param [in]sessionId,会话id
	* @param [in]strFile, 要输出的音频文件名
	* @param [in]pTextBuf, 要合成的文本缓冲区
	* @param [in]textLenByByte,要合成文本的长度，以字节为单位
	* @return  错误码
	* @retval 0成功，非0失败
	*/
	//TSERR_TYPE tsttsTextToAudioFile(uint32_t sessionId, const char* strFile, const void* pTextBuf, uint32_t textLenByByte);

}
