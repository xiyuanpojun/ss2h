$(function () {
    layui.use('element', function () {
        var element = layui.element;
    });

    function add(data) {
        $.ajax({
            url: ctx + "/config/sconfig_add",
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

    $("#uCan").click(function () {
        layer.closeAll();
    });
    layui.use('form', function () {
        var form = layui.form;
        //增加
        form.on('submit(btn)', function (data) {
            var msg;
            var s1 = data.field.sptype;
            var s2 = data.field.iptype;
            if (s1 === "" && s2 === "") {
                layer.msg("选择或输入一个选项类型");
                return false;
            } else {
                if (s1 !== "" && s2 === "") {
                    msg = {
                        'tpCodeEntity.pname': $("#pname").val(),
                        'tpCodeEntity.ptype': s1
                    };
                }
                if (s1 === "" && s2 !== "") {
                    msg = {
                        'tpCodeEntity.pname': $("#pname").val(),
                        'tpCodeEntity.ptype': s2
                    };
                }
                if (s1 !== "" && s2 !== "") {
                    msg = {
                        'tpCodeEntity.pname': $("#pname").val(),
                        'tpCodeEntity.ptype': s1
                    };
                }
                add(msg);
                return false;
            }
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

    function init(province) {
        layui.use('table', function () {
            var table = layui.table;
            var tableOptions = {
                url: ctx + '/config/sconfig_findAll',
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
                    dataName: 'sconfig'
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
                            url: ctx + "/config/sconfig_delete",
                            type: "POST",
                            data: {
                                'tpCodeEntity.pid': data.pid,
                                'tpCodeEntity.pname': data.pname,
                                'tpCodeEntity.ptype': data.ptype
                            },
                            dataType: "json",
                            success: function (data) {
                                checkLogon(data);
                                var error = parseInt(data.error);
                                if (error === 0) {
                                    obj.del();
                                    init($("#province").val());
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
                }
            });
        });
    }

    //打开页面时加载一次数据
    init($("#province").val());
});