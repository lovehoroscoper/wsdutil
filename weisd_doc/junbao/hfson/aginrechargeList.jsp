<%@ page language="java" contentType="text/html; charset=gb2312"
	pageEncoding="gb2312"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>补充申请及查询</title>
<link href="/css/base.css" rel="stylesheet" type="text/css" />
<script src="/js/ymd.js" language="javascript"></script>
<script src="/js/util.js" language="javascript"></script>
<script type="text/javascript" src="/js/jquery-1.6.1.min.js"></script>
<script type="text/javascript" src="/js/calender/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="/css/ui-lightness/jquery-ui-1.8.13.custom.css" />
<script type="text/javascript" src="/js/jquery-ui-1.8.13.custom.min.js"></script>
<script type="text/javascript" src="/js/jquery.form.js" ></script>
<script language="JavaScript">
var util = new Util();
function showRechargeRecords(_hfOrderid){
	$.ajax({
		  type: 'POST',
		  url: "/order_queryOrderRechargeRecordById.do",
		  data: { hfOrderid: _hfOrderid},
		  dataType: 'json',
		  success: function(data) {
			  var len = data.length;
			  if (len>0){
				  var div= document.getElementById("rechargeRecordView");
				  div.title.innerHTML = "订单【"+_hfOrderid+"】的充值记录";
				  var td="";
		          for(var i=0;i<len;i++){
		        	  var tr = "";
		        	  if(i%2==0){
		        		  tr = "<tr bgcolor='#F4F4F4' align='center'>";
		        	  }else{
		        		  tr = "<tr bgcolor='#E8E8E8' align='center'>";
		        	  }
	              	 td +=tr
			             +"<td height='25'>"+(i+1)+"</td>"
			             +"<td height='25'>"+data[i].hfSerialid+"</td>"
			             +"<td height='25'>"+data[i].fullMoney+"</td>"
			             +"<td height='25'>"+data[i].finishMoney+"</td>"
			             +"<td height='25'>"+data[i].hfStatusName+"</td>"
			             +"<td height='25'>"+data[i].hfErrorcode+"</td>"
			             +"<td height='25'>"+data[i].beginTime+"</td>"
			             +"<td height='25'>"+data[i].finishTime+"</td>"
			             +"<td height='25'>"+data[i].channelName+"</td>"
			             +"<td height='25'>"+data[i].channelSerialid+"</td>"
			             +"<td height='25'>"+data[i].sendSerialid+"</td>"
			             +"</tr>";
		           } 
					div.innerHTML="<table width='100%' cellpadding='0' cellspacing='1' style='background: #FFF; border: #009DCA 1px solid;'>"
					+"<tr align='center' class='color5'>"
					+"<td height='25'><b>序号</b></td>"
					+"<td height='25'><b>充值流水号</b></td>"
					+"<td height='25'><b>充值金额</b></td>"
					+"<td height='25'><b>已充金额</b></td>"
					+"<td height='25'><b>状态</b></td>"
					+"<td height='25'><b>错误码</b></td>"
					+"<td height='25'><b>开始时间</b></td>"
					+"<td height='25'><b>完成时间</b></td>"
					+"<td height='25'><b>渠道编码</b></td>"
					+"<td height='25'><b>渠道流水号</b></td>"
					+"<td height='25'><b>送给渠道流水号</b></td>"
					+"</tr>"
					+td;
					+"</table>";
			  }else{
				  div.innerHTML="<table width='100%' cellpadding='0' cellspacing='1' style='background: #FFF; border: #009DCA 1px solid;'>"
		             +"<tr><td colspan='12'>该订单没有充值记录！</td></tr>" 
		             +"</table>";
			  }
			  $("#rechargeRecordView").dialog({
					autoOpen: false,		
					height: 'auto',
					width: 800,
					minWidth:400,
					modal: true,
					title: "订单【"+_hfOrderid+"】的充值记录",
					close: function() {
					}
				});
			  $("#rechargeRecordView").dialog("open");
	        }
		});
}	

