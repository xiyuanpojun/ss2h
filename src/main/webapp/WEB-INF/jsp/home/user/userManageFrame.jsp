<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <%@include file="/WEB-INF/jsp/common.jsp" %>
    <link rel="stylesheet" href="${ctx}/support/css/userManageFrame.css">
    <script src="${ctx}/support/js/userManageFrame.js"></script>
    <title>管理中心</title>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo"><img alt="hill" src="${ctx}/support/img/hill2.png" width="60" height="41.2" style="margin-top: 6px"></div>
        <ul class="layui-nav layui-layout-left"></ul>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <img src="${ctx}/support/img/user.png" class="layui-nav-img">${userId}
                </a>
            </li>
            <li class="layui-nav-item"><a alt="${userId}" id="logon" href="#">退出系统</a>
            </li>
        </ul>
    </div>
    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <ul class="layui-nav layui-nav-tree" id="urls">
                <c:forEach items="#{func}" var="item">
                    <li class="layui-nav-item"><a href="${ctx}${item.fUrl}" target="show_page">${item.fName}</a></li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <div class="layui-body">
        <%--src="${ctx}/user/user_list_page"--%>
        <iframe class="show_page" id="show_page" name="show_page" frameborder="0"></iframe>
    </div>
    <div class="layui-footer">
        © layui.com
    </div>
</div>
</body>
</html>