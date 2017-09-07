(function($){

    let $ = jQuery;

    /**
     * 非绑定给节点的方法，写在 $.extend 内
     */
    $.extend({

        /**
         * a AJax request
         */
        KTAjax: function(url, method, data, headers, success, error, complete) {
            // stop before one ajax request
            if (typeof(window.currentKTAjax)==="object") {
                try{window.currentKTAjax.abort()}catch(e){}
            }
            // set headers
            if ($.type(headers)!=="object" || $.isEmptyObject(headers)) {
                headers = {};
            }
            headers['access-token'] = "";
            // set content type
            let contentType = (method.toLowerCase()==="post" && !(data instanceof FormData)) ? "application/x-www-form-urlencoded; charset=UTF-8" : false;
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
                    if ($.isFunction(complete)) complete(XMLHttpRequest);
                }
            });
        },

        /*
         *
         */
        KTAnchor: {
            // Ajax 正常返回
            success : function(container, responseText) {
                if (typeof(responseText)==="object" && responseText.action!==null) {

                }
                // 填充完后重新设定填充区域内的 KTLoader
                $(container).KTLoader();
            },
            // Ajax 错误
            error: function(container, XMLHttpRequest) {

            },
            // Ajax 处理完成
            complete: function(container, XMLHttpRequest) {

            }
        }
    });

    /**
     * 绑定给节点的方法，写在 $.extend 内
     */
    $.fn.extend({

        KTLoader: function() {
            this.KTAnchor();
        },

        KTAnchor : function(success, error, begin, complete) {
            // 取得 某文档下 所有没有被标注为原生的 anchor
            this.find("a").each(function(key, anchor){
                // jQuery 对象
                let $anchor = $(anchor);
                // 给 <a native 标记不绑 ajax 请求
                if (typeof($anchor.attr('native'))!=="undefined") {
                    // 这个点归点，别冒泡，可能的多次遍历到时，只处理一次
                    if ($anchor.data("native-click")===null) {
                        // 不冒泡
                        $anchor.bind("click", function(e){
                            e.stopPropagation();
                        });
                        // 标记
                        $anchor.data("native-click", "native-click");
                    }
                    return;
                }
                // 如果是 <a href="javascript:..." 也是不能去绑定
                if (/^javascript:/.test($anchor.attr("href"))) return;
                // 如果是 # 也是不能绑定
                if ($anchor.attr("href")==="#") return;
                // 如果已经绑定过
                if ($anchor.data("ajax-click")!==null) return;

                // 绑定点击事件
                $anchor.on("click", function() {
                    // 如果有 confirm 属性
                    if (typeof($anchor.attr("confirm"))!=="undefined" && $anchor.attr("confirm").length>1 && $anchor.data("confirm-click")===null) {
                        $.confirm($anchor.attr("confirm"), function(){
                            $anchor.data("confirm-click", "confirm-click");
                            $anchor.trigger("click");
                        });
                    }
                    // 聚焦会使得点击处框上虚线
                    $anchor.context.blur();
                    // 获取要请求的地址
                    let requestUrl = $anchor.attr("href");
                    // 获取当前的地址
                    // let currentRef = window.location.href;
                    // 如果设置了 <a unpushstate ..> 那么不做 url pushState
                    if (typeof($anchor.attr("unpushstate"))==="undefined") {
                        window.history.pushState(null, "", requestUrl);
                    }
                    // 返回的内容填充到哪
                    let container = $.KTAnchor.response_container;
                    if (typeof($anchor.attr("container"))!=="undefined" && $anchor.attr("container").length>1) {
                        container = $anchor.attr("container");
                    }
                    // set header
                    let header = null;
                    if ($.type($anchor.attr("header"))==="string") {
                        header = $.parseJSON($anchor.attr("header"));
                    }
                    // ajax 请求，并回调
                    $.KTAjax(requestUrl, "GET", null, header,
                        // 成功
                        function(responseText){
                            $.isFunction(success) ? success(container, responseText) : $.KTAjax.success(container, responseText);
                        },
                        // 错误
                        function(XMLHttpRequest){
                            $.isFunction(error) ? error(container, XMLHttpRequest) : $.KTAjax.error(container, XMLHttpRequest);
                        },
                        // 结束 ( 成功或失败后 )
                        function(XMLHttpRequest){
                            $.isFunction(complete) ? complete(container, XMLHttpRequest) : $.KTAjax.complete(container, XMLHttpRequest);
                        }
                    );
                    // data 的 confirm-click 值重置
                    $anchor.data("confirm-click", null);
                    // 防止链接点击生效
                    return false;
                });
            });
            // 返回 JQuery 对象
            return this;
        }
    });

})(jQuery);

$(document).ready(function(){
    $(document.body).KTLoader();
});