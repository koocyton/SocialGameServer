(function($){

	var $ = jQuery;

	$.extend({

		KTAnchor: {

			// version
			version : "1.0.1",

			// ajax : set default response_container
			response_container : ".body-content-right",

			// paging : set default paging_container
			paging_container : "#paging-container",

			// paging : set default paging_limit
			paging_limit : 30,

			// paging : the paging url parment ,  &.. or /..
			paging_symbol : "&cc",

			// 鼠标滚动的积累值
			wheel_delta : 0,

			// 如果是移动端浏览器
			mobile_browser : !!navigator.userAgent.match(/AppleWebKit.*Mobile.*/)||!!navigator.userAgent.match(/AppleWebKit/),

			// 默认进度条没有完成
			request_process_complete : false,

			/* init parment
			 *
			 * options.response_container
			 * options.paging_limit
			 * options.paging_symbol
			 */
			init: function(options){

				// init response_container
				if (typeof(options.response_container)=="string") {
					this.response_container = options.response_container;
				}
				// init paging_limit
				if (typeof(options.paging_limit)=="number" && options.paging_limit>=1) {
					this.paging_limit = options.paging_limit;
				}
				// init paging_symbol
				if (typeof(options.paging_symbol)=="string" && /[\/|&]\w+/.test(options.paging_symbol)) {
					this.paging_symbol = options.paging_symbol;
				}
				// init paging_container
				if (typeof(options.paging_container)=="string") {
					this.paging_container = options.paging_container;
				}
				// init dropdown_container
				if (typeof(options.dropdown_container)=="string") {
					this.dropdown_container = options.dropdown_container;
				}
				// init treemenu_container
				if (typeof(options.treemenu_container)=="string") {
					this.treemenu_container = options.treemenu_container;
				}
				// init mousewheel_container
				if (typeof(options.scroll_container)=="string") {
					this.scroll_container = options.scroll_container;
				}
			},

			showSlidMessage: function(message){
				var alert_elt = $(".alert");
				if (alert_elt.data("first-click")==null) {
					// 不冒泡
					alert_elt.bind("click", function(e){
						e.stopPropagation();
					});
					//
					alert_elt.data("first-click", "first-click");
				}
				// 弹出和弹出动画
				$(".alert-message").html(message);
				alert_elt.css({bottom:30,opacity:0,display:"block"}).animate({bottom:6,opacity:1}, 'fast' , function(){
					// 绑定界面点击时关闭弹出
					$(document.body).bind("click touchend", function(e){
						// 关闭动画
						alert_elt.animate({opacity: 0}, function(){
							$(".alert-message").html(message);
							alert_elt.css({display:"none"});
							$(document.body).unbind("click touchend");
						});
						e.stopPropagation();
					});
				});
			},

			inputError: function(input, message){
				// 关闭之间弹出的错误信息
				$(".input-error-pop").css("display", "none");
				// 获取 input , input 和 错误信息存放的容器，错误信息的层
				var $input = $(input);
				var $input_box = $input.parent();
				var $error_pop = $input_box.find(".input-error-pop");
				// 如果错误信息层不存在，就创建
				if ($error_pop.length==0) {
					$input_box.append("<dd class=\"input-error-pop radius-5\"><b class=\"angle-up input-error-angle\"></b>"+message+"</dd>");
				}
				// 显示这个错误信息
				$error_pop.css("display", "block");
				$input.context.focus();

				if ($input.data("change")!=true) {
					$input.bind('input change blur',function(ev){
						if ($input_box.children(".input-error-pop").exist()) {
							$input_box.children(".input-error-pop").css("display", "none");
							$input_box.children(".input-error-pop").remove();
						}
					});
					$input.data("change", true);
				}
			},

			success: function(container, responseText){
				// 如果获取到一个 json
				if (typeof(responseText)=="object" && typeof(responseText.action)=="string") {
					if (responseText.action=="redirect") {
						window.location = responseText.url;
					}
					else if (responseText.action=="showMessage") {
						$.KTAnchor.showSlidMessage(responseText.message);
					}
				}
				else if (typeof(responseText)=="object") {
					// 填充
					$(container).empty();
					$(container).html("{<div style=\"padding-left:20px;\">" + $.KTPrintr(responseText) + "</div>}");
				}
				else if (/^<script>(.+)<\/script>$/.test(responseText)) {
					var response = responseText.match(/<script>(.+)<\/script>/);
					eval(response[1]);
				}
				// 请求到的文本
				else {
					if (!responseText || responseText.length<1) {
						return;
					}
					// 填充
					$(container).empty();
					$(container).html(responseText);
					// 填充完后重新设定填充区域内的 KTLoader
					$(container).KTLoader();
					// 检查有无滚动条需要重置
					// $(container).parent($.KTAnchor.scroll_container).ktScrollReset();
				}
			},

			error: function(container, XMLHttpRequest){
				if (typeof(XMLHttpRequest.responseJSON)=="object" && typeof(XMLHttpRequest.responseJSON.action)=="string") {
					var responseJSON = XMLHttpRequest.responseJSON;
					if (responseJSON.action=="redirect") {
						window.location = responseText.url;
					}
					else if (responseJSON.action=="showMessage") {
						$.KTAnchor.showSlidMessage("Error : " + XMLHttpRequest.status + ' - ' + responseJSON.message);
					}
				}
				else if (typeof(XMLHttpRequest.responseJSON)=="object") {
					// 填充
					$(container).empty();
					$(container).html("{<div style=\"padding-left:20px;\">" + $.KTPrintr(XMLHttpRequest.responseJSON) + "</div>}");
				}
				else {
					$.KTAnchor.showSlidMessage("Error : " + XMLHttpRequest.status + ' ' +  XMLHttpRequest.statusText);
				}
			},

			begin: function(){
				// $.KTLog("JQuery.KTAnchor.begin");
			},

			complete: function(container, XMLHttpRequest){
				// $.KTLog("JQuery.KTAnchor.complete : " + container);
			},

			// 加载界面
			ajaxLoader : function(url){
				var container = $.KTAnchor.response_container;
				window.history.pushState(null, "", url);
				$.KTAnchor.begin();
				// ajax 请求，并回调
				$.KTAjax(url, "GET", null, null,
					// 成功
					function(responseText){
						$.KTAnchor.success(container, responseText);
					},
					// 错误
					function(XMLHttpRequest){
						$.KTAnchor.error(container, XMLHttpRequest);
					},
					// 结束 ( 成功或失败后 )
					function(XMLHttpRequest){
						$.KTAnchor.complete(container, XMLHttpRequest);
						$.KTAnchor.treemenuSelected(url);
					}
				);
			},

			// 弹出窗口
		    popupLoader : function(url){
		    	// 进度条开始
				//$.KTAnchor.setRequestProcess(0);
		    	var popup_elt = $(".popup-modal");
		    	var body_elt = $(".popup-modal .modal-body").empty();
		    	var title_elt = $(".popup-modal .modal-title").html("Loading...");

                // 特效
                if (popup_elt.data("on-show-set")==null) {
                    popup_elt.on('show.bs.modal', function () {
                        window.setTimeout(function(){
                            $(".body-content").addClass("blur-5").next(".navbar").addClass("blur-5");
                        }, 150);
                    });
                    popup_elt.on('hide.bs.modal', function () {
                        $(".body-content").removeClass("blur-5").next(".navbar").removeClass("blur-5");
                    });
                }

                // ajax 请求，并回调
                $.KTAjax(url, "GET", null, null,
                    // 成功
                    function(responseText){
                        popup_elt.modal('show');
                        $.KTAnchor.success(body_elt, responseText);
                        // 加载 title
                        var popup_title = body_elt.find(".popup-title").html();
                        title_elt.html(popup_title ? popup_title : 'Loading...');
                    },
                    // 错误
                    function(XMLHttpRequest){
                        $.KTAnchor.error(body_elt, XMLHttpRequest);
                    },
                    // 结束 ( 成功或失败后 )
                    null
                );
		    },

			// 左侧菜单被选中
		    treemenuSelected : function(location_url) {
		        // 获取当前 URL
				// var location_url = url;//'http://panda.doopp.com/admin/tracklog';//window.location.href;
				var url_pattern  = /^https?:\/\/[^\/]+(\/[^\/\?]+)(\/[^\/\?]+)?(\/[^\/\?]+)?(\/[^\/\?]+)?/;
				//
				if (location_url.substr(0,1)=="/") {
					url_pattern = /^(\/[^\/\?]+)(\/[^\/\?]+)?(\/[^\/\?]+)?(\/[^\/\?]+)?/;
				}
				var url_match    = location_url.match(url_pattern);
				if (url_match==null) {
					return null;
				}
				// 从长到短获取节点
				var menu_elt = $($.KTAnchor.treemenu_container + " a[href='" + url_match[1] + url_match[2] + url_match[3] + url_match[4] + "']");
				var span_elt = $($.KTAnchor.treemenu_container + " span[href='" + url_match[1] + url_match[2] + url_match[3] + url_match[4] + "']");

				if (!menu_elt.exist() && !span_elt.exist()) {
					menu_elt = $($.KTAnchor.treemenu_container + " a[href='" + url_match[1] + url_match[2] + url_match[3] + "']");
					span_elt = $($.KTAnchor.treemenu_container + " span[href='" + url_match[1] + url_match[2] + url_match[3] + "']");
				}

				if (!menu_elt.exist() && !span_elt.exist()) {
					menu_elt = $($.KTAnchor.treemenu_container + " a[href='" + url_match[1] + url_match[2] + "']");
					span_elt = $($.KTAnchor.treemenu_container + " span[href='" + url_match[1] + url_match[2] + "']");
				}

				if (!menu_elt.exist() && !span_elt.exist()) {
					menu_elt = $($.KTAnchor.treemenu_container + " a[href^='" + url_match[1] + "']");
					span_elt = $($.KTAnchor.treemenu_container + " span[href='" + url_match[1] + "']");
				}

				if (span_elt.exist()) {
					menu_elt = span_elt.parents("a.tree-menu");
				}
				else if (menu_elt.parent().prev().hasClass("tree-menu-close")) {
					menu_elt.parent().prev().trigger("click");
				}
				$(".tree-select-menu").removeClass("tree-select-menu");
				menu_elt.addClass("tree-select-menu");
		    },

			// show request process
			setRequestProcess: function(process_length){
				// 进度的长度
				var progress_elt = $(".request-progress");
				// 完整进度的长度
				var full_process_length = (this.request_process_complete==true) ? $(window).width() : $(window).width() * 0.8;
				// 如果没有进度条则创建一个
				if (progress_elt.length==0) {
					progress_elt = $('<div class="request-progress"></div>').appendTo(document.body);
				}
				// 当前的进度条长度
				var new_process_length = (this.request_process_complete==true) ? (process_length+1)*1.28 : process_length + 1;
				progress_elt.css({width:new_process_length, display:'block'});
				if (new_process_length<=full_process_length) {
					setTimeout(function(){$.KTAnchor.setRequestProcess(new_process_length)}, 1);
				}
				else if (this.request_process_complete==true && new_process_length>full_process_length) {
					progress_elt.delay(500).fadeOut("fast");
					this.request_process_complete=false;
				}
			},

			// remove request process
			completeRequestProcess: function(){
				this.request_process_complete=true;
			},
		},

		KTDateFormat: function(ts, dt)   {
			var now = ts ? new Date(parseInt(ts)) : new Date();
			var y = now.getFullYear();
			var m = ((now.getMonth()+1)<10?"0":"")+(now.getMonth()+1);
			var d = (now.getDate()<10?"0":"")+now.getDate();
			var h = (now.getHours()<10?"0":"")+now.getHours();
			var i = (now.getMinutes()<10?"0":"")+now.getMinutes();
			var s = (now.getSeconds()<10?"0":"")+now.getSeconds();
			var nowDate = y + "-" + m + "-" + d;
			var nowDatetime = nowDate + " " + h + ":" + i + ":" + s;
			return (dt) ? nowDatetime : nowDate;
		},

		// confirm
		confirm : function(message, success) {
			var confirm_elt = $(".confirm-modal");
			var success_btn = confirm_elt.find(".btn-success");
			var body_elt = confirm_elt.find(".modal-body");

            // 特效
            if (confirm_elt.data("on-show-set")==null) {
                confirm_elt.on('show.bs.modal', function () {
                	window.setTimeout(function(){
                        $(".body-content").addClass("blur-5").next(".navbar").addClass("blur-5");
					}, 150);
                });
                confirm_elt.on('hide.bs.modal', function () {
                    $(".body-content").removeClass("blur-5").next(".navbar").removeClass("blur-5");
                });
            }

			$(body_elt).html(message);
			confirm_elt.modal('show');
			$.KTAnchor.confirm_callback = success;
			if (success_btn.data("click-confirm")==null) {
				success_btn.data("click-confirm","click-confirm");
				$(success_btn).on("click", function(e){
					if($.isFunction($.KTAnchor.confirm_callback)) {
						$.KTAnchor.confirm_callback();
					}
					confirm_elt.modal('hide');
				});
			}
		},

		// print_r arguments
		KTLog: function(){
			if (window.console && window.console.log && arguments.length>=1){
				window.console.log("arguments.length : " + arguments.length);
				for (var ii=0; ii<arguments.length; ii++){
					window.console.log(arguments[ii]);
				}
			}
		},

		// open iframe
		KTIframe: function(el, url){
			var iframe = $('<iframe frameborder="0" src="" style="width:100%;height:100%;"></iframe>');
			iframe.attr("src", url);
			$(el).empty();
			$(el).append(iframe);
		},

		//
		KTPrintr: function(theObj) {
			var retStr = '';
			if (typeof theObj == 'object') {
				retStr += '<div>';
				for (var p in theObj) {
					if (typeof theObj[p] == 'object') {
						retStr += '<div><b>['+p+'] => ' + typeof(theObj) + '</b></div>';
						retStr += '<div style="padding-left:25px;">' + $.KTPrintr(theObj[p]) + '</div>';
					} else {
						retStr += '<div>['+p+'] => <b>' + theObj[p] + '</b></div>';
					}
				}
				retStr += '</div>';
			}
			return retStr;
		},

		// http request function
		KTAjax: function(url, method, data, headers, success, error, complete){
			$(document.body).trigger("click");
			// 进度条开始
			$.KTAnchor.setRequestProcess(0);
			// stop before one ajax request
			if (typeof(window.currentKTAjax)=="object") {
				try{window.currentKTAjax.abort()}catch(e){;}
			}
			// set headers
			if ($.type(headers)!="object" || $.isEmptyObject(headers)) {
				headers = {};
			}
			headers['Ajax-Request'] = "jQuery.KTAnchor";
			var contentType = false;
			if (method=="POST") {
				contentType = (data instanceof FormData) ? false : "application/x-www-form-urlencoded; charset=UTF-8";
			}
			// set ajax request
			window.currentKTAjax = $.ajax({
				"url"  : url,
				"type" : method,
				"data" : data,
				"contentType" : contentType,
				"processData" : false,
				"headers" : headers,
				"success" : function(responseText) {
					if ($.isFunction(success)) success(responseText);
				},
				"error" : function(XMLHttpRequest) {
					if ($.isFunction(error)) error(XMLHttpRequest);
				},
				"complete" : function(XMLHttpRequest) {
					$.KTAnchor.completeRequestProcess();
					if ($.isFunction(complete)) complete(XMLHttpRequest);
				}
			});
		},

		KTTreeMenuHTML: {

			getMenuItemHtml: function(menu_item, menu_level) {
				var pushstate_html = "";
				var badge_html = "";
				if (typeof(menu_item.pushstate)=="string" && menu_item.pushstate=="no") {
					pushstate_html = "pushstate=\"no\"";
				}
				if (typeof(menu_item.badge)=="string") {
					badge_html = "<span class=\"badge\">" + menu_item.badge + "</span>";
				}
				return '<a href="'+menu_item.href+'" '+pushstate_html+' class="tree-menu tree-menu-'+menu_level+'"><div>' + menu_item.text + "　" + badge_html + '</div></a>';
			},

			getMenuFolderHtml: function(menu_item, menu_level) {
				if (typeof(menu_item.href)=="string") {
					var icon = typeof(menu_item.icon)=="string" ? menu_item.icon : "cog";
					menu_item.text += "<span href=\"" + menu_item.href + "\" class=\"glyphicon glyphicon-" + icon + "\"></span>";
				}
				if (menu_level=="0") {
					return '<a href="javascript:;" class="tree-menu tree-menu-0"><div>'+menu_item.text+'</div></a>';
				}
				else if (menu_level=="1") {
					var toggle_class = menu_item.open ? "tree-menu-open" : "tree-menu-close" ;
					return '<a href="javascript:;" class="tree-menu tree-menu-1 '+toggle_class+'"><div>'+menu_item.text+'</div></a>';
				}
			},

			getMenuHtml: function(menus_data, menu_level) {
				// 初始化 html
				var menu_html = "";
				// 初始化菜单等级
				var menu_level = (typeof(menu_level)!="number") ? 0 : menu_level;
				// 开始循环
				$.each(menus_data, function(name, menu_data) {
					// 如果有子菜单
					if ($.isArray(menu_data.menus)) {
						menu_html += $.KTTreeMenuHTML.getMenuFolderHtml(menu_data, menu_level);
						if (menu_level==0) {
							menu_html += '<div style="display: block;">'+$.KTTreeMenuHTML.getMenuHtml(menu_data.menus, 1+menu_level)+'</div>';
						}
						else if (menu_level==1) {
							var display = (typeof(menu_data.open)!="undefined" && menu_data.open==true) ? "block" : "none" ;
							menu_html += '<div style="display: '+display+';">'+$.KTTreeMenuHTML.getMenuHtml(menu_data.menus, 1+menu_level)+'</div>';
						}
					}
					// 如果没有子菜单
					else if (typeof(menu_data.href)=="string") {
						menu_html += $.KTTreeMenuHTML.getMenuItemHtml(menu_data, menu_level);
					}
				});
				return (menu_level==0) ? '<div class="'+$.KTAnchor.treemenu_container.substr(1)+'">'+menu_html+'</div>' : menu_html;
				// return menu_html;
			}
		}
	});

	$.fn.extend({

		exist: function() {
			return ($(this).length>=1) ? true : false;
		},

		actionApplication: function(CallFunction) {
			var icon = $(this).find("img");
			CallFunction = $.isFunction(CallFunction) ? CallFunction : $.noop();
			icon.animate({top:'-20px'}, 230).animate({top:'0px'}, 130).animate({top: '-10px'}, 100, "", CallFunction).animate({top:'0px'}, 100).animate({top: '-5px'}, 50).animate({top:'0px'},  50);
		},

		KTLoader: function() {
			// 加载
			$(this).KTPaging().KTTreeMenu().KTAnchor().KTForm().KTInputBind();
			// bootrap 的 tooltip 需要手动激活
			$(this).find("[data-toggle='tooltip']").tooltip();
		},

		KTInputBind: function() {
			$(this).tagsInputBind().timeInputBind().uploadInputBind().htmlEditorBind();
		},

		KTAnchor : function(success, error, begin, complete) {
			// 取得 某文档下 所有没有被标注为原生的 anchor
			this.find("a").each(function(key, anchor){
				// jQuery 对象
				var $anchor = $(anchor);
				// 如果..
                if (typeof($anchor.attr('native'))!="undefined") {
                    if ($anchor.data("mouse-click")==null) {
                        // 不冒泡
                        $anchor.bind("click", function(e){
                            e.stopPropagation();
                        });
                        //
                        $anchor.data("mouse-click", "mouse-click");
                    }
                    return;
                }
				// 如果是 <a href="javascript:..." 也是不能去绑定
				if (/^javascript\:/.test($anchor.attr("href"))) return;
				// 如果是 # 也是不能绑定
				if ($anchor.attr("href")=="#") return;
				// 如果特别标注 <a> 不绑定事件
				// if ($anchor.attr("native")!=null) return;
				// 绑定点击事件
				$anchor.on("click", function() {
					var anchor_action = function() {
						// 聚焦会使得点击处框上虚线
						anchor.blur();
						// 获取要请求的地址
						var request_url = $anchor.attr("href");
						// 获取当前的地址
						var request_ref = window.location.href;
						// 如果设置了  <a pushstate="no" ... > 那么不做 url pushState
						if (typeof($anchor.attr("pushstate"))=="undefined" || $anchor.attr("pushstate")!="no") {
							window.history.pushState(null, "", request_url);
						}
						var container = $.KTAnchor.response_container;
						if (typeof($anchor.attr("container"))!="undefined" && $anchor.attr("container").length>1) {
							container = $anchor.attr("container");
						}
						// 开始
						$.isFunction(begin) ? begin() : $.KTAnchor.begin();
						// set header
						var header = null;
						if ($.type($anchor.attr("header"))=="string") {
							header = $.parseJSON($anchor.attr("header"));
						}
						// ajax 请求，并回调
						$.KTAjax(request_url, "GET", null, header,
							// 成功
							function(responseText){
								$.isFunction(success) ? success(container, responseText) : $.KTAnchor.success(container, responseText);
							},
							// 错误
							function(XMLHttpRequest){
								$.isFunction(error) ? error(container, XMLHttpRequest) : $.KTAnchor.error(container, XMLHttpRequest);
							},
							// 结束 ( 成功或失败后 )
							function(XMLHttpRequest){
								$.isFunction(complete) ? complete(container, XMLHttpRequest) : $.KTAnchor.complete(container, XMLHttpRequest);
								if (typeof($anchor.attr("pushstate"))=="undefined" || $anchor.attr("pushstate")!="no") {
									$.KTAnchor.treemenuSelected(request_url);
								}
							}
						);
					}
					// 如果有 confirm 属性
					if (typeof($anchor.attr("confirm"))!="undefined" && $anchor.attr("confirm").length>1) {
						$.confirm($anchor.attr("confirm"), anchor_action);
					}
					else {
						anchor_action();
					}
					// 防止链接点击生效
					return false;
				});
			});
			// 返回 JQuery 对象
			return this;
		},

		KTForm : function(inputError, success, error, begin, complete) {
			// 查找 form，如果 native="yes" 则跳过
			this.find("form[native!='yes']").each(function(key, form){
				// jQuery 对象
				var $form = $(form);
				$form.find(":input").each(function(key, input_elt){
					$(input_elt).bind("change keyup", function(){
						var submit_btn = $form.find("button[type='submit']");
						var button_class = submit_btn.attr("button-class");
						var submit_class = (button_class && button_class=="submit-btn") ? "submit-btn" : "button-btn";
						if ($form.checkInputs()){
							submit_btn.removeClass("disable-btn").addClass(submit_class);
						}
						else {
							submit_btn.removeClass(submit_class).addClass("disable-btn");
						}
					});
				});
				// 将 form 绑定 submit 事件
				$form.on("submit", function(){
                    // 将 ckeditor 的内容处理
                    for ( instance in CKEDITOR.instances ) {
                        CKEDITOR.instances[instance].updateElement();
                    }
					// 函数
					var submit_action = function(){
						// 检查表单
						// 自定义的错误处理
						if ($.isFunction(inputError)) {
							if (!$form.checkInputs(inputError)) return false;
						}
						// 默认的错误处理，会输出到浏览器的控制台
						else {
							if (!$form.checkInputs($.KTAnchor.inputError)) return false;
						}
						// 获取 url
						var request_url = $form.attr("action");
						// 默认是 Form method 是 POST
						var method = "POST";
						// 获取表单数据
						var data = $form.find("input[type='file']").exist() ? new FormData($form.context) : $form.serialize();
						// 获取返回数据将填充哪个节点
						var container = $.KTAnchor.response_container;
						if (typeof($form.attr("container"))!="undefined" && $form.attr("container").length>1) {
							container = $form.attr("container");
						}
						// 开始
						$.isFunction(begin) ? begin() : $.KTAnchor.begin();

						var on_success = $form.attr("success");
						if (typeof(on_success)=="string" && on_success.length>0) {
							success = function(container, responseText) {
								
							}
						}

						// 如果 form 是 GET, 适合用来搜索
						if ($form.attr("method")=="get") {
							// 将字段拼接在 action 后
							$form.find("input").each(function(key, input_elt){
								input_elt = $(input_elt);
								if (input_elt.attr("name").length>0 && input_elt.val().length>0) {
									if (/\?/.test(request_url)) {
										request_url = request_url + "&" + input_elt.attr("name") + "=" + encodeURI(input_elt.val());
									}
									else {
										request_url = request_url + "?" + input_elt.attr("name") + "=" + encodeURI(input_elt.val());
									}
								}
							});
							method = "GET";
							data = null;
							// 如果设置了  <a pushstate="no" ... > 那么不做 url pushState
							if (typeof($form.attr("pushstate"))=="undefined" || $form.attr("pushstate")!="no") {
								window.history.pushState(null, "", request_url);
							}
						}
						// set header
						var header = null;
						if ($.type($form.attr("header"))=="string") {
							header = $.parseJSON($form.attr("header"));
						}
						// ajax 请求，并回调
						$.KTAjax(request_url, method, data, header,
							// 成功
							function(responseText){
								$.isFunction(success) ? success(container, responseText) : $.KTAnchor.success(container, responseText);
							},
							// 错误
							function(XMLHttpRequest){
								$.isFunction(error) ? error(container, XMLHttpRequest) : $.KTAnchor.error(container, XMLHttpRequest);
							},
							// 结束 ( 成功或失败后 )
							function(XMLHttpRequest){
								$.isFunction(complete) ? complete(container, XMLHttpRequest) : $.KTAnchor.complete(container, XMLHttpRequest);
							}
						);
					}
					// 如果有 confirm 属性
					if (typeof($form.attr("confirm"))!="undefined" && $form.attr("confirm").length>1) {
						$.confirm($form.attr("confirm"), submit_action);
						return false;
					}
					submit_action();
					// 禁止表单继续提交
					return false;
				});
				$($form.find(":input")[0]).trigger("change");
			});
			// 返回 JQuery 对象
			return this;
		},

		checkInputs : function(inputError) {
			var field_ok = true;
			$(this).find(":input").each(function(key, input_elt){
				var validation = $(input_elt).attr("validation");
				if (typeof(validation)=="string" && validation.length>=1) {
					var input_value = $(input_elt).val();
					var valid_match = null;
					// 不能为空
					if (validation=="/!empty/") {
						if (input_value.length<1) {
							if ($.isFunction(inputError)) inputError(input_elt, "Can not is empty");
							field_ok = false;
							return false;
						}
					}
					// 请输入邮箱
					else if (valid_match=validation.match(/\/email:(.+)\//)) {
						if (!/^([0-9A-Za-z\-_\.]+)@([0-9A-Za-z\-_\.]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g.test(input_value)){
							if ($.isFunction(inputError)) inputError(input_elt, valid_match[1]);
							field_ok = false;
							return false;
						}
					}
					// 请输入数字，字母，下划线，横线
					else if (valid_match=validation.match(/\/word-number:(.+)\//)) {
						if (!/^[0-9A-Za-z\-_]+$/g.test(input_value)){
							if ($.isFunction(inputError)) inputError(input_elt, valid_match[1]);
							field_ok = false;
							return false;
						}
					}
					// 请输入数字
					else if (valid_match=validation.match(/\/number:(.+)\//)) {
						if (!/^\d+$/g.test(input_value)){
							if ($.isFunction(inputError)) inputError(input_elt, valid_match[1]);
							field_ok = false;
							return false;
						}
					}
					// 请输入密码，并不小于 8 位长
					else if (valid_match=validation.match(/\/password:(.+)\//)) {
						if (input_value.length<8) {
							if ($.isFunction(inputError)) inputError(input_elt, valid_match[1]);
							field_ok = false;
							return false;
						}
					}
					// 修改密码，要么留空，要么不小于8 位长
					else if (valid_match=validation.match(/\/edpassword:(.+)\//)) {
						if (input_value.length>=1) {
							if (input_value.length<8) {
								if ($.isFunction(inputError)) inputError(input_elt, valid_match[1]);
								field_ok = false;
								return false;
							}
						}
					}
					// 重复输入密码要和前面输入的密码一样
					else if (valid_match=validation.match(/\/repassword:(.+):(.+)\//)) {
						var password_value = $(form_elt).find("input[name="+valid_match[1]+"]").val();
						if (password_value!=input_value) {
							if ($.isFunction(inputError)) inputError(input_elt, valid_match[2]);
							field_ok = false;
							return false;
						}
					}
					// 如果空就弹出后面的提示
					else if (valid_match=validation.match(/\/!empty:(.+)\//)) {
						if (input_value.length<1) {
							if ($.isFunction(inputError)) inputError(input_elt, valid_match[1]);
							field_ok = false;
							return false;
						}
					}
					// 手机号判断
					else if (valid_match=validation.match(/\/mobile:(.+)\//)) {
						if (!/^1\d{10}$/g.test(input_value)){
							if ($.isFunction(inputError)) inputError(input_elt, valid_match[1]);
							field_ok = false;
							return false;
						}
					}
					// 年-月-日 时间判断
					else if (valid_match=validation.match(/\/date:(.+)\//)) {
						if (!/^[1|2]\d{3}\-(0[1-9]|1[0-2])\-(0[1-9]|[1-2][0-9]|3[0-1])$/g.test(input_value)){
							if ($.isFunction(inputError)) inputError(input_elt, valid_match[1]);
							field_ok = false;
							return false;
						}
					}
					// 年-月-日 时:分:秒时间判断
					else if (valid_match=validation.match(/\/datetime:(.+)\//)) {
						if (!/^[1|2]\d{3}\-(0[1-9]|1[0-2])\-(0[1-9]|[1-2][0-9]|3[0-1]) ([0|1][0-9]|2[0-4])\:([0-5][0-9])\:([0-5][0-9])$/g.test(input_value)){
							if ($.isFunction(inputError)) inputError(input_elt, valid_match[1]);
							field_ok = false;
							return false;
						}
					}
				}
			});
			return field_ok;
		},

		timeInputBind : function()
		{
			$(this).find("input.date").each(function(key, date_input) {
				// 绑定
				$(this).datetimepicker({
					 timepicker:false,
					 lang:"ch",
					 format:'Y-m-d'
				});
			});

			$(this).find("input.datetime").each(function(key, datetime_input) {
				// 绑定
				$(this).datetimepicker({
					 timepicker:true,
					 lang:"ch",
					 format:'Y-m-d H:i:s'
				});
			});

			$(this).find("input.time").each(function(key, datetime_input) {
				// 绑定
				$(this).datetimepicker({
					 timepicker:false,
					 lang:"ch",
					 format:'H:i:s'
				});
			});
			return this;
		},

        htmlEditorBind: function()
		{
            $(this).find("textarea.ckeditor").each(function(key, elt) {
            	var key = "html-editor-" + Math.floor( Math.random() * 1000000 );
                var ckeditor_elt = $(this);
                ckeditor_elt.attr("id", key);
                CKEDITOR.replace(key, {
                    extraPlugins: "image,table,dialog"
                });
            });
            return this;
		},

		uploadInputBind: function()
		{
			$(this).find("input.upload-input").each(function(key, input_elt) {
				var upload_input = $(this);
				if (!upload_input.hasClass("opacity-0")) {
					upload_input.addClass("opacity-0")
				}
				var upload_frame = $("<div class=\"upload-frame\"></div>").appendTo(upload_input.parent());
				upload_input.appendTo(upload_frame);
				// 绑定事件
				upload_input.change(function() {
					var image_url = $(this).getInputFilePath();
					upload_frame.css("background-image", "url(" + image_url+ ")");
				});
			});

			return this;
		},

		tagsInputBind : function()
		{
			// 搜索范围
			$(this).find("input.tags-input").each(function(key, elt) {
				// 循环处理
				new TagsInput(elt);
			});
			return this;
		},

		KTPaging : function() {
			// 从配置中获取参数配置
			var container = $.KTAnchor.paging_container;
			// 开始查找
			this.find(container).each(function(key, paging_bar){
				// jQuery 对象
				var $paging_elt = $(paging_bar);
				// 获取主要的数据
				var total = 0 + $paging_elt.attr("total");
				var current = 0 + $paging_elt.attr("current");
				var limit = $paging_elt.attr("limit");
				if (typeof(limit)=="undefined") limit = $.KTAnchor.paging_limit;
				// 最大页码 当前页码
				var max_page = Math.ceil(total / limit);
				var current_page = Math.ceil(current / limit);
				// 分页最长显示 7 个
				var page_list = [1,2,3,4,5,6,7];
				// 数组最后一个单元，不能大于 max_page
				if (max_page<=7) {
					page_list = page_list.slice(0, max_page);
				}
				else {
					// 如果当前页前 3 页
					if (current_page-1<=3) {
						page_list = [1,2,3,4,5,6,"...",max_page];
					}
					// 如果当前页为后 3 页
					else if (max_page-current_page<=3) {
						page_list = [1,"...",max_page-5,max_page-4,max_page-3,max_page-2,max_page-1,max_page];
					}
					// 当前页，离最大，最小，都超过 2 个单位
					else {
						page_list = [1,"...",current_page-2,current_page-1,current_page,current_page+1,current_page+2,"...",max_page];
					}
				}
				// class name
				var class_name = $paging_elt.attr("paging_class");
				if (typeof(class_name)=="undefined") {
					class_name = "paging-anchor radius-4";
				}
				// current class name
				var current_class_name = $paging_elt.attr("current_paging_class");
				if (typeof(current_class_name)=="undefined") {
					current_class_name = "paging-current-anchor radius-4";
				}
				// request url
				var request_url = $paging_elt.attr("request_url");
				if (typeof(request_url)=="undefined") {
					request_url = window.location.href;
				}
				// 分页参 连接 URL 的符号
				var paging_symbol = $paging_elt.attr("paging_symbol");
				if (!/[\&|\/]\w+/.test(paging_symbol)) {
					paging_symbol1 = $.KTAnchor.paging_symbol.substr(0, 1);
					paging_symbol2 = $.KTAnchor.paging_symbol.substr(1);
				}
				// 是否发生 pushstate
				var pushstate = $paging_elt.attr("pushstate");
				var pushstate_html = "";
				if (typeof(pushstate)=="string" && pushstate=="no") {
					pushstate_html = "pushstate=\"no\"";
				}
				// 输出页面
				var paging_html = "";
				for(var ii=0; ii<page_list.length; ii++) {
					var m = 1 + ( page_list[ii]-1 ) * limit;
					// $.KTLog(/\/p\/[0-9]+/, new RegExp("\/"+paging_symbol2+"\/[0-9]+"), /[\?\&]p=[0-9]+/, new RegExp("[\\?\\&]"+paging_symbol2+"=[0-9]+"));
					// get href
					if (paging_symbol1=="/") {
						var href = request_url.replace(new RegExp("\/"+paging_symbol2+"\/[0-9]+"), "")+"/"+paging_symbol2+"/"+m;
					}
					else {
						var href = request_url.replace(new RegExp("[\\?\\&]"+paging_symbol2+"=[0-9]+"), "");
						href = (/\?/.test(href)) ? href+"&"+paging_symbol2+"="+m : href+"?"+paging_symbol2+"="+m;
					}
					// 组织 html
					if (page_list[ii]=="...") {
						paging_html += "<li><span>...</span></li>";
					}
					else if (page_list[ii]==current_page){
						paging_html += '<li class="active"><a href="'+href+'" '+pushstate_html+'>'+page_list[ii]+'</a></li>';
					}
					else {
						paging_html += '<li><a href="'+href+'" '+pushstate_html+'>'+page_list[ii]+'</a></li>';
					}
				}
				$paging_elt.html('<nav><ul class="pagination">' + paging_html + '</ul></nav>');
			});
			// 返回 JQuery 对象
			return this;
		},

		KTTreeMenu : function(){
			//  从配置中获取参数
			var container = $.KTAnchor.treemenu_container;
			// 开始遍历
			this.find(container).each(function(key, treemenu_elt){
				// 处理节点，绑定事件
				$(treemenu_elt).setMainMenuEvent();
			});
			return this;
		},

		/* --------------------    tools function    ------------------ */

		// 得到图片的完整路径
		getInputFilePath: function() {
			var file = this.context.files[0];
			var url = null;
			if (window.createObjectURL != undefined) { // basic
				url = window.createObjectURL(file);
			}
			else if (window.URL != undefined) { // mozilla(firefox)
				url = window.URL.createObjectURL(file);
			}
			else if (window.webkitURL != undefined) { // webkit or chrome
				url = window.webkitURL.createObjectURL(file);
			}
			return url;
		},

		setMainMenuEvent : function(){
			var $treemenu_elt = $(this)
			// 开始循环
			$treemenu_elt.children("a").each(function(key, menu_elt) {
				// 取得一个节点
				var $menu_elt = $(menu_elt);
				// 取得下一个节点
				var $next_elt = $menu_elt.next("div");
				// 如果节点后有DIV，说明一个折叠菜单
				if ($next_elt.exist()) {
					$menu_elt.find("span[href]").click(function(e){
						$.KTAnchor.ajaxLoader($(this).attr("href"))
						e.stopPropagation();
					});
					// 绑定点击事件
					$menu_elt.bind("click", function() {
						// 打开菜单
						if ($next_elt.css("display")=="none") {
							// 滑出
							$next_elt.slideDown("fast"); // $next_elt.css("display", "block");
							if ($menu_elt.hasClass("tree-menu-1")) {
								$menu_elt.removeClass("tree-menu-close").addClass("tree-menu-open");
							}
						}
						// 折叠菜单
						else {
							// 滑入
							$next_elt.slideUp("fast"); // $next_elt.css("display", "none");
							if ($menu_elt.hasClass("tree-menu-1")) {
								$menu_elt.removeClass("tree-menu-open").addClass("tree-menu-close");
							}
						}
						// 重置滚动条
						// $treemenu_elt.parents($.KTAnchor.scroll_container).ktScrollSliding();
						// menu_elt 没错
						menu_elt.blur();
						return false;
					});
					// 循环的递归
					$next_elt.setMainMenuEvent();
				}
			});
		}
	});

})(jQuery);


function TagsInput(tags_input) {

	// 用来定义 tags input 的 input 组件
	this.tags_input = $(tags_input);

	// tags 输入组件
	this.tags_frame = null;

	// tags 输入组件，录入的 input
	this.tags_enter = null;

	// 自动补全窗口
	this.tags_complete = null;

	// 完整的 tags
	this.full_tags = this.fullTags();

	// 当前录入的 tags
	this.entered_tags = (this.tags_input.val().length<1) ? [] : this.tags_input.val().split(",");

	// 允许录入多个 tags
	this.multiple = typeof(this.tags_input.attr('multiple'))=="undefined" ? false : true;

	// 自动补全窗口弹出的方向
	this.complete_drop = typeof(this.tags_input.attr('drop-up'))=="undefined" ? 'dropdown' : 'dropup';

	// 当前自动补全选中的项
	this.selected_complete = null;

	// 初始化
	this.init();
}

/* 
 * init
 */
TagsInput.prototype.init = function() {

	// 清除上一层节点内的属性，让他们都隐藏
	this.tags_input.parent().children().each(function(key, elt){
		$(elt).css("display", "none");
	});

	// 输出 tags input 的 view 部分
	this.tagsInputGraph();

	// 插入已经定义的 tag
	this.insertTag(this.entered_tags);

	// 绑定事件
	this.tagsInputEvent();
}

// 给 input 部分添加键盘事件
TagsInput.prototype.tagsInputEvent = function()
{
	// 呃。没办法
	var instance = this;
	// onkeyup
	this.tags_enter.bind("keyup", function(e) {
		// 回车
		if(e.keyCode === 13) {
			if (instance.full_tags) {
				if (instance.selected_complete!=null) {
					var value = instance.selected_complete.children("a").attr("value");
					instance.insertTag(value);
					instance.tags_enter.val("");
					instance.selected_complete = null;
				}
			}
			else {
				var value = instance.tags_enter.val();
				if (value.length>=1 && !instance.tags_frame.children("span[value='" + value+ "']").exist()) {
					instance.insertTag(value);
					instance.tags_enter.val("");
				}
			}
		}
		// 上 or 下
		else if (e.keyCode === 38 || e.keyCode === 40 || e.keyCode === 9) {
			switch_selected_complete = null;
			// 
			if (instance.full_tags) {
				if (e.keyCode === 38) {
					switch_selected_complete = instance.selected_complete==null
											? instance.tags_complete.find("li:visible").last()
											: instance.selected_complete.prev("li:visible");
				}
				else if (e.keyCode === 40 || e.keyCode === 9) {
					switch_selected_complete = instance.selected_complete==null
											? instance.tags_complete.find("li:visible").first()
											: instance.selected_complete.next("li:visible");
				}
				if (!switch_selected_complete.exist()) {
					return false;
				}
				if (instance.selected_complete!=null) {
					instance.selected_complete.css("background-color", "#ffffff");
				}
				switch_selected_complete.css("background-color", "#eeeeee");
				instance.selected_complete = switch_selected_complete;
				return false;
			}
		}
		else {
			// 选择自动补全
			instance.selectEnterComplete();
			// 如果自动补全的内容都已经输入，就不显示了
			instance.showEnterComplete();
		}
	});
	// onkeydown
	this.tags_enter.bind("keydown", function(e) {
		
		// 删除
		if (e.keyCode === 8) {
			var value = instance.tags_enter.val();
			if (value.length<1) {
				var last_tag = instance.tags_frame.children("span").last();
				var last_value = last_tag.attr("value");
				instance.removeTag(last_value);
			}
		}
		else if (e.keyCode === 9) {
			instance.tags_enter.trigger("keyup");
			return false;
		}
	});
}

// 插入 tag
TagsInput.prototype.insertTag = function(values)
{
	if (typeof(values)=="string") {
		values = (values.length<1) ? [] : values.split(",");
	}
	if (typeof(values)=="object") {
		// 呃。没办法
		var instance = this;
		// loop
		$.each(values, function(key, value) {
			var name = (!instance.full_tags || typeof(instance.full_tags[value])=="undefined") ? value : instance.full_tags[value];
			var tag_elt = $("<span class=\"radius-5\" value=\"" + value + "\">" + name + "<p>×</p></span>");
			tag_elt.children("p").click(function(e){
				instance.removeTag(value);
			});
			if (instance.multiple!=true) {
				instance.tags_frame.children("span").remove();
			}
			tag_elt.insertBefore(instance.tags_enter);
		});
	}
	/* input value */
	this.flushTagsInputValue();
	this.flushEnterComplete();
}

// 移除 tag
TagsInput.prototype.removeTag = function(value)
{
	this.tags_frame.children("span[value='"+value+"']").empty().remove();
	/* input value */
	this.flushTagsInputValue();
	this.flushEnterComplete();
}

// 给 input 值
TagsInput.prototype.flushTagsInputValue = function()
{
	/* input value */
	var value = "";
	this.tags_frame.children("span").each(function(key, elt){
		value += (value=="") ? $(elt).attr("value") : "," + $(elt).attr("value");
	});
	this.tags_input.val(value);
}

// 选择自动补全
TagsInput.prototype.selectEnterComplete = function()
{
	if (!this.full_tags || !this.tags_complete.exist()) {
		return;
	}
	var enter_value = this.tags_enter.val()
	var enter_reg = new RegExp(enter_value, "g");
	var match_li = null;
	this.selected_complete = null;
	this.tags_complete.find("li").each(function(key, elt) {
		var name = $(elt).children("a").html();
		if (enter_value.length<1) {
			$(elt).css("background-color", "#ffffff");
			$(elt).css("display", "block");
		}
		else if (name.match(enter_reg)) {
			if (match_li==null) {
				match_li = $(elt).css("background-color", "#eeeeee");
			}
			else {
				$(elt).css("background-color", "#ffffff");
			}
			$(elt).css("display", "block");
		}
		else {
			$(elt).css("display", "none");
		}
	});
	if (match_li!=null) {
		this.selected_complete = match_li;
	}
}

// 列出自动补全
TagsInput.prototype.flushEnterComplete = function()
{
	if (!this.full_tags || !this.tags_complete.exist()) {
		return;
	}
	// 拿到需要排除的 tag，不会显示在自动补全
	var entered_tags = ',';
	this.tags_frame.children("span").each(function(key, elt){
		entered_tags += $(elt).attr("value") + ",";
	});
	if (this.full_tags) {
		// 先清除
		this.tags_complete.empty();
		// 呃。没办法
		var instance = this;
		// 开始搜索数据源
		$.each(this.full_tags, function(value, name) {
			var reg = new RegExp("," + value + ",", "g");
			if (!reg.test(entered_tags)) {
				var complete_li = $("<li><a href=\"javascript:void(0);\" value=\"" + value+ "\">" + name + "</a></li>");
				complete_li.click(function(e) {
					instance.insertTag(value);
					// instance.hiddEnterComplete();
				});
				complete_li.appendTo(instance.tags_complete);
			}
		});
	}
}

// 如果可以，显示自动补全
TagsInput.prototype.showEnterComplete = function()
{
	if (!this.full_tags || !this.tags_complete.exist()) {
		return;
	}
	// 如果自动补全已经录入完毕，就不再显示
	var display = this.tags_complete.children("li").exist() ? "block" : "none";
	this.tags_complete.css("display", display);
}

// 隐藏自动补全
TagsInput.prototype.hiddEnterComplete = function()
{
	if (!this.full_tags || !this.tags_complete.exist()) {
		return;
	}
	// 关闭自动补全
	this.tags_complete.css("display", "none");
}

// 输出 tags input 输入模块
TagsInput.prototype.tagsInputGraph = function()
{
	// 默认不带弹出检索层的 tag 输入系统
	this.tags_frame = $('<div class="tags-frame form-control"><input type="text" class="tags-enter" /></div>');
	
	// 带自动完成的输入，根据 options，并判断上弹出还是下弹出
	if (this.full_tags) {
		this.tags_frame = $('<div class="tags-frame form-control ' + this.complete_drop + '"><input type="text" class="tags-enter" /><ul class="dropdown-menu"></ul></div>');
	}
	
	// 插入到 input 的后面
	this.tags_frame.appendTo(this.tags_input.parent());
	// tags 输入组件，录入的 input
	this.tags_enter = this.tags_frame.children("input");
	// 自动补全窗口
	this.tags_complete = this.tags_frame.children("ul");
	// 自动补全窗口设定高度，和滚屏
	this.tags_complete.css({"max-height":200, "overflow-y":"auto"});

	// 呃。没办法
	var instance = this;
	// 刷新自动补全的内容
	this.flushEnterComplete();

	// 如果通过键盘操作让 input 获得焦点
	// 就等于 tags_frame. 被 click 一下
	this.tags_enter.bind("focus", function(e){
		if (!instance.tags_frame.hasClass("tags-frame-focus")) {
			instance.tags_frame.trigger("click");
		}
	});

	// 输入框 focus 时，外框亮色提示
	this.tags_frame.click(function(e){
		// 如果是从别的 tags input 点击过来的，触发下 document.click 事件
		if (!instance.tags_frame.hasClass("tags-frame-focus")) {
			$(document.body).trigger("click");
		}
		if (!instance.tags_enter.is(":focus")) {
			// 给焦点
			instance.tags_enter.focus();
		}
		// 弹出自动补全框
		instance.showEnterComplete();
		// add clsss
		instance.tags_frame.addClass("tags-frame-focus");
		// 回车键不会让 form 提交
		instance.tags_enter.parents("form").bind("keydown", function(e){
			if(e.keyCode === 13) {
				return false;
			}
		});
		// 关闭弹出
		$(document.body).bind("click touchend", function(e) {
			// 关闭自动弹出
			instance.hiddEnterComplete();
			// 去掉 focus 的 css 效果
			instance.tags_frame.removeClass("tags-frame-focus");
			// unbind keydown event
			instance.tags_enter.parents("form").unbind("keydown");
			// unbind document click/touchend event
			$(document.body).unbind("click touchend");
		});
		// 不冒泡
		e.stopPropagation();
	});
}

// 返回完整的 tags 数据
TagsInput.prototype.fullTags = function()
{
	// 获取属性
	var data_attr = this.tags_input.attr('tags-data');
	// 如果没有 tags-data ，说明没有检索部分
	if (typeof(data_attr)=="undefined" || data_attr.length<1) {
		return null;
	}
	// 切分字符串
	var tags_data = data_attr.split(",");
	// 切割字符串，循环处理
	full_tags = {};
	for(var ii=0; ii<tags_data.length; ii++) {
		// 空的 tag 内容要跳过去
		if (tags_data[ii].length==0) {
			continue;
		}
		var one_tags = tags_data[ii].split(":");
		full_tags[one_tags[0]] = (typeof(one_tags[1])=="undefined") ? one_tags[0] : one_tags[1];
	}
	// 返回
	return full_tags;
}


/*
 * 游戏大厅多标签操作界面
 */
function Hall(tabs_class, boards_class) {
	// hall tabs layout
	this.hall_tab_pool = $(tabs_class);
	// hall boards layout
	this.hall_board_pool = $(boards_class);

	// 当前操作的 tab
	this.selected_tab = null;
	// 当前操作的 board
	this.selected_board = null;

	// 全部的打开的 hall tabs
	this.opened_tabs = [];
	// 全部打开的 hall boards
	this.opened_boards = [];

	// 最后一个打开的 hall
	this.add_button = null;
	// 新的 hall_tab html
	// this.new_hall_tab_html = "<li role=\"presentation\" class=\"active\"><a href=\"#\">" + hall_name + "</a></li>";
}

/*
 * 新标签
 */
Hall.prototype.newTab = function(id, host, port, name) {
	var aa = $("<a href=\"javascript:void(0)\">" + name  + "</a>");
	var li = $("<li class=\"opacity-95\" id=\"hall_tab_" + id + "\" role=\"presentation\"></li>").append(aa);
	aa.data("host", host);
	aa.data("port", port);
	aa.click(function(ev){
		var host = $(this).data("host");
		var port = $(this).data("port");
		hall.selectedHall(id, host, port);
	});
	return li;
}

/*
 * 新大厅的 console 面板
 */
Hall.prototype.newBoard = function(id, host, port, name) {
	var bb = $("<div id=\"hall_board_"+id+"\" class=\"panel panel-default opacity-95\" style=\"left:0%;\"><div class=\"panel-body\"><div><div>" + name + " " + host + ":" + port + "</div></div></div></div>");
	return bb;
}

/*
 * 刷新选项
 */
Hall.prototype.resetOption = function() {
	// 全部的打开的 hall tabs
	this.opened_tabs = this.hall_tab_pool.children("li[role='presentation']");
	// 全部的打开的 hall boards
	this.opened_boards = this.hall_board_pool.children("div");
	// 最后一个 hall
	this.add_button = this.hall_tab_pool.children().last();
	// 判断初始化的节点
	return true;
}

/*
 * 创建新大厅面板
 */
Hall.prototype.createHall = function(id, host, port, name) {
	// reset options
	if (!this.resetOption()) {
		return false;
	}
	if (!$("#hall_tab_" + id).exist()) {
		// inset new tab
		this.add_button.before(this.newTab(id, host, port, name));
	}
	if (!$("#hall_board_" + id).exist()) {
		// inset new board
		this.hall_board_pool.append(this.newBoard(id, host, port, name));
	}
	// close popup
	$(".popup-modal").modal("hide");
	// 切换新的 hall 为当前操作 hall
	this.selectedHall(id, host, port);
}

/*
 * 选择大厅
 */
Hall.prototype.selectedHall = function(id, host, port) {
	// reset options
	if (!this.resetOption()) {
		return false;
	}
	// active
	this.opened_tabs.removeClass("active");
	$("#hall_tab_" + id).addClass("active");
	// ..
	this.opened_boards.css("display", "none");
	$("#hall_board_" + id).css("display", "block");
	//
	$("#hall_id").val(id);
	$("#hall_host").val(host);
	$("#hall_port").val(port);
}

/*
 * 显示结果
 */
Hall.prototype.showResult = function(id, message) {
	var board = $("#hall_board_" + id);
	var board_body = board.children(".panel-body");
	board_body.append("<div>" + message + "</div>");
	board.scrollTop( board[0].scrollHeight );
}

/*
 * 初始化为全局对象，给每页调用时，不再初始化，只 reset options
 */
window.hall = null;


/* set KTAnchor default value */
$.KTAnchor.init({
	response_container: ".body-content-right", // Ajax, 设定默认 response 填充的区域
	paging_container: ".paging-container", // 分页，分页的容器
	paging_limit: 30, // 分页，默认每页 30 条记录
	paging_symbol: "&po", // 分页，默认通过传统的 & 来分割，值通过 http.request.GET.cc 来传递
	dropdown_container: ".dropdown-container", // 弹出菜单，通过识别此节点，来绑定 下拉菜单的 事件
	treemenu_container: ".treemenu-container", // 树状菜单，通过识别此节点，来绑定 树状菜单 点击事件
	scroll_container: ".scroll-container" // 自定义相应鼠标滚动，通过识别此节点，来绑定
});

// 返回按钮监听
$(window).bind("popstate", function(){
	$(".body-content-right").load(window.top.location.href, function(){
		$.KTAnchor.treemenuSelected(window.top.location.href);
		$(".body-content-right").KTLoader();
	});
});

$(document).ready(function(){
  $(document.body).KTLoader();
  $(window).trigger("popstate");

  // 调整窗口时时，
  $(window).bind("resize", function(){
    $(".body-content").height($(window).height()-40);
    $(".popup-modal .modal-body").height($(window).height()*0.85-88);
  });

  // 手动触发一次
  $(window).trigger("resize");
});