function downloadExcel(){
	var oldAction = pageform.action;
	pageform.action = "/succOrder_queryExcelDown.do";
	pageform.submit();
	pageform.action = oldAction;
}

$(document).ready(function(){
    var url ="/orderAgainRecharge_addAgainApply.do";
    var options = { 
        beforeSubmit:  showRequest,  
        success:  showResponseJson,  
        error: showResponseError,
        url:       url ,         
        dataType:  'json',
        type:      'post'
    }; 
	
	$("#div_add_form").dialog({
		autoOpen: false,
		height: 'auto',
		width: 650,
		minWidth:600,
		modal: true,
		buttons: {
			"提交": function() {		
				$("#add_form").ajaxSubmit(options); 
			},
			"取消": function() {
				$(this).dialog( "close" );
				$("#add_form").resetForm();
			}
		}
	});
	
    
    $("#div_wait_ajax_form").dialog({    
    	closeOnEscape: false,    
		autoOpen: false,
		hide: "explode",
		height: 'auto',
		width: 350,
		minWidth:350,
		modal: true,
    	open: function(event, ui) { 
    		$(this).parent().children().children('.ui-dialog-titlebar-close').hide();
    	}
    }); 
    //调用结果显示窗口
    $("#div_result_ajax_form").dialog({
		width: 400,
		minWidth:400,
		modal: true,
		autoOpen: false,
		buttons: {
			"重新加载界面": function() {		
				document.location.reload();//当前页面
			},
			"关闭": function() {		
				$(this).dialog("close");
			}
		},
		close: function() {
			$(this).empty();
		}
    }); 
	
	//查询
	$("#queryBtn").click(function(){
		var returnInfo = validateTime();
		if(returnInfo != ""){
			alert(returnInfo);
		}else{
			$("#pageNo").val("1");
			$("#downLoad").val("");//设置下载
			$("#pageform").attr("action", "/csRefund_queryRefundList.do").submit();
		}
	 });
	
	
	$("a[name^='addRefundApply_']").click(function(){
		var thisHr = $(this);
		var againorderid = thisHr.attr('againorderid');
		var hfserialid = thisHr.attr('hfserialid');
		var finishMoney = thisHr.attr('finishMoney');
		$("#againorderid").val(againorderid);
		$("#againorderid2").val(againorderid);
		$("#hfserialid").val(hfserialid);
		$("#hfserialid2").val(hfserialid);
		$("#finishMoney").val(finishMoney);
		$("#div_add_form").dialog( "open" );
	});
	
});	

//调用前
function showRequest(formData, jqForm, options) {
	var bValid = validateAjax();//验证
	if ( bValid ) {
		$("#div_wait_ajax_form").dialog("open");
		$("#div_add_form").dialog("close");
		$.each(formData,function (i, d) {//需要转码的
			if($("#" + d.name).hasClass("encode-value")){
				//alert(encodeURIComponent(formData[i].value));
				formData[i].value = encodeURIComponent(formData[i].value);
			}
		});
	}
	return bValid;
} 
//调用成功
function showResponseJson(responseText, statusText, xhr, $form)  {
	$("#div_wait_ajax_form").dialog("close");
	var resultFlag = responseText.resultFlag;
	var errorInfo = responseText.errorInfo;
	if(true == resultFlag || "true" == resultFlag){
		$("#add_form").resetForm();
		var ht = "<font color=\"red\">操作成功," + errorInfo + "\n 操作完成,请重新加载数据</font>";
	 	$("#div_result_ajax_form").html(ht).dialog("open"); 
	}else{
	 	$("#div_result_ajax_form").html("<font color=\"red\">操作失败!" + errorInfo + "</font>").dialog("open"); 
	}
} 
//调用失败
function showResponseError(responseText, statusText, xhr, $form)  {
	$("#div_wait_ajax_form").dialog("close");
	var rels = "提交数据出现异常!!!";       
	$("#div_result_ajax_form").html("<font color=\"red\">"+rels+"</font>").dialog("open"); 
}

