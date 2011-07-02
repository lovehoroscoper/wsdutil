<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/common/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>代理商审核</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="${ctxPath}/css/base.css" />
	<link rel="stylesheet" type="text/css" href="${ctxPath}/css/ui-lightness/jquery-ui-1.8.13.custom.css" />
	<script type="text/javascript" src="${ctxPath}/js/jquery-1.6.1.min.js"></script>
	<script type="text/javascript" src="${ctxPath}/js/jquery-ui-1.8.13.custom.min.js"></script>
	<script type="text/javascript" src="${ctxPath}/js/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${ctxPath}/js/jquery.form.js"></script>
<script language="javascript">

//$(document).ajaxStop($.unblockUI); 
$(document).ajaxStop(function(){
	$("#wait_form").dialog("close");
	$("#add_form").dialog("open");
}); 

$(document).ready(function(){
	//查询
	$("#queryBtn").click(function(){
		var returnInfo = validateTime();
		if(returnInfo != ""){
			if(!window.confirm(returnInfo)){
				//忽略
				$("#pageNo").val("1");
				$("#downLoad").val("");//设置下载
				$("#pageform").attr("action", "/dealerInfo_queryDealerList.do").submit();
			}else{
				//确认检查 、不做操作
			}
		}else{
			$("#pageNo").val("1");
			$("#downLoad").val("");//设置下载
			$("#pageform").attr("action", "/dealerInfo_queryDealerList.do").submit();
		}
	 });
	
	$("#addInterfaceBtn").button().click(function() {
		$("#add_form").dialog( "open" );
	});
	
	
	  //下载的 ajaxForm
    var url ="/dealerInfo_updateDealerInterface.do";
    var options = { 
        beforeSubmit:  showRequest,  
        success:  showResponseJson,  
        error: showResponseError,
        url:       url ,         
        type:      'post', 
        dataType:  'json'
    }; 
	
    /*
    $("#wait_form").dialog({
		autoOpen: false,
		height: 'auto',
		width: 300,
		minWidth:300,
		modal: true
		
    });*/
    
    $("#wait_form").dialog({    
    	closeOnEscape: false,    
		autoOpen: false,
		height: 'auto',
		width: 300,
		minWidth:300,
		modal: true,
    	open: function(event, ui) { 
    		//$(".ui-dialog-titlebar-close").hide(); 
    		$(this).parent().children().children('.ui-dialog-titlebar-close').hide();
    	}
		
    }); 
    
	$("#add_form").dialog({
		autoOpen: false,
		height: 'auto',
		width: 750,
		minWidth:400,
		modal: true,
		buttons: {
			"提交": function() {
				
				//$.blockUI({ message: '<h1><img src="/images/busy.gif" /> Just a moment...</h1>' }); 
				$("#wait_form").dialog("open");
				
				
				var bValid = true;
				if ( bValid ) {
					$( this ).dialog( "close" );
				}
				
				$(this).ajaxSubmit(options); 
				
			},
			"取消": function() {
				$(this).dialog( "close" );
			}
		},
		close: function() {
			//allFields.val( "" ).removeClass( "ui-state-error" );
		}
	});
	
});	
	
function validateTime(){
	var strInfo = "";
	return strInfo;
}

//调用前
function showRequest(formData, jqForm, options) {
	//$.each(formData,function (i, d) {//商家ID 不转码
	//	if("mxName"==d.name){
	//		formData[i].value = encodeURIComponent(formData[i].value);
	//	}		 
	//});
	//$("#idDownLoad_Result").html("<img src=\"/images/inv/small_loading.gif\"</img>文件生成中...");
	//return true; 
	alert("before---------------------");
	return true;
} 
//调用成功
function showResponseJson(responseText, statusText, xhr, $form)  {
	//$("#wait_form").dialog("close");
	//$("#add_form").dialog("open");
	 // var reFlag = responseText.resultFlag;
	 // var fileURL = responseText.fileURL;
	//  if("true" == reFlag && fileURL.indexOf(".xls")>0){
	//	  $("#idDownLoad_Result").html("<a href=\"/pages/download.jsp?fileName="+fileURL+"\">"+fileURL+"</a>");
	//  }else{
	//	  $("#idDownLoad_Result").html("<font color=\"red\">"+fileURL+"</font>"); 
	//  }
} 
//调用失败
function showResponseError(responseText, statusText, xhr, $form)  { 
	alert("error------------");
	//var rels = "下载文件失败！";       
	//$("#idDownLoad_Result").html("<font color=\"red\">"+rels+"</font>"); 
}

