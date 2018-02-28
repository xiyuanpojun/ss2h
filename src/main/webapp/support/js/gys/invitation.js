var sury_type="";
var nowTab=null;
var nowOptions=null;
layui.use(['table','form','laydate'], function(){
    var table=layui.table;
    var form = layui.form;
    var laydate = layui.laydate;
    $=layui.jquery;//使用jQuery对象

    layer.load();
    $.getJSON(ctx + "/survey/survey_getCity", function (data) {
        $.each(data.dataList, function (i, item) {
            $('#city').append('<option  value=' + item.ORGID + '>' + item.ORGNAME + "</option>");
        });
        form.render('select');
    });
    $.getJSON(ctx + "/survey/survey_getFault", function (data) {
        $.each(data.dataList, function (i, item) {
            $("#faultRes").append('<option  value=' + item.PID + '>' + item.PNAME + "</option>");
        });
        form.render('select');
    });
    $.getJSON(ctx + "/survey/survey_getCustType", function (data) {
        $.each(data.dataList, function (i, item) {
            $('#custType').append('<option  value=' + item.PID + '>' + item.PNAME + "</option>");
        });
        form.render('select');
    });

    $.getJSON(ctx + "/survey/survey_getStype", function (data) {
        $.each(data.dataList, function (i, item) {
            $('#stype').append('<option value=' + item.tab + '>' + item.survey_type + "</option>");
        });
        form.render('select');
        layer.closeAll('loading');
    });

    //预约结果提交
    form.on('submit(invitSub)', function(data){
        checkLogin();
        var checkStatus = table.checkStatus('surveyList')
            ,tabData = checkStatus.data;
        if(typeof(tabData)=='undefined'||tabData.length==0){
            layer.msg('请先勾选样本数据');
        }else{
            var invRes=data.field.intRes;
            var faultRes=data.field.faultRes;
            if(invRes==2&&faultRes==""){
                layer.msg('请选择失败原因');
            }else{
                if(invRes==1){
                    faultRes="";
                }
                layer.load();
                var rowstr="";
                for(var o in tabData){
                    rowstr+=tabData[o].ROWVAL+",";
                }
                $.ajax({
                    url: ctx + "/survey/survey_subInvit",
                    context: document.body,
                    data:{
                        tab:sury_type,
                        rowv:rowstr,
                        invRes:invRes,
                        faultRes:faultRes
                    },
                    success: function(data){
                        if(data!='ok'){
                            layer.msg(data);
                        }else{
                            layer.msg('操作成功');
                            var allData=table.cache.surveyList;//表格当前数据
                            var loadData=[];
                            $.each(allData, function(p1, p2){
                                if(!this.LAY_CHECKED){  //如果没选中
                                    loadData.push(this);
                                }
                            });
                            var tmpurl = nowOptions.url;
                            nowOptions.url = null;
                            nowOptions.limit=loadData.length;
                            nowOptions.data = loadData;
                            table.render(nowOptions);
                            layer.closeAll('loading');
                            $('#tab-total').text(table.cache.surveyList.length);
                            $('#subInvitForm')[0].reset();
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
    $('.box').on('click','.layui-table-body tr',function(e){
        var evtTarget = e.target || e.srcElement;
        if (!$(evtTarget).is('i')) {
            $(this).find(".layui-form-checkbox i").trigger("click");
        }
    });
    //每次点搜索，得看表格是否还有未提交数据
    form.on('submit(searchForm)', function(data){
        checkLogin();
        var pgs=table.cache.surveyList;
        if(typeof(pgs)!='undefined'&&pgs.length>1){
            layer.msg('还有未提交预约结果数据！');
        }else{
            var stype=data.field.stype;
            var city=data.field.city;
            var custType=data.field.custType;
            if(stype!=sury_type){//更换了专项
                sury_type=stype;
                $.getJSON(ctx + "/survey/survey_getCol",{tab:sury_type}, function (data) {
                    var col="[{type:'checkbox'}";
                    //var col="[{field:'ROWVAL', title:'操作',templet: '#checkboxTpl'}";
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
                        ,height:'full-360'
                        //,size:'sm'
                        ,url: ctx + "/survey/survey_getData"
                        ,page: false
                        ,done:function(res, curr, count){
                            var allData=table.cache.surveyList;
                            $('#tab-total').text(allData.length);
                            var dzArray=new Array()
                            $.each(allData, function(p1, p2){
                                if(typeof(p2.YJRDZ)!="undefined"){
                                    dzArray[p1]=p2.YJRDZ;
                                }else if(typeof(p2.YDDZ)!="undefined"){
                                    dzArray[p1]=p2.YDDZ;
                                }
                            });
                            bdGEO(dzArray);
                        }
                        ,where:{
                            tab:sury_type,
                            city:city,
                            custType:custType
                        }
                        ,cols: [eval('(' + col + ')')]
                    };
                    nowTab=table.render(nowOptions);
                });
            }else{
                table.reload('surveyList', {
                    url: ctx + "/survey/survey_getData"
                    ,where: {
                        tab:sury_type,
                        city:city,
                        custType:custType
                    }
                    ,done:function(res, curr, count){
                        $('#tab-total').text(table.cache.surveyList.length);
                    }
                });
            }
        }
        return false;
    });

// 百度地图API功能
    var map = new BMap.Map("map");
//以当前城市为中心打开地图
    map.centerAndZoom("北京",12);
    map.enableScrollWheelZoom();
    var index = 0;
// 创建地址解析器实例
    var myGeo = new BMap.Geocoder();
    function bdGEO(adds){
        for(var i=0;i<adds.length;i++){
            geocodeSearch(adds[i],i+1);
        }
    }
    function geocodeSearch(add,idx){
        myGeo.getPoint(add, function(point){
            if (point) {
                //document.getElementById("result").innerHTML +=  idx+"、" +add +"</br>";
                var address = new BMap.Point(point.lng, point.lat);
                if(idx==1){
                    map.panTo(new BMap.Point(point.lng, point.lat));
                }
                addMarker(address,new BMap.Label(idx),add);
            }
        }, "全国");
    }
// 编写自定义函数,创建标注
    function addMarker(point,label,addr){
        var marker = new BMap.Marker(point);
        map.addOverlay(marker);
        marker.setLabel(label);
        var opts = {
            width : 150,     // 信息窗口宽度
            height: 60,     // 信息窗口高度
            title : "详细信息"
        }
        var infoWindow = new BMap.InfoWindow("地址："+addr, opts);  // 创建信息窗口对象
        marker.addEventListener("click", function(){
            map.openInfoWindow(infoWindow,point); //开启信息窗口
        });
    }
});