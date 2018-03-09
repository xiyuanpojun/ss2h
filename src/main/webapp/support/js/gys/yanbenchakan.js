var sury_type = "";
var nowTab = null;
var nowOptions = null;
layui.use(['table', 'form', 'laydate'], function () {
    var table = layui.table;
    var form = layui.form;
    var laydate = layui.laydate;
    $ = layui.jquery;//使用jQuery对象
    layer.load();
    //加载省份
    orginit();
    //监听省份下拉框改变事件
    layui.use('form', function () {
        var form = layui.form;
        //监听上级机构选择框
        form.on('select(searchChange)', function (data) {
            var porgid = data.value;
            //初始化地市
            init(porgid);
            return false;
        });
    });
    $.getJSON(ctx + "/survey/survey_getCity", function (data) {
        $.each(data.dataList, function (i, item) {
            $('#city').append('<option  value=' + item.ORGID + '>' + item.ORGNAME + "</option>");
        });
        form.render('select');
    });
    $.getJSON(ctx + "/survey/survey_getCustType", function (data) {
        $.each(data.dataList, function (i, item) {
            $('#custType').append('<option  value=' + item.PID + '>' + item.PNAME + "</option>");
        });
        form.render('select');
    });

    $.getJSON(ctx + "/survey/survey_getStype?rowv=1", function (data) {
        $.each(data.dataList, function (i, item) {
            $('#stype').append('<option value=' + item.tab + '>' + item.survey_type + "</option>");
        });
        form.render('select');
        layer.closeAll('loading');
    });

    $('.box').on('click', '.layui-table-body tr', function (e) {
        var evtTarget = e.target || e.srcElement;
        if (!$(evtTarget).is('i')) {
            $(this).find(".layui-form-checkbox i").trigger("click");
        }
    });
    //每次点搜索，得看表格是否还有未提交数据
    form.on('submit(searchForm)', function (data) {
        $("#searchForm").attr('disabled', "true");
        checkLogin();
        var pgs = table.cache.surveyList;

        var stype = data.field.stype;
        var city = data.field.city;
        var custType = data.field.custType;
        var address = data.field.address;
        var dist = data.field.dist;
        var orgid = data.field.orgid;
        if (stype != sury_type) {//更换了专项
            sury_type = stype;
            $.getJSON(ctx + "/survey/survey_getCol", {tab: sury_type}, function (data) {
                var col = "[{type:'checkbox'}";
                //var col="[{field:'ROWVAL', title:'操作',templet: '#checkboxTpl'}";
                if (data.dataList.length == 0) {
                    layer.msg('该专项还未配置可显示数据列');
                    return;
                }
                $.each(data.dataList, function (i, item) {
                    col += ",{field:'" + item.COL + "',title:'" + item.COL_NAME + "'}";
                });
                col += "]";
                nowOptions = {
                    elem: '#tab1'
                    , id: 'surveyList'
                    , height: 'full-410'
                    , size: 'sm'
                    , url: ctx + "/user/ybck"
                    , page: false
                    , done: function (res, curr, count) {
                        var allData = table.cache.surveyList;
                        $('#tab-total').text(allData.length);
                        var dzArray = new Array()
                        $.each(allData, function (p1, p2) {
                            var dzObj = new Object();
                            dzObj.lat = p2.LAT;
                            dzObj.lng = p2.LNG;
                            if (typeof(p2.YJRDZ) != "undefined") {
                                dzObj.yddz = p2.YJRDZ;
                            } else if (typeof(p2.YDDZ) != "undefined") {
                                dzObj.yddz = p2.YDDZ;
                            }
                            dzArray[p1] = dzObj;
                        });
                        bdGEO(dzArray);
                        $("#searchForm").removeAttr("disabled");
                    }
                    , method: 'post'
                    , where: {
                        tab: sury_type,
                        orgid: orgid,
                        city: city,
                        custType: custType,
                        address: address,
                        dist: dist
                    }
                    , cols: [eval('(' + col + ')')]
                };
                nowTab = table.render(nowOptions);
            });
        } else {
            table.reload('surveyList', {
                url: ctx + "/user/ybck"
                , method: 'post'
                , where: {
                    tab: sury_type,
                    orgid: orgid,
                    city: city,
                    custType: custType,
                    address: address,
                    dist: dist
                }
                , done: function (res, curr, count) {
                    var allData = table.cache.surveyList;
                    $('#tab-total').text(allData.length);
                    var dzArray = new Array()
                    $.each(allData, function (p1, p2) {
                        var dzObj = new Object();
                        dzObj.lat = p2.LAT;
                        dzObj.lng = p2.LNG;
                        if (typeof(p2.YJRDZ) != "undefined") {
                            dzObj.yddz = p2.YJRDZ;
                        } else if (typeof(p2.YDDZ) != "undefined") {
                            dzObj.yddz = p2.YDDZ;
                        }
                        dzArray[p1] = dzObj;
                    });
                    bdGEO(dzArray);
                    $("#searchForm").removeAttr("disabled");
                }
            });
        }
        return false;
    });

// 百度地图API功能
    var map = new BMap.Map("map");
//以当前城市为中心打开地图
    map.centerAndZoom("北京", 12);
    map.enableScrollWheelZoom();
    var index = 0;
// 创建地址解析器实例
    var myGeo = new BMap.Geocoder();

    function bdGEO(adds) {
        map.clearOverlays();//清除所有标签
        for (var i = 0; i < adds.length; i++) {
            geocodeSearch(adds[i], i + 1);
        }
    }

    function geocodeSearch(addObj, idx) {
        /*myGeo.getPoint(add, function (point) {
            if (point) {
                //document.getElementById("result").innerHTML +=  idx+"、" +add +"</br>";
                var address = new BMap.Point(point.lng, point.lat);
                if (idx == 1) {
                    map.panTo(new BMap.Point(point.lng, point.lat));
                }
                addMarker(address, new BMap.Label(idx), add);
            }
        }, "全国");*/
        var address = new BMap.Point(addObj.lng, addObj.lat);
        if (idx == 1) {
            map.panTo(new BMap.Point(addObj.lng, addObj.lat));
        }
        addMarker(address, new BMap.Label(idx), addObj.yddz);
    }

// 编写自定义函数,创建标注
    function addMarker(point, label, addr) {
        var marker = new BMap.Marker(point);
        map.addOverlay(marker);
        marker.setLabel(label);
        var opts = {
            width: 150,     // 信息窗口宽度
            height: 60,     // 信息窗口高度
            title: "详细信息"
        }
        var infoWindow = new BMap.InfoWindow("地址：" + addr, opts);  // 创建信息窗口对象
        marker.addEventListener("click", function () {
            map.openInfoWindow(infoWindow, point); //开启信息窗口
        });
    }
});

