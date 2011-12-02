<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>代理商审核</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/js/jquery-1.6.1.js"></script>
<script type="text/javascript" src="/js/jquery.cascadingDropDown.js"></script>
<script language="javascript">
	$(document).ready(function() {
		//查询
		//$("#profitstatus").CascadingDropDown("#reqProvinceid", '/JqueryAjaxSelect');
		$("#profitstatus").CascadingDropDown("#reqProvinceid", '/JqueryAjaxSelect', {
			promptText : '-- Pick an Order--',
			postData: function () { 
			    return { provinceid : $('#reqProvinceid').val() }; 
			},
			onLoading : function() {
				$(this).css("background-color", "#ff3");
			},
			onLoaded : function() {
				$(this).animate({
					backgroundColor : '#ffffff'
				}, 300);
			}
		});
		$("#orderID").CascadingDropDown("#customerID", '/JqueryAjaxSelect', {
			promptText : '-- Pick an Order--',
			postData: function () { 
			    return { provinceid : $('#customerID').val() }; 
			},
			onLoading : function() {
				$(this).css("background-color", "#ff3");
			},
			onLoaded : function() {
				$(this).animate({
					backgroundColor : '#ffffff'
				}, 300);
			}
		});
	});
</script>
</head>
<body>
	<br />
	<table width="97%" border="0" cellspacing="0" cellpadding="0"
		style="margin: 8px 15px;" class="title_bg1_m">
		<tr>
			<td width="14" height="31" class="title_bg1_l"></td>
			<td valign="bottom">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="7" height="28" class="title_fontbg_l"></td>
						<td align="center" nowrap="nowrap" class="title_fontbg_m"><b>客服待退款查询</b>
						</td>
						<td width="7" height="28" class="title_fontbg_r"></td>
						<td nowrap="nowrap" style="padding-left: 10px;"><a
							href="/csRefund_queryRefundApplyHis.do"
							style="font-size: 14px; color: #FFF"> <b>审核历史记录</b> </a></td>
					</tr>
				</table></td>
			<td width="344" class="title_bg1_r"></td>
		</tr>
		<tr>
			<td colspan="3" style="border: #009DCA 1px solid; border-top: 0;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<form name="pageform" id="pageform" method="post" action="">
								<input type="hidden" id="flag" name="flag" value="1" />
								<!-- 查询列表 -->
								<input type="hidden" id="pageNo" name="queryVo.pageNo" value="" />
								<!--  -->
								<input type="hidden" id="downLoad" name="downLoad" value="" />
								<!-- 下载-->
								<table width="97%" border="0" cellspacing="0" cellpadding="0"
									style="margin: 8px;">
									<tr>
										<td nowrap align="right" width="5%">订单号：</td>
										<td nowrap align="left" width="5%"><input type="text"
											id="ordersno" name="queryVo.ordersno" style="width: 150px;"
											value="" /></td>
										<td nowrap align="right" width="5%">
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;充值帐号：</td>
										<td nowrap align="left"><input type="text" id="account"
											name="queryVo.account" style="width: 150px;" value="" /></td>
									</tr>
									<tr>
										<td nowrap align="right" width="5%">省份：</td>
										<td nowrap align="left" width="5%"><select
											id="reqProvinceid" name="reqProvinceid" style="width: 80px;">
												<option value="-1">----请选择----</option>
												<option value="1">----1----</option>
												<option value="2">----2----</option>
												<option value="3">----3----</option>
												<option value="4">----4----</option>
												<option value="5">----5----</option>
										</select></td>
										<td nowrap align="right" width="5%">收益状态：</td>
										<td nowrap align="left"><select id="profitstatus"
											name="profitstatus" style="width: 155px;">
												<option value="">----请选择----</option>
										</select></td>
									</tr>
									<tr>
										<td nowrap align="right" width="5%">省份：</td>
										<td nowrap align="left" width="5%">
											<select id="customerID" name="customerID">   
												<option value="-1">--------</option>   
												<option value="1">----1---</option>   
												<option value="2">----2---</option>   
												<option value="3">----3---</option>   
												<option value="4">----4---</option>   
												<option value="5">----5---</option>   
											</select>
										</td>
										<td nowrap align="right" width="5%">收益状态：</td>
										<td nowrap align="left">
											<select id="orderID" name="orderID"> </select>
										</td>
									</tr>
								</table>
							</form></td>
					</tr>
				</table></td>
		</tr>
	</table>

</body>
</html>