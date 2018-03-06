var sury_type="";
var nowTab=null;
var nowOptions=null;
layui.use(['table','form','laydate'], function(){
    var table=layui.table;
    var form = layui.form;
    var laydate = layui.laydate;
    $=layui.jquery;//使用jQuery对象

    layer.load();
    //地市下拉框数据填充
    $.getJSON(ctx + "/survey/survey_getCity", function (data) {
        $.each(data.dataList, function (i, item) {
            $('#city').append('<option  value=' + item.ORGID + '>' + item.ORGNAME + "</option>");
        });
        form.render('select');
    });
    // $.getJSON(ctx + "/survey/survey_getFault", function (data) {
    //     $.each(data.dataList, function (i, item) {
    //         $("#faultRes").append('<option  value=' + item.PID + '>' + item.PNAME + "</option>");
    //     });
    //     form.render('select');
    // });
    //客户类型下拉框数据填充
    $.getJSON(ctx + "/survey/survey_getCustType", function (data) {
        $.each(data.dataList, function (i, item) {
            $('#custType').append('<option  value=' + item.PID + '>' + item.PNAME + "</option>");
        });
        form.render('select');
    });
    //调查员下拉框数据填充
    $.getJSON(ctx + "/survey/survey_getSurveyUser", function (data) {
        $.each(data.dataList, function (i, item) {
            $('#diaocy').append('<option  value=' + item.S_USER_ID + '>' + item.S_USER_NAME + "</option>");
        });
        form.render('select');
    });
    //调查专项类型下拉框数据填充
    $.getJSON(ctx + "/survey/survey_getStype?rowv=1", function (data) {
        $.each(data.dataList, function (i, item) {
            if(i==0){
                sury_type=item.tab;
            }
            $('#stype').append('<option value=' + item.tab + '>' + item.survey_type + "</option>");
        });
        form.render('select');
        $.getJSON(ctx + "/survey/survey_getCol",{tab:sury_type}, function (data) {
            //第一个实例
            var col="[{type:'checkbox'},{field:'SF_DIS',title:'分配情况'}";
            if(data.dataList.length==0){
                layer.msg('该专项还未配置可显示数据列');
                return;
            }
            $.each(data.dataList, function (i, item) {
                col+=",{field:'"+item.COL+"',title:'"+item.COL_NAME+"'}";
            });
            col+="]";
            nowOptions={
                elem: '#tab1'
                ,id:'surveyList'
                , height: 'full-360'
                ,url: ctx + "/survey/survey_getInvitData"
                ,page: true //开启分页
                ,where:{
                    tab:sury_type
                }
                , done: function (res, curr, count) {
                    showAddressMap();
                }
                ,cols: [eval('(' + col + ')')]
            };
            nowTab=table.render(nowOptions);
        });
        layer.closeAll('loading');
    });
    //将表格中客户地址标记在百度地图上
    function showAddressMap(){
        var allData = table.cache.surveyList;
        var dzArray = new Array()
        $.each(allData, function (p1, p2) {
            var dzObj=new Object();
            dzObj.lat=p2.LAT;
            dzObj.lng=p2.LNG;
            if (typeof(p2.YJRDZ) != "undefined") {
                dzObj.yddz = p2.YJRDZ;
            } else if (typeof(p2.YDDZ) != "undefined") {
                dzObj.yddz = p2.YDDZ;
            }
            dzArray[p1]=dzObj;
        });
        bdGEO(dzArray);
    }
    //表格行单击触发复选框点击事件
    $('.box').on('click','.layui-table-body tr',function(e){
        var evtTarget = e.target || e.srcElement;
        if (!$(evtTarget).is('i')) {
            $(this).find(".layui-form-checkbox i").trigger("click");
        }
    });
    //预约结果提交
    form.on('submit(distSub)', function(data){
        checkLogin();
        var checkStatus = table.checkStatus('surveyList')
            ,tabData = checkStatus.data;
        if(typeof(tabData)=='undefined'||tabData.length==0){
            layer.msg('请先勾选样本数据');
        }else{
            var distRes=data.field.distRes;
            var diaocy=data.field.diaocy;
            if(distRes==1&&diaocy==""){
                layer.msg('请选择调查员信息');
            }else{
                if(distRes==2){
                    diaocy="";
                }
                layer.load();
                var rowstr="";
                for(var o in tabData){
                    rowstr+=tabData[o].ROWVAL+",";
                }
                $.ajax({
                    url: ctx + "/survey/survey_subDist",
                    context: document.body,
                    data:{
                        tab:sury_type,
                        rowv:rowstr,
                        distRes:distRes,
                        diaocy:diaocy
                    },
                    success: function(data){
                        if(data!='ok'){
                            layer.msg(data);
                        }else{
                            layer.msg('操作成功');
                            table.reload('surveyList');
                            $('#subDistForm')[0].reset();
                            layer.closeAll('loading');
                        }
                    },
                    error:function(xhr,textStatus){
                        layer.closeAll('loading');
                        layer.msg('操作失败'+xhr.responseText);
                    }
                });
            }
        }
        return false;
    });
    form.on('submit(searchForm)', function(data){
        checkLogin();
        var stype=data.field.stype;
        var city=data.field.city;
        var custType=data.field.custType;
        if(stype!=sury_type){//更换了专项
            sury_type=stype;
            $.getJSON(ctx + "/survey/survey_getCol",{tab:sury_type}, function (data) {
                var col="[{type:'checkbox'},{field:'SF_DIS',title:'分配情况'}";
                if(data.dataList.length==0){
                    layer.msg('该专项还未配置可显示数据列');
                    return;
                }
                $.each(data.dataList, function (i, item) {
                    col+=",{field:'"+item.COL+"',title:'"+item.COL_NAME+"'}";
                });
                col+="]";
                nowOptions={
                    elem: '#tab1'
                    ,id:'surveyList'
                    //,size:'sm'
                    ,url: ctx + "/survey/survey_getInvitData"
                    ,page: true //开启分页
                    ,where:{
                        tab:sury_type,
                        city:city,
                        custType:custType
                    },
                    done: function (res, curr, count) {
                        showAddressMap();
                    }
                    ,cols: [eval('(' + col + ')')]
                };
                nowTab=table.render(nowOptions);
            });
        }else{
            table.reload('surveyList', {
                url:ctx + "/survey/survey_getInvitData"
                ,page: {
                    curr: 1 //重新从第 1 页开始
                }
                , done: function (res, curr, count) {
                    showAddressMap();
                }
                ,where: {
                    tab:sury_type,
                    city:city,
                    custType:custType
                }
            });
        }
        return false;
    });

    table.on('checkbox(surveyList)',function(obj){  //surveyList为容器lay-filter设定的值
        // 触发复选框后的回调函数
        if(obj.checked){
            //var addrIdx=obj.data.LAY_TABLE_INDEX;
            var opts = {
                width: 160,     // 信息窗口宽度
                height: 60,     // 信息窗口高度
                title: "详细信息"
            };
            var infoWindow = new BMap.InfoWindow("地址：" + obj.data.YDDZ, opts);  // 创建信息窗口对象
            map.openInfoWindow(infoWindow, new BMap.Point(obj.data.LNG, obj.data.LAT)); //开启信息窗口
            // var mark=map.getOverlays()[addrIdx];
        }
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