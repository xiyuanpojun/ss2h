<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Pragma" content="no-cache">
    <%@include file="/WEB-INF/jsp/common.jsp" %>
    <script src="${ctx}/support/js/checkFrame.js"></script>
</head>
<body>
<form class="layui-form" action="">
    专项名称:
    <div class="layui-inline">
        <select name="ZX" id="ZX">
            <option value="1">供电质量</option>
            <option value="2">营业厅服务</option>
            <option value="3">抄表交费</option>
            <option value="4">投诉举报</option>
            <option value="5">故障报修</option>
            <option value="6">业扩报装-低压</option>
            <option value="7">业扩报装-高压</option>
            <option value="100">电话调查结果</option>
        </select>
    </div>
    <div class="layui-inline">
        <button type="button" class="layui-btn" id="chooseFile">
            <i class="layui-icon">&#xe67c;</i>选择文件
        </button>
    </div>
</form>
<script>
    layui.use(['upload', 'form', 'layer'], function () {
        var upload = layui.upload;
        var layer = layui.layer;
        var form = layui.form;
        $ = layui.jquery;//使用jQuery对象
        /*  $.getJSON("getProjects", function (data) {
            $.each(data.dataList, function (i, item) {
                $('#pro_no').append('<option  value=' + item.PRO_NO + '>' + item.PRO_NAME + "</option>");
            });
            form.render('select');
         }); */
        //执行实例
        var uploadInst = upload.render({
            elem: '#chooseFile' //绑定元素
            , url: ctx + '/user/gwUser'//上传接口
            , before: function (obj) { //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
                var sName = $('#ZX').val();
                console.log(sName);
                uploadInst.config.data.survyName = sName;
                layer.load(); //上传loading
                checkLogin();
            }
            , done: function (res, index, upload) { //上传后的回调
                layer.closeAll('loading'); //关闭loading
                if (res) {
                    layer.msg("成功啦");
                } else {
                    layer.msg("失败啦:" + res);
                }
            }
            , exts: 'xls|xlsx'
            , accept: 'file' //允许上传的文件类型
            , error: function (res) {
                //请求异常回调
                layer.msg("失败啦:" + res);
                layer.closeAll('loading'); //关闭loading
            }
        });

    });
</script>
</body>
</html>