</script>
</head>
<body>
<br/>	
	<table width="97%" border="0" cellspacing="0" cellpadding="0" style="margin:8px 15px;" class="title_bg1_m">
	  <tr>
	    <td width="14" height="31" class="title_bg1_l"></td>
	    <td valign="bottom">
	    <table border="0" cellpadding="0" cellspacing="0">
	      <tr>
	        <td width="7" height="28" class="title_fontbg_l"></td>
	        <td align="center" nowrap="nowrap" class="title_fontbg_m"><b>代理商审核</b></td>
	        <td width="7" height="28" class="title_fontbg_r"></td>
	      </tr>
	    </table>
	    </td>
	    <td width="344" class="title_bg1_r"></td>
	  </tr>
		<tr>
			<td colspan="3" align="right"  height="24"></td>
		</tr>
		<c:if test="${'1' eq '1'}">
			<tr>
		<table width="97%" cellpadding="0" cellspacing="1"
			style="background: #FFF; border: #009DCA 1px solid; margin: 8px 15px;">
			<c:if test="${!empty resultVo}">
					<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
						<td width="20%" align="right">代理商ID</td>
						<td  align="center" colspan="2">${resultVo.dealerid}</td>
					</tr>
					<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
						<td width="20%" align="right">代理商名称</td>
						<td  align="center" colspan="2">${resultVo.dealername}</td>
					</tr>
				<c:if test="${empty resultList }">
					<tr bgcolor="#F4F4F4" align="center">
						<td height="25" colspan="11" align="center"
							style="border: 1px #009DCA solid; color: #F00"><b>该代理商暂无接口信息</b></td>
					</tr>
				</c:if>
				<c:if test="${!empty resultList }">
					<tr bgcolor="#D9D9D9" align="center" style="height:25px;font-weight: bold">
						<td  colspan="3">代理商名已存在接口</td>
					</tr>
					<c:forEach items="${resultList}" var="list" varStatus="idx">
						<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
							<td width="20%" align="right">接口${list.interfacetype}</td>
							<td  align="center"></td>
						</tr>
						<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
							<td width="20%" align="right">接口类型</td>
							<td  align="center">${list.interfacetype}</td>
						</tr>
						<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
							<td width="20%" align="right">接入方式</td>
							<td  align="center">${list.accesstype}</td>
						</tr>
						<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
							<td width="20%" align="right">是否IP限制</td>
							<td  align="center">${list.iplimit}</td>
						</tr>
						<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
							<td width="20%" align="right">IP</td>
							<td  align="center">${list.ip}</td>
						</tr>
						<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
							<td width="20%" align="right">状态</td>
							<td  align="center">${list.facestatus}</td>
						</tr>
						<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
							<td width="20%" align="right">生成时间</td>
							<td  align="center">${list.createtime}</td>
						</tr>
						<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
							<td width="20%" align="right">修改时间</td>
							<td  align="center">${list.edittime}</td>
						</tr>
						<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
							<td width="20%" align="right">最后一次修改人</td>
							<td  align="center">${list.operuserid}</td>
						</tr>
						<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
							<td width="20%" align="right">通知类型</td>
							<td  align="center">${list.noticetype}</td>
						</tr>
						<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
							<td width="20%" align="right">通知地址</td>
							<td  align="center">${list.noticeurl}</td>
						</tr>
					</c:forEach>
				</c:if>
				<tr bgcolor="#F4F4F4" style="height: 30px" align="center">
					<td width="20%" align="right">新增接口</td>
				  <td align="center" nowrap colspan="2"> 
	              	 <input id="addInterfaceBtn" type="button" value="添  加"  />
	              </td>
				</tr>
			</c:if>
			<c:if test="${empty resultVo}">
				<tr bgcolor="#F4F4F4" align="center">
					<td height="25" colspan="11" align="center"
						style="border: 1px #009DCA solid; color: #F00"><b>没有找到您要查询的内容！</b></td>
				</tr>
			</c:if>
		</table>
		</tr>
		</c:if>
	 </table>
<c:if test="${!empty resultVo}">
		<div id="add_form" title="审核通过并提交">
			<p class="validateTips">新增接口信息并提交审核通过.</p>
			<form>
				<input type="hidden" id="dealerid" name="dealerid" value="${resultVo.dealerid}">
				<fieldset>
				<table>
				<tr>
					<td nowrap="nowrap">
						<label for="interfacetype">接口类型</label>
					</td>
					<td nowrap="nowrap">
						<select id="interfacetype" name="interfacetype" style="width:150px;" class="text ui-widget-content ui-corner-all">
							<option value="0">话费</option>
							<option value="1">游戏</option>
							<option value="2">Q币</option>
						</select>(暂时支持话费)
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap">
						<label for="accesstype">接入方式</label>
					</td>
					<td nowrap="nowrap">
						<select id="accesstype" name="accesstype" style="width:150px;" class="text ui-widget-content ui-corner-all">
							<option value="0">http</option>
							<option value="1">socket</option>
							<option value="2">udp</option>
						</select>
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap">
						<label for="iplimit" >是否限制IP</label>
					 </td>
					<td nowrap="nowrap">
						<input type="checkbox" name="iplimit" id="iplimit" value="" class="text ui-widget-content ui-corner-all" />
						<input type="text" name="ip" id="ip" value="" style="width:500px;" class="text ui-widget-content ui-corner-all" />(限制后请输入IP)
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap">
						<label for="password">状态</label>
					</td>
					<td>
						<select id="status" name="status" style="width:150px;" class="text ui-widget-content ui-corner-all">
							<option value="0">开启</option>
							<option value="1">关闭</option>
						</select>
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap">
						<label for="password">通知类型</label>
					</td>
					<td nowrap="nowrap">
						<select id="noticetype" name="noticetype" style="width:150px;" class="text ui-widget-content ui-corner-all">
							<option value="0">被动等待</option>
							<option value="1">主动查询</option>
						</select>
					</td>
				</tr>
				<tr>				
					<td>
						<label for="noticeurl">通知URL</label>
					</td>
					<td >
						<input type="text" name="noticeurl" id="noticeurl" value="" style="width:550px;"  class="text ui-widget-content ui-corner-all" />
					</td>
					</table>
				</fieldset>
			</form>
		</div>
		<div id="wait_form" title="等待">
			<h1><img src="/images/busy.gif" /> Just a moment...</h1>
		</div>
</c:if>
</body>
</html>