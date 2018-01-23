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
    $.getJSON(ctx + "/survey/survey_getSurveyUser", function (data) {
        $.each(data.dataList, function (i, item) {
            $('#diaocy').append('<option  value=' + item.S_USER_ID + '>' + item.S_USER_NAME + "</option>");
        });
        form.render('select');
    });

    $.getJSON(ctx + "/survey/survey_getStype", function (data) {
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
                ,url: ctx + "/survey/survey_getInvitData"
                ,page: true //开启分页
                ,where:{
                    tab:sury_type
                }
                ,cols: [eval('(' + col + ')')]
            };
            nowTab=table.render(nowOptions);
        });
        layer.closeAll('loading');
    });
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
                ,where: {
                    tab:sury_type,
                    city:city,
                    custType:custType
                }
            });
        }
        return false;
    });
});