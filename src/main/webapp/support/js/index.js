$(function () {
    if ($("#id").val() !== "") {
        window.location.href = ctx + "/user/userManage";
    }
});
layui.use('form', function () {
    var form = layui.form;
    form.on('submit(login)', function (data) {
        login(data.field);
        return false;
    });

    function login(data) {
        $.ajax({
            url: ctx + "/user/user_login",
            type: "POST",
            data: data,
            dataType: "json",
            beforeSend: function () {
                $("#btn").attr('disabled', "true");
            },
            success: function (data) {
                var error = parseInt(data.error);
                if (error === 0) {
                    window.location.href = ctx + data.url;
                } else {
                    layer.msg(data.message);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.status);
                alert(XMLHttpRequest.readyState);
                layer.msg("连接服务器失败");
            },
            complete: function () {
                $("#btn").removeAttr("disabled");
            }
        });
    }
});