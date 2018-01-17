var layer;
// var sessionTime = 1000 * 60;//session时间
// var sessiontimeTask = 0;//记录时间
// var sessionTimer;//定时器
// function start() {
//     end();
//     sessiontimeTask = 0;//确保每次开始执行定时器时间记录都是从0开始
//     sessionTimer = window.setInterval(timerDo, 1000);
// }
//
// function end() {
//     window.clearInterval(sessionTimer);
// }
//
// function timerDo() {
//     //服务器到页面映射会有延迟。。。。。。。
//     if (sessiontimeTask >= sessionTime) {
//         end();
//         layer.open({
//             content: "你太久未操作，请重新登陆后继续。"
//             , btn: ['确定']
//             , yes: function (index, layero) {
//                 layer.close(index);
//             }, cancel: function () {
//             }, end: function () {
//                 // logon();
//             }
//         });
//     }
//     sessiontimeTask += 1000;//时间记录+1s
//     console.debug(sessiontimeTask / 1000);
// }
//
// start();

function logon() {
    $.ajax({
        url: ctx + "/user/user_logon",
        type: "POST",
        data: {
            'userEntity.userid': $("#logon").attr("alt")
        },
        dataType: "json",
        success: function (data) {
            var error = parseInt(data.error);
            if (error === 0) {
                $("#logon").attr("alt", "");
                layer.open({
                    time: 3000,
                    content: data.message + "，即将关闭该页面。"
                    , btn: ['确定']
                    , yes: function (index, layero) {
                        layer.close(index);
                        window.location.href = ctx + "/user/userManage";
                    }
                    , cancel: function () {
                        window.location.href = ctx + "/user/userManage";
                    }, end: function () {
                        window.location.href = ctx + "/user/userManage";
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
            if (i === 0) {
                $(this).click();
                $("#show_page").attr("src", $(this).prop("href"));
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