$(function () {
    layui.use("element", function () {
        var element = layui.element;
    });
    var flag = 0;
    layui.use("form", function () {
        //对输入的机构id进行监听
        $("#orgid").bind("input propertychange", function () {
            var orgid = $("#orgid").val();
            $("#checkId").html("");
            if (!isNaN(orgid)) {
                flag = 1;
                $.ajax({
                    url: ctx + '/org/org_checkId',
                    type: "POST",
                    data: {
                        'oid': $("#orgid").val()
                    },
                    success: function (data) {
                        if (data.message == "1") {
                            $("#checkId").html("机构id可用");
                            $("#checkId").attr("style", "color:green");
                            flag = 1;
                        }
                        else if (data.message = "0") {
                            if ($("#orgid").val() == null || $("#orgid").val() == "") {
                                $("#checkId").html("");
                                flag = 3;
                            }
                            else {
                                $("#checkId").html("机构id不可用");
                                flag = 2;
                                $("#checkId").attr("style", "color:red");
                            }
                        }

                    }
                });

            }
            else {
                $("#checkId").html("请输入数字");
                $("#checkId").attr("style", "color:red");
                flag = 2;
            }
            return false;
        });
    });
    //点击机构等级切换
    layui.use('form', function () {

        var form = layui.form;
        form.on('radio(radio1)', function (data) {
            if (data.value == "1") {
                $("#porglist").show();
                orginit();
                $("#porgid2").attr("lay-verify", "required");
            } else {
                orginit();
                $("#porglist").hide();
                $("#porgid2").val("");
                $("#spOrg").val("");
                $("#porgid2").removeAttr("lay-verify");
                $("#gporg").val("");
                //$(".pog option").remove();
            }
            reset();
            form.render();
            return false;
        });
    });
//对新增功能表单form监听
    layui.use('form', function () {
        checkLogin();
        var form = layui.form;
        form.on('submit(btn)', function (data) {
            if (flag == 1) {
                $.ajax({
                    url: ctx + "/org/org_add",
                    type: "POST",
                    data: data.field,
                    data_type: "json",
                    beforeSend: function (xhr) {
                        $("#btn").attr('disabled', "true");
                    },
                    success: function (data) {
                        if (data.message == "0") {
                            layer.msg("添加成功 ");
                            orginit();
                            form.render();
                        }
                        else if (data.message == "1") {
                            layer.msg("该机构已存在请 重新添加 ");
                        }
                        else if (data.message == "2") {
                            layer.msg("添加失败");
                        }
                        else {
                            layer.msg("非法操作 ");
                        }
                        $("#checkfId").html("");
                        reset();

                    },
                    error: function () {
                        layer.alert("连接服务器失败");
                    },
                    complete: function () {
                        $("#btn").removeAttr("disabled");
                    }

                });

            }
            else if (flag == 2) {
                layer.msg("功能id不可用请重新填写");
            }
            else {
                layer.msg("存在 空 值请重新 填写 ");
            }

            return false;
        });
    });
    layui.use('form', function () {
        var form = layui.form;
        //监听上级机构选择框
        form.on('select(searchChange)', function (data) {
            var porgid = data.value;
            init(porgid);
            return false;
        });
    });
    layui.use('form', function () {
        var form = layui.form;
        //新增机构栏上级机构选择框
        form.on('select(searchChange1)', function (data) {
            $("#gporg").val($(data.elem).find("option:selected").text());
            return false;
        });
    });
    $("#uCan").click(function () {
        layer.closeAll();
    });

//初始化方法
    function init(porgid) {
        layui.use('table', function () {
            var table = layui.table;
            var tableOptions = {
                url: ctx + '/org/org_findAll',
                method: 'POST',
                id: 'listReload',
                where: {
                    'porgid': porgid
                },
                page: true,
                request: {pageName: 'current', limitName: 'currentTotal'},
                response: {
                    statusName: 'error',
                    statusCode: 0,
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'orglist'
                }
            };
            table.init('table', tableOptions);

            //监听工具条
            table.on('tool(table)', function (obj) {
                var data = obj.data;//obj代表所选中的行
                if (obj.event === 'detail') {
                    layer.msg('ID：' + data.fId + ' 的查看操作');
                } else if (obj.event === 'del') {
                    $.ajax({
                        url: ctx + "/org/org_delete",
                        type: "POST",
                        data: {
                            'orgentity.orgid': data.orgid,
                            'orgentity.orgname': data.orgname
                        },
                        dataType: "json",
                        success: function (data) {
                            layer.msg(data.message);
                            var error = parseInt(data.error);
                            if (error === 0) {
                                obj.del();
                                layer.msg(data.message);
                                init($("#porgid").val());
                            }

                        },
                        error: function () {
                            layer.msg("连接服务器失败");
                        },
                        complete: function () {
                            layer.close(index);
                        }
                    });

                }
            });
        });
    }

    //上级机构数据初始化
    function orginit() {
        layui.use("form", function () {
            var form = layui.form;

            $.ajax({
                url: ctx + "/org/org_showporglist",
                type: "POST",
                success: function (data) {
                    $(".pog option").remove();
                    var li = data.porglist;
                    $("#porgid").append($('<option value="all">所有机构</option>'));
                    $("#porgid2").append($('<option value="">选择上级机构</option>'));
                    for (var i = 0; i < li.length; i++) {
                        var porglist = $("<option value=" + li[i].orgid + ">" + li[i].orgname + "</option>");
                        $(".pog").append(porglist);

                    }

                },
                error: function () {
                    layer.alert("连接服务器失败");
                },
            });
            form.render();
        });

    }

    //检查orgid是否为空
    function checkorgid() {
        if ($("#orgid").val() == null || $("#orgid").val() == "") {
            $("#checkId").html("");
        }
    }

    //集体重置
    function reset() {
        $("#checkId").html("");
        $("#orgid").val("");
        $("#orgname").val("");
    }

    //点击机构列表
    $("#orglistmenu").click(function () {
        orginit();
        init($("#porgid").val());
    });
    //点击添加机构重置 checkId
    $("#addorg").click(function () {
        $('#orgform')[0].reset();
        $("#checkfId").html("");
        $("#porglist").show();
        form.render();
    });
    //点击重置
    $("#reset1").click(function () {
        layui.use("form", function () {
            var form = layui.form;
            $("#porglist").show();
            form.render();
        });
    });
    //打开页面时加载一次数据
    init($("#porgid").val());

});