//初始化省份
function orginit() {
    layui.use("form", function () {
        var form = layui.form;
        $.ajax({
            url: ctx + "/org/org_showporglist",
            type: "POST",
            success: function (data) {
                var li = data.porglist;
                // $("#orgid").append($('<option value="">选择省份</option>'));
                for (var i = 0; i < li.length; i++) {
                    var porglist = $("<option value=" + li[i].orgid + ">" + li[i].orgname + "</option>");
                    $(".ogd").append(porglist);
                    form.render();
                }
                init(li[0].orgid);
            },
            error: function () {
                layer.alert("连接服务器失败");
            }
        });
        form.render();
    });
}

//初始化地市
function init(porgid) {
    layui.use("form", function () {
        $(".city option").remove();
        var form = layui.form;
        $.ajax({
            url: ctx + "/org/org_showcitylist",
            data: {
                'porgid': porgid
            },
            type: "POST",
            success: function (data) {
                var li = data.porglist;
                $(".city").append($('<option value="">请选择地市</option>'));
                for (var i = 0; i < li.length; i++) {
                    var citylist = $("<option value=" + li[i].orgid + ">" + li[i].orgname + "</option>");
                    $(".city").append(citylist);

                }
                form.render();
            },
            error: function () {
                layer.alert("连接服务器失败");
            },
        });
        form.render();
    });
}