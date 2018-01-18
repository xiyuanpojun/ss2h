$(function () {
    layui.use('element', function () {
        var element = layui.element;
    });

    function add(data) {
        $.ajax({
            url: ctx + "/user/user_add",
            type: "POST",
            data: data,
            dataType: "json",
            beforeSend: function (xhr) {
                $("#btn").attr('disabled', "true");
            },
            success: function (data) {
                checkLogon(data);
                var error = parseInt(data.error);
                if (error === 0) {
                    init($("#province").val());
                    $("#btn").next().click();
                }
                layer.msg(data.message);
            },
            error: function () {
                layer.msg("连接服务器失败");
            },
            complete: function () {
                $("#btn").removeAttr("disabled");
            }
        });
    }

    var updateLayer;//弹出层

    function update(data) {
        $.ajax({
            url: ctx + "/user/user_update",
            type: "POST",
            data: data,
            dataType: "json",
            success: function (data) {
                checkLogon(data);
                var error = parseInt(data.error);
                if (error === 0) {
                    layer.close(updateLayer);
                    init($("#province").val());
                }
                layer.msg(data.message);
            },
            error: function () {
                layer.msg("连接服务器失败");
            }
        })
    }

    $("#uCan").click(function () {
        layer.closeAll();
    });
    layui.use('form', function () {
        var form = layui.form;
        //增加
        form.on('submit(btn)', function (data) {
            add(data.field);
            return false;
        });
    });
    layui.use('form', function () {
        var form = layui.form;
        //修改
        form.on('submit(uBtn)', function (data) {
            update(data.field);
            return false;
        });
    });
    layui.use('form', function () {
        var form = layui.form;
        //监听省份选择框
        form.on('select(searchChange)', function (data) {
            var province = data.value;
            init(province);
            return false;
        });
    });
    layui.use('form', function () {
        var form = layui.form;
        //监听角色单选框
        form.on('radio(said)', function (data) {
            if (data.value === "2") {
                $("#said").attr("lay-verify", "required");
            } else {
                $("#said").removeAttr("lay-verify");
            }
            //重新渲染
            layui.use('form', function () {
                var form = layui.form;
                form.render();
            });
        });
    });

    function init(province) {
        layui.use('table', function () {
            var table = layui.table;
            var tableOptions = {
                url: '${ctx}/user/user_findAll',
                method: 'POST',
                id: 'listReload',
                where: {
                    'province': province
                },
                page: true,
                request: {pageName: 'current', limitName: 'currentTotal'},
                response: {
                    statusName: 'error',
                    statusCode: 0,
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'users'
                }
            };
            table.init('table', tableOptions);
            //监听工具条
            table.on('tool(table)', function (obj) {
                var data = obj.data;
                if (obj.event === 'detail') {
                    layer.msg('ID：' + data.id + ' 的查看操作');
                } else if (obj.event === 'del') {
                    layer.confirm('真的删除吗', function (index) {
                        $.ajax({
                            url: ctx + "/user/user_delete",
                            type: "POST",
                            data: {
                                'userEntity.userid': data.userid
                            },
                            dataType: "json",
                            success: function (data) {
                                checkLogon(data);
                                var error = parseInt(data.error);
                                if (error === 0) {
                                    obj.del();
                                }
                                layer.msg(data.message);
                            },
                            error: function () {
                                layer.msg("连接服务器失败");
                            },
                            complete: function () {
                                layer.close(index);
                            }
                        });
                    });
                } else if (obj.event === 'edit') {
                    //获取信息
                    $.ajax({
                        url: ctx + "/user/user_findOne",
                        type: "POST",
                        data: {
                            'userEntity.userid': data.userid
                        },
                        dataType: "json",
                        success: function (data) {
                            checkLogon(data);
                            if (data.error === 0) {
                                //设置值
                                var uname = data.user.uname;
                                var userid = data.user.userid;
                                var upwd = data.user.upwd;
                                var orgid = data.user.orgid;
                                var urole = data.user.urole;
                                $("#orgid").find("option").each(function () {
                                    if ($(this).html() === orgid) {
                                        $(this).prop("selected", true);
                                    } else {
                                        $(this).prop("selected", false);
                                    }
                                });
                                $("#userid").val(userid);
                                $("#uname").val(uname);
                                $("#upwd").val(upwd);
                                if (urole === "2") {
                                    $("#orgid").attr("lay-verify", "required");
                                } else {
                                    $("#orgid").removeAttr("lay-verify");
                                }
                                //重新渲染
                                layui.use('form', function () {
                                    var form = layui.form;
                                    form.render();
                                });
                                updateLayer = layer.open({
                                    type: 1,
                                    content: $('#update'),
                                    offset: 'auto',
                                    maxWidth: 500
                                });
                                layer.title('更新信息-用户id:' + userid, updateLayer);
                            } else {
                                layer.msg(data.message);
                            }
                        },
                        error: function () {
                            layer.msg("连接服务器失败");
                        }
                    });
                }
            });
        });
    }

    //打开页面时加载一次数据
    init($("#province").val());
});