function validateAjax(){
	var strInfo = true;
	var againorderid_v = $.trim($("#againorderid").val());//默认订单号
	var againorderid2_v = $.trim($("#againorderid2").val());//显示订单号
	
	var hfserialid_v = $.trim($("#hfserialid").val());//默认流水
	var hfserialid2_v = $.trim($("#hfserialid2").val());//显示流水
	
	
	var finishMoney_v = $.trim($("#finishMoney").val());//已完成金额
	var againMoney_v = $.trim($("#againMoney").val());//补冲金额
	var refundreason_v = $.trim($("#refundreason").val());//原因
	
	if("" == againorderid_v || "" == againorderid2_v){
		alert("补冲订单号为空");
		return false;
	}
	if(againorderid_v != againorderid2_v){
		alert("补冲订单号不匹配");
		return false;
	}
	if("" == hfserialid_v || "" == hfserialid2_v){
		alert("原充值成功流水号为空");
		return false;
	}
	if(hfserialid_v != hfserialid2_v){
		alert("原充值成功流水号不匹配");
		return false;
	}
	if (/[^\d]/.test(finishMoney_v)){
		alert("实际到帐金额只能为数字,整数");
		return false;
	}
	if("" == finishMoney_v ||  0 > Number(finishMoney_v)){
		alert("实际到帐金额不能为空或者小于0");
		return false;
	}
	if (/[^\d]/.test(againMoney_v)){
		alert("补冲金额只能数字,整数");
		return false;
	}
	if("" == againMoney_v || 0 >= Number(againMoney_v) ){
		alert("补冲金额不能为空且必须大于0");
		return false;
	}
	if("" == refundreason_v){
		alert("退款原因不能为空");
		return false;
	}
	return strInfo;
}
</script>
</head>
<body>
<table width="97%" height="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td></td>
	</tr>
	<tr>
		<td width="100%">
		
		<table id="showTable" width="97%" height="100%" border="0" cellspacing="0" cellpadding="0"
			style="margin: 8px 15px;" class="title_bg1_m">
			<tr>
				<td width="14" height="31" class="title_bg1_l"></td>
				<td valign="bottom">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="7" height="28" class="title_fontbg_l"></td>
						<td align="center" nowrap="nowrap" class="title_fontbg_m"><b>补充申请及查询</b></td>
						<td width="7" height="28" class="title_fontbg_r"></td>
						<td style="padding-left: 15px;" nowrap></td>
					</tr>
				</table>
				</td>
				<td width="344" class="title_bg1_r"></td>
			</tr>
			<tr>
				<td colspan="3" style="border: #009DCA 1px solid; border-top: 0;">
				<table border="0" cellspacing="0" width="97%" cellpadding="0"
					style="margin: 8px;">
					<form name="pageform" id="pageform" method="post" action="/orderAgainRecharge_queryNeedAgainList.do" >
					<input type="hidden" name="flag" value="1" />
					<input type="hidden" name="pageNo" value="${pageNo }" />
					<input type="hidden" name="pageSize" value="20" />
					<tr>
						<td>运营商：<input type="hidden" name="platformid" id="platformid" value="6"></td>
						<td><c:forEach var="il" items="${ispList}">
								<input class="checkbox" type="checkbox" <c:if test="${il.checked}">checked</c:if>
								name="ispIds" id="ispIds" value="${il.id }" />${il.name}
							</c:forEach>							
							<input type="checkbox" name="ispIdsAll" onclick="util.selectAllCheckBox('ispIds', this)" />全选</td>
						</td>
						<td>省份：</td>
						<td>
						<select name="provinceId" style="width: 126px" id="provinceId">
							<option value="">--全部--</option>
							<c:forEach var="pl" items="${provinceList}">
								<option value="${pl.id}" <c:if test="${pl.checked}">selected</c:if>>${pl.name}</option>
							</c:forEach>
						</select>			
						</td>
						<td>到账类型:</td>
						<td><select id="delayTime" name="delayTime"
							style="width: 120px">
							<option value="">---全部---</option>
							<c:forEach var="dl" items="${delayTimeList}">
								<option value="${dl.id}" <c:if test="${dl.checked}">selected</c:if>>${dl.name}</option>
							</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td>代理商编号：</td>
						<td><input type="text" name="reqUserid" id="reqUserid" value="${reqUserid }"
							style="width: 120px" /></td>
						<td>手机号：</td>
						<td><input type="text" name="reqAccnum" id="reqAccnum" value="${reqAccnum }" style="width: 120px" /></td>
						<td>订单金额：</td><td><input type="text" id="reqMoney" name="reqMoney" value="${reqMoney}"></td>
					</tr>
					<tr>
						<td>开始时间：</td>
						<td><input type="text" name="beginTime" id="beginTime"
							value="${beginTime}" style="width: 120px"
							onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" /></td>
						<td>结束时间：</td>
						<td><input type="text" name="endTime"
							id="endTime" value="${endTime}" style="width: 120px"
							onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" /></td>
						<td>业务类型：</td>
						<td><c:forEach var="stl" items="${serviceTypeList}">
							<input type="checkbox" name="serviceTypes" value="${stl.id}" <c:if test="${stl.checked}">checked</c:if>>${stl.name}
						</c:forEach></td>
					</tr>
					<tr>
						<td>订单来源：</td>
						<td colspan="3">
							<div style="width: 450px; height: 50px; overflow: auto; border: 2px inset #FFFFFF;">
								<c:forEach var="sl" items="${orderSourceList}">
									<label class="nock"> 
										<input class="checkbox" type="checkbox" name="orderSourceIds" id="orderSourceIds" value="${sl.id }" <c:if test="${sl.checked}">checked</c:if> />${sl.name}
									</label>
								</c:forEach>
							</div>
						<input type="checkbox" name="orderSourcesListAll" onclick="util.selectAllCheckBox('orderSourceIds',this)" />全部选择</td>
						<td colspan="2" align="left"><input type="submit" value="查询" style="width: 100px;" /></td>
					</tr>
					
					</form>
				</table>
				</td>
				
			</tr>
			<c:if test="${flag eq '1'}">
				<tr>
					<td colspan="6">
					<table width="100%" cellpadding="0" cellspacing="1"
						style="background: #FFF; border: #009DCA 1px solid;">
						<tr align="center" class="color5">
							<td height="25"><b>序号</b></td>
							<td height="25"><b>手机号</b></td>
							<td height="25"><b>订单金额</b></td>
							<td  height="25"><b>实际充</br>值金额</b></td>
							<td height="25" nowrap="nowrap"><b>充值完成时间</b></td>
							<td height="25"><b>运营商</b></td>
							<td height="25"><b>手机省份</b></td>
							<td height="25"><b>订单来源</b></td>
							<td height="25"><b>操作</b></td>
						</tr>
						<c:if test="${not empty page and !empty page.dataList }">
							<c:forEach var="data" items="${page.dataList}" varStatus="status">
								<tr
									bgcolor="<c:if test='${status.index%2==0}'>#F4F4F4</c:if><c:if test='${status.index%2!=0}'>#E8E8E8</c:if>">
									<td height="30" class="tdjj">${(pageNo-1)*20+status.index+1 }</td>
									<td height="30" class="tdjj"><a
											href="#" onclick="showRechargeRecords('${data.reqOrderid}')">${data.reqAccnum}
									</a></td>
									<td height="30" class="tdjj">${data.reqMoney}</td>
									<td height="30" class="tdjj">${data.finishMoney}</td>
									<td height="30" class="tdjj" nowrap="nowrap">
										${fn:substring(data.chargeFinishtime,0,19)}
									</td>
									<td height="30" class="tdjj"><c:if
										test="${data.hfIspid eq 2}">电信</c:if><c:if
										test="${data.hfIspid eq 0}">联通</c:if><c:if
										test="${data.hfIspid eq 1}">移动</c:if></td>
									<td height="30" class="tdjj">${data.provinceName}</td>
									<td height="30" class="tdjj">${data.osDescript}</td>
									<td>
										<a target="_blank" href="javascript:void(0);" id="addRefundApply_${data.reqOrderid}" name="addRefundApply_${data.reqOrderid}" againorderid="${data.reqOrderid}" finishMoney="${data.finishMoney}" hfserialid="${data.hfSerialid}">[补冲申请]</a>
									</td>
								</tr>
							</c:forEach>
						</c:if>					
						<c:if test="${empty page or empty page.dataList }">
							<tr bgcolor="#F4F4F4">
								<td height="25" colspan="16" align="center" style="color: #F00">
								<b>没有找到您要查询的内容！</b></td>
							</tr>
						</c:if>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="14">
					<c:if test="${not empty page and !empty page.dataList }">
					<%@ include file="/pages/public/page.jsp"%>
					<script language="javascript">
						var pageform = document.getElementById("pageform");//firefox
						<%@ include file="/js/changePage.js" %>
					</script>
					</c:if>
					</td>
				</tr>
			</c:if>
		</table>
		</td>
	</tr>
