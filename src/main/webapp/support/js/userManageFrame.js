var layer;

function logon() {
    $.ajax({
        url: ctx + "/user/user_logon",
        type: "POST",
        data: {
            'userEntity.userid': $("#logon").attr("alt")
        },
        dataType: "json",
        success: function (data) {
            checkLogon(data);
            var error = parseInt(data.error);
            if (error === 0) {
                $("#logon").attr("alt", "");
                layer.open({
                    time: 3000,
                    content: data.message + "，即将关闭该页面。"
                    , btn: ['确定']
                    , yes: function (index, layero) {
                        layer.close(index);
                        window.location.href = ctx + '/index.jsp';
                    }
                    , cancel: function () {
                        window.location.href = ctx + '/index.jsp';
                    }, end: function () {
                        window.location.href = ctx + '/index.jsp';
                    }
                });
            } else {
                layer.msg(data.message);
            }
        },
        error: function () {
            layer.msg("连接服务器失败");
        }
    });
}

layui.use(['element', 'layer'], function () {
    var element = layui.element;
    layer = layui.layer;
    $(function () {
        $("#urls").find("a").each(function (i) {
            var $obj = $(this);
            $(this).click(function () {
                $.ajax({
                    url: ctx + "/user/user_checkLogin",
                    type: "POST",
                    data: {},
                    dataType: "json",
                    success: function (data) {
                        if (data.isLogin === true) {
                            $("#show_page").prop("src", $obj.attr("alt"));
                        } else {
                            window.location.href = ctx + '/index.jsp';
                        }
                    }
                });
            });
            if (i === 0) {
                $(this).click();
            }
        });
        $("#logon").click(function () {
            logon();
        });
    });
});

window.onbeforeunload = function () {
    if ($("#logon").attr("alt") === "") {
        return;
    } else {
        logon();
        return;
    }
};