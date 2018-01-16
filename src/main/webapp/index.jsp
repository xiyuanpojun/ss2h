<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jsp/common.jsp" %>
    <link rel="stylesheet" href="${ctx}/support/css/index.css">
    <script src="${ctx}/support/js/index.js"></script>
    <title>请登陆</title>
</head>
<body>
<div class="base">
    <div class="form">
        <input type="hidden" id="id" value="${userId}">
        <form class="layui-form">
            <div class="layui-form-item">
                <label class="layui-form-label">账号</label>
                <div class="layui-input-block">
                    <input name="userEntity.userid" class="layui-input" type="text" placeholder="请输入账号"
                           autocomplete="off" lay-verify="required">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">密码</label>
                <div class="layui-input-block">
                    <input name="userEntity.upwd" class="layui-input" type="text" placeholder="请输入密码" autocomplete="off"
                           lay-verify="required">
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn" lay-submit lay-filter="login">立即登陆</button>
                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