</table>
<div id="rechargeRecordView" title="">
</div>

		<div id="div_add_form" title="补冲申请设置">
			<form id="add_form" name="add_form"  method="post" action="">
				<input type="hidden" id="againorderid" name="againorderid" value="">
				<input type="hidden" id="hfserialid" name="hfserialid" value="">
				<input type="hidden" id="flag" name="flag" value="1">
				<fieldset>
				<table>
				<tr>				
					<td nowrap="nowrap">
						<label for="actualrefundamount">补冲订单号</label>
					</td>
					<td nowrap="nowrap">
						<input type="text" name="againorderid2" id="againorderid2" value="" readonly="readonly" style="width:150px;"  class="text ui-widget-content ui-corner-all" /><font color="red">*不可手写</font>
					</td>
				</tr>
				<tr>				
					<td nowrap="nowrap">
						<label for="actualrefundamount">原充值成功流水号</label>
					</td>
					<td nowrap="nowrap">
						<input type="text" name="hfserialid2" id="hfserialid2" value="" readonly="readonly" style="width:150px;"  class="text ui-widget-content ui-corner-all" /><font color="red">*不可手写</font>
					</td>
				</tr>
				<tr>				
					<td nowrap="nowrap">
						<label for="refundcost">已完成金额</label>
					</td>
					<td nowrap="nowrap">
						<input type="text" name="finishMoney" id="finishMoney" value="0" style="width:150px;"  class="text ui-widget-content ui-corner-all" /><font color="red">* 只能为整数,可根据实际情况调整</font>
					</td>
				</tr>
				<tr>				
					<td nowrap="nowrap">
						<label for="refundaccount">补冲金额</label>
					</td>
					<td nowrap="nowrap">
						<input type="text" name="againMoney" id="againMoney" value="" style="width:150px;"  class="text ui-widget-content ui-corner-all" /><font color="red">*只能为整数</font>
					</td>
				</tr>
				<tr>				
					<td nowrap="nowrap">
						<label for="refundreason">其他备注</label>
					</td>
					<td nowrap="nowrap">
						<input type="text" name="refundreason" id="refundreason" value="" style="width:400px;"  class="text ui-widget-content ui-corner-all encode-value" maxlength="200"/><font color="red">*</font>长度不超过200
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" colspan="2">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" colspan="2">
						请认真检查信息,如果信息不匹配请修改后提交数据
					</td>
				</tr>
				</table>
				</fieldset>
			</form>
		</div>
		<div id="div_wait_ajax_form" title="正在提交数据,请稍等……">
			<img src="/images/busy.gif" />正在提交数据,请稍等……
			<div align="right">
				<span class="ui-icon ui-icon-alert" style="float:left;margin: 20px 1px 1px 130px;">
				</span>
				<span style="float:left; margin-top:22px;color:#99D8FE;font-size: 10px;">
						如果超出等待时长,重新来过或者自便!!!
				</span>
			</div>
		</div>
		<div id="div_result_ajax_form" title="调用结果">
		</div>
</body>
</html>