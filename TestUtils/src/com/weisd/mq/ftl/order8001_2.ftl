<#escape x as x!"">
<?xml version="1.0" encoding="GB2312" ?>
<xrpc>
	<xrpchead>
		<version>1.0</version>
	</xrpchead>
	<public_req>
		<rpcmode>0</rpcmode>
		<memcache_key />
		<mq />
		<filesvr>
			<fileflag />
			<filename />
			<filemd5 />
			<zipmode />
		</filesvr>
	</public_req>
	<transsvr_req>
		<head>
			<transcode>OT0201</transcode>
			<oper />
			<orgcode />
			<channel>03</channel>
			<chndate>20110831</chndate>
			<chntime>160449</chntime>
			<transdesp />
		</head>
		<querymode>
			<maxrecords>10</maxrecords>
			<resultmode />
			<filemode />
			<offset>1</offset>
			<countall>1</countall>
		</querymode>
		<body>
			<OID_WARE>3900</OID_WARE><!-- 合作方统一参数(固定为“3900”) -->
			<CINFO1 /><!-- 无含义 -->
			<CINFO2 /><!-- 无含义 -->
			<AMT_OCCUR>${chargeamount}</AMT_OCCUR><!-- 话费面值 -->
			<JNO_CLI>${orderid}</JNO_CLI><!-- 易百米流水号 -->
			<OID_REGUSER>13588168243</OID_REGUSER><!-- 易百米商户号 -->
			<OID_TRADER>${agentid}</OID_TRADER><!-- 易百米在合作方的商户号 -->
			<UID_CLI>${mobilenum}</UID_CLI><!-- 被充值号码 -->
			<COUNT_WARE>1</COUNT_WARE><!-- 数量 -->
		</body>
	</transsvr_req>
</xrpc>
</#escape>