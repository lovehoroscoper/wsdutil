<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>战略合作伙伴</title>
<link rel="stylesheet" href="<c:url value="/css/notRedirect.css"/>">
</head>
<body>
	<!-- page -->
	<div id="page">
		<div id="signup">
			<a href="#"><h1 id="login_logo"></h1></a>
			<div class="login_bar"></div>
			<!-- ntab.css start -->
			<div id="layout" class="div-table">
				<div>
					<div>
						<div class="text-browser"></div>
						<div id="set_default_browser_msg" class="text-browser"></div>
					</div>
				</div>
				<div>
					<div>
						<div id="main-content">
							<div id="quick_dial" class="div-table" style="position: relative; top: 0pt; left: 0pt;">
								<div>
									<div>
										<div id="quick_dial_box" class="div-table">
											<div>
												<div>
													<div class="div-table quick-dial-item dial-used" id="item-2" draggable="true">
														<div>
															<div>
																<div class="div-table dial-bar">
																	<div>
																		<div class="dial-favicon">
																			<div class="default-favicon" imagesrc="<c:url value="/images/showplat/baidu.ico"/>">
																				<img src="<c:url value="/images/showplat/baidu.ico"/>">
																			</div>
																		</div>
																		<div class="dial-title">
																			<div class="text-ellipsis">百度</div>
																		</div>
																		<div class="dial-opt-box">
																			<div class="btn-opt btn-opt-edit" _title="ntab.dial.label.edit" title="不能点击哦!!!"></div>
																		</div>
																		<div class="dial-opt-box">
																			<div class="btn-opt btn-opt-del" _title="ntab.dial.label.del" title="不能点击哦!!!"></div>
																		</div>
																	</div>
																</div>
															</div>
														</div>
														<div>
															<div>
																<div>
																	<a draggable="false" onclick="quickDial.onclickdial(2);" href="http://www.baidu.com/index.php">
																		<div style="height: 100%; width: 100%; background: url(../images/showplat/baidu.png) no-repeat scroll center 0 transparent" class="">
																		</div>
																	</a>
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
											<div>
												<div>
													<div class="div-table quick-dial-item dial-used" id="item-3" draggable="true">
														<div>
															<div>
																<div class="div-table dial-bar">
																	<div>
																		<div class="dial-favicon">
																			<div class="default-favicon" imagesrc="<c:url value="/images/showplat/sina.ico"/>">
																				<img src="<c:url value="/images/showplat/sina.ico"/>">
																			</div>
																		</div>
																		<div class="dial-title">
																			<div class="text-ellipsis">新浪</div>
																		</div>
																		<div class="dial-opt-box">
																			<div class="btn-opt btn-opt-edit" _title="ntab.dial.label.edit" title="不能点击哦!!!"></div>
																		</div>
																		<div class="dial-opt-box">
																			<div class="btn-opt btn-opt-del" _title="ntab.dial.label.del" title="不能点击哦!!!"></div>
																		</div>
																	</div>
																</div>
															</div>
														</div>
														<div>
															<div>
																<div>
																	<a draggable="false" onclick="quickDial.onclickdial(3);" href="http://www.sina.com.cn/">
																		<div style="height: 100%; width: 100%; background: url(../images/showplat/sina.png) no-repeat scroll center 0 transparent" class="">
																		</div>
																	</a>
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- // ntab.css end -->
		</div>
	</div>
</body>
</html>