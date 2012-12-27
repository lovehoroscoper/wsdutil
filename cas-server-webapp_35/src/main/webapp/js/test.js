fml.define("component/windowResize", ["jquery"],
function(a, b) {
	function f() {
		e && clearTimeout(e),
		e = window.setTimeout(function() {
			var a = d.length;
			for (var b = 0; b < a; b++) d[b]()
		},
		240)
	}
	var c = a("jquery"),
	d = [],
	e;
	if (c.browser.msie) {
		var g = document.createElement("div");
		g.style.cssText = "width:100%;height:0px;position:absolute;bottom:0px;left:0px;overflow:hidden",
		document.body.appendChild(g),
		g.onresize = f
	} else window.onresize = f;
	b.bind = function(a) {
		d.push(a)
	}
});
fml.define("component/animate", ["jquery"],
function(a, b) {
	var c = a("jquery");
	b.twinkle = function(a, b, d, e) {
		b || (b = 500),
		d || (d = 0),
		e || (e = !0),
		d = -d,
		a = c(a);
		if (!a.length) return;
		var f = a.get(0),
		g = 0,
		h = null,
		i = !1;
		clearTimeout(h),
		h = window.setTimeout(function() {
			i || (i = !0, a.show());
			var c = f.style.visibility;
			f.style.visibility = c == "hidden" ? "visible": "hidden",
			g--;
			if (g <= d) return window.clearTimeout(h),
			e ? a.show() : (a.hide(), i = !1),
			g = 0,
			!1;
			h = window.setTimeout(arguments.callee, b)
		},
		b)
	}
});
fml.define("component/userstate", ["jquery", "component/iStorage"],
function(a, b) {
	var c = a("jquery"),
	d = a("component/iStorage"),
	e = new Date,
	f = c.browser,
	g = {};
	c(document).bind("mousemove",
	function() {
		e = new Date
	}),
	b.browser = function(a, b) {
		a = {
			ie: "msie"
		} [a] || a;
		var c = g[a + b];
		return undefined !== c ? c: f[a] ? b && b != f.version ? c = !1 : c = !0 : c = !1
	},
	b.activity = function(a) {
		return a || (a = 30),
		new Date - e < a * 1e3
	},
	b.isNew = function() {
		var a = new Date,
		b = "0" + (a.getMonth() + 1),
		c = "0" + a.getDate(),
		e = a.getFullYear().toString().substr(2) + b.substr(b.length - 2) + c.substr(c.length - 2),
		f = d.getCookie("MEILISHUO_GLOBAL_KEY");
		if (!f) return ! 1;
		var g = f.substr(17, 6) == e;
		return g
	}
});
fml.define("component/iStorage", [],
function(a, b) {
	var c = !1,
	d = !1,
	e = {
		cookieArr: {},
		options: {
			domain: ".meilishuo.com",
			path: "/"
		},
		setCookie: function(a, b, c) {
			c = c || {};
			var d = a + "=" + encodeURIComponent(b);
			c.domain || (c.domain = this.options.domain),
			c.path || (c.path = this.options.path),
			d += "; domain=" + c.domain,
			c.path && (d += "; path=" + c.path);
			if (c.duration) {
				var e = new Date;
				e.setTime(e.getTime() + c.duration * 1e3),
				d += "; expires=" + e.toGMTString()
			}
			return c.secure && (d += "; secure"),
			document.cookie = d + ";"
		},
		getCookie: function(a) {
			return this.cookieArr[a] = this.cookieArr[a] ||
			function() {
				var b = window.document.cookie.match("(?:^|;)\\s*" + a.replace(/([-.*+?^${}()|[\]\/\\])/g, "\\$1") + "=([^;]*)");
				return b ? decodeURIComponent(b[1]) : undefined
			} (),
			this.cookieArr[a]
		},
		removeCookie: function(a) {
			return this.setCookie(a, "", {
				duration: -1
			})
		}
	},
	f = {
		set: function(a, b, c, d) {
			c ? sessionStorage.setItem(a, b) : localStorage.setItem(a, b),
			typeof d == "function" && d()
		},
		get: function(a, b, c) {
			b ? c(sessionStorage.getItem(a)) : c(localStorage.getItem(a))
		},
		remove: function(a, b) {
			b ? sessionStorage.removeItem(a) : localStorage.removeItem(a)
		}
	},
	g = {
		flash: document.getElementById("storage"),
		sessionId: "",
		callback: [],
		init: function(a) {
			a && (this.sessionId = this.sessionId || e.getCookie("PHPSESSID"));
			if (c || d) return;
			d = !0;
			var b = new Date,
			f = document.createElement("div"),
			g = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="storage"';
			g += 'codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="1" height="1">',
			g += '<param name="movie" value="http://i.meilishuo.net/css/images/window/storage.swf?d=' + b.getTime() + '" />',
			g += '<param name="quality" value="high" />',
			g += '<param name="allowScriptAccess" value="always" />',
			g += "</object>",
			f.innerHTML = g,
			document.body.appendChild(f),
			this.flash = document.getElementById("storage")
		},
		detectIE: function() {
			var a = navigator.userAgent.toLowerCase();
			if (window.ActiveXObject) {
				var b = a.match(/msie ([\d.]+)/)[1];
				if (b >= 5.5 && b < 8) return ! 0
			}
			return ! 1
		},
		set: function(a, b, d, e) {
			function g(a, b, c) {
				if (c) {
					var d = new Date;
					d = parseInt(d.getTime() / 36e5),
					f.flash.setSessionTime(d),
					f.flash.setSessionVal(f.sessionId + a, d),
					f.flash.set(f.sessionId + a, b)
				} else f.flash.set(a, b)
			}
			var f = this;
			f.init(d),
			c ? (g(a, b, d), typeof e == "function" && e()) : f.callback.push(function() {
				g(a, b, d),
				typeof e == "function" && e()
			})
		},
		get: function(a, b, d) {
			function f(a, b) {
				return b ? (e.flash.removeAllSession(), e.flash.get(e.sessionId + a)) : e.flash.get(a)
			}
			var e = this;
			e.init(b),
			c ? d(f(a, b)) : e.callback.push(function() {
				d(f(a, b))
			})
		},
		remove: function(a, b) {
			var d = this;
			d.init(b);
			if (!c) {
				var d = this;
				window.setTimeout(function() {
					d.remove(a, b)
				},
				100);
				return
			}
			b ? d.flash.remove(d.sessionId + a) : d.flash.remove(a)
		}
	},
	h = {
		set: function(a, b, c, d) {
			c ? e.setCookie(a, b) : e.setCookie(a, b, {
				duration: 15552e3
			}),
			typeof d == "function" && d()
		},
		get: function(a, b, c) {
			c(e.getCookie(a))
		},
		remove: function(a, b) {
			e.removeCookie(a)
		}
	},
	i = g.detectIE() ?
	function() {
		try {
			var a = new ActiveXObject("ShockwaveFlash.ShockwaveFlash");
			return a ? g: h
		} catch(b) {
			return h
		}
	} () : f;
	return {
		isJSReady: function() {
			return ! 0
		},
		getAllowDomain: function() {
			var a = a || ["www.meilishuo.com", "newlab.meilishuo.com", "newtest.meilishuo.com", "wwwtest.meilishuo.com", "cdjdev.meilishuo.com", "xhdev.meilishuo.com", "rwdev.meilishuo.com", "nanodev.meilishuo.com"];
			return a
		},
		flashReadyHandler: function() {
			c = !0;
			for (var a = 0,
			b = g.callback.length; a < b; a++) g.callback[a]()
		},
		setCookie: function(a, b, c) {
			return e.setCookie(a, b, c)
		},
		getCookie: function(a) {
			return e.getCookie(a)
		},
		removeCookie: function(a) {
			return e.removeCookie(a)
		},
		setSession: function(a, b, c)... ("jquery"),
		d = a("component/urlHandle"),
		e = a("component/dialog"),
		f = a("component/shareTmp"),
		g = a("app/eventHover"),
		h = a("app/followSome");
		return {
			success: function() {
				c("#sendemail").bind("click",
				function() {
					var a = "/user/reg/sendemail",
					b = {
						email: c(".regEmail").text()
					},
					d = function(a) {
						alert("发送成功，请稍后注意查收哦！")
					};
					c.post(a, b, d, "json")
				})
			},
			selectStyle: function(a, b) {
				var e = a || ".registerLikeStyle li",
				f = b || "span";
				g.hoverShow(e, f),
				c(e).bind("click",
				function(a) {
					var b = encodeURIComponent(c(this).find("h4").text()),
					e = Meilishuo.config.server_url + "user/register/step4" + "?type_name=" + b + "&frm=" + b;
					d.redirect(e)
				})
			},
			selectGroup: function() {
				h({
					addBtn: ".addGroupFollow",
					removeBtn: ".removeGroupFollow",
					hoverBtn: ".removeGroupFollow",
					followStyle: "btn",
					unfollowStyle: "followed",
					submitBtn: "#registerLikeGroupBtn",
					url: "/user/reg/finish"
				}),
				c(".removeGroupFollow").live("click",
				function() {
					if (c(".removeGroupFollow").size() === 0) {
						var a = this,
						b = f("registerLikeInfoTpl");
						e.meiliDialog({
							dialogTitle: "提示",
							dialogWidth: 330,
							dialogContent: b,
							onStart: function() {
								c(a).click(),
								c(".registerLikeInfoWin .btn").bind("click",
								function() {
									c(".close_z").click()
								})
							}
						})
					}
				}),
				c(".groupCon").click(function() {
					c("#registerLikeGroupBtn").click()
				})
			}
		}
	}); fml.define("app/register", ["jquery", "component/validate", "component/focus", "component/passFocus", "component/urlHandle", "component/dialog", "component/shareTmp", "app/checkcode"],
	function(a, b) {
		var c = a("jquery"),
		d = a("component/focus"),
		e = a("component/passFocus"),
		f = a("component/urlHandle"),
		g = a("component/validate"),
		h = a("component/shareTmp"),
		j = a("component/dialog"),
		k = a("app/checkcode"),
		l = f.getParams(window.location.href.toString());
		return function() {
			var a = ["#mlsEmail", "#mlsName", "#vmPass", "#vmConfirmPass", "#checkcode"],
			b = [["#vmPass", "#mlsPass"], ["#vmConfirmPass", "#mlsConfirmPass"]];
			for (i in a) d.inputFocus(a[i]);
			for (i in b) e.passwordFocus(b[i][1], b[i][0]);
			c("#mlsPass").focus(function() {
				c("#vmConfirmPass").parent().show(),
				c("#checkcode").parent().show(),
				c(".checkImage").find("img").attr("isblank") === "true" && (c(".checkImage").find("img").attr("isblank", "false"), c(".checkImage").click())
			});
			var m = function() {
				c(".genderBox").children("input, label").unbind("click", m),
				c(".genderBox .registerInfoMessage").css("display", "inline");
				var a = setTimeout(function() {
					c(".genderBox .registerInfoMessage").fadeOut(100,
					function() {
						c(".genderBox").children("input, label").bind("click", m)
					})
				},
				4e3)
			};
			c(".genderBox").children("input, label").bind("click", m);
			var n = function() {
				k(function() {
					c(".checkImage").unbind("click").parents(".checkImage").next(".good, .bad").attr("class", "").next(".registerErrorMessage").hide();
					var a = setTimeout(function() {
						c(".checkImage").bind("click", n)
					},
					600)
				})
			};
			c(".checkImage").bind("click", n);
			var o = function() {
				var a = "/user/reg/action",
				b = {
					email: c("[name=email]").val(),
					nickname: c("[name=nickname]").val(),
					password: c("[name=password]").val(),
					confirmpassword: c("[name=confirm_password]").val(),
					gender: c("[name=gender]:checked").val(),
					agreement: c("[name=agreement]")[0].checked,
					checkcode: c("[name=checkcode]").val()
				};
				l.invitecode != "" && (b.invitecode = l.invitecode);
				var d = function(a) {
					var b = Meilishuo.config.server_url;
					a.url === 1 ? b += "user/register/success": a.url === 2 && (b += "guang/hot"),
					f.redirect(b)
				};
				if (b.gender == "男") {
					var e = h("noBoyTpl");
					return j.meiliDialog({
						dialogTitle: "美丽提示",
						dialogWidth: 420,
						dialogContent: e
					}),
					!1
				}
				if (b.agreement == 0) return ! 1;
				c.post(a, b, d, "json")
			},
			p = "registerForm",
			q = {
				email: {
					"req=电子邮箱": "你还没有填写电子邮箱哦。",
					email: "电子邮箱格式有误，请重输！"
				},
				nickname: {
					"req=昵称": "你还没有填写昵称哦。",
					"maxlen=20": "支持中英文、数字、下划线，限长10个汉字。"
				},
				password: {
					"minlen=6": "输入密码需在6位到32位间。",
					"maxlen=32": "输入密码需在6位到32位间。"
				},
				confirm_password: {
					"compare=password": "两次密码输入不一致，请重新输入。",
					"minlen=6": "输入密码需在6位到32位间。"
				},
				checkcode: {
					"req=验证码": "你还没有填写验证码哦。",
					"minlen=4": "输入验证码需要4位。"
				},
				agreement: {
					selectradio: "需要同意美丽说服务使用协议。"
				}
			},
			r = {
				"showmsgbyline=registerErrorMessage": "",
				"showmsgforsubmit=registerBtn": o
			},
			s = {
				success: "strong=good",
				error: "strong=bad",
				isExist: {
					email: function(a) {
						var b = "/user/reg/validate",
						d = {
							rule: "email",
							data: c("[name=email]").val()
						},
						e = function(b) {
							b == 1 ? a("邮箱已经存在。") : b == 3 ? a("邮箱格式错误。") : a("")
						};
						c.post(b, d, e, "json")
					},
					nickname: function(a) {
						var b = "/user/reg/validate",
						d = {
							rule: "nickname",
							data: c("[name=nickname]").val()
						},
						e = function(b) {
							b == 2 ? a("用户名已经存在。") : b == 4 ? a("支持中英文、数字、下划线，限长10个汉字。") : b == 5 ? a("用户名已经存在。") : a("")
						};
						c.post(b, d, e, "json")
					},
					checkcode: function(a) {
						var b = "/user/reg/validate",
						d = {
							rule: "captcha",
							data: c("[name=checkcode]").val()
						},
						e = function(b) {
							b == 6 ? (c(".checkImage").click(), a("验证码错误。")) : a("")
						};
						c.post(b, d, e, "json")
					}
				}
			};
			g.validate(p, q, r, s)
		}
	}); fml.use(["app/register", "app/registerLike"],
	function() {
		switch (Meilishuo.config.controller) {
		case "register_form":
			$("#login_more").bind("click",
			function() {
				$(this).hide().nextAll("a").show()
			}),
			this.register();
			break;
		case "register_success":
			this.registerLike.success();
			break;
		case "register_step3":
			this.registerLike.selectStyle();
			break;
		case "register_step4":
			this.registerLike.selectGroup();
			break;
		default:
		}
	}), fml.use("app/cleanMsg",
	function(a) {
		a.msgFunc()
	}), fml.use("app/setting",
	function() {}), fml.define("page/register", [],
	function() {});