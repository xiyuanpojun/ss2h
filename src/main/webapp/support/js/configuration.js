$(function () {
    layui.use('element', function () {
        var element = layui.element;
    });

    function getConfigs(data) {
        $.ajax({
            url: ctx + "/config/config_findTabCol",
            type: "POST",
            data: {
                'typeId': data
            },
            dataType: "json",
            success: function (data) {
                checkLogon(data);
                var error = parseInt(data.error);
                if (error === 0) {
                    $("#checkOption").html("");
                    //插入的数据
                    for (var i = 0; i < data.cols.length; i++) {
                        var str = "<input name='" + data.cols[i].col + "' title='" + data.cols[i].colName + "' type='checkbox'>";
                        $("#checkOption").append(str);
                    }
                }
                //重新渲染
                layui.use('form', function () {
                    var form = layui.form;
                    form.render();
                });
            },
            error: function () {
                layer.msg("连接服务器失败");
            }
        });
    }

    function add(data) {
        $.ajax({
            url: ctx + "/config/config_add",
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
                    init($("#config").val());
                    $("#btn").next().click();
                    $("#checkOption").html("");
                    //重新渲染
                    layui.use('form', function () {
                        var form = layui.form;
                        form.render();
                    });
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
            url: ctx + "/config/config_update",
            type: "POST",
            data: data,
            dataType: "json",
            success: function (data) {
                checkLogon(data);
                var error = parseInt(data.error);
                if (error === 0) {
                    layer.close(updateLayer);
                    init($("#config").val());
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
            var str = JSON.stringify(data.field);
            str = str.replace("{", "");
            str = str.replace("}", "");
            var strs = str.split(",");
            var fruits = "";
            for (var i = 0; i < strs.length; i++) {
                var ss = strs[i].split(":", 2);
                var s = ss[0];
                if (i !== strs.length - 1) {
                    fruits += s + ",";
                } else {
                    fruits += s;
                }
            }
            fruits = fruits.replace(/"/g, "");
            fruits = fruits.replace("typeId,showNum,", "");
            fruits = fruits.replace("typeId,showNum", "");
            if ("" === fruits) {
                layer.alert("请选择显示字段");
                return false;
            }
            var msg = {
                'configItemView.surveyType': $("#atypeId").find("option:selected").text(),
                'configItemView.showNum': $("#ashowNum").val(),
                'fruits': fruits
            };
            add(msg);
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
        //监听专项搜索框
        form.on('select(searchChange)', function (data) {
            var config = data.value;
            init(config);
            return false;
        });
    });
    layui.use('form', function () {
        var form = layui.form;
        //监听专项选择框
        form.on('select(selectConfig)', function (data) {
            var config = data.value;
            if ("" !== config) {
                getConfigs(config);
            }
            return false;
        });
    });


    function init(typeId) {
        layui.use('table', function () {
            var table = layui.table;
            var tableOptions = {
                url: '${ctx}/config/config_findAll',
                method: 'POST',
                id: 'listReload',
                where: {
                    'typeId': typeId
                },
                page: true,
                request: {pageName: 'current', limitName: 'currentTotal'},
                response: {
                    statusName: 'error',
                    statusCode: 0,
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'config'
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
                            url: ctx + "/config/config_delete",
                            type: "POST",
                            data: {
                                'configItemView.surveyType': data.surveyType,
                                'configItemView.showNum': data.showNum,
                                'configItemView.col': data.col,
                                'configItemView.colName': data.colName
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
                    var surveyType = data.surveyType;
                    var showNum = data.showNum;
                    $("#surveyType").val(surveyType);
                    $("#showNum").val(showNum);
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
                    layer.title('更新信息', updateLayer);
                }
            });
        });
    }

    //打开页面时加载一次数据
    init($("#config").val());
});